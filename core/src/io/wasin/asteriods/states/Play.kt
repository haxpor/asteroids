package io.wasin.asteriods.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.wasin.asteriods.Game
import io.wasin.asteriods.entities.*
import io.wasin.asteriods.handlers.BBInput
import io.wasin.asteriods.handlers.GameStateManager

/**
 * Created by haxpor on 6/16/17.
 */
class Play(gsm: GameStateManager): GameState(gsm){

    private var sr: ShapeRenderer = ShapeRenderer()
    private var player: Player = Player(4)

    lateinit private var asteriodPool: AsteriodPool
    private var asteriods: ArrayList<Asteriod> = ArrayList()

    private var particlePool: ParticlePool = ParticlePool(20)
    private var particles: ArrayList<Particle> = ArrayList()

    private var level: Int = 1
    private var totalAsteriods: Int = 0
    private var numAsteriodsLeft: Int = 0

    private var font: BitmapFont

    companion object {
        const val SAFE_SPAWN_DIST: Float = 100f
    }

    init {
        // set font
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        val freeTypeFontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        freeTypeFontParams.size = 20
        font = fontGen.generateFont(freeTypeFontParams)

        spawnAsteriods()
    }

    override fun handleInput(dt: Float) {
        // left button is pressed and player is not going left
        if (BBInput.isDown(BBInput.BUTTON_LEFT) && !player.left && !player.isHit) {
            player.left = true
        }
        // left button is pressed and player is not going left
        else if (!BBInput.isDown(BBInput.BUTTON_LEFT) && player.left && !player.isHit) {
            player.left = false
        }

        // right button is pressed and player is not going right
        if (BBInput.isDown(BBInput.BUTTON_RIGHT) && !player.right && !player.isHit) {
            player.right = true
        }
        // right button is pressed and player is not going right
        else if (!BBInput.isDown(BBInput.BUTTON_RIGHT) && player.right && !player.isHit) {
            player.right = false
        }

        // up button is pressed and player is not going up
        if (BBInput.isDown(BBInput.BUTTON_UP) && !player.up && !player.isHit) {
            player.up = true
        }
        // up button is pressed and player is not going up
        else if (!BBInput.isDown(BBInput.BUTTON_UP) && player.up && !player.isHit) {
            player.up = false
        }

        // shoot
        if (BBInput.isPressed(BBInput.BUTTON_SPACE) && !player.isHit) {
            player.shoot()
        }
    }

    override fun update(dt: Float) {
        handleInput(dt)

        // if all asteriods are destroyed then progress to next level
        if (asteriods.count() == 0) {
            level++
            spawnAsteriods()
        }

        player.update(dt)

        // if player is dead then reset
        if (player.isDead) {
            player.reset()
            player.loseLife()
            return
        }

        for (i in asteriods.count() - 1 downTo 0) {
            val a = asteriods[i]

            if (a.shouldBeRemoved) {
                asteriods.removeAt(i)
                asteriodPool.free(a)
            }
            else {
                a.update(dt)
            }
        }

        for (i in particles.count() - 1 downTo 0) {
            val p = particles[i]

            if (p.shouldBeRemoved) {
                particles.removeAt(i)
                particlePool.free(p)
            }
            else {
                p.update(dt)
            }
        }

        checkCollisions()
    }

    override fun render() {
        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // render player
        player.render(sr)

        // batch render for asteriods
        sr.begin(ShapeRenderer.ShapeType.Line)
        for (asteriod in asteriods) {
            if (!asteriod.shouldBeRemoved) {
                asteriod.renderBatch(sr)
            }
        }

        // batch render for particles
        for (particle in particles) {
            if (!particle.shouldBeRemoved) {
                particle.renderBatch(sr)
            }
        }
        sr.end()

        // score
        sb.begin()
        font.draw(sb, player.score.toString(), 40f, camViewport.screenHeight - 30f)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {
    }

    private fun spawnAsteriods() {
        var numToSpawn = level + 3  // aim to spawn only large asteriods
        totalAsteriods = numToSpawn * 7 // as bigger asteriod can split into 2 asteriod, for all hierarchy for its types
        numAsteriodsLeft = totalAsteriods

        // create asteriod pool match total number of asteriods to have in such level
        asteriodPool = AsteriodPool(totalAsteriods)

        var x: Float
        var y: Float

        // spawn large asteriods outside of the safe area
        for (n in 1..numToSpawn) {
            do {
                x = MathUtils.random(Game.V_WIDTH)
                y = MathUtils.random(Game.V_HEIGHT)

                val dx = x - player.x
                val dy = y - player.y
                val dist = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            } while(dist < SAFE_SPAWN_DIST)

            // get free object from the pool
            val a = asteriodPool.obtain()
            // spawn
            a.spawn(x, y, Asteriod.Type.LARGE)
            // also add into our active list of asteriods
            asteriods.add(a)
        }
    }

    private fun checkCollisions() {

        // only check for collision when player is not hit
        if (player.isHit) return

        // player-asteriod collision
        for (i in asteriods.count()-1 downTo 0) {
            val asteriod = asteriods[i]

            if (asteriod.shouldBeRemoved) continue

            if (asteriod.intersects(player)) {
                player.hit()
                asteriod.shouldBeRemoved = true // mark for it to be removed automatically

                splitAsteriod(asteriod)
                break
            }
        }

        // bullet-asteriod collision
        for (i in player.bullets.count()-1 downTo 0) {
            val bullet = player.bullets[i]

            if (bullet.shouldBeRemoved) continue

            for (j in asteriods.count()-1 downTo 0) {
                val asteriod = asteriods[j]

                if (asteriod.shouldBeRemoved) continue

                if (asteriod.contains(bullet.x, bullet.y)) {
                    bullet.shouldBeRemoved = true   // mark for it to be removed automatically
                    asteriod.shouldBeRemoved = true // same

                    // split asteriod
                    splitAsteriod(asteriod)

                    // increment player score
                    player.incrementScore(asteriod.score.toLong())

                    break   // one bullet affects only one asteriod
                }
            }
        }
    }

    private fun splitAsteriod(asteriod: Asteriod) {
        numAsteriodsLeft--
        if (asteriod.type == Asteriod.Type.LARGE) {
            // spawn particles
            createParticles(asteriod.x, asteriod.y, 6)

            // split into 2 medium asteriods
            for (n in 1..2) {
                val newa = asteriodPool.obtain()
                newa.spawn(asteriod.x, asteriod.y, Asteriod.Type.MEDIUM)
                asteriods.add(newa)
                numAsteriodsLeft++
            }
        }
        else if (asteriod.type == Asteriod.Type.MEDIUM) {
            // spawn particles
            createParticles(asteriod.x, asteriod.y, 4)

            // split into 2 small asteriods
            for (n in 1..2) {
                val newa = asteriodPool.obtain()
                newa.spawn(asteriod.x, asteriod.y, Asteriod.Type.SMALL)
                asteriods.add(newa)
                numAsteriodsLeft++
            }
        }
        else if (asteriod.type == Asteriod.Type.SMALL) {
            // spawn particles
            createParticles(asteriod.x, asteriod.y, 2)
        }
    }

    private fun createParticles(x: Float, y: Float, numSpawned: Int) {
        for (n in 1..numSpawned) {
            val particle = particlePool.obtain()
            particle.spawn(x, y)
            particles.add(particle)
        }
    }
}