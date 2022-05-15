package btk.huawei.arge.posture.feature.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import btk.huawei.arge.posture.databinding.FragmentUploadImageBinding
import btk.huawei.arge.posture.feature.main.interfaces.ICameraOperator
import btk.huawei.arge.posture.feature.main.interfaces.IStorageOperator
import btk.huawei.arge.posture.util.ImageOperations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadImageFragment : Fragment() {

    private var binding: FragmentUploadImageBinding? = null
    private var cameraOperator: ICameraOperator? = null
    private var storageOperator: IStorageOperator? = null
    private var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
    private var storageResultLauncher: ActivityResultLauncher<Intent>? = null
    private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null
    private var storagePermissionLauncher: ActivityResultLauncher<String>? = null

    private var uploadedImage: Bitmap? = null

    companion object {
        private const val DATA_KEY = "data"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraResultLauncher = getCameraResultLauncher()
        storageResultLauncher = getStorageResultLauncher()
        cameraPermissionLauncher = getCameraPermissionLauncher()
        storagePermissionLauncher = getStoragePermissionLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        setUploadBtnClickListener()
        setCameraBtnClickListener()
    }

    private fun setCameraBtnClickListener() {
        binding?.cameraBtn?.setOnClickListener {
            cameraOperator = getCameraOperator()
            cameraOperator?.takePhoto()
        }
    }

    private fun navigateToShowImageFragment() {
        val action = UploadImageFragmentDirections
            .actionUploadImageFragmentToShowImageFragment(uploadedImage)
        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
    }


    private fun getCameraResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uploadedImage =
                    ImageOperations.toMutableBitmap(data?.extras?.get(DATA_KEY) as Bitmap)
                navigateToShowImageFragment()
            }
        }

    private fun getStorageResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dataUri = result.data?.data
                uploadedImage = ImageOperations.toMutableBitmap(getUploadedImage(dataUri))
                navigateToShowImageFragment()
            }

        }

    private fun getUploadedImage(dataUri: Uri?): Bitmap? {
        context?.let {
            if (Build.VERSION.SDK_INT >= 28) {
                val source = dataUri?.let { uri ->
                    ImageDecoder.createSource(it.contentResolver, uri)
                }
                return source?.let { src -> ImageDecoder.decodeBitmap(src) }
            } else {
                return MediaStore.Images.Media.getBitmap(it.contentResolver, dataUri)
            }
        }
        return null
    }

    private fun getCameraPermissionLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCameraAndSetImage()
            } else {
                showPermissionDeniedMessage()
            }
        }

    private fun getStoragePermissionLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openGalleryAndSetImage()
            } else {
                showPermissionDeniedMessage()
            }
        }

    private fun openCameraAndSetImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher?.launch(intent)
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(activity, "Permission has denied.", Toast.LENGTH_LONG).show()
    }

    private fun setUploadBtnClickListener() {
        binding?.uploadBtn?.setOnClickListener {
            storageOperator = getStorageOperator()
            storageOperator?.selectImage()
        }
    }

    private fun getCameraOperator(): ICameraOperator = object : ICameraOperator {
        override fun isTakingPhotoGranted(): Boolean = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            )
        } == PackageManager.PERMISSION_GRANTED


        override fun takePhoto() {
            if (isTakingPhotoGranted()) {
                openCameraAndSetImage()
            } else {
                requestCameraPermission()
            }
        }

        override fun requestCameraPermission() {
            cameraPermissionLauncher?.launch(Manifest.permission.CAMERA)
        }
    }

    private fun getStorageOperator(): IStorageOperator = object : IStorageOperator {
        override fun isReadStoragePermissionGranted() = activity?.applicationContext?.let {
            ContextCompat.checkSelfPermission(
                it, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED

        override fun isWriteStoragePermissionGranted() = activity?.applicationContext?.let {
            ContextCompat.checkSelfPermission(
                it, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED

        override fun selectImage() {
            if (isReadStoragePermissionGranted()) {
                openGalleryAndSetImage()
            } else {
                storagePermissionLauncher?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

    }

    private fun openGalleryAndSetImage() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        storageResultLauncher?.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
//        cameraOperator = null
//        storageOperator = null
//        storageResultLauncher = null
//        cameraResultLauncher = null
//        cameraPermissionLauncher = null
//        storagePermissionLauncher = null
    }

}