package com.example.data.telephony

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast

object SystemSettingsHelper {

    enum class LaunchResult {
        SUCCESS,
        NOT_FOUND,
        RESTRICTED
    }

    /**
     * Attempts to open the official Android Testing / RadioInfo screen (*#*#4636#*#*)
     * which allows setting LTE Only, NR Only, etc. directly.
     */
    fun openRadioInfo(context: Context): LaunchResult {
        val intents = listOf(
            // Standard AOSP & Pixel & Xiaomi & many others
            Intent().apply {
                component = ComponentName("com.android.settings", "com.android.settings.RadioInfo")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            // Alternative Phone component
            Intent().apply {
                component = ComponentName("com.android.phone", "com.android.phone.settings.RadioInfo")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            // Testing settings activity
            Intent().apply {
                component = ComponentName("com.android.settings", "com.android.settings.TestingSettings")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            // Samsung alternative
            Intent().apply {
                action = "android.intent.action.MAIN"
                component = ComponentName("com.samsung.android.app.telephonyui", "com.samsung.android.app.telephonyui.hiddennetworksetting.MainActivity")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )

        for (intent in intents) {
            try {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    return LaunchResult.SUCCESS
                }
            } catch (e: Exception) {
                // Try next
            }
        }

        // Fallback: Try launching via direct intent action
        try {
            val fallbackIntent = Intent("android.intent.action.MAIN").apply {
                setClassName("com.android.settings", "com.android.settings.RadioInfo")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(fallbackIntent)
            return LaunchResult.SUCCESS
        } catch (e: Exception) {
            // RadioInfo activity blocked or not present on this specific ROM
            return LaunchResult.NOT_FOUND
        }
    }

    /**
     * Opens Mobile Network Settings in Android Settings
     */
    fun openMobileNetworkSettings(context: Context, subId: Int? = null): LaunchResult {
        val intents = mutableListOf<Intent>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && subId != null && subId > 0) {
            intents.add(
                Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS).apply {
                    putExtra(Settings.EXTRA_SUB_ID, subId)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }

        intents.add(
            Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        intents.add(
            Intent(Settings.ACTION_DATA_ROAMING_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        intents.add(
            Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )

        for (intent in intents) {
            try {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    return LaunchResult.SUCCESS
                }
            } catch (e: Exception) {
                // Continue
            }
        }
        return LaunchResult.NOT_FOUND
    }

    /**
     * Opens APN (Access Point Names) Settings
     */
    fun openApnSettings(context: Context): LaunchResult {
        val intent = Intent(Settings.ACTION_APN_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                LaunchResult.SUCCESS
            } else {
                LaunchResult.NOT_FOUND
            }
        } catch (e: Exception) {
            LaunchResult.NOT_FOUND
        }
    }

    /**
     * Opens MediaTek Engineer Mode if installed
     */
    fun openMtkEngineerMode(context: Context): LaunchResult {
        val intents = listOf(
            Intent().apply {
                component = ComponentName("com.mediatek.engineermode", "com.mediatek.engineermode.EngineerMode")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            Intent().apply {
                component = ComponentName("com.mediatek.engineermode", "com.mediatek.engineermode.BandMode")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        for (intent in intents) {
            try {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    return LaunchResult.SUCCESS
                }
            } catch (e: Exception) {
                // Continue
            }
        }
        return LaunchResult.NOT_FOUND
    }

    /**
     * Opens Samsung Hidden Network Setting if available
     */
    fun openSamsungNetworkSettings(context: Context): LaunchResult {
        val intents = listOf(
            Intent().apply {
                component = ComponentName("com.samsung.android.app.telephonyui", "com.samsung.android.app.telephonyui.hiddennetworksetting.MainActivity")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            Intent().apply {
                component = ComponentName("com.samsung.android.app.telephonyui", "com.samsung.android.app.telephonyui.netsettings.NetSettingsActivity")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        for (intent in intents) {
            try {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    return LaunchResult.SUCCESS
                }
            } catch (e: Exception) {
                // Continue
            }
        }
        return LaunchResult.NOT_FOUND
    }

    /**
     * Launches the phone dialer with pre-populated USSD code
     */
    fun openDialerWithCode(context: Context, ussdCode: String): Boolean {
        return try {
            val encodedHash = Uri.encode("#")
            val dialUri = Uri.parse("tel:" + ussdCode.replace("#", encodedHash))
            val intent = Intent(Intent.ACTION_DIAL, dialUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}
