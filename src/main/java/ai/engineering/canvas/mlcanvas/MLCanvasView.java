package ai.engineering.canvas.mlcanvas;

import java.awt.Container;

import ai.engineering.canvas.CanvasView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

public class MLCanvasView extends CanvasView {

    public MLCanvasView() {
        super(0);
    }

    public MLCanvasView(int highlightPanelIndex) {
        super(highlightPanelIndex);
    }

    @Override
    protected Container createCanvasPane() {
        canvas = new MLCanvas(highlightPanelIndex);
        return canvas.getCanvas();
    }

    @Override
    public String getDescription() {
        return "ML Canvas View Class";
    }

    @Override
    public void removeSelectionListener(ISelectionListener iSelectionListener) {

    }

    @Override
    public String getTitle() {
        return "ML Canvas View";
    }
}