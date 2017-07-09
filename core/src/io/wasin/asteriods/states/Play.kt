package io.wasin.asteriods.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.wasin.asteriods.entities.Player
import io.wasin.asteriods.handlers.BBInput
import io.wasin.asteriods.handlers.GameStateManager

/**
 * Created by haxpor on 6/16/17.
 */
class Play(gsm: GameStateManager): GameState(gsm){

    private var sr: ShapeRenderer
    private var player: Player

    init {
        sr = ShapeRenderer()
        player = Player()
    }

    override fun handleInput(dt: Float) {
        // left button is pressed and player is not going left
        if (BBInput.isDown(BBInput.BUTTON_LEFT) && !player.left) {
            player.left = true
        }
        // left button is pressed and player is not going left
        else if (!BBInput.isDown(BBInput.BUTTON_LEFT) && player.left) {
            player.left = false
        }

        // right button is pressed and player is not going right
        if (BBInput.isDown(BBInput.BUTTON_RIGHT) && !player.right) {
            player.right = true
        }
        // right button is pressed and player is not going right
        else if (!BBInput.isDown(BBInput.BUTTON_RIGHT) && player.right) {
            player.right = false
        }

        // up button is pressed and player is not going up
        if (BBInput.isDown(BBInput.BUTTON_UP) && !player.up) {
            player.up = true
        }
        // up button is pressed and player is not going up
        else if (!BBInput.isDown(BBInput.BUTTON_UP) && player.up) {
            player.up = false
        }
    }

    override fun update(dt: Float) {
        handleInput(dt)

        player.update(dt)
    }

    override fun render() {
        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.draw(sr)
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {
    }
}