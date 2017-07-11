package io.wasin.asteroids.handlers

/**
 * Created by haxpor on 6/1/17.
 */
class GameRuntimeException: RuntimeException {

    companion object {
        const val SAVE_FILE_NOT_FOUND: Int = 9000
        const val SAVE_FILE_EMPTY_CONTENT: Int = 9001
        const val WRITE_FILE_ERROR: Int = 9002
        const val NULL_ERROR: Int = 9003
    }

    /**
     * Error code reresent this error
     */
    val code: Int

    constructor(message: String, code: Int): super(message) { this.code = code }
    constructor(t: Throwable, code: Int): super(t) { this.code = code }
    constructor(message: String, t: Throwable, code: Int): super(message,t ) { this.code = code }
}