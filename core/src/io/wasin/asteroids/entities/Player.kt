package io.wasin.asteroids.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.asteroids.Game
import io.wasin.asteroids.compat.Line2D
import io.wasin.asteroids.compat.Point2D

/**
 * Created by haxpor on 7/9/17.
 */
class Player(maxBullet: Int): SpaceObject() {

    companion object {
        const val HIT_TIME: Float = 2.0f
    }

    var left: Boolean = false
    var right: Boolean = false
    var up: Boolean = false
        set(value) {
            // loop playing sound when it's previously not set
            // and now it's set
            if (value && !field) {
                Game.res.getSound("thruster")?.let { it.loop() }
            }
            // stop playing sound when it's previously set
            // and now it's not set
            else if (!value && field) {
                Game.res.getSound("thruster")?.let { it.stop() }
            }

            field = value
        }

    private var maxSpeed: Float = 0.0f
    private var acceleration: Float = 0.0f
    private var deceleration: Float = 0.0f
    private var acceleratingTimer: Float = 0.0f
    private var flamex: Array<Float>
    private var flamey: Array<Float>

    private var maxBullet: Int = maxBullet
    private var bulletsPool: BulletPool = BulletPool(maxBullet)
    var bullets: ArrayList<Bullet> = ArrayList()
        private set

    var isHit: Boolean = false
        private set
    var isDead: Boolean = false
        private set

    private var hitTimer: Float = 0.0f
    private var hitLines: Array<Line2D>? = null
    private var hitLinesVector: Array<Point2D>? = null

    private var oldShapeRendererColor: Color? = null

    var score: Long = 0
        private set

    var extraLives: Int = 3
        private set

    private var requiredScore: Long = 10000

    init {
        x = Game.V_WIDTH / 2
        y = Game.V_HEIGHT / 2

        maxSpeed = 300f
        acceleration = 200f
        deceleration = 10f

        shapex = Array(4, { 0f })
        shapey = Array(4, { 0f })

        flamex = Array(3, { 0f })
        flamey = Array(3, { 0f })

        // rotate its head to upwards direction
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

    private fun setFlame() {
        flamex[0] = x + MathUtils.cos((radians - 5 * Math.PI / 6).toFloat()) * 5f
        flamey[0] = y + MathUtils.sin((radians - 5 * Math.PI / 6).toFloat()) * 5f

        flamex[1] = x + MathUtils.cos((radians - Math.PI).toFloat()) * (6 + acceleratingTimer * 50)
        flamey[1] = y + MathUtils.sin((radians - Math.PI).toFloat()) * (6 + acceleratingTimer * 50)

        flamex[2] = x + MathUtils.cos((radians + 5 * Math.PI / 6).toFloat()) * 5f
        flamey[2] = y + MathUtils.sin((radians + 5 * Math.PI / 6).toFloat()) * 5f
    }


    fun update(dt: Float, viewport: Viewport) {

        // if hit then render hitLines
        if (isHit) {
            hitTimer += dt
            if (hitTimer > HIT_TIME) {
                isDead = true
                hitTimer = 0.0f
            }
            hitLines?.let {
                val _hitLines = it

                _hitLines.forEachIndexed { i, hitLine ->
                    hitLinesVector?.let {
                        hitLine.setLine(
                                _hitLines[i].x1 + it[i].x * 10f * dt,
                                _hitLines[i].y1 + it[i].y * 10f * dt,
                                _hitLines[i].x2 + it[i].x * 10f * dt,
                                _hitLines[i].y2 + it[i].y * 10f * dt)
                    }
                }
            }

            // still update bullets
            for (i in bullets.count() - 1 downTo 0) {
                val b = bullets[i]

                if (b.shouldBeRemoved) {
                    bullets.removeAt(i)
                    bulletsPool.free(b)
                } else {
                    b.update(dt, viewport)
                }
            }
        }
        // otherwise normally render its compartments
        else {
            // check extra lives
            if (score >= requiredScore) {
                extraLives++
                requiredScore += 10000
                Game.res.getSound("extralife")?.let { it.play() }
            }

            // turning
            if (left) {
                radians += rotationSpeed * dt
            } else if (right) {
                radians -= rotationSpeed * dt
            }

            // acceleration
            if (up) {
                dx += MathUtils.cos(radians) * acceleration * dt
                dy += MathUtils.sin(radians) * acceleration * dt

                acceleratingTimer += dt
                if (acceleratingTimer > 0.1f) {
                    acceleratingTimer = 0.0f
                }
            } else {
                acceleratingTimer = 0.0f
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

            // set flame
            if (up) {
                setFlame()
            }

            // get rid of bullet from active list, and add it to the pool for reuse if necessary
            for (i in bullets.count() - 1 downTo 0) {
                val b = bullets[i]

                if (b.shouldBeRemoved) {
                    bullets.removeAt(i)
                    bulletsPool.free(b)
                } else {
                    b.update(dt, viewport)
                }
            }

            // screen wrap
            wrap(viewport)
        }
    }

    /**
     * Provide flexibility in doing batch render
     * Call this method before calling renderBatch()
     */
    fun beginRender(sr: ShapeRenderer) {
        // save old color to set it back later when we're done rendering
        oldShapeRendererColor = sr.color

        sr.color = Color(1f, 1f, 1f, 1f)
        sr.begin(ShapeRenderer.ShapeType.Line)
    }

    /**
     * Provide flexibility in doing batch render
     * Call this method after calling renderBatch()
     */
    fun endRender(sr: ShapeRenderer) {
        sr.end()

        // set old color back to shape renderer
        oldShapeRendererColor?.let { sr.color = it }
    }

    /**
     * Full single unit of render
     */
    fun render(sr: ShapeRenderer) {
        beginRender(sr)
        renderBatch(sr)
        endRender(sr)
    }

    fun renderBatch(sr: ShapeRenderer) {
        // if hit
        if (isHit) {
            hitLines?.let {
                it.forEach { sr.line(it.x1, it.y1, it.x2, it.y2) }
            }
        }
        else {
            // draw ship
            for (i in 0..shapex.size - 1) {
                sr.line(shapex[i], shapey[i], shapex[(i + 1) % shapex.size], shapey[(i + 1) % shapey.size])
            }

            // draw flame
            if (up) {
                for (i in 0..flamex.size - 1) {
                    sr.line(flamex[i], flamey[i], flamex[(i + 1) % flamex.size], flamey[(i + 1) % flamey.size])
                }
            }
        }

        // draw bullets
        for (bullet in bullets) {
            if (!bullet.shouldBeRemoved) {
                bullet.renderBatch(sr)
            }
        }
    }

    fun shoot() {
        if (bullets.count() >= maxBullet) return

        Game.res.getSound("shoot")?.let { it.play() }

        // obtain object from the pool
        val bullet = bulletsPool.obtain()
        // set position and angle match to what player is of now
        bullet.spawn(x, y, radians)
        // add new spawned bullet to bullets list
        bullets.add(bullet)
    }

    fun hit() {
        if (isHit) return

        isHit = true
        dx = 0f
        dy = 0f
        left = false
        right = false
        up = false

        hitLines = Array(shapex.size, {
            i ->
            Line2D(shapex[i], shapey[i], shapex[(i + 1) % shapex.size], shapey[(i + 1) % shapey.size])
        })

        hitLinesVector = arrayOf(
                Point2D(MathUtils.cos(radians - 1.5f), MathUtils.sin(radians - 1.5f)),
                Point2D(MathUtils.cos(radians - 2.8f), MathUtils.sin(radians - 2.8f)),
                Point2D(MathUtils.cos(radians + 2.8f), MathUtils.sin(radians + 2.8f)),
                Point2D(MathUtils.cos(radians + 1.5f), MathUtils.sin(radians + 1.5f))
        )
    }

    fun reset() {
        x = Game.V_WIDTH / 2
        y = Game.V_HEIGHT / 2
        setShape()
        isHit = false
        isDead = false
    }

    fun loseLife() {
        extraLives--
    }

    fun incrementScore(l: Long) {
        score += l
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        setShape()
    }
}