package de.zeus.merger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiNew extends Application {

    private static GuiNew instance;

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        System.setProperty("prism.lcdtext", "false");
//        System.out.println(getClass().getResource("/gui/logo.png") == null);

        Parent root = FXMLLoader.load(getClass().getResource("/gui/main.fxml"));

        Scene scene = new Scene(root);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("WorldMerger");
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/logo.png")));
        stage.show();
    }

    public static GuiNew getInstance() {
        return instance;
    }
}