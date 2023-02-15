package de.zeus.merger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Updater {

    private static File jarPath;
    private static final String currentVersion = "3.0";

    public static void start() {
        UpdaterAPI.setAutoDelete(true);
        UpdaterAPI.downloadUpdater(new File(getJarPath() + "/Updater.jar"));

        final String[][] data = {new String[2]};
        UpdaterAPI.getLatestReleaseFromGithub("ZeusSeinGrossopa", "WorldMerger", strings -> data[0] = strings);

        if (data[0][0] == null || data[0][1] == null) {
            System.out.println("Could not get latest release from github!");
            return;
        }

        if(!UpdaterAPI.needUpdate(currentVersion, data[0][0].replace("V", ""))) {
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down and updating...");
            try {
                UpdaterAPI.update(data[0][1], new File(getJarPath().getParentFile().getAbsolutePath() + "/" + getJarPath().getName()), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private static File getJarPath() {
        if (jarPath == null) {
            try {
                jarPath = new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return jarPath;
        }
        return jarPath;
    }
}