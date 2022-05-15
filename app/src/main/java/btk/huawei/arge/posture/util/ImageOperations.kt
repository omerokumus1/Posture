package btk.huawei.arge.posture.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object ImageOperations {

    fun bitmapToDrawable(resources: Resources, bitmap: Bitmap?): Drawable? {
        bitmap?.let {
            return BitmapDrawable(resources, bitmap)
        }
        return null
    }

    fun toMutableBitmap(bitmap: Bitmap?): Bitmap? {
        bitmap?.let {
            return it.copy(Bitmap.Config.ARGB_8888, true);
        }
        return null
    }
}