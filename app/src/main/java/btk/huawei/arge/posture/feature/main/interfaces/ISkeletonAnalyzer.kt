package btk.huawei.arge.posture.feature.main.interfaces

import android.graphics.Bitmap

interface ISkeletonAnalyzer {
    fun analyze(bitmap: Bitmap)
}