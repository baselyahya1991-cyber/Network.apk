package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceCompatibilityInfo
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SignalRed
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun CompatibilityScreen(
    compatibility: DeviceCompatibilityInfo,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Overview Header Card
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SpiderRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = null,
                                tint = SpiderRed,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "فحص توافق الجهاز والمعالج" else "Device & Chipset Compatibility",
                                color = SpiderWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${compatibility.manufacturer} ${compatibility.model}",
                                color = SpiderRedLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.ARABIC)
                            "يقوم التطبيق بفحص معماريات المعالجات (Qualcomm, MediaTek, Exynos, Tensor) وأنظمة المصنعين وتقديم تقرير دقيق للوظائف المدعومة والمقيدة."
                        else
                            "Audits processor architectures (Snapdragon, Dimensity, Exynos, Tensor) and vendor security to report supported vs restricted capabilities accurately.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Section 1: Hardware & Processor Specifications
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "مواصفات العتاد والمعالج (Hardware)" else "Hardware & Chipset Specs",
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "الشركة المصنعة (Manufacturer)" else "Manufacturer",
                        value = compatibility.manufacturer
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "العلامة التجارية (Brand)" else "Brand",
                        value = compatibility.brand
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "موديل الجهاز (Model)" else "Model",
                        value = compatibility.model
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "المعالج والشريحة (SoC / Chipset)" else "SoC Chipset Platform",
                        value = compatibility.processorChipset
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "اللوحة الأم (Motherboard)" else "Board Code",
                        value = compatibility.device
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "إصدار نظام Android" else "Android OS Version",
                        value = compatibility.androidVersion
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "مستوى تصحيح الأمان (Security Patch)" else "Security Patch Level",
                        value = compatibility.securityPatch
                    )
                }
            }
        }

        // Section 2: Capability & Feature Audit Matrix
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "مصفوفة فحص القدرات والميزات" else "Capabilities & Features Matrix",
                color = SpiderWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dual SIM Support
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "دعم الشريحتين عتادياً (Dual SIM Hardware)" else "Dual SIM Hardware Support",
                    isSupported = compatibility.isDualSimSupported,
                    supportedText = if (language == AppLanguage.ARABIC) "مدعوم عتادياً ومتاح في النظام" else "Supported & Ready",
                    unsupportedText = if (language == AppLanguage.ARABIC) "منفذ شريحة مفرد فقط" else "Single SIM Only"
                )

                // 5G Network Support
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "دعم شبكات الجيل الخامس (5G Hardware)" else "5G Radio Hardware Support",
                    isSupported = compatibility.is5GSupported,
                    supportedText = if (language == AppLanguage.ARABIC) "يدعم ترددات 5G NR" else "Supported (5G NR Ready)",
                    unsupportedText = if (language == AppLanguage.ARABIC) "مودم 4G LTE فقط" else "4G LTE Only Hardware"
                )

                // RadioInfo Engineering Menu
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "شاشة RadioInfo الهندسية الرسمية" else "Official RadioInfo Menu Shortcut",
                    isSupported = compatibility.isPhoneInfoShortcutSupported,
                    supportedText = if (language == AppLanguage.ARABIC) "متاحة وتعمل بدون روت بنقرة واحدة" else "Available Rootless via 1-Click",
                    unsupportedText = if (language == AppLanguage.ARABIC) "محظورة في واجهة هذا المصنع" else "Blocked by Manufacturer ROM"
                )

                // READ_PHONE_STATE Permission
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "إذن قراءة حالة الهاتف (READ_PHONE_STATE)" else "Phone State Permission",
                    isSupported = compatibility.readPhoneStateGranted,
                    supportedText = if (language == AppLanguage.ARABIC) "ممنوح ونشط" else "Granted",
                    unsupportedText = if (language == AppLanguage.ARABIC) "غير ممنوح (يتطلب تفعيل من الإعدادات)" else "Denied"
                )

                // ACCESS_FINE_LOCATION Permission
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "إذن قراءة الأبراج (ACCESS_FINE_LOCATION)" else "Location Permission for Cell Towers",
                    isSupported = compatibility.fineLocationGranted,
                    supportedText = if (language == AppLanguage.ARABIC) "ممنوح ونشط" else "Granted",
                    unsupportedText = if (language == AppLanguage.ARABIC) "غير ممنوح (يلزم لعرض تفاصيل البرج)" else "Denied"
                )

                // Direct Background Switching API
                CapabilityAuditItem(
                    title = if (language == AppLanguage.ARABIC) "تعديل المودم برمجياً في الخلفية بدون تدخل المستخدم" else "Direct Background Modem API Alteration",
                    isSupported = compatibility.directSwitchSupported,
                    supportedText = if (language == AppLanguage.ARABIC) "مدعوم (تطبيق نظام أو صلاحيات مشغل)" else "Supported (System / Carrier)",
                    unsupportedText = if (language == AppLanguage.ARABIC) "مقيد بأمان Android (يتطلب واجهة RadioInfo)" else "Restricted by Android OS (Use RadioInfo)"
                )
            }
        }

        // Section 3: Android Security Transparency Notice
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkOutline, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SpiderRedLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "شفافية ومصداقية المعايير التقنية" else "Technical Transparency Notice",
                            color = SpiderWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ARABIC)
                            "تلتزم منصة SPIDER NETWORK MODE بالمصداقية البرمجية التامة: لا نقوم بتزييف النتائج أو الادعاء بتغيير الشبكة دون تنفيذ حقيقي. يوفر التطبيق أقصى ما تتيحه واجهات برمجة أندرويد (APIs) الرسمية واختصارات الهندسة المعتمدة عالمياً."
                        else
                            "SPIDER NETWORK MODE operates on complete technical honesty: no fake simulations or mock toggles. The app utilizes maximum legitimate Android APIs and worldwide telecom engineering menus.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun CapabilityAuditItem(
    title: String,
    isSupported: Boolean,
    supportedText: String,
    unsupportedText: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = SpiderWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSupported) supportedText else unsupportedText,
                    color = if (isSupported) SignalGreen else SignalOrange,
                    fontSize = 10.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSupported) SignalGreen.copy(alpha = 0.2f) else SignalOrange.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSupported) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isSupported) SignalGreen else SignalOrange,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
