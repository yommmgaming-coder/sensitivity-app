package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class SensitivityRepository(private val database: AppDatabase) {

    val allDevices: Flow<List<PhoneDevice>> = database.deviceDao().getAllDevices()
    val allSensitivities: Flow<List<SavedSensitivity>> = database.sensitivityDao().getAllSensitivities()
    val allProSettings: Flow<List<ProSetting>> = database.proSettingDao().getAllProSettings()
    val allCommunityPosts: Flow<List<CommunityPost>> = database.communityPostDao().getAllCommunityPosts()
    val chatHistory: Flow<List<ChatMessage>> = database.chatMessageDao().getAllMessages()

    suspend fun insertSensitivity(setting: SavedSensitivity) {
         database.sensitivityDao().insertSensitivity(setting)
    }

    suspend fun deleteSensitivity(id: Int) {
         database.sensitivityDao().deleteSensitivity(id)
    }

    suspend fun insertPost(post: CommunityPost) {
         database.communityPostDao().insertPost(post)
    }

    suspend fun likePost(id: Int) {
         database.communityPostDao().likePost(id)
    }

    suspend fun insertDevice(device: PhoneDevice) {
         database.deviceDao().insertDevice(device)
    }

    suspend fun deleteDevice(id: Int) {
         database.deviceDao().deleteDevice(id)
    }

    suspend fun insertChatMessage(message: ChatMessage) {
         database.chatMessageDao().insertMessage(message)
    }

    suspend fun clearChatHistory() {
         database.chatMessageDao().clearHistory()
    }

    // Seed Data Helper to ensure the UI starts with realistic, functional data
    suspend fun seedDatabaseIfEmpty() {
        // 1. Device list seed
        val currentDevices = database.deviceDao().getAllDevices().first()
        if (currentDevices.isEmpty()) {
            val sampleDevices = listOf(
                PhoneDevice(brand = "Samsung", model = "Galaxy S24 Ultra", ram = "12GB", processor = "Snapdragon 8 Gen 3", refreshRate = "120Hz", screenSize = "6.8 inches", resolution = "1440x3120", osVersion = "Android 14 (One UI 6.1)", dpi = 411),
                PhoneDevice(brand = "Samsung", model = "Galaxy A55", ram = "8GB", processor = "Exynos 1480", refreshRate = "120Hz", screenSize = "6.6 inches", resolution = "1080x2340", osVersion = "Android 14 (One UI 6.1)", dpi = 384),
                PhoneDevice(brand = "Xiaomi", model = "POCO F6 Pro", ram = "12GB", processor = "Snapdragon 8 Gen 2", refreshRate = "120Hz", screenSize = "6.67 inches", resolution = "1440x3200", osVersion = "Android 14 (HyperOS)", dpi = 440),
                PhoneDevice(brand = "Xiaomi", model = "Redmi Note 13 Pro+", ram = "8GB", processor = "MediaTek Dimensity 7200 Ultra", refreshRate = "120Hz", screenSize = "6.67 inches", resolution = "1220x2712", osVersion = "Android 14 (HyperOS)", dpi = 446),
                PhoneDevice(brand = "Apple", model = "iPhone 15 Pro Max", ram = "8GB", processor = "Apple A17 Pro", refreshRate = "120Hz", screenSize = "6.7 inches", resolution = "1290x2796", osVersion = "iOS 17", dpi = 460),
                PhoneDevice(brand = "Apple", model = "iPhone 14 Plus", ram = "6GB", processor = "Apple A15 Bionic", refreshRate = "60Hz", screenSize = "6.7 inches", resolution = "1284x2778", osVersion = "iOS 16", dpi = 458),
                PhoneDevice(brand = "OnePlus", model = "OnePlus 12", ram = "16GB", processor = "Snapdragon 8 Gen 3", refreshRate = "120Hz", screenSize = "6.82 inches", resolution = "1440x3168", osVersion = "Android 14 (OxygenOS)", dpi = 450),
                PhoneDevice(brand = "Realme", model = "Realme GT 6T", ram = "12GB", processor = "Snapdragon 7+ Gen 3", refreshRate = "120Hz", screenSize = "6.78 inches", resolution = "1264x2780", osVersion = "Android 14 (Realme UI 5)", dpi = 450),
                PhoneDevice(brand = "Vivo", model = "V30 Pro", ram = "12GB", processor = "MediaTek Dimensity 8200", refreshRate = "120Hz", screenSize = "6.78 inches", resolution = "1260x2800", osVersion = "Android 14 (Funtouch 14)", dpi = 452),
                PhoneDevice(brand = "Oppo", model = "Reno 11 Pro 5G", ram = "12GB", processor = "MediaTek Dimensity 8200", refreshRate = "120Hz", screenSize = "6.7 inches", resolution = "1080x2412", osVersion = "Android 14 (ColorOS 14)", dpi = 394),
                PhoneDevice(brand = "ASUS", model = "ROG Phone 8 Pro", ram = "16GB", processor = "Snapdragon 8 Gen 3", refreshRate = "165Hz", screenSize = "6.78 inches", resolution = "1080x2400", osVersion = "Android 14 (ROG UI)", dpi = 388)
            )
            database.deviceDao().insertDevices(sampleDevices)
        }

        // 2. Pro Player preset seed
        val currentPro = database.proSettingDao().getAllProSettings().first()
        if (currentPro.isEmpty()) {
            val samplePro = listOf(
                ProSetting(id = "1", playerName = "Nobru", teamName = "Fluxo", deviceName = "iPhone 15 Pro Max", general = 196, redDot = 184, scope2X = 176, scope4X = 168, awm = 100, freeLook = 120, dpi = 720, customHudLayout = "4-Finger Claw: Shoot Left/Crouch on Top-Right/Gloo Wall extreme left bottom"),
                ProSetting(id = "2", playerName = "Ruok FF", teamName = "Solo Player", deviceName = "ASUS ROG Phone 8", general = 200, redDot = 190, scope2X = 180, scope4X = 184, awm = 90, freeLook = 140, dpi = 800, customHudLayout = "3-Finger Hybrid: Right trigger shoot, gloo wall button scaled to 100%"),
                ProSetting(id = "3", playerName = "WhiteFF", teamName = "E-Sports Latam", deviceName = "Samsung S24 Ultra", general = 190, redDot = 200, scope2X = 190, scope4X = 200, awm = 110, freeLook = 100, dpi = 650, customHudLayout = "2-Finger Speed: Jump bottom-right, Fire button layout size 48% at coordinate center"),
                ProSetting(id = "4", playerName = "Vincenzzo", teamName = "B2K Clan", deviceName = "iPad Pro", general = 180, redDot = 170, scope2X = 160, scope4X = 156, awm = 70, freeLook = 90, dpi = 580, customHudLayout = "4-Finger iPad: Large Gloo Wall and Jump keys spaced for index finger reflexes")
            )
            database.proSettingDao().insertProSettings(samplePro)
        }

        // 3. Community section seed
        val currentCommunity = database.communityPostDao().getAllCommunityPosts().first()
        if (currentCommunity.isEmpty()) {
            val samplePosts = listOf(
                CommunityPost(author = "HeadshotGod_FF", deviceModel = "Poco F6", title = "Insane drag headshot sensitivity! Super smooth auto-aim.", general = 198, redDot = 194, scope2X = 188, scope4X = 188, awm = 100, freeLook = 120, rating = 4.8f, likes = 128, commentsCount = 14),
                CommunityPost(author = "SoneetaFF", deviceModel = "iPhone 14 Pro", title = "Red Dot laser precision sensitivity. Perfect for Ranked Mode", general = 190, redDot = 180, scope2X = 174, scope4X = 170, awm = 80, freeLook = 100, rating = 4.5f, likes = 84, commentsCount = 7),
                CommunityPost(author = "Brazil_Spec", deviceModel = "Galaxy S23", title = "DPI 640 custom sens. Gloo wall placement speed increased by 30%", general = 200, redDot = 200, scope2X = 196, scope4X = 196, awm = 60, freeLook = 150, rating = 4.9f, likes = 246, commentsCount = 32)
            )
            for (p in samplePosts) {
                database.communityPostDao().insertPost(p)
            }
        }
    }
}
