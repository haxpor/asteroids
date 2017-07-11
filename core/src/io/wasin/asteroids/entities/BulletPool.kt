package io.wasin.asteroids.entities

import com.badlogic.gdx.utils.Pool

/**
 * Created by haxpor on 7/10/17.
 */
class BulletPool(initialCapacity: Int): Pool<Bullet>(initialCapacity) {

    override fun newObject(): Bullet {
        return Bullet()
    }
}