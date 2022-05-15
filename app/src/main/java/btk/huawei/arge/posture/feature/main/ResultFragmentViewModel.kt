package btk.huawei.arge.posture.feature.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import btk.huawei.arge.posture.feature.main.resp.SkeletonAnalyzer

class ResultFragmentViewModel : ViewModel() {

    val state = SkeletonAnalyzer.state

    fun startAnalyze(bitmap: Bitmap) {
        SkeletonAnalyzer.analyze(bitmap)
    }

}