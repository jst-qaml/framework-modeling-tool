package ai.engineering;

import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;
import javax.swing.tree.*;

import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

public class ProcessGuideView extends JSplitPane implements IPluginExtraTabView, ProjectEventListener{
    
    JSplitPane splitPane;
    JScrollPane guidePane, actionPane;
    JTree stepsTree;

    public JTree getStepsTree(){
        return stepsTree;
    }

    public ProcessGuideView(){
        createSplitPanel();
    }

    private void createSplitPanel(){
        createNodes();
        setMouseListener();

        guidePane = new JScrollPane(stepsTree);

        setLeftComponent(guidePane);
        setRightComponent(actionPane);
    }

    public void showAIProjectCanvas(int highlightPanel){
        CanvasView canvas = new AIProjectCanvasView(highlightPanel);
        actionPane = new JScrollPane(canvas);
        setRightComponent(actionPane);
    }

    public void showMLCanvas(int highlightPanel){
        System.out.println(highlightPanel);
        CanvasView canvas = new MLCanvasView(highlightPanel);
        actionPane = new JScrollPane(canvas);
        setRightComponent(actionPane);
    }

    private void createNodes(){
        
        DefaultMutableTreeNode topNode;
        DefaultMutableTreeNode firstLevel;
        DefaultMutableTreeNode secondLevel;

        topNode = new DefaultMutableTreeNode("Overall Process");
        stepsTree = new JTree(topNode);

        //AI Project Canvas

        firstLevel = new DefaultMutableTreeNode("1. Develop AI Project Canvas");
        topNode.add(firstLevel);

        secondLevel = new DefaultMutableTreeNode("1.1. Derive Value Proposition");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.2. Define Customers");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.3. Define Stakeholders");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.4. Define Integration");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.5. Define Output");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.6. Define Data");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.7. Define Skills");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.8. Define Cost");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("1.9. Define Revenue");
        firstLevel.add(secondLevel);

        //ML Canvas

        firstLevel = new DefaultMutableTreeNode("2. Develop Machine Learning Canvas");
        topNode.add(firstLevel);

        secondLevel = new DefaultMutableTreeNode("2.1. Derive Value Proposition");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.2. Define Prediction Task");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.3. Define Decisions");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.4. Define Impact Simulation");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.5. Define Making Prediction");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.6. Define Building Models");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.7. Define Data Collection");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.8. Define Data Sources");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.9. Define Features");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("2.10. Define Monitoring");
        firstLevel.add(secondLevel);

        // KAOS Goal

        firstLevel = new DefaultMutableTreeNode("3. Develop KAOS Goal Model");
        topNode.add(firstLevel);

        secondLevel = new DefaultMutableTreeNode("3.1. Derive Top Goals from ML Canvas");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("3.2. Decompose Sub-Goals into ML Component Goals");
        firstLevel.add(secondLevel);

        // Architectural Diagram

        firstLevel = new DefaultMutableTreeNode("4. Develop Architectural Diagram");
        topNode.add(firstLevel);

        secondLevel = new DefaultMutableTreeNode("4.1. Derive ML Components");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("4.2. Add non-ML Components");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("4.3. Connect Components");
        firstLevel.add(secondLevel);

        // STAMP/STPA
        firstLevel = new DefaultMutableTreeNode("5. Develop STAMP/STPA Analysis");
        topNode.add(firstLevel);

        // Safety Case
        firstLevel = new DefaultMutableTreeNode("6. Develop Safety Case Analysis");
        topNode.add(firstLevel);

        secondLevel = new DefaultMutableTreeNode("6.1. Derive Top Safety Goal from Top KAOS Goal");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("6.2. Derive Hazards from STAMP/STPA");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("6.3. Develop Connecting Argumentation Element Between Top Goal and Hazard");
        firstLevel.add(secondLevel);

        secondLevel = new DefaultMutableTreeNode("6.4. Develop Countermeasures");
        firstLevel.add(secondLevel);

        for (int i = 0; i < stepsTree.getRowCount(); i++) {
            stepsTree.expandRow(i);
        }
    }

    private void setMouseListener(){
        MouseListener ml = new ProcessEventHandler(this);
        stepsTree.addMouseListener(ml);
    }
    

    @Override
    public void projectChanged(ProjectEvent e) {createSplitPanel();}
 
    @Override
    public void projectClosed(ProjectEvent e) {createSplitPanel();}

    @Override
    public void projectOpened(ProjectEvent e) {createSplitPanel();}

    @Override
    public void addSelectionListener(ISelectionListener listener) {}

    @Override
    public String getTitle() {return "Process Guide View";}
 
    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Process Guide View Class";}

    public void activated() {}
   
    public void deactivated() {}

}
