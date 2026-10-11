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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.ListingEntity
import com.example.data.OrderEntity
import com.example.data.UserEntity
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafContainer
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerLightBg
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerRedContainer
import com.example.ui.theme.FarmerSlateGray
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerHomeScreen(
    farmer: UserEntity,
    currentTab: String,
    listings: List<ListingEntity>,
    pendingDecisions: List<ListingEntity>,
    orders: List<OrderEntity>,
    allBids: List<BidEntity> = emptyList(),
    onTabSelected: (String) -> Unit,
    onOpenListing: (Long) -> Unit,
    onPublishListing: (produce: String, grade: String, qty: Double, harvestDate: String, location: String, desc: String, reserve: Double, duration: Int) -> Unit,
    onConfirmSale: (Long) -> Unit,
    onDeclineSale: (Long) -> Unit,
    onDispatchOrder: (Long) -> Unit,
    onMarkOrderPaid: (Long) -> Unit,
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
                            Text(text = "🌾", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = farmer.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                VerifiedBadge(isVerified = farmer.isVerified)
                            }
                            Text(
                                text = "Farmer Portal • ${farmer.location}",
                                color = Color(0xFFD4E7DC),
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onResetDemo,
                        modifier = Modifier.testTag("farmer_top_reset_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset Demo", tint = Color.White)
                    }
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("farmer_top_logout_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Switch Portal / Log Out", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FarmerDarkGreen,
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
                    selected = currentTab == "listings",
                    onClick = { onTabSelected("listings") },
                    icon = { Icon(Icons.Default.Spa, contentDescription = "My Produce") },
                    label = { Text("My Produce") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FarmerDarkGreen,
                        selectedTextColor = FarmerDarkGreen,
                        indicatorColor = FarmerContainerLight
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "new",
                    onClick = { onTabSelected("new") },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Publish Batch") },
                    label = { Text("New Lot") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FarmerDarkGreen,
                        selectedTextColor = FarmerDarkGreen,
                        indicatorColor = FarmerContainerLight
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "decisions",
                    onClick = { onTabSelected("decisions") },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (pendingDecisions.isNotEmpty()) {
                                    Badge(
                                        containerColor = FarmerEarthRed,
                                        contentColor = Color.White
                                    ) {
                                        Text("${pendingDecisions.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = "Decisions")
                        }
                    },
                    label = { Text("Decisions") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FarmerDarkGreen,
                        selectedTextColor = FarmerDarkGreen,
                        indicatorColor = FarmerContainerLight
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "orders",
                    onClick = { onTabSelected("orders") },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FarmerDarkGreen,
                        selectedTextColor = FarmerDarkGreen,
                        indicatorColor = FarmerContainerLight
                    )
                )

                NavigationBarItem(
                    selected = currentTab == "how",
                    onClick = { onTabSelected("how") },
                    icon = { Icon(Icons.Default.HelpOutline, contentDescription = "How It Works") },
                    label = { Text("Help") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FarmerDarkGreen,
                        selectedTextColor = FarmerDarkGreen,
                        indicatorColor = FarmerContainerLight
                    )
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(FarmerLightBg)
        ) {
            when (currentTab) {
                "listings" -> FarmerListingsView(
                    farmer = farmer,
                    listings = listings,
                    allBids = allBids,
                    onOpen = onOpenListing,
                    onNewListing = { onTabSelected("new") }
                )
                "new" -> FarmerNewListingForm(farmer = farmer, onPublish = onPublishListing)
                "decisions" -> FarmerDecisionsView(pendingListings = pendingDecisions, onConfirm = onConfirmSale, onDecline = onDeclineSale)
                "orders" -> FarmerOrdersView(orders = orders, onDispatch = onDispatchOrder, onMarkPaid = onMarkOrderPaid)
                "how" -> FarmerHowItWorksView()
            }
        }
    }
}

@Composable
fun FarmerListingsView(
    farmer: UserEntity,
    listings: List<ListingEntity>,
    onOpen: (Long) -> Unit,
    onNewListing: () -> Unit
) {
    var viewMode by remember { mutableStateOf("listings") } // listings or tracker
    val activeListings = listings.filter { it.status == "open" }

    if (viewMode == "tracker") {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = false,
                    onClick = { viewMode = "listings" },
                    label = { Text("🌾 Produce Batches (${listings.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors()
                )
                FilterChip(
                    selected = true,
                    onClick = { viewMode = "tracker" },
                    label = { Text("📊 Live Bid Tracker (${activeListings.size} Live)", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FarmerDarkGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
            FarmerLotBidTrackerView(
                farmer = farmer,
                listings = listings,
                onOpenListing = onOpen,
                onEndAuctionEarly = {},
                modifier = Modifier.weight(1f)
            )
        }
    } else if (listings.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🚜", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No produce listed yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarmerDarkGreen
            )
            Text(
                text = "Publish your harvest lot to attract verified wholesale buyers and real-time bids.",
                color = FarmerSlateGray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )
            Button(
                onClick = onNewListing,
                colors = ButtonDefaults.buttonColors(containerColor = FarmerDarkGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Publish First Crop Batch")
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = true,
                        onClick = { viewMode = "listings" },
                        label = { Text("🌾 Produce Batches (${listings.size})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FarmerDarkGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = false,
                        onClick = { viewMode = "tracker" },
                        label = { Text("📊 Live Bid Tracker (${activeListings.size} Live)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors()
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Harvest Batches (${listings.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FarmerDarkGreen
                    )
                    Surface(
                        color = FarmerContainerLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Confidential Reserves",
                            color = FarmerPineGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            items(listings, key = { it.id }) { listing ->
                FarmerListingCard(listing = listing, onClick = { onOpen(listing.id) })
            }
        }
    }
}

@Composable
fun FarmerListingCard(listing: ListingEntity, onClick: () -> Unit) {
    val isOpen = listing.status == "open"
    val isReserveMet = listing.topPrice != null && listing.topPrice >= listing.reservePrice

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (isOpen) FarmerLeafGreen.copy(alpha = 0.5f) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("farmer_listing_card_${listing.id}")
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
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = listing.produce,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FarmerDarkGreen
                        )
                        Text(
                            text = "${listing.quantityKg.roundToInt()} kg • Grade ${listing.grade} • ${listing.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = FarmerSlateGray
                        )
                    }
                }

                StatusBadge(status = listing.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = FarmerLightBg,
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
                            text = "Current Top Bid",
                            fontSize = 11.sp,
                            color = FarmerSlateGray
                        )
                        Text(
                            text = if (listing.topPrice != null) formatMoney(listing.topPrice) + "/kg" else "No bids yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (listing.topPrice != null) FarmerDarkGreen else FarmerSlateGray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Your Private Reserve",
                            fontSize = 11.sp,
                            color = FarmerPineGreen
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatMoney(listing.reservePrice) + "/kg",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = FarmerPineGreen
                            )
                            if (isOpen && listing.topPrice != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isReserveMet) "✅ Met" else "⏳ Below",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isReserveMet) FarmerLeafGreen else FarmerEarthRed
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${listing.bidCount} bid${if (listing.bidCount == 1) "" else "s"}" +
                            if (listing.topBidderName != null) " • Top: ${listing.topBidderName}" else "",
                    fontSize = 12.sp,
                    color = FarmerSlateGray
                )

                if (isOpen) {
                    CompactAuctionCountdownTimer(
                        endsAtSeconds = listing.endsAt
                    )
                }
            }
        }
    }
}

@Composable
fun FarmerNewListingForm(
    farmer: UserEntity,
    onPublish: (produce: String, grade: String, qty: Double, harvestDate: String, location: String, desc: String, reserve: Double, duration: Int) -> Unit
) {
    var produce by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("A") }
    var qtyString by remember { mutableStateOf("100") }
    var harvestDate by remember { mutableStateOf("Today, morning harvest") }
    var location by remember { mutableStateOf(farmer.location) }
    var desc by remember { mutableStateOf("") }
    var reserveString by remember { mutableStateOf("25") }
    var durationMinutes by remember { mutableIntStateOf(60) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val qty = qtyString.toDoubleOrNull() ?: 0.0
    val reserve = reserveString.toDoubleOrNull() ?: 0.0

    // Net earnings estimation: Gross - 2% Platform Fee - ₹1.5/kg Transport
    val gross = qty * reserve
    val platformFee = gross * 0.02
    val transport = qty * 1.5
    val estimatedNet = if (gross > 0) gross - platformFee - transport else 0.0

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .testTag("new_listing_form")
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Publish Crop Batch for Auction",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarmerDarkGreen
                )
                Text(
                    text = "Verified buyers will bid per kilogram for the entire lot.",
                    style = MaterialTheme.typography.bodySmall,
                    color = FarmerSlateGray,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                OutlinedTextField(
                    value = produce,
                    onValueChange = { produce = it },
                    label = { Text("Produce Name") },
                    placeholder = { Text("e.g. Tomatoes, Green Chillies, Nendran Bananas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_produce_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FarmerDarkGreen,
                        focusedLabelColor = FarmerDarkGreen
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Produce Grade", style = MaterialTheme.typography.labelMedium, color = FarmerDarkGreen)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    listOf("A", "B", "C").forEach { g ->
                        FilterChip(
                            selected = grade == g,
                            onClick = { grade = g },
                            label = { Text("Grade $g ${if (g == "A") "(Premium)" else if (g == "B") "(Standard)" else "(Fair)"}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FarmerDarkGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = qtyString,
                        onValueChange = { qtyString = it },
                        label = { Text("Quantity (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("input_quantity"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmerDarkGreen,
                            focusedLabelColor = FarmerDarkGreen
                        )
                    )

                    OutlinedTextField(
                        value = reserveString,
                        onValueChange = { reserveString = it },
                        label = { Text("Reserve Price (₹/kg)") },
                        supportingText = { Text("Private: kept hidden from buyers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("input_reserve_price"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmerDarkGreen,
                            focusedLabelColor = FarmerDarkGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = harvestDate,
                        onValueChange = { harvestDate = it },
                        label = { Text("Harvest Date / Time") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmerDarkGreen,
                            focusedLabelColor = FarmerDarkGreen
                        )
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Farm Location") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmerDarkGreen,
                            focusedLabelColor = FarmerDarkGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description & Crop Quality Notes") },
                    placeholder = { Text("Freshly hand-picked, uniform color, ready for transport...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FarmerDarkGreen,
                        focusedLabelColor = FarmerDarkGreen
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Auction Duration", style = MaterialTheme.typography.labelMedium, color = FarmerDarkGreen)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    listOf(30 to "30 mins", 60 to "1 hour", 120 to "2 hours", 360 to "6 hours").forEach { (m, label) ->
                        FilterChip(
                            selected = durationMinutes == m,
                            onClick = { durationMinutes = m },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FarmerDarkGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Real-time Earnings Calculator Box
                Surface(
                    color = FarmerContainerLight,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Estimated Net Earnings at Reserve Price:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = FarmerDarkGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatMoney(estimatedNet),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmerDarkGreen
                        )
                        Text(
                            text = "Gross ${formatMoney(gross)} − 2% Platform Fee (${formatMoney(platformFee)}) − Transport @ ₹1.5/kg (${formatMoney(transport)})",
                            fontSize = 11.sp,
                            color = FarmerPineGreen
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = FarmerEarthRed,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (produce.isBlank()) {
                            errorMessage = "Please enter produce name."
                        } else if (qty <= 0) {
                            errorMessage = "Quantity must be greater than zero."
                        } else if (reserve <= 0) {
                            errorMessage = "Reserve price must be greater than zero."
                        } else {
                            errorMessage = null
                            onPublish(produce, grade, qty, harvestDate, location, desc, reserve, durationMinutes)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FarmerDarkGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_publish_listing")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish Harvest Auction", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun FarmerDecisionsView(
    pendingListings: List<ListingEntity>,
    onConfirm: (Long) -> Unit,
    onDecline: (Long) -> Unit
) {
    if (pendingListings.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "⚖️", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No Pending Decisions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarmerDarkGreen
            )
            Text(
                text = "When an auction ends and meets your reserve price, you decide whether to confirm the sale and dispatch the order.",
                color = FarmerSlateGray,
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
                    text = "Awaiting Your Sale Confirmation (${pendingListings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarmerDarkGreen
                )
            }

            items(pendingListings, key = { it.id }) { item ->
                val gross = (item.topPrice ?: 0.0) * item.quantityKg
                val net = gross - (gross * 0.02) - (item.quantityKg * 1.5)

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("pending_decision_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                EmojiProduceBox(emoji = produceEmoji(item.produce), modifier = Modifier.size(46.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = item.produce,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = FarmerDarkGreen
                                    )
                                    Text(
                                        text = "${item.quantityKg.roundToInt()} kg • Grade ${item.grade}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FarmerSlateGray
                                    )
                                }
                            }
                            StatusBadge(status = item.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = FarmerContainerLight,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Winning Bidder: ${item.topBidderName ?: "Verified Wholesale Buyer"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FarmerDarkGreen
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Winning Offer: ${formatMoney(item.topPrice ?: 0.0)}/kg • Lot Total: ${formatMoney(gross)}",
                                    fontSize = 13.sp,
                                    color = FarmerDarkGreen
                                )
                                Text(
                                    text = "Estimated Net after fee & transport: ${formatMoney(net)} (Your reserve was ${formatMoney(item.reservePrice)}/kg)",
                                    fontSize = 11.sp,
                                    color = FarmerPineGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onConfirm(item.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = FarmerDarkGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("confirm_sale_btn_${item.id}")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Confirm Sale", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onDecline(item.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = FarmerEarthRed),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("decline_sale_btn_${item.id}")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Decline", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerOrdersView(
    orders: List<OrderEntity>,
    onDispatch: (Long) -> Unit,
    onMarkPaid: (Long) -> Unit
) {
    if (orders.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "📦", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No Orders Yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = FarmerDarkGreen
            )
            Text(
                text = "Confirmed auction sales turn into active orders here for dispatch and payment tracking.",
                color = FarmerSlateGray,
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
                    text = "Dispatch & Fulfillment Orders (${orders.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FarmerDarkGreen
                )
            }

            items(orders, key = { it.id }) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("farmer_order_${order.id}")
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
                                    color = FarmerDarkGreen
                                )
                                Text(
                                    text = "Buyer: ${order.buyerName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FarmerSlateGray
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
                                color = FarmerDarkGreen
                            )
                            Text(
                                text = "Total: ${formatMoney(order.totalAmount)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = FarmerDarkGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Progress Bar
                        val progress = when (order.status) {
                            "confirmed" -> 0.33f
                            "dispatched" -> 0.66f
                            "delivered" -> 1.0f
                            else -> 0.5f
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = FarmerLeafGreen,
                            trackColor = Color(0xFFE2E8F0)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Confirmed", fontSize = 10.sp, color = FarmerSlateGray)
                            Text("Dispatched", fontSize = 10.sp, color = FarmerSlateGray)
                            Text("Delivered", fontSize = 10.sp, color = FarmerSlateGray)
                        }

                        if (order.disputeReason != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = FarmerRedContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Dispute Note: ${order.disputeReason}",
                                    color = FarmerEarthRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (order.paymentStatus == "paid") Color(0xFFD1FAE5) else Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Payment: ${order.paymentStatus.uppercase()}",
                                    color = if (order.paymentStatus == "paid") Color(0xFF065F46) else Color(0xFF92400E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            if (order.status == "confirmed") {
                                Button(
                                    onClick = { onDispatch(order.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FarmerDarkGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("dispatch_order_btn_${order.id}")
                                ) {
                                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark Dispatched", fontSize = 12.sp)
                                }
                            } else if (order.status == "delivered" && order.paymentStatus == "pending") {
                                Button(
                                    onClick = { onMarkPaid(order.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FarmerPineGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("mark_paid_btn_${order.id}")
                                ) {
                                    Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Record Payment Received", fontSize = 12.sp)
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
fun FarmerHowItWorksView() {
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
                    text = "How FarmBid Works for Farmers",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarmerDarkGreen
                )
                Spacer(modifier = Modifier.height(14.dp))

                val steps = listOf(
                    "1. List Produce with Confidential Reserve" to "Specify harvest time, grade, quantity, and your private reserve price per kg. Buyers never see your reserve, preventing artificial price suppression.",
                    "2. Live Competitive Bidding" to "Verified buyers (supermarkets, restaurants, mandis) place competitive whole-lot bids. Anti-sniping extends auctions by 2 minutes on late bids to ensure fair price discovery.",
                    "3. Zero Forced Losses" to "If bids don't meet your reserve, nothing sells at a loss. If the top bid meets reserve, you have final authority to confirm or decline the winning bid.",
                    "4. Guaranteed Dispatch & Direct Payment" to "Once confirmed, dispatch your harvest with tracking. Upon produce delivery inspection, record payment directly."
                )

                steps.forEach { (title, desc) ->
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = FarmerDarkGreen
                    )
                    Text(
                        text = desc,
                        fontSize = 13.sp,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )
                }

                Surface(
                    color = FarmerContainerLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Platform Fee Transparency",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = FarmerDarkGreen
                        )
                        Text(
                            text = "Platform fee is strictly 2.0% of the sale value. Farm gate transport logistics are credited at ₹1.50 per kg.",
                            fontSize = 11.sp,
                            color = FarmerPineGreen
                        )
                    }
                }
            }
        }
    }
}
