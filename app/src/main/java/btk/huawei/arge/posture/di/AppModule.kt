package btk.huawei.arge.posture.di

import btk.huawei.arge.posture.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideCameraOperator(cameraRelatedJobs: UploadImageFragment.CameraRelatedJobs): ICameraOperator =
//        CameraOperator(cameraRelatedJobs)
//
//    @Singleton
//    @Provides
//    fun provideCameraRelatedJobs(@ApplicationContext context: Context) = object : UploadImageFragment.CameraRelatedJobs {
//        override fun isTakingPhotoGranted(): Boolean = ContextCompat.checkSelfPermission(
//            context,
//            android.Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//
//        override fun startActivityForResult() {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        }
//
//    }

//    @Singleton
//    @Provides
//    fun provideStorageOperator(@ApplicationContext context: Context): IStorageManager =
//        btk.huawei.arge.posture.feature.main.resp.StorageOperator(context)


    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

}