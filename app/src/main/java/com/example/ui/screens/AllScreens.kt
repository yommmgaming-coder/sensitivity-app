package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.data.*
import com.example.ui.*
import java.io.File
import kotlinx.coroutines.delay
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import android.os.SystemClock

// --- CUSTOM STYLE ELEMENTS ---
val CarbonBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF151923), Color(0xFF0F111A))
)
val FireOrangeGoldBrush = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF5722), Color(0xFFFFBC00))
)
val EmeraldSuccessBrush = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00E676), Color(0xFF00B0FF))
)

// --- COMPONENET: NAVIGATION HEADER ---
// --- COMPONENT: NAVIGATION HEADER ---
@Composable
fun ScreenHeader(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF161B22))
            .drawBehind {
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = Color(0xFFFF4B2B)
                    )
                }
            } else {
                // Professional Polish FF Brand Logo emblem
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFF4B2B), Color(0xFFFF0600))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FF",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
            
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.2.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PRO",
                        color = Color(0xFFFF4B2B),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    )
                }
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
        
        // Notifications & Avatar HUD actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .clickable { /* Active HUD Alert */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), CircleShape)
            )
        }
    }
}

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(
    viewModel: FFSensViewModel,
    onNavigate: (String) -> Unit
) {
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val sensitivities by viewModel.sensitivitiesList.collectAsState()
    
    // Check if hero image file exists
    val bannerFile = File("/app/src/main/res/drawable/ff_hero_banner_1781768255269.jpg")

    Scaffold(
        topBar = {
            ScreenHeader(title = "Sensitivity", subtitle = "Tactile game optimizer")
        },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D0E12))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Image Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(Color(0xFFFF4B2B), Color(0xFFFF7000)))),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    if (bannerFile.exists()) {
                        Image(
                            painter = rememberAsyncImagePainter(bannerFile),
                            contentDescription = "Free Fire Sensitivity Pro Hero Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Modern styled carbon gaming placeholder with glowing neon circles
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(CarbonBrush)
                                .drawBehind {
                                    drawCircle(
                                        color = Color(0xFFFF4B2B).copy(alpha = 0.15f),
                                        radius = 200f,
                                        center = Offset(size.width, 0f)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Whatshot,
                                    contentDescription = "Fire Icon",
                                    tint = Color(0xFFFF4B2B),
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "AI FREE FIRE SENSITIVITY PRO",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                    
                    // Overlay Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "AI ACTIVE",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFFFBC00)
                        )
                    }
                }
            }

            // Current Detection HUD Card (Matches Samsung layout)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF1C1F26))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    // Right background icon overlay
                    Icon(
                        imageVector = Icons.Default.DeveloperBoard,
                        contentDescription = "Memory Tech",
                        tint = Color.White.copy(alpha = 0.03f),
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.CenterEnd)
                    )
                    
                    Column {
                        Text(
                            text = "CURRENT DETECTION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            ),
                            color = Color(0xFFFF4B2B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = (selectedDevice?.model ?: "SAMSUNG S23 ULTRA").uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("REFRESH RATE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(selectedDevice?.refreshRate ?: "120Hz", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Color.White.copy(alpha = 0.10f))
                            )
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text("RAM SIZE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(selectedDevice?.ram ?: "12.0 GB", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Color.White.copy(alpha = 0.10f))
                            )
                            
                            Column(modifier = Modifier.weight(1.5f)) {
                                Text("CHIPSET", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text((selectedDevice?.processor ?: "SD 8 GEN 2").uppercase(), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                        }
                    }
                }
            }

            // AI RECOMMENDED SETTINGS GRID Preview
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "AI RECOMMENDED SETTINGS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF4B2B).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "UPDATED 2H AGO",
                                color = Color(0xFFFF4B2B),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // General Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1C1F26))
                                .drawBehind {
                                    // Highlight left margin element
                                    drawRect(
                                        color = Color(0xFFFF4B2B),
                                        topLeft = Offset(0f, 0f),
                                        size = androidx.compose.ui.geometry.Size(12f, size.height)
                                    )
                                }
                                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Text("GENERAL", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("98", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                        }
                        
                        // Red Dot Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1C1F26))
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Text("RED DOT", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("92", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 2X scope
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1C1F26))
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Text("2X SCOPE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("85", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                        }
                        
                        // 4x scope
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1C1F26))
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Text("4X SCOPE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("80", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                        }
                    }
                }
            }

            // Headshot Optimizer Quick Gradient Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFFFF4B2B), Color(0xFFFF7000))))
                        .clickable { onNavigate("headshot_opt") }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Optimizer",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text("HEADSHOT OPTIMIZER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Drag speed synchronized with DPI", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("APPLY", color = Color(0xFFFF4B2B), fontWeight = FontWeight.Black, fontSize = 10.sp)
                    }
                }
            }

            // Low Lag Verification Status Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF161B22))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF00FF00), CircleShape)
                    )
                    Text(
                        text = "DEVICE STATUS: OPTIMIZED FOR COMPETITIVE PLAY (LOW LAG)",
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Feature Navigation Buttons Grid
            item {
                Text(
                    "AI TACTICAL SERVICES",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White
                )
            }

            item {
                DashboardItemRow(
                    title_1 = "SENS ANALYZER",
                    subtitle_1 = "Tune phone tactile rates",
                    icon_1 = Icons.Default.FilterList,
                    tag_1 = "sens_analyzer",
                    title_2 = "HEADSHOT GEAR",
                    subtitle_2 = "Drag & DPI modifiers",
                    icon_2 = Icons.Default.OfflineBolt,
                    tag_2 = "headshot_opt",
                    onNavigate = onNavigate
                )
            }

            item {
                DashboardItemRow(
                    title_1 = "LAG SCANNER",
                    subtitle_1 = "Device performance scan",
                    icon_1 = Icons.Default.Speed,
                    tag_1 = "lag_scanner",
                    title_2 = "AI COACHING GYM",
                    subtitle_2 = "Rank & style training",
                    icon_2 = Icons.Default.Sports,
                    tag_2 = "training_gym",
                    onNavigate = onNavigate
                )
            }

            item {
                DashboardItemRow(
                    title_1 = "COMMUNITY HUD",
                    subtitle_1 = "Trending sensitivities",
                    icon_1 = Icons.Default.Group,
                    tag_1 = "community_hub",
                    title_2 = "AI CHAT COACH",
                    subtitle_2 = "Ask grandmaster tactile advice",
                    icon_2 = Icons.Default.Chat,
                    tag_2 = "chat_coach",
                    onNavigate = onNavigate
                )
            }

            // Admin Panel Quick Launch
            item {
                Button(
                    onClick = { onNavigate("admin_panel") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF161B22)),
                    border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("admin_nav_button")
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin View", tint = Color(0xFFFFBC00), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "LAUNCH DEVELOPER ADMIN CENTER",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardItemRow(
    title_1: String,
    subtitle_1: String,
    icon_1: androidx.compose.ui.graphics.vector.ImageVector,
    tag_1: String,
    title_2: String,
    subtitle_2: String,
    icon_2: androidx.compose.ui.graphics.vector.ImageVector,
    tag_2: String,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            onClick = { onNavigate(tag_1) },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B26)),
            border = BorderStroke(1.dp, Color(0xFF222938)),
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
                .testTag("${tag_1}_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(icon_1, contentDescription = title_1, tint = Color(0xFFFF5722), modifier = Modifier.size(32.dp))
                Column {
                    Text(
                        title_1,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        subtitle_1,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
        }

        Card(
            onClick = { onNavigate(tag_2) },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B26)),
            border = BorderStroke(1.dp, Color(0xFF222938)),
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
                .testTag("${tag_2}_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(icon_2, contentDescription = title_2, tint = Color(0xFFFF5722), modifier = Modifier.size(32.dp))
                Column {
                    Text(
                        title_2,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        subtitle_2,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. SENSITIVITY ANALYZER SCREEN (Device specs & recommended settings)
// ==========================================
@Composable
fun SensitivityAnalyzerScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analyzedSetting by viewModel.analyzedSetting.collectAsState()
    val devices by viewModel.devicesList.collectAsState()

    var showDevicePicker by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) } // 0 = Auto-Detect Rig, 1 = Custom AI Tuning Form

    // Custom Input Form States
    var customBrand by remember { mutableStateOf("Asus") }
    var customModel by remember { mutableStateOf("ROG Phone 8") }
    var customRam by remember { mutableStateOf("16GB") }
    var customProcessor by remember { mutableStateOf("Snapdragon 8 Gen 3") }
    var customRefreshRate by remember { mutableStateOf("120 Hz") }
    var playstyleRole by remember { mutableStateOf("Rusher") }
    var dragTechnique by remember { mutableStateOf("J-Curve Flick") }

    Scaffold(
        topBar = { ScreenHeader(title = "AI Sens Analyzer", subtitle = "Tactile screen tuning", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tab layout selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { activeTab = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeTab == 0) Color(0xFFFF4B2B) else Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Auto-Detect Rig", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { activeTab = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeTab == 1) Color(0xFFFF4B2B) else Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Custom AI Tuning", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (activeTab == 0) {
                // Selected Device Details
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("ACTIVE RIG FOR CALCULATION", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))
                                Text(
                                    "${selectedDevice?.brand} ${selectedDevice?.model}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { showDevicePicker = !showDevicePicker },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF161B22)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("SWITCH DEVICE", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        if (showDevicePicker) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Popular specifications in database:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                devices.forEach { dev ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (dev.model == selectedDevice?.model) Color(0xFF161B22) else Color.Transparent,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable {
                                                viewModel.updateSelectedDevice(dev)
                                                showDevicePicker = false
                                            }
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${dev.brand} ${dev.model}", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                                        Text(dev.processor, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        Box(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        // Specs details
                        selectedDevice?.let { dev ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                SpecBadge("RAM", dev.ram)
                                SpecBadge("REFRESH", dev.refreshRate)
                                SpecBadge("CPU", dev.processor.take(14))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                SpecBadge("RESOLUTE", dev.resolution)
                                SpecBadge("OS", dev.osVersion.take(16))
                                SpecBadge("SYS DPI", "${dev.dpi} DPI")
                            }
                        }
                    }
                }

                // Calculation Action Button
                Button(
                    onClick = { selectedDevice?.let { viewModel.generateRecommendedSensitivity(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("calculate_sens_btn"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isAnalyzing
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("AI IS COMPUTING SENSITIVITIES...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Memory, contentDescription = "Compile settings", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPTIMIZE & SOLVE SENSITIVITY", fontWeight = FontWeight.ExtraBold)
                    }
                }
            } else {
                // Custom Form Tab
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("CUSTOM DEVICE SPECIFICATION DETAILS", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))

                        // Brand & Model
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextField(
                                value = customBrand,
                                onValueChange = { customBrand = it },
                                label = { Text("Brand", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).testTag("custom_brand_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0D0E12),
                                    unfocusedContainerColor = Color(0xFF0D0E12),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.LightGray
                                )
                            )
                            TextField(
                                value = customModel,
                                onValueChange = { customModel = it },
                                label = { Text("Model", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).testTag("custom_model_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0D0E12),
                                    unfocusedContainerColor = Color(0xFF0D0E12),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.LightGray
                                )
                            )
                        }

                        // RAM & Processor
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextField(
                                value = customRam,
                                onValueChange = { customRam = it },
                                label = { Text("RAM (e.g. 8GB)", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).testTag("custom_ram_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0D0E12),
                                    unfocusedContainerColor = Color(0xFF0D0E12),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.LightGray
                                )
                            )
                            TextField(
                                value = customProcessor,
                                onValueChange = { customProcessor = it },
                                label = { Text("Processor/Chipset", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).testTag("custom_processor_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF0D0E12),
                                    unfocusedContainerColor = Color(0xFF0D0E12),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.LightGray
                                )
                            )
                        }

                        // Refresh rate selector
                        Text("DISPLAY SCREEN REFRESH RATE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("60 Hz", "90 Hz", "120 Hz", "144 Hz").forEach { rate ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (customRefreshRate == rate) Color(0xFFFFBC00) else Color(0xFF161B22),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { customRefreshRate = rate }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(rate, color = if (customRefreshRate == rate) Color.Black else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Box(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        // PLAYSTYLE ROLE Choice
                        Text("PLAYSTYLE / ROLE IN COMBAT", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Rusher", "Sniper", "Support", "Flanker").forEach { role ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (playstyleRole == role) Color(0xFFFF4B2B) else Color(0xFF161B22),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { playstyleRole = role }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(role, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // DRAG / SHOOT STYLE Choice
                        Text("DRAG ACCELERATION STYLE", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("J-Curve Flick", "Linear Up", "Full Spray").forEach { technique ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (dragTechnique == technique) Color(0xFFFFBC00) else Color(0xFF161B22),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { dragTechnique = technique }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(technique, color = if (dragTechnique == technique) Color.Black else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // AI Action Trigger for Custom Preset
                Button(
                    onClick = {
                        viewModel.generateCustomRecommendedSensitivity(
                            brand = customBrand,
                            model = customModel,
                            ram = customRam,
                            processor = customProcessor,
                            refreshRate = customRefreshRate,
                            playstyle = playstyleRole,
                            dragStyle = dragTechnique
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("generate_custom_sens_btn"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isAnalyzing
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("AI IS COMPUTING SPECIFICATIONS...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Build, contentDescription = "AI settings", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GENERATE AI CUSTOM RECOMMENDATION", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            // Computed settings output
            analyzedSetting?.let { sens ->
                Text("OPTIMAL RECOIL CALIBRATION", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color(0xFFFF4B2B).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("TACTILE ACCELERATION INDEX (FREE FIRE)", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))

                        SensitivityBar("General Sensitivity", sens.general, Color(0xFFFF4B2B))
                        SensitivityBar("Red Dot Sight", sens.redDot, Color(0xFFFF4B2B))
                        SensitivityBar("2X Scope Zoom", sens.scope2X, Color(0xFFFFBC00))
                        SensitivityBar("4X Scope Zoom", sens.scope4X, Color(0xFFFFBC00))
                        SensitivityBar("AWM Sniper Scope", sens.awm, Color(0xFF00FF00))
                        SensitivityBar("Free Look Movement", sens.freeLook, Color(0xFF00E676))

                        Box(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        // Headshot tips
                        Text("AI Drag Aim Calibration Method:", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF4B2B))
                        Text(
                            sens.aimPrecision,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        
                        val context = LocalContext.current
                        var exportStatus by remember { mutableStateOf<String?>(null) }
                        
                        Button(
                            onClick = {
                                try {
                                    val uri = com.example.ui.util.ImageExporter.generateAndSaveSensitivityCard(context, sens)
                                    if (uri != null) {
                                        android.widget.Toast.makeText(context, "🎮 Pro Setup Card Saved to Gallery!", android.widget.Toast.LENGTH_LONG).show()
                                        exportStatus = "SAVED TO GALLERY"
                                    } else {
                                        android.widget.Toast.makeText(context, "⚠️ Save failed. Try writing again.", android.widget.Toast.LENGTH_LONG).show()
                                        exportStatus = "EXPORT FAILED"
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    android.widget.Toast.makeText(context, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F1115)),
                            border = BorderStroke(1.dp, Color(0xFF00FF00)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("export_card_gallery_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Download Card", tint = Color(0xFF00FF00))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = exportStatus ?: "DOWNLOAD ESPORTS PROFILE CARD",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecBadge(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFF0C0E14), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label.uppercase(), fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold, maxLines = 1, textAlign = TextAlign.Center)
    }
}

@Composable
fun SensitivityBar(label: String, value: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$value", color = color, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold)
                Text(" / 200", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF0C0E14))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((value / 200f).coerceIn(0f, 1f))
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// ==========================================
// 3. HEADSHOT OPTIMIZER SCREEN
// ==========================================
@Composable
fun OptimizerScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val sensitivities by viewModel.sensitivitiesList.collectAsState()
 
    // Retrieve last analyzed or calculate static recommended settings
    val activeSens = sensitivities.firstOrNull()
 
    Scaffold(
        topBar = { ScreenHeader(title = "AI Headshot Optimizer", subtitle = "Drag acceleration techniques", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Interactive 3D Screen Mockup visualizing optimal HUD Touch Target
            Text("AI TARGET COMPONENT LOCK INTERFACE (HUD)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(20.dp))
                    .background(CarbonBrush)
                    .padding(12.dp)
            ) {
                // Represents phone bounds
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(BorderStroke(1.dp, Color(0xFFFF4B2B).copy(alpha = 0.3f)), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    // Left joystick anchor
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.CenterStart)
                            .border(BorderStroke(2.dp, Color.Gray.copy(alpha = 0.4f)), CircleShape)
                    ) {
                        Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color.Gray).align(Alignment.Center))
                    }
 
                    // Gloo Wall Button Mock (e-sports recommended left placement)
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2196F3).copy(alpha = 0.4f))
                            .border(BorderStroke(1.5.dp, Color(0xFF2196F3)), RoundedCornerShape(10.dp))
                            .align(Alignment.TopStart)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = "Wall", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
 
                    // Scope Right trigger button (right scale)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFBC00).copy(alpha = 0.3f))
                            .border(BorderStroke(1.5.dp, Color(0xFFFFBC00)), CircleShape)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.TrackChanges, contentDescription = "Aim", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
 
                    // Fire Button placement (esports highlight)
                    var fireBtnSize by remember { mutableStateOf(45f) }
                    Column(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("RIGHT FIRE Button: ${fireBtnSize.toInt()}%", fontSize = 9.sp, color = Color(0xFFFF4B2B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(fireBtnSize.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF4B2B).copy(alpha = 0.4f))
                                .border(BorderStroke(2.dp, Color(0xFFFF4B2B)), CircleShape)
                                .clickable {
                                    // Let users interactively try scaling the fire button
                                    fireBtnSize = if (fireBtnSize >= 55f) 38f else fireBtnSize + 5f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Adjust, contentDescription = "Fire Button Trigger", tint = Color.White)
                        }
                    }
 
                    Text(
                        "Drag Crosshair Path\n[ Swipe up direction: J-curve ]",
                        fontSize = 11.sp,
                        color = Color.Green,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
 
            // Dynamic Fire Button & Best Drag Advisor
            Text("AI TARGET ALIGNMENT & DRAG CALIBRATOR", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            
            var selectedStyle by remember { mutableStateOf("rush") } // "rush", "one_tap", "ar_mid", "sniper"
            
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.3f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("SELECT YOUR CURRENT PREFERRED WEAPON CLASS", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val stylesList = listOf(
                            Triple("rush", "SMG / Rush", Color(0xFFFF4B2B)),
                            Triple("one_tap", "Shotgun / 1-Tap", Color(0xFFFFBC00)),
                            Triple("ar_mid", "AR HeadSnap", Color(0xFF00FF00)),
                            Triple("sniper", "Sniper Trick", Color(0xFF00E676))
                        )
                        stylesList.forEach { (id, label, accent) ->
                            val active = selectedStyle == id
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) accent.copy(alpha = 0.2f) else Color(0xFF0D0E12))
                                    .border(BorderStroke(1.dp, if (active) accent else Color.Transparent), RoundedCornerShape(8.dp))
                                    .clickable { selectedStyle = id }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active) Color.White else Color.Gray,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    
                    val recButtonSize = when (selectedStyle) {
                        "rush" -> "44% - 48%"
                        "one_tap" -> "52% - 56%"
                        "ar_mid" -> "38% - 42%"
                        else -> "48% - 52%"
                    }
                    
                    val recommendedDragType = when (selectedStyle) {
                        "rush" -> "Instant J-Curl Drag: Pull downwards briefly to anchor tracking, then snap violently upwards."
                        "one_tap" -> "Super Fast Straight Flick: Instant high-velocity flick straight upward without delay."
                        "ar_mid" -> "Steady Linear Drag: Medium pace constant upward slide. Keeps crosshair from spreading."
                        else -> "Snap-Scope Drag: Quick Scope button click instant drag to targets head."
                    }
                    
                    val dragSensitivitySpeed = when (selectedStyle) {
                        "rush" -> "CRITICAL SPEED (Swipe speed needs to be exceptionally high to override SMG default lock-on)"
                        "one_tap" -> "HIGH SPEED SNAP (A single instantaneous flick to burst before recoil spreads)"
                        "ar_mid" -> "CONTROLLED VELOCITY (Steady pace to minimize over-shooting the enemy head)"
                        else -> "RAPID MICRO-DRAG (Tiny high-dpi snap coordinate)"
                    }

                    // Stats row 
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0E12)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("IDEAL FIRE BUTTON", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(recButtonSize, fontSize = 16.sp, color = Color(0xFFFF4B2B), fontWeight = FontWeight.ExtraBold)
                                Text("HUD Size", fontSize = 9.sp, color = Color.LightGray)
                            }
                        }
                        
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0E12)),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("SPEED REQUIREMENT", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (selectedStyle == "rush") "EXTREME (180+)" else if (selectedStyle == "one_tap") "VERY FAST (160+)" else "MODERATE (120+)",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFBC00),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text("Recommended Drag Speed", fontSize = 9.sp, color = Color.LightGray)
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                    Text("BEST DRAG TECHNIQUE SYSTEM", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))
                    Text(recommendedDragType, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Text(dragSensitivitySpeed, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)

                    // Interactive Drag Practice Test Area
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))
                    
                    Text("DRAG SPEED SNAP SIMULATOR", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00FF00))
                    
                    var practiceFeedback by remember { mutableStateOf("Hold circular button and swipe straight up quickly!") }
                    var hasSwipeRegistered by remember { mutableStateOf(false) }
                    var swipeVelocityRating by remember { mutableStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color(0xFF0D0E12), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(practiceFeedback, fontSize = 11.sp, color = if (hasSwipeRegistered) Color(0xFF00E676) else Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 4.dp))
                            
                            // Drag simulator slider/bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Touch drag point
                                var offsetY by remember { mutableStateOf(0f) }
                                var startTime by remember { mutableStateOf(0L) }

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF4B2B).copy(alpha = 0.2f))
                                        .border(BorderStroke(2.dp, Color(0xFFFF4B2B)), CircleShape)
                                        .pointerInput(Unit) {
                                            detectDragGestures(
                                                onDragStart = {
                                                    startTime = SystemClock.uptimeMillis()
                                                    offsetY = 0f
                                                    hasSwipeRegistered = false
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    offsetY += dragAmount.y
                                                },
                                                onDragEnd = {
                                                    val duration = SystemClock.uptimeMillis() - startTime
                                                    if (offsetY < -10f && duration > 10) {
                                                        val pixelsPerMs = -offsetY / duration
                                                        swipeVelocityRating = pixelsPerMs * 100f
                                                        hasSwipeRegistered = true
                                                        practiceFeedback = when {
                                                            swipeVelocityRating > 180f -> "⚡ ULTRA DRAG! Extreme Headshot precision registered!"
                                                            swipeVelocityRating > 110f -> "🎯 OPTIMAL SPEED! Perfect drag velocity reached!"
                                                            else -> "⚠️ SLOW DRAG! Snap upwards faster to unlock the aim block."
                                                        }
                                                    } else {
                                                        practiceFeedback = "Swipe upward from the button!"
                                                    }
                                                }
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.TouchApp, contentDescription = "Practice Drag", tint = Color.White)
                                }
                            }
                            
                            if (hasSwipeRegistered) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Flick Speed Rating: ${swipeVelocityRating.toInt()}", fontSize = 10.sp, color = Color.Gray)
                                    Text("Target: ${if (selectedStyle == "rush") "Speed > 180" else "Speed > 110"}", fontSize = 10.sp, color = Color.Gray)
                                }
                            } else {
                                Text("Drag upwards in a single swift movement", fontSize = 9.sp, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onNavigate("drag_test_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("enter_full_drag_lab_button"),
                        border = BorderStroke(1.5.dp, Color(0xFFFFBC00))
                    ) {
                        Icon(Icons.Default.SportsEsports, contentDescription = "Esports Lab", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPEN FULLSCREEN ADVANCED DRAG LAB", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onNavigate("hud_visualizer") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF131722)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("enter_hud_visualizer_button"),
                        border = BorderStroke(1.5.dp, Color(0xFF00FF00))
                    ) {
                        Icon(Icons.Default.Games, contentDescription = "HUD Visualizer", tint = Color(0xFF00FF00))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("INTERACTIVE HUD LAYOUT VISUALIZER", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            // Recommendations Cards
            Text("COACHING HARDWARE TUNING MANUAL", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
 
            GridOptimizerCard(
                title = "DPI TACTIC MODIFIER",
                desc = "We recommend ${selectedDevice?.dpi?.plus(60) ?: 480} DPI for rapid turnarounds. Open your Android Developer Options and configure 'Smallest Width' safely.",
                icon = Icons.Default.AspectRatio,
                accent = Color(0xFFFFBC00)
            )
 
            GridOptimizerCard(
                title = "AIM ACCELERATION SPEED",
                desc = "Set Pointer Speed to 80% inside System Languages & Input options. Eliminates weapon jitter when swiping upward.",
                icon = Icons.Default.FilterCenterFocus,
                accent = Color(0xFFFF4B2B)
            )
 
            GridOptimizerCard(
                title = "GRAPHICS & REFRESH RATIO",
                desc = "FPS configuration is mandatory: Select 'SMOOTH' style graphics, toggle 'HIGH FPS' to ON. Increases refresh frequency of drawing updates to register aim snapshots.",
                icon = Icons.Default.PersonalVideo,
                accent = Color(0xFF00E676)
            )
        }
    }
}
 
@Composable
fun GridOptimizerCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = title,
                tint = accent,
                modifier = Modifier
                    .size(44.dp)
                    .background(accent.copy(alpha = 0.1f), CircleShape)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold), color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(desc, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
            }
        }
    }
}

// ==========================================
// 4. LAG PERFORMANCE SCANNER SCREEN
// ==========================================
@Composable
fun PerformanceScannerScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val isScanning by viewModel.isScanning.collectAsState()
    val logs by viewModel.scannerLogs.collectAsState()
    val report by viewModel.scannerReport.collectAsState()

    Scaffold(
        topBar = { ScreenHeader(title = "Lag Diagnostics", subtitle = "Solve heating and game spikes", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("DEVICE HEALTH TESTER", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isScanning) {
                        CircularProgressIndicator(
                            color = Color(0xFFFF4B2B),
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(60.dp)
                        )
                    } else {
                        IconButton(
                            onClick = { viewModel.runPerformanceDiagnostics() },
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFFFF4B2B).copy(alpha = 0.1f), CircleShape)
                                .border(BorderStroke(2.dp, Color(0xFFFF4B2B)), CircleShape)
                                .testTag("trigger_scan_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Run scanner", tint = Color(0xFFFF4B2B), modifier = Modifier.size(36.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        if (isScanning) "DIAGNOSTIC CRON RUNNING..." else "START HIGH-RATE LAG DIAGNOSTIC",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        "Scans allocated memory heap and system temperatures.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Real-time log monitoring terminal
            if (logs.isNotEmpty()) {
                Text("PROCESS CONSOLE OUTPUT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        logs.forEach { log ->
                            Text(
                                log,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = if (log.contains("🌡️") || log.contains("📱")) Color(0xFFFFBC00) else Color.Green
                            )
                        }
                    }
                }
            }

            // Computed Optimizer Diagnostics Report
            report?.let { rep ->
                Text("DIAGNOSTICS REPORT CARD", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DiagnosticMiniCard("Lag Rating", "${rep.lagScore}/100", Color(0xFFFF4B2B), Modifier.weight(1f))
                    DiagnosticMiniCard("Sys Temp", rep.activeThermalLevel, Color(0xFFFFBC00), Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DiagnosticMiniCard("Junk Cache", "${rep.junkMemoryMb} MB", Color(0xFF2196F3), Modifier.weight(1f))
                    DiagnosticMiniCard("Frame Sync", rep.fpsStability, Color(0xFF00E676), Modifier.weight(1f))
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("RECOMMENDED HARDWARE TWEAKS", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF9100))
                        rep.optimizationTips.forEach { tip ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text("• ", color = Color(0xFFFF9100), fontWeight = FontWeight.Bold)
                                Text(tip, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosticMiniCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 14.sp, color = color, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// ==========================================
// 5. AI TRAINING MODE SCREEN
// ==========================================
data class PlaystyleDrill(
    val title: String,
    val description: String,
    val durationMin: Int,
    val trainingGroundTask: String
)

@Composable
fun TrainingScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val isGenerating by viewModel.isGeneratingTraining.collectAsState()
    val report by viewModel.trainingReport.collectAsState()

    var selectedRank by remember { mutableStateOf("Diamond III") }
    var selectedPlaystyle by remember { mutableStateOf("Rush") }

    val playstyles = listOf("Rush", "Sniper", "Support", "All-Rounder")
    val ranks = listOf("Bronze/Silver", "Platinum", "Diamond III", "Heroic", "Grandmaster")

    // Interactive Prebuilt Daily Drill Sets
    val prebuiltDrills = remember {
        mapOf(
            "Rush" to listOf(
                PlaystyleDrill(
                    title = "J-Curve Shotgun Flicking",
                    description = "Equip M1887 or MAG-7. Swipe fire button in a swift J-curve direction to lock onto bots' heads rapidly.",
                    durationMin = 10,
                    trainingGroundTask = "Deliver 30 headshot kills in a row with shotguns."
                ),
                PlaystyleDrill(
                    title = "180° Gloo Wall Fast-Drop",
                    description = "Sprint forward, fire one shell, tap the Gloo Wall button, crouch immediately, and deploy covering armor behind your viewport.",
                    durationMin = 5,
                    trainingGroundTask = "Execute the escape-deploy sequence 20 times cleanly without errors."
                ),
                PlaystyleDrill(
                    title = "Close-Range Jump-Strafe Peeks",
                    description = "Perform continuous jump-strafes around artificial pillars in the range, keeping your crosshair locked onto chest/head level.",
                    durationMin = 5,
                    trainingGroundTask = "Kill 15 bots around corners without taking virtual counter-damage."
                )
            ),
            "Sniper" to listOf(
                PlaystyleDrill(
                    title = "Double Sniper Quick-Switch",
                    description = "Equip dual AWMs or M82Bs. Fire your first rifle, instantly tap the quick-switch button, and aim/fire the second sniper right away.",
                    durationMin = 10,
                    trainingGroundTask = "Chain 3 consecutive bullet impacts against moving bots at 80 meters."
                ),
                PlaystyleDrill(
                    title = "Microscopic Drag Tuning",
                    description = "Keep your 4x/8x scope zoomed in slightly away from target. Move your right hand in micro-ticks to snap onto the head area.",
                    durationMin = 10,
                    trainingGroundTask = "Secure 20 headshots using scope micro-adjustment snaps."
                ),
                PlaystyleDrill(
                    title = "Moving Target Prediction",
                    description = "Follow vehicles or running silhouettes. Place crosshair 2-3 meters in front of the target path vector to adjust for latency and speed.",
                    durationMin = 5,
                    trainingGroundTask = "Knock down 10 fast-moving dummy driver outlines."
                )
            ),
            "Support" to listOf(
                PlaystyleDrill(
                    title = "M416-Y Recoil Control",
                    description = "Trigger a continuous AR bullet burst at medium range. Maintain constant pressure dragging your thumb downwards at a counter-pace.",
                    durationMin = 10,
                    trainingGroundTask = "Group 90% of a 30-round magazine within the central target zone."
                ),
                PlaystyleDrill(
                    title = "Zig-Zag Smoke Escorter",
                    description = "Deploy 3 smoke points in a zig-zag coordinate line. Slide between smoke clouds to cover teammates' repositioning.",
                    durationMin = 5,
                    trainingGroundTask = "Execute 10 smoke-covered dummy recovery exercises under fire."
                ),
                PlaystyleDrill(
                    title = "UAV Wall Arc Grenades",
                    description = "Toggle visual trajectory arc of Flashbangs or Grenades over simulated compound walls utilizing high-angle projection physics.",
                    durationMin = 5,
                    trainingGroundTask = "Land 15 grenade tosses cleanly inside standard window structures."
                )
            ),
            "All-Rounder" to listOf(
                PlaystyleDrill(
                    title = "360° Shield Encase",
                    description = "Instantly construct a complete circles of Gloo Walls encircling yourself in less than 1.5 seconds.",
                    durationMin = 5,
                    trainingGroundTask = "Execute 10 complete 360-degree defensive circles perfectly."
                ),
                PlaystyleDrill(
                    title = "Hip-Fire Head Leveling",
                    description = "Navigate through training checkpoints keeping your primary crosshair circle strictly aligned with the bots' head plane.",
                    durationMin = 10,
                    trainingGroundTask = "Maintain a head-level amber crosshair through 5 laps of the course."
                ),
                PlaystyleDrill(
                    title = "Desert Eagle Headshots",
                    description = "Fight in the FFA combat zone using only the Desert Eagle pistol to master precision single-shot aiming mechanics.",
                    durationMin = 5,
                    trainingGroundTask = "Attain 8 single-tap headshot kills in a single range session."
                )
            )
        )
    }

    // Drill Completions Set
    var completedDrills by remember { mutableStateOf(setOf<String>()) }
    var activeTimerDrillIndex by remember { mutableStateOf<Int?>(null) }
    var timeLeftSeconds by remember { mutableStateOf(300) }
    var originalTimeSeconds by remember { mutableStateOf(300) }
    var isTimerActive by remember { mutableStateOf(false) }

    // Reset timer when changing playstyles
    LaunchedEffect(selectedPlaystyle) {
        activeTimerDrillIndex = null
        isTimerActive = false
    }

    // Timer Thread Execution
    LaunchedEffect(isTimerActive, timeLeftSeconds) {
        if (isTimerActive && timeLeftSeconds > 0) {
            delay(1000L)
            timeLeftSeconds -= 1
            if (timeLeftSeconds == 0) {
                isTimerActive = false
                activeTimerDrillIndex?.let { index ->
                    completedDrills = completedDrills + "${selectedPlaystyle}_${index}"
                }
            }
        }
    }

    val currentDrills = prebuiltDrills[selectedPlaystyle] ?: emptyList()
    val completedCount = currentDrills.filterIndexed { index, _ ->
        completedDrills.contains("${selectedPlaystyle}_${index}")
    }.size

    Scaffold(
        topBar = { ScreenHeader(title = "AI Training Gym", subtitle = "Custom practice regimens", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("SPECIFY DRILL PROFILE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

                    // Playstyle Row Selectors
                    Text("Select Combat Profile:", style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        playstyles.forEach { style ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                        .background(
                                            if (selectedPlaystyle == style) Color(0xFFFF4B2B) else Color(0xFF161B22),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedPlaystyle = style }
                                        .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(style, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    // Rank Selection vertical list
                    Text("Select Rank Level:", style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ranks.forEach { rank ->
                            Box(
                                modifier = Modifier
                                        .background(
                                            if (selectedRank == rank) Color(0xFFFFBC00) else Color(0xFF161B22),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { selectedRank = rank }
                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(rank, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (selectedRank == rank) Color.Black else Color.White)
                            }
                        }
                    }
                }
            }

            // ==========================================
            // NEW MODULE: DAILY TACTICAL MISSIONS WORKBENCH
            // ==========================================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131720)),
                border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.15f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Header progress indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("DAILY COMBAT MISSION DECK", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))
                            Text(
                                "Tactile Muscle Calibration",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(54.dp)) {
                            CircularProgressIndicator(
                                progress = {
                                    if (currentDrills.isNotEmpty()) completedCount.toFloat() / currentDrills.size else 0f
                                },
                                modifier = Modifier.fillMaxSize(),
                                color = Color(0xFFFF4B2B),
                                strokeWidth = 5.dp,
                                trackColor = Color.White.copy(alpha = 0.05f),
                            )
                            Text(
                                "$completedCount/${currentDrills.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Done Celebrate Banner
                    if (completedCount == currentDrills.size && currentDrills.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF00E676).copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, Color(0xFF00E676).copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = "Calibrated", tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "🔥 ALL TACTICAL MISSIONS LOGGED! Your muscle reflex is calibrated for battle.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF00E676),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Drills Deck Render
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        currentDrills.forEachIndexed { idx, drill ->
                            val isCompleted = completedDrills.contains("${selectedPlaystyle}_${idx}")
                            val isSelectedForTimer = activeTimerDrillIndex == idx

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelectedForTimer) Color(0xFF1F2531) else Color(0xFF1A1E26)
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelectedForTimer) Color(0xFFFFBC00).copy(alpha = 0.4f) else Color.White.copy(alpha = 0.04f)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activeTimerDrillIndex = if (isSelectedForTimer) null else idx
                                        isTimerActive = false
                                        if (activeTimerDrillIndex != null) {
                                            timeLeftSeconds = drill.durationMin * 60
                                            originalTimeSeconds = drill.durationMin * 60
                                        }
                                    }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val key = "${selectedPlaystyle}_${idx}"
                                                completedDrills = if (isCompleted) {
                                                    completedDrills - key
                                                } else {
                                                    completedDrills + key
                                                }
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = "Toggle Complete",
                                                tint = if (isCompleted) Color(0xFF00E676) else Color.Gray,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                drill.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                color = if (isCompleted) Color.Gray else Color.White
                                            )
                                            Text(
                                                "${drill.durationMin} MIN REGIMEN",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFFFFBC00),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Icon(
                                            imageVector = if (isSelectedForTimer) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = "Expand info",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        drill.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.LightGray
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                            .padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("🏆", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Target: ${drill.trainingGroundTask}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF00E676),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // TIMER WIDGET BLOCK
                                    if (isSelectedForTimer) {
                                        val minutes = timeLeftSeconds / 60
                                        val seconds = timeLeftSeconds % 60
                                        val formattedTime = String.format("%02d:%02d", minutes, seconds)

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.Black, RoundedCornerShape(10.dp))
                                                .padding(12.dp)
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text("TACTICAL DRILL COUNTDOWN", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                                        Text(
                                                            formattedTime,
                                                            fontFamily = FontFamily.Monospace,
                                                            fontSize = 24.sp,
                                                            color = if (isTimerActive) Color(0xFFFF4B2B) else Color.White,
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                    }

                                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                        // Resume/Pause Trigger
                                                        IconButton(
                                                            onClick = { isTimerActive = !isTimerActive },
                                                            modifier = Modifier
                                                                .size(36.dp)
                                                                .background(Color(0xFF1C1F26), CircleShape)
                                                        ) {
                                                            Icon(
                                                                imageVector = if (isTimerActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                                contentDescription = "Play status",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }

                                                        // Reset Trigger
                                                        IconButton(
                                                            onClick = {
                                                                isTimerActive = false
                                                                timeLeftSeconds = originalTimeSeconds
                                                            },
                                                            modifier = Modifier
                                                                .size(36.dp)
                                                                .background(Color(0xFF1C1F26), CircleShape)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Refresh,
                                                                contentDescription = "Reset countdown",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }

                                                        // Quick test simulator
                                                        Button(
                                                            onClick = {
                                                                isTimerActive = true
                                                                timeLeftSeconds = 10
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBC00)),
                                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                            modifier = Modifier.height(36.dp),
                                                            shape = RoundedCornerShape(8.dp)
                                                        ) {
                                                            Text("Quick Fast-Forward (10s)", fontSize = 9.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold)
                                                        }
                                                    }
                                                }

                                                if (isTimerActive) {
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    LinearProgressIndicator(
                                                        progress = { timeLeftSeconds.toFloat() / originalTimeSeconds },
                                                        color = Color(0xFFFF4B2B),
                                                        trackColor = Color.White.copy(alpha = 0.05f),
                                                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Command trigger button
            Button(
                onClick = { viewModel.generateAIPlaystyleTraining(selectedRank, selectedPlaystyle) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_training_btn"),
                shape = RoundedCornerShape(12.dp),
                enabled = !isGenerating
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("COACH IS DRAFTING ROUTINE...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.SportsScore, contentDescription = "Gym Drills")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GENERATE PERSONALIZED DRILLS", fontWeight = FontWeight.ExtraBold)
                }
            }

            // Produced training routine
            report?.let { rep ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("DRILL CARD: ${rep.playstyle.uppercase()}", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.ExtraBold)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFBC00), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(rep.rank, color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text("Priority Target: ${rep.focusArea}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFBC00))

                        Box(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        // Coach text Output
                        Text(
                            rep.generatedDrills,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. COMMUNITY SECTION SCREEN
// ==========================================
@Composable
fun CommunityScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val posts by viewModel.communityPostsList.collectAsState()
    val selectedDevice by viewModel.selectedDevice.collectAsState()

    var postTitle by remember { mutableStateOf("") }
    var shareGen by remember { mutableStateOf(180f) }
    var shareRed by remember { mutableStateOf(175f) }

    Scaffold(
        topBar = { ScreenHeader(title = "Community Sens Feed", subtitle = "Verify other configurations", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Share my config panel
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SHARE YOUR PRESETS TO THE CLOUD", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF4B2B))
                    
                    TextField(
                        value = postTitle,
                        onValueChange = { postTitle = it },
                        placeholder = { Text("E.g. Full-Headshot recoil bypass. Very fluid.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("share_title_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0D0E12),
                            unfocusedContainerColor = Color(0xFF0D0E12),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("General Sens: ${shareGen.toInt()} / 200", fontSize = 11.sp, color = Color.White)
                            Slider(
                                value = shareGen,
                                onValueChange = { shareGen = it },
                                valueRange = 0f..200f,
                                colors = SliderDefaults.colors(thumbColor = Color(0xFFFF4B2B), activeTrackColor = Color(0xFFFF4B2B))
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Red Dot Sight: ${shareRed.toInt()} / 200", fontSize = 11.sp, color = Color.White)
                            Slider(
                                value = shareRed,
                                onValueChange = { shareRed = it },
                                valueRange = 0f..200f,
                                colors = SliderDefaults.colors(thumbColor = Color(0xFFFFBC00), activeTrackColor = Color(0xFFFFBC00))
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (postTitle.isNotBlank()) {
                                viewModel.postCommunitySensitivity(postTitle, selectedDevice?.model ?: "Generic", shareGen.toInt(), shareRed.toInt())
                                postTitle = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("share_config_btn")
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Publish sensitivity")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PUBLISH PRESETS AT FEED", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Feed of lists
            Text("TRENDING COMMUNITY CONFIGURATIONS", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(posts) { post ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(post.author, style = MaterialTheme.typography.titleSmall, color = Color(0xFFFFBC00), fontWeight = FontWeight.Bold)
                                    Text("Device: ${post.deviceModel}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF161B22), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFBC00), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(String.format("%.1f", post.rating), fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(post.title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f).background(Color(0xFF0D0E12), RoundedCornerShape(4.dp)).padding(6.dp)) {
                                    Text("General: ${post.general} / 200", fontSize = 11.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                }
                                Box(modifier = Modifier.weight(1f).background(Color(0xFF0D0E12), RoundedCornerShape(4.dp)).padding(6.dp)) {
                                    Text("Red Dot: ${post.redDot} / 200", fontSize = 11.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.clickable { viewModel.likeCommunityPost(post.id) },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color(0xFFFF4B2B), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("${post.likes} Upvotes", fontSize = 12.sp, color = Color.LightGray)
                                }

                                Text("${post.commentsCount} Comments", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. DEVELOPER ADMIN SCREEN
// ==========================================
@Composable
fun AdminScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    var brandInput by remember { mutableStateOf("") }
    var modelInput by remember { mutableStateOf("") }
    var ramInput by remember { mutableStateOf("12GB") }
    var cpuInput by remember { mutableStateOf("Snapdragon 8 Gen 3") }
    var dpiInput by remember { mutableStateOf("480") }

    val devices by viewModel.devicesList.collectAsState()

    Scaffold(
        topBar = { ScreenHeader(title = "Developer Admin Panel", subtitle = "Database & cloud adjustments", onBack = onBack) },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.6f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ADD COMPATIBLE EQUIPMENT", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))

                    TextField(
                        value = brandInput,
                        onValueChange = { brandInput = it },
                        label = { Text("Brand (E.g. Samsung, Xiaomi)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_brand_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0D0E12),
                            unfocusedContainerColor = Color(0xFF0D0E12),
                            focusedTextColor = Color.White
                        )
                    )

                    TextField(
                        value = modelInput,
                        onValueChange = { modelInput = it },
                        label = { Text("Model Name (E.g. POCO F6)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_model_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0D0E12),
                            unfocusedContainerColor = Color(0xFF0D0E12),
                            focusedTextColor = Color.White
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextField(
                            value = ramInput,
                            onValueChange = { ramInput = it },
                            label = { Text("RAM") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF0D0E12), unfocusedContainerColor = Color(0xFF0D0E12), focusedTextColor = Color.White)
                        )
                        TextField(
                            value = dpiInput,
                            onValueChange = { dpiInput = it },
                            label = { Text("Default DPI") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF0D0E12), unfocusedContainerColor = Color(0xFF0D0E12), focusedTextColor = Color.White)
                        )
                    }

                    TextField(
                        value = cpuInput,
                        onValueChange = { cpuInput = it },
                        label = { Text("Processor Specs") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF0D0E12), unfocusedContainerColor = Color(0xFF0D0E12), focusedTextColor = Color.White)
                    )

                    Button(
                        onClick = {
                            if (brandInput.isNotBlank() && modelInput.isNotBlank()) {
                                val dpiVal = dpiInput.toIntOrNull() ?: 411
                                viewModel.adminAddNewDevice(brandInput, modelInput, ramInput, cpuInput, dpiVal)
                                brandInput = ""
                                modelInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBC00)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_submit_btn")
                    ) {
                        Text("INJECT DEVICE PROFILE", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Database items footprint list
            Text("DATABASE REGISTER FOOTPRINT", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)

            devices.forEach { dev ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${dev.brand} ${dev.model}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text("CPU: ${dev.processor} | RAM: ${dev.ram}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        IconButton(onClick = { viewModel.deleteDevice(dev.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete device", tint = Color(0xFFFF4B2B))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. AI CHAT ASST COACH SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.chatHistory.collectAsState()
    val isTyping by viewModel.isTypingChat.collectAsState()
    var textInput by remember { mutableStateOf("") }
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()

    // Autoscroll bottom on layout update
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Live Sens Coach AI", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 16.sp)
                        Text(if (isTyping) "Typing strategy..." else "Grandmaster Tactician Bot", fontSize = 11.sp, color = Color(0xFFFFBC00))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFFF4B2B))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearChatMessages() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Clear thread", tint = Color.LightGray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161B22))
            )
        },
        containerColor = Color(0xFF0D0E12)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            // Chat Message list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ChatBubble, contentDescription = "Chat active", tint = Color(0xFF1C1F26), modifier = Modifier.size(64.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "No messages with your Grandmaster Coach.\nAsk about J-curve dragging, DPI optimizations, or scope speed modifiers!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(messages) { msg ->
                        ChatBubbleRow(msg)
                    }
                }
                
                if (isTyping) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F26)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Text("Analyzing parameters...", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFBC00), modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action row input
            Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Ask about DPI or headshot optimization...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_text_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1C1F26),
                        unfocusedContainerColor = Color(0xFF1C1F26),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            viewModel.sendChatMessage(textInput)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .background(Color(0xFFFF4B2B), CircleShape)
                        .size(48.dp)
                        .testTag("chat_send_btn")
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send text", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChatBubbleRow(msg: ChatMessage) {
    val isUser = msg.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 0.dp,
                bottomEnd = if (isUser) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFFFF4B2B) else Color(0xFF1C1F26)
            ),
            border = if (isUser) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                     text = msg.text,
                     color = Color.White,
                     fontSize = 13.sp
                )
            }
        }
    }
}

// ===============================================
// 10. ESPORTS ADVANCED FULLSCREEN DRAG LAB
// ===============================================
@Composable
fun DragTestFullScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    
    // Configurable Parameters for drag physical models
    var weaponClass by remember { mutableStateOf("SMG / MP40") }
    var fireButtonSize by remember { mutableStateOf(48) } // HUD fire button % size
    var sensitivityPresetValue by remember { mutableStateOf(180) } // out of 200 general sens
    var customDpiInput by remember { mutableStateOf(640) } // DPI configuration

    // Simulation metrics & statistics
    var currentSpeedRating by remember { mutableStateOf(0f) }
    var touchPathPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var simulationResultFeedback by remember { mutableStateOf("PLACE FINGER ON THE CHROME FIRE BUTTON AND SWIPE UPWARD QUICKLY!") }
    var hitTypeResult by remember { mutableStateOf("") } // "BODY", "HEADSHOT", "OVER_AIM" (too fast), "UNDER_AIM" (too slow)
    var dragStreak by remember { mutableStateOf(0) }
    var highestStreak by remember { mutableStateOf(0) }
    var dragTracedCurveType by remember { mutableStateOf("Linear Up") }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "E-Sports Drag Calibrator Lab",
                subtitle = "Practice exact muscle-memory drag curves",
                onBack = onBack
            )
        },
        containerColor = Color(0xFF090A0F)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats panel at the Top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                    border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("CURRENT VELOCITY", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (currentSpeedRating > 0f) "${currentSpeedRating.toInt()} / 200" else "0 / 200",
                            fontSize = 20.sp,
                            color = if (currentSpeedRating > 160) Color(0xFFFF4B2B) else if (currentSpeedRating > 110) Color(0xFF00FF00) else Color(0xFFFFBC00),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text("Limit: 200 Max", fontSize = 9.sp, color = Color.LightGray)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                    border = BorderStroke(1.dp, Color(0xFF00FF00).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("HEADSHOT STREAK", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = "🔥 $dragStreak",
                            fontSize = 20.sp,
                            color = Color(0xFF00FF00),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text("Highest: $highestStreak", fontSize = 9.sp, color = Color.LightGray)
                    }
                }
            }

            // Interactive Controls Board
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11141B)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("LIVE PHYSICAL CALIBRATION ARRAYS", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF4B2B))
                    
                    // Weapon Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("SMG / MP40", "Shotgun / M1887", "Desert Eagle").forEach { item ->
                            val active = weaponClass == item
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) Color(0xFFFF4B2B).copy(alpha = 0.15f) else Color(0xFF1A1F29))
                                    .border(BorderStroke(1.dp, if (active) Color(0xFFFF4B2B) else Color.Transparent), RoundedCornerShape(8.dp))
                                    .clickable { weaponClass = item }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color.Gray)
                            }
                        }
                    }

                    // DPI & Size Sliders
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("HUD Fire Button: $fireButtonSize%", fontSize = 11.sp, color = Color.White)
                            Slider(
                                value = fireButtonSize.toFloat(),
                                onValueChange = { fireButtonSize = it.toInt() },
                                valueRange = 30f..100f,
                                colors = SliderDefaults.colors(thumbColor = Color(0xFFFF4B2B), activeTrackColor = Color(0xFFFF4B2B))
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Simulated DPI: $customDpiInput", fontSize = 11.sp, color = Color.White)
                            Slider(
                                value = customDpiInput.toFloat(),
                                onValueChange = { customDpiInput = it.toInt() },
                                valueRange = 360f..800f,
                                colors = SliderDefaults.colors(thumbColor = Color(0xFFFFBC00), activeTrackColor = Color(0xFFFFBC00))
                            )
                        }
                    }

                    // Sensitivity Tuning Slider (up to 200 total)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("General Tech Sensitivity (Total 200):", fontSize = 11.sp, color = Color.White)
                            Text("$sensitivityPresetValue / 200", fontSize = 12.sp, color = Color(0xFFFFBC00), fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = sensitivityPresetValue.toFloat(),
                            onValueChange = { sensitivityPresetValue = it.toInt() },
                            valueRange = 0f..200f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFFFFBC00), activeTrackColor = Color(0xFFFFBC00))
                        )
                    }
                }
            }

            // Massive Gaming Drag HUD Simulator Viewport ("The Big Screen")
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1116)),
                border = BorderStroke(1.5.dp, if (hitTypeResult == "HEADSHOT") Color(0xFF00FF00) else Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Visual gaming background mesh
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(color = Color(0xFF07080B))
                        
                        // Tactical grid lines
                        val gridSpace = 40.dp.toPx()
                        for (x in 0..size.width.toInt() step gridSpace.toInt()) {
                            drawLine(Color.White.copy(alpha = 0.03f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height))
                        }
                        for (y in 0..size.height.toInt() step gridSpace.toInt()) {
                            drawLine(Color.White.copy(alpha = 0.03f), Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()))
                        }
                    }

                    // 1. Simulated Target Bot Dummy (The head at top, body at center)
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Dummy Helmet / Head
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (hitTypeResult == "HEADSHOT") Color(0xFFFF4B2B) else Color(0xFF2E2E3E)
                                )
                                .border(
                                    BorderStroke(
                                        2.dp, 
                                        if (hitTypeResult == "HEADSHOT") Color.Red else Color.Gray
                                    ), 
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "CRIT", 
                                fontSize = 9.sp, 
                                color = Color.White, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Neck joint
                        Box(modifier = Modifier.size(12.dp, 8.dp).background(Color.Gray))
                        
                        // Torso body target
                        Box(
                            modifier = Modifier
                                .size(72.dp, 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (hitTypeResult == "BODY") Color(0xFFFFBC00) else Color(0xFF1E1F29)
                                )
                                .border(BorderStroke(1.dp, Color.DarkGray), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("BODY", fontSize = 10.sp, color = Color.LightGray)
                        }
                    }

                    // 2. Headshot Explosion animation popup
                    androidx.compose.animation.AnimatedVisibility(
                        visible = hitTypeResult == "HEADSHOT",
                        enter = scaleIn(initialScale = 0.5f) + fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "☠️ KILL TRIGGERED ☠️",
                                color = Color(0xFFFF4B2B),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "HEADSHOT!",
                                color = Color.Red,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "+200 ESPORTS POINTS",
                                color = Color(0xFFFFBC00),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Body Hit text indicator
                    androidx.compose.animation.AnimatedVisibility(
                        visible = hitTypeResult == "BODY",
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            "⚡ yellow body hit - lock-on blocked! drag faster!",
                            color = Color(0xFFFFBC00),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Overaim feedback indicator (too fast)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = hitTypeResult == "OVER_AIM",
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            "❌ REC OIL OVER-SHOOT! TOO VISUAL (DRAG TOO FAST)",
                            color = Color.Magenta,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Touch Trace Neon Line Drawer
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        if (touchPathPoints.size > 1) {
                            for (i in 0 until touchPathPoints.size - 1) {
                                drawLine(
                                    color = if (hitTypeResult == "HEADSHOT") Color(0xFF00FF00) else Color(0xFFFF5722),
                                    start = touchPathPoints[i],
                                    end = touchPathPoints[i + 1],
                                    strokeWidth = 6f
                                )
                            }
                        }
                    }

                    // Instruction/Feedback overlay at top corner
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text("DRAG PATH TYPE: $dragTracedCurveType", fontSize = 9.sp, color = Color(0xFF00FF00), fontWeight = FontWeight.Bold)
                            Text("HUD CALIBRATION METRIC: Active", fontSize = 8.sp, color = Color.White)
                        }
                    }

                    // 3. Floating Interactive Fire Button Anchor (Place near the bottom center)
                    var touchDownY by remember { mutableStateOf(0f) }
                    var touchDownX by remember { mutableStateOf(0f) }
                    var dragTimerStart by remember { mutableStateOf(0L) }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 50.dp)
                            .size(fireButtonSize.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFFF5722), Color(0xFF11141B))
                                )
                            )
                            .border(BorderStroke(2.dp, Color(0xFFFFBC00)), CircleShape)
                            .pointerInput(fireButtonSize, customDpiInput, sensitivityPresetValue, weaponClass) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        touchDownX = offset.x
                                        touchDownY = offset.y
                                        dragTimerStart = SystemClock.uptimeMillis()
                                        touchPathPoints = listOf(Offset(size.width / 2f + offset.x - (fireButtonSize.dp.toPx() / 2f), size.height - 50.dp.toPx() - (fireButtonSize.dp.toPx() / 2f) + offset.y))
                                        hitTypeResult = ""
                                        dragTracedCurveType = "Detecting..."
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        // Accumulate points to draw path line
                                        val latest = touchPathPoints.last()
                                        val nextPoint = Offset(latest.x + dragAmount.x, latest.y + dragAmount.y)
                                        touchPathPoints = touchPathPoints + nextPoint
                                    },
                                    onDragEnd = {
                                        val timeElapsed = SystemClock.uptimeMillis() - dragTimerStart
                                        if (touchPathPoints.size > 2 && timeElapsed > 20) {
                                            // Calculate vertical displacement
                                            val startY = touchPathPoints.first().y
                                            val endY = touchPathPoints.last().y
                                            val totalVerticalDistance = startY - endY // positive if slid up

                                            // Determine J-Curve via horizontal variation
                                            val startX = touchPathPoints.first().x
                                            val maxHorizontalDelta = touchPathPoints.map { Math.abs(it.x - startX) }.maxOrNull() ?: 0f

                                            dragTracedCurveType = if (maxHorizontalDelta > 45f) {
                                                "J-Curve Flick (Advanced Esports)"
                                            } else {
                                                "Linear Up swipe"
                                            }

                                            // Swipe speed calculation factoring custom UI DPI and general tech sensitivity scaling
                                            val basicSpeed = (totalVerticalDistance / timeElapsed) * 85f
                                            val dpiMultiplier = customDpiInput / 500f
                                            val sensMultiplier = (sensitivityPresetValue + 50) / 150f
                                            
                                            // Total physics drag force scaled up to 0 - 200 rating
                                            val finalPhysicDragRating = (basicSpeed * dpiMultiplier * sensMultiplier).coerceIn(0f, 200f)
                                            currentSpeedRating = finalPhysicDragRating

                                            // Determine exact hit targeting
                                            val targetSpeedMin = if (weaponClass == "Shotgun / M1887") 150f else 120f
                                            val targetSpeedMax = if (weaponClass == "Shotgun / M1887") 195f else 175f

                                            if (totalVerticalDistance < 50f) {
                                                hitTypeResult = "UNDER_AIM"
                                                simulationResultFeedback = "⛔ SWIPE LENGTH TOO SHORT. Pull further up to align scope!"
                                                dragStreak = 0
                                            } else {
                                                when {
                                                    finalPhysicDragRating < targetSpeedMin -> {
                                                        hitTypeResult = "BODY"
                                                        simulationResultFeedback = "🎯 SPEED IS TOO LOW (${finalPhysicDragRating.toInt()}). Aim locked on target's center chest. Drag quicker!"
                                                        dragStreak = 0
                                                    }
                                                    finalPhysicDragRating in targetSpeedMin..targetSpeedMax -> {
                                                        hitTypeResult = "HEADSHOT"
                                                        simulationResultFeedback = "💥 BOOM! PERFECT ${dragTracedCurveType}! Red numbers registered!"
                                                        dragStreak++
                                                        if (dragStreak > highestStreak) {
                                                            highestStreak = dragStreak
                                                        }
                                                    }
                                                    else -> {
                                                        hitTypeResult = "OVER_AIM"
                                                        simulationResultFeedback = "⚠️ TOO COMPRESSIVE SPEED (${finalPhysicDragRating.toInt()}). Aim flew completely over target's head."
                                                        dragStreak = 0
                                                    }
                                                }
                                            }
                                        } else {
                                            simulationResultFeedback = "Tap and drag upwards with velocity!"
                                            dragStreak = 0
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(0.6f)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    }
                }
            }

            // Real-Time physical feedback summary text
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SIMULATOR RECOIL TELEMETRY", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFBC00))
                        Text(
                            text = if (hitTypeResult == "HEADSHOT") "READY TO MATCH" else "TUNING REQUIRED",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hitTypeResult == "HEADSHOT") Color.Green else Color.Gray,
                            modifier = Modifier
                                .background(Color.Black, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = simulationResultFeedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pro Tip for $weaponClass: ${if (weaponClass == "Shotgun / M1887") "Requires intense instant snapping torque due to heavy target magnetic friction. Configure system DPI above 600." else "Constant fluid J-Curve dragging overrides structural weapon stability perfectly."}",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

// ===============================================
// 11. INTERACTIVE HUD LAYOUT VISUALIZER
// ===============================================
@Composable
fun HudVisualizerScreen(
    viewModel: FFSensViewModel,
    onBack: () -> Unit
) {
    // 1. Aspect Ratio options
    val aspectOptions = listOf("Phone (16:9)", "Tablet (4:3)", "Foldable (1.1:1)")
    var selectedAspect by remember { mutableStateOf("Phone (16:9)") }

    // 2. Playstyle Presets
    val playstylePresets = listOf("2-Finger (Thumbing)", "3-Finger Hybrid", "4-Finger Claw (Pro)")
    var selectedPreset by remember { mutableStateOf("3-Finger Hybrid") }

    // 3. Button definitions
    val buttonsList = remember {
        listOf(
            InteractiveHudButton("shoot", "Fire Trigger", Icons.Default.RadioButtonChecked, Color(0xFFFF4B2B)),
            InteractiveHudButton("gloo", "Gloo Wall", Icons.Default.Shield, Color(0xFF00E676)),
            InteractiveHudButton("jump", "Jump Key", Icons.Default.ArrowUpward, Color(0xFFFFBC00)),
            InteractiveHudButton("crouch", "Crouch Key", Icons.Default.ArrowDownward, Color(0xFFFFBC00)),
            InteractiveHudButton("scope", "Scope Zoom", Icons.Default.Visibility, Color(0xFFFF5722)),
            InteractiveHudButton("left_shoot", "Left Fire", Icons.Default.FilterCenterFocus, Color(0xFFFF4B2B)),
            InteractiveHudButton("sprint", "Sprint", Icons.Default.DirectionsRun, Color(0xFFE040FB)),
            InteractiveHudButton("weapon", "Weapon Switch", Icons.Default.Cached, Color(0xFF00B0FF))
        )
    }

    // Coordinates mapped as percentages (0.0f..1.0f)
    val xOffsets = remember { mutableStateMapOf<String, Float>() }
    val yOffsets = remember { mutableStateMapOf<String, Float>() }
    val customSizes = remember { mutableStateMapOf<String, Int>() }

    // Active focused button
    var selectedButtonId by remember { mutableStateOf("shoot") }

    // Apply presets helper function
    val applyPreset: (String) -> Unit = { preset ->
        selectedPreset = preset
        when (preset) {
            "2-Finger (Thumbing)" -> {
                xOffsets["shoot"] = 0.78f; yOffsets["shoot"] = 0.72f; customSizes["shoot"] = 52
                xOffsets["gloo"] = 0.15f; yOffsets["gloo"] = 0.80f; customSizes["gloo"] = 65
                xOffsets["jump"] = 0.85f; yOffsets["jump"] = 0.52f; customSizes["jump"] = 48
                xOffsets["crouch"] = 0.72f; yOffsets["crouch"] = 0.88f; customSizes["crouch"] = 44
                xOffsets["scope"] = 0.88f; yOffsets["scope"] = 0.30f; customSizes["scope"] = 48
                xOffsets["left_shoot"] = 0.12f; yOffsets["left_shoot"] = 0.45f; customSizes["left_shoot"] = 40
                xOffsets["sprint"] = 0.18f; yOffsets["sprint"] = 0.20f; customSizes["sprint"] = 45
                xOffsets["weapon"] = 0.50f; yOffsets["weapon"] = 0.18f; customSizes["weapon"] = 50
            }
            "3-Finger Hybrid" -> {
                xOffsets["shoot"] = 0.74f; yOffsets["shoot"] = 0.75f; customSizes["shoot"] = 48
                xOffsets["gloo"] = 0.15f; yOffsets["gloo"] = 0.75f; customSizes["gloo"] = 70
                xOffsets["jump"] = 0.15f; yOffsets["jump"] = 0.20f; customSizes["jump"] = 55
                xOffsets["crouch"] = 0.86f; yOffsets["crouch"] = 0.80f; customSizes["crouch"] = 44
                xOffsets["scope"] = 0.82f; yOffsets["scope"] = 0.25f; customSizes["scope"] = 48
                xOffsets["left_shoot"] = 0.28f; yOffsets["left_shoot"] = 0.20f; customSizes["left_shoot"] = 46
                xOffsets["sprint"] = 0.42f; yOffsets["sprint"] = 0.18f; customSizes["sprint"] = 45
                xOffsets["weapon"] = 0.50f; yOffsets["weapon"] = 0.82f; customSizes["weapon"] = 52
            }
            "4-Finger Claw (Pro)" -> {
                xOffsets["left_shoot"] = 0.16f; yOffsets["left_shoot"] = 0.18f; customSizes["left_shoot"] = 58
                xOffsets["gloo"] = 0.32f; yOffsets["gloo"] = 0.18f; customSizes["gloo"] = 65
                xOffsets["scope"] = 0.82f; yOffsets["scope"] = 0.18f; customSizes["scope"] = 55
                xOffsets["jump"] = 0.68f; yOffsets["jump"] = 0.18f; customSizes["jump"] = 52
                xOffsets["shoot"] = 0.76f; yOffsets["shoot"] = 0.72f; customSizes["shoot"] = 46
                xOffsets["crouch"] = 0.88f; yOffsets["crouch"] = 0.82f; customSizes["crouch"] = 45
                xOffsets["sprint"] = 0.15f; yOffsets["sprint"] = 0.45f; customSizes["sprint"] = 42
                xOffsets["weapon"] = 0.50f; yOffsets["weapon"] = 0.85f; customSizes["weapon"] = 55
            }
        }
    }

    // Initialize layout offsets dynamically on launch
    LaunchedEffect(Unit) {
        if (xOffsets.isEmpty()) {
            applyPreset("3-Finger Hybrid")
        }
    }

    // Dynamic collision evaluation
    val firstOverlapWarning = remember(xOffsets, yOffsets, customSizes) {
        val keys = buttonsList.map { it.id }
        var warning: String? = null
        for (i in 0 until keys.size) {
            for (j in i + 1 until keys.size) {
                val id1 = keys[i]
                val id2 = keys[j]
                val x1 = xOffsets[id1] ?: 0.5f
                val y1 = yOffsets[id1] ?: 0.5f
                val x2 = xOffsets[id2] ?: 0.5f
                val y2 = yOffsets[id2] ?: 0.5f
                
                val dx = (x1 - x2) * 1.77f
                val dy = y1 - y2
                val dist = Math.sqrt((dx * dx + dy * dy).toDouble())
                if (dist < 0.11) {
                    val b1 = buttonsList.find { it.id == id1 }?.displayName ?: id1
                    val b2 = buttonsList.find { it.id == id2 }?.displayName ?: id2
                    warning = "⚠️ Overlap Warning: $b1 and $b2 are extremely close! Separating them avoids accidental misclicks."
                    break
                }
            }
            if (warning != null) break
        }
        warning
    }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Interactive HUD Studio",
                subtitle = "Align triggers based on your screen size",
                onBack = onBack
            )
        },
        containerColor = Color(0xFF090A0E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Description Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                border = BorderStroke(1.dp, Color(0xFFFFBC00).copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "SCREEN RE-SCALING & PLAYSTYLE CALIBRATION",
                        fontSize = 10.sp,
                        color = Color(0xFFFFBC00),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Esports players adapt button sizes based on device screen types. Larger screens (Tablets) require tighter clusters near indices, while phones require spread-out configurations.",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }

            // Controls section: Aspect ratio + Presets selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Screen Aspect selection
                Column(modifier = Modifier.weight(1f)) {
                    Text("SIMULATED HARDWARE SCREEN", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        var expanded by remember { mutableStateOf(false) }
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B202D)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(selectedAspect, fontSize = 11.sp, color = Color.White)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color(0xFF131722))
                        ) {
                            aspectOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt, color = Color.White, fontSize = 12.sp) },
                                    onClick = {
                                        selectedAspect = opt
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Preset Playstyle selector
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("TARGET ESPORTS PLAYSTYLE", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        var expandedPreset by remember { mutableStateOf(false) }
                        Button(
                            onClick = { expandedPreset = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B2B)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(selectedPreset.uppercase(), fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
                        }
                        DropdownMenu(
                            expanded = expandedPreset,
                            onDismissRequest = { expandedPreset = false },
                            modifier = Modifier.background(Color(0xFF131722))
                        ) {
                            playstylePresets.forEach { preset ->
                                DropdownMenuItem(
                                    text = { Text(preset, color = Color.White, fontSize = 12.sp) },
                                    onClick = {
                                        applyPreset(preset)
                                        expandedPreset = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Map Label helper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🚨 SIMULATOR: DRAG PLACEHOLDERS TO POSE",
                    fontSize = 11.sp,
                    color = Color(0xFF00FF00),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Active Aspect: ${if(selectedAspect.contains("16:9")) "16:9 Widescreen" else if(selectedAspect.contains("4:3")) "4:3 iPad" else "1.1:1 Foldable"}",
                    fontSize = 9.sp,
                    color = Color.LightGray
                )
            }

            // Simulated Landscape Gaming Viewport
            val mockAspect = when (selectedAspect) {
                "Phone (16:9)" -> 1.77f
                "Tablet (4:3)" -> 1.33f
                else -> 1.1f
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF04060A)),
                border = BorderStroke(2.dp, Color.White.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(mockAspect)
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val widthPx = constraints.maxWidth.toFloat()
                    val heightPx = constraints.maxHeight.toFloat()

                    // Grid Layout underlay for accuracy
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(color = Color(0xFF06090F))
                        val gridSpace = 30.dp.toPx()
                        for (x in 0..size.width.toInt() step gridSpace.toInt()) {
                            drawLine(Color.White.copy(alpha = 0.04f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height))
                        }
                        for (y in 0..size.height.toInt() step gridSpace.toInt()) {
                            drawLine(Color.White.copy(alpha = 0.04f), Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()))
                        }

                        // Draw diagonal screen division lines (tactical)
                        drawLine(Color(0xFFFF4B2B).copy(alpha = 0.05f), Offset(0f, 0f), Offset(size.width, size.height))
                        drawLine(Color(0xFFFF4B2B).copy(alpha = 0.05f), Offset(size.width, 0f), Offset(0f, size.height))
                    }

                    // Render HUD button placeholders
                    buttonsList.forEach { hudBtn ->
                        val relativeX = xOffsets[hudBtn.id] ?: 0.5f
                        val relativeY = yOffsets[hudBtn.id] ?: 0.5f
                        val currentSize = customSizes[hudBtn.id] ?: 48
                        val isFocused = selectedButtonId == hudBtn.id

                        // Calculate DP coordinates in a performant, density-aware scope
                        val density = androidx.compose.ui.platform.LocalDensity.current
                        val (buttonXDp, buttonYDp) = remember(relativeX, relativeY, currentSize, widthPx, heightPx, density) {
                            with(density) {
                                val sizePx = currentSize.dp.toPx()
                                val xPx = (relativeX * widthPx) - (sizePx / 2f)
                                val yPx = (relativeY * heightPx) - (sizePx / 2f)
                                Pair(xPx.toDp(), yPx.toDp())
                            }
                        }

                        Box(
                            modifier = Modifier
                                .offset(x = buttonXDp, y = buttonYDp)
                                .size(currentSize.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = if (isFocused) {
                                            listOf(hudBtn.themeColor.copy(alpha = 0.6f), Color(0xFF172030))
                                        } else {
                                            listOf(hudBtn.themeColor.copy(alpha = 0.25f), Color(0xFF0F1116))
                                        }
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        if (isFocused) 2.5.dp else 1.2.dp,
                                        if (isFocused) Color(0xFF00FF00) else hudBtn.themeColor
                                    ),
                                    CircleShape
                                )
                                .pointerInput(hudBtn.id, widthPx, heightPx) {
                                    detectDragGestures(
                                        onDragStart = {
                                            selectedButtonId = hudBtn.id
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            val currentX = xOffsets[hudBtn.id] ?: 0.5f
                                            val currentY = yOffsets[hudBtn.id] ?: 0.5f

                                            val deltaX = dragAmount.x / widthPx
                                            val deltaY = dragAmount.y / heightPx

                                            xOffsets[hudBtn.id] = (currentX + deltaX).coerceIn(0.04f, 0.96f)
                                            yOffsets[hudBtn.id] = (currentY + deltaY).coerceIn(0.04f, 0.96f)
                                        }
                                    )
                                }
                                .clickable { selectedButtonId = hudBtn.id },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = hudBtn.icon,
                                    contentDescription = hudBtn.displayName,
                                    tint = if (isFocused) Color(0xFF00FF00) else Color.White,
                                    modifier = Modifier.size((currentSize * 0.45f).dp)
                                )
                                if (currentSize >= 45) {
                                    Text(
                                        text = "${currentSize}%",
                                        fontSize = if (currentSize >= 55) 9.sp else 7.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isFocused) Color(0xFF00FF00) else Color.LightGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Real-Time Spacing & Overlap warnings banner
            firstOverlapWarning?.let { warningMsg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF33141E)),
                    border = BorderStroke(1.dp, Color(0xFFFF4B2B).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = warningMsg,
                            color = Color(0xFFFFB3B3),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Telemetry & Active Button Customizer Control Panel
            val focusedButton = buttonsList.find { it.id == selectedButtonId }
            focusedButton?.let { btn ->
                val currentSize = customSizes[btn.id] ?: 48
                val currentX = xOffsets[btn.id] ?: 0.5f
                val currentY = yOffsets[btn.id] ?: 0.5f

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF11141C)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(btn.themeColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CONTROL CALIBRATOR: ${btn.displayName.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text(
                                "LOC: X ${(currentX * 100).toInt()}% | Y ${(currentY * 100).toInt()}%",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Size Slider for focused button
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Dynamic HUD Button Scale:", fontSize = 11.sp, color = Color.White)
                                Text("$currentSize% Size Scale", fontSize = 12.sp, color = btn.themeColor, fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = currentSize.toFloat(),
                                onValueChange = { customSizes[btn.id] = it.toInt() },
                                valueRange = 30f..100f,
                                colors = SliderDefaults.colors(
                                    thumbColor = btn.themeColor,
                                    activeTrackColor = btn.themeColor
                                )
                            )
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.04f)))

                        // Pro advice tailored to focused button
                        val proAdvice = when (btn.id) {
                            "shoot" -> "Main fire trigger should be located exactly at thumb landing coordinate. We recommend 45%-55% scale for general snaps; higher misses over-aim drag sweeps."
                            "gloo" -> "Gloo wall trigger MUST be scaled extremely large (65%-80%) near left index/thumb for instant tactical barrier deployment."
                            "scope" -> "Scope key functions best when configured to 46% near right thumb/index. Ensure there is significant clearance to avoid overlapping regular jump keys."
                            "jump" -> "Esports layout positions Jump Key high up for right claw indexing or right-edge thumb snapping."
                            "crouch" -> "Keep crouch slightly separated but below the main Fire Key, allowing fluid crouch-and-shoot sequences."
                            else -> "Keep custom actions clustered cleanly by frequency of use. Frequently used items require higher size scaling."
                        }

                        Text(
                            text = proAdvice,
                            fontSize = 11.sp,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // CTA Reset Button
            Button(
                onClick = { applyPreset(selectedPreset) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B202D)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("reset_hud_defaults_button")
            ) {
                Text("RESET TO RECOMMENDED PLAYSTYLE LAYOUT", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class InteractiveHudButton(
    val id: String,
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val themeColor: Color
)


