package io.wasin.asteroids.compat

/**
 * Created by haxpor on 7/11/17.
 *
 * Line2D is a minimal data class with aim to just hold data structure of java.awt.geom.Line2D
 * in which it is not supported on Android.
 *
 * It doesn't provide similar functionality of original class.
 *
 */
data class Point2D(var x: Float = 0f, var y: Float = 0f) {

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
}