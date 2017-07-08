package io.wasin.asteriods.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.wasin.asteriods.Game

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = Game.TITLE
        config.width = Game.V_WIDTH.toInt()
        config.height = Game.V_HEIGHT.toInt()
        LwjglApplication(Game(), config)
    }
}
