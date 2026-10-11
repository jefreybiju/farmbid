package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FarmerContainerLight
import com.example.ui.theme.FarmerDarkGreen
import com.example.ui.theme.FarmerEarthRed
import com.example.ui.theme.FarmerLeafGreen
import com.example.ui.theme.FarmerPineGreen
import com.example.ui.theme.FarmerRedContainer
import com.example.ui.theme.FarmerSlateGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale

/**
 * Real-time Countdown Timer Component for Live Bidding Interface.
 * Accurately ticks every second and reflects live anti-snipe urgency.
 */
@Composable
fun LiveAuctionHeroCountdownTimer(
    endsAtSeconds: Long,
    createdAtSeconds: Long = endsAtSeconds - 3600,
    modifier: Modifier = Modifier,
    onTimerExpired: () -> Unit = {}
) {
    var remainingSeconds by remember(endsAtSeconds) {
        mutableLongStateOf(maxOf(0L, endsAtSeconds - (System.currentTimeMillis() / 1000)))
    }

    LaunchedEffect(endsAtSeconds) {
        while (isActive) {
            val nowSeconds = System.currentTimeMillis() / 1000
            val rem = maxOf(0L, endsAtSeconds - nowSeconds)
            remainingSeconds = rem
            if (rem <= 0L) {
                onTimerExpired()
                break
            }
            delay(1000L)
        }
    }

    val isExpired = remainingSeconds <= 0
    val isAntiSnipeWindow = remainingSeconds in 1..120 // Critical last 2 minutes
    val isExpiringSoon = remainingSeconds in 121..900   // Warning last 15 minutes

    // Pulsing transition for critical anti-snipe urgency
    val infiniteTransition = rememberInfiniteTransition(label = "timer_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val blinkColon by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colon_blink"
    )

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    // Elapsed percentage
    val totalDuration = maxOf(1L, endsAtSeconds - createdAtSeconds)
    val elapsed = maxOf(0L, totalDuration - remainingSeconds)
    val progress = (elapsed.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)

    val urgencyBgColor by animateColorAsState(
        targetValue = when {
            isExpired -> Color(0xFFF3F4F6)
            isAntiSnipeWindow -> FarmerRedContainer
            isExpiringSoon -> Color(0xFFFFFBEB)
            else -> FarmerContainerLight
        },
        label = "bg_color"
    )

    val urgencyBorderColor by animateColorAsState(
        targetValue = when {
            isExpired -> Color(0xFFE5E7EB)
            isAntiSnipeWindow -> FarmerEarthRed
            isExpiringSoon -> Color(0xFFD97706)
            else -> FarmerLeafGreen.copy(alpha = 0.5f)
        },
        label = "border_color"
    )

    val urgencyTextColor = when {
        isExpired -> Color(0xFF4B5563)
        isAntiSnipeWindow -> FarmerEarthRed
        isExpiringSoon -> Color(0xFFB45309)
        else -> FarmerDarkGreen
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isAntiSnipeWindow) 2.dp else 1.dp,
                color = if (isAntiSnipeWindow) urgencyBorderColor.copy(alpha = pulseAlpha) else urgencyBorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("hero_countdown_timer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            urgencyBgColor.copy(alpha = 0.6f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Header: Status title and Live pulse
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isAntiSnipeWindow) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = FarmerEarthRed,
                            modifier = Modifier
                                .size(20.dp)
                                .alpha(pulseAlpha)
                        )
                    } else if (isExpiringSoon) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (isExpired) {
                        Icon(
                            imageVector = Icons.Default.TimerOff,
                            contentDescription = null,
                            tint = Color(0xFF4B5563),
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = FarmerLeafGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = when {
                            isExpired -> "AUCTION CONCLUDED"
                            isAntiSnipeWindow -> "FINAL 2 MINUTES • HIGH URGENCY"
                            isExpiringSoon -> "ENDING SOON • ACTIVE BIDDING"
                            else -> "LIVE AUCTION COUNTDOWN"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = urgencyTextColor
                    )
                }

                Surface(
                    color = when {
                        isExpired -> Color(0xFFE5E7EB)
                        isAntiSnipeWindow -> FarmerEarthRed
                        isExpiringSoon -> Color(0xFFF59E0B)
                        else -> FarmerLeafGreen
                    },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isExpired) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .alpha(if (isAntiSnipeWindow) pulseAlpha else 1f)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = if (isExpired) "CLOSED" else "REAL-TIME",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Large Digital Countdown Segments
            if (isExpired) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bidding Time Has Expired",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hours Box
                    TimeSegmentBox(
                        value = String.format(Locale.US, "%02d", hours),
                        label = "HOURS",
                        textColor = urgencyTextColor
                    )

                    Text(
                        text = ":",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = urgencyTextColor.copy(alpha = blinkColon),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    // Minutes Box
                    TimeSegmentBox(
                        value = String.format(Locale.US, "%02d", minutes),
                        label = "MINUTES",
                        textColor = urgencyTextColor
                    )

                    Text(
                        text = ":",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = urgencyTextColor.copy(alpha = blinkColon),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    // Seconds Box
                    TimeSegmentBox(
                        value = String.format(Locale.US, "%02d", seconds),
                        label = "SECONDS",
                        textColor = urgencyTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    isExpired -> Color(0xFF9CA3AF)
                    isAntiSnipeWindow -> FarmerEarthRed
                    isExpiringSoon -> Color(0xFFD97706)
                    else -> FarmerLeafGreen
                },
                trackColor = Color(0xFFE2E8F0)
            )

            // Anti-Sniping Policy Explainer
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = if (isAntiSnipeWindow) FarmerRedContainer else Color(0xFFF8FAFC),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (isAntiSnipeWindow) FarmerEarthRed else FarmerPineGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAntiSnipeWindow)
                            "Anti-Snipe Protection Active: Bids placed now automatically extend the auction by 120s!"
                        else
                            "Anti-Snipe Guarantee: Bids submitted in the final 2 minutes extend the clock by 2 minutes.",
                        fontSize = 11.sp,
                        color = if (isAntiSnipeWindow) FarmerEarthRed else FarmerSlateGray,
                        fontWeight = if (isAntiSnipeWindow) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeSegmentBox(
    value: String,
    label: String,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 2.dp,
            modifier = Modifier
                .width(66.dp)
                .height(56.dp)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = FarmerSlateGray,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Compact Real-time Countdown Pill for Cards and List Headers.
 */
@Composable
fun CompactAuctionCountdownTimer(
    endsAtSeconds: Long,
    modifier: Modifier = Modifier,
    onTimerExpired: () -> Unit = {}
) {
    var remainingSeconds by remember(endsAtSeconds) {
        mutableLongStateOf(maxOf(0L, endsAtSeconds - (System.currentTimeMillis() / 1000)))
    }

    LaunchedEffect(endsAtSeconds) {
        while (isActive) {
            val nowSeconds = System.currentTimeMillis() / 1000
            val rem = maxOf(0L, endsAtSeconds - nowSeconds)
            remainingSeconds = rem
            if (rem <= 0L) {
                onTimerExpired()
                break
            }
            delay(1000L)
        }
    }

    val isExpired = remainingSeconds <= 0
    val isAntiSnipe = remainingSeconds in 1..120

    val infiniteTransition = rememberInfiniteTransition(label = "compact_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "compact_alpha"
    )

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    val timeString = if (hours > 0) {
        String.format(Locale.US, "%dh %02dm %02ds", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02dm %02ds", minutes, seconds)
    }

    Surface(
        color = when {
            isExpired -> Color(0xFFF3F4F6)
            isAntiSnipe -> FarmerRedContainer.copy(alpha = pulseAlpha)
            else -> FarmerContainerLight
        },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("compact_countdown_timer")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isExpired) "Closed" else if (isAntiSnipe) "🔥 $timeString" else "⏳ $timeString",
                color = when {
                    isExpired -> Color(0xFF6B7280)
                    isAntiSnipe -> FarmerEarthRed
                    else -> FarmerDarkGreen
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
