package ai.engineering;
 
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.lang.model.element.Element;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;

import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IRequirement;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

public abstract class CanvasView extends JPanel implements IPluginExtraTabView, ProjectEventListener {
 
    private IModel currentProject;
    private JLabel label;
    private CanvasModel canvas;
  
    public CanvasView(){
      initComponents();
    }
   
    private void initComponents() {
      setLayout(new BorderLayout());
      add(createCanvasPane(), null);
      addProjectEventListener();
      canvas.updateCanvasSize();
    }
   
    private void addProjectEventListener() {
      ToolUtilities utilities = ToolUtilities.getToolUtilities();
      utilities.setProjectListener(this);
    }
   
    private Container createCanvasPane() {
      canvas = new MLCanvas();
      return canvas.getCanvas();
    }
  
    private void updateCanvasPane(){
      this.removeAll();
      add(createCanvasPane());
    }
  
    @Override
    public void projectChanged(ProjectEvent e) {
      updateCanvasPane();
    }
   
    @Override
    public void projectClosed(ProjectEvent e) {}
   
    @Override
    public void projectOpened(ProjectEvent e) {
      updateCanvasPane();
    }
   
    @Override
    public void addSelectionListener(ISelectionListener listener) {}
   
    @Override
    public Component getComponent() {return this;}
   
    @Override
    public String getDescription() {return "Canvas View Class";}
   
    @Override
    public String getTitle() {return "Canvas View";}
   
    public void activated() {}
   
    public void deactivated() {}
  
}
