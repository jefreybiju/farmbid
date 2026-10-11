package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ListingEntity
import com.example.data.UserEntity
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.BuyerSlate
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerSlateGray

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FarmerDirectoryView(
    farmers: List<UserEntity>,
    allListings: List<ListingEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCertification: String,
    onCertificationChange: (String) -> Unit,
    selectedProduce: String,
    onProduceChange: (String) -> Unit,
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    onOpenListing: (Long) -> Unit
) {
    var selectedFarmerForModal by remember { mutableStateOf<UserEntity?>(null) }

    // Dialog displaying Farmer Profile Summary
    if (selectedFarmerForModal != null) {
        val f = selectedFarmerForModal!!
        val farmerListings = allListings.filter { it.farmerId == f.id && it.status == "open" }

        AlertDialog(
            onDismissRequest = { selectedFarmerForModal = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FarmerContainerLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👨‍🌾", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = f.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = BuyerNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            VerifiedBadge(isVerified = f.isVerified)
                        }
                        Text(
                            text = f.businessType,
                            style = MaterialTheme.typography.bodySmall,
                            color = BuyerSlate
                        )
                    }
                }
            },
            text = {
                Column {
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("📍 Location: ${f.location}", fontSize = 12.sp, color = BuyerSlate)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("${f.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BuyerNavy)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🌱 Cultivation Area: ${f.farmSizeAcres} Acres • ${f.experienceYears} Years Exp.", fontSize = 12.sp, color = BuyerSlate)
                            Text("📞 Phone: ${f.phone}", fontSize = 12.sp, color = BuyerNavy, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Certifications & Verified Standards:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BuyerNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        f.certifications.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { cert ->
                            Surface(
                                color = FarmerContainerLight,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "🌿 $cert",
                                    color = FarmerDarkGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Produce Offered:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BuyerNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        f.produceOffered.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { prod ->
                            Surface(
                                color = Color(0xFFEFF6FF),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${produceEmoji(prod)} $prod",
                                    color = BuyerNavy,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (farmerListings.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Active Harvest Lots (${farmerListings.size}):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BuyerNavy)
                        Spacer(modifier = Modifier.height(4.dp))
                        farmerListings.forEach { lot ->
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedFarmerForModal = null
                                        onOpenListing(lot.id)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${produceEmoji(lot.produce)} ${lot.produce} (${lot.quantityKg.toInt()} kg)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BuyerNavy)
                                    Text(
                                        text = if (lot.topPrice != null) formatMoney(lot.topPrice) + "/kg" else "Start: " + formatMoney(lot.startPrice) + "/kg",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FarmerLeafGreen
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedFarmerForModal = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BuyerNavy)
                ) {
                    Text("Close")
                }
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Search & Filter Farmers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BuyerNavy
            )
            Text(
                text = "Discover verified agricultural producers by name, location, produce type, and organic certifications.",
                style = MaterialTheme.typography.bodySmall,
                color = BuyerSlate,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            // Robust Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search farmer name, district, crop (e.g. Anil, Munnar, Organic)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BuyerSlate) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = BuyerSlate)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_directory_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Certification Filter Strip
        item {
            Column {
                Text(
                    text = "Filter by Certifications:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val certs = listOf(
                        "ALL" to "All Standards",
                        "Organic" to "🌿 Organic (NPOP)",
                        "Jaivik Bharat" to "🏅 Jaivik Bharat",
                        "GAP" to "🛡️ GAP Certified",
                        "Natural" to "🌾 Natural / SPNF",
                        "Export" to "✈️ Export Quality"
                    )
                    items(certs) { (key, label) ->
                        FilterChip(
                            selected = selectedCertification.equals(key, ignoreCase = true),
                            onClick = { onCertificationChange(key) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BuyerNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Produce Filter Strip
        item {
            Column {
                Text(
                    text = "Filter by Produce Offered:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val produces = listOf(
                        "ALL" to "All Produce",
                        "Tomato" to "🍅 Tomatoes",
                        "Banana" to "🍌 Bananas",
                        "Pineapple" to "🍍 Pineapples",
                        "Okra" to "🌿 Okra",
                        "Chilli" to "🌶️ Chillies",
                        "Pepper" to "🫚 Black Pepper / Spices"
                    )
                    items(produces) { (key, label) ->
                        FilterChip(
                            selected = selectedProduce.equals(key, ignoreCase = true),
                            onClick = { onProduceChange(key) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2563EB),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Location Filter Strip
        item {
            Column {
                Text(
                    text = "Filter by Location / District:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val locations = listOf(
                        "ALL" to "All Kerala",
                        "Ernakulam" to "📍 Ernakulam",
                        "Idukki" to "📍 Idukki / Munnar",
                        "Palakkad" to "📍 Palakkad",
                        "Wayanad" to "📍 Wayanad",
                        "Alappuzha" to "📍 Alappuzha"
                    )
                    items(locations) { (key, label) ->
                        FilterChip(
                            selected = selectedLocation.equals(key, ignoreCase = true),
                            onClick = { onLocationChange(key) },
                            label = { Text(label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F766E),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Farmer Profile Summaries (${farmers.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BuyerNavy
                )
                if (searchQuery.isNotEmpty() || selectedCertification != "ALL" || selectedProduce != "ALL" || selectedLocation != "ALL") {
                    TextButton(onClick = {
                        onSearchQueryChange("")
                        onCertificationChange("ALL")
                        onProduceChange("ALL")
                        onLocationChange("ALL")
                    }) {
                        Text("Reset Filters", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }
                }
            }
        }

        if (farmers.isEmpty()) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔍", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No farmers match your criteria",
                            fontWeight = FontWeight.Bold,
                            color = BuyerNavy,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Try clearing the certification or produce filters to view all registered growers.",
                            color = BuyerSlate,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(farmers, key = { it.id }) { farmer ->
                FarmerProfileSummaryCard(
                    farmer = farmer,
                    activeLotsCount = allListings.count { it.farmerId == farmer.id && it.status == "open" },
                    onViewSummary = { selectedFarmerForModal = farmer }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FarmerProfileSummaryCard(
    farmer: UserEntity,
    activeLotsCount: Int,
    onViewSummary: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewSummary)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .testTag("farmer_summary_card_${farmer.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Avatar, Name, Verified, Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(FarmerContainerLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌾", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = farmer.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = BuyerNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            VerifiedBadge(isVerified = farmer.isVerified)
                        }
                        Text(
                            text = farmer.businessType,
                            style = MaterialTheme.typography.bodySmall,
                            color = FarmerPineGreen
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${farmer.rating}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location & Farm Size Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BuyerSlate,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = farmer.location,
                    fontSize = 12.sp,
                    color = BuyerSlate
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Landscape,
                    contentDescription = null,
                    tint = BuyerSlate,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${farmer.farmSizeAcres} Acres",
                    fontSize = 12.sp,
                    color = BuyerSlate
                )
            }

            // Certifications Badges
            if (farmer.certifications.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    farmer.certifications.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { cert ->
                        Surface(
                            color = FarmerContainerLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "🌿 $cert",
                                color = FarmerDarkGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Produce Offered tags
            if (farmer.produceOffered.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Produce: ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuyerSlate
                    )
                    Text(
                        text = farmer.produceOffered,
                        fontSize = 11.sp,
                        color = BuyerNavy,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions: Summary & Active Lots count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (activeLotsCount > 0) Color(0xFFD1FAE5) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (activeLotsCount > 0) "🌾 $activeLotsCount Live Auctions" else "No Live Lots",
                        color = if (activeLotsCount > 0) Color(0xFF065F46) else Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onViewSummary,
                    colors = ButtonDefaults.buttonColors(containerColor = BuyerNavy),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("View Profile Summary", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
