package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NetworkModeItem
import com.example.data.telephony.SystemSettingsHelper
import com.example.data.telephony.TelephonyRepository
import com.example.ui.AppLanguage
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SignalGreen
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary

@Composable
fun NetworkModeResultDialog(
    mode: NetworkModeItem,
    result: TelephonyRepository.NetworkSwitchResult,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(20.dp)),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (result.success) SignalGreen.copy(alpha = 0.2f) else SpiderRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (result.success) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (result.success) SignalGreen else SpiderRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) mode.titleAr else mode.titleEn,
                        color = SpiderWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (result.success) (if (language == AppLanguage.ARABIC) "تم بنجاح" else "Success")
                        else (if (language == AppLanguage.ARABIC) "حماية نظام Android" else "Android Security Notice"),
                        color = if (result.success) SignalGreen else SignalOrange,
                        fontSize = 12.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == AppLanguage.ARABIC) result.messageAr else result.messageEn,
                    color = SpiderWhite,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                if (result.isRestrictedByAndroid) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // Instruction Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = SpiderRedLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "طريقة التفعيل الرسمية (خطوة واحدة):" else "Official 1-Click Method:",
                                    color = SpiderRedLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC)
                                    "1. اضغط الزر الأحمر أدناه لفتح شاشة RadioInfo الهندسية.\n2. ابحث عن خيار 'Set Preferred Network Type'.\n3. اختر الوضع المطلوب (${mode.codeName}) وسيتم تثبيته فوراً على المودم."
                                else
                                    "1. Tap the red button below to open official RadioInfo menu.\n2. Locate 'Set Preferred Network Type' dropdown.\n3. Select your mode (${mode.codeName}) and it locks instantly.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Primary Action: Open RadioInfo
                    Button(
                        onClick = {
                            val res = SystemSettingsHelper.openRadioInfo(context)
                            if (res == SystemSettingsHelper.LaunchResult.NOT_FOUND) {
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.ARABIC)
                                        "تعذر فتح RadioInfo مباشرة على واجهة جهازك. تم توفير كود الاتصال *#*#4636#*#* أدناه."
                                    else
                                        "Could not open RadioInfo directly on this ROM. Use dialer code below.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SpiderRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_radio_info_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Launch,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "فتح شاشة RadioInfo الرسمية" else "Open Official RadioInfo",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary Action: Open Phone Dialer with USSD
                    OutlinedButton(
                        onClick = {
                            val code = "*#*#4636#*#*"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("USSD", code))
                            SystemSettingsHelper.openDialerWithCode(context, code)
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.ARABIC) "تم نسخ الكود $code وفتح لوحة الاتصال" else "Copied $code and opened dialer",
                                Toast.LENGTH_SHORT
                            ).show()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_dialer_code_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = SpiderWhite,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "طلب كود الاختبار: *#*#4636#*#*" else "Dial Testing Code: *#*#4636#*#*",
                            color = SpiderWhite,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Open Mobile Network Settings
                    OutlinedButton(
                        onClick = {
                            SystemSettingsHelper.openMobileNetworkSettings(context)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_system_net_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "إعدادات شبكة الجوال في النظام" else "System Mobile Network Settings",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_dialog_btn")
            ) {
                Text(
                    text = if (language == AppLanguage.ARABIC) "إغلاق" else "Close",
                    color = SpiderRedLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
