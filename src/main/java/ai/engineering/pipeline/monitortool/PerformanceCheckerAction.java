package ai.engineering;

import java.awt.Color;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.gsn.model.*;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import com.change_vision.jude.api.gsn.model.IGoal;

import com.change_vision.jude.api.inf.view.IDiagramViewManager;
import com.fasterxml.jackson.core.Version;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;

import com.change_vision.jude.api.inf.editor.ITransactionManager;

import java.io.InputStream;

public class PerformanceCheckerAction implements IPluginActionDelegate{
    
    JComboBox versionComboBox;
    JTable summaryTable;
    Object[][] latestResult;

    public Object run(IWindow window){

        String[][] versionMap = VersionFetcher.GetVersions();
        String[] versionList = versionMap[0];

        versionComboBox = new JComboBox(versionList);

        GridBagConstraints gbc = new GridBagConstraints();

        JButton startButton = new JButton("Fetch Data");
        startButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                checkPerformance();
                updateTableData();
                MonitoringSummaryView.updateTable();
            } 
          }
        );

        JButton copyButton = new JButton("Use Performance As Baseline");
        copyButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                updateBaseline();
                updateTableData();
                MonitoringSummaryView.updateTable();
            } 
          }
        );

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 10;
        gbc.ipadx = 30;
        panel.add(new JLabel("Select model version: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        gbc.gridwidth = 2;
        panel.add(versionComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 10;
        gbc.ipadx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Result: "), gbc);
        String[] columnSummary = {"Element", "Monitored Label", "Monitored Metric", "Desired Value", "Real Value", "Result", "Baseline?"};
        DefaultTableModel tableModel = new DefaultTableModel(MonitoringConfigurations.createSummaryTable(true), columnSummary){
            public Class getColumnClass(int column){
                switch(column){
                    case 5: return Icon.class;
                    case 6: return Boolean.class;
                    default: return super.getColumnClass(column);
                }
            }
        };
        summaryTable = new JTable(tableModel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipady = 40;
        gbc.ipadx = 0;
        gbc.gridwidth = 3;
        panel.add(new JScrollPane(summaryTable), gbc);
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.ipady = 10;
        gbc.ipadx = 40;
        gbc.gridwidth = 1;
        panel.add(startButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.ipady = 10;
        gbc.ipadx = 40;
        gbc.gridwidth = 1;
        panel.add(copyButton, gbc);

        JDialog dialog = new JDialog(window.getParent(), "Fetch model performance");
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.NORTH);
        // dialog.setPreferredSize(new Dimension(1200, 800));
        // panel.setPreferredSize(new Dimension(1));
        dialog.setVisible(true);

	    return null;
	}

    private void setColor(IPresentation iPresentation, String hexColor){

        String currentColor = iPresentation.getProperty("fill.color");
        if(currentColor.equals(hexColor)){
            return;
        }

        ToolUtilities utilities = ToolUtilities.getToolUtilities();

        IElement model = iPresentation.getModel();
        
        if (model instanceof IGoal) {
            IGoal goal = (IGoal) model;
            if (goal.isUndeveloped()) {
                boolean isOrNodeFailed = true;
                IDiagram diagram = iPresentation.getDiagram();
                try {
                    IPresentation[] presentations = diagram.getPresentations();
                    for (IPresentation presentation : presentations) {
                        if(presentation instanceof ILinkPresentation){
                            ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                            IPresentation source = linkPresentation.getSourceEnd();
                            if(source.getLabel() == iPresentation.getLabel()){
                                IPresentation subGoal = linkPresentation.getTargetEnd();
                                String subGoalColor = iPresentation.getProperty("fill.color");
                                isOrNodeFailed = isOrNodeFailed && subGoalColor.equals(hexColor);
                            }              
                        }
                    }
                if (!isOrNodeFailed) {
                    return;
                }

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        if(model instanceof IRequirement){
            IRequirement req = (IRequirement) iPresentation.getModel();
            CanvasElement canvasElement = CanvasElementCollection.findCanvasElement(req);
            canvasElement.setColor(hexColor);
        }else{
            ITransactionManager transactionManager = utilities.getTransactionManager();
            try {
                transactionManager.beginTransaction();
                iPresentation.setProperty("fill.color", hexColor);
                transactionManager.endTransaction();
            } catch (Exception exc) {
                transactionManager.abortTransaction();
            }
        }

        IDiagram openDiagram = iPresentation.getDiagram();

        try {
            IPresentation[] presentations = openDiagram.getPresentations();
            for (IPresentation presentation : presentations) {
                if(presentation instanceof ILinkPresentation){
                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                    IPresentation target = linkPresentation.getTargetEnd();
                    if(target.getLabel() == iPresentation.getLabel()){
                        IPresentation source = linkPresentation.getSourceEnd();
                        setColor(source, hexColor);
                    }              
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) iPresentation;

        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

        for (IEntity iEntity : relatedEntities) {
            if (iEntity instanceof IPresentation) {
                IPresentation presentation = (IPresentation) iEntity;
                setColor(presentation, hexColor);
            }

            if(iEntity instanceof IElement){
                IElement relatedElement = (IElement) iEntity;
                try {
                    IPresentation[] presentations = relatedElement.getPresentations();
                    for (int i = 0; i < presentations.length; i++) {
                        setColor(presentations[i], hexColor);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } 
            }
        }
    }

    private void setColor(IElement element, String hexColor){

        if(element instanceof IRequirement){
            IRequirement req = (IRequirement) element;
            CanvasElement canvasElement = CanvasElementCollection.findCanvasElement(req);
            if (canvasElement.isSameColor(hexColor)) {
                return;   
            } else {
                canvasElement.setColor(hexColor);
            }          
        }else{
            ToolUtilities utilities = ToolUtilities.getToolUtilities();
            ITransactionManager transactionManager = utilities.getTransactionManager();

            try {
                IPresentation[] presentations = null;

                if(element != null){
                    presentations = element.getPresentations();
                }

                if(element instanceof IGoal){
                    IGoal goal = (IGoal) element;
                    if(goal.isUndeveloped()){
                        boolean isOrNodeFailed = true;
                        for (IPresentation iPresentation : presentations) {
                            IDiagram diagram = iPresentation.getDiagram();

                            IPresentation[] relatedpresentations = diagram.getPresentations();
                            for (IPresentation presentation : relatedpresentations) {
                                if(presentation instanceof ILinkPresentation){
                                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                                    IPresentation source = linkPresentation.getSourceEnd();
                                    if(source.getLabel().equals(iPresentation.getLabel())){
                                        IPresentation subGoal = linkPresentation.getTargetEnd();
                                        String subGoalColor = subGoal.getProperty("fill.color");
                                        isOrNodeFailed = isOrNodeFailed && subGoalColor.equals(hexColor);
                                    }
                                }
                            }    
                        }
                        if (!isOrNodeFailed) {
                            return;
                        }
                    }
                }

                if (presentations != null) {
                    for (IPresentation iPresentation : presentations) {
                        if (iPresentation != null) {
                            String currentColor = iPresentation.getProperty("fill.color");
                        
                            if(currentColor.equals(hexColor)){
                                return;
                            }                      

                            transactionManager.beginTransaction();
                            iPresentation.setProperty("fill.color", hexColor);
                            transactionManager.endTransaction();
        
                            System.out.println("Colored element " + iPresentation.getLabel() + " with color " + iPresentation.getProperty("fill.color"));

                            IDiagram openDiagram = iPresentation.getDiagram();

                            IPresentation[] relatedpresentations = openDiagram.getPresentations();
                            for (IPresentation presentation : relatedpresentations) {
                                if(presentation instanceof ILinkPresentation){
                                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                                    IPresentation target = linkPresentation.getTargetEnd();
                                    IPresentation source = linkPresentation.getSourceEnd();
                                    if(openDiagram instanceof IInternalBlockDiagram){
                                        if(source.getLabel().equals(iPresentation.getLabel())){
                                            setColor(target.getModel(), hexColor);
                                        }
                                    }else{
                                        if(target.getLabel() == iPresentation.getLabel()){
                                            setColor(source.getModel(), hexColor);
                                        }
                                    }
                                }
                           }
                        }
                    }
                }
            } catch (Exception e) {
                transactionManager.abortTransaction();
            }
        }

        IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) element;
        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

        for (IEntity iEntity : relatedEntities) {
            if(iEntity instanceof IElement){
                IElement relatedElement = (IElement) iEntity;
                setColor(relatedElement, hexColor);
            }    
        }

    }

    private void updateTableData(){
        String[] columnSummary = {"Element", "Monitored Label", "Monitored Metric", "Desired Value", "Real Value", "Result", "Baseline?"};
        latestResult = MonitoringConfigurations.createSummaryTable(true);
        DefaultTableModel tableModel = new DefaultTableModel(latestResult, columnSummary){
            public Class getColumnClass(int column){
                switch(column){
                    case 5: return Icon.class;
                    case 6: return Boolean.class;
                    default: return super.getColumnClass(column);
                }
            }
        };
        summaryTable.setModel(tableModel);
    }

    private void checkPerformance(){
        removeLastResult();
        signalNewResult();
    }

    private void removeLastResult(){
        List<DesiredPerformance> desiredPerformances = MonitoringConfigurations.getDesiredPerformances();

        for (DesiredPerformance desiredPerformance : desiredPerformances) {
            IGoal monitoredEntity = desiredPerformance.getMonitoredEntity();
            try {
                IPresentation[] selectedPresentations = monitoredEntity.getPresentations();

                for (IPresentation selectedPresentation : selectedPresentations) {
                    String hexColor = "#FFFFCC";
                    setColor(selectedPresentation.getModel(), hexColor); 
                }
            } catch (Exception e) {
                // TODO: handle exception
            }  
        }
    }

    private void signalNewResult(){
        List<DesiredPerformance> desiredPerformances = MonitoringConfigurations.getDesiredPerformances();

        String selectedVersionString = versionComboBox.getSelectedItem().toString();

        int selectedVersionIndex = versionComboBox.getSelectedIndex();
        
        String[][] versionMap = VersionFetcher.GetVersions();

        int[][] confusionMatrix = VersionFetcher.GetConfusionMatrix(versionMap[1][selectedVersionIndex]);

        for (DesiredPerformance desiredPerformance : desiredPerformances) {
            //float result = VersionFetcher.GetPerformanceResult(desiredPerformance, selectedVersionString);
            IGoal monitoredEntity = desiredPerformance.getMonitoredEntity();
            try {
                IPresentation[] selectedPresentations = monitoredEntity.getPresentations();
                // System.out.println("result:" + result);
                // desiredPerformance.setRealPerformance(result);

                MetricsCalculator.calculateAchievement(desiredPerformance, confusionMatrix);

                for (IPresentation selectedPresentation : selectedPresentations) {
                    if(!desiredPerformance.isSatisfying()){
                        String hexColor = "#FF0000";
                        setColor(selectedPresentation.getModel(), hexColor); 
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            
        }
    }

    private void updateBaseline(){
        for (int i = 0; i < summaryTable.getRowCount(); i++) {
            Boolean isChecked = (Boolean) summaryTable.getValueAt(i, 6);
            if (isChecked) {
                if (latestResult[i][7] instanceof DesiredPerformance) {
                    DesiredPerformance performance = (DesiredPerformance) latestResult[i][7];
                    performance.setDesiredValue(performance.getRealPerformance());
                    performance.updateDescription();
                    try {
                        updateContext(performance);
                    } catch (Exception e) {
                        System.err.println("Updating justification failed.");
                    }
                }
            }
        }
    }

    private void updateContext(DesiredPerformance desiredPerformance) throws InvalidUsingException{
        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        ProjectAccessor projectAccessor = toolUtilities.getProjectAccessor();
        ITransactionManager transactionManager = toolUtilities.getTransactionManager();
        
        IGoal monitoredEntity = desiredPerformance.getMonitoredEntity();
        IPresentation[] presentations = monitoredEntity.getPresentations();

        try {
            transactionManager.beginTransaction();

            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            IFacet facet = projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
            IModule module = facet.getRootElement(IModule.class);

            GsnDiagramEditor diagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);

            for (IPresentation selectedPresentation : presentations) {
                IGsnDiagram diagram = (IGsnDiagram) selectedPresentation.getDiagram();
                diagramEditor.setDiagram(diagram);
    
                IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
                GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);

                INodePresentation selectedNodePresentation = (INodePresentation) selectedPresentation;

                ILinkPresentation[] links = selectedNodePresentation.getLinks();

                Point2D selectedPresentationLocation = selectedNodePresentation.getLocation();
                Point2D justificationNodeLocation = new Point((int) selectedPresentationLocation.getX(), (int) (selectedPresentationLocation.getY() + 100));
    
                IJustification justification = modelEditor.createJustification(module, selectedPresentation.getLabel() + " justification");
                justification.setContent("Based on experiment " + versionComboBox.getSelectedItem().toString());
                INodePresentation justificationPresentation = diagramEditor.createNodePresentation(justification, justificationNodeLocation);
                
                IInContextOf connection = modelEditor.createInContextOf((IArgumentAsset) selectedPresentation.getModel(), justification);
                diagramEditor.createLinkPresentation(connection, selectedNodePresentation, justificationPresentation);   
            }

            transactionManager.endTransaction();  
        } catch (Exception e) {
            System.err.println("Justification Generation Failed");
            System.err.println(e);
        }

    }

}
