package com.agilework.sims.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一日志打印工具
 */
public final class SLogger {
    private static final Logger logger = LoggerFactory.getLogger(SLogger.class);
    private static final String LOG_TEMPLATE = "SIMS --> {}: {}";
    private SLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static void info(String tag, String msg) {
        logger.info(LOG_TEMPLATE, tag, msg);
    }

    public static void debug(String tag, String msg) {
        logger.debug(LOG_TEMPLATE, tag, msg);
    }

    public static void warn(String tag, String msg) {
        logger.warn(LOG_TEMPLATE, tag, msg);
    }

    public static void error(String tag, String msg) {
        logger.error(LOG_TEMPLATE, tag, msg);
    }
}
