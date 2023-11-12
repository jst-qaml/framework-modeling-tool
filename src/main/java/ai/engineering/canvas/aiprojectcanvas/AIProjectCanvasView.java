package ai.engineering.canvas.aiprojectcanvas;

import ai.engineering.canvas.CanvasView;

import java.awt.*;
 
public class AIProjectCanvasView extends CanvasView {
 
  public AIProjectCanvasView(){
    super(0);
  }

  public AIProjectCanvasView(int highlightPanelIndex){
    super(highlightPanelIndex);
  }

  @Override
  protected Container createCanvasPane() {
    canvas = new AIProjectCanvas(highlightPanelIndex);
    return canvas.getCanvas();
  }
 
  @Override
  public String getDescription() {return "AI Project Canvas View Class";}
 
  @Override
  public String getTitle() {return "AI Project Canvas View";}
}