package de.zeus.merger;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WorldMerger {

    private final File dropFolder;

    private final String[] worldNames = new String[]{"world", "world_nether", "world_the_end"};

    private static WorldMerger instance;
    private static File jarFile;

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

    public WorldMerger(String[] args) {
        instance = this;

        dropFolder = new File(getJarPath() + "/yourworldshere/");
        dropFolder.mkdir();

        new WorldMergerGui();
    }

    public void start(String worldName) { //Multiplayer zu Singleplayer
        if(dropFolder.exists() && dropFolder.listFiles() != null && dropFolder.listFiles().length > 0) {
            if(Arrays.asList(worldNames).equals(Arrays.stream(dropFolder.listFiles()).map(File::getName).collect(Collectors.toList()))) {
                File finalWorld = new File(getJarPath() + "/" + worldName);

                if(finalWorld.exists()) {
                    error("A error occurred! Folder " + worldName + " already exists");
                    return;
                }

                if(!finalWorld.mkdir()) {
                    error("A error occurred! Can not creating folder " + worldName);
                    return;
                }

                File worldFile = new File(dropFolder + "/world/");
                System.out.println(worldFile.getAbsolutePath());
                try {
                    FileUtils.copyDirectory(worldFile, finalWorld);
                } catch (IOException e) {
                    error("A error occurred! Can not copy default world to " + worldName);
                    e.printStackTrace();
                    return;
                }

                File netherFile = new File(dropFolder + "/world_nether/DIM-1/");
                File dim1File = new File(finalWorld + "/DIM-1/");

                try {
                    if(dim1File.exists()) {
                        FileUtils.deleteDirectory(dim1File);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!dim1File.mkdir()) {
                    error("A error occurred! Can not create folder " + dim1File.getName());
                    return;
                } else
                    System.out.println("Created folder " + dim1File.getName());

                try {
                    FileUtils.copyDirectory(netherFile, dim1File);
                } catch (IOException e) {
                    error("A error occurred! Can not copy folder " + netherFile.getName() + " to " + dim1File.getName());
                    e.printStackTrace();
                    return;
                }

                File endFile = new File(dropFolder + "/world_the_end/DIM1/");
                File dim2File = new File(finalWorld + "/DIM1/");

                try {
                    if(dim2File.exists()) {
                        FileUtils.deleteDirectory(dim2File);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!dim2File.mkdir()) {
                    error("A error occurred! Can not create folder " + dim2File.getName());
                    return;
                }

                System.out.println("Created folder " + dim2File.getName());

                try {
                    FileUtils.copyDirectory(endFile, dim2File);
                } catch (IOException e) {
                    error("A error occurred! Can not copy folder " + endFile.getName() + " to " + dim2File.getName());
                    e.printStackTrace();
                    return;
                }

                int input = JOptionPane.showOptionDialog(null, "Done!", "ServerMerger", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (input == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            } else {
                error("Please use " + Arrays.toString(worldNames));
            }
        } else {
            error("Please copy the worlds in the " + dropFolder.getName() + " folder");
        }
    }

    public void error(String message) {
        int error = JOptionPane.showOptionDialog(null, message, "ServerMerger", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        if(error == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public static File getJarPath() {
        if(jarFile == null) {
            try {
                return (jarFile = new File(WorldMerger.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile());
            } catch (URISyntaxException e) {
                return new File("");
            }
        }
        return jarFile;
    }

    public File getDropFolder() {
        return dropFolder;
    }

    public String[] getWorldNames() {
        return worldNames;
    }

    public static WorldMerger getInstance() {
        return instance;
    }
}