package ai.engineering;

import javax.swing.JTextArea;
import java.awt.Color;

import com.change_vision.jude.api.inf.model.IRequirement;

public class CanvasElement {
 
    private JTextArea textArea;
    private IRequirement requirement;

    public CanvasElement(IRequirement req){
        requirement = req;
        textArea = new JTextArea(requirement.getRequirementID() + " - " + requirement.getName());
        textArea.setLineWrap(true);
        textArea.getDocument().addDocumentListener(new CanvasModelListener(req, textArea));
        textArea.setBackground(Color.decode("#FFFFCC"));
    }

    public boolean isSame(IRequirement req){
        return requirement.equals(req);
    }

    public JTextArea getTextArea(){
        if(requirement == null){
            return null;
        }

        textArea.setText(requirement.getRequirementID() + " - " + requirement.getName());
        return textArea;
    }

    public IRequirement getRequirement(){
        return requirement;
    }

    public void setColor(String hexColor){
        textArea.setBackground(Color.decode(hexColor));
    }

    public boolean isSameColor(String hexColor){
        Color newColor = Color.decode(hexColor);
        return newColor.equals(textArea.getBackground());
    }

}
