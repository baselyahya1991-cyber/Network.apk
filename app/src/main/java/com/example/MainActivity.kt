package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppLanguage
import com.example.ui.AppNavDestination
import com.example.ui.MainViewModel
import com.example.ui.components.NetworkModeResultDialog
import com.example.ui.components.SpiderTopBar
import com.example.ui.screens.AdvancedInfoScreen
import com.example.ui.screens.CompatibilityScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NetworkModesScreen
import com.example.ui.screens.SettingsAboutScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SpiderNetworkTheme
import com.example.ui.theme.SpiderRed
import com.example.ui.theme.SpiderRedLight
import com.example.ui.theme.SpiderWhite
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpiderNetworkTheme {
                SpiderApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SpiderApp(viewModel: MainViewModel) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val navDest by viewModel.currentNavDestination.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val simCards by viewModel.simCards.collectAsStateWithLifecycle()
    val selectedSimIndex by viewModel.selectedSimIndex.collectAsStateWithLifecycle()
    val cellTowers by viewModel.cellTowers.collectAsStateWithLifecycle()
    val compatibility by viewModel.deviceCompatibility.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val autoRefreshSec by viewModel.autoRefreshSeconds.collectAsStateWithLifecycle()
    val switchResult by viewModel.switchResult.collectAsStateWithLifecycle()
    val selectedModeForDialog by viewModel.selectedModeForDialog.collectAsStateWithLifecycle()

    var showSplash by remember { mutableStateOf(true) }

    // Runtime Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.loadData()
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val layoutDirection = if (language == AppLanguage.ARABIC) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (showSplash) {
            SplashScreen(
                language = language,
                onEnterApp = { showSplash = false }
            )
        } else {
            // Intercept Back button if on sub-screens
            BackHandler(enabled = navDest != AppNavDestination.HOME) {
                viewModel.setDestination(AppNavDestination.HOME)
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SpiderTopBar(
                        language = language,
                        generation = networkStatus.generation,
                        simCards = simCards,
                        selectedSimIndex = selectedSimIndex,
                        isRefreshing = isRefreshing,
                        onSelectSim = { viewModel.selectSim(it) },
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onRefresh = { viewModel.refreshManually() }
                    )
                },
                bottomBar = {
                    SpiderBottomNavigation(
                        currentDest = navDest,
                        language = language,
                        onSelectDest = { viewModel.setDestination(it) }
                    )
                },
                contentWindowInsets = WindowInsets.navigationBars
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(
                        targetState = navDest,
                        label = "screenTransition"
                    ) { dest ->
                        when (dest) {
                            AppNavDestination.HOME -> {
                                HomeScreen(
                                    status = networkStatus,
                                    simCards = simCards,
                                    selectedSimIndex = selectedSimIndex,
                                    language = language,
                                    onNavigate = { viewModel.setDestination(it) }
                                )
                            }
                            AppNavDestination.MODES -> {
                                NetworkModesScreen(
                                    modes = viewModel.networkModes,
                                    currentStatus = networkStatus,
                                    compatibility = compatibility,
                                    language = language,
                                    onSelectMode = { viewModel.attemptApplyNetworkMode(it) }
                                )
                            }
                            AppNavDestination.ADVANCED -> {
                                AdvancedInfoScreen(
                                    status = networkStatus,
                                    simCards = simCards,
                                    selectedSimIndex = selectedSimIndex,
                                    cellTowers = cellTowers,
                                    hasFineLocationPermission = compatibility.fineLocationGranted,
                                    language = language,
                                    onRefresh = { viewModel.refreshManually() },
                                    onRequestLocationPermission = {
                                        permissionLauncher.launch(
                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                                        )
                                    }
                                )
                            }
                            AppNavDestination.COMPATIBILITY -> {
                                CompatibilityScreen(
                                    compatibility = compatibility,
                                    language = language
                                )
                            }
                            AppNavDestination.SETTINGS -> {
                                SettingsAboutScreen(
                                    currentLanguage = language,
                                    autoRefreshSeconds = autoRefreshSec,
                                    onSetLanguage = { viewModel.setLanguage(it) },
                                    onSetAutoRefresh = { viewModel.setAutoRefresh(it) },
                                    onManualRefresh = { viewModel.refreshManually() },
                                    onRequestPermissions = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.READ_PHONE_STATE,
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Result dialog when user tests or clicks any network mode
                    if (switchResult != null && selectedModeForDialog != null) {
                        NetworkModeResultDialog(
                            mode = selectedModeForDialog!!,
                            result = switchResult!!,
                            language = language,
                            onDismiss = { viewModel.dismissResultDialog() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpiderBottomNavigation(
    currentDest: AppNavDestination,
    language: AppLanguage,
    onSelectDest: (AppNavDestination) -> Unit
) {
    NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.border(0.5.dp, DarkOutline, androidx.compose.ui.graphics.RectangleShape)
    ) {
        val items = listOf(
            NavNavItem(
                dest = AppNavDestination.HOME,
                icon = Icons.Default.CellTower,
                titleAr = "الرئيسية",
                titleEn = "Status"
            ),
            NavNavItem(
                dest = AppNavDestination.MODES,
                icon = Icons.Default.Tune,
                titleAr = "الأوضاع",
                titleEn = "Modes"
            ),
            NavNavItem(
                dest = AppNavDestination.ADVANCED,
                icon = Icons.Default.Speed,
                titleAr = "متقدم",
                titleEn = "Advanced"
            ),
            NavNavItem(
                dest = AppNavDestination.COMPATIBILITY,
                icon = Icons.Default.Memory,
                titleAr = "التوافق",
                titleEn = "Device"
            ),
            NavNavItem(
                dest = AppNavDestination.SETTINGS,
                icon = Icons.Default.Settings,
                titleAr = "الإعدادات",
                titleEn = "Settings"
            )
        )

        items.forEach { item ->
            val isSelected = currentDest == item.dest
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectDest(item.dest) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.titleEn,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = if (language == AppLanguage.ARABIC) item.titleAr else item.titleEn,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SpiderRed,
                    selectedTextColor = SpiderRedLight,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = DarkSurfaceElevated
                ),
                modifier = Modifier.testTag("nav_item_${item.dest.name.lowercase()}")
            )
        }
    }
}

private data class NavNavItem(
    val dest: AppNavDestination,
    val icon: ImageVector,
    val titleAr: String,
    val titleEn: String
)
