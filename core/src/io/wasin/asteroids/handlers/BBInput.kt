package io.wasin.asteroids.handlers

import com.badlogic.gdx.controllers.Controller

/**
 * Created by haxpor on 5/16/17.
 */
class BBInput {

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

    enum class MouseKey {
        LEFT,
        RIGHT
    }

    enum class GamePadKey {
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
        LEFT_TRIGGER,
        RIGHT_TRIGGER
    }

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

        var keys: Array<Boolean> = Array(ButtonKey.values().size, { _ -> false})
        var pkeys: Array<Boolean> = Array(ButtonKey.values().size, { _ -> false})

        var mouseKeys: Array<Boolean> = Array(MouseKey.values().size, { _ -> false})
        var pMouseKeys: Array<Boolean> = Array(MouseKey.values().size, { _ -> false})

        var controller1Keys: Array<Boolean> = Array(GamePadKey.values().size, { _ -> false})
        var pController1Keys: Array<Boolean> = Array(GamePadKey.values().size, { _ -> false})

        var controller2Keys: Array<Boolean> = Array(GamePadKey.values().size, { _ -> false})
        var pController2Keys: Array<Boolean> = Array(GamePadKey.values().size, { _ -> false})

        fun update() {
            // update previous down
            pdown = down
            pMouseDown = mouseDown
            pController1Down = controller1Down
            pController2Down = controller2Down

            for (i in 0..ButtonKey.values().size - 1) {
                pkeys[i] = keys[i]
            }

            for (i in 0..MouseKey.values().size - 1) {
                pMouseKeys[i] = mouseKeys[i]
            }

            for (i in 0..GamePadKey.values().size - 1) {
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
        fun isGamePadDown(cindex: Int, key: GamePadKey): Boolean {
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
        fun isGamePadPressed(cindex: Int, key: GamePadKey): Boolean {
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
        fun setGamePadKey(cindex: Int, key: GamePadKey, b: Boolean) {
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
        fun isGamePadDown(cindex: Int): Boolean {
            if (cindex == 0) {
                return controller1Down
            }
            else {
                return controller2Down
            }
        }

        /** isPressed **/
        fun isButtonPressed(): Boolean {
            return down && !pdown
        }
        fun isMousePressed(): Boolean {
            return mouseDown && !pMouseDown
        }
        fun isGamePadPressed(cindex: Int): Boolean {
            if (cindex == 0) {
                return controller1Down && !pController1Down
            }
            else {
                return controller2Down && !pController2Down
            }
        }

        /** isReleased **/
        fun isButtonReleased(): Boolean {
            return pdown && !down
        }
        fun isMouseReleased(): Boolean {
            return pMouseDown && !mouseDown
        }
        fun isGamePadReleased(cindex: Int): Boolean {
            if (cindex == 0) {
                return pController1Down && !controller1Down
            }
            else {
                return pController2Down && !controller2Down
            }
        }
    }
}