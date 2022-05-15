package btk.huawei.arge.posture.feature.main.interfaces

interface IStorageOperator {
    fun isReadStoragePermissionGranted(): Boolean
    fun isWriteStoragePermissionGranted(): Boolean
    fun selectImage()

}