package de.nihas101.imageToPdfConverter.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Level.*
import ch.qos.logback.classic.Logger
import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory

class JaKoLoggerTest {
    @Test
    fun setLoggingLevelTrace() {
        val logger = TestLogger.createTestLogger(this.javaClass)
        logger.setLoggingLevel(TRACE)
        assertLoggings(logger, TRACE)
    }

    @Test
    fun setLoggingLevelDebug() {
        val logger = TestLogger.createTestLogger(this.javaClass)
        logger.setLoggingLevel(DEBUG)
        assertLoggings(logger, DEBUG)
    }

    @Test
    fun setLoggingLevelInfo() {
        val logger = TestLogger.createTestLogger(this.javaClass)
        logger.setLoggingLevel(INFO)
        assertLoggings(logger, INFO)
    }

    @Test
    fun setLoggingLevelWarn() {
        val logger = TestLogger.createTestLogger(this.javaClass)
        logger.setLoggingLevel(WARN)
        assertLoggings(logger, WARN)
    }

    @Test
    fun setLoggingLevelError() {
        val logger = TestLogger.createTestLogger(this.javaClass)
        logger.setLoggingLevel(ERROR)
        assertLoggings(logger, ERROR)
    }

    @Test
    fun setRootLoggerLevelTest() {
        setRootLoggerLevel(TRACE)
        val logger = TestLogger.createTestLogger(this.javaClass)
        assertLogging(logger, TRACE)
    }

    private fun assertLoggings(logger: TestLogger, level: Level) {
        assertLogging(logger, level)
        assertLogging(logger, level)
        assertLogging(logger, level)
        assertLogging(logger, level)
        assertLogging(logger, level)
    }

    private fun assertLogging(logger: TestLogger, level: Level) {
        log(logger, level)

        if (logger.isEnabledFor(level)) assertEquals(level.toString(), logger.getLastMessage())
        else assertEquals("", logger.getLastMessage())
    }

    private fun log(logger: TestLogger, level: Level) {
        when (level) {
            TRACE -> logger.trace("{}", level.toString())
            DEBUG -> logger.debug("{}", level.toString())
            INFO -> logger.info("{}", level.toString())
            WARN -> logger.warn("{}", level.toString())
            ERROR -> logger.error("{}", level.toString())
        }
    }

    class TestLogger private constructor(logger: Logger) : JaKoLogger(logger) {

        fun isEnabledFor(level: Level): Boolean {
            return logger.isEnabledFor(level)
        }

        fun getLastMessage(): String {
            val lastMessage = (logger.loggerContext.getLogger("root").getAppender("TEST") as StringAppender).lastMessage
            (logger.loggerContext.getLogger("root").getAppender("TEST") as StringAppender).lastMessage = ""
            return lastMessage
        }

        companion object {
            fun createTestLogger(javaClass: Class<*>, level: Level = Level.DEBUG): TestLogger {
                val logger: Logger = LoggerFactory.getLogger(javaClass) as Logger
                logger.level = level

                return TestLogger(logger)
            }
        }
    }
}