package com.nimbus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,          // "CLASSIC", "FLOW", "SPRINT"
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val dayOfYear: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR),
    val year: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
)
