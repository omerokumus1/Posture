package btk.huawei.arge.posture.feature.main.resp

import android.graphics.Bitmap
import btk.huawei.arge.posture.feature.main.interfaces.ISkeletonAnalyzer
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.skeleton.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

object SkeletonAnalyzer : ISkeletonAnalyzer {

    private val _state = MutableStateFlow<AnalyzingStatus>(AnalyzingStatus.Idle)
    val state: StateFlow<AnalyzingStatus> = _state
    private var analyzer: MLSkeletonAnalyzer? = null

    private const val SCORE_LOWER_BOUND = 0.65F
    const val HEAD_SLOPE_BOUNDARY = 1F


    override fun analyze(bitmap: Bitmap) {
        _state.value = AnalyzingStatus.Started
        createAnalyzer()
        val frame = getMLFrame(bitmap)
        startAsyncAnalyzing(frame)
        stopAnalyzer()
    }

    private fun stopAnalyzer() {
        try {
            analyzer?.stop()
        } catch (e: IOException) {
            // Exception handling.
        }
    }

    private fun startAsyncAnalyzing(frame: MLFrame) {
        val task = analyzer?.asyncAnalyseFrame(frame)
        _state.value = AnalyzingStatus.Analyzing
        task?.addOnSuccessListener {
            _state.value = AnalyzingStatus.Success(clearJointsWithLowScore(it))
        }?.addOnFailureListener {
            _state.value = AnalyzingStatus.Failure("Analyzing has failed.")
        }
    }

    private fun clearJointsWithLowScore(joints: MutableList<MLSkeleton>): MutableList<MLSkeleton> {
        val jointsToBeRemoved = mutableListOf<MLJoint>()
        if (joints.isNotEmpty()) {
            for (j in joints.first().joints) {
                if (j.score < SCORE_LOWER_BOUND) {
                    jointsToBeRemoved.add(j)
                }
            }
            joints.first().joints.removeAll(jointsToBeRemoved)
        }

        return joints
    }

    private fun getMLFrame(bitmap: Bitmap): MLFrame {
        return MLFrame.fromBitmap(bitmap)
    }


    private fun createAnalyzer() {
        val setting = MLSkeletonAnalyzerSetting.Factory() // Set the detection mode.
            // Detect skeleton points for normal postures.
            .setAnalyzerType(MLSkeletonAnalyzerSetting.TYPE_NORMAL)
            .create()
        analyzer = MLSkeletonAnalyzerFactory.getInstance().getSkeletonAnalyzer(setting)
    }

    // to represent the state of the main app
    sealed class AnalyzingStatus {
        class Success(
            val results: MutableList<MLSkeleton>,
        ) : AnalyzingStatus()

        class Failure(val errorText: String) : AnalyzingStatus()
        object Analyzing : AnalyzingStatus()
        object Idle : AnalyzingStatus()
        object Started : AnalyzingStatus()
    }

    class JointPoint(val x: Float, val y: Float)

}