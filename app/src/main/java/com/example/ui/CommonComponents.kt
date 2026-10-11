package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen

fun produceEmoji(name: String): String {
    val lower = name.lowercase()
    return when {
        lower.contains("tomato") -> "🍅"
        lower.contains("banana") -> "🍌"
        lower.contains("pineapple") -> "🍍"
        lower.contains("okra") || lower.contains("finger") -> "🌿"
        lower.contains("chilli") || lower.contains("chili") || lower.contains("pepper") -> "🌶️"
        lower.contains("mango") -> "🥭"
        lower.contains("potato") -> "🥔"
        lower.contains("onion") -> "🧅"
        lower.contains("carrot") -> "🥕"
        lower.contains("brinjal") || lower.contains("eggplant") -> "🍆"
        lower.contains("coconut") -> "🥥"
        lower.contains("cabbage") -> "🥬"
        lower.contains("ginger") -> "🫚"
        lower.contains("garlic") -> "🧄"
        else -> "🌾"
    }
}

fun formatMoney(amount: Double): String {
    return "₹" + String.format(java.util.Locale.US, "%.2f", amount)
}

fun formatTimeRemaining(endsAtSeconds: Long): String {
    val nowSeconds = System.currentTimeMillis() / 1000
    val diff = endsAtSeconds - nowSeconds
    if (diff <= 0) return "Closed"
    val hours = diff / 3600
    val minutes = (diff % 3600) / 60
    val seconds = diff % 60
    return if (hours > 0) {
        String.format(java.util.Locale.US, "%dh %02dm", hours, minutes)
    } else {
        String.format(java.util.Locale.US, "%dm %02ds", minutes, seconds)
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (label, bg, fg) = when (status) {
        "open" -> Triple("LIVE AUCTION", FarmerLeafGreen.copy(alpha = 0.15f), FarmerDarkGreen)
        "pending_confirmation" -> Triple("AWAITING CONFIRMATION", Color(0xFFFEF3C7), Color(0xFFB45309))
        "sold" -> Triple("SOLD", Color(0xFFD1FAE5), Color(0xFF047857))
        "unsold" -> Triple("RESERVE NOT MET", Color(0xFFF3F4F6), Color(0xFF4B5563))
        "declined" -> Triple("DECLINED", FarmerEarthRed.copy(alpha = 0.15f), FarmerEarthRed)
        "confirmed" -> Triple("ORDER CONFIRMED", Color(0xFFE0E7FF), Color(0xFF3730A3))
        "dispatched" -> Triple("DISPATCHED", Color(0xFFFEF3C7), Color(0xFF92400E))
        "delivered" -> Triple("DELIVERED", Color(0xFFD1FAE5), Color(0xFF065F46))
        "disputed" -> Triple("DISPUTED", FarmerEarthRed.copy(alpha = 0.15f), FarmerEarthRed)
        else -> Triple(status.uppercase(), Color(0xFFE5E7EB), Color(0xFF374151))
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun VerifiedBadge(isVerified: Boolean, modifier: Modifier = Modifier) {
    if (isVerified) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified",
                tint = FarmerLeafGreen,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "Verified",
                color = FarmerDarkGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFFBEB))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Unverified",
                tint = Color(0xFFD97706),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "Pending Verification",
                color = Color(0xFF92400E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EmojiProduceBox(emoji: String, modifier: Modifier = Modifier, background: Color = Color(0xFFE8EFEA)) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 36.sp)
    }
}
