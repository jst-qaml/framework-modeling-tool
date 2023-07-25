package ai.engineering;
 
import java.awt.*;
import java.awt.event.*;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.text.AttributeSet.FontAttribute;

import com.change_vision.jude.api.inf.model.IRequirement;

public abstract class CanvasModel implements ComponentListener{
    
    private JPanel canvasPanel;
    protected int xGridDivider, yGridDivider;
    private Dimension canvasDimension;
    protected int highlightPanelIndex;


    public CanvasModel(int xGridDivider, int yGridDivider, int highlightPanelIndex){      
        this.highlightPanelIndex = highlightPanelIndex;
        this.xGridDivider = xGridDivider;
        this.yGridDivider = yGridDivider;
        canvasPanel = new JPanel();
        canvasPanel.setLayout(null);
    }
    
    public void updateCanvasLayout(){}
    
    public void updateModel(){}
    
    protected void clearExistingCanvas(){}
    
    protected void clearExisting(JPanel panel){
        for (int i = panel.getComponentCount() - 1; i > 0; i--) {
            panel.remove(i);
        }
    }
    
    protected void addRequirementToPanel(JPanel panel, IRequirement req){
        CanvasElementCollection.clearUnusedElement();

        CanvasElement canvasElement = CanvasElementCollection.findCanvasElement(req);
        
        if (canvasElement == null) {
            canvasElement = new CanvasElement(req);
            CanvasElementCollection.addCanvasElement(canvasElement);
        }
        
        panel.add(canvasElement.getTextArea());
    }
    
    protected JPanel generateCanvasElement(String title, int gridx, int gridy, int height, int width, boolean isHiglighted) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if(isHiglighted){
            panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED));
        }else{
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        Dimension gridDimension = new Dimension((int) canvasDimension.getWidth()/xGridDivider, (int) canvasDimension.getHeight()/yGridDivider); 

        panel.setVisible(true);
        canvasPanel.add(panel);
    
        panel.setSize(width * (int) gridDimension.getWidth(), height * (int) gridDimension.getHeight());
        panel.setLocation(gridx * (int) gridDimension.getWidth(), gridy * (int) gridDimension.getHeight());
        
        JLabel titleLabel = new JLabel(title);
        panel.add(titleLabel);
        titleLabel.setSize(panel.getWidth(), titleLabel.getHeight());

        return panel;
    }
    
    public JPanel getCanvas(){
        return canvasPanel;
    }

    private void updateSize(JPanel parentPanel){
        canvasDimension = parentPanel.getSize();
    }

    @Override
    public void componentResized(ComponentEvent e){
        canvasPanel.removeAll();
        JPanel parentPanel = (JPanel) e.getComponent();
        updateSize(parentPanel);
        updateCanvasLayout();
        updateModel();
    }

    @Override
    public void componentHidden(ComponentEvent e){}

    public void componentMoved(ComponentEvent e){}

    public void componentShown(ComponentEvent e){
        canvasPanel.removeAll();
        JPanel parentPanel = (JPanel) e.getComponent();
        updateSize(parentPanel);
        updateCanvasLayout();
        updateModel();
    }

}
