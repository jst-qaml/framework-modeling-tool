package ai.engineering.pipeline.monitortool.robustness;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;

public class RobustnessWizard extends JFrame {
    private final JLabel imagePreview;
    private File[] imageFiles;
    private int currentIndex;
    private double noiseLevel = 800;
    private double changesThisRound = 0.0;
    public RobustnessWizard() {
        // Set the title of the window
        setTitle("Robustness Wizard");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold components
        JPanel panel = new JPanel();

        // Create a button and add it to the panel
        imageFiles = null;
        currentIndex = -1;

        // init layout
        panel.setLayout(new BorderLayout());

        // add buttons for selecting images and accepting/rejecting
        JButton selectFolderButton = new JButton("Select Folder");
        selectFolderButton.addActionListener(e -> selectFolder());
        JButton acceptButton = new JButton("Acceptable");
        acceptButton.addActionListener(e -> acceptNoise());
        JButton rejectButton = new JButton("Unacceptable");
        rejectButton.addActionListener(e -> rejectNoise());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectFolderButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        // add image preview pane
        imagePreview = new JLabel();
        imagePreview.setOpaque(true);
        imagePreview.setHorizontalAlignment(JLabel.CENTER);
        imagePreview.setVerticalAlignment(JLabel.CENTER);
        panel.add(imagePreview, BorderLayout.CENTER);

        // Add the panel to the content pane of the frame
        getContentPane().add(panel);

        // Set the size of the window
        setSize(300, 200);

        // Set the window to be visible
        setVisible(true);
    }


    private void selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imageFiles = fileChooser.getSelectedFile().listFiles();
            currentIndex = 0;
            showImage();
        }
    }

    private void acceptNoise() {
        changesThisRound += noiseLevel * 0.1;
        mark();
    }

    private void rejectNoise() {
        changesThisRound -= noiseLevel * 0.1;
        mark();
    }

    private void mark() {
        if (currentIndex < imageFiles.length - 1) {
            // round is still ongoing
            currentIndex++;
            showImage();
        } else {
            // end of round reached
            if (changesThisRound < 0.02 * noiseLevel) {
                showMessageDialog(null, "Finished. Acceptable L2-Norm is: " + noiseLevel + ".");
            } else {
                noiseLevel += changesThisRound;
                showMessageDialog(null, "Change too great. Noise changed by: " + changesThisRound);
            }

            // start new round
            changesThisRound = 0;
            currentIndex = 0;
            showImage();
        }
    }

    private void showImage() {
        ImageIcon scaledNoisyImage = null;
        try {
            File inputFile = new File(imageFiles[currentIndex].getAbsolutePath());
            PerturbedImage noisyImage = new PerturbedImage(ImageIO.read(inputFile), noiseLevel);
            double scaleX = (double) 400 / noisyImage.getWidth();
            double scaleY = (double) 400 / noisyImage.getHeight();
            double scale = Math.min(scaleX, scaleY);
            scaledNoisyImage = new ImageIcon(noisyImage.getScaledInstance(
                    (int) (noisyImage.getWidth() * scale),
                    (int) (noisyImage.getHeight() * scale),
                    Image.SCALE_FAST
            ));
        } catch (IOException | ArrayIndexOutOfBoundsException ignored) {
        }
        imagePreview.setIcon(scaledNoisyImage);
    }
}
