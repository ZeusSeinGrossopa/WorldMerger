package de.zeus.merger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.*;

public class Utils {

    private static volatile boolean launched = false;

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

    public static void betterLaunch(Class<? extends Application> applicationClass) {
        if (!launched) {
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(applicationClass)).start();
            launched = true;
        } else {
            Platform.runLater(() -> {
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}