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

        controller?.let {
            getCIndex(it)?.let {
                BBInput.controller1Down = true

                if (buttonCode == Xbox.X) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.X, true)
                }
                if (buttonCode == Xbox.A) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.A, true)
                }
                if (buttonCode == Xbox.B) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.B, true)
                }
                if (buttonCode == Xbox.Y) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.Y, true)
                }
                if (buttonCode == Xbox.DPAD_LEFT) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_LEFT, true)
                }
                if (buttonCode == Xbox.DPAD_RIGHT) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_RIGHT, true)
                }
                if (buttonCode == Xbox.DPAD_UP) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_UP, true)
                }
                if (buttonCode == Xbox.DPAD_DOWN) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_DOWN, true)
                }
                if (buttonCode == Xbox.L_BUMPER) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.LEFT_BUMPER, true)
                }
                if (buttonCode == Xbox.R_BUMPER) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.RIGHT_BUMPER, true)
                }
            }
        }

        return true
    }

    override fun buttonUp(controller: Controller?, buttonCode: Int): Boolean {

        controller?.let {
            getCIndex(it)?.let {
                BBInput.controller1Down = false

                if (buttonCode == Xbox.X) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.X, false)
                }
                if (buttonCode == Xbox.A) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.A, false)
                }
                if (buttonCode == Xbox.B) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.B, false)
                }
                if (buttonCode == Xbox.Y) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.Y, false)
                }
                if (buttonCode == Xbox.DPAD_LEFT) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_LEFT, false)
                }
                if (buttonCode == Xbox.DPAD_RIGHT) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_RIGHT, false)
                }
                if (buttonCode == Xbox.DPAD_UP) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_UP, false)
                }
                if (buttonCode == Xbox.DPAD_DOWN) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.DPAD_DOWN, false)
                }
                if (buttonCode == Xbox.L_BUMPER) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.LEFT_BUMPER, false)
                }
                if (buttonCode == Xbox.R_BUMPER) {
                    BBInput.setControllerKey(it, BBInput.ControllerKey.RIGHT_BUMPER, false)
                }
            }
        }

        return true
    }

    override fun axisMoved(controller: Controller?, axisCode: Int, value: Float): Boolean {

        // handle analog stick and map it to button down/press for convenient in using in game
        // in case the game needs precision and direct control of axis moved, recommended to use
        // Controllers#getAxis() as provided by libgdx
        controller?.let {
            getCIndex(it)?.let {
                // process left analog stick
                // - horizontal
                if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) {
                    // left
                    if (value < -BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_LEFT, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_LEFT, false)

                    // right
                    if (value > BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_RIGHT, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_RIGHT, false)
                }
                // - vertical
                if (axisCode == Xbox.L_STICK_VERTICAL_AXIS) {
                    // up
                    if (value > BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_UP, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_UP, false)

                    // down
                    if (value < -BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_DOWN, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.L_ANALOG_DOWN, false)
                }

                // process right analog stick
                // - horizontal
                if (axisCode == Xbox.R_STICK_HORIZONTAL_AXIS) {
                    // left
                    if (value < -BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_LEFT, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_LEFT, false)

                    // right
                    if (value > BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_RIGHT, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_RIGHT, false)
                }
                // - vertical
                if (axisCode == Xbox.R_STICK_VERTICAL_AXIS) {
                    // up
                    if (value > BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_UP, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_UP, false)

                    // down
                    if (value < -BBInput.deadZone) BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_DOWN, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.R_ANALOG_DOWN, false)
                }

                // trigger button
                if (axisCode == Xbox.L_TRIGGER) {
                    if (value > (1f - BBInput.sensitivity)) BBInput.setControllerKey(it, BBInput.ControllerKey.LEFT_TRIGGER, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.LEFT_TRIGGER, false)
                }
                if (axisCode == Xbox.R_TRIGGER) {
                    if (value > (1f - BBInput.sensitivity)) BBInput.setControllerKey(it, BBInput.ControllerKey.RIGHT_TRIGGER, true)
                    else BBInput.setControllerKey(it, BBInput.ControllerKey.RIGHT_TRIGGER, false)
                }
            }
        }

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

    /**
     * Get c-index of input controller.
     * If no matching controller found, then return null.
     */
    private fun getCIndex(controller: Controller): Int? {
        if (controller == BBInput.controller1) return 0
        else if (controller == BBInput.controller2) return 1
        else return null
    }
}