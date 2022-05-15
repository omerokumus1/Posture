package btk.huawei.arge.posture.feature.main.resp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.collection.arrayMapOf
import btk.huawei.arge.posture.feature.main.interfaces.IBitmapDrawer
import com.huawei.hms.mlsdk.skeleton.MLJoint
import com.huawei.hms.mlsdk.skeleton.MLSkeleton

object BitmapDrawer : IBitmapDrawer {

    private const val SCALE: Float = 250.0F

    private val LEFT_SIDE_ASPECT_JOINT_PAIRS = arrayMapOf(
        Pair(MLJoint.TYPE_HEAD_TOP, MLJoint.TYPE_NECK),
        Pair(MLJoint.TYPE_LEFT_SHOULDER, MLJoint.TYPE_LEFT_ELBOW),
    )
    private val RIGHT_SIDE_ASPECT_JOINT_PAIRS = arrayMapOf(
        Pair(MLJoint.TYPE_HEAD_TOP, MLJoint.TYPE_NECK),
        Pair(MLJoint.TYPE_RIGHT_SHOULDER, MLJoint.TYPE_RIGHT_ELBOW),
    )
    private val FRONT_ASPECT_JOINT_PAIRS = arrayMapOf(
        Pair(MLJoint.TYPE_HEAD_TOP, MLJoint.TYPE_NECK),
        Pair(MLJoint.TYPE_RIGHT_SHOULDER, MLJoint.TYPE_LEFT_SHOULDER),
    )

    private val RIGHT_JOINTS =
        listOf(
            MLJoint.TYPE_RIGHT_ANKLE, MLJoint.TYPE_RIGHT_ELBOW, MLJoint.TYPE_RIGHT_HIP,
            MLJoint.TYPE_RIGHT_KNEE, MLJoint.TYPE_RIGHT_SHOULDER, MLJoint.TYPE_RIGHT_WRIST
        )

    private val LEFT_JOINTS =
        listOf(
            MLJoint.TYPE_LEFT_ANKLE, MLJoint.TYPE_LEFT_ELBOW, MLJoint.TYPE_LEFT_HIP,
            MLJoint.TYPE_LEFT_KNEE, MLJoint.TYPE_LEFT_SHOULDER, MLJoint.TYPE_LEFT_WRIST
        )

    private var crossingPoints: List<AxisPoints>? = null

    override fun drawOnBitmap(
        bitmap: Bitmap,
        results: MutableList<MLSkeleton>,
        color: Int
    ): Bitmap {
        if (results.isEmpty()) {
            return bitmap
        }

        val canvas = Canvas(bitmap)
        val paint = getPaint(color)
        drawJointCircles(results, canvas, paint)
        drawLines(results, canvas, paint)

        return bitmap
    }

    private fun drawLines(
        results: MutableList<MLSkeleton>,
        canvas: Canvas,
        paint: Paint
    ) {
        crossingPoints = getPoints(results)
        if (crossingPoints != null) {
            for (p in crossingPoints!!) {
                canvas.drawLine(p.x, 0F, 0F, p.y, paint)
            }
        }


    }

    private fun getPoints(results: MutableList<MLSkeleton>): List<AxisPoints> {
        val joints = results.first().joints
        val points = mutableListOf<AxisPoints>()
        if (isLeftSideAspect(joints)) {
            points.addAll(getLeftSidePoints(joints))
        } else if (isRightSideAspect(joints)) {
            points.addAll(getRightSidePoints(joints))
        } else {
            points.addAll(getFrontAspectPoints(joints))
        }

        return points
    }

    private fun getFrontAspectPoints(joints: MutableList<MLJoint>): Collection<AxisPoints> {
        val frontAspectPoints = mutableListOf<AxisPoints>()
        for (pair in FRONT_ASPECT_JOINT_PAIRS) {
            if (isPairAvailable(pair, joints)) {
                getCrossingAxisPoints(pair, joints)?.let { frontAspectPoints.add(it) }
            }
        }

        return frontAspectPoints
    }

    private fun getRightSidePoints(joints: MutableList<MLJoint>): Collection<AxisPoints> {
        val rightSidePoints = mutableListOf<AxisPoints>()
        for (pair in RIGHT_SIDE_ASPECT_JOINT_PAIRS) {
            if (isPairAvailable(pair, joints)) {
                getCrossingAxisPoints(pair, joints)?.let { rightSidePoints.add(it) }
            }
        }

        return rightSidePoints
    }

    private fun getLeftSidePoints(joints: MutableList<MLJoint>): Collection<AxisPoints> {
        val leftSidePoints = mutableListOf<AxisPoints>()
        for (pair in LEFT_SIDE_ASPECT_JOINT_PAIRS) {
            if (isPairAvailable(pair, joints)) {
                getCrossingAxisPoints(pair, joints)?.let { leftSidePoints.add(it) }
            }
        }
        return leftSidePoints
    }

    private fun isPairAvailable(
        pair: MutableMap.MutableEntry<Int, Int>,
        joints: MutableList<MLJoint>
    ): Boolean = isTypeAvailable(pair.key, joints) && isTypeAvailable(pair.value, joints)

    private fun isTypeAvailable(type: Int, joints: MutableList<MLJoint>): Boolean {
        for (j in joints) {
            if (j.type == type) {
                return true
            }
        }
        return false
    }

    private fun getCrossingAxisPoints(
        pair: MutableMap.MutableEntry<Int, Int>,
        joints: MutableList<MLJoint>
    ): AxisPoints? {
        val keyCoords = getCoords(pair.key, joints)
        val valCoords = getCoords(pair.value, joints)
        val crossingPoints = getAxisCrossingPoints(keyCoords, valCoords)
        crossingPoints?.let {
            return AxisPoints(
                crossingPoints[0],
                crossingPoints[1]
            )
        }
        return null
    }

    private fun getAxisCrossingPoints(
        keyCoords: List<Float>?,
        valCoords: List<Float>?
    ): List<Float>? {
        keyCoords?.let {
            valCoords?.let {
                if ((valCoords[0] - keyCoords[0]) != 0F) {
                    val m =
                        getSlope(valCoords, keyCoords) // (y2-y1) / (x2-x1)
                    val a = m // a = m
                    val b = keyCoords[1] - m * keyCoords[0] // b = y1 + m*x1
                    // y = 0 => ax = -b, x = -b/a
                    val xCrossing = -b / a
                    // x = 0 => y = b
                    val yCrossing = b
                    return listOf(xCrossing, -yCrossing)
                } else {
                    val xCrossing = valCoords[0]
                    val yCrossing = 0F
                    return listOf(xCrossing, -yCrossing)
                }
            }
        }
        return null
    }

    private fun getSlope(
        p1: List<Float>,
        p2: List<Float>
    ) = (p1[1] - p2[1]) / (p1[0] - p2[0])

    private fun getCoords(type: Int, joints: MutableList<MLJoint>): List<Float>? {
        for (j in joints) {
            if (j.type == type) {
                return listOf(j.pointX, -j.pointY)
            }
        }
        return null
    }

    private fun isRightSideAspect(coords: MutableList<MLJoint>) =
        (rightSideAvailable(coords) && !leftSideAvailable(coords))

    private fun isLeftSideAspect(coords: MutableList<MLJoint>) =
        (leftSideAvailable(coords) && !rightSideAvailable(coords))

    private fun rightSideAvailable(coords: MutableList<MLJoint>): Boolean {
        for (c in coords) {
            if (c.type in RIGHT_JOINTS) {
                return true
            }
        }
        return false
    }

    private fun leftSideAvailable(coords: MutableList<MLJoint>): Boolean {
        for (c in coords) {
            if (c.type in LEFT_JOINTS) {
                return true
            }
        }
        return false
    }

    private fun drawJointCircles(
        results: MutableList<MLSkeleton>,
        canvas: Canvas,
        paint: Paint
    ) {
        val coords = getCoords(results)
        val radius = getRadius(canvas)
        for (c in coords)
            canvas.drawCircle(c.x, c.y, radius, paint)
    }

    private fun getCoords(results: MutableList<MLSkeleton>): MutableList<SkeletonAnalyzer.JointPoint> {
        val joints = results.first().joints
        val arr = mutableListOf<SkeletonAnalyzer.JointPoint>()
        for (j in joints) {
            arr.add(
                SkeletonAnalyzer.JointPoint(j.pointX, j.pointY)
            )
        }
        return arr
    }

    private fun getPaint(color: Int) =
        Paint().apply {
            isAntiAlias = true
            this.color = color
            style = Paint.Style.FILL
        }


    private fun getRadius(canvas: Canvas): Float = (canvas.width / SCALE) * (canvas.height / SCALE)

    override fun calculateSlope(): Float? {
        val point = crossingPoints?.get(0)
        return if (point?.x != 0F) {
            point?.y?.div(point.x)
        } else {
            0F
        }
    }

    class AxisPoints(val x: Float, val y: Float)
}