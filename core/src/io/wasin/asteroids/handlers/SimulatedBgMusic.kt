package io.wasin.asteroids.handlers

import com.badlogic.gdx.audio.Sound
import io.wasin.asteroids.Game

/**
 * Created by haxpor on 7/12/17.
 *
 * This class is used to simulate playing bg music alternates between high or low pulse sound effect
 * periodically in consecutive of each delay time.
 */
class SimulatedBgMusic {

    private val minDelay: Float = 0.25f
    private val maxDelay: Float = 1f
    private var currentDelay: Float = 0f
    private var bgTimer: Float = 0f
    private var playLowPulse: Boolean = true

    private val lowPulse: Sound? = Game.res.getSound("pulselow")
    private val highPulse: Sound? = Game.res.getSound("pulsehigh")

    init {
        init()
    }

    private fun init() {
        currentDelay = maxDelay
        bgTimer = maxDelay
        playLowPulse = true
    }

    fun reset() {
        bgTimer = 0f
        playLowPulse = true
        currentDelay = maxDelay
    }

    fun updateCurrentDelay(currentLeftAsteroids: Int, totalAsteroids: Int) {
        currentDelay = ((maxDelay - minDelay) * currentLeftAsteroids.toFloat() / totalAsteroids.toFloat()) + minDelay
    }

    fun update(dt: Float, isPlay: Boolean) {
        bgTimer += dt

        if (isPlay) {
            if (bgTimer > currentDelay) {
                if (playLowPulse) {
                    lowPulse?.let { it.play(0.4f) }
                }
                else {
                    highPulse?.let { it.play(0.4f) }
                }
                playLowPulse = !playLowPulse
                bgTimer -= currentDelay
            }
        }
    }
}