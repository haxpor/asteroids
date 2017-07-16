package io.wasin.asteroids.handlers

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.Controllers

/**
 * Created by haxpor on 5/16/17.
 */
class BBInput {

    /**
     * Key mapping for keyboard that supported in this game.
     */
    enum class ButtonKey {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        ENTER,
        ESCAPE,
        SPACE,
        SHIFT
    }

    /**
     * Key mapping for mouse that supported in this game.
     */
    enum class MouseKey {
        LEFT,
        RIGHT
    }

    /**
     * Key mapping for controller that supported in this game.
     */
    enum class ControllerKey {
        X,
        A,
        B,
        Y,
        DPAD_LEFT,
        DPAD_RIGHT,
        DPAD_UP,
        DPAD_DOWN,
        LEFT_BUMPER,
        RIGHT_BUMPER,

        // handle analog as digital (if needed)
        L_ANALOG_LEFT,
        L_ANALOG_RIGHT,
        L_ANALOG_UP,
        L_ANALOG_DOWN,

        R_ANALOG_LEFT,
        R_ANALOG_RIGHT,
        R_ANALOG_UP,
        R_ANALOG_DOWN,

        LEFT_TRIGGER,
        RIGHT_TRIGGER
    }

    companion object {

        /**
         * Dead zone for GamePad
         */
        var deadZone: Float = 0.3f

        /**
         * Sensitivity when pressing analog button
         */
        var sensitivity: Float = 0.7f

        /**
         * Number of player that supported
         */
        const val NUM_CONTROLLER_SUPPORT: Int = 2

        var screenX: Int = 0
        var screenY: Int = 0

        var down: Boolean = false
        var pdown: Boolean = false

        var mouseDown: Boolean = false
        var pMouseDown: Boolean = false

        var controllerDowns: Array<Boolean> = Array(NUM_CONTROLLER_SUPPORT, { false })
        var pControllerDowns: Array<Boolean> = Array(NUM_CONTROLLER_SUPPORT, { false })

        var controllers: Array<Controller?> = Array(NUM_CONTROLLER_SUPPORT, { null })

        var keys: Array<Boolean> = Array(ButtonKey.values().size, { false })
        var pkeys: Array<Boolean> = Array(ButtonKey.values().size, { false })

        var mouseKeys: Array<Boolean> = Array(MouseKey.values().size, { false })
        var pMouseKeys: Array<Boolean> = Array(MouseKey.values().size, { false })

        var controller1Keys: Array<Boolean> = Array(ControllerKey.values().size, { false })
        var pController1Keys: Array<Boolean> = Array(ControllerKey.values().size, { false })

        var controller2Keys: Array<Boolean> = Array(ControllerKey.values().size, { false })
        var pController2Keys: Array<Boolean> = Array(ControllerKey.values().size, { false })

        // convenient methods to get which controller from total number of controllers supported
        fun controller1(): Controller? { return controllers[0] }
        fun controller2(): Controller? { return controllers[1] }

        fun update() {
            // update previous and current down for general
            pdown = down
            pMouseDown = mouseDown

            for (i in 0..NUM_CONTROLLER_SUPPORT-1) {
                pControllerDowns[i] = controllerDowns[i]
            }

            // update previous and current down for specific keys
            for (i in 0..ButtonKey.values().size - 1) {
                pkeys[i] = keys[i]
            }

            for (i in 0..MouseKey.values().size - 1) {
                pMouseKeys[i] = mouseKeys[i]
            }

            for (i in 0..ControllerKey.values().size - 1) {
                pController1Keys[i] = controller1Keys[i]
                pController2Keys[i] = controller2Keys[i]
            }
        }

        /** isDown - specific **/
        fun isButtonDown(key: ButtonKey): Boolean {
            return keys[key.ordinal]
        }
        fun isMouseDown(key: MouseKey): Boolean {
            return mouseKeys[key.ordinal]
        }
        fun isControllerDown(cindex: Int, key: ControllerKey): Boolean {
            if (cindex == 0) {
                return controller1Keys[key.ordinal]
            }
            else {
                return controller2Keys[key.ordinal]
            }
        }

        /** isPressed - specific **/
        fun isButtonPressed(key: ButtonKey): Boolean {
            return keys[key.ordinal] && !pkeys[key.ordinal]
        }
        fun isMousePressed(key: MouseKey): Boolean {
            return mouseKeys[key.ordinal] && !pMouseKeys[key.ordinal]
        }
        fun isControllerPressed(cindex: Int, key: ControllerKey): Boolean {
            if (cindex == 0) {
                return controller1Keys[key.ordinal] && !pController1Keys[key.ordinal]
            }
            else {
                return controller2Keys[key.ordinal] && !pController2Keys[key.ordinal]
            }
        }

        /** setKey **/
        fun setButtonKey(key: ButtonKey, b: Boolean) {
            keys[key.ordinal] = b
        }
        fun setMouseKey(key: MouseKey, b: Boolean) {
            mouseKeys[key.ordinal] = b
        }
        fun setControllerKey(cindex: Int, key: ControllerKey, b: Boolean) {
            if (cindex == 0) {
                controller1Keys[key.ordinal] = b
            }
            else {
                controller2Keys[key.ordinal] = b
            }
        }

        /** isDown **/
        fun isButtonDown(): Boolean {
            return down
        }
        fun isMouseDown(): Boolean {
            return mouseDown
        }
        fun isControllerDown(cindex: Int): Boolean {
            return controllerDowns[cindex]
        }

        /** isPressed **/
        fun isButtonPressed(): Boolean {
            return down && !pdown
        }
        fun isMousePressed(): Boolean {
            return mouseDown && !pMouseDown
        }
        fun isControllerPressed(cindex: Int): Boolean {
            return controllerDowns[cindex] && !pControllerDowns[cindex]
        }

        /** isReleased **/
        fun isButtonReleased(): Boolean {
            return pdown && !down
        }
        fun isMouseReleased(): Boolean {
            return pMouseDown && !mouseDown
        }
        fun isControllerReleased(cindex: Int): Boolean {
            return pControllerDowns[cindex] && !controllerDowns[cindex]
        }

        /**
         * It will nullify all current reference controller, then set all current detected
         * controllers back again.
         *
         * Use this to sync controllers at any given time.
         */
        fun syncControllers(listener: ControllerListener) {

            // null all current controllers
            for (i in 0..controllers.size-1) {
                controllers[i] = null
            }

            // loop to set all detected controllers
            Controllers.getControllers().forEachIndexed { i, ct ->
                ct.addListener(listener)
                controllers[i] = ct
            }
        }
    }
}