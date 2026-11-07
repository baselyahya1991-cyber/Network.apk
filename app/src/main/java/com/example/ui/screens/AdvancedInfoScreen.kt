package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CellTowerInfo
import com.example.data.model.NetworkStatusInfo
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
import com.example.ui.theme.TextTertiary

@Composable
fun AdvancedInfoScreen(
    status: NetworkStatusInfo,
    simCards: List<SimCardInfo>,
    selectedSimIndex: Int,
    cellTowers: List<CellTowerInfo>,
    hasFineLocationPermission: Boolean,
    language: AppLanguage,
    onRefresh: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSim = simCards.getOrNull(selectedSimIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Section 1: Carrier & Operator Identification
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "بيانات المشغل والشريحة (SIM & Operator)" else "Carrier & SIM Identification",
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
                        label = if (language == AppLanguage.ARABIC) "اسم مشغل الشبكة (Network Operator)" else "Network Operator Name",
                        value = status.carrierName
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "مشغل الشريحة (SIM Operator)" else "SIM Operator Name",
                        value = status.simOperator
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "كود MCC - MNC" else "MCC / MNC Code",
                        value = status.mccMnc
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "حالة الشريحة (SIM State)" else "SIM State",
                        value = status.simState
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "اسم تعريف الشريحة (Carrier Name)" else "SIM Carrier Display",
                        value = currentSim?.displayName ?: "SIM ${selectedSimIndex + 1}"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "منفذ الشريحة (Slot Index)" else "SIM Slot Index",
                        value = "Slot ${status.simSlot} (SIM ${status.simSlot})"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "معرّف الاشتراك (Subscription ID)" else "Subscription ID",
                        value = if (status.subscriptionId > 0) "#${status.subscriptionId}" else "Default"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "معاينة ICCID (آخر 4 أرقام)" else "ICCID Preview (Last 4)",
                        value = currentSim?.iccIdPreview?.ifEmpty { "محمي بالنظام / Protected" } ?: "محمي بالنظام / Protected"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "رمز الدولة (Country ISO)" else "Country ISO",
                        value = currentSim?.countryIso ?: "SA"
                    )
                }
            }
        }

        // Section 2: Radio & Telephony Channels
        item {
            Text(
                text = if (language == AppLanguage.ARABIC) "قنوات وتقنيات الاتصال الهوائي" else "Radio & Telephony Channels",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "جيل الاتصال الأساسي (Generation)" else "Cellular Generation",
                        value = status.generation.label
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "نوع شبكة البيانات (Data Net Type)" else "Data Network Type",
                        value = status.dataNetworkType
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "نوع شبكة المكالمات الصوتية (Voice Net)" else "Voice Network Type",
                        value = status.voiceNetworkType
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "نوع الهاتف والمودم (Phone Type)" else "Phone Modem Architecture",
                        value = status.phoneType
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "حالة تسجيل البرج (Registration)" else "Tower Registration Status",
                        value = status.registrationState
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "وضع التجوال (Roaming Status)" else "Data Roaming Status",
                        value = if (status.isRoaming) (if (language == AppLanguage.ARABIC) "نعم - في وضع التجوال" else "Active (Roaming)")
                        else (if (language == AppLanguage.ARABIC) "لا - شبكة محلية معتمدة" else "Off (Home Network)")
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "قوة الإشارة (dBm)" else "Signal Strength (dBm)",
                        value = if (status.signalDbm != null) "${status.signalDbm} dBm" else "غير متاح / Unavailable"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "مستوى الإشارة (Signal Level)" else "Signal Level Bars",
                        value = "${status.signalLevel} من 4 درجات"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "وحدات القوة الهوائية (ASU)" else "Arbitrary Strength Unit (ASU)",
                        value = if (status.signalAsu != null) "${status.signalAsu} ASU" else "غير متاح / Unavailable"
                    )
                    DetailRow(
                        label = if (language == AppLanguage.ARABIC) "عنوان IP الجوال (Mobile IP)" else "Cellular IP Address",
                        value = status.ipAddress ?: if (language == AppLanguage.ARABIC) "غير متاح (يتطلب تشغيل بيانات الجوال)" else "Unavailable (Requires Mobile Data)"
                    )
                }
            }
        }

        // Section 3: Cell Towers Telemetry (CellInfo)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (language == AppLanguage.ARABIC) "أبراج الاتصال المجاورة والمُسجلة (Cell Info)" else "Registered & Neighboring Cell Towers",
                    color = SpiderWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )

                if (!hasFineLocationPermission) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SpiderRed.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "مطلوب إذن الموقع" else "Location Req.",
                            color = SpiderRedLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (!hasFineLocationPermission) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SpiderRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SpiderRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إذن الموقع مطلوب لقراءة بيانات الأبراج" else "Location Permission Needed for Cell Towers",
                                color = SpiderWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC)
                                "يفرض نظام Android إذن الموقع الدقيق لعرض معرّف البرج (CID)، التردد (EARFCN/ARFCN)، والمنطقة (TAC/LAC) لمنع تتبع الأبراج دون موافقة."
                            else
                                "Android mandates Location permission to expose Cell ID (CID), Frequency (EARFCN), and Area (TAC) to safeguard tower tracking privacy.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onRequestLocationPermission,
                            colors = ButtonDefaults.buttonColors(containerColor = SpiderRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("request_location_perm_btn")
                        ) {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "منح إذن الموقع لعرض الأبراج" else "Grant Location Permission",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else if (cellTowers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkOutline, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CellTower,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "لم يتم رصد أبراج خلوية حالياً أو الجهاز في وضع السكون" else "No cell towers currently detected or device in standby",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(cellTowers) { tower ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if (tower.isRegistered) SpiderRed.copy(alpha = 0.5f) else DarkOutline, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = tower.type,
                                    color = SpiderRedLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                if (tower.isRegistered) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SignalGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "البرج المتصل به" else "Serving Cell",
                                            color = SignalGreen,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "CID: ${tower.cellId} • ${tower.tacOrLac}",
                                color = SpiderWhite,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${tower.pciOrPsc} • ${tower.earfcn}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        if (tower.signalDbm != null) {
                            Text(
                                text = "${tower.signalDbm} dBm",
                                color = SpiderWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            color = SpiderWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
