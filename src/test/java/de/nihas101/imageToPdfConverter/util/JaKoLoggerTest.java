package de.nihas101.imageToPdfConverter.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static ch.qos.logback.classic.Level.*;
import static de.nihas101.imageToPdfConverter.util.JaKoLogger.setRootLoggerLevel;
import static junit.framework.TestCase.assertEquals;

public class JaKoLoggerTest {
    @Test
    public void setLoggingLevelTrace() {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(TRACE);
        assertLoggings(logger, TRACE);
    }

    @Test
    public void setLoggingLevelDebug() {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(DEBUG);
        assertLoggings(logger, DEBUG);
    }

    @Test
    public void setLoggingLevelInfo() {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(INFO);
        assertLoggings(logger, INFO);
    }

    @Test
    public void setLoggingLevelWarn() {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(WARN);
        assertLoggings(logger, WARN);
    }

    @Test
    public void setLoggingLevelError() {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(ERROR);
        assertLoggings(logger, ERROR);
    }

    @Test
    public void setRootLoggerLevelTest() {
        setRootLoggerLevel(TRACE);
        TestLogger logger = createTestLogger(DEBUG);
        assertLogging(logger, TRACE);
    }

    private void assertLoggings(TestLogger logger, Level level) {
        assertLogging(logger, level);
        assertLogging(logger, level);
        assertLogging(logger, level);
        assertLogging(logger, level);
        assertLogging(logger, level);
    }

    private void assertLogging(TestLogger logger, Level level) {
        log(logger, level);

        if (logger.isEnabledFor(level)) assertEquals(level.toString(), logger.getLastMessage());
        else assertEquals("", logger.getLastMessage());
    }

    private void log(TestLogger logger, Level level) {
        logFormatted(logger, level, level.toString());
    }

    private void logFormatted(TestLogger logger, Level level, Object args) {
        if (level == TRACE) logger.trace("{}", args);
        else if (level == DEBUG) logger.debug("{}", args);
        else if (level == INFO) logger.info("{}", args);
        else if (level == WARN) logger.warn("{}", args);
        else if (level == ERROR) logger.error("{}", args);
    }

    @Test
    public void multipleArgumentsTrace() {
        TestLogger logger = logMultipleArguments(TRACE);
        assertEquals("1,2,3", logger.getLastMessage());
    }

    @Test
    public void multipleArgumentsDebug() {
        TestLogger logger = logMultipleArguments(DEBUG);
        assertEquals("1,2,3", logger.getLastMessage());
    }

    @Test
    public void multipleArgumentsInfo() {
        TestLogger logger = logMultipleArguments(INFO);
        assertEquals("1,2,3", logger.getLastMessage());
    }

    @Test
    public void multipleArgumentsWarn() {
        TestLogger logger = logMultipleArguments(WARN);
        assertEquals("1,2,3", logger.getLastMessage());
    }

    @Test
    public void multipleArgumentsError() {
        TestLogger logger = logMultipleArguments(ERROR);
        assertEquals("1,2,3", logger.getLastMessage());
    }

    private TestLogger logMultipleArguments(Level level) {
        TestLogger logger = createTestLogger(DEBUG);
        logger.setLoggingLevel(TRACE);

        Object[] args = new Object[]{1, 2, 3};

        logFormatted(logger, level, args);

        return logger;
    }

    private void logFormatted(TestLogger logger, Level level, Object[] args) {
        if (level == TRACE) logger.trace("{},{},{}", args);
        else if (level == DEBUG) logger.debug("{},{},{}", args);
        else if (level == INFO) logger.info("{},{},{}", args);
        else if (level == WARN) logger.warn("{},{},{}", args);
        else if (level == ERROR) logger.error("{},{},{}", args);
    }

    private TestLogger createTestLogger(Level level) {
        Logger logger = (Logger) LoggerFactory.getLogger(JaKoLoggerTest.class);
        logger.setLevel(level);

        return new TestLogger(logger);
    }

    class TestLogger extends JaKoLogger {
        public TestLogger(Logger logger) {
            super(logger);
        }

        public boolean isEnabledFor(Level level) {
            return logger.isEnabledFor(level);
        }

        public String getLastMessage() {
            String lastMessage = ((StringAppender) logger.getLoggerContext().getLogger("root").getAppender("TEST")).getLastMessage();
            ((StringAppender) logger.getLoggerContext().getLogger("root").getAppender("TEST")).setLastMessage("");
            return lastMessage;
        }
    }
}