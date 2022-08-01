package ai.engineering;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEventListener;

import com.change_vision.jude.api.inf.editor.ITransactionManager;

import com.change_vision.jude.api.inf.view.IViewManager;
import com.change_vision.jude.api.inf.view.IProjectViewManager;

import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IEntity;

public class ToolUtilities{

    private ProjectAccessor projectAccessor;
    private static boolean isInitialized;
    private static ToolUtilities instance;

    private ToolUtilities(){
        setupProjectAccessor();
        isInitialized = true;        
    }

    public static ToolUtilities getToolUtilities(){
        if(!isInitialized){
            instance = new ToolUtilities();
        }

        return instance;
    }

    private void setupProjectAccessor(){
        try {
            projectAccessor = ProjectAccessorFactory.getProjectAccessor();
            IModel currentProject = projectAccessor.getCurrentProject();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        } catch (ProjectNotFoundException e){
            String message = "Please open a project";
        }
    }

    public void setProjectListener(ProjectEventListener listener){
        projectAccessor.addProjectEventListener(listener);
    }

    public ProjectAccessor getProjectAccessor(){
        return projectAccessor;
    }

    public IModel getCurrentProject(){
        try {
            return projectAccessor.getCurrentProject();            
        } catch (ProjectNotFoundException e){
            String message = "Please open a project";
        }

        return null;
    }

    public IEntity getSelectedEntity(){
        try {
            IViewManager viewManager = projectAccessor.getViewManager();
            IProjectViewManager projectViewManager = viewManager.getProjectViewManager();
            IEntity[] entities = projectViewManager.getSelectedEntities();
            
            if(entities.length > 0){
                return entities[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return null;
    }

    public ITransactionManager getTransactionManager(){
        return projectAccessor.getTransactionManager();
    }

}
