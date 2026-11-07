package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NetworkStatusInfo
import com.example.data.model.SimCardInfo
import com.example.data.telephony.SystemSettingsHelper
import com.example.ui.AppLanguage
import com.example.ui.AppNavDestination
import com.example.ui.components.SignalGaugeCard
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SignalRed
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedDark
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun HomeScreen(
    status: NetworkStatusInfo,
    simCards: List<SimCardInfo>,
    selectedSimIndex: Int,
    language: AppLanguage,
    onNavigate: (AppNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 1. Live Signal Gauge & Overview
        item {
            SignalGaugeCard(
                status = status,
                language = language
            )
        }

        // 2. Quick Action Banner to Network Modes (The most important feature)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigate(AppNavDestination.MODES) }
                    .border(1.dp, SpiderRed.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .testTag("goto_modes_banner"),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSurfaceElevated
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(listOf(SpiderRed, Color(0xFF6B0008)))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "التحكم في أوضاع الشبكة (10 أوضاع)" else "Network Mode Controller (10 Modes)",
                                color = SpiderWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "تثبيت 4G LTE Only أو 5G Only والتبديل التلقائي" else "Force 4G LTE, 5G Only, or Auto Modes",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = SpiderRedLight,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // 3. Grid of Comprehensive Network Telemetry Cards
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "تفاصيل وحالة الاتصال الحالية" else "Current Network Telemetry",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1: Generation & Data Connection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TelemetryItemCard(
                        icon = Icons.Default.CellTower,
                        title = if (language == AppLanguage.ARABIC) "جيل الشبكة" else "Generation",
                        value = status.generation.label,
                        subValue = status.networkTypeName,
                        accentColor = SpiderRed,
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryItemCard(
                        icon = Icons.Default.Public,
                        title = if (language == AppLanguage.ARABIC) "بيانات الجوال" else "Mobile Data",
                        value = if (status.isDataConnected) (if (language == AppLanguage.ARABIC) "متصلة بالإنترنت" else "Connected")
                        else (if (language == AppLanguage.ARABIC) "غير متصلة" else "Disconnected"),
                        subValue = if (status.isRoaming) (if (language == AppLanguage.ARABIC) "في وضع التجوال" else "Roaming") else (if (language == AppLanguage.ARABIC) "شبكة محلية" else "Home Network"),
                        accentColor = if (status.isDataConnected) SignalGreen else SignalOrange,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Data Tech & Voice Tech
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TelemetryItemCard(
                        icon = Icons.Default.Speed,
                        title = if (language == AppLanguage.ARABIC) "تقنية البيانات" else "Data Technology",
                        value = status.dataNetworkType,
                        subValue = "Data Protocol",
                        accentColor = Color(0xFF29B6F6),
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryItemCard(
                        icon = Icons.Default.PhoneAndroid,
                        title = if (language == AppLanguage.ARABIC) "تقنية المكالمات" else "Voice Technology",
                        value = status.voiceNetworkType,
                        subValue = "Voice Channel",
                        accentColor = Color(0xFFAB47BC),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Registration Status & SIM Slot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TelemetryItemCard(
                        icon = Icons.Default.NetworkCell,
                        title = if (language == AppLanguage.ARABIC) "تسجيل الشبكة" else "Registration",
                        value = status.registrationState,
                        subValue = "Tower State",
                        accentColor = if (status.registrationState.contains("مسجل") || status.registrationState.contains("Service")) SignalGreen else SignalRed,
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryItemCard(
                        icon = Icons.Default.SimCard,
                        title = if (language == AppLanguage.ARABIC) "منفذ الشريحة" else "SIM Slot",
                        value = "SIM ${status.simSlot}",
                        subValue = status.simState,
                        accentColor = Color(0xFFFFB300),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 4: MCC/MNC & IP Address
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TelemetryItemCard(
                        icon = Icons.Default.Radio,
                        title = if (language == AppLanguage.ARABIC) "كود MCC - MNC" else "MCC / MNC Code",
                        value = status.mccMnc,
                        subValue = "Country / Network",
                        accentColor = Color(0xFF26A69A),
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryItemCard(
                        icon = Icons.Default.Router,
                        title = if (language == AppLanguage.ARABIC) "عنوان IP" else "Device IP Address",
                        value = status.ipAddress ?: if (language == AppLanguage.ARABIC) "غير متاح" else "Unavailable",
                        subValue = "Cellular IPv4",
                        accentColor = Color(0xFF7E57C2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Direct Engineering Shortcuts Section
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "اختصارات الضبط وأدوات النظام" else "System Settings Shortcuts",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // RadioInfo Official Button
                Button(
                    onClick = {
                        val res = SystemSettingsHelper.openRadioInfo(context)
                        if (res == SystemSettingsHelper.LaunchResult.NOT_FOUND) {
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.ARABIC) "تعذر فتح RadioInfo مباشرة، يمكنك استخدام كود الاتصال *#*#4636#*#*" else "RadioInfo activity not found on this vendor ROM",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpiderRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("home_open_radioinfo_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "فتح شاشة RadioInfo الهندسية الرسمية (*#*#4636#*#*)" else "Open Official RadioInfo Engineering Menu",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // System Mobile Network Settings Button
                OutlinedButton(
                    onClick = {
                        val currentSub = simCards.getOrNull(selectedSimIndex)
                        SystemSettingsHelper.openMobileNetworkSettings(context, currentSub?.subscriptionId)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("home_open_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = SpiderWhite,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إعدادات شبكة الجوال في نظام Android" else "Android Mobile Network Settings",
                        color = SpiderWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TelemetryItemCard(
    icon: ImageVector,
    title: String,
    value: String,
    subValue: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, DarkOutline, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = TextTertiary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = subValue,
                color = TextSecondary,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
