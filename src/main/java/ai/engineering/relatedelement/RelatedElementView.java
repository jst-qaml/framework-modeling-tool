package ai.engineering;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import java.awt.Component;
import java.awt.BorderLayout;

import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IDiagram;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.gsn.model.IArgumentAsset;


public class RelatedElementView extends JPanel implements IPluginExtraTabView, ProjectEventListener, Runnable, TableModelListener {

    boolean isActive;
    Thread thread;
    IEntity selectedEntity, oldSelectedEntity;
    ToolUtilities utilities;
    RelatedElementModel model;

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
            
            updateSelectedEntity();
            
            if(isEntitySelect()){
                if(isSelectionChanged()){
                    oldSelectedEntity = selectedEntity;
                    updateTableContent();
                }
            }
            
            try{
                Thread.sleep(100);
            }catch(Exception e){};
        }
    }

    private void updateSelectedEntity(){
        selectedEntity = utilities.getSelectedEntity();
    }

    private boolean isEntitySelect(){
        return selectedEntity != null;
    }

    private void updateTableContent(){
        JTable table = model.createRelatedTable(selectedEntity);
        removeAll();
        add(table);
        addTableListener(table);
    }

    private void addTableListener(JTable table){
        TableModel model = table.getModel();
        if(model != null){
            model.addTableModelListener(this);
        }
    }

    @Override
    public void projectChanged(ProjectEvent e) {}
 
    @Override
    public void projectClosed(ProjectEvent e) {isActive = false;}

     @Override
    public void projectOpened(ProjectEvent e) {}
 
    @Override
    public void addSelectionListener(ISelectionListener listener) {}

    @Override
    public String getTitle() {return "Related Elements View";}
 
    @Override
    public Component getComponent() {return this;}
 
    @Override
    public String getDescription() {return "Related Element View Class";}

    @Override
    public void tableChanged(TableModelEvent e){

        JTable relatedElementTable = (JTable) this.getComponent(0);

        TableModel tableModel = relatedElementTable.getModel();

        model.updateElementsFromTable(tableModel, e);

    }

    public void activated() {}
   
    public void deactivated() {}

    private void setupUtilities(){
        utilities = ToolUtilities.getToolUtilities();
        utilities.setProjectListener(this);
    }

}
