package io.wasin.asteriods.handlers

/**
 * Created by haxpor on 6/1/17.
 */
class Settings {
    companion object {
        const val PLAYER_SAVEFILE_RELATIVE_PATH: String = "player.json"
        const val TOTAL_LEVELS: Int = 15

        // all durations are in seconds
        const val GROWTH_TO_LEVEL_2_DURATION: Int = 15
        const val GROWTH_TO_LEVEL_3_DURATION: Int = 30

        // effect that watering can reduce time wait in growing of the tree
        // in seconds
        const val WATER_REDUCE_TIME_DURATION: Int = 5

        // in seconds
        const val FRUIT_GENERATION_COOLDOWN: Int = 30

        // in seconds
        const val MATCH_TIME: Int = 180
    }
}