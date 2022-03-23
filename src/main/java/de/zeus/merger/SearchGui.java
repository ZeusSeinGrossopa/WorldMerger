package de.zeus.merger;

import de.zeus.merger.controller.MainController;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SearchGui extends Application {

    @Override
    public void start(Stage primaryStage) {
        DirectoryChooser chooser = new DirectoryChooser();

        File file = chooser.showDialog(primaryStage);

        chooser.setTitle("Select the folder");

        if(file == null) {
            Utils.error("Please select an correct folder!", false);
            return;
        }
        MainController.getInstance().getTextfieldPath().setText(file.getAbsolutePath());
    }
}