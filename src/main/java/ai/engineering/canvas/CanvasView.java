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

public abstract class CanvasView extends JPanel implements IPluginExtraTabView, ProjectEventListener, Runnable {
 
    private IModel currentProject;
    private JLabel label;
    protected CanvasModel canvas;
    protected int highlightPanelIndex;
    private Thread thread;
    private boolean isActive;

    public CanvasView(int highlightPanelIndex){
      this.highlightPanelIndex = highlightPanelIndex;
      initComponents();
      thread = new Thread(this);
    }

    public CanvasView(){
      this.highlightPanelIndex = 0;
      initComponents();
      thread = new Thread(this);
    }
   
    private void initComponents() {
      setLayout(new BorderLayout());
      add(createCanvasPane(), null);
      addProjectEventListener();
      addComponentListener(canvas);
    }
   
    private void addProjectEventListener() {
      ToolUtilities utilities = ToolUtilities.getToolUtilities();
      utilities.setProjectListener(this);
    }

    protected abstract Container createCanvasPane();
  
    private void updateCanvasPane(){
      this.removeAll();
      add(createCanvasPane(), null);
    }
  
    @Override
    public void projectChanged(ProjectEvent e) {
    }
   
    @Override
    public void projectClosed(ProjectEvent e) {
      isActive = false;
    }
   
    @Override
    public void projectOpened(ProjectEvent e) {
      initComponents();
      isActive = true;
    }
   
    @Override
    public void addSelectionListener(ISelectionListener listener) {}
   
    @Override
    public Component getComponent() {return this;}
   
    @Override
    public String getDescription() {return "Canvas View Class";}
   
    @Override
    public String getTitle() {return "Canvas View";}
   
    public void activated() {isActive = true;}
   
    public void deactivated() {isActive = false;}

    public void run(){
      while(true){
        updateCanvasPane();
      }
    }
  
}
