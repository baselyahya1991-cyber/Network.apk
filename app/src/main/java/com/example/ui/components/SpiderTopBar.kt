package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NetworkGen
import com.example.data.model.SimCardInfo
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary

@Composable
fun SpiderTopBar(
    language: AppLanguage,
    generation: NetworkGen,
    simCards: List<SimCardInfo>,
    selectedSimIndex: Int,
    isRefreshing: Boolean,
    onSelectSim: (Int) -> Unit,
    onToggleLanguage: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by if (isRefreshing) {
        val transition = rememberInfiniteTransition(label = "spin")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "spinRot"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    Surface(
        color = DarkSurface,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Main App Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(SpiderRed, Color(0xFF6B0008))
                                )
                            )
                            .border(1.dp, SpiderRedLight, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CellTower,
                            contentDescription = "Spider Network Icon",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SPIDER",
                                color = SpiderRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "NETWORK",
                                color = SpiderWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Live Generation Badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (generation != NetworkGen.UNKNOWN) SignalGreen else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "مباشر • " + generation.label else "LIVE • " + generation.label,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Action Buttons: Language & Refresh
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Language Switcher Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkOutline, RoundedCornerShape(8.dp))
                            .clickable { onToggleLanguage() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("language_toggle_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = SpiderRedLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "EN" else "عربي",
                                color = SpiderWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Manual Refresh Button
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkOutline, CircleShape)
                            .testTag("refresh_data_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = if (isRefreshing) SpiderRed else SpiderWhite,
                            modifier = Modifier
                                .size(18.dp)
                                .rotate(rotation)
                        )
                    }
                }
            }

            // SIM Selector Tabs (if SIMs are detected)
            if (simCards.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    simCards.forEachIndexed { index, sim ->
                        val isSelected = index == selectedSimIndex
                        val isDefaultData = sim.isDefaultData

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) SpiderRed.copy(alpha = 0.2f) else DarkSurfaceElevated
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) SpiderRed else DarkOutline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectSim(index) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("sim_tab_$index")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SimCard,
                                        contentDescription = "SIM",
                                        tint = if (isSelected) SpiderRed else TextSecondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "SIM ${sim.slotIndex + 1}: ${sim.carrierName.take(12)}",
                                            color = if (isSelected) SpiderWhite else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (isDefaultData) {
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "بيانات الجوال الافتراضية" else "Default Data SIM",
                                                color = SpiderRedLight,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(SpiderRed)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
