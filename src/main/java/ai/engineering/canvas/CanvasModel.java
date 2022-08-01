package ai.engineering;
 
import java.awt.*;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.model.IRequirement;

public abstract class CanvasModel {
    
    private JPanel canvasPanel;
    
    public CanvasModel(){      
        canvasPanel = new JPanel();
        canvasPanel.setLayout(null);
        updateCanvasSize();
        updateModel();
    }
    
    public void updateCanvasSize(){}
    
    public void updateModel(){}
    
    protected void clearExistingCanvas(){}
    
    protected void clearExisting(JPanel panel){
        for (int i = panel.getComponentCount() - 1; i > 0; i--) {
            panel.remove(i);
        }
    }
    
    protected void addRequirementToPanel(JPanel panel, IRequirement req){
        JTextArea textArea = new JTextArea(req.getRequirementID() + " - " + req.getName());
        textArea.setLineWrap(true);
        panel.add(textArea);
    }
    
    protected JPanel generateCanvasElement(String title, int gridx, int gridy, int height, int width) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new JLabel(title));
        
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
        Dimension canvasDimension = new Dimension(1080, 420);
        Dimension gridDimension = new Dimension((int) canvasDimension.getWidth()/5, (int) canvasDimension.getHeight()/20); 
    
        panel.setVisible(true);
        canvasPanel.add(panel);
    
        panel.setSize(width * (int) gridDimension.getWidth(), height * (int) gridDimension.getHeight());
        panel.setLocation(gridx * (int) gridDimension.getWidth(), gridy * (int) gridDimension.getHeight());
    
        return panel;
    }
    
    public JPanel getCanvas(){
        return canvasPanel;
    }   

}
