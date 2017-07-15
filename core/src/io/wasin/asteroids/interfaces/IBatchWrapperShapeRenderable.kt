package io.wasin.asteroids.interfaces

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Created by haxpor on 7/16/17.
 *
 * IBatchWrapperShapeRenderable needs to work with IBatchShapeRenderable.
 *
 * It improves performance in rendering multiple objects in a single batch. It will prepare
 * rendering environment before actual render by IBatchShapeRenderable, begin, and end it.
 *
 * Recommended to implement this interface in companion object of class.
 */
interface IBatchWrapperShapeRenderable {

    /**
     * Begin batch render
     */
    fun beginBatchRender(sr: ShapeRenderer)


    /**
     * End batch render.
     */
    fun endBatchRender(sr: ShapeRenderer)
}