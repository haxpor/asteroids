package io.wasin.asteriods.handlers

import io.wasin.asteriods.Game
import io.wasin.asteriods.states.*

import java.util.Stack

/**
 * Created by haxpor on 5/14/17.
 */

class GameStateManager(game: Game){
    var game: Game
        private set
    private var gameStates: Stack<GameState>

    private var isCurrentStateClear: Boolean = false
    private var currentStateCystalsAmount: Int = 0
    private var currentMaxCrystalAmount: Int = 0

    init {
        this.game = game
        this.gameStates = Stack<GameState>()
    }

    fun update(dt: Float) {
        this.gameStates.peek().update(dt)
    }

    fun resize(width: Int, height: Int) {
        this.gameStates.peek().resize(width, height)
    }

    fun render() {
        for (state in this.gameStates) {
            state.render()
        }
    }

    fun setState(state: GameState) {
        this.gameStates.clear()
        this.pushState(state)
    }

    fun pushState(state: GameState) {
        this.gameStates.push(state)
    }

    fun popState() {
        val g = this.gameStates.pop()
        g.dispose()
    }

    fun setCurrentActiveLevelAsGameOver() {
        isCurrentStateClear = false
        currentStateCystalsAmount = 0
        currentMaxCrystalAmount = 0
    }
}