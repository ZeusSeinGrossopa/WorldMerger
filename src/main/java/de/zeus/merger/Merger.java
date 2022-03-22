package de.zeus.merger;

import java.io.File;

public interface Merger {
    boolean mergeWorld(File from, File destination, String worldName);

    boolean checkValid(File from);
}