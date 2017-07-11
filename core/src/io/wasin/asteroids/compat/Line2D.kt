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
data class Line2D(var x1: Float = 0f, var y1: Float = 0f, var x2: Float = 0f, var y2: Float = 0f) {

    fun setLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
    }
}