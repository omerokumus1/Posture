package btk.huawei.arge.posture.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider { // for testing
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}