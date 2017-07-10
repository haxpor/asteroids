package io.wasin.asteriods.entities

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool

/**
 * Created by haxpor on 7/10/17.
 */
class Asteriod(x: Float, y: Float, type: Type): SpaceObject(), Pool.Poolable {
    enum class Type {
        SMALL,
        MEDIUM,
        LARGE
    }

    companion object {
        const val MAX_NUMPOINTS: Int = 12
    }

    var type: Type = type
        private set

    private var numPoints: Int = 0
    lateinit private var dists: Array<Float>
    var shouldBeRemoved: Boolean = false

    constructor(): this(0f, 0f, Type.SMALL) {}

    init {
        init(x, y, type)
    }

    fun init(x: Float, y: Float, type: Type) {
        this.x = x
        this.y = y
        this.type = type

        when(type) {
            Type.SMALL -> {
                numPoints = 8
                width = 12
                height = 12
                speed = MathUtils.random(70f, 100f)
            }
            Type.MEDIUM -> {
                numPoints = 10
                width = 20
                height = 20
                speed = MathUtils.random(50f, 60f)
            }
            Type.LARGE -> {
                numPoints = MAX_NUMPOINTS
                width = 40
                height = 40
                speed = MathUtils.random(20f, 30f)
            }
        }

        rotationSpeed = MathUtils.random(-1f, 1f)
        radians = MathUtils.random((2 * Math.PI).toFloat())
        dx = MathUtils.cos(radians) * speed
        dy = MathUtils.sin(radians) * speed

        shapex = Array(numPoints, { 0f })
        shapey = Array(numPoints, { 0f })
        dists = Array(numPoints, { 0f })

        val radius = width / 2f
        for (i in 0..numPoints-1) {
            dists[i] = MathUtils.random(radius / 2f, radius)
        }
    }

    private fun setShape() {
        var angle = 0f
        for (i in 0..numPoints-1) {
            shapex[i] = x + MathUtils.cos(angle + radians) * dists[i]
            shapey[i] = y + MathUtils.sin(angle + radians) * dists[i]

            angle += (2 * Math.PI / numPoints).toFloat()
        }
    }

    fun spawn(x: Float, y: Float, type: Type) {
        init(x, y, type)
    }

    override fun reset() {
        x = 0f
        y = 0f
        type = Type.SMALL
        numPoints = 0
        width = 0
        height = 0
        speed = 0f
        rotationSpeed = 0f
        radians = 0f
        dx = 0f
        dy = 0f
        shouldBeRemoved = false
    }

    fun update(dt: Float) {
        x += dx * dt
        y += dy * dt

        radians += rotationSpeed * dt
        setShape()
        wrap()
    }

    fun render(sr: ShapeRenderer) {
        sr.begin(ShapeRenderer.ShapeType.Line)
        for (i in 0..numPoints-1) {
            sr.line(shapex[i], shapey[i], shapex[(i+1)%numPoints], shapey[(i+1)%numPoints])
        }
        sr.end()
    }

    fun renderBatch(sr: ShapeRenderer) {
        for (i in 0..numPoints-1) {
            sr.line(shapex[i], shapey[i], shapex[(i+1)%numPoints], shapey[(i+1)%numPoints])
        }
    }
}