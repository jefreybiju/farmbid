package com.example.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BidEntity
import com.example.data.ListingEntity
import com.example.data.OrderEntity
import com.example.data.UserEntity
import com.example.ui.theme.BuyerAmber
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerLightBg
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.BuyerRed
import com.example.ui.theme.BuyerSlate
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerHomeScreen(
    buyer: UserEntity,
    currentTab: String,
    openListings: List<ListingEntity>,
    userBids: List<BidEntity>,
    orders: List<OrderEntity>,
    filteredFarmers: List<UserEntity>,
    farmerSearchQuery: String,
    onFarmerSearchQueryChange: (String) -> Unit,
    selectedCertification: String,
    onCertificationChange: (String) -> Unit,
    selectedProduce: String,
    onProduceChange: (String) -> Unit,
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    onOpenListing: (Long) -> Unit,
    onQuickRaiseBid: (Long, Double) -> Unit = { _, _ -> },
    onSimulateRivalBid: (Long) -> Unit = {},
    recentActivityBids: List<BidEntity> = emptyList(),
    allBids: List<BidEntity> = emptyList(),
    onDeliverOrder: (Long) -> Unit,
    onDisputeOrder: (Long, String) -> Unit,
    onLogout: () -> Unit,
    onResetDemo: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🛒", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = buyer.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                VerifiedBadge(isVerified = buyer.isVerified)
                            }
                            Text(
                                text = "Buyer Portal • ${buyer.businessType}",
                                color = Color(0xFFBFDBFE),
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onResetDemo,
                        modifier = Modifier.testTag("buyer_top_reset_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset Demo", tint = Color.White)
                    }
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("buyer_top_logout_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Switch Portal / Log Out", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BuyerNavy,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == "market",
                    onClick = { onTabSelected("market") },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Marketplace") },
                    label = { Text("Market") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BuyerNavy,
                        selectedTextColor = BuyerNavy,
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "directory",
                    onClick = { onTabSelected("directory") },
                    icon = { Icon(Icons.Default.FilterList, contentDescription = "Farmers Directory") },
                    label = { Text("Farmers") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BuyerNavy,
                        selectedTextColor = BuyerNavy,
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "bids",
                    onClick = { onTabSelected("bids") },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Bid Tracker") },
                    label = { Text("Bid Tracker") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BuyerNavy,
                        selectedTextColor = BuyerNavy,
                        indicatorColor = Color(0xFFDBEAFE)
                    ),
                    modifier = Modifier.testTag("nav_bid_tracker")
                )

                NavigationBarItem(
                    selected = currentTab == "orders",
                    onClick = { onTabSelected("orders") },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BuyerNavy,
                        selectedTextColor = BuyerNavy,
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "how",
                    onClick = { onTabSelected("how") },
                    icon = { Icon(Icons.Default.HelpOutline, contentDescription = "How It Works") },
                    label = { Text("Help") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BuyerNavy,
                        selectedTextColor = BuyerNavy,
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BuyerLightBg)
        ) {
            when (currentTab) {
                "market" -> BuyerMarketplaceView(
                    buyer = buyer,
                    listings = openListings,
                    userBids = userBids,
                    onOpen = onOpenListing,
                    onNavigateToBidTracker = { onTabSelected("bids") }
                )
                "directory" -> FarmerDirectoryView(
                    farmers = filteredFarmers,
                    allListings = openListings,
                    searchQuery = farmerSearchQuery,
                    onSearchQueryChange = onFarmerSearchQueryChange,
                    selectedCertification = selectedCertification,
                    onCertificationChange = onCertificationChange,
                    selectedProduce = selectedProduce,
                    onProduceChange = onProduceChange,
                    selectedLocation = selectedLocation,
                    onLocationChange = onLocationChange,
                    onOpenListing = onOpenListing
                )
                "bids" -> LiveBidTrackerView(
                    buyer = buyer,
                    userBids = userBids,
                    allListings = openListings,
                    allBids = allBids,
                    recentActivityBids = recentActivityBids,
                    onOpenListing = onOpenListing,
                    onQuickRaiseBid = onQuickRaiseBid,
                    onSimulateRivalBid = onSimulateRivalBid
                )
                "orders" -> BuyerOrdersView(
                    orders = orders,
                    onDeliver = onDeliverOrder,
                    onDispute = onDisputeOrder
                )
                "how" -> BuyerHowItWorksView()
            }
        }
    }
}

@Composable
fun BuyerMarketplaceView(
    buyer: UserEntity,
    listings: List<ListingEntity>,
    userBids: List<BidEntity> = emptyList(),
    onOpen: (Long) -> Unit,
    onNavigateToBidTracker: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf("ending") } // ending, price, qty
    var selectedFilter by remember { mutableStateOf("all") } // all, gradeA, veg, fruit

    val filteredListings = listings.filter { l ->
        val matchesSearch = l.produce.contains(searchQuery, ignoreCase = true) ||
                l.location.contains(searchQuery, ignoreCase = true) ||
                l.farmerName.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "gradeA" -> l.grade == "A"
            "veg" -> listOf("tomato", "okra", "chilli", "potato", "onion").any { l.produce.lowercase().contains(it) }
            "fruit" -> listOf("banana", "pineapple", "mango", "apple").any { l.produce.lowercase().contains(it) }
            else -> true
        }

        matchesSearch && matchesFilter
    }.sortedWith { a, b ->
        when (selectedSort) {
            "price" -> (b.topPrice ?: b.startPrice).compareTo(a.topPrice ?: a.startPrice)
            "qty" -> b.quantityKg.compareTo(a.quantityKg)
            else -> a.endsAt.compareTo(b.endsAt)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (userBids.isNotEmpty()) {
            item {
                val leadingCount = listings.count { it.status == "open" && it.topBidderId == buyer.id }
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                        .clickable { onNavigateToBidTracker() }
                        .testTag("marketplace_bid_tracker_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📊", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Live Bid Tracker Active ($leadingCount Winning)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BuyerNavy
                                )
                                Text(
                                    text = "Track all active lots, outbid alerts & 1-tap counter-bids",
                                    fontSize = 11.sp,
                                    color = BuyerSlate
                                )
                            }
                        }
                        Text(
                            text = "View Tracker →",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = BuyerBlue
                        )
                    }
                }
            }
        }
        if (!buyer.isVerified) {
            item {
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚠️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Your buyer account is awaiting business verification. You can browse live auctions, but bidding is reserved for verified businesses.",
                            color = Color(0xFF92400E),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search produce: Tomatoes, Bananas, Chillies...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BuyerSlate) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            // Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedFilter == "all",
                        onClick = { selectedFilter = "all" },
                        label = { Text("All Lots") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "gradeA",
                        onClick = { selectedFilter = "gradeA" },
                        label = { Text("Grade A Premium") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "veg",
                        onClick = { selectedFilter = "veg" },
                        label = { Text("Vegetables") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "fruit",
                        onClick = { selectedFilter = "fruit" },
                        label = { Text("Fruits") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BuyerNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Produce Auctions (${filteredListings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )

                // Sort toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (selectedSort == "ending") "Ending Soon" else if (selectedSort == "price") "Top Bid" else "Largest Qty",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BuyerBlue,
                        modifier = Modifier
                            .clickable {
                                selectedSort = when (selectedSort) {
                                    "ending" -> "price"
                                    "price" -> "qty"
                                    else -> "ending"
                                }
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        if (filteredListings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No live lots matching your search.",
                        color = BuyerSlate,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(filteredListings, key = { it.id }) { listing ->
                BuyerListingCard(
                    listing = listing,
                    currentBuyerId = buyer.id,
                    onClick = { onOpen(listing.id) }
                )
            }
        }
    }
}

@Composable
fun BuyerListingCard(
    listing: ListingEntity,
    currentBuyerId: Long,
    onClick: () -> Unit
) {
    val isLeading = listing.topBidderId == currentBuyerId
    val currentPrice = listing.topPrice ?: listing.startPrice
    val lotTotal = currentPrice * listing.quantityKg

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = if (isLeading) 1.5.dp else 1.dp,
                color = if (isLeading) BuyerBlue else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("buyer_listing_card_${listing.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EmojiProduceBox(
                        emoji = produceEmoji(listing.produce),
                        modifier = Modifier.size(52.dp),
                        background = Color(0xFFEFF6FF)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = listing.produce,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BuyerNavy
                        )
                        Text(
                            text = "${listing.quantityKg.roundToInt()} kg • Grade ${listing.grade}",
                            style = MaterialTheme.typography.bodySmall,
                            color = BuyerSlate
                        )
                    }
                }

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Grade ${listing.grade}",
                        color = BuyerNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Farmer: ${listing.farmerName} • 📍 ${listing.location}",
                style = MaterialTheme.typography.bodySmall,
                color = BuyerSlate
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (listing.topPrice != null) "Current Bid" else "Starting From",
                            fontSize = 11.sp,
                            color = BuyerSlate
                        )
                        Text(
                            text = "${formatMoney(currentPrice)}/kg",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = BuyerNavy
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Whole Lot Value",
                            fontSize = 11.sp,
                            color = BuyerSlate
                        )
                        Text(
                            text = formatMoney(lotTotal),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = BuyerBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${listing.bidCount} bid${if (listing.bidCount == 1) "" else "s"}",
                        fontSize = 12.sp,
                        color = BuyerSlate
                    )
                    if (isLeading) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFD1FAE5),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "👑 You Lead",
                                color = Color(0xFF065F46),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                CompactAuctionCountdownTimer(
                    endsAtSeconds = listing.endsAt
                )
            }
        }
    }
}

@Composable
fun BuyerMyBidsView(
    buyer: UserEntity,
    userBids: List<BidEntity>,
    allListings: List<ListingEntity>,
    onOpen: (Long) -> Unit
) {
    if (userBids.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🏷️", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No Bids Placed Yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BuyerNavy
            )
            Text(
                text = "Browse the marketplace and place your first whole-lot bid directly with local farmers.",
                color = BuyerSlate,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    } else {
        // Group by listingId
        val bidsByListing = userBids.groupBy { it.listingId }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "My Active & Past Bids (${bidsByListing.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )
            }

            items(bidsByListing.entries.toList(), key = { it.key }) { (listingId, bids) ->
                val highestMyBid = bids.maxByOrNull { it.pricePerKg }
                val listing = allListings.find { it.id == listingId }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(listingId) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = listing?.produce ?: "Produce Lot #$listingId",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = BuyerNavy
                            )

                            val isLeading = listing?.topBidderId == buyer.id
                            Surface(
                                color = if (isLeading) Color(0xFFD1FAE5) else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (isLeading) "LEADING" else "OUTBID",
                                    color = if (isLeading) Color(0xFF065F46) else Color(0xFF92400E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Your Highest Bid", fontSize = 11.sp, color = BuyerSlate)
                                Text(
                                    text = formatMoney(highestMyBid?.pricePerKg ?: 0.0) + "/kg",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = BuyerNavy
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Current Highest Bid", fontSize = 11.sp, color = BuyerSlate)
                                Text(
                                    text = formatMoney(listing?.topPrice ?: 0.0) + "/kg",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = BuyerBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { onOpen(listingId) },
                            colors = ButtonDefaults.buttonColors(containerColor = BuyerNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(40.dp)
                        ) {
                            Text("Open Auction & Place New Bid", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuyerOrdersView(
    orders: List<OrderEntity>,
    onDeliver: (Long) -> Unit,
    onDispute: (Long, String) -> Unit
) {
    var disputeDialogOrderId by remember { mutableStateOf<Long?>(null) }
    var disputeReasonInput by remember { mutableStateOf("") }

    if (disputeDialogOrderId != null) {
        AlertDialog(
            onDismissRequest = { disputeDialogOrderId = null },
            title = { Text("Report Quality / Quantity Discrepancy") },
            text = {
                Column {
                    Text(
                        text = "Specify the problem with the delivered lot (e.g. grade differs from listing, spoiled produce, or short weight):",
                        fontSize = 13.sp,
                        color = BuyerSlate
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = disputeReasonInput,
                        onValueChange = { disputeReasonInput = it },
                        placeholder = { Text("Describe the problem clearly...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val oid = disputeDialogOrderId ?: return@Button
                        if (disputeReasonInput.isNotBlank()) {
                            onDispute(oid, disputeReasonInput)
                            disputeDialogOrderId = null
                            disputeReasonInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BuyerRed)
                ) {
                    Text("Submit Report")
                }
            },
            dismissButton = {
                TextButton(onClick = { disputeDialogOrderId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (orders.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🚚", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No Orders In Transit",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BuyerNavy
            )
            Text(
                text = "When your winning bid is confirmed by the farmer, your order appears here with live delivery status.",
                color = BuyerSlate,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "My Produce Orders (${orders.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )
            }

            items(orders, key = { it.id }) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("buyer_order_${order.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Order #${order.id} • ${order.produce}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = BuyerNavy
                                )
                                Text(
                                    text = "Farmer: ${order.farmerName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BuyerSlate
                                )
                            }
                            StatusBadge(status = order.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${order.quantityKg.roundToInt()} kg @ ${formatMoney(order.pricePerKg)}/kg",
                                fontSize = 14.sp,
                                color = BuyerNavy
                            )
                            Text(
                                text = "Total: ${formatMoney(order.totalAmount)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = BuyerNavy
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val progress = when (order.status) {
                            "confirmed" -> 0.33f
                            "dispatched" -> 0.66f
                            "delivered" -> 1.0f
                            else -> 0.5f
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = BuyerBlue,
                            trackColor = Color(0xFFE2E8F0)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Awaiting Dispatch", fontSize = 10.sp, color = BuyerSlate)
                            Text("In Transit", fontSize = 10.sp, color = BuyerSlate)
                            Text("Delivered", fontSize = 10.sp, color = BuyerSlate)
                        }

                        if (order.disputeReason != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color(0xFFFEE2E2),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Reported Discrepancy: ${order.disputeReason}",
                                    color = BuyerRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (order.status == "dispatched") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onDeliver(order.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).testTag("confirm_delivery_btn_${order.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Confirm Delivery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { disputeDialogOrderId = order.id },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BuyerRed),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).testTag("report_problem_btn_${order.id}")
                                ) {
                                    Icon(Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Report Issue", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuyerHowItWorksView() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "How FarmBid Works for Commercial Buyers",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )
                Spacer(modifier = Modifier.height(14.dp))

                val steps = listOf(
                    "1. Direct Farm Sourcing" to "Browse live produce auctions from regional growers. All lots specify crop variety, harvest timestamp, location, and verified grading.",
                    "2. Whole-Lot Transparent Bidding" to "Bids are placed per kilogram for the entire lot. A minimal increment of ₹0.50/kg ensures transparent price escalations.",
                    "3. Anti-Sniping Protection" to "Bids placed in the final 2 minutes automatically extend the clock by 2 minutes, preventing bot snipes and ensuring fair competition.",
                    "4. Guaranteed Delivery & Dispute Rights" to "Farmers dispatch verified stock. You inspect produce on arrival before confirming acceptance, with built-in dispute resolution for quality variances."
                )

                steps.forEach { (title, desc) ->
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BuyerNavy
                    )
                    Text(
                        text = desc,
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}
