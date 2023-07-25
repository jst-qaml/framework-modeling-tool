package ai.engineering;
 
import java.awt.*;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.model.IRequirement;

public class MLCanvas extends CanvasModel{
    
    private JPanel ptPanel, isPanel, dPanel, mpPanel, vpPanel, dcPanel, bmPanel, dsPanel, fPanel, lmPanel;

    public MLCanvas(int highlightPanelIndex){
        super(5,20, highlightPanelIndex);
        this.highlightPanelIndex = highlightPanelIndex;
    }

    public MLCanvas(){
        super(5,20, 0);
    }

    @Override
    public void updateCanvasLayout(){
        ptPanel = generateCanvasElement("Prediction Task", 0,0,9,1,highlightPanelIndex == 2);
        isPanel = generateCanvasElement("Impact Simulation", 0, 9, 11, 1,highlightPanelIndex == 4);
        dPanel = generateCanvasElement("Decision", 1, 0, 9, 1,highlightPanelIndex == 3);
        mpPanel = generateCanvasElement("Making Prediction", 1, 9, 7, 1,highlightPanelIndex == 5);
        vpPanel = generateCanvasElement("Value Proposition", 2, 0, 16, 1,highlightPanelIndex == 1);
        dcPanel = generateCanvasElement("Data Collection", 3, 0, 9, 1,highlightPanelIndex == 7);
        bmPanel = generateCanvasElement("Building Models", 3, 9, 7, 1,highlightPanelIndex == 6);
        dsPanel = generateCanvasElement("Data Sources", 4, 0, 9, 1,highlightPanelIndex == 8);
        fPanel = generateCanvasElement("Features", 4, 9, 11, 1,highlightPanelIndex == 9);
        lmPanel = generateCanvasElement("Live Monitoring", 1, 16, 4, 3,highlightPanelIndex == 10);
    }

    @Override
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

    @Override
    protected void clearExistingCanvas(){
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
}
