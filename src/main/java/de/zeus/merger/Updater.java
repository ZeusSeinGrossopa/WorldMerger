package de.zeus.merger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Updater {

    private static File jarPath;

    public static void start() {
        UpdaterAPI.setAutoDelete(true);
        UpdaterAPI.downloadUpdater(new File(getJarPath() + "/Updater.jar"));

        final String[][] versionArguments = {null};
        UpdaterAPI.getLatestReleaseFromGithub("ZeusSeinGrossopa", "WorldMerger", strings -> versionArguments[0] = strings);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                UpdaterAPI.update(versionArguments[0][0], new File(getJarPath().getParentFile().getAbsolutePath() + "/" + getJarPath().getName()), false);
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