package de.zeus.merger.controller;

import com.gluonhq.charm.glisten.control.TextField;
import de.zeus.merger.WorldMerger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public Button startButton;

    @FXML
    public TextField textfield;

    @FXML
    public TextField textfieldPath;

    @FXML
    public Button openButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.textfieldPath.setText(WorldMerger.getSavePath().getAbsolutePath());

        this.startButton.setOnAction(e -> WorldMerger.getInstance().start(textfield.getText(), textfieldPath.getText()));

        this.openButton.setOnAction(e -> {
            JFileChooser guiChooser = new JFileChooser();
            guiChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            guiChooser.setAcceptAllFileFilterUsed(false);

            int returnValue = guiChooser.showOpenDialog(null);

            if(returnValue == JFileChooser.APPROVE_OPTION) {
                this.textfieldPath.setText(guiChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }
}