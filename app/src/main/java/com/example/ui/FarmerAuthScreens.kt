package com.example.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserEntity
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerLightBg
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerSlateGray

@Composable
fun FarmerLoginScreen(
    farmers: List<UserEntity>,
    onLogin: (UserEntity) -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit
) {
    var selectedFarmerId by remember(farmers) { mutableStateOf(farmers.firstOrNull()?.id ?: 0L) }
    var phoneInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmerLightBg)
            .verticalScroll(scrollState)
            .testTag("farmer_login_screen")
    ) {
        // Farmer Header with banner & dark green theme (#1b3f30)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.farmer_field_banner_1791702655614),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FarmerDarkGreen.copy(alpha = 0.88f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
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
                    Text(
                        text = "Farmer Portal Login",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(FarmerLeafGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Agriculture, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Grower Authentication",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Direct harvest listings with confidential reserve prices",
                            color = Color(0xFFD4E7DC),
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
            // Option A: Quick Profile Selection
            Text(
                text = "Select Registered Farmer Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FarmerDarkGreen
            )
            Text(
                text = "Choose your verified farm identity to manage lots & orders:",
                style = MaterialTheme.typography.bodySmall,
                color = FarmerSlateGray,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            farmers.forEach { farmer ->
                val isSelected = farmer.id == selectedFarmerId
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) FarmerContainerLight else Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) FarmerDarkGreen else Color(0xFFDDE5E0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedFarmerId = farmer.id }
                        .testTag("farmer_card_${farmer.id}")
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
                                    .background(if (isSelected) FarmerDarkGreen else FarmerPineGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🌾",
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = farmer.name,
                                        fontWeight = FontWeight.Bold,
                                        color = FarmerDarkGreen,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(isVerified = farmer.isVerified)
                                }
                                Text(
                                    text = "${farmer.businessType} • ${farmer.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FarmerSlateGray
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(FarmerDarkGreen),
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
                    val target = farmers.find { it.id == selectedFarmerId } ?: farmers.firstOrNull()
                    if (target != null) onLogin(target)
                },
                enabled = farmers.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FarmerDarkGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_farmer_login_btn")
            ) {
                Text("Sign In as Selected Farmer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Or register new farm
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "New grower to the platform?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = FarmerSlateGray
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.testTag("register_new_farmer_btn")
                ) {
                    Text(
                        text = "Register Farm",
                        color = FarmerDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun FarmerRegisterScreen(
    onRegister: (name: String, location: String, farmType: String, phone: String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var farmType by remember { mutableStateOf("Vegetable & Crop Farm") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmerLightBg)
            .verticalScroll(scrollState)
            .testTag("farmer_register_screen")
    ) {
        // Header
        Surface(
            color = FarmerDarkGreen,
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
                        text = "Register New Farm",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Join FarmBid as a verified agricultural producer",
                        color = Color(0xFFD4E7DC),
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
                label = { Text("Farmer / Farm Name") },
                placeholder = { Text("e.g. Joy Varghese / Green Valley Farm") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FarmerPineGreen) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FarmerDarkGreen,
                    focusedLabelColor = FarmerDarkGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Farm Location / Village & District") },
                placeholder = { Text("e.g. Kothamangalam, Ernakulam") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = FarmerPineGreen) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_location_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FarmerDarkGreen,
                    focusedLabelColor = FarmerDarkGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = farmType,
                onValueChange = { farmType = it },
                label = { Text("Crops & Produce Specialty") },
                placeholder = { Text("e.g. Vegetables, Plantains, Cardamom") },
                leadingIcon = { Icon(Icons.Default.Spa, contentDescription = null, tint = FarmerPineGreen) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_crop_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FarmerDarkGreen,
                    focusedLabelColor = FarmerDarkGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Mobile Contact Number") },
                placeholder = { Text("e.g. +91 98470 12345") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = FarmerPineGreen) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_phone_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FarmerDarkGreen,
                    focusedLabelColor = FarmerDarkGreen
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    color = FarmerEarthRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name or farm name."
                    } else if (location.isBlank()) {
                        errorMessage = "Please enter your farm location."
                    } else {
                        onRegister(name, location, farmType, phone)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FarmerDarkGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_farmer_register_btn")
            ) {
                Text("Create Account & Open Portal", fontWeight = FontWeight.Bold)
            }
        }
    }
}
