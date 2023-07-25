package ai.engineering;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrainPanel extends JPanel{

    private JComboBox trainingDataVersionBox, modelTypeBox;
    private JTextField versionNameField, batchSizeField, epochsField, validationSplitField;

    public TrainPanel(){
        createTrainTab();
    }

    private void createTrainTab(){

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // String[][] versionMap = VersionFetcher.GetVersions("data");
        // String[] versionList = versionMap[1];
        // trainingDataVersionBox = new JComboBox(versionList);

        versionNameField = new JTextField();

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        add(new JLabel("Version name: "), gbc);
        gbc.gridx = 1;
        add(versionNameField, gbc);

        // gbc.anchor = GridBagConstraints.WEST;
        // gbc.gridy++;
        // gbc.gridx = 0;
        // add(new JLabel("Training dataset: "), gbc);
        // gbc.gridx = 1;
        // add(trainingDataVersionBox, gbc);

        String[] dnnModelString = {"Base CNN model", "VGG19 Fine Tuning Model", "VGG16 Fine Tuning", "ViT", "Hydra", "Hydra Head", "Keras App"};
        modelTypeBox = new JComboBox<>(dnnModelString);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("DNN size: "), gbc);
        gbc.gridx = 1;
        add(modelTypeBox, gbc);

        batchSizeField = new JTextField("32");

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Batch size: "), gbc);
        gbc.gridx = 1;
        add(batchSizeField, gbc);

        epochsField = new JTextField("5");

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Epochs: "), gbc);
        gbc.gridx = 1;
        add(epochsField, gbc);

        validationSplitField = new JTextField("0.2");

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Validation split: "), gbc);
        gbc.gridx = 1;
        add(validationSplitField, gbc);

        JButton trainButton = new JButton("Train Model");
        trainButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
              trainModel();
            } 
          }
        );        
        
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        add(trainButton, gbc);
    }

    private void trainModel(){
        //String datasetVersion = trainingDataVersionBox.getSelectedItem().toString();
        
        JOptionPane.showMessageDialog(this, "Now training on the server...");

        String experimentName = versionNameField.getText();
        String dnnModel = modelTypeBox.getSelectedItem().toString();
        dnnModel = dnnModel.replace(' ', '_');
        dnnModel = dnnModel.toLowerCase();

        String batchSize = batchSizeField.getText();
        String epoch = epochsField.getText();
        String validationSplit = validationSplitField.getText();

        VersionFetcher.TrainNewModel(experimentName, "dummy", dnnModel, batchSize, epoch, validationSplit);

        //ModelTrainer.TestPanel.updateList();
    }

    public void updateList(){
        // String[][] versionMap = VersionFetcher.GetVersions("data");
        // String[] dataVersionList = versionMap[1];
        // DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(dataVersionList);
        // trainingDataVersionBox.setModel(model);
    }

}
