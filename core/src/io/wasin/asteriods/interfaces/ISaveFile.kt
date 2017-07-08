package io.wasin.asteriods.interfaces

import io.wasin.asteriods.data.PlayerSave

/**
 * Created by haxpor on 6/1/17.
 */
interface ISaveFile {
    /**
     * Read save file from the specified file path.
     * Returned data should be PlayerSave which can be null.
     */
    fun readSaveFile(filePath: String): PlayerSave?

    /**
     * Write save file to the specified file path.
     * Data to be written should be PlayerSave.
     */
    fun writeSaveFile(data: PlayerSave, filePath: String)
}