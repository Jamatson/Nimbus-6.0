package com.nimbus.data.db

import androidx.room.*
import com.nimbus.data.model.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: SessionEntity)

    @Query("SELECT * FROM sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT COUNT(*) FROM sessions WHERE year = :year AND dayOfYear = :day")
    fun getSessionsForDay(year: Int, day: Int): Flow<Int>

    @Query("SELECT SUM(durationMinutes) FROM sessions WHERE year = :year AND dayOfYear >= :startDay AND dayOfYear <= :endDay")
    fun getWeeklyMinutes(year: Int, startDay: Int, endDay: Int): Flow<Int?>
}
