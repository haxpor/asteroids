package io.wasin.asteroids.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.SerializationException
import io.wasin.asteroids.data.LevelResult
import io.wasin.asteroids.data.PlayerSave
import io.wasin.asteroids.data.PlayerSaveCache
import io.wasin.asteroids.interfaces.ISaveFile

/**
 * Created by haxpor on 5/31/17.
 */
class PlayerSaveFileManager(filePath: String): ISaveFile {
    var saveFilePath: String = filePath
        private set
    var cache: PlayerSaveCache
        private set
    private var json: Json

    init {
        json = Json()
        json.setOutputType(JsonWriter.OutputType.json)
        json.setUsePrototypes(false)

        cache = PlayerSaveCache()
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     */
    @Throws(GameRuntimeException::class)
    fun readSaveFile(): PlayerSave? {
        return readSaveFile(saveFilePath)
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     * @throws SerializationException if file's content cannot be parsed back to Json object, file might be corrupted
     */
    @Throws(GameRuntimeException::class, SerializationException::class)
    override fun readSaveFile(filePath: String): PlayerSave? {
        val handle = Gdx.files.local(filePath)
        if(!handle.exists()) {
            throw GameRuntimeException("Save file doesn't exist at $filePath", GameRuntimeException.SAVE_FILE_NOT_FOUND)
        }

        val jsonString = handle.readString()
        if (jsonString.length == 0) {
            // invalidate internal cached then return null
            cache.data = null
            throw GameRuntimeException("Save file's content is empty. Save file might be corrupted.", GameRuntimeException.SAVE_FILE_EMPTY_CONTENT)
        }

        Gdx.app.log("PlayerSaveFileManager", "save file content")
        Gdx.app.log("PlayerSaveFileManager", jsonString)
        val playerSave = json.fromJson(PlayerSave::class.java, jsonString)
        // syn to internal cache
        cache.data = playerSave

        return playerSave
    }

    /**
     * @throws GameRuntimeException if there's any error haeppning
     */
    fun writeSaveFile(data: PlayerSave) {
        writeSaveFile(data, saveFilePath)
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     */
    @Throws(GameRuntimeException::class)
    override fun writeSaveFile(data: PlayerSave, filePath: String) {
        val handle = Gdx.files.local(filePath)
        val toWriteString = json.prettyPrint(data)
        Gdx.app.log("PlayerSaveFileManager", "Write content")
        Gdx.app.log("PlayerSaveFileManager", toWriteString)

        try {
            handle.writeString(toWriteString, false)
        }
        catch(e: GdxRuntimeException) {
            throw GameRuntimeException("${e.message}", GameRuntimeException.WRITE_FILE_ERROR)
        }
        finally {
            // sync with internal cache
            cache.data = data.copy()
            Gdx.app.log("PlayerSaveFileManager", "synced with internal cache")
        }
    }

    /**
     * Update level result for particular level with LevelResult with an option to write updated
     * content to the file immediately.
     * @throws GameRuntimeException if cache's data is null
     */
    @Throws(GameRuntimeException::class)
    fun updateLevelResult(level: Int, levelResult: LevelResult, writeImmediately: Boolean) {

        if (cache.data == null) throw GameRuntimeException("Cache's data should not be null prior to calling this method", GameRuntimeException.NULL_ERROR)

        val l = cache.data!!.levelResults[level - 1]
        l.score = levelResult.score

        if (writeImmediately) {
            // write out cached data
            writeSaveFile(cache.data!!)
        }
    }

    /**
     * Write a fresh save file for specified total number of levels.
     * @throws GameRuntimeException if there's any error regarding to I/O operation
     */
    fun writeFreshSaveFile(totalLevels: Int) {
        val tmpArray = ArrayList<LevelResult>()
        for (i in 0..totalLevels-1) {
            tmpArray.add(LevelResult(0))
        }
        val data = PlayerSave(tmpArray.toTypedArray())
        writeSaveFile(data)

        // sync to internal cache
        cache.data = data
    }

    /**
     * Get a copy of level result at specified level.
     * @return LevelResult if there is such level result available, otherwise return null which might be of null cache, or index out of bound
     */
    fun getLevelResult(level: Int): LevelResult? {
        if (cache.data == null) return null
        if (level > cache.data!!.levelResults.count()) return null

        return cache.data!!.levelResults[level-1].copy()
    }
}