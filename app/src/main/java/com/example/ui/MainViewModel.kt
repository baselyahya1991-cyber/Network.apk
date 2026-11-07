package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CellTowerInfo
import com.example.data.model.DeviceCompatibilityInfo
import com.example.data.model.NetworkModeItem
import com.example.data.model.NetworkStatusInfo
import com.example.data.model.SimCardInfo
import com.example.data.telephony.TelephonyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppLanguage(val code: String, val title: String) {
    ARABIC("ar", "العربية"),
    ENGLISH("en", "English")
}

enum class AppNavDestination(val titleAr: String, val titleEn: String) {
    HOME("الرئيسية", "Status"),
    MODES("أوضاع الشبكة", "Network Modes"),
    ADVANCED("معلومات متقدمة", "Advanced"),
    COMPATIBILITY("توافق الجهاز", "Device Test"),
    SETTINGS("الإعدادات", "Settings")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = TelephonyRepository(application)

    private val _currentLanguage = MutableStateFlow(AppLanguage.ARABIC)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentNavDestination = MutableStateFlow(AppNavDestination.HOME)
    val currentNavDestination: StateFlow<AppNavDestination> = _currentNavDestination.asStateFlow()

    private val _simCards = MutableStateFlow<List<SimCardInfo>>(emptyList())
    val simCards: StateFlow<List<SimCardInfo>> = _simCards.asStateFlow()

    private val _selectedSimIndex = MutableStateFlow(0)
    val selectedSimIndex: StateFlow<Int> = _selectedSimIndex.asStateFlow()

    private val _networkStatus = MutableStateFlow(NetworkStatusInfo())
    val networkStatus: StateFlow<NetworkStatusInfo> = _networkStatus.asStateFlow()

    private val _cellTowers = MutableStateFlow<List<CellTowerInfo>>(emptyList())
    val cellTowers: StateFlow<List<CellTowerInfo>> = _cellTowers.asStateFlow()

    val networkModes: List<NetworkModeItem> = repository.getAvailableNetworkModes()

    private val _deviceCompatibility = MutableStateFlow(repository.getDeviceCompatibility())
    val deviceCompatibility: StateFlow<DeviceCompatibilityInfo> = _deviceCompatibility.asStateFlow()

    private val _autoRefreshSeconds = MutableStateFlow(3)
    val autoRefreshSeconds: StateFlow<Int> = _autoRefreshSeconds.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _switchResult = MutableStateFlow<TelephonyRepository.NetworkSwitchResult?>(null)
    val switchResult: StateFlow<TelephonyRepository.NetworkSwitchResult?> = _switchResult.asStateFlow()

    private val _selectedModeForDialog = MutableStateFlow<NetworkModeItem?>(null)
    val selectedModeForDialog: StateFlow<NetworkModeItem?> = _selectedModeForDialog.asStateFlow()

    private var refreshJob: Job? = null

    init {
        loadData()
        startPeriodicRefresh()
    }

    fun setDestination(dest: AppNavDestination) {
        _currentNavDestination.value = dest
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ARABIC) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.ARABIC
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setAutoRefresh(seconds: Int) {
        _autoRefreshSeconds.value = seconds
        startPeriodicRefresh()
    }

    fun selectSim(index: Int) {
        _selectedSimIndex.value = index
        loadData()
    }

    fun refreshManually() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadData()
            delay(400)
            _isRefreshing.value = false
        }
    }

    fun loadData() {
        val sims = repository.getActiveSimCards()
        _simCards.value = sims

        val currentSim = if (sims.isNotEmpty() && _selectedSimIndex.value in sims.indices) {
            sims[_selectedSimIndex.value]
        } else {
            sims.firstOrNull()
        }

        val subId = currentSim?.subscriptionId ?: -1
        val slotIndex = currentSim?.slotIndex ?: _selectedSimIndex.value

        _networkStatus.value = repository.getNetworkStatus(
            selectedSubId = subId,
            selectedSlotIndex = slotIndex
        )

        _cellTowers.value = repository.getCellTowers()
        _deviceCompatibility.value = repository.getDeviceCompatibility()
    }

    fun attemptApplyNetworkMode(mode: NetworkModeItem) {
        val currentSim = _simCards.value.getOrNull(_selectedSimIndex.value)
        val subId = currentSim?.subscriptionId ?: -1
        val result = repository.attemptSetNetworkMode(mode, subId)
        _switchResult.value = result
        _selectedModeForDialog.value = mode
        loadData()
    }

    fun dismissResultDialog() {
        _switchResult.value = null
        _selectedModeForDialog.value = null
    }

    private fun startPeriodicRefresh() {
        refreshJob?.cancel()
        val interval = _autoRefreshSeconds.value
        if (interval <= 0) return

        refreshJob = viewModelScope.launch {
            while (isActive) {
                delay(interval * 1000L)
                loadData()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }
}
