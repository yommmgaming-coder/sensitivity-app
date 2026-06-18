package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "devices")
data class PhoneDevice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brand: String,
    val model: String,
    val ram: String,
    val processor: String,
    val refreshRate: String,
    val screenSize: String,
    val resolution: String,
    val osVersion: String,
    val dpi: Int
) : java.io.Serializable

@Entity(tableName = "sensitivities")
data class SavedSensitivity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val brand: String,
    val model: String,
    val general: Int,
    val redDot: Int,
    val scope2X: Int,
    val scope4X: Int,
    val awm: Int,
    val freeLook: Int,
    val customHudLayout: String,
    val aimPrecision: String,
    val bestDpi: Int,
    val dragSensitivity: String,
    val graphicsSetting: String,
    val fpsSetting: String,
    val timestamp: Long = System.currentTimeMillis()
) : java.io.Serializable

@Entity(tableName = "pro_settings")
data class ProSetting(
    @PrimaryKey val id: String,
    val playerName: String,
    val teamName: String,
    val deviceName: String,
    val general: Int,
    val redDot: Int,
    val scope2X: Int,
    val scope4X: Int,
    val awm: Int,
    val freeLook: Int,
    val dpi: Int,
    val customHudLayout: String,
    val likes: Int = 240
) : java.io.Serializable

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val deviceModel: String,
    val title: String,
    val general: Int,
    val redDot: Int,
    val scope2X: Int,
    val scope4X: Int,
    val awm: Int,
    val freeLook: Int,
    val rating: Float,
    val likes: Int,
    val commentsCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
