package ai.engineering;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.*;
import javax.swing.text.*;

import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.editor.ITransactionManager;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;


public class CanvasModelListener implements DocumentListener{
    
    IRequirement requirement;
    String oldRequirementString;
    ITransactionManager transactionManager;
    SysmlModelEditor modelEditor;
    JTextArea textArea;

    CanvasModelListener(IRequirement requirement, JTextArea textArea){
        this.requirement = requirement;
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        transactionManager = utilities.getTransactionManager();
        modelEditor = utilities.getSysmlModelEditor();
        this.textArea = textArea;
        try {
            Document document = textArea.getDocument();
            oldRequirementString = document.getText(0, document.getLength());
        } catch (Exception e) {
            
        }
    }

    public void insertUpdate(DocumentEvent e) {
        UpdateExistingString(e.getDocument());
    }

    public void removeUpdate(DocumentEvent e) {
        UpdateExistingString(e.getDocument());
    }

    public void changedUpdate(DocumentEvent e) {
        
    }

    private void UpdateExistingString(Document document){
        try {
            String newRequirementString = document.getText(0, document.getLength());
            
            // if(oldRequirementString.equals(newRequirementString)){
            //     transactionManager.endTransaction();
            // }
            // else if(newRequirementString.equals("")){
            //     transactionManager.beginTransaction();
            //     modelEditor.delete(requirement);
            //     Container parent = textArea.getParent();
            //     textArea.setVisible(false);
            //     parent.remove(textArea);
            // }
            // else{
            //     String[] splitStrings = newRequirementString.split(" - ", 2);
            //     oldRequirementString = newRequirementString;
            //     transactionManager.beginTransaction();
            //     requirement.setRequirementID(splitStrings[0]);
            //     requirement.setName(splitStrings[1]);
            // }
        } catch (Exception e) {
            transactionManager.abortTransaction();
        }
    }

    

}
