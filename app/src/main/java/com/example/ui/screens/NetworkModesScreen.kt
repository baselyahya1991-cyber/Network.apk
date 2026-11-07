package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceCompatibilityInfo
import com.example.data.model.NetworkGen
import com.example.data.model.NetworkModeItem
import com.example.data.model.NetworkStatusInfo
import com.example.data.telephony.SystemSettingsHelper
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedDark
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun NetworkModesScreen(
    modes: List<NetworkModeItem>,
    currentStatus: NetworkStatusInfo,
    compatibility: DeviceCompatibilityInfo,
    language: AppLanguage,
    onSelectMode: (NetworkModeItem) -> Unit,
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

        // Header Title & Current Detected Mode
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpiderRed.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(SpiderRed.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = SpiderRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "أوضاع الشبكة المدعومة" else "Supported Network Modes",
                                    color = SpiderWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "10 أوضاع معتمدة في هندسة الاتصالات" else "10 Telecom Standard Modes",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Current active mode indicator
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpiderRedDark)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = currentStatus.generation.label,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Educational notice regarding Android security & RadioInfo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SpiderRedLight,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC)
                                    "تطبيق حقيقي: يتم فحص صلاحيات النظام وتطبيق الوضع برمجياً. إذا منع أندرويد التعديل المباشر (لحماية المودم)، يتم فتح شاشة RadioInfo الهندسية الرسمية لتثبيت الوضع يدوياً بنقرة واحدة دون الحاجة إلى روت أو كمبيوتر."
                                else
                                    "Real execution: Applies mode directly when permissions permit. If standard Android blocks direct radio alteration, the official RadioInfo testing menu is opened for rootless 1-click lock.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Direct Shortcut Button to RadioInfo
        item {
            Button(
                onClick = {
                    val res = SystemSettingsHelper.openRadioInfo(context)
                    if (res == SystemSettingsHelper.LaunchResult.NOT_FOUND) {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تعذر فتح RadioInfo مباشرة، استخدم كود الاتصال أدناه" else "RadioInfo unavailable directly. Use dialer code.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpiderRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("modes_open_radioinfo_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Launch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ARABIC) "فتح شاشة RadioInfo لاختيار أي وضع مباشرة" else "Open RadioInfo Menu to Select Any Mode",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // List of all 10 Network Modes
        items(modes, key = { it.id }) { mode ->
            NetworkModeCard(
                mode = mode,
                isActive = isModeActive(mode, currentStatus),
                language = language,
                onApply = { onSelectMode(mode) }
            )
        }

        // Manufacturer Specific Tools Section
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "أدوات متقدمة حسب نوع المعالج والشركة" else "Manufacturer & Chipset Tools",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 10.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Samsung Band Selection
                if (compatibility.manufacturer.contains("Samsung", ignoreCase = true)) {
                    VendorToolButton(
                        title = if (language == AppLanguage.ARABIC) "إعدادات شبكة سامسونج المخفية" else "Samsung Hidden Network Setting",
                        icon = Icons.Default.Build,
                        onClick = {
                            val res = SystemSettingsHelper.openSamsungNetworkSettings(context)
                            if (res == SystemSettingsHelper.LaunchResult.NOT_FOUND) {
                                SystemSettingsHelper.openDialerWithCode(context, "*#2263#")
                            }
                        }
                    )
                }

                // MediaTek Engineer Mode
                if (compatibility.processorChipset.contains("MediaTek", ignoreCase = true)) {
                    VendorToolButton(
                        title = if (language == AppLanguage.ARABIC) "وضع المهندس لمعالجات ميديا تيك (MTK)" else "MediaTek Engineer Mode",
                        icon = Icons.Default.Build,
                        onClick = {
                            val res = SystemSettingsHelper.openMtkEngineerMode(context)
                            if (res == SystemSettingsHelper.LaunchResult.NOT_FOUND) {
                                SystemSettingsHelper.openDialerWithCode(context, "*#*#3646633#*#*")
                            }
                        }
                    )
                }

                // Dialing Code Launcher
                VendorToolButton(
                    title = if (language == AppLanguage.ARABIC) "طلب كود الهاتف الهندسي: *#*#4636#*#*" else "Dial Engineering Code: *#*#4636#*#*",
                    icon = Icons.Default.Phone,
                    onClick = {
                        val code = "*#*#4636#*#*"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Code", code))
                        SystemSettingsHelper.openDialerWithCode(context, code)
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تم نسخ الكود $code وفتح لوحة الاتصال" else "Copied $code and opened dialer",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                // APN Settings
                VendorToolButton(
                    title = if (language == AppLanguage.ARABIC) "إعدادات نقاط الوصول (APN)" else "Access Point Names (APN) Settings",
                    icon = Icons.Default.Settings,
                    onClick = {
                        SystemSettingsHelper.openApnSettings(context)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun NetworkModeCard(
    mode: NetworkModeItem,
    isActive: Boolean,
    language: AppLanguage,
    onApply: () -> Unit
) {
    val genColor = when (mode.generation) {
        NetworkGen.G5 -> Color(0xFFFF1744)
        NetworkGen.G4 -> Color(0xFF2979FF)
        NetworkGen.G3 -> Color(0xFFFF9100)
        NetworkGen.G2 -> Color(0xFF8D6E63)
        NetworkGen.UNKNOWN -> Color(0xFF607D8B)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isActive) SpiderRed else DarkOutline,
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) DarkSurfaceElevated else DarkSurface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Generation Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(genColor.copy(alpha = 0.2f))
                            .border(1.dp, genColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = mode.generation.label,
                            color = genColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) mode.titleAr else mode.titleEn,
                            color = SpiderWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "RIL System Mode: ${mode.codeName}",
                            color = TextTertiary,
                            fontSize = 10.sp
                        )
                    }
                }

                if (isActive) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SignalGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(SignalGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "الوضع الحالي" else "Active Mode",
                                color = SignalGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation
            Text(
                text = if (language == AppLanguage.ARABIC) mode.descriptionAr else mode.descriptionEn,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Apply Button
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) DarkSurfaceElevated else SpiderRed
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        1.dp,
                        if (isActive) SpiderRedLight else Color.Transparent,
                        RoundedCornerShape(10.dp)
                    )
                    .testTag("apply_mode_${mode.id}")
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Default.CheckCircle else Icons.Default.Tune,
                    contentDescription = null,
                    tint = if (isActive) SpiderRedLight else Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isActive)
                        (if (language == AppLanguage.ARABIC) "الوضع مفعل حالياً • إعادة التطبيق" else "Currently Active • Re-apply")
                    else
                        (if (language == AppLanguage.ARABIC) "تطبيق هذا الوضع" else "Apply Network Mode"),
                    color = if (isActive) SpiderRedLight else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VendorToolButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SpiderRedLight,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = SpiderWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun isModeActive(mode: NetworkModeItem, status: NetworkStatusInfo): Boolean {
    return when (mode.generation) {
        NetworkGen.G5 -> status.generation == NetworkGen.G5
        NetworkGen.G4 -> status.generation == NetworkGen.G4
        NetworkGen.G3 -> status.generation == NetworkGen.G3
        NetworkGen.G2 -> status.generation == NetworkGen.G2
        NetworkGen.UNKNOWN -> false
    }
}
