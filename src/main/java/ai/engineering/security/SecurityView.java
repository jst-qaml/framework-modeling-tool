package ai.engineering.security;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class SecurityView extends JPanel implements IPluginExtraTabView, ProjectEventListener {

    private JButton selectFolderButton;
    private JButton legitButton;
    private JButton susButton;
    private JLabel imagePreview;
    private File[] imageFiles;
    private int currentIndex;

    private double noiseLevel = 800;
    private double changesThisRound = 0.0;

    public SecurityView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(createLabelPane(), BorderLayout.CENTER);
        addProjectEventListener();



        // Initialize components
        selectFolderButton = new JButton("Select Folder");
        legitButton = new JButton("Acceptable");
        susButton = new JButton("Unacceptable");
        imagePreview = new JLabel();
        imageFiles = null;
        currentIndex = -1;

        // Set layout manager
        setLayout(new BorderLayout());

        // Add components to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectFolderButton);
        buttonPanel.add(legitButton);
        buttonPanel.add(susButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(imagePreview, BorderLayout.CENTER);

        // Add action listeners
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });

        legitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markLegit();
            }
        });

        susButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markSus();
            }
        });
    }

    private void addProjectEventListener() {
        try {
            ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
            projectAccessor.addProjectEventListener(this);
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }

    private Container createLabelPane() {
        JLabel label = new JLabel("SecurityView");
        JScrollPane pane = new JScrollPane(label);
        return pane;
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
            File selectedFolder = fileChooser.getSelectedFile();
            imageFiles = selectedFolder.listFiles();
            currentIndex = 0;
            showImage();
        }
        selectFolderButton.setVisible(false);
    }

    private void markSus() {
        changesThisRound -= noiseLevel*0.1;
        mark();
    }

    private void markLegit() {
        changesThisRound += noiseLevel*0.1;
        mark();
    }

    private void mark() {
        if (currentIndex < imageFiles.length - 1) {
            currentIndex++;
            showImage();
        } else {
            if (changesThisRound < 0.02*noiseLevel) {
                showMessageDialog(null, "Finished. Acceptable L2-Norm is: " + noiseLevel + ". TODO enter this value into model and pipeline");
            } else {
                showMessageDialog(null, "Change too great. Noise changed by: " + changesThisRound);
                noiseLevel += changesThisRound;
                changesThisRound = 0;
            }
            currentIndex = 0;
            showImage();
        }
    }

    private void showImage() {
        if (imageFiles != null && currentIndex >= 0 && currentIndex < imageFiles.length) {
            File inputFile = new File(imageFiles[currentIndex].getAbsolutePath());
            try {
                BufferedImage image = ImageIO.read(inputFile);

                // Create a BufferedImage with TYPE_INT_RGB format
                PerturbedImage noisyImage = new PerturbedImage(image, noiseLevel);

                double scalex = (double) 400 / noisyImage.getWidth();
                double scaley = (double) 400 / noisyImage.getHeight();
                double scale = Math.min(scalex, scaley);
                Image scaledNoisyImage = noisyImage.getScaledInstance(
                        (int) (noisyImage.getWidth() * scale),
                        (int) (noisyImage.getHeight() * scale),
                        Image.SCALE_FAST
                );

                ImageIcon imageIcon = new ImageIcon(scaledNoisyImage);
                imagePreview.setIcon(imageIcon);
            } catch (IOException | NullPointerException e) {
                imagePreview.setIcon(null);
            }
        } else {
            imagePreview.setIcon(null);
        }
    }
}
