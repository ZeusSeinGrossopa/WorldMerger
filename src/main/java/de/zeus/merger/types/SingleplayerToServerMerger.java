package de.zeus.merger.types;

import de.zeus.merger.Merger;
import de.zeus.merger.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SingleplayerToServerMerger extends Utils implements Merger {

    @Override
    public boolean mergeWorld(File from, File destination, String worldName) {
        if (destination.exists()) {
            error("A error occurred! Folder " + worldName + " already exists", false);
            return false;
        }

        if (!destination.mkdir()) {
            error("A error occurred! Can not creating folder " + worldName);
            return false;
        }

        File defaultWorld = new File(destination + "/world/");
        File worldFile = new File(from + "/" + Objects.requireNonNull(from.listFiles())[0].getName());

        if(!defaultWorld.mkdir()) {
            error("A error occurred! Can not create folder " + defaultWorld.getName());
            return false;
        }

        try {
            FileUtils.copyDirectory(worldFile, defaultWorld);
        } catch (IOException e) {
            error("A error occurred! Can not copy default world to " + worldName);
            e.printStackTrace();
            return false;
        }

        File DIM1World = new File(defaultWorld + "/DIM-1");
        File DIM2World = new File(defaultWorld + "/DIM1");

        File iconFile = new File(defaultWorld + "/icon.png");
        File lockFile = new File(defaultWorld + "/session.lock");

        try {
            if (DIM1World.exists()) FileUtils.deleteDirectory(DIM1World);
            if (DIM2World.exists()) FileUtils.deleteDirectory(DIM2World);

            if(iconFile.exists()) FileUtils.deleteQuietly(iconFile);
            if(lockFile.exists()) FileUtils.deleteQuietly(lockFile);
        } catch (IOException e) {
            error("A error occurred! Can not delete folders " + DIM1World.getName() + " & " + DIM2World.getName());
            e.printStackTrace();
            return false;
        }

        System.out.println("Copied world to " + destination.getName() + " folder.");

        File DIM1 = new File(worldFile + "/DIM-1/");
        File DIM2 = new File(worldFile + "/DIM1/");

        File netherWorld = new File(destination + "/world_nether/DIM-1/");
        File endWorld = new File(destination + "/world_the_end/DIM1/");

        if(!netherWorld.mkdirs()) {
            error("A error occurred! Can not create folder " + netherWorld.getName());
            return false;
        }

        if(!endWorld.mkdirs()) {
            error("A error occurred! Can not create folder " + endWorld.getName());
            return false;
        }

//        if((!DIM1.exists() && !DIM1.mkdir()) || (DIM2.exists() && !DIM2.mkdir())) {
//            WorldMerger.error("A error occurred! Can not create folders " + netherWorld.getName() + " & " + endWorld.getName());
//            return false;
//        }

        try {
            if(DIM1.exists()) FileUtils.copyDirectory(DIM1, netherWorld);
            if(DIM2.exists()) FileUtils.copyDirectory(DIM2, endWorld);
        } catch (IOException e) {
            error("A error occurred! Can not copy folders " + netherWorld.getName() + " & " + endWorld.getName());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean checkValid(File from) {
        boolean valid = Objects.requireNonNull(from.listFiles()).length == 1;

        if(!valid)
            error("Please put a singleplayer world in the " + from.getName() + " folder!", false);

        return valid;
    }
}