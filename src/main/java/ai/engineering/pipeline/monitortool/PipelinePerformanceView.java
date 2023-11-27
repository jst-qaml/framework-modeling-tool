package ai.engineering.pipeline.monitortool;

import ai.engineering.pipeline.VersionFetcher;
import ai.engineering.pipeline.monitortool.DesiredPerformance.*;
import ai.engineering.utilities.ToolUtilities;
import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;
import com.change_vision.jude.api.inf.view.IDiagramViewManager;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;
import com.change_vision.jude.api.inf.view.IEntitySelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PipelinePerformanceView extends JPanel implements IPluginExtraTabView, ProjectEventListener, ActionListener, IEntitySelectionListener {

    String[] labelStrings;
    JComboBox labelList, metricsList, misclassificationList;
    JTextField desiredValueField, actualValueField;
    JButton saveButton;
    JLabel conclusionLabel;

    public PipelinePerformanceView() {
        labelStrings = VersionFetcher.GetLabels(true);
        MonitoringConfigurations.initializeConfigurations();
        initiateForm();
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        IDiagramViewManager diagramViewManager = utilities.getDiagramViewManager();
        diagramViewManager.addEntitySelectionListener(this);
    }

    public void entitySelectionChanged(IEntitySelectionEvent e) {
        parseGoalInfo();
        setupForm();
    }

    private void initiateForm() {
        setLayout(new GridBagLayout());

        labelList = new JComboBox(labelStrings);
        labelList.setSelectedIndex(0);

        misclassificationList = new JComboBox(labelStrings);
        misclassificationList.setSelectedIndex(0);

        desiredValueField = new JTextField("90.00");

        actualValueField = new JTextField();
        actualValueField.setEditable(false);

        conclusionLabel = new JLabel("Save the configuration and run the performance fetch to compare result");

        saveButton = new JButton("Save setup");
        saveButton.addActionListener(this);

        conclusionLabel = new JLabel();

        metricsList = new JComboBox(Metric.values());
        metricsList.setSelectedIndex(0);
        metricsList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setupForm();
            }
        });

        setupForm();
    }

    private IPresentation getSelectedPresentation() {
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        return utilities.getSelectedPresentation();
    }

    private boolean isOnMisrecognize() {
        int selectedIndex = metricsList.getSelectedIndex();
        return selectedIndex == 3;
    }

    private void setupForm() {
        this.removeAll();

        if (getSelectedPresentation() == null) {
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
            add(new JLabel("Desired Maximum Value:"), gbc);
            gbc.gridx++;
            add(desiredValueField, gbc);
        } else {
            gbc.gridy++;
            gbc.gridx = 0;
            add(new JLabel("Desired Minimum Value:"), gbc);
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
    public void actionPerformed(ActionEvent e) {
        safeNewDesiredPerformance();
        MonitoringSummaryView.updateTable();
    }

    private void parseGoalInfo() {
        IPresentation currentPresentation = getSelectedPresentation();

        if (currentPresentation == null) {
            return;
        }

        if (currentPresentation.getModel() instanceof IGoal) {
            IGoal goal = (IGoal) currentPresentation.getModel();
            if (GoalParser.isParsable(goal)) {
                DesiredPerformance performance = GoalParser.parseGoal(goal);
                System.out.println(performance.getLabel());
                MonitoringConfigurations.addDesiredPerformance(performance);
                updateFormValue();
                MonitoringSummaryView.updateTable();
            }
        }
    }

    private void updateFormValue() {

        IPresentation selectedPresentation = getSelectedPresentation();

        DesiredPerformance desiredPerformance = MonitoringConfigurations.findDesiredPerformance(selectedPresentation);

        try {
            if (desiredPerformance == null) {
                labelList.setSelectedIndex(0);
                metricsList.setSelectedIndex(0);
                desiredValueField.setText("90.00");
            } else {

                String label = desiredPerformance.getLabel();
                int index = 0;

                if (!label.equalsIgnoreCase("overall")) {
                    index = Integer.parseInt(label) + 1;
                }

                labelList.setSelectedIndex(index);
                metricsList.setSelectedItem(desiredPerformance.getMetricsType());
                desiredValueField.setText(desiredPerformance.getDesiredValue() + "");

                if (!desiredPerformance.isTested()) {
                    actualValueField.setText("");
                    conclusionLabel.setText("Not tested yet.");
                } else {
                    actualValueField.setText(desiredPerformance.getRealPerformance() + "");

                    if (desiredPerformance.isSatisfying()) {
                        conclusionLabel.setText("Model performance satisfy this element");
                    } else {
                        conclusionLabel.setText("Model performance did not satisfy this element");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void safeNewDesiredPerformance() {
        Metric selectedMetric = (Metric) metricsList.getSelectedItem();

        int selectedLabelIndex = labelList.getSelectedIndex() - 1;
        String index = selectedLabelIndex + "";

        if (selectedLabelIndex == -1) {
            index = "overall";
        }

        IPresentation selectedPresentation = getSelectedPresentation();

        if (selectedPresentation.getModel() instanceof IGoal) {
            IGoal monitoredEntity = (IGoal) selectedPresentation.getModel();

            DesiredPerformance newDesiredPerformance;
            float desiredValue = Float.parseFloat(desiredValueField.getText());
            if (isOnMisrecognize()) {

                int misclassificationListIndex = misclassificationList.getSelectedIndex() - 1;
                String targetIndex = misclassificationListIndex + "";

                if (misclassificationListIndex == -1) {
                    targetIndex = "overall";
                }

                System.out.println("Label: " + index);
                System.out.println("Target Label: " + targetIndex);

                newDesiredPerformance = new MisclassificationPerformance(monitoredEntity, index, targetIndex, desiredValue);
            } else {
                switch (selectedMetric) {
                    case Accuracy:
                        newDesiredPerformance = new DesiredAccuracy(monitoredEntity, index, desiredValue);
                        break;
                    case Recall:
                        newDesiredPerformance = new DesiredRecall(monitoredEntity, index, desiredValue);
                        break;
                    case Precision:
                        newDesiredPerformance = new DesiredPrecision(monitoredEntity, index, desiredValue);
                        break;
                    default:
                        newDesiredPerformance = null;
                }
            }

            MonitoringConfigurations.addDesiredPerformance(newDesiredPerformance);

            GoalParser.parseGoal(monitoredEntity);
        }
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
    public String getTitle() {
        return "Pipeline Performance View";
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Pipeline Performance View Class";
    }

    public void activated() {
    }

    public void deactivated() {
    }


}
