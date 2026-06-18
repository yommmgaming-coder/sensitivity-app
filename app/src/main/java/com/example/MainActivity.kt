package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Chat
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.SensitivityRepository
import com.example.ui.FFSensViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "sens_pro_database"
        ).fallbackToDestructiveMigration().build()
    }

    private val repository by lazy {
        SensitivityRepository(database)
    }

    private val viewModel: FFSensViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FFSensViewModel(application, repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val adminNotification by viewModel.adminNotification.collectAsState()

                // Listen to admin events and show native user toasts
                LaunchedEffect(adminNotification) {
                    adminNotification?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearAdminNotification()
                    }
                }

                val navItems = listOf(
                    BottomNavItem("dashboard", "Home", Icons.Default.Dashboard),
                    BottomNavItem("sens_analyzer", "AI Sens", Icons.Default.FilterList),
                    BottomNavItem("training_gym", "AI Gym", Icons.Default.Sports),
                    BottomNavItem("community_hub", "Community", Icons.Default.Group),
                    BottomNavItem("chat_coach", "AI Coach", Icons.Default.Chat)
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF0D0E12),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        
                        NavigationBar(
                            containerColor = Color(0xFF161B22),
                            contentColor = Color.White,
                            tonalElevation = 8.dp
                        ) {
                            navItems.forEach { item ->
                                val selected = currentRoute == item.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        if (currentRoute != item.route) {
                                            navController.navigate(item.route) {
                                                popUpTo("dashboard") {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selected) Color(0xFFFF4B2B) else Color.Gray
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = Color(0xFFFF4B2B),
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = Color(0xFFFFBC00)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigate = { direction -> navController.navigate(direction) }
                            )
                        }
                        composable("sens_analyzer") {
                            SensitivityAnalyzerScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("headshot_opt") {
                            OptimizerScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNavigate = { destination -> navController.navigate(destination) }
                            )
                        }
                        composable("lag_scanner") {
                            PerformanceScannerScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("training_gym") {
                            TrainingScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("community_hub") {
                            CommunityScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("chat_coach") {
                            ChatScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("drag_test_screen") {
                            DragTestFullScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("hud_visualizer") {
                            HudVisualizerScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("admin_panel") {
                            AdminScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
