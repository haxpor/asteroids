package io.wasin.asteriods.entities

import com.badlogic.gdx.utils.Pool

/**
 * Created by haxpor on 7/10/17.
 */
class AsteriodPool(initialCapacity: Int): Pool<Asteriod>(initialCapacity) {

    override fun newObject(): Asteriod {
        return Asteriod()
    }
}