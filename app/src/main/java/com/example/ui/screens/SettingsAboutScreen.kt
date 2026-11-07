package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedDark
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun SettingsAboutScreen(
    currentLanguage: AppLanguage,
    autoRefreshSeconds: Int,
    onSetLanguage: (AppLanguage) -> Unit,
    onSetAutoRefresh: (Int) -> Unit,
    onManualRefresh: () -> Unit,
    onRequestPermissions: () -> Unit,
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

        // Section 1: Language Selection (عربي / English)
        item {
            Text(
                text = if (currentLanguage == AppLanguage.ARABIC) "إعدادات اللغة (Language)" else "Language Settings",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkOutline, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Arabic Radio
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSetLanguage(AppLanguage.ARABIC) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == AppLanguage.ARABIC,
                            onClick = { onSetLanguage(AppLanguage.ARABIC) },
                            colors = RadioButtonDefaults.colors(selectedColor = SpiderRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "اللغة العربية (Arabic)",
                                color = SpiderWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "واجهة عربية كاملة مع دعم اتجاه اليمين إلى اليسار (RTL)",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // English Radio
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSetLanguage(AppLanguage.ENGLISH) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == AppLanguage.ENGLISH,
                            onClick = { onSetLanguage(AppLanguage.ENGLISH) },
                            colors = RadioButtonDefaults.colors(selectedColor = SpiderRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "English (الإنجليزية)",
                                color = SpiderWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Full English interface with international telecom terms",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Auto Refresh Rate
        item {
            Text(
                text = if (currentLanguage == AppLanguage.ARABIC) "معدل تحديث بيانات الشبكة والإشارة" else "Network Refresh Interval",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkOutline, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val intervals = listOf(
                        2 to if (currentLanguage == AppLanguage.ARABIC) "كل ثانيتين (فائق السرعة)" else "Every 2 Seconds (High Rate)",
                        3 to if (currentLanguage == AppLanguage.ARABIC) "كل 3 ثوانٍ (موصى به - متوازن)" else "Every 3 Seconds (Recommended)",
                        5 to if (currentLanguage == AppLanguage.ARABIC) "كل 5 ثوانٍ (اقتصادي للبطارية)" else "Every 5 Seconds (Battery Saver)",
                        10 to if (currentLanguage == AppLanguage.ARABIC) "كل 10 ثوانٍ" else "Every 10 Seconds",
                        0 to if (currentLanguage == AppLanguage.ARABIC) "يدوي فقط (عند الضغط على التحديث)" else "Manual Only (On demand)"
                    )

                    intervals.forEach { (seconds, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onSetAutoRefresh(seconds) }
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = autoRefreshSeconds == seconds,
                                onClick = { onSetAutoRefresh(seconds) },
                                colors = RadioButtonDefaults.colors(selectedColor = SpiderRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                color = if (autoRefreshSeconds == seconds) SpiderWhite else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (autoRefreshSeconds == seconds) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Theme Info & Permissions Check
        item {
            Text(
                text = if (currentLanguage == AppLanguage.ARABIC) "المظهر والصلاحيات" else "Theme & Permissions",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkOutline, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = SpiderRedLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "السمة الليلية (Dark Cyber Theme)" else "Dark Cyber Theme",
                                color = SpiderWhite,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "مفعلة أساسياً" else "Enabled",
                            color = SpiderRedLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onRequestPermissions,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SpiderWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "طلب / فحص أذونات الهاتف والموقع" else "Request / Audit App Permissions",
                            color = SpiderWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Section 4: About Application (Required Developer Credits)
        item {
            Text(
                text = if (currentLanguage == AppLanguage.ARABIC) "حول التطبيق والمطور" else "About Application & Developer",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpiderRed.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Developer Credit Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(SpiderRed.copy(alpha = 0.2f), DarkSurfaceElevated)
                                )
                            )
                            .border(1.dp, SpiderRedLight.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "SPIDER NETWORK MODE",
                                color = SpiderRed,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "تطوير المبرمج باسل الشرعاوي",
                                color = SpiderWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Developer: Basel Al-Sharawi",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الإصدار الرسمي: v1.0.0 (Release 2026)",
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC)
                            "تطبيق SPIDER NETWORK MODE هو منظومة متقدمة مصممة لهواتف أندرويد لتوفير أقصى قدر من معلومات شبكات الجوال وإدارة أوضاع الاتصال (2G, 3G, 4G LTE, 5G) والوصول السريع إلى شاشة RadioInfo الهندسية الرسمية بدون روت وبمصداقية برمجية كاملة."
                        else
                            "SPIDER NETWORK MODE is an Android network information and network settings management application designed to provide convenient access to available network modes and mobile network information.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val shareText = "SPIDER NETWORK MODE\nتطوير المبرمج باسل الشرعاوي\nمراقب ومعالج أوضاع الشبكة الاحترافي"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Share", shareText))
                            Toast.makeText(
                                context,
                                if (currentLanguage == AppLanguage.ARABIC) "تم نسخ معلومات التطبيق والمطور" else "App details copied",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp))
                            .testTag("copy_about_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = SpiderWhite,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "نسخ بيانات التطبيق والمطور" else "Copy Developer Credit",
                            color = SpiderWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
