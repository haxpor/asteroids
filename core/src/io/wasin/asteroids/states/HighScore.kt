package io.wasin.asteroids.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import io.wasin.asteroids.handlers.BBInput
import io.wasin.asteroids.handlers.GameStateManager
import io.wasin.asteroids.handlers.Settings

/**
 * Created by haxpor on 7/12/17.
 */
class HighScore(gsm: GameStateManager): GameState(gsm) {

    private var titleFont: BitmapFont
    private var titleGlyph: GlyphLayout
    private var textFont: BitmapFont
    private var highScoreGlyphs: Array<GlyphLayout>

    init {
        // create font
        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"))
        // - title font
        titleFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 40
        })
        // - text font
        textFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also {
            it.size = 35
            it.kerning = false  // we want monospace-like for name
        })
        fontGen.dispose()

        // read highscores from save file
        game.playerSaveFileManager.sync(Settings.TOTAL_HIGH_SCORES_RECORD)

        // create glyphs
        titleGlyph = GlyphLayout(titleFont, "High Scores")
        // get synced highscores
        val highScores = game.playerSaveFileManager.cache.data!!.highScores
        highScoreGlyphs = Array(Settings.TOTAL_HIGH_SCORES_RECORD, { i ->
            GlyphLayout(textFont, String.format("%2d. %7d %s", i+1, highScores[i].score, highScores[i].name))
        })
    }

    override fun handleInput(dt: Float) {
        if (BBInput.isPressed(BBInput.BUTTON_ENTER) ||
                BBInput.isPressed(BBInput.BUTTON_ESCAPE)) {
            gsm.setState(Mainmenu(gsm))
        }
    }

    override fun update(dt: Float) {
        handleInput(dt)
    }

    override fun render() {
        sb.projectionMatrix = hudCam.combined
        hudViewport.apply(true)

        // clear screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.begin()
        titleFont.draw(sb, titleGlyph,
                hudCam.viewportWidth/2f - titleGlyph.width/2f,
                hudCam.viewportHeight - 30f - titleGlyph.height)

        val xPos = hudCam.viewportWidth/2f - highScoreGlyphs.first().width/2
        highScoreGlyphs.forEachIndexed { i, it ->
            textFont.draw(sb, highScoreGlyphs[i], xPos, hudCam.viewportHeight - 30f - titleGlyph.height - 50f - i*35f)
        }
        sb.end()
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}