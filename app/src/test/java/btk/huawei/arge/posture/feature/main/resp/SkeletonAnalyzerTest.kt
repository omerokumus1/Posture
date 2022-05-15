package btk.huawei.arge.posture.feature.main.resp

import android.graphics.Bitmap
import btk.huawei.arge.posture.feature.main.interfaces.ISkeletonAnalyzer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SkeletonAnalyzerTest {

    lateinit var skeletonAnalyzer: ISkeletonAnalyzer

    @Before
    fun setUp() {
        skeletonAnalyzer = mock(ISkeletonAnalyzer::class.java)
    }

    @Test
    fun analyzeTest() {
        // Given
        val bitmap = mock(Bitmap::class.java)

        // When
        skeletonAnalyzer.analyze(bitmap)

        // Then
        verify(skeletonAnalyzer).analyze(bitmap)

    }
}