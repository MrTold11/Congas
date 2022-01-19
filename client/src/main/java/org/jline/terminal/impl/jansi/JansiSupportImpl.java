/*
 * Copyright (c) 2002-2020, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.terminal.impl.jansi;

import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.impl.jansi.freebsd.FreeBsdNativePty;
import org.jline.terminal.impl.jansi.linux.LinuxNativePty;
import org.jline.terminal.impl.jansi.osx.OsXNativePty;
import org.jline.terminal.impl.jansi.win.JansiWinSysTerminal;
import org.jline.terminal.Pty;
import org.jline.utils.OSUtils;
import ru.congas.input.InputThread;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JansiSupportImpl {

    static final int JANSI_MAJOR_VERSION;
    static final int JANSI_MINOR_VERSION;
    static {
        int major = 0, minor = 0;
        try {
            String v = null;
            try (InputStream is = AnsiConsole.class.getResourceAsStream("jansi.properties")) {
                if (is != null) {
                    Properties props = new Properties();
                    props.load(is);
                    v = props.getProperty("version");
                }
            } catch (IOException ignored) {}
            if (v == null) {
                v = AnsiConsole.class.getPackage().getImplementationVersion();
            }
            if (v != null) {
                Matcher m = Pattern.compile("([0-9]+)\\.([0-9]+)([.-]\\S+)?").matcher(v);
                if (m.matches()) {
                    major = Integer.parseInt(m.group(1));
                    minor = Integer.parseInt(m.group(2));
                }
            }
        } catch (Throwable ignored) {}
        JANSI_MAJOR_VERSION = major;
        JANSI_MINOR_VERSION = minor;
    }

    public static boolean isAtLeast(int major, int minor) {
        return JANSI_MAJOR_VERSION > major || JANSI_MAJOR_VERSION == major && JANSI_MINOR_VERSION >= minor;
    }

    public Pty current() throws IOException {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux")) {
            return LinuxNativePty.current();
        }
        else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            return OsXNativePty.current();
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            // Solaris is not supported by jansi
            // return SolarisNativePty.current();
        }
        else if (osName.startsWith("FreeBSD")) {
            if (isAtLeast(1, 16)) {
                return FreeBsdNativePty.current();
            }
        }
        throw new UnsupportedOperationException();
    }

    public Pty open(Attributes attributes, Size size) {
        if (isAtLeast(1, 16)) {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Linux")) {
                return LinuxNativePty.open(attributes, size);
            }
            else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
                return OsXNativePty.open(attributes, size);
            }
            //else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            //    //Solaris is not supported by jansi
            //    return SolarisNativePty.current();
            //}
            else if (osName.startsWith("FreeBSD")) {
                return FreeBsdNativePty.open(attributes, size);
            }
        }
        throw new UnsupportedOperationException();
    }

    public Terminal winSysTerminal(String name, String type, boolean ansiPassThrough, Charset encoding, int codepage, boolean nativeSignals, Terminal.SignalHandler signalHandler, boolean paused, InputThread inputHandler) throws IOException {
        if (isAtLeast(1, 12)) {
            JansiWinSysTerminal terminal = JansiWinSysTerminal.createTerminal(name, type, ansiPassThrough, encoding, codepage, nativeSignals, signalHandler, paused, inputHandler);
            if (!isAtLeast(1, 16)) {
                terminal.disableScrolling();
            }
            return terminal;
        }
        throw new UnsupportedOperationException();
    }

    public boolean isConsoleOutput() {
        if (OSUtils.IS_CYGWIN || OSUtils.IS_MSYSTEM) {
            if (isAtLeast(2,1)) {
                return JansiNativePty.isConsoleOutput();
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (OSUtils.IS_WINDOWS) {
            return JansiWinSysTerminal.isConsoleOutput();
        }
        return JansiNativePty.isConsoleOutput();
    }

    public boolean isConsoleInput() {
        if (OSUtils.IS_CYGWIN || OSUtils.IS_MSYSTEM) {
            if (isAtLeast(2,1)) {
                return JansiNativePty.isConsoleInput();
            } else {
                throw new UnsupportedOperationException();
            }
        } else if (OSUtils.IS_WINDOWS) {
            return JansiWinSysTerminal.isConsoleInput();
        }
        return JansiNativePty.isConsoleInput();
    }

}
