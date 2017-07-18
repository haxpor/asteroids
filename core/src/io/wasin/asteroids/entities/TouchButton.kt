package io.wasin.asteroids.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport

/**
 * Created by haxpor on 7/18/17.
 *
 * TouchButton provides touch checking and render text on top of its button.
 * It can either check for pressed or down touching state.
 */
class TouchButton(x: Float, y: Float, radius: Float, text: String, font: BitmapFont, viewport: Viewport): UIObject(x, y) {

    var radius: Float = radius
    var font: BitmapFont = font     // don't own, no need to dispose() in here
    var text: String = text
        set(value) {
            // also update glyph
            glyph.setText(font, value)
            field = value
        }

    private var glyph: GlyphLayout = GlyphLayout(font, text)
    private var viewport: Viewport = viewport

    private var cachedCenterPos: Vector3 = Vector3(x, y, 0f)
    private var touchId: Int? = null
    private var isFirstTouch: Boolean = true

    fun isDown(): Boolean {
        return checkInput(false)
    }

    fun isPressed(): Boolean {
        return checkInput(true)
    }

    private fun checkInput(forPressed: Boolean): Boolean {

        // loop through 2 touches at max
        for (i in 0..1) {
            if (Gdx.input.isTouched(i)) {
                // set touch location
                var location = Vector3(Gdx.input.getX(i).toFloat(), Gdx.input.getY(i).toFloat(), 0f)
                viewport.camera.unproject(location, viewport.screenX.toFloat(), viewport.screenY.toFloat(),
                        viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())

                // check if touch or not
                val touchedInBound = cachedCenterPos.dst(location) < this.radius

                // register first touch for pressed and down approach
                if (touchedInBound && isFirstTouch) {
                    // save this touch id
                    touchId = i
                    isFirstTouch = false

                    return true
                }
                // check down state for only down approach
                else if (touchedInBound && !forPressed && !isFirstTouch && touchId != null && touchId == i) {
                    return true
                }
                // check to return untouched (false) for consecutive touch for pressed state
                // as it's already touched
                else if (touchedInBound && forPressed && !isFirstTouch && touchId != null && touchId == i) {
                    return false
                }
                // if touch outside of the bound, then false
                // note: checking touch id is still important as we don't want to cancel other touches
                else if (touchId != null && touchId == i) {
                    return false
                }
            }
        }

        // note: pointer index is preserved for touched one
        if (touchId != null) {
            if (!Gdx.input.isTouched(touchId!!)) {
                // clear touch id, and reset states
                touchId = null
                isFirstTouch = true
            }
        }

        return false
    }

    fun render(sb: SpriteBatch, sr: ShapeRenderer) {
        // render lines
        val oldColor = sr.color
        sr.color = Color.WHITE

        sr.begin(ShapeRenderer.ShapeType.Line)
        sr.circle(x, y, radius)
        sr.end()

        sr.color = oldColor

        // render text
        sb.begin()
        font.draw(sb, glyph, x - glyph.width/2f, y + glyph.height/2f)
        sb.end()
    }
}