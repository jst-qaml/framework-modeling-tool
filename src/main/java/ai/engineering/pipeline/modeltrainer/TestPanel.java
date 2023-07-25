package ai.engineering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestPanel extends JPanel {
 
    private JComboBox testDataVersionBox, modelVersionBox;

    public TestPanel(){
        createTestTab();
    }

    private void createTestTab(){

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // String[][] versionMap = VersionFetcher.GetVersions("models");
        // String[] modelVersionList = versionMap[1];
        // modelVersionBox = new JComboBox(modelVersionList);

        // String[] dataVersionList = versionMap[1];
        // testDataVersionBox = new JComboBox(dataVersionList);

        JButton testButton = new JButton("Test Model");
        testButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
              testModel();
            } 
          }
        );

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(new JLabel("Model version: "), gbc);
        gbc.gridx = 1;
        //add(modelVersionBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Test dataset: "), gbc);
        gbc.gridx = 1;
        //add(testDataVersionBox, gbc);
        
        gbc.gridy++;
        gbc.gridx = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        add(testButton, gbc);
    }

    private void testModel(){
        // String modelVersion = modelVersionBox.getSelectedItem().toString();
        // String datasetVersion = testDataVersionBox.getSelectedItem().toString();
        // VersionFetcher.TestModel(modelVersion, datasetVersion);
    }

    public void updateList(){
        // String[][] versionMap = VersionFetcher.GetVersions("data");
        // String[] modelVersionBox = versionMap[1];
        // DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(modelVersionBox);
        // testDataVersionBox.setModel(model);
    }

}
