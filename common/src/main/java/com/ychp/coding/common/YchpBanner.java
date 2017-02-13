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

    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        for (String line : BANNER) {
            printStream.println(line);
        }

        String versionStr = "(v1.0)";

        String empty = "";
        for(; empty.length() < STRAP_LINE_SIZE - (versionStr.length() + SPRING_BOOT.length());) {
            empty = empty + " ";
        }

        printStream.println(AnsiOutput.toString(new Object[]{AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, empty, AnsiStyle.FAINT, versionStr}));
        printStream.println();
    }
}
