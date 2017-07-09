package io.wasin.asteriods.entities

import io.wasin.asteriods.Game

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

    fun wrap() {
        if (x < 0f) x = Game.V_WIDTH
        if (x > Game.V_WIDTH) x = 0f
        if (y < 0f) y = Game.V_HEIGHT
        if (y > Game.V_HEIGHT) y = 0f
    }
}