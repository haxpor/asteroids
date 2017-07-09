package io.wasin.asteriods.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.wasin.asteriods.Game

/**
 * Created by haxpor on 7/9/17.
 */
class Player: SpaceObject() {
    var left: Boolean = false
    var right: Boolean = false
    var up: Boolean = false

    private var maxSpeed: Float = 0.0f
    private var acceleration: Float = 0.0f
    private var deceleration: Float = 0.0f

    init {
        x = Game.V_WIDTH / 2
        y = Game.V_HEIGHT / 2

        maxSpeed = 300f
        acceleration = 200f
        deceleration = 10f

        shapex = Array(4, { 0f })
        shapey = Array(4, { 0f })

        radians = (Math.PI / 2f).toFloat()
        rotationSpeed = 3f
    }

    private fun setShape() {
        shapex[0] = x + MathUtils.cos(radians) * 8
        shapey[0] = y + MathUtils.sin(radians) * 8

        shapex[1] = x + MathUtils.cos((radians - 4 * Math.PI / 5f).toFloat()) * 8f
        shapey[1] = y + MathUtils.sin((radians - 4 * Math.PI / 5f).toFloat()) * 8f

        shapex[2] = x + MathUtils.cos((radians + Math.PI).toFloat()) * 5f
        shapey[2] = y + MathUtils.sin((radians + Math.PI).toFloat()) * 5f

        shapex[3] = x + MathUtils.cos((radians + 4 * Math.PI / 5f).toFloat()) * 8f
        shapey[3] = y + MathUtils.sin((radians + 4 * Math.PI / 5f).toFloat()) * 8f
    }

    fun update(dt: Float) {
        // turning
        if (left) {
            radians += rotationSpeed * dt
        }
        else if (right) {
            radians -= rotationSpeed * dt
        }

        // acceleration
        if (up) {
            dx += MathUtils.cos(radians) * acceleration * dt
            dy += MathUtils.sin(radians) * acceleration * dt
        }

        // deacceleration
        var vec = Math.sqrt((dx * dx + dy * dy).toDouble())
        if (vec > 0) {
            dx -= ((dx / vec) * deceleration * dt).toFloat()
            dy -= ((dy / vec) * deceleration * dt).toFloat()
        }
        if (vec > maxSpeed) {
            dx = ((dx / vec) * maxSpeed).toFloat()
            dy = ((dy / vec) * maxSpeed).toFloat()
        }

        // set position
        x += dx * dt
        y += dy * dt

        // set shape
        setShape()

        // screen wrap
        wrap()
    }

    fun draw(sr: ShapeRenderer) {
        sr.color = Color(1f, 1f, 1f, 1f)
        sr.begin(ShapeRenderer.ShapeType.Line)

        for (i in 0..shapex.size-1) {
            sr.line(shapex[i], shapey[i], shapex[(i+1)%shapex.size], shapey[(i+1)%shapey.size])
        }

        sr.end()
    }
}