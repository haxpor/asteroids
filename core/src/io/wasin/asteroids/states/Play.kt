package io.wasin.asteroids.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.wasin.asteroids.Game
import io.wasin.asteroids.entities.Asteroid
import io.wasin.asteroids.entities.AsteroidPool
import io.wasin.asteroids.entities.Particle
import io.wasin.asteroids.entities.ParticlePool
import io.wasin.asteroids.entities.Player
import io.wasin.asteroids.handlers.BBInput
import io.wasin.asteroids.handlers.GameStateManager
import io.wasin.asteroids.handlers.SimulatedBgMusic

/**
 * Created by haxpor on 6/16/17.
 */
class Play(gsm: GameStateManager): GameState(gsm){

    private var sr: ShapeRenderer = ShapeRenderer()
    private var player: Player = Player(4)
    private var hudPlayer: Player = Player(0)

    lateinit private var asteroidPool: AsteroidPool
    private var asteroids: ArrayList<Asteroid> = ArrayList()

    private var particlePool: ParticlePool = ParticlePool(20)
    private var particles: ArrayList<Particle> = ArrayList()

    private var level: Int = 1
    private var totalAsteroids: Int = 0
    private var numAsteroidsLeft: Int = 0

    private var font: BitmapFont
    private var bgMusic: SimulatedBgMusic = SimulatedBgMusic()

    companion object {
        const val SAFE_SPAWN_DIST: Float = 100f
    }

    init {
        // set font
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        val freeTypeFontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        freeTypeFontParams.size = 20
        font = fontGen.generateFont(freeTypeFontParams)
        // dispose font generator
        fontGen.dispose()

        spawnAsteroids()
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

        // if all asteroids are destroyed then progress to next level
        if (asteroids.count() == 0) {
            level++
            spawnAsteroids()
        }

        player.update(dt, camViewport)

        // if player is dead then reset
        if (player.isDead) {
            player.reset()
            player.loseLife()
            bgMusic.reset()

            // if player has no more extra lives
            // then go to mainmenu
            if (player.extraLives < 0) {
                gsm.setState(GameOver(player.score, gsm))
            }

            return
        }

        for (i in asteroids.count() - 1 downTo 0) {
            val a = asteroids[i]

            if (a.shouldBeRemoved) {
                asteroids.removeAt(i)
                asteroidPool.free(a)
            }
            else {
                a.update(dt, camViewport)
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

        // update bg music; simulated
        bgMusic.update(dt, !player.isHit)
    }

    override fun render() {
        // GAME CONTENT
        sb.projectionMatrix = cam.combined
        sr.projectionMatrix = cam.combined
        camViewport.apply(true)

        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // render player
        player.render(sr)

        // batch render for asteroids
        sr.begin(ShapeRenderer.ShapeType.Line)
        for (asteroid in asteroids) {
            if (!asteroid.shouldBeRemoved) {
                asteroid.renderBatch(sr)
            }
        }

        // batch render for particles
        for (particle in particles) {
            if (!particle.shouldBeRemoved) {
                particle.renderBatch(sr)
            }
        }
        sr.end()

        // UI
        sb.projectionMatrix = hudCam.combined
        sr.projectionMatrix = hudCam.combined
        hudViewport.apply(true)

        // lives
        hudPlayer.beginRender(sr)
        for (i in 0..player.extraLives-1) {
            hudPlayer.setPosition(46f +i*14f, hudCam.viewportHeight - 70f)
            hudPlayer.renderBatch(sr)
        }
        hudPlayer.endRender(sr)

        // score
        sb.begin()
        font.draw(sb, player.score.toString(), 40f, hudCam.viewportHeight - 30f)
        sb.end()
    }

    override fun dispose() {
        sr.dispose()
        font.dispose()
    }

    override fun resize_user(width: Int, height: Int) {
    }

    private fun spawnAsteroids() {
        var numToSpawn = level + 3  // aim to spawn only large asteroids
        totalAsteroids = numToSpawn * 7 // as bigger asteroid can split into 2 asteroid, for all hierarchy for its types
        numAsteroidsLeft = totalAsteroids

        bgMusic.updateCurrentDelay(numAsteroidsLeft, totalAsteroids)

        // create asteroid pool match total number of asteroids to have in such level
        asteroidPool = AsteroidPool(totalAsteroids)

        var x: Float
        var y: Float

        // spawn large asteroids outside of the safe area
        for (n in 1..numToSpawn) {
            do {
                x = MathUtils.random(Game.V_WIDTH)
                y = MathUtils.random(Game.V_HEIGHT)

                val dx = x - player.x
                val dy = y - player.y
                val dist = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            } while(dist < SAFE_SPAWN_DIST)

            // get free object from the pool
            val a = asteroidPool.obtain()
            // spawn
            a.spawn(x, y, Asteroid.Type.LARGE)
            // also add into our active list of asteroids
            asteroids.add(a)
        }
    }

    private fun checkCollisions() {

        // only check for collision when player is not hit
        if (player.isHit) return

        // player-asteroid collision
        for (i in asteroids.count()-1 downTo 0) {
            val asteroid = asteroids[i]

            if (asteroid.shouldBeRemoved) continue

            if (asteroid.intersects(player)) {
                player.hit()
                asteroid.shouldBeRemoved = true // mark for it to be removed automatically

                splitAsteroid(asteroid)

                Game.res.getSound("explode")?.let { it.play() }
                break
            }
        }

        // bullet-asteroid collision
        for (i in player.bullets.count()-1 downTo 0) {
            val bullet = player.bullets[i]

            if (bullet.shouldBeRemoved) continue

            for (j in asteroids.count()-1 downTo 0) {
                val asteroid = asteroids[j]

                if (asteroid.shouldBeRemoved) continue

                if (asteroid.contains(bullet.x, bullet.y)) {
                    bullet.shouldBeRemoved = true   // mark for it to be removed automatically
                    asteroid.shouldBeRemoved = true // same

                    // split asteroid
                    splitAsteroid(asteroid)

                    // increment player score
                    player.incrementScore(asteroid.score.toLong())

                    Game.res.getSound("explode")?.let { it.play() }

                    break   // one bullet affects only one asteroid
                }
            }
        }
    }

    private fun splitAsteroid(asteroid: Asteroid) {
        numAsteroidsLeft--
        bgMusic.updateCurrentDelay(numAsteroidsLeft, totalAsteroids)

        if (asteroid.type == Asteroid.Type.LARGE) {
            // spawn particles
            createParticles(asteroid.x, asteroid.y, 6)

            // split into 2 medium asteroids
            for (n in 1..2) {
                val newa = asteroidPool.obtain()
                newa.spawn(asteroid.x, asteroid.y, Asteroid.Type.MEDIUM)
                asteroids.add(newa)

            }
        }
        else if (asteroid.type == Asteroid.Type.MEDIUM) {
            // spawn particles
            createParticles(asteroid.x, asteroid.y, 4)

            // split into 2 small asteroids
            for (n in 1..2) {
                val newa = asteroidPool.obtain()
                newa.spawn(asteroid.x, asteroid.y, Asteroid.Type.SMALL)
                asteroids.add(newa)
            }
        }
        else if (asteroid.type == Asteroid.Type.SMALL) {
            // spawn particles
            createParticles(asteroid.x, asteroid.y, 2)
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