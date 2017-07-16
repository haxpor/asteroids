package io.wasin.asteroids.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.PovDirection
import com.badlogic.gdx.controllers.mappings.Xbox
import com.badlogic.gdx.math.Vector3

/**
 * Created by haxpor on 5/16/17.
 */
class BBInputProcessor : InputAdapter(), ControllerListener {

    /** Keyboard, Mouse, and Touch **/
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        BBInput.screenX = screenX
        BBInput.screenY = screenY
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // keyboard
        BBInput.screenX = screenX
        BBInput.screenY = screenY
        BBInput.down = true

        // mouse
        BBInput.mouseDown = true
        if (button == Input.Buttons.LEFT) {
            BBInput.setMouseKey(BBInput.MouseKey.LEFT, true)
        }
        if (button == Input.Buttons.RIGHT) {
            BBInput.setMouseKey(BBInput.MouseKey.RIGHT, true)
        }

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // keyboard
        BBInput.screenX = screenX
        BBInput.screenY = screenY
        BBInput.down = false

        // mouse
        BBInput.mouseDown = false
        if (button == Input.Buttons.LEFT) {
            BBInput.setMouseKey(BBInput.MouseKey.LEFT, false)
        }
        if (button == Input.Buttons.RIGHT) {
            BBInput.setMouseKey(BBInput.MouseKey.RIGHT, false)
        }

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        BBInput.screenX = screenX
        BBInput.screenY = screenY
        BBInput.down = true
        BBInput.mouseDown = true
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ENTER) {
            BBInput.setButtonKey(BBInput.ButtonKey.ENTER, true)
        }
        if (keycode == Input.Keys.ESCAPE) {
            BBInput.setButtonKey(BBInput.ButtonKey.ESCAPE, true)
        }
        if (keycode == Input.Keys.SPACE) {
            BBInput.setButtonKey(BBInput.ButtonKey.SPACE, true)
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            BBInput.setButtonKey(BBInput.ButtonKey.SHIFT, true)
        }
        if (keycode == Input.Keys.LEFT) {
            BBInput.setButtonKey(BBInput.ButtonKey.LEFT, true)
        }
        if (keycode == Input.Keys.RIGHT) {
            BBInput.setButtonKey(BBInput.ButtonKey.RIGHT, true)
        }
        if (keycode == Input.Keys.UP) {
            BBInput.setButtonKey(BBInput.ButtonKey.UP, true)
        }
        if (keycode == Input.Keys.DOWN) {
            BBInput.setButtonKey(BBInput.ButtonKey.DOWN, true)
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.ENTER) {
            BBInput.setButtonKey(BBInput.ButtonKey.ENTER, false)
        }
        if (keycode == Input.Keys.ESCAPE) {
            BBInput.setButtonKey(BBInput.ButtonKey.ESCAPE, false)
        }
        if (keycode == Input.Keys.SPACE) {
            BBInput.setButtonKey(BBInput.ButtonKey.SPACE, false)
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            BBInput.setButtonKey(BBInput.ButtonKey.SHIFT, false)
        }
        if (keycode == Input.Keys.LEFT) {
            BBInput.setButtonKey(BBInput.ButtonKey.LEFT, false)
        }
        if (keycode == Input.Keys.RIGHT) {
            BBInput.setButtonKey(BBInput.ButtonKey.RIGHT, false)
        }
        if (keycode == Input.Keys.UP) {
            BBInput.setButtonKey(BBInput.ButtonKey.UP, false)
        }
        if (keycode == Input.Keys.DOWN) {
            BBInput.setButtonKey(BBInput.ButtonKey.DOWN, false)
        }
        return true
    }

    /** Contrllers **/
    override fun buttonDown(controller: Controller?, buttonCode: Int): Boolean {

        // return false to allow other system to handle event too
        if (controller != BBInput.controller1 && controller != BBInput.controller2 || controller == null) return false

        var cindex = 0

        // check which controller to work with
        if (controller == BBInput.controller1) {
            cindex = 0
        }
        else if (controller == BBInput.controller2) {
            cindex = 1
        }

        BBInput.controller1Down = true

        if (buttonCode == Xbox.X) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.X, true)
        }
        if (buttonCode == Xbox.A) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.A, true)
        }
        if (buttonCode == Xbox.B) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.B, true)
        }
        if (buttonCode == Xbox.Y) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.Y, true)
        }
        if (buttonCode == Xbox.DPAD_LEFT) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_LEFT, true)
        }
        if (buttonCode == Xbox.DPAD_RIGHT) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_RIGHT, true)
        }
        if (buttonCode == Xbox.DPAD_UP) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_UP, true)
        }
        if (buttonCode == Xbox.DPAD_DOWN) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_DOWN, true)
        }
        if (buttonCode == Xbox.L_BUMPER) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.LEFT_BUMPER, true)
        }
        if (buttonCode == Xbox.R_BUMPER) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.RIGHT_BUMPER, true)
        }

        return true
    }

    override fun buttonUp(controller: Controller?, buttonCode: Int): Boolean {
        // return false to allow other system to handle event too
        if (controller != BBInput.controller1 && controller != BBInput.controller2 || controller == null) return false

        var cindex = 0

        // check which controller to work with
        if (controller == BBInput.controller1) {
            cindex = 0
        }
        else if (controller == BBInput.controller2) {
            cindex = 1
        }

        BBInput.controller1Down = false

        if (buttonCode == Xbox.X) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.X, false)
        }
        if (buttonCode == Xbox.A) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.A, false)
        }
        if (buttonCode == Xbox.B) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.B, false)
        }
        if (buttonCode == Xbox.Y) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.Y, false)
        }
        if (buttonCode == Xbox.DPAD_LEFT) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_LEFT, false)
        }
        if (buttonCode == Xbox.DPAD_RIGHT) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_RIGHT, false)
        }
        if (buttonCode == Xbox.DPAD_UP) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_UP, false)
        }
        if (buttonCode == Xbox.DPAD_DOWN) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.DPAD_DOWN, false)
        }
        if (buttonCode == Xbox.L_BUMPER) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.LEFT_BUMPER, false)
        }
        if (buttonCode == Xbox.R_BUMPER) {
            BBInput.setGamePadKey(cindex, BBInput.GamePadKey.RIGHT_BUMPER, false)
        }

        return true
    }

    override fun axisMoved(controller: Controller?, axisCode: Int, value: Float): Boolean {
        // ignore axis
        // no need to return false to let other system handle it further
        return true
    }

    override fun povMoved(controller: Controller?, povCode: Int, value: PovDirection?): Boolean {
        return true
    }

    override fun xSliderMoved(controller: Controller?, sliderCode: Int, value: Boolean): Boolean {
        return true
    }

    override fun ySliderMoved(controller: Controller?, sliderCode: Int, value: Boolean): Boolean {
        return true
    }

    override fun accelerometerMoved(controller: Controller?, accelerometerCode: Int, value: Vector3?): Boolean {
        return true
    }

    override fun connected(controller: Controller?) {

        Gdx.app.log("BBInputProcessor", "New controller connected ${controller?.name}")

        if (controller == null) return

        if (BBInput.controller1 == null) {
            BBInput.controller1 = controller
        }
        else if (BBInput.controller2 == null) {
            BBInput.controller2 = controller
        }
    }

    override fun disconnected(controller: Controller?) {

        Gdx.app.log("BBInputProcessor", "Controller disconnected ${controller?.name}")

        if (controller == null) return

        if (BBInput.controller1 == controller && BBInput.controller1 != null) {
            // remove previous listener
            BBInput.controller1!!.removeListener(this)
            // reset controller
            BBInput.controller1 = null
        }

        if (BBInput.controller2 == controller && BBInput.controller2 != null) {
            // remove previous listener
            BBInput.controller2!!.removeListener(this)
            // reset controller
            BBInput.controller2 = null
        }
    }

    fun setController1(controller: Controller) {
        BBInput.controller1 = controller
    }
    fun setContorller2(controller: Controller) {
        BBInput.controller2 = controller
    }
}