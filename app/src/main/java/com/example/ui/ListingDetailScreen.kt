package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BidEntity
import com.example.data.ListingEntity
import com.example.data.UserEntity
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerLightBg
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.BuyerSlate
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerLightBg
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerRedContainer
import com.example.ui.theme.FarmerSlateGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listing: ListingEntity?,
    bids: List<BidEntity>,
    currentUser: UserEntity?,
    onBack: () -> Unit,
    onPlaceBid: (listingId: Long, price: Double) -> Unit,
    onSimulateRivalBid: (listingId: Long) -> Unit,
    onEndAuctionEarly: (Long) -> Unit,
    onConfirmSale: (Long) -> Unit,
    onDeclineSale: (Long) -> Unit
) {
    BackHandler { onBack() }

    if (listing == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading harvest details...")
        }
        return
    }

    val isFarmerRole = currentUser?.role == "farmer"
    val isOwner = currentUser?.id == listing.farmerId
    val isOpen = listing.status == "open"
    val isLeading = currentUser?.id == listing.topBidderId
    val myHighestBid = bids.filter { it.buyerId == currentUser?.id }.maxByOrNull { it.pricePerKg }
    val isOutbid = myHighestBid != null && !isLeading && listing.topBidderId != null
    val minNextBid = if (listing.topPrice != null) listing.topPrice + 0.50 else listing.startPrice
    var bidInputText by remember(minNextBid) { mutableStateOf(String.format(Locale.US, "%.2f", minNextBid)) }

    val themeColor = if (isFarmerRole) FarmerDarkGreen else BuyerNavy
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listing.produce, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(if (isFarmerRole) FarmerLightBg else BuyerLightBg)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .testTag("listing_detail_screen")
        ) {
            // Hero card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            EmojiProduceBox(
                                emoji = produceEmoji(listing.produce),
                                modifier = Modifier.size(64.dp),
                                background = if (isFarmerRole) FarmerContainerLight else Color(0xFFEFF6FF)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = listing.produce,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor
                                )
                                Text(
                                    text = "Grade ${listing.grade} • ${listing.quantityKg.roundToInt()} kg",
                                    fontWeight = FontWeight.SemiBold,
                                    color = FarmerSlateGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        StatusBadge(status = listing.status)
                    }

                    if (listing.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = listing.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF374151)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "👨‍🌾", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = listing.farmerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = FarmerDarkGreen
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(isVerified = listing.farmerVerified)
                                }
                                Text(
                                    text = "📍 ${listing.location} • 🗓️ Harvested ${listing.harvestDate}",
                                    fontSize = 11.sp,
                                    color = FarmerSlateGray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-Time Countdown Timer Component for Live Bidding
            LiveAuctionHeroCountdownTimer(
                endsAtSeconds = listing.endsAt,
                createdAtSeconds = listing.createdAt,
                onTimerExpired = {
                    // Handled automatically when auction timer reaches 0
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Quantity Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Lot Quantity", fontSize = 11.sp, color = FarmerSlateGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${listing.quantityKg.roundToInt()} kg", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = themeColor)
                    }
                }

                // Top Bid Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Highest Bid", fontSize = 11.sp, color = FarmerSlateGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (listing.topPrice != null) formatMoney(listing.topPrice) + "/kg" else "No bids",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = themeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Total value
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Lot Value", fontSize = 11.sp, color = FarmerSlateGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        val currentVal = (listing.topPrice ?: listing.startPrice) * listing.quantityKg
                        Text(formatMoney(currentVal), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = themeColor)
                    }
                }

                // Time remaining / status
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(if (isOpen) "Time Remaining" else "Status", fontSize = 11.sp, color = FarmerSlateGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isOpen) formatTimeRemaining(listing.endsAt) else listing.status.uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isOpen) FarmerLeafGreen else FarmerEarthRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Role-specific sections
            if (isFarmerRole && isOwner) {
                // Farmer private info card
                val gross = (listing.topPrice ?: listing.reservePrice) * listing.quantityKg
                val net = gross - (gross * 0.02) - (listing.quantityKg * 1.5)
                val reserveMet = listing.topPrice != null && listing.topPrice >= listing.reservePrice

                Card(
                    colors = CardDefaults.cardColors(containerColor = FarmerContainerLight),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🔒 Confidential Grower Reserve Details",
                            fontWeight = FontWeight.Bold,
                            color = FarmerDarkGreen,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your Reserve Price: ${formatMoney(listing.reservePrice)}/kg  •  Status: ${if (reserveMet) "✅ Reserve Met!" else "Reserve Not Met Yet"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (reserveMet) FarmerLeafGreen else FarmerEarthRed
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estimated Net Payout at current bid: ${formatMoney(net)} (Gross ${formatMoney(gross)} − 2% fee − ₹1.5/kg transport)",
                            fontSize = 12.sp,
                            color = FarmerPineGreen
                        )

                        if (isOpen) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = { onEndAuctionEarly(listing.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmerEarthRed),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("end_auction_early_btn")
                            ) {
                                Text("End Auction Now")
                            }
                        } else if (listing.status == "pending_confirmation") {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onConfirmSale(listing.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FarmerDarkGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("detail_confirm_sale_btn")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Confirm Sale")
                                }

                                OutlinedButton(
                                    onClick = { onDeclineSale(listing.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmerEarthRed),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("detail_decline_sale_btn")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Decline")
                                }
                            }
                        }
                    }
                }
            } else if (!isFarmerRole && isOpen) {
                // Buyer Bidding Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Place Whole-Lot Bid",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = BuyerNavy
                        )
                        Text(
                            text = "Bid is per kilogram for the complete ${listing.quantityKg.roundToInt()} kg batch.",
                            fontSize = 12.sp,
                            color = BuyerSlate,
                            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                        )

                        if (isLeading) {
                            Surface(
                                color = Color(0xFFD1FAE5),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("👑", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "You are currently the highest bidder at ${formatMoney(listing.topPrice ?: 0.0)}/kg!",
                                        color = Color(0xFF065F46),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else if (isOutbid) {
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("⚠️", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "OUTBID ALERT: You have been outbid by ${listing.topBidderName ?: "rival"}!",
                                            color = Color(0xFF991B1B),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "Your previous bid was ${formatMoney(myHighestBid?.pricePerKg ?: 0.0)}/kg. Minimum next bid is ${formatMoney(minNextBid)}/kg.",
                                            color = Color(0xFFB91C1C),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Quick Increment Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val target = minNextBid
                                    bidInputText = String.format(Locale.US, "%.2f", target)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("Min (${formatMoney(minNextBid)})", fontSize = 11.sp, maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = {
                                    val target = minNextBid + 0.50
                                    bidInputText = String.format(Locale.US, "%.2f", target)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("+₹0.50", fontSize = 11.sp, maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = {
                                    val target = minNextBid + 1.00
                                    bidInputText = String.format(Locale.US, "%.2f", target)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("+₹1.00 Jump", fontSize = 11.sp, maxLines = 1)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = bidInputText,
                                onValueChange = { bidInputText = it },
                                label = { Text("Your Bid (₹/kg)") },
                                placeholder = { Text("Min ${formatMoney(minNextBid)}") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("buyer_bid_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BuyerNavy,
                                    focusedLabelColor = BuyerNavy
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    val price = bidInputText.toDoubleOrNull()
                                    if (price != null) {
                                        onPlaceBid(listing.id, price)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BuyerNavy),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(54.dp).testTag("buyer_submit_bid_btn")
                            ) {
                                Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Place Bid", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val previewPrice = bidInputText.toDoubleOrNull() ?: 0.0
                        if (previewPrice > 0) {
                            Text(
                                text = "Total Lot Commitment: ${formatMoney(previewPrice * listing.quantityKg)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BuyerBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Demo Simulation Tool
                        OutlinedButton(
                            onClick = { onSimulateRivalBid(listing.id) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("simulate_rival_bid_btn")
                        ) {
                            Text("🎭 Demo: Simulate Higher Rival Bid (+₹0.50 ~ ₹2.00)", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bid History
            Text(
                text = "Live Bid History (${bids.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (bids.isEmpty()) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No bids placed yet. Be the first to start the auction!", color = FarmerSlateGray, fontSize = 13.sp)
                    }
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        bids.forEachIndexed { index, bid ->
                            val isMe = currentUser?.id == bid.buyerId
                            val timeStr = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(Date(bid.createdAt * 1000))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "#${index + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FarmerSlateGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isMe) "You (${bid.buyerName})" else bid.buyerName,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = if (isMe) BuyerBlue else Color(0xFF1E293B)
                                        )
                                        Text(timeStr, fontSize = 10.sp, color = FarmerSlateGray)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${formatMoney(bid.pricePerKg)}/kg",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (index == 0) FarmerLeafGreen else Color(0xFF1E293B)
                                    )
                                    Text(
                                        text = "Lot: ${formatMoney(bid.pricePerKg * listing.quantityKg)}",
                                        fontSize = 11.sp,
                                        color = FarmerSlateGray
                                    )
                                }
                            }

                            if (index < bids.size - 1) {
                                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color(0xFFF1F5F9)))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
