package ai.engineering;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.project.ProjectEvent;

import java.awt.Component;

import javax.swing.JPanel;

public class TraceabilityRecommenderView extends JPanel implements IPluginExtraTabView, ProjectEventListener, Runnable{

    public TraceabilityRecommenderView(){
        
    }

    public void run(){}

    @Override
    public void projectChanged(ProjectEvent e) {}
 
    @Override
    public void projectClosed(ProjectEvent e) {}
 
    @Override
    public void projectOpened(ProjectEvent e) {}

    @Override
    public void addSelectionListener(ISelectionListener listener) {}
 
    @Override
    public String getTitle() {return "Traceability Recommender View";}

    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Traceability Recommender View Class";}

    public void activated() {}
   
    public void deactivated() {}

    
}
