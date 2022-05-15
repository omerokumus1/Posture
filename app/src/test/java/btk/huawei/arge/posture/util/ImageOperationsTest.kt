package btk.huawei.arge.posture.util

import android.content.res.Resources
import android.graphics.Bitmap
import btk.huawei.arge.posture.feature.main.resp.BitmapDrawer
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImageOperationsTest {

    lateinit var resources: Resources
    lateinit var bitmap: Bitmap

    @Before
    fun setup(){
    }

    @Test
    fun `bitmapToDrawable when bitmap is null`() {
        // Given
        val bitmap: Bitmap? = null
        resources = mock(Resources::class.java)

        // When
        val result = ImageOperations.bitmapToDrawable(resources, bitmap)

        // Then
        assertNull(result)
    }

    @Test
    fun `bitmapToDrawable when bitmap is not null`() {
        // Given
        bitmap = mock(Bitmap::class.java)
        resources = mock(Resources::class.java)

        // When
        val result = ImageOperations.bitmapToDrawable(resources, bitmap)

        // Then
        assertNotNull(result)
    }



    @Test
    fun `toMutableBitmap when bitmap is null`() {
        // Given
        val bitmap: Bitmap? = null

        // When
        val result = ImageOperations.toMutableBitmap(bitmap)

        // Then
        assertNull(result)
    }

    @Test
    fun `toMutableBitmap when bitmap is not null`() {
        // Given
        bitmap = mock(Bitmap::class.java)

        // When
        val result = ImageOperations.toMutableBitmap(bitmap)

        // Then
        assertNotEquals(bitmap, result)
    }


}