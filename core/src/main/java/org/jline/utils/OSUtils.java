/*
 * Copyright (c) 2002-2016, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.utils;

import java.io.File;

public class OSUtils {

    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    public static final boolean IS_CYGWIN = IS_WINDOWS
            && System.getenv("PWD") != null
            && System.getenv("PWD").startsWith("/");

    @Deprecated
    public static final boolean IS_MINGW = IS_WINDOWS
            && System.getenv("MSYSTEM") != null
            && System.getenv("MSYSTEM").startsWith("MINGW");

    public static final boolean IS_MSYSTEM = IS_WINDOWS
            && System.getenv("MSYSTEM") != null
            && (System.getenv("MSYSTEM").startsWith("MINGW")
                || System.getenv("MSYSTEM").equals("MSYS"));

    public static final boolean IS_CONEMU = IS_WINDOWS
            && System.getenv("ConEmuPID") != null;

    public static final boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("mac");

    public static String TTY_COMMAND;
    public static String STTY_COMMAND;
    public static String STTY_F_OPTION;
    public static String INFOCMP_COMMAND;

    static {
        String tty = null;
        String stty = null;
        String sttyfopt = null;
        String infocmp = null;
        if (OSUtils.IS_CYGWIN || OSUtils.IS_MSYSTEM) {
            String path = System.getenv("PATH");
            if (path != null) {
                String[] paths = path.split(";");
                for (String p : paths) {
                    if (tty == null && new File(p, "tty.exe").exists())
                        tty = new File(p, "tty.exe").getAbsolutePath();
                    if (stty == null && new File(p, "stty.exe").exists())
                        stty = new File(p, "stty.exe").getAbsolutePath();
                    if (infocmp == null && new File(p, "infocmp.exe").exists())
                        infocmp = new File(p, "infocmp.exe").getAbsolutePath();
                }
            }
            if (tty == null) tty = "tty.exe";
            if (stty == null) stty = "stty.exe";
            if (infocmp == null) infocmp = "infocmp.exe";
        } else {
            tty = "tty";
            stty = "stty";
            sttyfopt = "-F";
            infocmp = "infocmp";
            if (IS_OSX) {
                stty = "/bin/stty";
                sttyfopt = "-f";
            }
        }
        TTY_COMMAND = tty;
        STTY_COMMAND = stty;
        STTY_F_OPTION = sttyfopt;
        INFOCMP_COMMAND = infocmp;
    }

}
