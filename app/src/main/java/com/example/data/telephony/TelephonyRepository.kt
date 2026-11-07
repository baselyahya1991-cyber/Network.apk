package com.example.data.telephony

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrength
import android.telephony.CellSignalStrengthGsm
import android.telephony.CellSignalStrengthLte
import android.telephony.CellSignalStrengthNr
import android.telephony.CellSignalStrengthWcdma
import android.telephony.SignalStrength
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.example.data.model.CellTowerInfo
import com.example.data.model.DeviceCompatibilityInfo
import com.example.data.model.NetworkGen
import com.example.data.model.NetworkModeItem
import com.example.data.model.NetworkStatusInfo
import com.example.data.model.SimCardInfo
import com.example.data.model.SimStateDesc
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface

class TelephonyRepository(private val context: Context) {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    private val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    /**
     * Checks if permission is granted
     */
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Returns the list of standard network modes requested by user
     */
    fun getAvailableNetworkModes(): List<NetworkModeItem> {
        return listOf(
            NetworkModeItem(
                id = "mode_2g_only",
                titleAr = "2G فقط (GSM Only)",
                titleEn = "2G Only (GSM Only)",
                codeName = "GSM_ONLY",
                generation = NetworkGen.G2,
                descriptionAr = "تثبيت الاتصال على الجيل الثاني فقط لتوفير البطارية وضمان استقبال المكالمات في المناطق النائية.",
                descriptionEn = "Force 2G only to conserve battery and maintain basic voice calls in weak reception areas.",
                systemModeValue = 1 // NETWORK_MODE_GSM_ONLY
            ),
            NetworkModeItem(
                id = "mode_3g_only",
                titleAr = "3G فقط (WCDMA Only)",
                titleEn = "3G Only (WCDMA Only)",
                codeName = "WCDMA_ONLY",
                generation = NetworkGen.G3,
                descriptionAr = "تثبيت الاتصال على شبكات الجيل الثالث ومنع التحويل إلى 2G.",
                descriptionEn = "Lock connection to 3G networks and prevent falling back to 2G.",
                systemModeValue = 2 // NETWORK_MODE_WCDMA_ONLY
            ),
            NetworkModeItem(
                id = "mode_4g_only",
                titleAr = "4G LTE فقط (LTE Only)",
                titleEn = "4G LTE Only",
                codeName = "LTE_ONLY",
                generation = NetworkGen.G4,
                descriptionAr = "تثبيت الشبكة إجبارياً على 4G LTE فقط لمنع الهبوط لـ 3G/2G أثناء التصفح والألعاب.",
                descriptionEn = "Force 4G LTE only mode. Prevents dropping to 3G/2G during gaming and data usage.",
                systemModeValue = 11 // NETWORK_MODE_LTE_ONLY
            ),
            NetworkModeItem(
                id = "mode_5g_only",
                titleAr = "5G فقط (NR Only)",
                titleEn = "5G Only (NR Only)",
                codeName = "NR_ONLY",
                generation = NetworkGen.G5,
                descriptionAr = "تثبيت شبكة الجيل الخامس المستقلة (5G SA) للاستفادة القصوى من السرعات العالية.",
                descriptionEn = "Force standalone 5G NR only mode for maximum peak data speeds.",
                systemModeValue = 23 // NETWORK_MODE_NR_ONLY
            ),
            NetworkModeItem(
                id = "mode_2g_3g_auto",
                titleAr = "2G / 3G تلقائي (GSM/WCDMA)",
                titleEn = "2G/3G Auto (GSM/WCDMA)",
                codeName = "GSM_UMTS",
                generation = NetworkGen.G3,
                descriptionAr = "التبديل التلقائي بين شبكات 2G و 3G مع تفضيل 3G.",
                descriptionEn = "Automatic switching between 2G and 3G with 3G preferred.",
                systemModeValue = 0 // NETWORK_MODE_WCDMA_PREF
            ),
            NetworkModeItem(
                id = "mode_3g_4g_auto",
                titleAr = "3G / 4G تلقائي (WCDMA/LTE)",
                titleEn = "3G/4G Auto (WCDMA/LTE)",
                codeName = "LTE_WCDMA",
                generation = NetworkGen.G4,
                descriptionAr = "التنقل بين شبكات 4G و 3G دون السماح بالنزول إلى 2G البطيء.",
                descriptionEn = "Handover between 4G and 3G without dropping to 2G.",
                systemModeValue = 12 // NETWORK_MODE_LTE_WCDMA
            ),
            NetworkModeItem(
                id = "mode_4g_5g_auto",
                titleAr = "4G / 5G تلقائي (LTE/NR)",
                titleEn = "4G/5G Auto (LTE/NR)",
                codeName = "NR_LTE",
                generation = NetworkGen.G5,
                descriptionAr = "الدمج الذكي بين شبكات الجيل الرابع والخامس دون الرجوع للأجيال القديمة.",
                descriptionEn = "Seamless transition between 4G LTE and 5G NR networks.",
                systemModeValue = 24 // NETWORK_MODE_NR_LTE
            ),
            NetworkModeItem(
                id = "mode_lte_3g_2g_auto",
                titleAr = "4G / 3G / 2G تلقائي (LTE/3G/2G)",
                titleEn = "LTE / 3G / 2G Auto",
                codeName = "LTE_GSM_WCDMA",
                generation = NetworkGen.G4,
                descriptionAr = "الوضع الافتراضي لمعظم هواتف 4G مع إمكانية التبديل التلقائي الكامل.",
                descriptionEn = "Standard multi-mode switching for 4G phones across LTE, 3G, and 2G.",
                systemModeValue = 9 // NETWORK_MODE_LTE_GSM_WCDMA
            ),
            NetworkModeItem(
                id = "mode_all_auto",
                titleAr = "2G / 3G / 4G / 5G تلقائي شامل",
                titleEn = "2G/3G/4G/5G Comprehensive Auto",
                codeName = "NR_LTE_GSM_WCDMA",
                generation = NetworkGen.G5,
                descriptionAr = "الوضع التلقائي الشامل لجميع الأجيال الحديثة والقديمة لأفضل تغطية.",
                descriptionEn = "Full spectrum automatic handover across all mobile generations.",
                systemModeValue = 26 // NETWORK_MODE_NR_LTE_GSM_WCDMA
            ),
            NetworkModeItem(
                id = "mode_global",
                titleAr = "الوضع العالمي (Global / World Mode)",
                titleEn = "Global / World Mode",
                codeName = "GLOBAL",
                generation = NetworkGen.G5,
                descriptionAr = "الوضع العالمي للترددات الدولية والتجوال (LTE/CDMA/EvDo/GSM/WCDMA).",
                descriptionEn = "Global multi-carrier mode designed for international roaming.",
                systemModeValue = 10 // NETWORK_MODE_GLOBAL
            )
        )
    }

    /**
     * Reads all active SIM cards in the device
     */
    @SuppressLint("MissingPermission")
    fun getActiveSimCards(): List<SimCardInfo> {
        val simList = mutableListOf<SimCardInfo>()
        if (subscriptionManager == null) return emptyList()

        val defaultDataSubId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SubscriptionManager.getDefaultDataSubscriptionId()
        } else {
            SubscriptionManager.getDefaultSubscriptionId()
        }
        val defaultVoiceSubId = SubscriptionManager.getDefaultVoiceSubscriptionId()
        val defaultSmsSubId = SubscriptionManager.getDefaultSmsSubscriptionId()

        if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                val subInfoList = subscriptionManager.activeSubscriptionInfoList
                if (!subInfoList.isNullOrEmpty()) {
                    for (info in subInfoList) {
                        val slotIndex = info.simSlotIndex
                        val subId = info.subscriptionId
                        val carrierName = info.carrierName?.toString()?.trim()
                            ?: telephonyManager?.createForSubscriptionId(subId)?.simOperatorName?.trim()
                            ?: "غير معروف / Unknown"
                        val displayName = info.displayName?.toString()?.trim()
                            ?: "SIM ${slotIndex + 1}"
                        val countryIso = info.countryIso?.uppercase() ?: ""

                        val simState = when (telephonyManager?.getSimState(slotIndex)) {
                            TelephonyManager.SIM_STATE_READY -> SimStateDesc.READY
                            TelephonyManager.SIM_STATE_ABSENT -> SimStateDesc.ABSENT
                            TelephonyManager.SIM_STATE_PIN_REQUIRED -> SimStateDesc.PIN_REQUIRED
                            TelephonyManager.SIM_STATE_PUK_REQUIRED -> SimStateDesc.PUK_REQUIRED
                            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> SimStateDesc.NETWORK_LOCKED
                            else -> SimStateDesc.UNKNOWN
                        }

                        val mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) info.mccString ?: "" else "${info.mcc}"
                        val mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) info.mncString ?: "" else "${info.mnc}"
                        val mccMnc = if (mcc.isNotEmpty() && mnc.isNotEmpty()) "$mcc-$mnc" else ""

                        simList.add(
                            SimCardInfo(
                                slotIndex = slotIndex,
                                subscriptionId = subId,
                                displayName = displayName,
                                carrierName = carrierName.ifEmpty { "SIM ${slotIndex + 1}" },
                                countryIso = countryIso,
                                isDefaultData = subId == defaultDataSubId,
                                isDefaultVoice = subId == defaultVoiceSubId,
                                isDefaultSms = subId == defaultSmsSubId,
                                simState = simState,
                                iccIdPreview = if (info.iccId.isNullOrEmpty()) "" else "•••" + info.iccId.takeLast(4),
                                mccMnc = mccMnc
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                // Subscription query error or permission issue
            }
        }

        // If no subscriptions found or permission not yet granted, query general TelephonyManager
        if (simList.isEmpty() && telephonyManager != null) {
            val simStateRaw = telephonyManager.simState
            val simState = when (simStateRaw) {
                TelephonyManager.SIM_STATE_READY -> SimStateDesc.READY
                TelephonyManager.SIM_STATE_ABSENT -> SimStateDesc.ABSENT
                TelephonyManager.SIM_STATE_PIN_REQUIRED -> SimStateDesc.PIN_REQUIRED
                TelephonyManager.SIM_STATE_PUK_REQUIRED -> SimStateDesc.PUK_REQUIRED
                else -> SimStateDesc.UNKNOWN
            }
            val carrier = telephonyManager.simOperatorName.ifEmpty { telephonyManager.networkOperatorName }.ifEmpty { "SIM 1" }
            simList.add(
                SimCardInfo(
                    slotIndex = 0,
                    subscriptionId = 1,
                    displayName = "SIM 1",
                    carrierName = carrier,
                    countryIso = telephonyManager.simCountryIso.uppercase(),
                    isDefaultData = true,
                    isDefaultVoice = true,
                    isDefaultSms = true,
                    simState = simState,
                    mccMnc = telephonyManager.simOperator
                )
            )
        }

        return simList.sortedBy { it.slotIndex }
    }

    /**
     * Reads complete network status for a specific subscription/SIM
     */
    @SuppressLint("MissingPermission")
    fun getNetworkStatus(selectedSubId: Int = -1, selectedSlotIndex: Int = 0): NetworkStatusInfo {
        val tm = if (telephonyManager != null && selectedSubId > 0) {
            telephonyManager.createForSubscriptionId(selectedSubId)
        } else {
            telephonyManager
        }

        if (tm == null) {
            return NetworkStatusInfo()
        }

        // Check Airplane Mode
        val isAirplaneMode = Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0

        // Carrier names
        val rawOperator = tm.networkOperatorName.trim()
        val rawSimOp = tm.simOperatorName.trim()
        val carrierName = when {
            rawOperator.isNotEmpty() -> rawOperator
            rawSimOp.isNotEmpty() -> rawSimOp
            else -> "غير متاح / Unavailable"
        }
        val simOperator = rawSimOp.ifEmpty { "غير متاح / Unavailable" }

        val mccMncRaw = tm.networkOperator.ifEmpty { tm.simOperator }
        val mccMnc = if (mccMncRaw.length >= 5) {
            "${mccMncRaw.substring(0, 3)} - ${mccMncRaw.substring(3)}"
        } else {
            "-- / --"
        }

        val simStateStr = when (tm.simState) {
            TelephonyManager.SIM_STATE_READY -> "جاهزة ونشطة / Ready"
            TelephonyManager.SIM_STATE_ABSENT -> "لا توجد شريحة / Absent"
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "مطلوب PIN"
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "مطلوب PUK"
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "مقفلة على الشبكة"
            else -> "غير معروفة / Unknown"
        }

        // Network Generation & Data Types
        val dataNetType = if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                tm.dataNetworkType
            } catch (e: Exception) {
                TelephonyManager.NETWORK_TYPE_UNKNOWN
            }
        } else {
            TelephonyManager.NETWORK_TYPE_UNKNOWN
        }

        val voiceNetType = if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                tm.voiceNetworkType
            } catch (e: Exception) {
                TelephonyManager.NETWORK_TYPE_UNKNOWN
            }
        } else {
            TelephonyManager.NETWORK_TYPE_UNKNOWN
        }

        // Resolve generation
        val (generation, genName) = resolveNetworkGeneration(dataNetType, voiceNetType)

        // Roaming
        val isRoaming = tm.isNetworkRoaming

        // Data connectivity & Wi-Fi
        var isDataConnected = false
        var isWifiConnected = false
        var wifiSsid: String? = null

        connectivityManager?.let { cm ->
            val activeNet = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(activeNet)
            if (caps != null) {
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isDataConnected = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isWifiConnected = true
                    wifiSsid = "Wi-Fi Connected"
                }
            }
        }

        // Phone Type
        val phoneTypeStr = when (tm.phoneType) {
            TelephonyManager.PHONE_TYPE_GSM -> "GSM"
            TelephonyManager.PHONE_TYPE_CDMA -> "CDMA"
            TelephonyManager.PHONE_TYPE_SIP -> "SIP"
            else -> "NONE"
        }

        // Signal Strength
        val (signalLevel, signalDbm, signalAsu, signalPercentage) = extractSignalMetrics(tm)

        // Registration State
        val regState = if (isAirplaneMode) {
            "وضع الطيران مفعل / Airplane Mode"
        } else if (signalLevel > 0 || (signalDbm != null && signalDbm > -140)) {
            "مسجل في الشبكة / In Service"
        } else if (tm.simState == TelephonyManager.SIM_STATE_READY) {
            "جاري البحث عن الشبكة / Searching..."
        } else {
            "لا توجد خدمة / No Service"
        }

        // IP Address
        val ipAddress = getDeviceIpAddress()

        return NetworkStatusInfo(
            generation = generation,
            networkTypeName = genName,
            dataNetworkType = getNetworkTypeLabel(dataNetType),
            voiceNetworkType = getNetworkTypeLabel(voiceNetType),
            carrierName = carrierName,
            simOperator = simOperator,
            mccMnc = mccMnc,
            simState = simStateStr,
            simSlot = selectedSlotIndex + 1,
            subscriptionId = selectedSubId,
            isRoaming = isRoaming,
            isDataConnected = isDataConnected,
            registrationState = regState,
            signalLevel = signalLevel,
            signalDbm = signalDbm,
            signalAsu = signalAsu,
            signalPercentage = signalPercentage,
            phoneType = phoneTypeStr,
            ipAddress = ipAddress,
            isAirplaneMode = isAirplaneMode,
            isWifiConnected = isWifiConnected,
            wifiSsid = wifiSsid
        )
    }

    /**
     * Extracts signal level and dBm metrics
     */
    private fun extractSignalMetrics(tm: TelephonyManager): SignalMetrics {
        var level = 0
        var dbm: Int? = null
        var asu: Int? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val ss = tm.signalStrength
                if (ss != null) {
                    level = ss.level // 0..4
                    val cellStrengths = ss.cellSignalStrengths
                    for (cs in cellStrengths) {
                        if (cs.dbm != CellInfo.UNAVAILABLE && cs.dbm != Integer.MAX_VALUE && cs.dbm < 0) {
                            dbm = cs.dbm
                            asu = cs.asuLevel
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                // Handled
            }
        }

        // Fallback for cell info if signal strength object was empty
        if (dbm == null && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            try {
                @SuppressLint("MissingPermission")
                val cellInfoList = tm.allCellInfo
                if (!cellInfoList.isNullOrEmpty()) {
                    for (info in cellInfoList) {
                        if (info.isRegistered) {
                            val signal = extractSignalFromCellInfo(info)
                            if (signal != null) {
                                dbm = signal.dbm
                                level = signal.level
                                asu = signal.asuLevel
                                break
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Handled
            }
        }

        // Calculate meaningful percentage if dBm or level is present
        val percentage = when {
            dbm != null -> {
                // Standard cellular dBm range: -120 dBm (0%) to -65 dBm (100%)
                val clamped = dbm.coerceIn(-120, -65)
                (((clamped - (-120)) * 100) / ((-65) - (-120))).coerceIn(0, 100)
            }
            level > 0 -> (level * 25).coerceIn(0, 100)
            else -> null
        }

        return SignalMetrics(level, dbm, asu, percentage)
    }

    private data class SignalMetrics(
        val level: Int,
        val dbm: Int?,
        val asu: Int?,
        val percentage: Int?
    )

    private fun extractSignalFromCellInfo(info: CellInfo): CellSignalStrength? {
        return when (info) {
            is CellInfoLte -> info.cellSignalStrength
            is CellInfoNr -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) info.cellSignalStrength else null
            }
            is CellInfoWcdma -> info.cellSignalStrength
            is CellInfoGsm -> info.cellSignalStrength
            else -> null
        }
    }

    /**
     * Reads cell tower telemetry
     */
    @SuppressLint("MissingPermission")
    fun getCellTowers(): List<CellTowerInfo> {
        val towers = mutableListOf<CellTowerInfo>()
        if (telephonyManager == null || !hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return emptyList()
        }

        try {
            val cellList = telephonyManager.allCellInfo
            if (!cellList.isNullOrEmpty()) {
                for (info in cellList) {
                    when (info) {
                        is CellInfoLte -> {
                            val id = info.cellIdentity
                            val ss = info.cellSignalStrength
                            val cidStr = if (id.ci != CellInfo.UNAVAILABLE && id.ci > 0) id.ci.toString() else "N/A"
                            val tacStr = if (id.tac != CellInfo.UNAVAILABLE && id.tac > 0) id.tac.toString() else "N/A"
                            val pciStr = if (id.pci != CellInfo.UNAVAILABLE && id.pci > 0) id.pci.toString() else "N/A"
                            val earfcnStr = if (id.earfcn != CellInfo.UNAVAILABLE && id.earfcn > 0) id.earfcn.toString() else "N/A"
                            val dbmVal = if (ss.dbm != CellInfo.UNAVAILABLE && ss.dbm < 0) ss.dbm else null
                            towers.add(
                                CellTowerInfo(
                                    type = "4G LTE",
                                    cellId = cidStr,
                                    tacOrLac = "TAC: $tacStr",
                                    pciOrPsc = "PCI: $pciStr",
                                    earfcn = "EARFCN: $earfcnStr",
                                    signalDbm = dbmVal,
                                    isRegistered = info.isRegistered
                                )
                            )
                        }
                        is CellInfoNr -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val id = info.cellIdentity as? android.telephony.CellIdentityNr
                                val ss = info.cellSignalStrength as? CellSignalStrengthNr
                                val nciStr = if (id != null && id.nci != CellInfo.UNAVAILABLE_LONG && id.nci > 0L) id.nci.toString() else "N/A"
                                val tacStr = if (id != null && id.tac != CellInfo.UNAVAILABLE && id.tac > 0) id.tac.toString() else "N/A"
                                val pciStr = if (id != null && id.pci != CellInfo.UNAVAILABLE && id.pci > 0) id.pci.toString() else "N/A"
                                val nrarfcnStr = if (id != null && id.nrarfcn != CellInfo.UNAVAILABLE && id.nrarfcn > 0) id.nrarfcn.toString() else "N/A"
                                val dbmVal = if (ss != null && ss.dbm != CellInfo.UNAVAILABLE && ss.dbm < 0) ss.dbm else null
                                towers.add(
                                    CellTowerInfo(
                                        type = "5G NR",
                                        cellId = nciStr,
                                        tacOrLac = "TAC: $tacStr",
                                        pciOrPsc = "PCI: $pciStr",
                                        earfcn = "NR-ARFCN: $nrarfcnStr",
                                        signalDbm = dbmVal,
                                        isRegistered = info.isRegistered
                                    )
                                )
                            }
                        }
                        is CellInfoWcdma -> {
                            val id = info.cellIdentity
                            val ss = info.cellSignalStrength
                            val cidStr = if (id.cid != CellInfo.UNAVAILABLE && id.cid > 0) id.cid.toString() else "N/A"
                            val lacStr = if (id.lac != CellInfo.UNAVAILABLE && id.lac > 0) id.lac.toString() else "N/A"
                            val pscStr = if (id.psc != CellInfo.UNAVAILABLE && id.psc > 0) id.psc.toString() else "N/A"
                            val uarfcnStr = if (id.uarfcn != CellInfo.UNAVAILABLE && id.uarfcn > 0) id.uarfcn.toString() else "N/A"
                            val dbmVal = if (ss.dbm != CellInfo.UNAVAILABLE && ss.dbm < 0) ss.dbm else null
                            towers.add(
                                CellTowerInfo(
                                    type = "3G WCDMA",
                                    cellId = cidStr,
                                    tacOrLac = "LAC: $lacStr",
                                    pciOrPsc = "PSC: $pscStr",
                                    earfcn = "UARFCN: $uarfcnStr",
                                    signalDbm = dbmVal,
                                    isRegistered = info.isRegistered
                                )
                            )
                        }
                        is CellInfoGsm -> {
                            val id = info.cellIdentity
                            val ss = info.cellSignalStrength
                            val cidStr = if (id.cid != CellInfo.UNAVAILABLE && id.cid > 0) id.cid.toString() else "N/A"
                            val lacStr = if (id.lac != CellInfo.UNAVAILABLE && id.lac > 0) id.lac.toString() else "N/A"
                            val arfcnStr = if (id.arfcn != CellInfo.UNAVAILABLE && id.arfcn > 0) id.arfcn.toString() else "N/A"
                            val dbmVal = if (ss.dbm != CellInfo.UNAVAILABLE && ss.dbm < 0) ss.dbm else null
                            towers.add(
                                CellTowerInfo(
                                    type = "2G GSM",
                                    cellId = cidStr,
                                    tacOrLac = "LAC: $lacStr",
                                    pciOrPsc = "BSIC: ${id.bsic}",
                                    earfcn = "ARFCN: $arfcnStr",
                                    signalDbm = dbmVal,
                                    isRegistered = info.isRegistered
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Handled
        }
        return towers
    }

    /**
     * Resolves human readable network generation
     */
    private fun resolveNetworkGeneration(dataNetType: Int, voiceNetType: Int): Pair<NetworkGen, String> {
        val target = if (dataNetType != TelephonyManager.NETWORK_TYPE_UNKNOWN) dataNetType else voiceNetType
        return when (target) {
            TelephonyManager.NETWORK_TYPE_NR -> NetworkGen.G5 to "5G New Radio (NR)"
            TelephonyManager.NETWORK_TYPE_LTE -> NetworkGen.G4 to "4G LTE"
            TelephonyManager.NETWORK_TYPE_IWLAN -> NetworkGen.G4 to "4G VoWiFi / IWLAN"
            19 -> NetworkGen.G4 to "4G LTE Advanced (LTE_CA)" // NETWORK_TYPE_LTE_CA

            TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkGen.G3 to "3G HSPA+ (H+)"
            TelephonyManager.NETWORK_TYPE_HSPA -> NetworkGen.G3 to "3G HSPA (H)"
            TelephonyManager.NETWORK_TYPE_HSDPA -> NetworkGen.G3 to "3G HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> NetworkGen.G3 to "3G HSUPA"
            TelephonyManager.NETWORK_TYPE_UMTS -> NetworkGen.G3 to "3G UMTS"
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD -> NetworkGen.G3 to "3G CDMA/EVDO"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkGen.G3 to "3G TD-SCDMA"

            TelephonyManager.NETWORK_TYPE_EDGE -> NetworkGen.G2 to "2G EDGE (E)"
            TelephonyManager.NETWORK_TYPE_GPRS -> NetworkGen.G2 to "2G GPRS (G)"
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN,
            TelephonyManager.NETWORK_TYPE_GSM -> NetworkGen.G2 to "2G GSM / 1xRTT"

            else -> NetworkGen.UNKNOWN to "غير متاح / Unavailable"
        }
    }

    private fun getNetworkTypeLabel(type: Int): String {
        return when (type) {
            TelephonyManager.NETWORK_TYPE_NR -> "NR (5G)"
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE (4G)"
            19 -> "LTE_CA (4G+)"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPA+"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS (3G)"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE (2G)"
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS (2G)"
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> "غير متاح / Unavailable"
            else -> "Type #$type"
        }
    }

    /**
     * Reads IP address for cellular network interface
     */
    private fun getDeviceIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val intf = interfaces.nextElement()
                val name = intf.name.lowercase()
                // Check mobile data interface names like rmnet, ccmni, pdp, wwan, or wlan
                if (name.contains("rmnet") || name.contains("ccmni") || name.contains("pdp") || name.contains("wwan") || name.contains("wlan")) {
                    val addrs = intf.inetAddresses
                    while (addrs.hasMoreElements()) {
                        val addr = addrs.nextElement()
                        if (!addr.isLoopbackAddress && addr is Inet4Address) {
                            return addr.hostAddress
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Handled
        }
        return null
    }

    /**
     * Executes real attempt to switch network mode programmatically
     * and reports honest diagnostic result.
     */
    data class NetworkSwitchResult(
        val success: Boolean,
        val isRestrictedByAndroid: Boolean,
        val messageAr: String,
        val messageEn: String,
        val technicalDetails: String
    )

    fun attemptSetNetworkMode(mode: NetworkModeItem, subId: Int = -1): NetworkSwitchResult {
        if (telephonyManager == null) {
            return NetworkSwitchResult(
                success = false,
                isRestrictedByAndroid = false,
                messageAr = "خدمة الاتصالات غير متوفرة على هذا الجهاز.",
                messageEn = "Telephony service is not available on this device.",
                technicalDetails = "TelephonyManager is null"
            )
        }

        // Real invocation attempt
        // 1. Try Android 13+ setAllowedNetworkTypesForReason if subId is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && subId > 0) {
            try {
                val tm = telephonyManager.createForSubscriptionId(subId)
                val bitmask = getBitmaskForSystemMode(mode.systemModeValue)
                tm.setAllowedNetworkTypesForReason(
                    TelephonyManager.ALLOWED_NETWORK_TYPES_REASON_USER,
                    bitmask
                )
                return NetworkSwitchResult(
                    success = true,
                    isRestrictedByAndroid = false,
                    messageAr = "تم تطبيق الوضع ${mode.titleAr} بنجاح!",
                    messageEn = "Successfully applied ${mode.titleEn} mode!",
                    technicalDetails = "TelephonyManager.setAllowedNetworkTypesForReason executed successfully"
                )
            } catch (se: SecurityException) {
                // Expected on standard non-system/unrooted Android apps
                return buildSecurityRestrictionResult(se)
            } catch (e: Exception) {
                // Other exception, proceed to reflection fallback
            }
        }

        // 2. Reflection attempt on setPreferredNetworkType (Legacy & System ROMs)
        try {
            val method = if (subId > 0) {
                telephonyManager.javaClass.getMethod("setPreferredNetworkType", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            } else {
                telephonyManager.javaClass.getMethod("setPreferredNetworkType", Int::class.javaPrimitiveType)
            }
            method.isAccessible = true
            val invoked = if (subId > 0) {
                method.invoke(telephonyManager, subId, mode.systemModeValue)
            } else {
                method.invoke(telephonyManager, mode.systemModeValue)
            }
            val isSuccess = (invoked as? Boolean) ?: true
            if (isSuccess) {
                return NetworkSwitchResult(
                    success = true,
                    isRestrictedByAndroid = false,
                    messageAr = "تم تطبيق وضع الشبكة بنجاح عبر النظام!",
                    messageEn = "Network mode successfully applied via system API!",
                    technicalDetails = "Reflection setPreferredNetworkType returned true"
                )
            }
        } catch (se: SecurityException) {
            return buildSecurityRestrictionResult(se)
        } catch (ite: java.lang.reflect.InvocationTargetException) {
            if (ite.targetException is SecurityException) {
                return buildSecurityRestrictionResult(ite.targetException as SecurityException)
            }
        } catch (e: Exception) {
            // Handled below
        }

        // Standard Android Security restriction explanation
        return NetworkSwitchResult(
            success = false,
            isRestrictedByAndroid = true,
            messageAr = "تتطلب أنظمة Android الرسمية إذن النظام MODIFY_PHONE_STATE أو صلاحيات المشغل لتغيير المودم برمجياً بدون تدخل المستخدم.\n\nيمكنك تفعيل هذا الوضع فوراً بنقرة واحدة عبر شاشة RadioInfo الهندسية الرسمية أدناه.",
            messageEn = "Standard Android restricts direct modem switching without system MODIFY_PHONE_STATE permission.\n\nYou can apply this mode immediately with 1-click via the official RadioInfo engineering menu below.",
            technicalDetails = "Android OS Security Enforcement (Permission MODIFY_PHONE_STATE required for direct background radio switching)"
        )
    }

    private fun buildSecurityRestrictionResult(se: SecurityException): NetworkSwitchResult {
        return NetworkSwitchResult(
            success = false,
            isRestrictedByAndroid = true,
            messageAr = "قامت حماية أندرويد بحظر التعديل المباشر:\n${se.localizedMessage ?: "يتطلب إذن MODIFY_PHONE_STATE"}\n\nالحل المعتمد والموثوق: اضغط زر 'فتح RadioInfo' أدناه لتحديد الوضع مباشرة من قائمة الاختبار الرسمية.",
            messageEn = "Android security enforced restriction:\n${se.localizedMessage ?: "Requires MODIFY_PHONE_STATE permission"}\n\nApproved solution: Tap 'Open RadioInfo' below to pick this mode from the official testing list.",
            technicalDetails = "SecurityException: ${se.message}"
        )
    }

    private fun getBitmaskForSystemMode(rilMode: Int): Long {
        // Bitmask mapping for AllowedNetworkTypes
        return when (rilMode) {
            1 -> 1L shl 16 // GSM
            2 -> 1L shl 3 // WCDMA
            11 -> 1L shl 13 // LTE
            23 -> 1L shl 19 // NR 5G
            12 -> (1L shl 13) or (1L shl 3)
            24 -> (1L shl 19) or (1L shl 13)
            9 -> (1L shl 13) or (1L shl 3) or (1L shl 16)
            26 -> (1L shl 19) or (1L shl 13) or (1L shl 3) or (1L shl 16)
            else -> 0xFFFFFFFFL
        }
    }

    /**
     * Gathers complete device and hardware compatibility info
     */
    fun getDeviceCompatibility(): DeviceCompatibilityInfo {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        val model = Build.MODEL
        val brand = Build.BRAND.replaceFirstChar { it.uppercase() }
        val device = Build.DEVICE
        val hardware = Build.HARDWARE

        // Chipset detection
        val processor = detectProcessorChipset(hardware, Build.BOARD)

        // Dual SIM support
        val isDualSim = if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                telephonyManager.activeModemCount >= 2
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                telephonyManager.isMultiSimSupported == TelephonyManager.MULTISIM_ALLOWED
            } else {
                telephonyManager.phoneCount >= 2
            }
        } else {
            false
        }

        // 5G capability check
        val is5G = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && telephonyManager != null) {
            try {
                val bitmask = telephonyManager.supportedRadioAccessFamily
                (bitmask and (1L shl 19)) != 0L // RAF_NR
            } catch (e: Exception) {
                processor.contains("Snapdragon", ignoreCase = true) || processor.contains("Dimensity", ignoreCase = true)
            }
        } else {
            false
        }

        val hasPhoneInfoShortcut = SystemSettingsHelper.openRadioInfo(context) != SystemSettingsHelper.LaunchResult.NOT_FOUND

        return DeviceCompatibilityInfo(
            manufacturer = manufacturer,
            model = model,
            brand = brand,
            device = device,
            hardware = hardware,
            processorChipset = processor,
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH ?: "N/A",
            isDualSimSupported = isDualSim,
            is5GSupported = is5G,
            isPhoneInfoShortcutSupported = true,
            readPhoneStateGranted = hasPermission(Manifest.permission.READ_PHONE_STATE),
            fineLocationGranted = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION),
            directSwitchSupported = false,
            directSwitchExplanationAr = "يسمح أندرويد بقراءة كامل البيانات الحقيقية وتوفير اختصار RadioInfo الرسمي للتحكم في الأوضاع بدون روت.",
            directSwitchExplanationEn = "Android allows reading live telemetry and provides official RadioInfo engineering menu for rootless mode switching."
        )
    }

    private fun detectProcessorChipset(hardware: String, board: String): String {
        val hw = (hardware + " " + board).lowercase()
        return when {
            hw.contains("qcom") || hw.contains("snapdragon") || hw.contains("sm") || hw.contains("sdm") || hw.contains("msm") -> "Qualcomm Snapdragon ($hardware)"
            hw.contains("mt") || hw.contains("mediatek") || hw.contains("dimensity") || hw.contains("helio") -> "MediaTek ($hardware)"
            hw.contains("exynos") || hw.contains("s5e") || hw.contains("universal") -> "Samsung Exynos ($hardware)"
            hw.contains("tensor") || hw.contains("gs101") || hw.contains("gs201") || hw.contains("zuma") -> "Google Tensor ($hardware)"
            hw.contains("kirin") || hw.contains("hi36") || hw.contains("hi62") -> "HiSilicon Kirin ($hardware)"
            hw.contains("sprd") || hw.contains("unisoc") || hw.contains("sc98") || hw.contains("ums") -> "Unisoc / Spreadtrum ($hardware)"
            else -> "ARM / $hardware"
        }
    }
}
