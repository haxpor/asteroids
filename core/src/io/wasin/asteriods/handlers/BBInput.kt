package io.wasin.asteriods.handlers

import com.badlogic.gdx.controllers.Controller

/**
 * Created by haxpor on 5/16/17.
 */
class BBInput {
    companion object {

        var screenX: Int = 0
        var screenY: Int = 0

        var down: Boolean = false
        var pdown: Boolean = false

        var mouseDown: Boolean = false
        var pMouseDown: Boolean = false

        var controller1Down: Boolean = false
        var pController1Down: Boolean = false

        var controller2Down: Boolean = false
        var pController2Down: Boolean = false

        var controller1: Controller? = null
        var controller2: Controller? = null

        const val NUM_KEYS: Int = 6
        const val BUTTON1: Int = 0
        const val BUTTON2: Int = 1
        const val BUTTON_LEFT: Int = 2
        const val BUTTON_RIGHT: Int = 3
        const val BUTTON_UP: Int = 4
        const val BUTTON_DOWN: Int = 5

        const val NUM_MOUSE_KEYS: Int = 2
        const val MOUSE_BUTTON_LEFT: Int = 0
        const val MOUSE_BUTTON_RIGHT: Int = 1

        const val NUM_CONTROLLER_KEYS: Int = 10
        const val CONTROLLER_BUTTON_1: Int = 0
        const val CONTROLLER_BUTTON_2: Int = 1
        const val CONTROLLER_BUTTON_LEFT: Int = 2
        const val CONTROLLER_BUTTON_RIGHT: Int = 3
        const val CONTROLLER_BUTTON_UP: Int = 4
        const val CONTROLLER_BUTTON_DOWN: Int = 5
        const val CONTROLLER_BUTTON_LEFT_TRIGGER: Int = 6
        const val CONTROLLER_BUTTON_RIGHT_TRIGGER: Int = 7
        const val CONTROLLER_BUTTON_LEFT_BUMPER: Int = 8
        const val CONTROLLER_BUTTON_RIGHT_BUMPER: Int = 9

        var keys: Array<Boolean> = Array<Boolean>(NUM_KEYS, { i -> false})
        var pkeys: Array<Boolean> = Array<Boolean>(NUM_KEYS, { i -> false})

        var mouseKeys: Array<Boolean> = Array<Boolean>(NUM_MOUSE_KEYS, { i -> false})
        var pMouseKeys: Array<Boolean> = Array<Boolean>(NUM_MOUSE_KEYS, { i -> false})

        var controller1Keys: Array<Boolean> = Array<Boolean>(NUM_CONTROLLER_KEYS, { i -> false})
        var pController1Keys: Array<Boolean> = Array<Boolean>(NUM_CONTROLLER_KEYS, { i -> false})

        var controller2Keys: Array<Boolean> = Array<Boolean>(NUM_CONTROLLER_KEYS, { i -> false})
        var pController2Keys: Array<Boolean> = Array<Boolean>(NUM_CONTROLLER_KEYS, { i -> false})

        fun update() {
            // update previous down
            pdown = down
            pMouseDown = mouseDown
            pController1Down = controller1Down
            pController2Down = controller2Down

            for (i in 0..NUM_KEYS-1) {
                pkeys[i] = keys[i]
            }

            for (i in 0..NUM_MOUSE_KEYS-1) {
                pMouseKeys[i] = mouseKeys[i]
            }

            for (i in 0..NUM_CONTROLLER_KEYS-1) {
                pController1Keys[i] = controller1Keys[i]
                pController2Keys[i] = controller2Keys[i]
            }
        }

        /** isDown - specific **/
        fun isDown(i: Int): Boolean {
            return keys[i]
        }
        fun isMouseDown(i: Int): Boolean {
            return mouseKeys[i]
        }
        fun isControllerDown(cindex: Int, i: Int): Boolean {
            if (cindex == 0) {
                return controller1Keys[i]
            }
            else {
                return controller2Keys[i]
            }
        }

        /** isPressed - specific **/
        fun isPressed(i: Int): Boolean {
            return keys[i] && !pkeys[i]
        }
        fun isMousePressed(i: Int): Boolean {
            return mouseKeys[i] && !pMouseKeys[i]
        }
        fun isControllerPressed(cindex: Int, i: Int): Boolean {
            if (cindex == 0) {
                return controller1Keys[i] && !pController1Keys[i]
            }
            else {
                return controller2Keys[i] && !pController2Keys[i]
            }
        }

        /** setKey **/
        fun setKey(i: Int, b: Boolean) {
            keys[i] = b
        }
        fun setMouseKey(i: Int, b: Boolean) {
            mouseKeys[i] = b
        }
        fun setControllerKey(cindex: Int, i: Int, b: Boolean) {
            if (cindex == 0) {
                controller1Keys[i] = b
            }
            else {
                controller2Keys[i] = b
            }
        }

        /** isDown **/
        fun isDown(): Boolean {
            return down
        }
        fun isMouseDown(): Boolean {
            return mouseDown
        }
        fun isControllerDown(cindex: Int): Boolean {
            if (cindex == 0) {
                return controller1Down
            }
            else {
                return controller2Down
            }
        }

        /** isPressed **/
        fun isPressed(): Boolean {
            return down && !pdown
        }
        fun isMousePressed(): Boolean {
            return mouseDown && !pMouseDown
        }
        fun isControllerPressed(cindex: Int): Boolean {
            if (cindex == 0) {
                return controller1Down && !pController1Down
            }
            else {
                return controller2Down && !pController2Down
            }
        }

        /** isReleased **/
        fun isReleased(): Boolean {
            return pdown && !down
        }
        fun isMouseReleased(): Boolean {
            return pMouseDown && !mouseDown
        }
        fun isControllerReleased(cindex: Int): Boolean {
            if (cindex == 0) {
                return pController1Down && !controller1Down
            }
            else {
                return pController2Down && !controller2Down
            }
        }
    }
}