package io.wasin.asteriods.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.wasin.asteriods.Game

/**
 * Created by haxpor on 7/9/17.
 */
class Player(maxBullet: Int): SpaceObject() {
    var left: Boolean = false
    var right: Boolean = false
    var up: Boolean = false

    private var maxSpeed: Float = 0.0f
    private var acceleration: Float = 0.0f
    private var deceleration: Float = 0.0f
    private var acceleratingTimer: Float = 0.0f
    private var flamex: Array<Float>
    private var flamey: Array<Float>

    private var maxBullet: Int = maxBullet
    private var bulletsPool: BulletPool = BulletPool(4)
    private var bullets: ArrayList<Bullet> = ArrayList()

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

            acceleratingTimer += dt
            if (acceleratingTimer > 0.1f) {
                acceleratingTimer = 0.0f
            }
        }
        else {
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
        if (bullets.count() > 0) {
            for (i in bullets.count() - 1 downTo 0) {
                val b = bullets[i]
                bullets[i].update(dt)
                if (b.shouldBeRemoved) {
                    bullets.removeAt(i)
                    bulletsPool.free(b)
                }
            }
        }

        // screen wrap
        wrap()
    }

    fun render(sr: ShapeRenderer) {
        sr.color = Color(1f, 1f, 1f, 1f)
        sr.begin(ShapeRenderer.ShapeType.Line)

        // draw ship
        for (i in 0..shapex.size-1) {
            sr.line(shapex[i], shapey[i], shapex[(i+1)%shapex.size], shapey[(i+1)%shapey.size])
        }

        // draw flame
        if (up) {
            for (i in 0..flamex.size - 1) {
                sr.line(flamex[i], flamey[i], flamex[(i + 1) % flamex.size], flamey[(i + 1) % flamey.size])
            }
        }

        // draw bullets
        for (bullet in bullets) {
            bullet.renderBatch(sr)
        }

        sr.end()
    }

    fun shoot() {
        if (bullets.count() >= maxBullet) return

        // obtain object from the pool
        val bullet = bulletsPool.obtain()
        // set position and angle match to what player is of now
        bullet.spawn(x, y, radians)
        // add new spawned bullet to bullets list
        bullets.add(bullet)
    }
}