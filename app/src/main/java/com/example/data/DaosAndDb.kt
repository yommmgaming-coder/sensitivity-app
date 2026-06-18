package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices ORDER BY brand ASC, model ASC")
    fun getAllDevices(): Flow<List<PhoneDevice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: PhoneDevice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<PhoneDevice>)

    @Query("DELETE FROM devices WHERE id = :id")
    suspend fun deleteDevice(id: Int)
}

@Dao
interface SensitivityDao {
    @Query("SELECT * FROM sensitivities ORDER BY timestamp DESC")
    fun getAllSensitivities(): Flow<List<SavedSensitivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSensitivity(setting: SavedSensitivity)

    @Query("DELETE FROM sensitivities WHERE id = :id")
    suspend fun deleteSensitivity(id: Int)
}

@Dao
interface ProSettingDao {
    @Query("SELECT * FROM pro_settings ORDER BY likes DESC")
    fun getAllProSettings(): Flow<List<ProSetting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProSettings(settings: List<ProSetting>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProSetting(setting: ProSetting)
}

@Dao
interface CommunityPostDao {
    @Query("SELECT * FROM community_posts ORDER BY timestamp DESC")
    fun getAllCommunityPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPost)

    @Query("UPDATE community_posts SET likes = likes + 1 WHERE id = :id")
    suspend fun likePost(id: Int)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}

@Database(
    entities = [
        PhoneDevice::class,
        SavedSensitivity::class,
        ProSetting::class,
        CommunityPost::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun sensitivityDao(): SensitivityDao
    abstract fun proSettingDao(): ProSettingDao
    abstract fun communityPostDao(): CommunityPostDao
    abstract fun chatMessageDao(): ChatMessageDao
}
