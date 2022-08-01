package ai.engineering;
 
import java.awt.*;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.model.IRequirement;

public class MLCanvas {
    
    private JPanel canvasPanel;
    private JPanel ptPanel, isPanel, dPanel, mpPanel, vpPanel, dcPanel, bmPanel, dsPanel, fPanel, lmPanel;

    public MLCanvas(){      
        canvasPanel = new JPanel();
        canvasPanel.setLayout(null);
        updateCanvasSize();
        updateModel();
    }

    public void updateCanvasSize(){    
        ptPanel = generateCanvasElement("Prediction Task", 0,0,9,1);
        isPanel = generateCanvasElement("Impact Simulation", 0, 9, 11, 1);
        dPanel = generateCanvasElement("Decision", 1, 0, 9, 1);
        mpPanel = generateCanvasElement("Making Prediction", 1, 9, 7, 1);
        vpPanel = generateCanvasElement("Value Proposition", 2, 0, 16, 1);
        dcPanel = generateCanvasElement("Data Collection", 3, 0, 9, 1);
        bmPanel = generateCanvasElement("Building Models", 3, 9, 7, 1);
        dsPanel = generateCanvasElement("Data Sources", 4, 0, 9, 1);
        fPanel = generateCanvasElement("Features", 4, 9, 11, 1);
        lmPanel = generateCanvasElement("Live Monitoring", 1, 16, 4, 3);
    }

    public void updateModel(){
        List<IRequirement> requirements = ElementPicker.getMLCanvasElements();

        if(!requirements.isEmpty()){
            clearExistingCanvas();
            for (IRequirement req : requirements) {           
                if(req.hasStereotype("ML.PredictionTask")){
                    addRequirementToPanel(ptPanel, req);
                }
                else if(req.hasStereotype("ML.ImpactSimulation")){
                    addRequirementToPanel(isPanel, req);
                }
                else if(req.hasStereotype("ML.Decision")){
                    addRequirementToPanel(dPanel, req);
                }
                else if(req.hasStereotype("ML.MakingPrediction")){
                    addRequirementToPanel(mpPanel, req);
                }
                else if(req.hasStereotype("ML.ValueProposition")){
                    addRequirementToPanel(vpPanel, req);
                }
                else if(req.hasStereotype("ML.DataCollection")){
                    addRequirementToPanel(dcPanel, req);
                }
                else if(req.hasStereotype("ML.BuildingModels")){
                    addRequirementToPanel(bmPanel, req);
                }
                else if(req.hasStereotype("ML.DataSources")){
                    addRequirementToPanel(dsPanel, req);
                }
                else if(req.hasStereotype("ML.Features")){
                    addRequirementToPanel(fPanel, req);
                }
                else if(req.hasStereotype("ML.LiveMonitoring")){
                    addRequirementToPanel(lmPanel, req);
                }
            }
        }
    }

    private void clearExistingCanvas(){
        clearExisting(ptPanel);
        clearExisting(isPanel);
        clearExisting(dPanel);
        clearExisting(mpPanel);
        clearExisting(vpPanel);
        clearExisting(dcPanel);
        clearExisting(bmPanel);
        clearExisting(dsPanel);
        clearExisting(fPanel);
        clearExisting(lmPanel);
    }

    private void clearExisting(JPanel panel){
        for (int i = panel.getComponentCount() - 1; i > 0; i--) {
            panel.remove(i);
        }
    }

    private void addRequirementToPanel(JPanel panel, IRequirement req){
        JTextArea textArea = new JTextArea(req.getRequirementID() + " - " + req.getName());
        textArea.setLineWrap(true);
        panel.add(textArea);
    }

    private JPanel generateCanvasElement(String title, int gridx, int gridy, int height, int width) {
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
