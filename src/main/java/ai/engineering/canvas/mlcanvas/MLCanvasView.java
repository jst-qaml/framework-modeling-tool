package ai.engineering.canvas.mlcanvas;

import ai.engineering.canvas.CanvasView;

import java.awt.*;
 
public class MLCanvasView extends CanvasView {
 
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