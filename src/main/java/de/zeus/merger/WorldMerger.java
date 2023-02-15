package de.zeus.merger;

import de.zeus.merger.types.ServerToSingleplayerMerger;
import de.zeus.merger.types.SingleplayerToServerMerger;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class WorldMerger extends Utils {

    private static WorldMerger instance;
    private final File dropFolder;
    private File jarFile;
    private Merger currentMerger;

    public WorldMerger(String[] args) {
        instance = this;

        dropFolder = new File(getJarPath() + "/yourworldshere/");
        dropFolder.mkdir();

        Updater.start();
        Utils.betterLaunch(GuiNew.class);
    }

    public static void main(String[] args) {
        System.out.println(
                " █████╗  █████╗ ███████╗ █████╗ ███╗  ██╗ ██████╗██████╗ ██╗██████╗ ███████╗\n" +
                        "██╔══██╗██╔══██╗██╔════╝██╔══██╗████╗ ██║██╔════╝██╔══██╗██║██╔══██╗██╔════╝\n" +
                        "██║  ██║██║  ╚═╝█████╗  ███████║██╔██╗██║╚█████╗ ██████╔╝██║██████╔╝█████╗\n" +
                        "██║  ██║██║  ██╗██╔══╝  ██╔══██║██║╚████║ ╚═══██╗██╔═══╝ ██║██╔══██╗██╔══╝\n" +
                        "╚█████╔╝╚█████╔╝███████╗██║  ██║██║ ╚███║██████╔╝██║     ██║██║  ██║███████╗\n" +
                        " ╚════╝  ╚════╝ ╚══════╝╚═╝  ╚═╝╚═╝  ╚══╝╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝"
        );

        new WorldMerger(args);
    }

    public static WorldMerger getInstance() {
        return instance;
    }

    public File getJarPath() {
        if (jarFile == null) {
            try {
                return (jarFile = new File(WorldMerger.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile());
            } catch (URISyntaxException e) {
                return new File(".");
            }
        }
        return jarFile;
    }

    public File getSavePath() {
        return new File(getMinecraftPath() + "/saves/");
    }

    public File getMinecraftPath() {
        String os = System.getProperty("os.name").toLowerCase();
        File finalFile = null;

        if (os.contains("windows")) {
            finalFile = new File(System.getenv("APPDATA") + "/.minecraft/");
        } else if (os.contains("mac")) {
            finalFile = new File(System.getenv("user.home") + "Library/Application Support/minecraft");
        } else if (os.contains("linux") || os.contains("unix")) {
            finalFile = new File(System.getenv("user.home") + '.' + "minecraft" + '/');
        }

        return finalFile;
    }

    public void start(String worldName, String serverPath) {
        if (!isNull(serverPath)) {
            if (dropFolder.exists() && dropFolder.listFiles() != null && Objects.requireNonNull(dropFolder.listFiles()).length > 0) {
                if (Objects.requireNonNull(dropFolder.listFiles()).length == 1) {
                    setCurrentMerger(new SingleplayerToServerMerger());
                } else {
                    setCurrentMerger(new ServerToSingleplayerMerger());
                }

                File finalWorld = new File(getJarPath() + "/" + worldName);
                if (currentMerger instanceof ServerToSingleplayerMerger) {
                    File serverPathFile;

                    if (isNull(serverPath) || !(serverPathFile = new File(serverPath)).exists()) {
                        error("Please enter a correct .minecraft path!", false);
                        return;
                    }

                    finalWorld = new File(serverPathFile + "/" + worldName);
                }

                if (currentMerger != null) {
                    System.out.println("Using " + getCurrentMerger().getClass().getSimpleName() + " merging tool");

                    if (currentMerger.checkValid(dropFolder)) {
                        boolean done = currentMerger.mergeWorld(dropFolder, finalWorld, worldName);
                        if (done) {
                            int input = JOptionPane.showOptionDialog(null, "Done!", "ServerMerger", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            if (input == JOptionPane.OK_OPTION) {
                                System.exit(0);
                            }
                        }
                    }
                } else {
                    error("Can not recognize the worlds", false);
                }
            } else {
                error("Please copy the worlds in the " + dropFolder.getName() + " folder", false);
            }
        } else {
            error("Please enter a name for the world!", false);
        }
    }

    public File getDropFolder() {
        return dropFolder;
    }

    public Merger getCurrentMerger() {
        return currentMerger;
    }

    public void setCurrentMerger(Merger currentMerger) {
        this.currentMerger = currentMerger;
    }
}