package com.example.data.model

enum class NetworkGen(val label: String, val badgeColorHex: Long) {
    UNKNOWN("N/A", 0xFF78909C),
    G2("2G", 0xFF8D6E63),
    G3("3G", 0xFFFF9800),
    G4("4G LTE", 0xFF2196F3),
    G5("5G NR", 0xFFFF1744)
}

enum class SimStateDesc(val labelAr: String, val labelEn: String) {
    ABSENT("غير موجودة", "Absent"),
    READY("جاهزة ونشطة", "Ready & Active"),
    PIN_REQUIRED("مقفلة برمز PIN", "PIN Required"),
    PUK_REQUIRED("مقفلة برمز PUK", "PUK Required"),
    NETWORK_LOCKED("مقفلة على الشبكة", "Network Locked"),
    UNKNOWN("غير معروفة", "Unknown")
}

data class SimCardInfo(
    val slotIndex: Int,
    val subscriptionId: Int,
    val displayName: String,
    val carrierName: String,
    val countryIso: String,
    val isDefaultData: Boolean,
    val isDefaultVoice: Boolean,
    val isDefaultSms: Boolean,
    val simState: SimStateDesc,
    val iccIdPreview: String = "",
    val mccMnc: String = ""
)

data class CellTowerInfo(
    val type: String,
    val cellId: String,
    val tacOrLac: String,
    val pciOrPsc: String,
    val earfcn: String,
    val signalDbm: Int?,
    val isRegistered: Boolean
)

data class NetworkStatusInfo(
    val generation: NetworkGen = NetworkGen.UNKNOWN,
    val networkTypeName: String = "غير متاح / Unavailable",
    val dataNetworkType: String = "غير متاح / Unavailable",
    val voiceNetworkType: String = "غير متاح / Unavailable",
    val carrierName: String = "غير متاح / Unavailable",
    val simOperator: String = "غير متاح / Unavailable",
    val mccMnc: String = "-- / --",
    val simState: String = "غير متاح / Unavailable",
    val simSlot: Int = 1,
    val subscriptionId: Int = -1,
    val isRoaming: Boolean = false,
    val isDataConnected: Boolean = false,
    val registrationState: String = "جاري الفحص... / Checking...",
    val signalLevel: Int = 0, // 0 to 4 or 5
    val signalDbm: Int? = null,
    val signalAsu: Int? = null,
    val signalPercentage: Int? = null,
    val phoneType: String = "GSM",
    val ipAddress: String? = null,
    val isAirplaneMode: Boolean = false,
    val isWifiConnected: Boolean = false,
    val wifiSsid: String? = null
)

data class NetworkModeItem(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val codeName: String,
    val generation: NetworkGen,
    val descriptionAr: String,
    val descriptionEn: String,
    val systemModeValue: Int // Standard RIL preferred network mode value
)

data class DeviceCompatibilityInfo(
    val manufacturer: String,
    val model: String,
    val brand: String,
    val device: String,
    val hardware: String,
    val processorChipset: String,
    val androidVersion: String,
    val sdkInt: Int,
    val securityPatch: String,
    val isDualSimSupported: Boolean,
    val is5GSupported: Boolean,
    val isPhoneInfoShortcutSupported: Boolean,
    val readPhoneStateGranted: Boolean,
    val fineLocationGranted: Boolean,
    val directSwitchSupported: Boolean,
    val directSwitchExplanationAr: String,
    val directSwitchExplanationEn: String
)
