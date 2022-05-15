package btk.huawei.arge.posture.feature.main.interfaces

interface ICameraOperator {
    fun isTakingPhotoGranted(): Boolean
    fun takePhoto()
    fun requestCameraPermission()
}