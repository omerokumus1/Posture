package btk.huawei.arge.posture.feature.main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import btk.huawei.arge.posture.databinding.FragmentShowImageBinding
import btk.huawei.arge.posture.util.ImageOperations


class ShowImageFragment : Fragment() {

    private var binding: FragmentShowImageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartBtnOnClickListener()
        setUploadedImage()
    }

    private fun setStartBtnOnClickListener() {
        binding?.startBtn?.setOnClickListener {
            val action = getImageFromBundle()?.let { uploadedBitmap ->
                ShowImageFragmentDirections
                    .actionShowImageFragmentToResultFragment(uploadedBitmap)
            }
            if (action != null) {
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    private fun setUploadedImage() {
        val image = getImageAsDrawable()
        if (image != null) {
            binding?.uploadedImg?.setImageBitmap(getImageFromBundle())
        }
    }

    private fun getImageAsDrawable(): Drawable? {
        val bitmap = getImageFromBundle()
        return ImageOperations.bitmapToDrawable(resources, bitmap)
    }

    private fun getImageFromBundle(): Bitmap? {
        arguments?.let {
            return ShowImageFragmentArgs.fromBundle(it).uploadedBitmap
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}