package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.UserEntity
import com.example.ui.theme.BuyerBlue
import com.example.ui.theme.BuyerLightBg
import com.example.ui.theme.BuyerNavy
import com.example.ui.theme.BuyerRed
import com.example.ui.theme.BuyerSlate

@Composable
fun BuyerLoginScreen(
    buyers: List<UserEntity>,
    onLogin: (UserEntity) -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit
) {
    var selectedBuyerId by remember(buyers) { mutableStateOf(buyers.firstOrNull()?.id ?: 0L) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuyerLightBg)
            .verticalScroll(scrollState)
            .testTag("buyer_login_screen")
    ) {
        // Buyer Header
        Surface(
            color = BuyerNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Buyer Portal Login",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuyerBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Storefront, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Commercial Produce Sourcing",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Participate in transparent farm auctions & order fulfillment",
                            color = Color(0xFFBFDBFE),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Select Buyer Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BuyerNavy
            )
            Text(
                text = "Sign in to place bids and track lot deliveries:",
                style = MaterialTheme.typography.bodySmall,
                color = BuyerSlate,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            buyers.forEach { buyer ->
                val isSelected = buyer.id == selectedBuyerId
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) BuyerNavy else Color(0xFFCBD5E1),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedBuyerId = buyer.id }
                        .testTag("buyer_card_${buyer.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) BuyerNavy else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🛒",
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = buyer.name,
                                        fontWeight = FontWeight.Bold,
                                        color = BuyerNavy,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(isVerified = buyer.isVerified)
                                }
                                Text(
                                    text = "${buyer.businessType} • ${buyer.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BuyerSlate
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BuyerNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    val target = buyers.find { it.id == selectedBuyerId } ?: buyers.firstOrNull()
                    if (target != null) onLogin(target)
                },
                enabled = buyers.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BuyerNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_buyer_login_btn")
            ) {
                Text("Sign In as Selected Buyer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "New business or retailer?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BuyerSlate
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.testTag("register_new_buyer_btn")
                ) {
                    Text(
                        text = "Register Business",
                        color = BuyerNavy,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BuyerRegisterScreen(
    onRegister: (name: String, location: String, businessType: String, phone: String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("Wholesaler / Supermarket") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuyerLightBg)
            .verticalScroll(scrollState)
            .testTag("buyer_register_screen")
    ) {
        // Header
        Surface(
            color = BuyerNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Register Commercial Buyer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Connect directly with local farmers across regions",
                        color = Color(0xFFBFDBFE),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Business / Store Name") },
                placeholder = { Text("e.g. Apex Hypermarket / Royal Food Wholesalers") },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null, tint = BuyerBlue) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Business Location / City") },
                placeholder = { Text("e.g. Kakkanad, Kochi") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = BuyerBlue) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_location_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = businessType,
                onValueChange = { businessType = it },
                label = { Text("Business Type") },
                placeholder = { Text("e.g. Supermarket, Wholesaler, Restaurant, Retailer") },
                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = BuyerBlue) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_type_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Contact Number") },
                placeholder = { Text("e.g. +91 98471 99887") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BuyerBlue) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_phone_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BuyerNavy,
                    focusedLabelColor = BuyerNavy
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    color = BuyerRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your business or store name."
                    } else if (location.isBlank()) {
                        errorMessage = "Please enter your business location."
                    } else {
                        onRegister(name, location, businessType, phone)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BuyerNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_buyer_register_btn")
            ) {
                Text("Register & Enter Marketplace", fontWeight = FontWeight.Bold)
            }
        }
    }
}
