package io.wasin.asteriods.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.wasin.asteriods.entities.Asteriod
import io.wasin.asteriods.entities.AsteriodPool
import io.wasin.asteriods.entities.Player
import io.wasin.asteriods.handlers.BBInput
import io.wasin.asteriods.handlers.GameStateManager

/**
 * Created by haxpor on 6/16/17.
 */
class Play(gsm: GameStateManager): GameState(gsm){

    private var sr: ShapeRenderer
    private var player: Player

    private var asteriodPool: AsteriodPool = AsteriodPool(30)
    private var asteriods: ArrayList<Asteriod> = ArrayList()

    init {
        sr = ShapeRenderer()
        player = Player(4)
        asteriods.add(Asteriod(100f, 300f, Asteriod.Type.SMALL))
        asteriods.add(Asteriod(200f, 250f, Asteriod.Type.MEDIUM))
        asteriods.add(Asteriod(300f, 350f, Asteriod.Type.LARGE))
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

        // shoot
        if (BBInput.isPressed(BBInput.BUTTON_SPACE)) {
            player.shoot()
        }
    }

    override fun update(dt: Float) {
        handleInput(dt)

        player.update(dt)

        if (asteriods.count() > 0) {
            for (i in asteriods.count()-1 downTo 0) {
                val a = asteriods[i]
                a.update(dt)
                if (a.shouldBeRemoved) {
                    asteriods.removeAt(i)
                    asteriodPool.free(a)
                }
            }
        }
    }

    override fun render() {
        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // render player
        player.render(sr)

        // batch render for asteriods
        sr.begin(ShapeRenderer.ShapeType.Line)
        for (a in asteriods) {
            a.renderBatch(sr)
        }
        sr.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {
    }
}