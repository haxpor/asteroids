package io.wasin.asteriods.handlers

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
            BBInput.setMouseKey(BBInput.MOUSE_BUTTON_LEFT, true)
        }
        if (button == Input.Buttons.RIGHT) {
            BBInput.setMouseKey(BBInput.MOUSE_BUTTON_RIGHT, true)
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
            BBInput.setMouseKey(BBInput.MOUSE_BUTTON_LEFT, false)
        }
        if (button == Input.Buttons.RIGHT) {
            BBInput.setMouseKey(BBInput.MOUSE_BUTTON_RIGHT, false)
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
        if (keycode == Input.Keys.Z) {
            BBInput.setKey(BBInput.BUTTON1, true)
        }
        if (keycode == Input.Keys.X) {
            BBInput.setKey(BBInput.BUTTON2, true)
        }
        if (keycode == Input.Keys.LEFT) {
            BBInput.setKey(BBInput.BUTTON_LEFT, true)
        }
        if (keycode == Input.Keys.RIGHT) {
            BBInput.setKey(BBInput.BUTTON_RIGHT, true)
        }
        if (keycode == Input.Keys.UP) {
            BBInput.setKey(BBInput.BUTTON_UP, true)
        }
        if (keycode == Input.Keys.DOWN) {
            BBInput.setKey(BBInput.BUTTON_DOWN, true)
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.Z) {
            BBInput.setKey(BBInput.BUTTON1, false)
        }
        if (keycode == Input.Keys.X) {
            BBInput.setKey(BBInput.BUTTON2, false)
        }
        if (keycode == Input.Keys.LEFT) {
            BBInput.setKey(BBInput.BUTTON_LEFT, false)
        }
        if (keycode == Input.Keys.RIGHT) {
            BBInput.setKey(BBInput.BUTTON_RIGHT, false)
        }
        if (keycode == Input.Keys.UP) {
            BBInput.setKey(BBInput.BUTTON_UP, false)
        }
        if (keycode == Input.Keys.DOWN) {
            BBInput.setKey(BBInput.BUTTON_DOWN, false)
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

        // TODO: we fix to use xbox360 layout for mapping here, if *you* have time add configuration for user to map key and let the game use that configuration file
        if (buttonCode == Xbox.B) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_1, true)
        }
        else if (buttonCode == Xbox.A) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_2, true)
        }
        else if (buttonCode == Xbox.DPAD_LEFT) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT, true)
        }
        else if (buttonCode == Xbox.DPAD_RIGHT) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT, true)
        }
        else if (buttonCode == Xbox.DPAD_UP) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_UP, true)
        }
        else if (buttonCode == Xbox.DPAD_DOWN) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_DOWN, true)
        }
        else if (buttonCode == Xbox.L_TRIGGER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT_TRIGGER, true)
        }
        else if (buttonCode == Xbox.R_TRIGGER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT_TRIGGER, true)
        }
        else if (buttonCode == Xbox.L_BUMPER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT_BUMPER, true)
        }
        else if (buttonCode == Xbox.R_BUMPER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT_BUMPER, true)
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

        // TODO: we fix to use xbox360 layout for mapping here, if *you* have time add configuration for user to map key and let the game use that configuration file
        if (buttonCode == Xbox.B) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_1, false)
        }
        else if (buttonCode == Xbox.A) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_2, false)
        }
        else if (buttonCode == Xbox.DPAD_LEFT) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT, false)
        }
        else if (buttonCode == Xbox.DPAD_RIGHT) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT, false)
        }
        else if (buttonCode == Xbox.DPAD_UP) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_UP, false)
        }
        else if (buttonCode == Xbox.DPAD_DOWN) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_DOWN, false)
        }
        else if (buttonCode == Xbox.L_TRIGGER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT_TRIGGER, false)
        }
        else if (buttonCode == Xbox.R_TRIGGER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT_TRIGGER, false)
        }
        else if (buttonCode == Xbox.L_BUMPER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_LEFT_BUMPER, false)
        }
        else if (buttonCode == Xbox.R_BUMPER) {
            BBInput.setControllerKey(cindex, BBInput.CONTROLLER_BUTTON_RIGHT_BUMPER, false)
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