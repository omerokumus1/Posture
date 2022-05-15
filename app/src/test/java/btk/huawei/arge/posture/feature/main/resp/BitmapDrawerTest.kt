package btk.huawei.arge.posture.feature.main.resp

import android.graphics.Bitmap
import android.graphics.Color
import btk.huawei.arge.posture.feature.main.interfaces.IBitmapDrawer
import com.huawei.hms.mlsdk.skeleton.MLJoint
import com.huawei.hms.mlsdk.skeleton.MLSkeleton
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.properties.Delegates

@RunWith(MockitoJUnitRunner::class)
class BitmapDrawerTest {

    lateinit var bitmapDrawer: IBitmapDrawer
    lateinit var bitmap: Bitmap
    var color by Delegates.notNull<Int>()

    @Before
    fun setUp() {
        bitmapDrawer = mock(IBitmapDrawer::class.java)
        bitmap = mock(Bitmap::class.java)
        color = Color.RED
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `drawOnBitmap when result input is empty`() {
        // Given
        val resultList = mutableListOf<MLSkeleton>()

        // When
        val resultingBitmap = BitmapDrawer.drawOnBitmap(bitmap, resultList, color)

        // Then
        assertEquals(bitmap, resultingBitmap)
    }

    @Test
    fun `drawOnBitmap when result input has joints with low score`() {
        // Given
        val joints = mutableListOf(
            MLJoint(10F, 10F, MLJoint.TYPE_HEAD_TOP, 0.8F),
            MLJoint(10F, 20F, MLJoint.TYPE_NECK, 0.8F),
            MLJoint(10F, 30F, MLJoint.TYPE_LEFT_SHOULDER, 0.3F),
            MLJoint(10F, 40F, MLJoint.TYPE_LEFT_ELBOW, 0.2F),
        )
        val resultList = mutableListOf(MLSkeleton(joints))

        // When
        bitmapDrawer.drawOnBitmap(bitmap, resultList, color)

        // Then
        verify(bitmapDrawer).drawOnBitmap(bitmap, resultList, color)
    }


    @Test
    fun `drawOnBitmapWhen result input has valid joints`() {
        // Given
        val joints = mutableListOf(
            MLJoint(10F, 10F, MLJoint.TYPE_HEAD_TOP, 0.8F),
            MLJoint(10F, 20F, MLJoint.TYPE_NECK, 0.8F),
            MLJoint(10F, 30F, MLJoint.TYPE_LEFT_SHOULDER, 0.8F),
            MLJoint(10F, 40F, MLJoint.TYPE_LEFT_ELBOW, 0.8F),
        )
        val resultList = mutableListOf(MLSkeleton(joints))

        // When
        bitmapDrawer.drawOnBitmap(bitmap, resultList, color)

        // Then
        verify(bitmapDrawer).drawOnBitmap(bitmap, resultList, color)
    }

    @Test
    fun `calculateSlopeTest result is not null`() {
        // Given

        // When
        val result = bitmapDrawer.calculateSlope()

        // Then
        assertNotNull(result)
    }

}