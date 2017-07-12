package io.wasin.asteroids.entities

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.asteroids.Game

/**
 * Created by haxpor on 7/9/17.
 */
open class SpaceObject {
    var x: Float = 0f
    var y: Float = 0f

    var dx: Float = 0f
    var dy: Float = 0f

    var radians: Float = 0f
    var speed: Float = 0f
    var rotationSpeed: Float = 0f

    var width: Int = 0
    var height: Int = 0

    var shapex: Array<Float> = emptyArray()
    var shapey: Array<Float> = emptyArray()

    open fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun wrap(viewport: Viewport) {
        if (x < 0f) x = viewport.camera.viewportWidth
        if (x > viewport.camera.viewportWidth) x = 0f
        if (y < 0f) y = viewport.camera.viewportHeight
        if (y > viewport.camera.viewportHeight) y = 0f
    }

    fun intersects(other: SpaceObject): Boolean {
        val otherShapex = other.shapex
        val otherShapey = other.shapey

        for (i in 0..otherShapex.count()-1) {
            if (contains(otherShapex[i], otherShapey[i])) {
                return true
            }
        }

        return false
    }

    // see algorithm at https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule
    fun contains(x: Float, y: Float): Boolean {
        var j = shapex.count() - 1
        var c = false

        for (i in 0..shapex.count()-1) {
            if ((shapey[i] > y) != (shapey[j] > y) &&
                    (x < (shapex[j] - shapex[i]) * (y - shapey[i]) / (shapey[j] - shapey[i]) + shapex[i])) {
                c = !c
            }
            j = i
        }
        return c
    }
}