package de.zeus.merger.types;

import de.zeus.merger.Merger;
import de.zeus.merger.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerToSingleplayerMerger extends Utils implements Merger {

    private final String[] worldNames = new String[]{"world", "world_nether", "world_the_end"};

    @Override
    public boolean mergeWorld(File dropFolder, File finalWorld, String worldName) {
        if (finalWorld.exists()) {
            error("A error occurred! Folder " + worldName + " already exists", false);
            return false;
        }

        if (!finalWorld.mkdir()) {
            error("A error occurred! Can not creating folder " + worldName);
            return false;
        }

        File worldFile = new File(dropFolder + "/world/");

        try {
            FileUtils.copyDirectory(worldFile, finalWorld);
        } catch (IOException e) {
            error("A error occurred! Can not copy default world to " + worldName);
            e.printStackTrace();
            return false;
        }

        File netherFile = new File(dropFolder + "/world_nether/DIM-1/");
        File dim1File = new File(finalWorld + "/DIM-1/");

        try {
            if (dim1File.exists()) {
                FileUtils.deleteDirectory(dim1File);
            }
        } catch (IOException e) {
            error("A error occurred! Can not delete folder " + dim1File.getName());
            e.printStackTrace();
            return false;
        }

        if (!dim1File.mkdir()) {
            error("A error occurred! Can not create folder " + dim1File.getName());
            return false;
        } else
            System.out.println("Created folder " + dim1File.getName());

        try {
            FileUtils.copyDirectory(netherFile, dim1File);
        } catch (IOException e) {
            error("A error occurred! Can not copy folder " + netherFile.getName() + " to " + dim1File.getName());
            e.printStackTrace();
            return false;
        }

        File endFile = new File(dropFolder + "/world_the_end/DIM1/");
        File dim2File = new File(finalWorld + "/DIM1/");

        try {
            if (dim2File.exists()) {
                FileUtils.deleteDirectory(dim2File);
            }
        } catch (IOException e) {
            error("A error occurred! Can not delete folder " + dim2File.getName());
            e.printStackTrace();
            return false;
        }

        if (!dim2File.mkdir()) {
            error("A error occurred! Can not create folder " + dim2File.getName());
            return false;
        }
        System.out.println("Created folder " + dim2File.getName());

        try {
            FileUtils.copyDirectory(endFile, dim2File);
        } catch (IOException e) {
            error("A error occurred! Can not copy folder " + endFile.getName() + " to " + dim2File.getName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkValid(File dropFolder) {
        boolean valid = Arrays.asList(worldNames).equals(Arrays.stream(Objects.requireNonNull(dropFolder.listFiles())).map(File::getName).collect(Collectors.toList()));

        if(!valid)
            error("Please use " + Arrays.toString(worldNames), false);

        return valid;
    }
}