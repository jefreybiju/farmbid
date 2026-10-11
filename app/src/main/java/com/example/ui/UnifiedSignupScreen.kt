package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerLightBg
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerLightBg
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerSlateGray

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UnifiedSignupScreen(
    initialRole: String = "farmer",
    onRegister: (
        role: String,
        name: String,
        location: String,
        businessOrFarmType: String,
        phone: String,
        certifications: String,
        produceOffered: String,
        farmSize: Double
    ) -> Unit,
    onNavigateToLogin: (role: String) -> Unit,
    onBack: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(initialRole) }
    val isFarmer = selectedRole == "farmer"

    // Form inputs
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var businessOrFarmType by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var produceOffered by remember { mutableStateOf("") }
    var farmSizeStr by remember { mutableStateOf("5.0") }

    // Certifications for Farmer
    val availableCerts = listOf(
        "Certified Organic (NPOP)",
        "Jaivik Bharat",
        "GAP Certified",
        "Zero Budget Natural",
        "Export Certified",
        "Fair Trade"
    )
    val selectedCerts = remember { mutableStateOf(setOf("Certified Organic (NPOP)")) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val currentThemeColor = if (isFarmer) FarmerDarkGreen else BuyerNavy
    val currentBgColor = if (isFarmer) FarmerLightBg else BuyerLightBg

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentBgColor)
            .verticalScroll(scrollState)
            .testTag("unified_signup_screen")
    ) {
        // Dynamic Header
        Surface(
            color = currentThemeColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Create FarmBid Account",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Select your role to access role-tailored dashboards",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mandatory Role Selector
                Text(
                    text = "SELECT YOUR ACCOUNT ROLE:",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.25f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Farmer Option
                    Surface(
                        color = if (isFarmer) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = "farmer" }
                            .testTag("signup_role_farmer")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌾 Farmer",
                                fontWeight = FontWeight.Bold,
                                color = if (isFarmer) FarmerDarkGreen else Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Buyer Option
                    Surface(
                        color = if (!isFarmer) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = "buyer" }
                            .testTag("signup_role_buyer")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🛒 Commercial Buyer",
                                fontWeight = FontWeight.Bold,
                                color = if (!isFarmer) BuyerNavy else Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Form Body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isFarmer) "Agricultural Producer Registration" else "Commercial Buyer Registration",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = currentThemeColor
                    )
                    Text(
                        text = if (isFarmer)
                            "Publish crops with private reserve prices and receive verified bids."
                        else
                            "Browse harvest lots, view certified farmer profiles, and bid whole-lots.",
                        style = MaterialTheme.typography.bodySmall,
                        color = FarmerSlateGray,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(if (isFarmer) "Farmer / Farm Name" else "Business / Store Name") },
                        placeholder = { Text(if (isFarmer) "e.g. Thomas Farm / Green Hills" else "e.g. Cochin Wholesale Mart") },
                        leadingIcon = { Icon(if (isFarmer) Icons.Default.Person else Icons.Default.Storefront, contentDescription = null, tint = currentThemeColor) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = currentThemeColor,
                            focusedLabelColor = currentThemeColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location / District") },
                        placeholder = { Text("e.g. Munnar, Idukki / Kakkanad, Kochi") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = currentThemeColor) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_location_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = currentThemeColor,
                            focusedLabelColor = currentThemeColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = businessOrFarmType,
                        onValueChange = { businessOrFarmType = it },
                        label = { Text(if (isFarmer) "Farm Type / Specialty" else "Business Category") },
                        placeholder = { Text(if (isFarmer) "e.g. Organic Vegetables, Highland Fruits" else "e.g. Supermarket, Wholesaler, Restaurant") },
                        leadingIcon = { Icon(if (isFarmer) Icons.Default.Spa else Icons.Default.Business, contentDescription = null, tint = currentThemeColor) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_type_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = currentThemeColor,
                            focusedLabelColor = currentThemeColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Contact") },
                        placeholder = { Text("+91 98470 12345") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = currentThemeColor) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_phone_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = currentThemeColor,
                            focusedLabelColor = currentThemeColor
                        )
                    )

                    // Farmer-Specific Additional Fields
                    if (isFarmer) {
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = produceOffered,
                            onValueChange = { produceOffered = it },
                            label = { Text("Primary Produce Offered") },
                            placeholder = { Text("e.g. Tomatoes, Pineapples, Bananas, Chillies, Spices") },
                            supportingText = { Text("Buyers search and discover your farm by these produce types") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("signup_produce_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FarmerDarkGreen,
                                focusedLabelColor = FarmerDarkGreen
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = farmSizeStr,
                            onValueChange = { farmSizeStr = it },
                            label = { Text("Cultivated Farm Size (Acres)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            leadingIcon = { Icon(Icons.Default.Landscape, contentDescription = null, tint = FarmerPineGreen) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FarmerDarkGreen,
                                focusedLabelColor = FarmerDarkGreen
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Agricultural Certifications:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = FarmerDarkGreen
                        )
                        Text(
                            text = "Select any verified standards your farm adheres to:",
                            fontSize = 11.sp,
                            color = FarmerSlateGray
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableCerts.forEach { cert ->
                                val selected = selectedCerts.value.contains(cert)
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        val updated = selectedCerts.value.toMutableSet()
                                        if (selected) updated.remove(cert) else updated.add(cert)
                                        selectedCerts.value = updated
                                    },
                                    label = { Text(cert, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FarmerDarkGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = FarmerEarthRed,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                errorMessage = "Please enter your name or business name."
                            } else if (location.isBlank()) {
                                errorMessage = "Please enter your location."
                            } else {
                                errorMessage = null
                                val farmSize = farmSizeStr.toDoubleOrNull() ?: 3.0
                                val certsString = selectedCerts.value.joinToString(", ")
                                onRegister(
                                    selectedRole,
                                    name,
                                    location,
                                    businessOrFarmType.ifBlank { if (isFarmer) "Organic Farm" else "Wholesaler" },
                                    phone,
                                    certsString,
                                    produceOffered.ifBlank { "Vegetables, Fruits" },
                                    farmSize
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentThemeColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_unified_signup_btn")
                    ) {
                        Text(
                            text = if (isFarmer) "Register & Open Farmer Portal" else "Register & Open Buyer Portal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FarmerSlateGray
                )
                TextButton(onClick = { onNavigateToLogin(selectedRole) }) {
                    Text(
                        text = if (isFarmer) "Farmer Sign In" else "Buyer Sign In",
                        color = currentThemeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
