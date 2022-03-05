/*
 * Copyright (c) 2002-2020, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.core.CongasCore;

import java.util.function.Supplier;


/**
 * Internal logger.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author <a href="mailto:gnodet@gmail.com">Guillaume Nodet</a>
 * @since 2.0
 */
public final class Log {
    private static final Logger logger = LogManager.getLogger(Log.class);

    public static void trace(final Object... messages) {
        if (isDebugEnabled()) logger.trace(messages);
    }

    public static void trace(Supplier<String> supplier) {
        if (isDebugEnabled()) logger.trace(supplier);
    }

    public static void debug(Supplier<String> supplier) {
        if (isDebugEnabled()) logger.debug(supplier);
    }

    public static void debug(final Object... messages) {
        if (isDebugEnabled()) logger.debug(messages);
    }

    public static void info(final Object... messages) {
        logger.info(messages);
    }

    public static void warn(final Object... messages) {
        logger.warn(messages);
    }

    public static void error(final Object... messages) {
        logger.error(messages);
    }

    public static boolean isDebugEnabled() {
        return CongasCore.isDebug();
    }

}