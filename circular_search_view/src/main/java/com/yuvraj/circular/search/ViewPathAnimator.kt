package com.yuvraj.search


import android.graphics.Path
import android.graphics.PathMeasure
import android.os.Handler
import android.util.Pair
import android.view.View
import java.lang.ref.WeakReference
import java.util.*

open class ViewPathAnimator {
    val DEFAULT_DELAY = 1000 / 10
    val DEFAULT_FRAMESKIP = 3

    var handler: Handler? = null
    var animatedViews: HashMap<Int, PathRunnable>? = null

    @JvmOverloads
    fun animate(view: View, path: Path, delay: Int = DEFAULT_DELAY, frameSkip: Int = DEFAULT_FRAMESKIP) {
        if (animatedViews == null) {
            animatedViews = HashMap()
        }

        if (handler == null) {
            handler = Handler()
        }

        if (animatedViews!!.containsKey(view.hashCode())) {
            cancel(view)
        }

        view.visibility = View.GONE
        val runnable = PathRunnable(view, path, delay, frameSkip)
        animatedViews!![view.hashCode()] = runnable
        handler!!.postDelayed(runnable, delay.toLong())
    }

    fun cancel(view: View) {
        if (animatedViews != null && handler != null) {
            val task = animatedViews!![view.hashCode()]
            if (task != null) {
                handler!!.removeCallbacks(task)
                animatedViews!!.remove(view.hashCode())
            }
        }
    }

    class PathRunnable internal constructor(view: View, path: Path, private val delay: Int, frameSkip: Int) : Runnable {
        private val view: WeakReference<View> = WeakReference(view)
        internal var points: Array<Pair<Float, Float>?>
        private val frameSkip: Int
        private var frame: Int = 0
        var handler = Handler()

        init {
            this.points = getPoints(path)
            this.frameSkip = Math.max(frameSkip, 0)
            this.frame = 0
        }

        override fun run() {
            view.get()?.visibility = View.VISIBLE
            frame = (frame + frameSkip + 1) % points.size
            val pair = points[frame]

            val v = view.get()
            if (v != null) {
                v.translationX = pair?.first!!
                v.translationY = pair.second

                handler.postDelayed(this, delay.toLong())
            }
        }

        // https://stackoverflow.com/questions/7972780/how-do-i-find-all-the-points-in-a-path-in-android
        private fun getPoints(path: Path): Array<Pair<Float, Float>?> {
            val pathMeasure = PathMeasure(path, true)
            val frames = pathMeasure.length.toInt()

            val pointArray = arrayOfNulls<Pair<Float, Float>>(frames)
            val length = pathMeasure.length
            var distance = 0f
            val speed = length / pointArray.size
            var counter = 0
            val aCoordinates = FloatArray(2)

            while (distance < length && counter < pointArray.size) {
                // get point from the path
                pathMeasure.getPosTan(distance, aCoordinates, null)
                pointArray[counter] = Pair(aCoordinates[0], aCoordinates[1])
                counter++
                distance += speed
            }

            return pointArray
        }
    }
}