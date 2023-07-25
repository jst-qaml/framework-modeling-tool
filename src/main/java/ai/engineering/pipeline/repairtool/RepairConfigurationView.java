package ai.engineering;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.view.IDiagramViewManager;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.view.IEntitySelectionListener;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;

import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.change_vision.jude.api.inf.presentation.IPresentation;

public class RepairConfigurationView extends JPanel implements IPluginExtraTabView, ProjectEventListener, ActionListener, IEntitySelectionListener{
    
    String[] labelStrings;
    JComboBox labelList, priorityValueList, preventValuelist;
    JButton saveButton;

    public RepairConfigurationView(){
        labelStrings = VersionFetcher.GetLabels(false);
        setupForm();
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        IDiagramViewManager diagramViewManager = utilities.getDiagramViewManager();
        diagramViewManager.addEntitySelectionListener(this);
    }

    public void entitySelectionChanged(IEntitySelectionEvent e){
        updateFormValue(getSelectedPresentation());
    }

    private IPresentation getSelectedPresentation(){
        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        return toolUtilities.getSelectedPresentation();
    }

    private void setupForm(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        labelList = new JComboBox(labelStrings);
        labelList.setSelectedIndex(0);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(new JLabel("Repaired label:"), gbc);
        gbc.gridx++;
        add(labelList, gbc);

        priorityValueList = new JComboBox(RepairConfiguration.values);
        priorityValueList.setSelectedIndex(0);
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Repair Priority:"), gbc);
        gbc.gridx++;
        add(priorityValueList, gbc);

        preventValuelist = new JComboBox(RepairConfiguration.values);
        preventValuelist.setSelectedIndex(0);
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Prevent Degradation:"), gbc);
        gbc.gridx++;
        add(preventValuelist, gbc);

        saveButton = new JButton("Save configuration");
        saveButton.addActionListener(this);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(saveButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e){   
     	safeNewConfig();
        RepairConfigurationSummaryView.updateTable();
    }

    private void updateFormValue(IPresentation selectedPresentation){
        LabelConfiguration labelConfig = RepairConfiguration.findLabelConfiguration(selectedPresentation);

        if (labelConfig == null) {
            labelList.setSelectedIndex(0);
            priorityValueList.setSelectedIndex(0);
            preventValuelist.setSelectedIndex(0);
        } else {
            labelList.setSelectedIndex(Integer.parseInt(labelConfig.getLabel()));
            priorityValueList.setSelectedIndex(Integer.parseInt(labelConfig.getRepairPriority()));
            preventValuelist.setSelectedIndex(Integer.parseInt(labelConfig.getPreventDegradation()));
        }

    }

    private void safeNewConfig(){
        LabelConfiguration labelConfig = new LabelConfiguration(getSelectedPresentation(), labelList.getSelectedIndex()+"", priorityValueList.getSelectedItem().toString(), preventValuelist.getSelectedItem().toString());

        RepairConfiguration.addConfiguration(labelConfig);
    }

    @Override
    public void projectChanged(ProjectEvent e) {}
 
    @Override
    public void projectClosed(ProjectEvent e) {}
 
    @Override
    public void projectOpened(ProjectEvent e) {}

    @Override
    public void addSelectionListener(ISelectionListener listener) {}
 
    @Override
    public String getTitle() {return "Repair Configuration View";}

    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Repair Configuration View Class";}

    public void activated() {}
   
    public void deactivated() {}

}
