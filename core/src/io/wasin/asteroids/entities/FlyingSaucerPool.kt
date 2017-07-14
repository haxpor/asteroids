package io.wasin.asteroids.entities

import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.viewport.Viewport

/**
 * Created by haxpor on 7/14/17.
 */
class FlyingSaucerPool(initialCapacity: Int, player: Player, viewport: Viewport): Pool<FlyingSaucer>(initialCapacity) {

    private var player: Player = player
    private var viewport: Viewport = viewport

    override fun newObject(): FlyingSaucer {
        return FlyingSaucer(player, viewport)
    }


}