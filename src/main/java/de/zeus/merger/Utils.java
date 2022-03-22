package de.zeus.merger;

import javax.swing.*;

public class Utils {

    public static void error(String message) {
        error(message, true);
    }

    public static void error(String message, boolean close) {
        System.err.println(message);

        int error = JOptionPane.showOptionDialog(null, message, "ServerMerger", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);

        if (error == JOptionPane.OK_OPTION && close) {
            System.exit(0);
        }
    }

    public static boolean isNull(String string) {
        return string == null || string.isEmpty();
    }
}