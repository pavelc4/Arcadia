package com.android.arcadia.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface SystemHelper {
    fun getFps(): Flow<Int>
    fun getCpuUsage(): Flow<Int>
    fun getRamUsage(): Flow<RamInfo>
    fun getBatteryInfo(): Flow<BatteryInfo>
}

data class RamInfo(
    val usedMb: Long,
    val totalMb: Long,
    val percent: Int
)

data class BatteryInfo(
    val level: Int,
    val temperature: Float,
    val currentNow: Int, // in mA
    val status: String
)


class SystemHelperMock : SystemHelper {
    override fun getFps(): Flow<Int> = flow {
        while (true) {
            emit((55..60).random())
            delay(1000)
        }
    }

    override fun getCpuUsage(): Flow<Int> = flow {
        while (true) {
            emit((20..45).random())
            delay(2000)
        }
    }

    override fun getRamUsage(): Flow<RamInfo> = flow {
        val total = 8192L // 8GB Mock
        while (true) {
            val used = (3000L..7000L).random()
            val percent = ((used.toDouble() / total) * 100).toInt()
            emit(RamInfo(used, total, percent))
            delay(3000)
        }
    }

    override fun getBatteryInfo(): Flow<BatteryInfo> = flow {
        while (true) {
            emit(BatteryInfo(
                level = (20..100).random(),
                temperature = (30..45).random().toFloat(),
                currentNow = (-1000..500).random(),
                status = "Discharging"
            ))
            delay(5000)
        }
    }
}

/**
 * Real implementation for System Build (AOSP).
 * TODO: Implement actual Binder calls to SurfaceFlinger and /proc/stat reading.
 */
class SystemHelperReal : SystemHelper {
    override fun getFps(): Flow<Int> = flow {
        while (true) {
            emit(0) 
            delay(1000)
        }
    }

    override fun getCpuUsage(): Flow<Int> = flow {
        while (true) {
            emit(0)
            delay(2000)
        }
    }

    override fun getRamUsage(): Flow<RamInfo> = flow {
        while (true) {
            emit(RamInfo(0, 0, 0))
            delay(10000)
        }
    }

    override fun getBatteryInfo(): Flow<BatteryInfo> = flow {
        while (true) {
            emit(BatteryInfo(0, 0f, 0, "Unknown"))
            delay(10000)
        }
    }
}
