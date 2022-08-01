package ai.engineering;
 
import java.awt.*;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.change_vision.jude.api.inf.model.IRequirement;

public class AIProjectCanvas extends CanvasModel{
    
    private JPanel dataPanel, skillsPanel, outputPanel, valuePropositionPanel, integrationPanel, stakeholdersPanel, customersPanel, costPanel, revenuePanel;

    public AIProjectCanvas(){
        super(10,5);
    }

    @Override
    public void updateCanvasSize(){    
        dataPanel = generateCanvasElement("Data", 0, 0, 4, 2);
        skillsPanel = generateCanvasElement("Skills", 2, 0, 2, 2);
        outputPanel = generateCanvasElement("Output", 2, 2, 2, 2);
        valuePropositionPanel = generateCanvasElement("Value Proposition", 4, 0, 4, 2);
        integrationPanel = generateCanvasElement("Integration", 6, 0, 2, 2);
        stakeholdersPanel = generateCanvasElement("Stakeholders", 6, 2, 2, 2);
        customersPanel = generateCanvasElement("Skills", 8, 0, 4, 2);
        costPanel = generateCanvasElement("Cost", 0, 4, 1, 5);
        revenuePanel = generateCanvasElement("Revenue", 5, 4, 1, 5);
    }

    @Override
    public void updateModel(){
        List<IRequirement> requirements = ElementPicker.getAIProjectCanvasElements();

        if(!requirements.isEmpty()){
            clearExistingCanvas();
            for (IRequirement req : requirements) {           
                if(req.hasStereotype("AI.Data")){
                    addRequirementToPanel(dataPanel, req);
                }
                else if(req.hasStereotype("AI.Skills")){
                    addRequirementToPanel(skillsPanel, req);
                }
                else if(req.hasStereotype("AI.Output")){
                    addRequirementToPanel(outputPanel, req);
                }
                else if(req.hasStereotype("AI.ValueProposition")){
                    addRequirementToPanel(valuePropositionPanel, req);
                }
                else if(req.hasStereotype("AI.Integration")){
                    addRequirementToPanel(integrationPanel, req);
                }
                else if(req.hasStereotype("AI.Stakeholders")){
                    addRequirementToPanel(stakeholdersPanel, req);
                }
                else if(req.hasStereotype("AI.Customers")){
                    addRequirementToPanel(customersPanel, req);
                }
                else if(req.hasStereotype("AI.Cost")){
                    addRequirementToPanel(costPanel, req);
                }
                else if(req.hasStereotype("AI.Revenue")){
                    addRequirementToPanel(revenuePanel, req);
                }
            }
        }
    }

    @Override
    protected void clearExistingCanvas(){
        clearExisting(dataPanel);
        clearExisting(skillsPanel);
        clearExisting(outputPanel);
        clearExisting(valuePropositionPanel);
        clearExisting(integrationPanel);
        clearExisting(stakeholdersPanel);
        clearExisting(customersPanel);
        clearExisting(costPanel);
        clearExisting(revenuePanel);
    }
}
