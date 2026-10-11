package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BidEntity
import com.example.data.ListingEntity
import com.example.data.UserEntity
import com.example.ui.theme.BuyerAmber
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerLightBg
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.BuyerRed
import com.example.ui.theme.BuyerSlate
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerLightBg
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerSlateGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Interactive Live Bid Tracker:
 * Allows buyers to track their active bids, outbid alerts, win rates,
 * and execute 1-tap counter-bids (+₹0.50 / +₹1.00) in real-time.
 */
@Composable
fun LiveBidTrackerView(
    buyer: UserEntity,
    userBids: List<BidEntity>,
    allListings: List<ListingEntity>,
    recentActivityBids: List<BidEntity> = emptyList(),
    onOpenListing: (Long) -> Unit,
    onQuickRaiseBid: (listingId: Long, increment: Double) -> Unit,
    onSimulateRivalBid: (listingId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var trackerFilter by remember { mutableStateOf("all") } // all, leading, outbid, market
    var searchQuery by remember { mutableStateOf("") }
    var showLiveFeed by remember { mutableStateOf(false) }

    // Aggregate user bid data by listing
    val userBidsByListing = remember(userBids) {
        userBids.groupBy { it.listingId }
    }

    // Identify listings where the user placed bids
    val myBidListings = remember(userBidsByListing, allListings) {
        allListings.filter { listing ->
            userBidsByListing.containsKey(listing.id)
        }
    }

    // Metrics calculations
    val nowSeconds = System.currentTimeMillis() / 1000
    val activeBidListings = myBidListings.filter { it.status == "open" && it.endsAt > nowSeconds }
    val leadingListings = activeBidListings.filter { it.topBidderId == buyer.id }
    val outbidListings = activeBidListings.filter { it.topBidderId != null && it.topBidderId != buyer.id }

    val totalCommittedFunds = activeBidListings.sumOf { listing ->
        val myHighestBid = userBidsByListing[listing.id]?.maxByOrNull { it.pricePerKg }?.pricePerKg
            ?: (listing.topPrice ?: listing.startPrice)
        myHighestBid * listing.quantityKg
    }

    val displayListings = when (trackerFilter) {
        "leading" -> leadingListings
        "outbid" -> outbidListings
        "market" -> allListings.filter { it.status == "open" }
        else -> if (myBidListings.isNotEmpty()) myBidListings else allListings.filter { it.status == "open" }
    }.filter { listing ->
        if (searchQuery.trim().isEmpty()) true else {
            listing.produce.contains(searchQuery, ignoreCase = true) ||
            listing.farmerName.contains(searchQuery, ignoreCase = true) ||
            listing.location.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BuyerLightBg)
            .testTag("bid_tracker_container")
    ) {
        // Sticky Header / Summary Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Bid Tracker",
                            tint = BuyerNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Live Bid Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BuyerNavy
                        )
                        Text(
                            text = "Real-time lot tracking, outbid alerts & 1-tap counter-bids",
                            fontSize = 11.sp,
                            color = BuyerSlate
                        )
                    }
                }

                Surface(
                    color = if (outbidListings.isNotEmpty()) Color(0xFFFEE2E2) else Color(0xFFD1FAE5),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (outbidListings.isNotEmpty()) "⚠️ ${outbidListings.size} Outbid" else "🟢 All In Lead",
                            color = if (outbidListings.isNotEmpty()) Color(0xFF991B1B) else Color(0xFF065F46),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 KPI Metric Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrackerMetricCard(
                    title = "Tracked",
                    value = "${myBidListings.size}",
                    subtitle = "Lots",
                    color = BuyerNavy,
                    modifier = Modifier.weight(1f)
                )
                TrackerMetricCard(
                    title = "Winning",
                    value = "${leadingListings.size}",
                    subtitle = "In Lead",
                    color = Color(0xFF047857),
                    modifier = Modifier.weight(1f)
                )
                TrackerMetricCard(
                    title = "Outbid",
                    value = "${outbidListings.size}",
                    subtitle = "Need Action",
                    color = if (outbidListings.isNotEmpty()) Color(0xFFB91C1C) else BuyerSlate,
                    modifier = Modifier.weight(1f)
                )
                TrackerMetricCard(
                    title = "Committed",
                    value = formatMoney(totalCommittedFunds).replace(".00", ""),
                    subtitle = "Max Exposure",
                    color = BuyerBlue,
                    modifier = Modifier.weight(1.3f)
                )
            }

            // Urgent Outbid Banner if any lots are outbid
            if (outbidListings.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "You have been outbid on ${outbidListings.size} active auction lot${if (outbidListings.size > 1) "s" else ""}! Tap '+₹0.50' below to counter.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF991B1B)
                            )
                        }

                        TextButton(
                            onClick = { trackerFilter = "outbid" },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("View Outbid", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips & Search
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter tracked lots...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("tracker_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BuyerNavy,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                OutlinedButton(
                    onClick = { showLiveFeed = !showLiveFeed },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(44.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (showLiveFeed) BuyerNavy else BuyerSlate
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (showLiveFeed) BuyerAmber else BuyerSlate
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showLiveFeed) "Feed On" else "Live Feed", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = trackerFilter == "all",
                        onClick = { trackerFilter = "all" },
                        label = { Text("All My Bids (${myBidListings.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = trackerFilter == "leading",
                        onClick = { trackerFilter = "leading" },
                        label = { Text("👑 Winning (${leadingListings.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF047857),
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = trackerFilter == "outbid",
                        onClick = { trackerFilter = "outbid" },
                        label = { Text("⚠️ Outbid (${outbidListings.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFDC2626),
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = trackerFilter == "market",
                        onClick = { trackerFilter = "market" },
                        label = { Text("🌐 All Live Market Lots (${allListings.count { it.status == "open" }})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Live Activity Ticker (if toggled on)
        AnimatedVisibility(visible = showLiveFeed) {
            Surface(
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22C55E))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Market Bidding Pulse (Real-Time Activity)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${recentActivityBids.size} recent bids",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (recentActivityBids.isEmpty()) {
                        Text(
                            text = "No recent bids recorded yet in this session.",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(recentActivityBids.take(6)) { bid ->
                                Surface(
                                    color = Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                                ) {
                                    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                                        Text(
                                            text = "${bid.buyerName}: ${formatMoney(bid.pricePerKg)}/kg",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "Lot #${bid.listingId} • ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(bid.createdAt * 1000))}",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // List of Tracked Auction Cards
        if (displayListings.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "📊", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (trackerFilter == "outbid") "No Outbid Lots!" else if (trackerFilter == "leading") "No Winning Lots Yet" else "No Bids Tracked",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )
                Text(
                    text = if (trackerFilter == "outbid")
                        "You hold the highest bid on all your active auctions! Great job."
                    else
                        "Explore the marketplace and place your bids. They will appear here with live updates, outbid alerts, and 1-tap counter-bids.",
                    color = BuyerSlate,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                if (trackerFilter != "market") {
                    Button(
                        onClick = { trackerFilter = "market" },
                        colors = ButtonDefaults.buttonColors(containerColor = BuyerNavy),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Browse Live Marketplace Lots")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayListings, key = { it.id }) { listing ->
                    val myBidsOnLot = userBidsByListing[listing.id] ?: emptyList()
                    val myHighestBid = myBidsOnLot.maxByOrNull { it.pricePerKg }

                    BidTrackerItemCard(
                        listing = listing,
                        buyer = buyer,
                        myHighestBid = myHighestBid,
                        onOpen = { onOpenListing(listing.id) },
                        onQuickRaise = { inc -> onQuickRaiseBid(listing.id, inc) },
                        onSimulateOutbid = { onSimulateRivalBid(listing.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackerMetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = BuyerSlate, fontWeight = FontWeight.Medium)
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = subtitle, fontSize = 9.sp, color = BuyerSlate)
        }
    }
}

/**
 * Individual Auction Lot Card inside the Bid Tracker:
 * Displays real-time timer, current position, trailing delta, and 1-tap counter-bid triggers.
 */
@Composable
fun BidTrackerItemCard(
    listing: ListingEntity,
    buyer: UserEntity,
    myHighestBid: BidEntity?,
    onOpen: () -> Unit,
    onQuickRaise: (increment: Double) -> Unit,
    onSimulateOutbid: () -> Unit
) {
    val isLeading = listing.topBidderId == buyer.id
    val hasBid = myHighestBid != null
    val isOutbid = hasBid && !isLeading && listing.topBidderId != null
    val currentPrice = listing.topPrice ?: listing.startPrice
    val nowSeconds = System.currentTimeMillis() / 1000
    val isOpen = listing.status == "open" && listing.endsAt > nowSeconds
    val reserveMet = listing.topPrice != null && listing.topPrice >= listing.reservePrice

    val cardBorderColor = when {
        isOutbid -> Color(0xFFEF4444)
        isLeading -> Color(0xFF10B981)
        else -> Color(0xFFE2E8F0)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, cardBorderColor, RoundedCornerShape(16.dp))
            .clickable { onOpen() }
            .testTag("bid_tracker_item_${listing.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Produce, Grade, Timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(text = produceEmoji(listing.produce), fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = listing.produce,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = BuyerNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = FarmerContainerLight,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Grade ${listing.grade}",
                                    color = FarmerDarkGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "${listing.farmerName} • ${listing.location}",
                            fontSize = 11.sp,
                            color = BuyerSlate
                        )
                    }
                }

                // Live Countdown Timer
                CompactAuctionCountdownTimer(
                    endsAtSeconds = listing.endsAt
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Banner: Winning vs Outbid vs Live Watching
            Surface(
                color = when {
                    isLeading -> Color(0xFFECFDF5)
                    isOutbid -> Color(0xFFFEF2F2)
                    else -> Color(0xFFF0FDF4)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when {
                                isLeading -> "👑"
                                isOutbid -> "⚠️"
                                else -> "🌾"
                            },
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when {
                                isLeading -> "YOU HOLD WINNING BID (Lead position)"
                                isOutbid -> "OUTBID! Trailing by ₹${"%.2f".format((listing.topPrice ?: 0.0) - (myHighestBid?.pricePerKg ?: 0.0))}/kg"
                                else -> "Watching Auction • Starting at ${formatMoney(listing.startPrice)}/kg"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isLeading -> Color(0xFF065F46)
                                isOutbid -> Color(0xFFB91C1C)
                                else -> FarmerDarkGreen
                            }
                        )
                    }

                    if (reserveMet) {
                        Surface(
                            color = Color(0xFFD1FAE5),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Reserve Met",
                                color = Color(0xFF065F46),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing & Commitment Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Current Top Bid", fontSize = 10.sp, color = BuyerSlate)
                    Text(
                        text = "${formatMoney(currentPrice)}/kg",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isLeading) Color(0xFF047857) else BuyerNavy
                    )
                    Text(
                        text = "Held by ${if (isLeading) "You" else (listing.topBidderName ?: "Nobody")}",
                        fontSize = 10.sp,
                        color = BuyerSlate
                    )
                }

                if (hasBid) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Your High Bid", fontSize = 10.sp, color = BuyerSlate)
                        Text(
                            text = "${formatMoney(myHighestBid!!.pricePerKg)}/kg",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isLeading) Color(0xFF047857) else Color(0xFFDC2626)
                        )
                        Text(
                            text = "${listing.bidCount} total bids",
                            fontSize = 10.sp,
                            color = BuyerSlate
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Lot Commitment", fontSize = 10.sp, color = BuyerSlate)
                    Text(
                        text = formatMoney(currentPrice * listing.quantityKg),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BuyerBlue
                    )
                    Text(
                        text = "For ${listing.quantityKg.roundToInt()} kg harvest",
                        fontSize = 10.sp,
                        color = BuyerSlate
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1-Tap Quick Action Buttons (Counter-Bid)
            if (isOpen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isOutbid || !hasBid) {
                        // Quick 1-Tap Counter (+₹0.50)
                        Button(
                            onClick = { onQuickRaise(0.50) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isOutbid) Color(0xFFDC2626) else BuyerNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("tracker_quick_counter_${listing.id}")
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isOutbid) "+₹0.50 Counter" else "Bid ${formatMoney((listing.topPrice ?: listing.startPrice) + 0.50)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Aggressive Jump (+₹1.00)
                        OutlinedButton(
                            onClick = { onQuickRaise(1.00) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(38.dp)
                                .testTag("tracker_raise_100_${listing.id}")
                        ) {
                            Text("+₹1.00 Jump", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BuyerNavy)
                        }
                    } else if (isLeading) {
                        // Demo Tool: Simulate Rival Bid to trigger Outbid alert
                        OutlinedButton(
                            onClick = onSimulateOutbid,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("tracker_simulate_rival_${listing.id}")
                        ) {
                            Text("🎭 Simulate Rival Outbid", fontSize = 11.sp, color = BuyerAmber, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // View Details Button
                    OutlinedButton(
                        onClick = onOpen,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("tracker_view_detail_${listing.id}")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "View", modifier = Modifier.size(14.dp))
                    }
                }
            } else {
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Auction Ended • ${if (isLeading) "🏆 You won this lot!" else "Closed"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLeading) Color(0xFF047857) else BuyerSlate
                        )

                        TextButton(onClick = onOpen, contentPadding = PaddingValues(0.dp)) {
                            Text("View Lot Details →", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Farmer Lot Bid Tracker:
 * Allows farmers to monitor incoming bids across their produce lots in real time.
 */
@Composable
fun FarmerLotBidTrackerView(
    farmer: UserEntity,
    listings: List<ListingEntity>,
    onOpenListing: (Long) -> Unit,
    onEndAuctionEarly: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeListings = listings.filter { it.status == "open" }
    val totalBidsReceived = listings.sumOf { it.bidCount }
    val highestLotValue = activeListings.maxOfOrNull { (it.topPrice ?: it.startPrice) * it.quantityKg } ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FarmerLightBg)
            .testTag("farmer_bid_tracker")
    ) {
        // Summary Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FarmerContainerLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Farmer Bid Tracker",
                            tint = FarmerDarkGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Harvest Bid Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarmerDarkGreen
                        )
                        Text(
                            text = "Real-time incoming buyer bids on your crops",
                            fontSize = 11.sp,
                            color = FarmerSlateGray
                        )
                    }
                }

                Surface(
                    color = FarmerContainerLight,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🌾 ${activeListings.size} Live Lots",
                        color = FarmerDarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrackerMetricCard(
                    title = "Active Lots",
                    value = "${activeListings.size}",
                    subtitle = "Live on Mandi",
                    color = FarmerDarkGreen,
                    modifier = Modifier.weight(1f)
                )
                TrackerMetricCard(
                    title = "Total Bids",
                    value = "$totalBidsReceived",
                    subtitle = "Incoming Bids",
                    color = FarmerPineGreen,
                    modifier = Modifier.weight(1f)
                )
                TrackerMetricCard(
                    title = "Peak Lot Value",
                    value = formatMoney(highestLotValue).replace(".00", ""),
                    subtitle = "Highest Batch",
                    color = FarmerLeafGreen,
                    modifier = Modifier.weight(1.3f)
                )
            }
        }

        if (activeListings.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🌱", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No Active Auctions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarmerDarkGreen
                )
                Text(
                    text = "Publish a new harvest lot under 'New Lot' to start receiving live bids from commercial buyers.",
                    fontSize = 13.sp,
                    color = FarmerSlateGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(activeListings, key = { it.id }) { lot ->
                    val reserveMet = lot.topPrice != null && lot.topPrice >= lot.reservePrice
                    val gross = (lot.topPrice ?: lot.startPrice) * lot.quantityKg

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenListing(lot.id) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = produceEmoji(lot.produce), fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = lot.produce,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = FarmerDarkGreen
                                        )
                                        Text(
                                            text = "${lot.quantityKg.roundToInt()} kg  •  Reserve: ${formatMoney(lot.reservePrice)}/kg",
                                            fontSize = 11.sp,
                                            color = FarmerSlateGray
                                        )
                                    }
                                }

                                CompactAuctionCountdownTimer(endsAtSeconds = lot.endsAt)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(FarmerContainerLight, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Current Top Bid", fontSize = 10.sp, color = FarmerSlateGray)
                                    Text(
                                        text = if (lot.topPrice != null) "${formatMoney(lot.topPrice)}/kg" else "No bids yet",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (lot.topPrice != null) FarmerLeafGreen else FarmerSlateGray
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Status", fontSize = 10.sp, color = FarmerSlateGray)
                                    Text(
                                        text = if (reserveMet) "✅ Reserve Met" else "⏳ Below Reserve",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (reserveMet) FarmerLeafGreen else FarmerEarthRed
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Gross Lot Value", fontSize = 10.sp, color = FarmerSlateGray)
                                    Text(
                                        text = formatMoney(gross),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = FarmerDarkGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Top Bidder: ${lot.topBidderName ?: "Waiting for first bidder..."}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = FarmerSlateGray
                                )

                                OutlinedButton(
                                    onClick = { onOpenListing(lot.id) },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Open Auction →", fontSize = 11.sp, color = FarmerDarkGreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
