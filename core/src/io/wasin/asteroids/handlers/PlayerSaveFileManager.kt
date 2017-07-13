package io.wasin.asteroids.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.SerializationException
import io.wasin.asteroids.data.PlayerScore
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
     * Update with a new player score with an option to write updated content to file immediately.
     * A new player score will be added into high score records when it has more score than the least score as currently have.
     * @throws GameRuntimeException if cache's data is null
     */
    @Throws(GameRuntimeException::class)
    fun updateWithNewPlayerScore(playerScore: PlayerScore, writeImmediately: Boolean) {
        if (cache.data == null) throw GameRuntimeException("Cache's data should not be null prior to calling this method", GameRuntimeException.NULL_ERROR)

        // check if a new score should be added into the records or not
        if (playerScore.score > cache.data!!.highScores.last().score) {
            val highScores = cache.data!!.highScores

            highScores[highScores.lastIndex] = playerScore
            // re-sort
            highScores.sortByDescending { it.score }

            if (writeImmediately) {
                writeSaveFile(cache.data!!)
            }
        }
    }

    /**
     * Write a fresh save file for specified total number of records.
     * @throws GameRuntimeException if there's any error regarding to I/O operation
     */
    fun writeFreshSaveFile(numRecords: Int) {
        val tmpArray = ArrayList<PlayerScore>()
        for (i in 0..numRecords-1) {
            tmpArray.add(PlayerScore())
        }
        val data = PlayerSave(tmpArray.toTypedArray())
        writeSaveFile(data)

        // sync to internal cache
        cache.data = data
    }

    /**
     * Return whether cache data is available or not
     */
    fun isCacheDataAvailable(): Boolean {
        return cache.data != null
    }

    /**
     * Write save file if cache data is not available, then read it.
     * @param numRecords number of records to write in case of a need to write a new fresh save file
     * @param alwaysRead true if always read save file no matter availability of the cache,
     * false to only when cache is not available. Default to false.
     * @return player save
     */
    fun sync(numRecords: Int, alwaysRead: Boolean=false): PlayerSave {
        if (!isCacheDataAvailable() || alwaysRead) {
            try {
                Gdx.app.log("PlayerSaveFileManager", "read save file")
                readSaveFile()
            }
            catch(e: GameRuntimeException) {
                if (e.code == GameRuntimeException.SAVE_FILE_NOT_FOUND ||
                        e.code == GameRuntimeException.SAVE_FILE_EMPTY_CONTENT) {

                    // write a new fresh save file to resolve the issue
                    Gdx.app.log("PlayerSaveFileManager", "write a fresh save file")
                    writeFreshSaveFile(numRecords)
                }
            }
            catch(e: SerializationException) {
                Gdx.app.log("PlayerSaveFileManager", "save file is corrupted, rewrite a fresh one : ${e.message}")

                writeFreshSaveFile(numRecords)
            }
        }
        return cache.data!!
    }
}