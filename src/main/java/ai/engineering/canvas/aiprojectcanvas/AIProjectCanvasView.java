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
 
public class AIProjectCanvasView extends CanvasView {
 
  @Override
  protected Container createCanvasPane() {
    canvas = new AIProjectCanvas();
    return canvas.getCanvas();
  }
 
  @Override
  public String getDescription() {return "AI Project Canvas View Class";}
 
  @Override
  public String getTitle() {return "AI Project Canvas View";}
}