package btk.huawei.arge.posture.feature.main

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import btk.huawei.arge.posture.R
import btk.huawei.arge.posture.databinding.FragmentResultBinding
import btk.huawei.arge.posture.feature.main.resp.BitmapDrawer
import btk.huawei.arge.posture.feature.main.resp.SkeletonAnalyzer
import btk.huawei.arge.posture.util.ImageOperations
import com.huawei.hms.mlsdk.skeleton.MLSkeleton


class ResultFragment : Fragment() {

    private var binding: FragmentResultBinding? = null

    private val viewModel: ResultFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
        getImageFromBundle()?.let { viewModel.startAnalyze(it) }
    }

    private fun observeViewModelState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { event ->
                when (event) {
                    is SkeletonAnalyzer.AnalyzingStatus.Started -> {
                        setVisibilitiesForStart()
                    }
                    is SkeletonAnalyzer.AnalyzingStatus.Analyzing -> {
                    }
                    is SkeletonAnalyzer.AnalyzingStatus.Success -> {
                        setVisibilitiesForResult()
                        setResultingImage(event)
                        setResultingText()
                    }
                    is SkeletonAnalyzer.AnalyzingStatus.Failure -> {
                        setVisibilitiesForResult()
                        binding?.resultText?.text = "FAILURE!"
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setVisibilitiesForStart() {
        hideResultingViews()
        showProgressBar()
    }

    private fun setVisibilitiesForResult() {
        hideProgressBar()
        showResultingViews()

    }

    private fun showResultingViews() {
        binding?.run {
            resultImg.visibility = View.VISIBLE
            resultText.visibility = View.VISIBLE
        }
    }

    private fun hideResultingViews() {
        binding?.run {
            resultImg.visibility = View.GONE
            resultText.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        binding?.progressCircular?.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding?.progressCircular?.visibility = View.VISIBLE
    }

    private fun setResultingText() {
        val slope = BitmapDrawer.calculateSlope()
        val resultingText = if (slope != null) {
            if (slope > SkeletonAnalyzer.HEAD_SLOPE_BOUNDARY) {
                "Result: Bad posture! "
            } else {
                "Result: Good posture!"
            }
        } else {
            "Result: No comment."
        }
        binding?.resultText?.text = resultingText

    }

    private fun setResultingImage(event: SkeletonAnalyzer.AnalyzingStatus.Success) {
        val resultingBitmap = getResultingBitmap(event.results)
        val img = ImageOperations.bitmapToDrawable(resources, resultingBitmap)
        binding?.resultImg?.setImageBitmap(resultingBitmap)
    }

    private fun getResultingBitmap(results: MutableList<MLSkeleton>): Bitmap? {
        val bitmap = getImageFromBundle()
        val resultingBitmap =
            bitmap?.let {
                BitmapDrawer.drawOnBitmap(
                    it,
                    results,
                    ResourcesCompat.getColor(resources, R.color.red, null)
                )
            }
        return resultingBitmap
    }


    private fun getImageFromBundle(): Bitmap? {
        arguments?.let {
            return ResultFragmentArgs.fromBundle(it).uploadedBitmap
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}