package ai.engineering.security;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class SecurityView extends JPanel implements IPluginExtraTabView, ProjectEventListener {
    private JLabel imagePreview;
    private File[] imageFiles;
    private int currentIndex;
    private double noiseLevel = 800;
    private double changesThisRound = 0.0;

    public SecurityView() {
        initComponents();
    }

    private void initComponents() {
        // init business logic
        imageFiles = null;
        currentIndex = -1;

        // init layout
        setLayout(new BorderLayout());

        // add image preview pane
        imagePreview = new JLabel();
        add(imagePreview, BorderLayout.CENTER);

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
        add(buttonPanel, BorderLayout.NORTH);
    }

    @Override
    public void projectChanged(ProjectEvent e) {
    }

    @Override
    public void projectClosed(ProjectEvent e) {
    }

    @Override
    public void projectOpened(ProjectEvent e) {
    }

    @Override
    public void addSelectionListener(ISelectionListener listener) {
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Show SecurityView here";
    }

    @Override
    public String getTitle() {
        return "Security View";
    }

    public void activated() {
    }

    public void deactivated() {
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
                showMessageDialog(null, "Finished. Acceptable L2-Norm is: " + noiseLevel + ". TODO enter this value into model and pipeline");
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
