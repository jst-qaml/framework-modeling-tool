package ai.engineering.relatedelement;

import ai.engineering.utilities.ToolUtilities;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class RelatedElementView extends JPanel implements IPluginExtraTabView, ProjectEventListener, Runnable {

    boolean isActive;
    Thread thread;
    IEntity selectedEntity, oldSelectedEntity;
    ToolUtilities utilities;
    RelatedElementModel model;
    TraceabilityMaps tmaps;

    public RelatedElementView() {
        setLayout(new BorderLayout());
        setupUtilities();
        setupInitialValues();
        setupThread();
    }

    private boolean isSelectionChanged(){
        return !Objects.equals(oldSelectedEntity, selectedEntity);
    }

    private void setupInitialValues(){
        isActive = true;
        oldSelectedEntity = null;

        model = new RelatedElementModel();
    }

    private void setupThread(){
        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while(isActive){
            
            updateTableContent();
            
            // try{
            //     Thread.sleep(100);
            // }catch(Exception e){};
        }
    }

    private void updateSelectedEntity(){
        selectedEntity = utilities.getSelectedEntity();
    }

    private boolean isEntitySelected(){
        return selectedEntity != null;
    }

    private void updateTableContent(){
        JTable table = model.createRelatedTable();
        JScrollPane scrollPane = new JScrollPane(table);
        removeAll();
        add(scrollPane);
    }

    @Override
    public void projectChanged(ProjectEvent e) {}
 
    @Override
    public void projectClosed(ProjectEvent e) {isActive = false;}

     @Override
    public void projectOpened(ProjectEvent e) {
        
    }
 
    @Override
    public void addSelectionListener(ISelectionListener listener) {}

    @Override
    public String getTitle() {return "Related Elements View";}
 
    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Related Element View Class";}

    public void activated() {}
   
    public void deactivated() {}

    private void setupUtilities(){
        utilities = ToolUtilities.getToolUtilities();
        utilities.setProjectListener(this);
    }

}
