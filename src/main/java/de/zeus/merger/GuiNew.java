package de.zeus.merger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Objects;

public class GuiNew extends Application {

    private static GuiNew instance;

    public static GuiNew getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        System.setProperty("prism.lcdtext", "false");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/main.fxml")));

        Scene scene = new Scene(root);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("WorldMerger");
        stage.show();

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> System.exit(0));
    }
}