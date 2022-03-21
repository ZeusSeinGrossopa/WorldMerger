package de.zeus.merger;

import javax.swing.*;

public class WorldMergerGui2 extends JFrame {
    private JButton startButton;
    private JPanel WorldMerger;
//    private JProgressBar progressBar;
    private JTextField worldName;

    public WorldMergerGui2() {
        setContentPane(WorldMerger);
        setTitle("WorldMerger");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        startButton.addActionListener(e -> {
            String worldName = this.worldName.getText();

            if(worldName != null && !worldName.isEmpty()) {
                de.zeus.merger.WorldMerger.getInstance().start(worldName);
            } else {
                de.zeus.merger.WorldMerger.getInstance().error("Please enter something in the text field!");
            }
        });

    }

    private void createUIComponents() {

    }
}
