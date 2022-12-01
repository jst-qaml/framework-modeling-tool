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
 
public class MLCanvasView extends CanvasView{
 
  public MLCanvasView(){
    super(0);
  }

  public MLCanvasView(int highlightPanelIndex){
    super(highlightPanelIndex);
  }

  @Override
  protected Container createCanvasPane() {
    canvas = new MLCanvas(highlightPanelIndex);
    return canvas.getCanvas();
  }
 
  @Override
  public String getDescription() {return "ML Canvas View Class";}
 
  @Override
  public String getTitle() {return "ML Canvas View";}
}