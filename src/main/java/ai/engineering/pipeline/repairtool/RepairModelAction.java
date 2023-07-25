package ai.engineering;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;

import com.change_vision.jude.api.inf.ui.IWindow;

public class RepairModelAction implements IPluginActionDelegate{
    
    JComboBox versionComboBox;
    JTextField versionNameField;
    
    public Object run(IWindow window){

        String[][] versionMap = VersionFetcher.GetVersions();   
        String[] versionList = versionMap[0];

        versionComboBox = new JComboBox(versionList);
        versionNameField = new JTextField();

        GridBagConstraints gbc = new GridBagConstraints();

        JButton startButton = new JButton("Repair");
        startButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
              repairModel();
            } 
          }
        );

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 10;
        gbc.ipadx = 30;
        panel.add(new JLabel("New model name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        gbc.gridwidth = 2;
        panel.add(versionNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 10;
        gbc.ipadx = 30;
        panel.add(new JLabel("Base model version: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        gbc.gridwidth = 2;
        panel.add(versionComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipady = 10;
        gbc.ipadx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Existing configuration: "), gbc);
        String[] column = {"Element", "Label", "Repair Priority", "Prevent Degradation"};
        JTable configTable = new JTable(RepairConfiguration.generateTableData(), column);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.ipady = 40;
        gbc.ipadx = 0;
        gbc.gridwidth = 3;
        panel.add(new JScrollPane(configTable), gbc);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.ipady = 10;
        gbc.ipadx = 40;
        gbc.gridwidth = 1;
        panel.add(startButton, gbc);

        JDialog dialog = new JDialog(window.getParent(), "Repair existing ML models");
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.NORTH);
        dialog.setVisible(true);

        // Object selectedVersion = JOptionPane.showInputDialog(window.getParent(), "Select base model version", "Repair model", JOptionPane.PLAIN_MESSAGE, null, versionList, versionList[0]);

        // if (selectedVersion != null) {
        //     selectedVersionString = selectedVersion.toString();
        // }

        // repairModel();
        
	    return null;
	}

    private void repairModel(){

      int selectedVersionIndex = versionComboBox.getSelectedIndex();
      String[][] versionMap = VersionFetcher.GetVersions();

      String versionName = versionNameField.getText();

      VersionFetcher.RepairModel(versionName, versionMap[1][selectedVersionIndex]);
    }

}
