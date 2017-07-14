package io.wasin.asteroids.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.asteroids.Game
import io.wasin.asteroids.compat.Line2D
import io.wasin.asteroids.compat.Point2D

/**
 * Created by haxpor on 7/14/17.
 */
class FlyingSaucer(player: Player, type: Type, dir: Direction, viewport: Viewport): SpaceObject(), Pool.Poolable {

    /**
     * Type of FlyingSaucer
     */
    enum class Type {
        LARGE,
        SMALL
    }

    /**
     * Direction of movement
     */
    enum class Direction {
        LEFT,
        RIGHT
    }

    // static context
    companion object {
        const val HIT_TIME: Float = 2.0f
        const val NUM_POINT: Int = 6

        private var oldShapeRendererColor: Color? = null

        fun beginRender(sr: ShapeRenderer) {
            // save old color of renderer, we will set it back later
            oldShapeRendererColor = sr.color
            sr.color = Color.WHITE
            sr.begin(ShapeRenderer.ShapeType.Line)
        }

        fun endRender(sr: ShapeRenderer) {
            sr.end()

            // set old color back to renderer
            oldShapeRendererColor?.let { sr.color = it }
        }
    }

    private var maxBullet: Int = 1
    private var bulletsPool: BulletPool = BulletPool(maxBullet)
    var bullets: ArrayList<Bullet> = ArrayList()
        private set

    var type: Type = type
        private set
    var score: Long = 0
        private set

    private var fireTimer: Float = 0f
    private var fireTime: Float = 0f
    private var player: Player = player

    /** movement mechanic: move along the horizontal path for duration of pathTime1,
     *  then move in tangent line downward for duration of pathTime2-pathTime1,
     *  then finally move along the horizontal path for the less of time until out
     *  of screen
     */
    private var pathTimer: Float = 0f
    private var pathTime1: Float = 0f
    private var pathTime2: Float = 0f

    private var viewport: Viewport = viewport

    /**
     * Hit animation-line sequence
     */
    var isHit: Boolean = false
        private set
    private var hitTimer: Float = 0.0f
    // +2 as mid-line we would like to split it when explodes into 2 lines
    // the following thows declarations are for caching and better performance not to calculate position
    // every time FlyingSaucer is destroyed
    // note: as FlyingSaucer might not get destroyed after it spawned, thus suitable to use lazy in this case
    private val hitLines: Array<Line2D> by lazyOf(Array(NUM_POINT+2, { Line2D(-1f, -1f, -1f, -1f) }))
    private val hitLinesVector: Array<Point2D> by lazy {
        arrayOf(
                Point2D(MathUtils.cos(MathUtils.degRad * -45f*3f), MathUtils.sin(MathUtils.degRad * -45f*3f)),
                Point2D(MathUtils.cos(MathUtils.degRad * -45f*2f), MathUtils.sin(MathUtils.degRad * -45f*2f)),
                Point2D(MathUtils.cos(MathUtils.degRad * -45f), MathUtils.sin(MathUtils.degRad * -45f)),
                Point2D(MathUtils.cos(MathUtils.degRad * 45f), MathUtils.sin(MathUtils.degRad * 45f)),
                Point2D(MathUtils.cos(MathUtils.degRad * 45f*2f), MathUtils.sin(MathUtils.degRad * 45f*2f)),
                Point2D(MathUtils.cos(MathUtils.degRad * 45f*3f), MathUtils.sin(MathUtils.degRad * 45f*3f)),
                Point2D(-1f, -1f),      // subject to change dynamically
                Point2D(-1f, -1f)       // subject to change dynamically
        )
    }

    var direction: Direction = dir
        private set
    var shouldBeRemoved: Boolean = false

    constructor(player: Player, viewport: Viewport): this(player, Type.LARGE, Direction.RIGHT, viewport)

    init {
        init(type, dir)
    }

    private fun init(type: Type, dir: Direction) {
        speed = 70f
        this.type = type
        direction = dir

        if (direction == Direction.LEFT) {
            dx = -speed
            x = viewport.camera.viewportWidth   // or just viewport.worldWidth
        }
        else if (direction == Direction.RIGHT) {
            dx = speed
            x = 0f
        }
        y = MathUtils.random(viewport.camera.viewportHeight)

        if (type == Type.LARGE) {
            score = 200
        }
        else {
            score = 1000
        }

        fireTimer = 0f
        fireTime = 1f

        pathTimer = 0f
        pathTime1 = 2f
        pathTime2 = pathTime1  + 2f

        shapex = Array(NUM_POINT, { 0f })
        shapey = Array(NUM_POINT, { 0f })
    }

    fun spawn(type: Type, dir: Direction) {
        init(type, dir)

        // play spawn sound effect
        if (type == Type.LARGE) {
            Game.res.getSound("largesaucer")?.loop(0.2f)
        }
        else if (type == Type.SMALL) {
            Game.res.getSound("smallsaucer")?.loop(0.2f)
        }
    }

    private fun setShape() {
        // the following is counter-clockwise declaration of points
        if (type == Type.LARGE) {
            shapex[0] = x - 10
            shapey[0] = y

            shapex[1] = x - 3
            shapey[1] = y - 5

            shapex[2] = x + 3
            shapey[2] = y - 5

            shapex[3] = x + 10
            shapey[3] = y

            shapex[4] = x + 3
            shapey[4] = y + 5

            shapex[5] = x - 3
            shapey[5] = y + 5
        }
        else if (type == Type.SMALL) {
            shapex[0] = x - 6
            shapey[0] = y

            shapex[1] = x - 2
            shapey[1] = y - 3

            shapex[2] = x + 2
            shapey[2] = y - 3

            shapex[3] = x + 6
            shapey[3] = y

            shapex[4] = x + 2
            shapey[4] = y + 3

            shapex[5] = x - 2
            shapey[5] = y + 3
        }
    }

    fun update(dt: Float) {
        if (isHit) {
            hitTimer += dt
            if (hitTimer > HIT_TIME) {
                shouldBeRemoved = true
                hitTimer = 0.0f
            }
            hitLines?.let {
                val _hitLines = it
                _hitLines.forEachIndexed { i, hitLine ->
                    hitLinesVector?.let {
                        // also random speed of exploding line
                        hitLine.setLine(
                                _hitLines[i].x1 + it[i].x * MathUtils.random(-10.0f, 30.0f) * dt,
                                _hitLines[i].y1 + it[i].y * MathUtils.random(-10.0f, 30.0f) * dt,
                                _hitLines[i].x2 + it[i].x * MathUtils.random(-10.0f, 30.0f) * dt,
                                _hitLines[i].y2 + it[i].y * MathUtils.random(-10.0f, 30.0f) * dt
                        )
                    }
                }
            }

            // update bullets
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
        else {
            // fire
            if (!player.isHit) {
                fireTimer += dt

                // if time to shoot, and it is inside the screen
                if (fireTimer > fireTime &&
                        x > 0f && x < viewport.camera.viewportWidth &&
                        y > 0f && y < viewport.camera.viewportHeight) {

                    fireTimer -= fireTime
                    shoot()
                }
            }

            // move along path
            pathTimer += dt
            // move forward
            if (pathTimer < pathTime1) {
                dy = 0f
            }

            // move downward
            if (pathTimer > pathTime1 && pathTimer < pathTime2) {
                dy = -speed
            }

            // move to end of screen
            if (pathTimer > pathTime1 + pathTime2) {
                dy = 0f
            }

            x += dx * dt
            y += dy * dt

            // screen wrap only for y-direction
            if (y < 0f) y = viewport.camera.viewportHeight

            // set shape
            setShape()

            // check if it needs to be removed
            // and all of its bullets are recycled
            // that means if flying saucer is out of screen, or destroyed but it shot before that,
            // bullet can still does damage on player
            if (((direction == Direction.RIGHT && x > viewport.camera.viewportWidth) ||
                    (direction == Direction.LEFT && x < 0f)) &&
                    bullets.count() == 0) {
                shouldBeRemoved = true
            }

            // update bullets
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
    }

    fun render(sr: ShapeRenderer) {
        beginRender(sr)
        renderBatch(sr)
        endRender(sr)
    }

    fun renderBatch(sr: ShapeRenderer) {
        // draw exploding structure, or normal depend on isHit flag
        if (isHit) {
            hitLines?.let {
                it.forEach { sr.line(it.x1, it.y1, it.x2, it.y2) }
            }
        }
        else {
            // render main structure
            for (i in 0..shapex.size - 1) {
                sr.line(shapex[i], shapey[i], shapex[(i + 1) % shapex.size], shapey[(i + 1) % shapey.size])
            }
            // render line across the structure
            sr.line(shapex[0], shapey[0], shapex[3], shapey[3])
        }

        // draw bullets
        bullets.filter { !it.shouldBeRemoved }.map { it.renderBatch(sr) }
    }

    fun hit() {
        // only one hit
        if (isHit) return

        // mark as hit
        isHit = true

        // create hit lines, line extract itself from the original line structure
        // note: initialize also take into account of core mid-line of its structure which reuses its points
        hitLines.forEachIndexed { i, it ->
            // for outer lines
            if (i < shapex.size) {
                it.setLine(shapex[i], shapey[i], shapex[(i + 1) % shapex.size], shapey[(i + 1) % shapey.size])
            }
            // for mid-line, tessellate into 2 sub-line
            // - 1st sub-line
            else if (i == shapex.size){
                it.setLine(shapex[0], shapey[0], (shapex[0] + shapex[3])/2f, shapey[3])
            }
            // - 2nd sub-line
            else {
                it.setLine((shapex[0] + shapex[3])/2f, shapey[3], shapex[3], shapey[3])
            }
        }

        // random direction to move for mid-line (2 sub-lines) in any direction
        val radiansForMiddleLine = MathUtils.random(MathUtils.PI2)
        hitLinesVector[hitLinesVector.size-2].set(MathUtils.cos(radiansForMiddleLine), MathUtils.sin(radiansForMiddleLine))
        hitLinesVector[hitLinesVector.size-1].set(MathUtils.cos(radiansForMiddleLine), MathUtils.sin(radiansForMiddleLine))
    }

    fun shoot() {
        if (type == Type.LARGE) {
            // shoot in random direction
            radians = MathUtils.random(MathUtils.PI2)
        }
        else if (type == Type.SMALL) {
            // try to shoot to last known position of player
            radians = MathUtils.atan2(player.y - y, player.x - x)
        }
        // get bullet to shoot
        val bullet = bulletsPool.obtain()
        bullet.spawn(x, y, radians)
        // add bullet to active list
        bullets.add(bullet)
        // play sfx
        Game.res.getSound("saucershoot")?.play()
    }

    override fun reset() {
        // stop sound
        if (type == Type.SMALL) {
            Game.res.getSound("smallsaucer")?.stop()
        }
        else if (type == Type.LARGE) {
            Game.res.getSound("largesaucer")?.stop()
        }

        type = Type.LARGE
        direction = Direction.LEFT
        maxBullet = 1
        // recycle all remaining bullets
        bullets.forEach { bulletsPool.free(it) }
        fireTimer = 0f
        pathTimer = 0f
        isHit = false
        hitTimer = 0f
        shouldBeRemoved = false
    }
}