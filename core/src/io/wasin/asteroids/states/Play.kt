package io.wasin.asteroids.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import io.wasin.asteroids.Game
import io.wasin.asteroids.entities.*
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

    private var asteroidPool: AsteroidPool
    private var asteroids: ArrayList<Asteroid> = ArrayList()

    private var particlePool: ParticlePool = ParticlePool(20)
    private var particles: ArrayList<Particle> = ArrayList()

    private var flyingSaucerPool: FlyingSaucerPool
    private var flyingSaucers: ArrayList<FlyingSaucer> = ArrayList()
    private var flyingSaucerSpawnTimer: Float = 0f
    private var flyingSaucerSpawnTime: Float = FLYING_SAUCER_SPAWN_COOLDOWN

    private var level: Int = 1
    private var totalAsteroids: Int = 0
    private var numAsteroidsLeft: Int = 0

    private var font: BitmapFont
    private var bgMusic: SimulatedBgMusic = SimulatedBgMusic()

    companion object {
        const val SAFE_SPAWN_DIST: Float = 100f
        const val FLYING_SAUCER_SPAWN_COOLDOWN: Float = 15f
    }

    init {
        // set font
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        val freeTypeFontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        freeTypeFontParams.size = 20
        font = fontGen.generateFont(freeTypeFontParams)
        // dispose font generator
        fontGen.dispose()

        // create pool for asteroids
        // 70 is pre-determined number of asteriod created in the pool, if player progressed further
        // than N asteroids, it will dynamically create
        // this number is pre-determined that user might die before level 10 (mostly)
        asteroidPool = AsteroidPool(70)
        // create a pool for flying saucer
        // pre-determined to be only 1 at a time
        flyingSaucerPool = FlyingSaucerPool(1, player, camViewport)

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

            // mark to remove of flying saucers in next frame
            flyingSaucers.forEach { it.shouldBeRemoved = true }
            // reset spawn timer
            flyingSaucerSpawnTimer = 0f

            // if player has no more extra lives
            // then go to mainmenu
            if (player.extraLives < 0) {
                // directly stop playing effect sound of flyingsaucer as there's no chance in next frame
                Game.res.getSound("smallsaucer")?.stop()
                Game.res.getSound("largesaucer")?.stop()

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

        // if there's some flying saucers
        if (flyingSaucers.count() > 0) {
            for (i in flyingSaucers.count() - 1 downTo 0) {
                val f = flyingSaucers[i]

                if (f.shouldBeRemoved) {
                    flyingSaucers.removeAt(i)
                    flyingSaucerPool.free(f)
                } else {
                    f.update(dt)
                }
            }
        }
        // if there's no flying saucer
        else {
            // update flying saucer's spawn timer
            flyingSaucerSpawnTimer += dt
            if (flyingSaucerSpawnTimer > flyingSaucerSpawnTime) {
                flyingSaucerSpawnTimer -= flyingSaucerSpawnTime

                // random type to spawn
                val fType = if (MathUtils.randomBoolean()) FlyingSaucer.Type.SMALL else FlyingSaucer.Type.LARGE
                // random direction to spawn
                val fDir = if (MathUtils.randomBoolean()) FlyingSaucer.Direction.RIGHT else FlyingSaucer.Direction.LEFT
                // obtain from the pool and spawn
                val f = flyingSaucerPool.obtain()
                f.spawn(fType, fDir)
                // add to active list
                flyingSaucers.add(f)
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
        Asteroid.beginBatchRender(sr)
        asteroids.forEach {
            if (!it.shouldBeRemoved) { it.renderBatch(sr) }
        }
        Asteroid.endBatchRender(sr)

        // batch render for particles
        Particle.beginBatchRender(sr)
        particles.forEach {
            if (!it.shouldBeRemoved) { it.renderBatch(sr) }
        }
        Particle.endBatchRender(sr)

        // flying saucers
        FlyingSaucer.beginRender(sr)
        flyingSaucers.forEach { it.renderBatch(sr) }
        FlyingSaucer.endRender(sr)

        // UI
        sb.projectionMatrix = hudCam.combined
        sr.projectionMatrix = hudCam.combined
        hudViewport.apply(true)

        // lives
        Player.beginBatchRender(sr)
        for (i in 0..player.extraLives-1) {
            hudPlayer.setPosition(46f +i*14f, hudCam.viewportHeight - 70f)
            hudPlayer.renderBatch(sr)
        }
        Player.endBatchRender(sr)

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

    private fun spawnFlyingSaucers() {
        // obtain a free object from the pool
        val f = flyingSaucerPool.obtain()
        f.spawn(FlyingSaucer.Type.LARGE, FlyingSaucer.Direction.RIGHT)
        // add to the active list
        flyingSaucers.add(f)
    }

    private fun checkCollisions() {

        // only check for collision when player is not hit
        if (player.isHit) return

        // player-asteroid collision
        for (i in 0..asteroids.count()-1) {
            val asteroid = asteroids[i]
            if (asteroid.shouldBeRemoved) continue        // skip if it's marked to be removed

            if (asteroid.intersects(player)) {
                player.hit()
                asteroid.shouldBeRemoved = true // mark for it to be removed automatically

                splitAsteroid(asteroid)

                Game.res.getSound("explode")?.play()
                break
            }
        }

        // bullet-asteroid collision
        for (i in 0..player.bullets.count()-1) {
            val bullet = player.bullets[i]

            if (bullet.shouldBeRemoved) continue    // skip if marked to be removed

            for (j in 0..asteroids.count()-1) {
                val asteroid = asteroids[j]

                if (asteroid.shouldBeRemoved) continue      // skip if marked to be removed

                if (asteroid.contains(bullet.x, bullet.y)) {
                    bullet.shouldBeRemoved = true   // mark for it to be removed automatically
                    asteroid.shouldBeRemoved = true // same

                    // split asteroid
                    splitAsteroid(asteroid)

                    // increment player score
                    player.incrementScore(asteroid.score.toLong())

                    Game.res.getSound("explode")?.play()

                    break   // one bullet affects only one asteroid
                }
            }
        }

        // player-flyingsaucer collision
        flyingSaucers.forEach {
            it.takeUnless { it.shouldBeRemoved || it.isHit }?.also { f ->
                if (it.intersects(player)) {
                    player.hit()
                    createParticles(player.x, player.y, 3)
                    createParticles(f.x, f.y, 3)
                    // flying saucer takes hit, and marked for removal
                    // no need to mark as remove, hit() will handle it
                    f.hit()
                    Game.res.getSound("explode")?.play()
                    return@forEach
                }
            }
        }

        // player-flyingsaucer's bullets collision
        flyingSaucers.forEach {
            // even flying saucer is hit but if it's not removed from active list yet
            // then its bullets are still in effect
            it.bullets.forEach inner@ {
                it.takeUnless { it.shouldBeRemoved }?.also { bullet ->
                    if (player.contains(bullet.x, bullet.y)) {
                        // mark removal for bullet
                        bullet.shouldBeRemoved = true
                        // player takes hit
                        player.hit()
                        // create particle
                        createParticles(player.x, player.y, 3)
                        // play sound effect
                        Game.res.getSound("explode")?.play()
                        return@inner
                    }
                }
            }
        }

        // player's bullets-flyingsaucer
        player.bullets.forEach {
            it.takeUnless { it.shouldBeRemoved }?.also { bullet ->
                flyingSaucers.forEach inner@ {
                    it.takeUnless { it.shouldBeRemoved || it.isHit }?.also { f ->
                        if (f.contains(bullet.x, bullet.y)) {
                            // mark removal for bullet
                            bullet.shouldBeRemoved = true
                            // player gains point
                            player.incrementScore(f.score)
                            // flying saucer takes hit, and marked for removal
                            // no need to mark as remove, hit() will handle it
                            f.hit()
                            // create particle depends on type
                            createParticles(f.x, f.y, if (f.type == FlyingSaucer.Type.LARGE) 6 else 3)
                            // play sound effect
                            Game.res.getSound("explode")?.play()
                            return@inner
                        }
                    }
                }
            }
        }

        // flying saucers - asteroids
        for (i in 0..flyingSaucers.count()-1) {
            val fs = flyingSaucers[i]
            if (fs.shouldBeRemoved || fs.isHit) continue

            for (j in 0..asteroids.count()-1) {
                val asteroid = asteroids[j]
                if (asteroid.shouldBeRemoved) continue

                if (fs.intersects(asteroid)) {
                    // mark removal for asteroid
                    asteroid.shouldBeRemoved = true
                    splitAsteroid(asteroid)

                    // create particle for flying saucers
                    createParticles(fs.x, fs.y, if (fs.type == FlyingSaucer.Type.LARGE) 6 else 3)
                    // make flying saucer hit
                    fs.hit()

                    Game.res.getSound("explode")?.play()
                    break
                }
            }
        }

        // flying saucers' bullets - asteroids
        for (i in 0..flyingSaucers.count()-1) {
            val fs = flyingSaucers[i]
            val fBullets = fs.bullets

            for (j in 0..fBullets.count()-1) {
                val bullet = fBullets[j]
                if (bullet.shouldBeRemoved) continue

                for (k in 0..asteroids.count()-1) {
                    val asteroid = asteroids[k]
                    if (asteroid.shouldBeRemoved) continue

                    if (asteroid.contains(bullet.x, bullet.y)) {
                        // mark removal for asteroid
                        asteroid.shouldBeRemoved = true
                        splitAsteroid(asteroid)

                        // mark bullet for removal
                        bullet.shouldBeRemoved = true

                        // play sound effect
                        Game.res.getSound("explode")?.play()
                        break
                    }
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