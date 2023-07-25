package ai.engineering;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.*;

import org.omg.CORBA.portable.ValueBase;

import javax.swing.JTree;

import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.view.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.gsn.editor.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.inf.presentation.*;


public class ProcessEventHandler extends MouseAdapter{
    
    JTree stepsTree;
    ProcessGuideView view;

    public ProcessEventHandler(ProcessGuideView view){
        this.view = view;
        this.stepsTree = view.getStepsTree();
    }

    public void mousePressed(MouseEvent e) {
        int selRow = stepsTree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = stepsTree.getPathForLocation(e.getX(), e.getY());
        if(selPath != null){
            if(e.getClickCount() == 2 && selPath.getPathCount() == 3) {
                triggerAction(selPath);
            }
        }
    }

    private void triggerAction(TreePath selPath){
        String selectedAction = selPath.getLastPathComponent().toString();

        if(selectedAction.startsWith("1.")){
            view.showAIProjectCanvas(Integer.parseInt(selectedAction.charAt(2)+""));
        }

        if(selectedAction.startsWith("2.")){
            view.showMLCanvas(Integer.parseInt(selectedAction.charAt(2)+""));
        }

        if(selectedAction.startsWith("3.")){
            if (selectedAction.startsWith("3.1.")){
                createKaosRootGoal();
            }else{
                showKAOSModel();
            }
        }

        if(selectedAction.startsWith("4.")){

        }

        if(selectedAction.startsWith("5.")){

        }

        if(selectedAction.startsWith("6.")){
            if (selectedAction.startsWith("6.1.")){
                createSafeyCaseRootGoal();
            }else{
                showSafetyCaseModel();
            }
        }
    }

    private void createKaosRootGoal(){
        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        ProjectAccessor projectAccessor = toolUtilities.getProjectAccessor();
        IDiagramViewManager diagramViewManager = toolUtilities.getDiagramViewManager();
        ITransactionManager transactionManager = toolUtilities.getTransactionManager();

        try {
            transactionManager.beginTransaction();

            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            IFacet facet = projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
            IModule module = facet.getRootElement(IModule.class);

            GsnDiagramEditor diagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);
            IGsnDiagram diagram = diagramEditor.createGsnDiagram(module, "KAOS Diagram");

            IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
            GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
            
            List<IRequirement> valuePropositions = ElementPicker.getMLCanvasElements("ML.ValueProposition");
            IGoal rootGoal = modelEditor.createGoal(module, "Root KAOS Goal");
            rootGoal.setContent(valuePropositions.get(0).getName());
            rootGoal.createElementHyperlink(valuePropositions.get(0), "");
            valuePropositions.get(0).createElementHyperlink(rootGoal, "");
            INodePresentation rootGoalPresentation = diagramEditor.createNodePresentation(rootGoal, new Point(500, 50));
            
            List<IRequirement> decisions = ElementPicker.getMLCanvasElements("ML.Decision");
            List<IGoal> decisionGoals = new ArrayList<IGoal>();
            for (int i = 0; i < decisions.size(); i++) {
                IGoal decisionGoal = modelEditor.createGoal(module, "Decision Goal " + (i+1));
                decisionGoals.add(decisionGoal);
                decisionGoal.setContent(decisions.get(i).getName());
                
                ISupportedBy link = modelEditor.createSupportedBy(rootGoal, decisionGoal);
                decisionGoal.createElementHyperlink(rootGoal, "");
                rootGoal.createElementHyperlink(decisionGoal, "");

                INodePresentation decisionGoalPresentation = diagramEditor.createNodePresentation(decisionGoal, new Point(500, 200));
                diagramEditor.createLinkPresentation(link, rootGoalPresentation, decisionGoalPresentation);              
            }

            List<IRequirement> tasks = ElementPicker.getMLCanvasElements("ML.PredictionTask");
            List<IGoal> taskGoals = new ArrayList<IGoal>();
            IGoal decisionGoal = decisionGoals.get(0);
            for (int i = 0; i < tasks.size(); i++) {
                IGoal taskGoal = modelEditor.createGoal(module, "Prediction Task Goal " + (i+1));
                taskGoal.setContent(tasks.get(i).getName());
                
                ISupportedBy link = modelEditor.createSupportedBy(decisionGoal, taskGoal);
                taskGoal.createElementHyperlink(decisionGoal, "");
                decisionGoal.createElementHyperlink(taskGoal, "");

                INodePresentation taskGoalPresentation = diagramEditor.createNodePresentation(taskGoal, new Point((i +1) * 200, 300));

                IPresentation[] decisionGoalPresentations = decisionGoal.getPresentations();
                INodePresentation decisionGoalPresentation = (INodePresentation) decisionGoalPresentations[0];
                diagramEditor.createLinkPresentation(link, decisionGoalPresentation, taskGoalPresentation);              
            }

            diagramViewManager.open(diagram);
            transactionManager.endTransaction();            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void showKAOSModel(){

    }

    private void createSafeyCaseRootGoal(){
        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        ProjectAccessor projectAccessor = toolUtilities.getProjectAccessor();
        IDiagramViewManager diagramViewManager = toolUtilities.getDiagramViewManager();
        ITransactionManager transactionManager = toolUtilities.getTransactionManager();

        try {
            transactionManager.beginTransaction();

            IDiagramEditorFactory diagramEditorFactory = projectAccessor.getDiagramEditorFactory();
            IFacet facet = projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
            IModule module = facet.getRootElement(IModule.class);

            IModelEditorFactory modelEditorFactory = projectAccessor.getModelEditorFactory();
            GsnModelEditor modelEditor = modelEditorFactory.getModelEditor(GsnModelEditor.class);
            IGoal rootGoal = modelEditor.createGoal(module, "Root Safety Goal");

            IGoal rootKAOSGoal = ElementPicker.getGoalbyId("Root KAOS Goal");
            rootGoal.setContent(rootKAOSGoal.getContent());
            rootGoal.createElementHyperlink(rootKAOSGoal, "");

            GsnDiagramEditor diagramEditor = diagramEditorFactory.getDiagramEditor(GsnDiagramEditor.class);
            IGsnDiagram diagram = diagramEditor.createGsnDiagram(module, "Safety Case Diagram");
            diagramEditor.createNodePresentation(rootGoal, new Point(250, 250));

            IPresentation[] presentations = diagram.getPresentations();

            diagramViewManager.open(diagram);

            transactionManager.endTransaction();            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void showSafetyCaseModel(){};

}
