package btk.huawei.arge.posture.feature.main.interfaces

import android.graphics.Bitmap
import com.huawei.hms.mlsdk.skeleton.MLSkeleton

interface IBitmapDrawer {
    fun drawOnBitmap(
        bitmap: Bitmap,
        results: MutableList<MLSkeleton>,
        color: Int
    ): Bitmap

    fun calculateSlope(): Float?
}