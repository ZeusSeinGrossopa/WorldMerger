package de.zeus.merger.controller;

import com.gluonhq.charm.glisten.control.TextField;
import com.sun.javafx.PlatformUtil;
import de.zeus.merger.SearchGui;
import de.zeus.merger.Utils;
import de.zeus.merger.WorldMerger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static MainController instance;
    @FXML
    public Button startButton;
    @FXML
    public TextField textfield;
    @FXML
    public TextField textfieldPath;
    @FXML
    public Button openButton;

    public static MainController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        this.textfieldPath.setText(WorldMerger.getInstance().getSavePath().getAbsolutePath());

        this.startButton.setOnAction(e -> WorldMerger.getInstance().start(textfield.getText(), textfieldPath.getText()));

        this.openButton.setOnAction(e -> {
            if (PlatformUtil.isWindows()) {
                Utils.betterLaunch(SearchGui.class);
            } else {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }

                JFileChooser guiChooser = new JFileChooser();
                guiChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                guiChooser.setAcceptAllFileFilterUsed(false);

                int returnValue = guiChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    this.textfieldPath.setText(guiChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }

    public TextField getTextfieldPath() {
        return textfieldPath;
    }
}