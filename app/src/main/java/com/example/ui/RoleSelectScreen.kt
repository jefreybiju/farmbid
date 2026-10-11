package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserEntity
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

@Composable
fun RoleSelectScreen(
    farmers: List<UserEntity>,
    buyers: List<UserEntity>,
    onSelectRole: (role: String) -> Unit,
    onNavigateToSignup: (role: String) -> Unit,
    onQuickLogin: (UserEntity) -> Unit,
    onThreeColumnLogin: (role: String, userId: String, pass: String) -> Unit,
    onResetDemo: () -> Unit
) {
    var selectedRole by remember { mutableStateOf("farmer") }
    var userIdInput by remember { mutableStateOf("farmer_anil") }
    var passwordInput by remember { mutableStateOf("farm123") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val isFarmer = selectedRole.equals("farmer", ignoreCase = true)
    val activeThemeColor = if (isFarmer) FarmerDarkGreen else BuyerNavy
    val activeBgColor = if (isFarmer) FarmerLightBg else BuyerLightBg

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmerLightBg)
            .verticalScroll(scrollState)
            .testTag("role_select_screen")
    ) {
        // Hero Header with Farm Field Banner & Tractor Branding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.farmer_field_banner_1791702655614),
                contentDescription = "Farm Field and Tractor",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                FarmerDarkGreen.copy(alpha = 0.55f),
                                FarmerDarkGreen.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_farmbid_logo_1791702637411),
                            contentDescription = "FarmBid Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "FarmBid",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Direct Agricultural Mandi & Live Auction Marketplace",
                            color = Color(0xFFD4E7DC),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 3.dp)) {
                            Surface(
                                color = Color(0xFF0F291E),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "⚡ Live Bid Tracker & Real-Time Countdown Active",
                                    color = Color(0xFF86EFAC),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header for 3-Column Gateway
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Role Selection Gateway",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = FarmerDarkGreen
                    )
                    Text(
                        text = "Three-column sign-in: (1) Role, (2) User ID, (3) Password",
                        style = MaterialTheme.typography.bodySmall,
                        color = FarmerSlateGray
                    )
                }

                Surface(
                    color = activeThemeColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = if (isFarmer) "🌾 Farmer Mode" else "🛒 Buyer Mode",
                        color = activeThemeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ================= THREE COLUMNS GATEWAY CONTAINER =================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, activeThemeColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .testTag("three_column_gateway_card")
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val isWide = maxWidth >= 700.dp

                    if (isWide) {
                        // Desktop / Tablet Landscape: 3 Side-by-Side Columns
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Column 1: Select Role
                            Box(modifier = Modifier.weight(1f)) {
                                ColumnOneRoleSelect(
                                    selectedRole = selectedRole,
                                    onRoleChange = { role ->
                                        selectedRole = role
                                        if (role == "farmer") {
                                            userIdInput = "farmer_anil"
                                            passwordInput = "farm123"
                                        } else {
                                            userIdInput = "buyer_fresh"
                                            passwordInput = "buyer123"
                                        }
                                    }
                                )
                            }

                            // Column 2: User ID
                            Box(modifier = Modifier.weight(1f)) {
                                ColumnTwoUserId(
                                    selectedRole = selectedRole,
                                    userIdInput = userIdInput,
                                    onUserIdChange = { userIdInput = it },
                                    activeThemeColor = activeThemeColor,
                                    farmers = farmers,
                                    buyers = buyers
                                )
                            }

                            // Column 3: Password
                            Box(modifier = Modifier.weight(1f)) {
                                ColumnThreePassword(
                                    passwordInput = passwordInput,
                                    onPasswordChange = { passwordInput = it },
                                    isPasswordVisible = isPasswordVisible,
                                    onToggleVisibility = { isPasswordVisible = !isPasswordVisible },
                                    activeThemeColor = activeThemeColor,
                                    onSubmit = {
                                        onThreeColumnLogin(selectedRole, userIdInput, passwordInput)
                                    },
                                    onSignupClick = { onNavigateToSignup(selectedRole) }
                                )
                            }
                        }
                    } else {
                        // Mobile Portrait: Structured 3-Column Stack with Numbered Sections
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Column 1: Select Role (Two Options)
                            ColumnOneRoleSelect(
                                selectedRole = selectedRole,
                                onRoleChange = { role ->
                                    selectedRole = role
                                    if (role == "farmer") {
                                        userIdInput = "farmer_anil"
                                        passwordInput = "farm123"
                                    } else {
                                        userIdInput = "buyer_fresh"
                                        passwordInput = "buyer123"
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color(0xFFE2E8F0)))

                            // Column 2: User ID
                            ColumnTwoUserId(
                                selectedRole = selectedRole,
                                userIdInput = userIdInput,
                                onUserIdChange = { userIdInput = it },
                                activeThemeColor = activeThemeColor,
                                farmers = farmers,
                                buyers = buyers
                            )

                            Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color(0xFFE2E8F0)))

                            // Column 3: Password
                            ColumnThreePassword(
                                passwordInput = passwordInput,
                                onPasswordChange = { passwordInput = it },
                                isPasswordVisible = isPasswordVisible,
                                onToggleVisibility = { isPasswordVisible = !isPasswordVisible },
                                activeThemeColor = activeThemeColor,
                                onSubmit = {
                                    onThreeColumnLogin(selectedRole, userIdInput, passwordInput)
                                },
                                onSignupClick = { onNavigateToSignup(selectedRole) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= REFERENCE DEMO ACCOUNTS & ONE-TAP SWITCHER =================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reference_demo_accounts_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Credentials Reference & One-Tap Switcher",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FarmerDarkGreen
                            )
                            Text(
                                text = "Tap '1-Tap Sign In' to instantly enter, or 'Autofill' to test the gateway.",
                                fontSize = 11.sp,
                                color = FarmerSlateGray
                            )
                        }

                        Surface(
                            color = FarmerContainerLight,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Demo Pass: pass123",
                                color = FarmerDarkGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Section A: Farmer Reference Accounts
                    Text(
                        text = "🌾 Farmer Reference Accounts (Earthy Tractor Palette):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = FarmerDarkGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    farmers.take(3).forEach { farmer ->
                        val username = farmer.username.ifEmpty { "farmer_${farmer.name.split(" ").first().lowercase()}" }
                        val pass = farmer.password.ifEmpty { "farm123" }

                        ReferenceAccountRow(
                            roleIcon = "🌾",
                            name = farmer.name,
                            category = "${farmer.businessType} • ${farmer.location.split(",").first()}",
                            userId = username,
                            password = pass,
                            roleColor = FarmerDarkGreen,
                            onAutofill = {
                                selectedRole = "farmer"
                                userIdInput = username
                                passwordInput = pass
                            },
                            onOneTapLogin = { onQuickLogin(farmer) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Section B: Buyer Reference Accounts
                    Text(
                        text = "🛒 Commercial Buyer Reference Accounts (Trade Palette):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = BuyerNavy
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    buyers.take(3).forEach { buyer ->
                        val username = buyer.username.ifEmpty { "buyer_${buyer.name.split(" ").first().lowercase()}" }
                        val pass = buyer.password.ifEmpty { "buyer123" }

                        ReferenceAccountRow(
                            roleIcon = "🛒",
                            name = buyer.name,
                            category = "${buyer.businessType} • ${buyer.location.split(",").first()}",
                            userId = username,
                            password = pass,
                            roleColor = BuyerNavy,
                            onAutofill = {
                                selectedRole = "buyer"
                                userIdInput = username
                                passwordInput = pass
                            },
                            onOneTapLogin = { onQuickLogin(buyer) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom Actions: Register & Reset Demo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigateToSignup(selectedRole) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("signup_gateway_btn")
                ) {
                    Text("Register New Account", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onResetDemo,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("reset_demo_btn")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Demo Data", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ---------------- COLUMN 1: ROLE SELECTION (CLICK TO EXPAND SUB-OPTIONS) ----------------
@Composable
private fun ColumnOneRoleSelect(
    selectedRole: String,
    onRoleChange: (String) -> Unit
) {
    val isFarmer = selectedRole == "farmer"
    var isExpanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "1. SELECT ROLE",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isFarmer) FarmerDarkGreen else BuyerNavy,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "Click to open sub-options:",
            fontSize = 11.sp,
            color = FarmerSlateGray,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Main Role Selector Trigger (Click to open sub options)
        Surface(
            color = if (isFarmer) FarmerContainerLight else Color(0xFFEFF6FF),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = if (isFarmer) FarmerDarkGreen else BuyerNavy,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { isExpanded = !isExpanded }
                .testTag("select_role_trigger_btn")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isFarmer) FarmerDarkGreen else BuyerNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isFarmer) "🌾" else "🛒", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isFarmer) "🌾 Farmer Portal" else "🛒 Buyer Portal",
                            fontWeight = FontWeight.Bold,
                            color = if (isFarmer) FarmerDarkGreen else BuyerNavy,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (isExpanded) "Sub-options open (tap to hide)" else "Tap to choose Farmer / Buyer",
                            fontSize = 10.sp,
                            color = FarmerSlateGray
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse sub-options" else "Expand sub-options",
                    tint = if (isFarmer) FarmerDarkGreen else BuyerNavy,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Sub Options Container
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Sub-Options (Select one):",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmerSlateGray,
                    modifier = Modifier.padding(start = 2.dp)
                )

                // Sub-Option 1: Farmer
                Surface(
                    color = if (isFarmer) Color.White else Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isFarmer) 2.dp else 1.dp,
                            color = if (isFarmer) FarmerDarkGreen else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onRoleChange("farmer")
                        }
                        .testTag("sub_option_farmer")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isFarmer) FarmerDarkGreen else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Agriculture,
                                    contentDescription = null,
                                    tint = if (isFarmer) Color.White else FarmerDarkGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Farmer",
                                        fontWeight = FontWeight.Bold,
                                        color = FarmerDarkGreen,
                                        fontSize = 13.sp
                                    )
                                    if (isFarmer) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = FarmerContainerLight,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "SELECTED",
                                                color = FarmerDarkGreen,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "List crops with private reserve",
                                    fontSize = 10.sp,
                                    color = FarmerPineGreen
                                )
                            }
                        }

                        if (isFarmer) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = FarmerDarkGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Sub-Option 2: Buyer
                Surface(
                    color = if (!isFarmer) Color.White else Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (!isFarmer) 2.dp else 1.dp,
                            color = if (!isFarmer) BuyerNavy else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onRoleChange("buyer")
                        }
                        .testTag("sub_option_buyer")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (!isFarmer) BuyerNavy else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = if (!isFarmer) Color.White else BuyerNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Buyer",
                                        fontWeight = FontWeight.Bold,
                                        color = BuyerNavy,
                                        fontSize = 13.sp
                                    )
                                    if (!isFarmer) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFFDBEAFE),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "SELECTED",
                                                color = BuyerNavy,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Commercial bidding & order sourcing",
                                    fontSize = 10.sp,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        if (!isFarmer) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = BuyerNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- COLUMN 2: USER ID ----------------
@Composable
private fun ColumnTwoUserId(
    selectedRole: String,
    userIdInput: String,
    onUserIdChange: (String) -> Unit,
    activeThemeColor: Color,
    farmers: List<UserEntity>,
    buyers: List<UserEntity>
) {
    val isFarmer = selectedRole == "farmer"

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "2. USER ID",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = activeThemeColor,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "Username, contact phone, or select:",
            fontSize = 11.sp,
            color = FarmerSlateGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = userIdInput,
            onValueChange = onUserIdChange,
            placeholder = { Text(if (isFarmer) "e.g. farmer_anil" else "e.g. buyer_fresh") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = activeThemeColor,
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("user_id_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = activeThemeColor,
                focusedLabelColor = activeThemeColor
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick user select chips
        Text(
            text = "Quick Select ID:",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = FarmerSlateGray
        )
        Spacer(modifier = Modifier.height(4.dp))

        val pool = if (isFarmer) farmers.take(3) else buyers.take(3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            pool.forEach { u ->
                val uId = u.username.ifEmpty { if (isFarmer) "farmer_${u.name.split(" ").first().lowercase()}" else "buyer_${u.name.split(" ").first().lowercase()}" }
                Surface(
                    color = if (userIdInput == uId) activeThemeColor.copy(alpha = 0.15f) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onUserIdChange(uId) }
                ) {
                    Text(
                        text = u.name.split(" ").first(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (userIdInput == uId) activeThemeColor else Color(0xFF334155),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

// ---------------- COLUMN 3: PASSWORD & SIGN IN ----------------
@Composable
private fun ColumnThreePassword(
    passwordInput: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    activeThemeColor: Color,
    onSubmit: () -> Unit,
    onSignupClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "3. PASSWORD",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = activeThemeColor,
            letterSpacing = 0.5.sp
        )
        Text(
            text = "Enter account password (or demo PIN):",
            fontSize = 11.sp,
            color = FarmerSlateGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = passwordInput,
            onValueChange = onPasswordChange,
            placeholder = { Text("Password (e.g. farm123)") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = activeThemeColor,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility",
                        tint = FarmerSlateGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = activeThemeColor,
                focusedLabelColor = activeThemeColor
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = activeThemeColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("gateway_signin_btn")
        ) {
            Text("Sign In to Portal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

// ---------------- REFERENCE DEMO ACCOUNT ROW COMPONENT ----------------
@Composable
private fun ReferenceAccountRow(
    roleIcon: String,
    name: String,
    category: String,
    userId: String,
    password: String,
    roleColor: Color,
    onAutofill: () -> Unit,
    onOneTapLogin: () -> Unit
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = roleIcon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = roleColor
                    )
                    Text(
                        text = "ID: $userId • Pass: $password",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF475569)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Autofill into 3 columns
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .border(1.dp, roleColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .clickable(onClick = onAutofill)
                ) {
                    Text(
                        text = "Autofill",
                        color = roleColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }

                // 1-Tap Login
                Surface(
                    color = roleColor,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable(onClick = onOneTapLogin)
                ) {
                    Text(
                        text = "1-Tap Sign In",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
