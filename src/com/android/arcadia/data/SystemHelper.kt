package com.android.arcadia.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface SystemHelper {
    fun getFps(): Flow<Int>
    fun getCpuUsage(): Flow<Int>
}


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
}
