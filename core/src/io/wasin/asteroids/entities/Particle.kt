package io.wasin.asteroids.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool
import io.wasin.asteroids.interfaces.IBatchShapeRenderable
import io.wasin.asteroids.interfaces.IBatchWrapperShapeRenderable

/**
 * Created by haxpor on 7/10/17.
 */
class Particle(x: Float, y: Float): SpaceObject(), Pool.Poolable, IBatchShapeRenderable {
    private var timer: Float = 0.0f
    private var time: Float = 1.0f
    var shouldBeRemoved: Boolean = false
        private set

    // static context
    companion object: IBatchWrapperShapeRenderable {
        private var oldShapeRendererColor: Color? = null

        override fun beginBatchRender(sr: ShapeRenderer) {
            // save old color of renderer, we will set it back later
            oldShapeRendererColor = sr.color
            sr.color = Color.WHITE
            sr.begin(ShapeRenderer.ShapeType.Line)
        }

        override fun endBatchRender(sr: ShapeRenderer) {
            sr.end()

            // set old color back to renderer
            oldShapeRendererColor?.let { sr.color = it }
        }
    }

    constructor(): this(0f, 0f)

    init {
        init(x, y)
    }

    private fun init(x: Float, y: Float) {
        this.x = x
        this.y = y
        width = 2
        height = 2

        speed = MathUtils.random(45f, 70f)
        radians = MathUtils.random((2f * Math.PI).toFloat())
        dx = MathUtils.cos(radians) * speed
        dy = MathUtils.sin(radians) * speed
        timer = 0.0f
        time = MathUtils.random(1.0f, 1.5f)
    }

    fun spawn(x: Float, y: Float) {
        init(x, y)
    }

    fun update(dt: Float) {
        x += dx * dt
        y += dy * dt

        timer += dt
        if (timer > time) {
            shouldBeRemoved = true
        }
    }

    fun render(sr: ShapeRenderer) {
        beginBatchRender(sr)
        renderBatch(sr)
        endBatchRender(sr)
    }

    override fun renderBatch(sr: ShapeRenderer) {
        sr.circle(x - width / 2f, y - height / 2f, width / 2f)
    }

    override fun reset() {
        shouldBeRemoved = false
    }
}