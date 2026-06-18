package com.example.ui.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.data.SavedSensitivity
import java.io.OutputStream

object ImageExporter {

    fun generateAndSaveSensitivityCard(context: Context, sens: SavedSensitivity): Uri? {
        val width = 800
        val height = 1100
        
        // Create a bitmap and attach a canvas to it
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Paint for background
        val paintBg = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        
        // 1. Draw futuristic premium dark background with angular carbon gradient
        val bgGradient = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(Color.parseColor("#0C0E14"), Color.parseColor("#1B202A"), Color.parseColor("#050608")),
            null, Shader.TileMode.CLAMP
        )
        paintBg.shader = bgGradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)
        paintBg.shader = null // Clear shader
        
        // 2. Draw border highlights
        val paintBorder = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.parseColor("#342E29") // subtle metallic border
        }
        val innerPadding = 20f
        canvas.drawRect(innerPadding, innerPadding, width - innerPadding, height - innerPadding, paintBorder)
        
        // Neon red/amber accents in corners (esports aesthetic)
        val paintAccent = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 6f
            color = Color.parseColor("#FF4B2B") // Pro Red
        }
        // Top-left corner mark
        canvas.drawLine(20f, 20f, 80f, 20f, paintAccent)
        canvas.drawLine(20f, 20f, 20f, 80f, paintAccent)
        // Top-right corner mark
        canvas.drawLine(width - 20f, 20f, width - 80f, 20f, paintAccent)
        canvas.drawLine(width - 20f, 20f, width - 20f, 80f, paintAccent)
        // Bottom-left corner mark
        canvas.drawLine(20f, height - 20f, 80f, height - 20f, paintAccent)
        canvas.drawLine(20f, height - 20f, 20f, height - 80f, paintAccent)
        // Bottom-right corner mark
        canvas.drawLine(width - 20f, height - 20f, width - 80f, height - 20f, paintAccent)
        canvas.drawLine(width - 20f, height - 20f, width - 20f, height - 80f, paintAccent)

        // Draw crosshair alignment grid lines at 4% alpha opacity
        val paintGrid = Paint().apply {
            color = Color.parseColor("#FF5E3A")
            alpha = 10
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }
        val step = 50f
        var x = 0f
        while (x < width) {
            canvas.drawLine(x, 0f, x, height.toFloat(), paintGrid)
            x += step
        }
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, width.toFloat(), y, paintGrid)
            y += step
        }

        // Draw an absolute centerpiece calibration circle
        val paintTarget = Paint().apply {
            color = Color.parseColor("#FF4B2B")
            alpha = 24
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
        canvas.drawCircle(width / 2f, height / 2f, 220f, paintTarget)
        canvas.drawCircle(width / 2f, height / 2f, 110f, paintTarget)
        canvas.drawLine(width / 2f - 240f, height / 2f, width / 2f + 240f, height / 2f, paintTarget)
        canvas.drawLine(width / 2f, height / 2f - 240f, width / 2f, height / 2f + 240f, paintTarget)

        // 3. Draw Header Title and branding
        val paintText = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        
        // Brand logo target icon
        val paintLogo = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#FF4B2B")
        }
        canvas.drawCircle(60f, 75f, 16f, paintLogo)
        paintLogo.color = Color.parseColor("#0C0E14")
        canvas.drawCircle(60f, 75f, 9f, paintLogo)
        paintLogo.color = Color.parseColor("#FFBC00")
        canvas.drawCircle(60f, 75f, 4f, paintLogo)
        
        paintText.textSize = 34f
        paintText.color = Color.parseColor("#FF4B2B")
        canvas.drawText("AI SENS PRO", 95f, 75f, paintText)
        
        paintText.textSize = 12f
        paintText.color = Color.parseColor("#888888")
        paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        canvas.drawText("TACTILE ACCELERATION PROFILE // SECURE EXPORT", 95f, 102f, paintText)
        
        // Divide header
        val paintDivide = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.parseColor("#50443D")
        }
        canvas.drawLine(40f, 125f, width - 40f, 125f, paintDivide)

        // 4. Device Spec Showcase Banner
        val cardRect = RectF(50f, 150f, width - 50f, 260f)
        val paintSpecBg = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#15181F")
        }
        canvas.drawRoundRect(cardRect, 14f, 14f, paintSpecBg)
        val paintSpecBorder = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.parseColor("#FF4B2B")
            alpha = 130
        }
        canvas.drawRoundRect(cardRect, 14f, 14f, paintSpecBorder)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 11f
        paintText.color = Color.parseColor("#FFBC00")
        canvas.drawText("TARGET DEVICE HARDWARE PROFILE", 70f, 182f, paintText)
        
        paintText.textSize = 28f
        paintText.color = Color.WHITE
        canvas.drawText("${sens.brand.uppercase()} ${sens.model.uppercase()}", 70f, 220f, paintText)
        
        // DPI badge
        val dpiPill = RectF(width - 210f, 175f, width - 70f, 235f)
        paintLogo.color = Color.parseColor("#FFBC00")
        paintLogo.alpha = 30
        canvas.drawRoundRect(dpiPill, 10f, 10f, paintLogo)
        paintLogo.color = Color.parseColor("#FFBC00")
        paintLogo.strokeWidth = 2f
        paintLogo.style = Paint.Style.STROKE
        canvas.drawRoundRect(dpiPill, 10f, 10f, paintLogo)
        
        paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paintText.textSize = 10f
        paintText.color = Color.parseColor("#FFBC00")
        canvas.drawText("RECOMMENDED DPI", width - 200f, 195f, paintText)
        paintText.textSize = 18f
        paintText.color = Color.WHITE
        canvas.drawText("${sens.bestDpi} DPI", width - 165f, 225f, paintText)

        // 5. Sensitivity values (The Core layout)
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 16f
        paintText.color = Color.WHITE
        canvas.drawText("TACTILE RECOIL INDICES", 50f, 310f, paintText)
        
        // Highlight accent bar
        val paintBarLine = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.parseColor("#FF4B2B")
        }
        canvas.drawLine(50f, 322f, 250f, 322f, paintBarLine)

        // Draw sensitivity bars
        val sensItems = listOf(
            Triple("GENERAL SENSITIVITY", sens.general, "#FF4B2B"),
            Triple("RED DOT SIGHT", sens.redDot, "#FF4B2B"),
            Triple("2X SCOPE ZOOM", sens.scope2X, "#FFBC00"),
            Triple("4X SCOPE ZOOM", sens.scope4X, "#FFBC00"),
            Triple("AWM SNIPER SCOPE", sens.awm, "#00FF00"),
            Triple("FREE LOOK MOVEMENT", sens.freeLook, "#00E676")
        )
        
        var startY = 360f
        sensItems.forEach { (label, value, hexColor) ->
            // Draw label
            paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paintText.textSize = 14f
            paintText.color = Color.parseColor("#CCCCCC")
            canvas.drawText(label, 50f, startY + 5f, paintText)
            
            // Draw value / 200 text
            paintText.textSize = 16f
            paintText.color = Color.parseColor(hexColor)
            val valStr = "$value"
            val textWidth = paintText.measureText(valStr)
            canvas.drawText(valStr, width - 110f - textWidth, startY + 5f, paintText)
            
            paintText.textSize = 11f
            paintText.color = Color.GRAY
            canvas.drawText(" / 200", width - 105f, startY + 5f, paintText)
            
            // Draw progress track background
            val trackRect = RectF(50f, startY + 18f, width - 50f, startY + 28f)
            paintSpecBg.color = Color.parseColor("#0C0E14")
            canvas.drawRoundRect(trackRect, 5f, 5f, paintSpecBg)
            
            // Draw progress fill
            val fillPercent = (value / 200f).coerceIn(0f, 1f)
            if (fillPercent > 0.02f) {
                val fillRect = RectF(50f, startY + 18f, 50f + (width - 100f) * fillPercent, startY + 28f)
                paintLogo.style = Paint.Style.FILL
                paintLogo.color = Color.parseColor(hexColor)
                canvas.drawRoundRect(fillRect, 5f, 5f, paintLogo)
            }
            
            startY += 65f
        }

        // 6. HUD / Fire Button Calibration
        startY += 20f
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 16f
        paintText.color = Color.WHITE
        canvas.drawText("HUD OPTIMIZED TRIGGERS", 50f, startY, paintText)
        canvas.drawLine(50f, startY + 14f, 320f, startY + 14f, paintBarLine)
        
        val hudY = startY + 35f
        val boxWidth = (width - 120f) / 2f
        
        // Box 1: Optimal HUD Fire Button Size
        val box1 = RectF(50f, hudY, 50f + boxWidth, hudY + 120f)
        paintSpecBg.color = Color.parseColor("#15181F")
        canvas.drawRoundRect(box1, 12f, 12f, paintSpecBg)
        paintSpecBorder.color = Color.parseColor("#FFBC00")
        paintSpecBorder.alpha = 80
        canvas.drawRoundRect(box1, 12f, 12f, paintSpecBorder)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 11f
        paintText.color = Color.parseColor("#888888")
        canvas.drawText("RECOMMENDED FIRE BUTTON", 68f, hudY + 30f, paintText)
        
        var btnSizeStr = "46% SIZE"
        if (sens.customHudLayout.contains("%")) {
            val idx = sens.customHudLayout.indexOf("%")
            if (idx != -1) {
                var startIdx = idx - 1
                while (startIdx >= 0 && sens.customHudLayout[startIdx].isDigit()) {
                    startIdx--
                }
                val parsed = sens.customHudLayout.substring(startIdx + 1, idx)
                if (parsed.isNotEmpty()) {
                    btnSizeStr = "$parsed% SIZE"
                }
            }
        }
        
        paintText.textSize = 24f
        paintText.color = Color.parseColor("#FF4B2B")
        canvas.drawText(btnSizeStr, 68f, hudY + 68f, paintText)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        paintText.textSize = 11f
        paintText.color = Color.parseColor("#AAAAAA")
        canvas.drawText("Position: Center-Right Low Hud", 68f, hudY + 98f, paintText)

        // Box 2: Drag Headshot Assist Method
        val box2 = RectF(70f + boxWidth, hudY, 70f + boxWidth * 2, hudY + 120f)
        paintSpecBg.color = Color.parseColor("#15181F")
        canvas.drawRoundRect(box2, 12f, 12f, paintSpecBg)
        canvas.drawRoundRect(box2, 12f, 12f, paintSpecBorder)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 11f
        paintText.color = Color.parseColor("#888888")
        canvas.drawText("DRAG AIM STRATEGY", 70f + boxWidth + 18f, hudY + 30f, paintText)
        
        paintText.textSize = 21f
        paintText.color = Color.parseColor("#00FF00")
        val dragStr = if (sens.dragSensitivity.length > 17) sens.dragSensitivity.substring(0, 16) + ".." else sens.dragSensitivity
        canvas.drawText(dragStr.uppercase(), 70f + boxWidth + 18f, hudY + 68f, paintText)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        paintText.textSize = 11f
        paintText.color = Color.parseColor("#AAAAAA")
        canvas.drawText("Flick: Instantaneous upward snap", 70f + boxWidth + 18f, hudY + 98f, paintText)

        // 7. Render AI Drag Aim Calibration Tip
        val tipY = hudY + 155f
        val tipBg = RectF(50f, tipY, width - 50f, tipY + 95f)
        paintSpecBg.color = Color.parseColor("#0C0E14")
        canvas.drawRoundRect(tipBg, 8f, 8f, paintSpecBg)
        paintSpecBorder.color = Color.parseColor("#FFFFFF")
        paintSpecBorder.alpha = 20
        canvas.drawRoundRect(tipBg, 8f, 8f, paintSpecBorder)
        
        paintText.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paintText.textSize = 10f
        paintText.color = Color.parseColor("#FFBC00")
        canvas.drawText("AI RECOIL CALIBRATION MANUAL KEY", 65f, tipY + 25f, paintText)
        
        // Wrap tip text perfectly
        val fullTip = sens.aimPrecision.replace("\n", " ").trim()
        val textPaintForLines = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#CCCCCC")
            textSize = 11f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        
        var currentTipY = tipY + 45f
        val maxWordsInLine = 11
        val words = fullTip.split(" ")
        var sentence = StringBuilder()
        var count = 0
        for (word in words) {
            sentence.append(word).append(" ")
            count++
            if (count >= maxWordsInLine) {
                if (currentTipY < tipY + 86f) {
                    canvas.drawText(sentence.toString(), 65f, currentTipY, textPaintForLines)
                    currentTipY += 18f
                }
                sentence = StringBuilder()
                count = 0
            }
        }
        if (sentence.isNotEmpty() && currentTipY < tipY + 86f) {
            canvas.drawText(sentence.toString(), 65f, currentTipY, textPaintForLines)
        }

        // 8. Draw Footer bar with security Hash Verification Code
        val footerY = height - 55f
        paintText.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paintText.textSize = 10f
        paintText.color = Color.parseColor("#666666")
        val hash = "CERT_AI_HASH_" + Integer.toHexString(sens.hashCode()).uppercase()
        canvas.drawText("VERIFIED: $hash", 50f, footerY, paintText)
        
        val waterMark = "GENERATED BY AI SENS PRO"
        val waterTextWidth = paintText.measureText(waterMark)
        canvas.drawText(waterMark, width - 50f - waterTextWidth, footerY, paintText)

        // Save Bitmap using MediaStore API
        return copyBitmapToGallery(context, bitmap, sens.brand, sens.model)
    }

    private fun copyBitmapToGallery(context: Context, bitmap: Bitmap, brand: String, model: String): Uri? {
        val filename = "AI_Sens_Pro_${brand}_${model}_${System.currentTimeMillis()}.png"
        val cr = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AISensPro")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val imageUri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (imageUri != null) {
            try {
                val outputStream: OutputStream? = cr.openOutputStream(imageUri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    cr.update(imageUri, contentValues, null, null)
                }
                return imageUri
            } catch (e: Exception) {
                e.printStackTrace()
                // cleanup
                cr.delete(imageUri, null, null)
            }
        }
        return null
    }
}
