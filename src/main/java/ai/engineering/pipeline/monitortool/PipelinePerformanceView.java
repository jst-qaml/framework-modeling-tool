package ai.engineering;

import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.gsn.editor.*;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;
import com.change_vision.jude.api.inf.view.IDiagramViewManager;

import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.project.ProjectEvent;

import com.change_vision.jude.api.inf.model.*;

import com.change_vision.jude.api.inf.editor.ITransactionManager;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import com.change_vision.jude.api.inf.view.IEntitySelectionListener;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;

import com.change_vision.jude.api.gsn.model.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;
import java.util.List;

public class PipelinePerformanceView extends JPanel implements IPluginExtraTabView, ProjectEventListener, ActionListener, IEntitySelectionListener{
    
    String[] labelStrings;
    String[] metricsStrings  = {"Accuracy", "Precision", "Recall", "Misclassification", "IoU"};
    String[] desiredValueStrings = {"Very Low", "Low", "Medium", "High", "Very High", "Specific Threshold"};
    JComboBox labelList, metricsList, desiredValueList, misclassificationList;
    JTextField desiredValueField, actualValueField;
    JButton saveButton;
    JLabel conclusionLabel;

    public PipelinePerformanceView(){
        labelStrings = VersionFetcher.GetLabels(true);
        MonitoringConfigurations.initializeConfigurations();
        initiateForm();
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        IDiagramViewManager diagramViewManager = utilities.getDiagramViewManager();
        diagramViewManager.addEntitySelectionListener(this);
    }

    public void entitySelectionChanged(IEntitySelectionEvent e){
        parseGoalInfo();
        setupForm();
    }

    private void initiateForm(){
        setLayout(new GridBagLayout());

        labelList = new JComboBox(labelStrings);
        labelList.setSelectedIndex(0);

        misclassificationList = new JComboBox(labelStrings);
        misclassificationList.setSelectedIndex(0);

        desiredValueList = new JComboBox(desiredValueStrings);
        desiredValueList.setSelectedIndex(0);
        desiredValueList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setupForm();
            }
        });

        desiredValueField = new JTextField("90.00");

        actualValueField = new JTextField();
        actualValueField.setEditable(false);

        conclusionLabel = new JLabel("Save the configuration and run the performance fetch to compare result");

        saveButton = new JButton("Save setup");
        saveButton.addActionListener(this);

        conclusionLabel = new JLabel();

        metricsList = new JComboBox(metricsStrings);
        metricsList.setSelectedIndex(0);
        metricsList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setupForm();
            }
        });

        setupForm();
    }

    private IPresentation getSelectedPresentation(){
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        return utilities.getSelectedPresentation();
    }

    private boolean isOnMisrecognize(){
        int selectedIndex = metricsList.getSelectedIndex();
        return selectedIndex == 3;
    }

    private boolean isDesiredOnSpecificThreshold(){
        int selectedIndex = desiredValueList.getSelectedIndex();
        return selectedIndex == 5;
    }

    private void setupForm(){
        this.removeAll();

        if(getSelectedPresentation() == null){
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        add(new JLabel("Monitored Label's Performance:"), gbc);
        gbc.gridx++;
        add(labelList, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Monitored Performance Metrics:"), gbc);
        gbc.gridx++;
        add(metricsList, gbc);

        if (isOnMisrecognize()) {    
            gbc.gridy++;
            gbc.gridx = 0;
            add(new JLabel("Misrecognized as:"), gbc);
            gbc.gridx++;
            add(misclassificationList, gbc);

            gbc.gridy++;
            gbc.gridx = 0;
            add(new JLabel("Desired Maximum Value Range:"), gbc);
            gbc.gridx++;
            add(desiredValueList, gbc);
        }else{
            gbc.gridy++;
            gbc.gridx = 0;
            add(new JLabel("Desired Minimum Value Range:"), gbc);
            gbc.gridx++;
            add(desiredValueList, gbc);
        }       

        if (isDesiredOnSpecificThreshold()){
            gbc.gridy++;
            gbc.gridx = 0;
            add(new JLabel("Specific Threshold Value:"), gbc);
            gbc.gridx++;
            add(desiredValueField, gbc);
        }

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Actual Performance:"), gbc);
        gbc.gridx++;
        add(actualValueField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(conclusionLabel, gbc);

        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(saveButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e){   
     	safeNewDesiredPerformance();
        addAssumptionNode();
        MonitoringSummaryView.updateTable();
    }

    private void parseGoalInfo(){
        IPresentation currentPresentation = getSelectedPresentation();

        if (currentPresentation == null) {
            return;
        }

        if (currentPresentation.getModel() instanceof IGoal) {
            IGoal goal = (IGoal) currentPresentation.getModel();
            if(GoalParser.isParsable(goal)){
                DesiredPerformance performance = GoalParser.parseGoal(goal);
                System.out.println(performance.getLabel());
                MonitoringConfigurations.addDesiredPerformance(performance);
                updateFormValue();
                MonitoringSummaryView.updateTable();
            }                    
        }
    }

    private void updateFormValue(){

        IPresentation selectedPresentation = getSelectedPresentation();

        DesiredPerformance desiredPerformance = MonitoringConfigurations.findDesiredPerformance(selectedPresentation);
    
        try {
            if (desiredPerformance == null) {
                labelList.setSelectedIndex(0);
                metricsList.setSelectedIndex(0);
                desiredValueList.setSelectedIndex(0);
                desiredValueField.setText("90.00");
            }else{
    
                String label = desiredPerformance.getLabel();
                Integer index = 0;
    
                if(!label.equalsIgnoreCase("overall")){
                    index = Integer.parseInt(label)+1;
                }
    
                labelList.setSelectedIndex(index);
                metricsList.setSelectedItem(desiredPerformance.getMetricsType());
                desiredValueField.setText(desiredPerformance.getDesiredValue()+"");
    
                if(!desiredPerformance.isTested()){
                    actualValueField.setText("");
                    conclusionLabel.setText("Not tested yet.");
                }else{
                    actualValueField.setText(desiredPerformance.getRealPerformance()+"");
    
                    if (desiredPerformance.isSatisfying()) {
                        conclusionLabel.setText("Model performance satisfy this element");
                    } else {
                        conclusionLabel.setText("Model performance did not satisfy this element");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        
    }

    private void safeNewDesiredPerformance()
    {
        int selectedMetricsIndex = metricsList.getSelectedIndex();
        
        int selectedLabelIndex = labelList.getSelectedIndex()-1;
        String index = selectedLabelIndex+"";
        
        if(selectedLabelIndex == -1){
            index = "overall";
        }

        IPresentation selectedPresentation = getSelectedPresentation();

        if(selectedPresentation.getModel() instanceof IGoal){
            IGoal monitoredEntity = (IGoal) selectedPresentation.getModel();

            DesiredPerformance newDesiredPerformance;

            if (isOnMisrecognize()) {

                int misclassificationListIndex = misclassificationList.getSelectedIndex()-1;
                String targetIndex = misclassificationListIndex+"";
        
                if(misclassificationListIndex == -1){
                    targetIndex = "overall";
                }

                if (isDesiredOnSpecificThreshold()) {
                    newDesiredPerformance = new MisclassificationPerformance(monitoredEntity, index, targetIndex, Float.parseFloat(desiredValueField.getText()), "");
                }else{
                    newDesiredPerformance = new MisclassificationPerformance(monitoredEntity, index, targetIndex, -1.0f, desiredValueList.getSelectedItem().toString());
                }
            } else {
                if (isDesiredOnSpecificThreshold()) {
                    newDesiredPerformance = new ConfusionMetricsPerformance(monitoredEntity, index, metricsStrings[selectedMetricsIndex], Float.parseFloat(desiredValueField.getText()), "");
                }else{
                    newDesiredPerformance = new ConfusionMetricsPerformance(monitoredEntity, index, metricsStrings[selectedMetricsIndex], -1.0f, desiredValueList.getSelectedItem().toString());
                }
            }

            MonitoringConfigurations.addDesiredPerformance(newDesiredPerformance);

            GoalParser.parseGoal(monitoredEntity);
        }
    }

    private void addAssumptionNode(){
        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        ProjectAccessor projectAccessor = toolUtilities.getProjectAccessor();
        ITransactionManager transactionManager = toolUtilities.getTransactionManager();
        IPresentation selectedPresentation = getSelectedPresentation();

        try {
            transactionManager.beginTransaction();

            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            IFacet facet = projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
            IModule module = facet.getRootElement(IModule.class);

            GsnDiagramEditor diagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);
            IGsnDiagram diagram = (IGsnDiagram) selectedPresentation.getDiagram();
            diagramEditor.setDiagram(diagram);

            IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
            GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);

            INodePresentation selectedNodePresentation = (INodePresentation) selectedPresentation;
            Point2D selectedPresentationLocation = selectedNodePresentation.getLocation();
            Point2D assumptionNodeLocation = new Point((int) selectedPresentationLocation.getX(), (int) (selectedPresentationLocation.getY() + 100));

            IAssumption assumption = modelEditor.createAssumption(module, selectedPresentation.getLabel());
            assumption.setContent("To be updated with a baseline experiment.");
            INodePresentation assumptionPresentation = diagramEditor.createNodePresentation(assumption, assumptionNodeLocation);
            
            IInContextOf connection = modelEditor.createInContextOf((IArgumentAsset) selectedPresentation.getModel(), assumption);
            diagramEditor.createLinkPresentation(connection, selectedNodePresentation, assumptionPresentation);

            transactionManager.endTransaction();  
        } catch (Exception e) {
            System.err.println("Assumption Generation Failed");
            System.err.println(e);
        }

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
    public String getTitle() {return "Pipeline Performance View";}

    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Pipeline Performance View Class";}

    public void activated() {}
   
    public void deactivated() {}


}
