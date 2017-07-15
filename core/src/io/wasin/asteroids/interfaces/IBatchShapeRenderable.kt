package io.wasin.asteroids.interfaces

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created by haxpor on 7/15/17.
 *
 * IBatchShapeRenderable needs to work with IBatchWrapperShapeRenderable.
 *
 * It improves performance in rendering in situation of rendering multiple
 * objects in a single batch. Thus without a waste of setting up rendering environment on and off
 * multiple time for each individual object; as we know it won't be changed after all.
 *
 */
interface IBatchShapeRenderable {

    /**
     * Render batch.
     */
    fun renderBatch(sr: ShapeRenderer)
}