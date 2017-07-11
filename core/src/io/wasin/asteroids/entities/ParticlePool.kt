package io.wasin.asteroids.entities

import com.badlogic.gdx.utils.Pool

/**
 * Created by haxpor on 7/10/17.
 */
class ParticlePool(initialCapacity: Int): Pool<Particle>(initialCapacity) {

    override fun newObject(): Particle {
        return Particle()
    }
}