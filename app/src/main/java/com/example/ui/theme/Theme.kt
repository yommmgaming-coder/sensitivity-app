package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = FireOrange,
    secondary = CyberGold,
    tertiary = CrimsonRed,
    background = ObsidianBackground,
    surface = CarbonSurface,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = ObsidianBackground,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    surfaceVariant = ActiveCardSlate,
    outline = BorderGrey
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme for gaming look
  dynamicColor: Boolean = false, // Disable dynamic colors to keep Free Fire signature orange
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
