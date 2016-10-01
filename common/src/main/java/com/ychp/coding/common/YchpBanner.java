package com.ychp.coding.common;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/16
 */
public class YchpBanner implements org.springframework.boot.Banner {
    private static final String[] BANNER = new String[]{"",
            "                     __       ______ \n" +
            "   __  __   _____   / /_     / __   |\n" +
            "  / / / /  / ___/  / __ \\   / /__/ /\n" +
            " / /_/ /  / /__   / / / /  /  ___ /\n" +
            " \\__, /  /____/  /_/ /_/  / /  \n" +
            "/____/                   /_/"};
    private static final String SPRING_BOOT = " :: Powered by Ychp.inc :: ";
    private static final int STRAP_LINE_SIZE = 42;

    public YchpBanner() {
    }

    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        String[] version = BANNER;
        int padding = version.length;

        for(int var6 = 0; var6 < padding; ++var6) {
            String line = version[var6];
            printStream.println(line);
        }

        String var8 = "(v1.0)";

        String var9;
        for(var9 = ""; var9.length() < 42 - (var8.length() + " :: Powered by Ychp.inc :: ".length()); var9 = var9 + " ") {
            ;
        }

        printStream.println(AnsiOutput.toString(new Object[]{AnsiColor.GREEN, " :: Powered by Ychp.inc :: ", AnsiColor.DEFAULT, var9, AnsiStyle.FAINT, var8}));
        printStream.println();
    }
}
