package io.wasin.asteroids.entities

import com.badlogic.gdx.utils.Pool

/**
 * Created by haxpor on 7/10/17.
 */
class AsteroidPool(initialCapacity: Int): Pool<Asteroid>(initialCapacity) {

    override fun newObject(): Asteroid {
        return Asteroid()
    }
}