package com.example.ui.components

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
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NetworkGen
import com.example.data.model.NetworkStatusInfo
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SignalRed
import com.example.ui.theme.SignalYellow
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedDark
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun SignalGaugeCard(
    status: NetworkStatusInfo,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val level = status.signalLevel.coerceIn(0, 4)
    val dbm = status.signalDbm
    val percentage = status.signalPercentage ?: if (level > 0) level * 25 else null

    // Determine quality color and text
    val (qualityColor, qualityTextAr, qualityTextEn) = when {
        status.isAirplaneMode -> Triple(SignalRed, "وضع الطيران", "Airplane Mode")
        level >= 4 || (dbm != null && dbm >= -80) -> Triple(SignalGreen, "إشارة ممتازة جداً", "Excellent Signal")
        level == 3 || (dbm != null && dbm >= -95) -> Triple(Color(0xFF00E5FF), "إشارة جيدة وقوية", "Good Signal")
        level == 2 || (dbm != null && dbm >= -108) -> Triple(SignalYellow, "إشارة متوسطة", "Fair Signal")
        level == 1 || (dbm != null && dbm >= -120) -> Triple(SignalOrange, "إشارة ضعيفة", "Weak Signal")
        else -> Triple(SignalRed, "لا توجد إشارة / انقطاع", "No Signal / Out of Service")
    }

    val genBadgeColor = when (status.generation) {
        NetworkGen.G5 -> Color(0xFFFF1744)
        NetworkGen.G4 -> Color(0xFF2979FF)
        NetworkGen.G3 -> Color(0xFFFF9100)
        NetworkGen.G2 -> Color(0xFF8D6E63)
        NetworkGen.UNKNOWN -> Color(0xFF607D8B)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Carrier & Generation Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "مشغل الشبكة والشريحة" else "Network Carrier",
                        color = TextTertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = status.carrierName,
                        color = SpiderWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // High-tech Generation Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(genBadgeColor, genBadgeColor.copy(alpha = 0.6f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = status.generation.label,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Signal Visualizer: Futuristic signal bars and large dBm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceElevated)
                    .border(1.dp, DarkOutline.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Futuristic 5-bar Signal Gauge
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val barHeights = listOf(14.dp, 22.dp, 30.dp, 38.dp, 46.dp)
                    for (i in 0..4) {
                        val isActive = i <= level && level > 0
                        val barColor = if (isActive) qualityColor else DarkOutline

                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .height(barHeights[i])
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isActive) {
                                        Brush.verticalGradient(
                                            listOf(qualityColor, qualityColor.copy(alpha = 0.5f))
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            listOf(DarkOutline, DarkOutline.copy(alpha = 0.3f))
                                        )
                                    }
                                )
                        )
                    }
                }

                // Big dBm & Percentage Readout
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = if (dbm != null) "$dbm" else if (level > 0) "L$level" else "--",
                            color = SpiderWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (dbm != null) "dBm" else "",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }

                    // Quality text and percent
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(qualityColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) qualityTextAr else qualityTextEn,
                            color = qualityColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (percentage != null) {
                            Text(
                                text = " • $percentage%",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub-metrics Bar: ASU, Roaming, Data, Registration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ASU Metric
                Column {
                    Text(
                        text = "ASU",
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (status.signalAsu != null) "${status.signalAsu}" else if (dbm != null) "${(dbm + 113) / 2}" else "N/A",
                        color = SpiderWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Technology
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "التقنية" else "Technology",
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = status.dataNetworkType.take(10),
                        color = SpiderWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Roaming
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "التجوال" else "Roaming",
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (status.isRoaming) (if (language == AppLanguage.ARABIC) "نشط" else "Active")
                        else (if (language == AppLanguage.ARABIC) "معطل" else "Off"),
                        color = if (status.isRoaming) SignalOrange else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Mobile Data State
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "بيانات الجوال" else "Data Status",
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (status.isDataConnected) (if (language == AppLanguage.ARABIC) "متصل" else "Connected")
                        else (if (language == AppLanguage.ARABIC) "غير متصل" else "Offline"),
                        color = if (status.isDataConnected) SignalGreen else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
