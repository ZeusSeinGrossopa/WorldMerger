package de.zeus.merger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The api class for using the UpdaterAPI.
 * See <a href="https://github.com/ZeusSeinGrossopa/UpdaterAPI">https://github.com/ZeusSeinGrossopa/UpdaterAPI</a>
 *
 * @author ZeusSeinGrossopa
 * @version 1.5
 */
public class UpdaterAPI {

    public static final String GITHUB_CUSTOM_URL = "https://api.github.com/repos/%s/releases/latest";

    private static File updaterFile = null;
    private static boolean autoDelete = false;

    private static File jarPath;

    /**
     * Downloading the updater file
     *
     * @param destination the destination folder where it should download the Updater jar file
     * @see #downloadUpdater(File, Consumer)
     */
    public static void downloadUpdater(File destination) {
        downloadUpdater(destination, null);
    }

    /**
     * Downloading the updater file
     *
     * @param destination the destination folder where it should download the Updater jar file
     * @param consumer    the consumer for the callback when the updater is downloaded
     */
    public static void downloadUpdater(File destination, Consumer<File> consumer) {
        destination = new File((destination.isDirectory() ? new File(destination.getAbsolutePath()) + "/Updater.jar" : destination.getAbsolutePath()));

        final File finalDestination = destination;
        updaterFile = finalDestination;

        if (autoDelete) {
            if (destination.exists())
                destination.delete();

            if (consumer != null)
                consumer.accept(destination);
            return;
        }

        getLatestReleaseFromGithub("ZeusSeinGrossopa", "UpdaterAPI", callback -> {
            try {
                URL url = new URL(callback[1]);

                FileUtils.copyURLToFile(url, finalDestination);

                if (consumer != null)
                    consumer.accept(finalDestination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * API method for getting the latest release version and the download link of the GitHub repository
     *
     * @param githubUser the GitHub user of the repository
     * @param repository the repository name
     * @param consumer   callbacks the infos from the api website. First argument is the newest version and the second argument is the download link to the newest release
     * @apiNote this api is not working for pre-releases
     */
    public static void getLatestReleaseFromGithub(String githubUser, String repository, Consumer<String[]> consumer) {
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(String.format(GITHUB_CUSTOM_URL, githubUser + "/" + repository)).openConnection();

            connect.setConnectTimeout(10000);

            connect.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connect.setRequestProperty("Content-Type", "application/json");

            connect.setRequestProperty("User-Agent", githubUser + "/" + repository + " (" + System.getProperty("os.name") + "; " + System.getProperty("os.arch") + ")");

            connect.connect();

            InputStream in = connect.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            if (connect.getResponseCode() == 200) {
                JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();

                String downloadLink = object.entrySet().stream().filter(e -> e.getKey().equals("assets"))
                        .map(Map.Entry::getValue).findFirst().orElseThrow(() -> new RuntimeException("Can not update system"))
                        .getAsJsonArray()
                        .get(0).getAsJsonObject().get("browser_download_url").getAsString();

                consumer.accept(new String[]{object.get("tag_name").getAsString(), downloadLink});
            } else {
                throw new IOException("Could not connect to the GitHub API. Response code: " + connect.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updating your old java program file with the newest jar file
     *
     * @param url     the url of your java program to download and replace it
     * @param newFile the new jar file of your program to replace your old jar
     * @throws IOException calls if the java -jar syntax command can not be called
     * @apiNote the method {@link #downloadUpdater(File, Consumer)} must be called before calling this method. Alternate use the {@link #update(File, String, File)} method
     * @see #update(File, String, File)
     */
    public static void update(String url, File newFile) throws IOException {
        if (updaterFile == null)
            throw new NullPointerException("The downloadUpdater must be called before using this method. Alternate use the UpdaterAPI#update(updaterFile, url, newFile) method.");

        update(updaterFile, url, newFile);
    }

    /**
     * Updating your old java program file with the newest jar file
     *
     * @param url     the url of your java program to download and replace it
     * @param newFile the new jar file of your program to replace your old jar
     * @param restart if the jar should be executed after updating
     * @throws IOException calls if the java -jar syntax command can not be called
     * @apiNote the method {@link #downloadUpdater(File, Consumer)} must be called before calling this method. Alternate use the {@link #update(File, String, File)} method
     * @see #update(File, String, File, boolean)
     */
    public static void update(String url, File newFile, boolean restart) throws IOException {
        if (updaterFile == null)
            throw new NullPointerException("The downloadUpdater must be called before using this method. Alternate use the #update(updaterFile, url, newFile) method.");

        update(updaterFile, url, newFile, restart);
    }

    /**
     * Updating your old java program file with the newest jar file
     *
     * @param updaterFile the downloaded Updater file path
     * @param url         the url of your java program to download and replace it
     * @param newFile     the new jar file of your program to replace your old jar
     * @throws IOException calls if the java -jar syntax command can not be called
     * @apiNote use the {@link #update(String, File)} method if you already downloaded the updater with the {@link #downloadUpdater(File, Consumer)} method
     * @see #update(File, File, String, File, boolean)
     */
    public static void update(File updaterFile, String url, File newFile) throws IOException {
        update(updaterFile, getJarPath(), url, newFile, false);
    }


    /**
     * Updating your old java program file with the newest jar file
     *
     * @param updaterFile the downloaded Updater file path
     * @param url         the url of your java program to download and replace it
     * @param newFile     the new jar file of your program to replace your old jar
     * @param restart     if the jar should be executed after updating
     * @throws IOException calls if the java -jar syntax command can not be called
     * @apiNote use the {@link #update(String, File)} method if you already downloaded the updater with the {@link #downloadUpdater(File, Consumer)} method
     * @see #update(File, File, String, File, boolean)
     */
    public static void update(File updaterFile, String url, File newFile, boolean restart) throws IOException {
        update(updaterFile, getJarPath(), url, newFile, restart);
    }

    /**
     * Updating your old java program file with the newest jar file
     * Using {@link System#getProperty(String)} method to get the java bin path
     *
     * @param updaterFile the downloaded Updater path file
     * @param oldFile     the jar file of your java program to replace it
     * @param url         the url of your java program to download and replace it
     * @param newFile     the new jar file of your program to replace your old jar
     * @param restart     if the jar should be executed after updating
     * @throws IOException calls if the java -jar syntax command can not be called
     * @see <a href="https://github.com/ZeusSeinGrossopa/UpdaterAPI#run-parameters">here</a> for information about to execute paramets of the UpdaterAPI file
     */
    public static void update(File updaterFile, File oldFile, String url, File newFile, boolean restart) throws IOException {
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", updaterFile.getAbsolutePath(), url, oldFile.getAbsolutePath(), newFile.getAbsolutePath(), restart ? "true" : "");

        if (autoDelete) {
            autoDelete = false;
            downloadUpdater(updaterFile, file -> {
                try {
                    builder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            autoDelete = true;
        } else {
            builder.start();
        }
    }

    /**
     * Compares two versions
     *
     * @param version1 the current version of your java program
     * @param version2 the newest version of your java program
     * @return if the version2 is newer than version1
     * @see #compareVersions(String, String)
     */
    public static boolean needUpdate(String version1, String version2) {
        return compareVersions(version1, version2) == -1;
    }

    /**
     * Compares two versions
     *
     * @param version1 the current version of your java program
     * @param version2 the newest version of your java program
     * @return if the version2 is newer than version1
     * @see Integer#compare(int, int)
     */
    public static int compareVersions(String version1, String version2) {
        String[] levels1 = version1.split("\\.");
        String[] levels2 = version2.split("\\.");

        int length = Math.max(levels1.length, levels2.length);
        for (int i = 0; i < length; i++) {
            Integer v1 = i < levels1.length ? Integer.parseInt(levels1[i]) : 0;
            Integer v2 = i < levels2.length ? Integer.parseInt(levels2[i]) : 0;
            int compare = v1.compareTo(v2);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    /**
     * @return the file path of this running jar
     */
    public static File getJarPath() {
        if (jarPath == null) {
            try {
                return new File(UpdaterAPI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsoluteFile();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return jarPath;
    }

    /**
     * Use this method that the Updater is only available in the directory, when the program has a new version to
     * update. Default is that the updater is always available in the folder.
     *
     * @param value the value to set
     * @see #downloadUpdater(File, Consumer)
     */
    public static void setAutoDelete(boolean value) {
        autoDelete = value;
    }

    /**
     * Returning the downloaded updater. The variable is only variable after the {@link #downloadUpdater(File, Consumer)} method is called
     *
     * @return the downloaded updater
     */
    public static File getCurrentUpdater() {
        return updaterFile;
    }
}