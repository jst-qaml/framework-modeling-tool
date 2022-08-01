package ai.engineering;

import java.lang.annotation.ElementType;
import java.util.List;

import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.gsn.model.IStrategy;

import com.change_vision.jude.api.inf.ui.IWindow;

import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class RelationChecker implements Runnable{

    private boolean isActive;
    private IWindow window;

    public RelationChecker(IWindow window){
        this.window = window;
    }

    public void activateChecking(){isActive = true;}

    public void deactivateChecking(){isActive = false;}

    public void run(){
        
        activateChecking();

        while(isActive){
            checkAllElementHyperlinks();

            try{
                Thread.sleep(200);
            }catch(Exception e){};
        }
    }

    private void checkAllElementHyperlinks(){

        List<IHyperlinkOwner> hyperlinkOwners = ElementPicker.getAllHyperlinksOwner();

        for (IHyperlinkOwner hyperlinkOwner : hyperlinkOwners) {

            List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

            for (IEntity relatedEntity : relatedEntities) {
                
                if(isRelationshipIllegal(hyperlinkOwner, relatedEntity)){
                    triggerErrorWindow(hyperlinkOwner, relatedEntity);
                    deleteRelationship(hyperlinkOwner, relatedEntity);
                }

            }
        }
    }

    private void triggerErrorWindow(IHyperlinkOwner owner, IEntity relatedEntity){ 
        String errorMessage = generateErrorMessage(owner, relatedEntity);
        JOptionPane.showMessageDialog(window.getParent(), errorMessage, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private String generateErrorMessage(IHyperlinkOwner owner, IEntity relatedEntity){
        INamedElement namedOwner = (INamedElement) owner;
        INamedElement namedRelatedElement = (INamedElement) relatedEntity;
        
        String elementNameInfo = "Illegal relationship found between " + namedOwner.getName() + " and " + namedRelatedElement.getName() + ". \n The hyperlink will be deleted.";
        return elementNameInfo;
    }

    private boolean isRelationshipIllegal(IHyperlinkOwner owner, IEntity relatedEntity){
        boolean isIllegal = true;

        if(owner instanceof IRequirement){
            IRequirement req = (IRequirement) owner;
            isIllegal = isMLCanvasRelationshipIllegal(req, relatedEntity);
        }

        return isIllegal;
    }

    private boolean isMLCanvasRelationshipIllegal(IRequirement req, IEntity relatedEntity){
        
        boolean isIllegal = true;

        if(req.hasStereotype("ML.PredictionTask")){
            if(ElementTypeChecker.isKAOSGoal(relatedEntity)){
                isIllegal = false;
            }
        }
        else if(req.hasStereotype("ML.ImpactSimulation")){
            
        }
        else if(req.hasStereotype("ML.Decision")){
            
        }
        else if(req.hasStereotype("ML.MakingPrediction")){
            
        }
        else if(req.hasStereotype("ML.ValueProposition")){
            if(ElementTypeChecker.isKAOSGoal(relatedEntity)){
                isIllegal = false;
            }
            if(ElementTypeChecker.isSafetyGoal(relatedEntity)){
                isIllegal = false;
            }
        }
        else if(req.hasStereotype("ML.DataCollection")){
            
        }
        else if(req.hasStereotype("ML.BuildingModels")){
            
        }
        else if(req.hasStereotype("ML.DataSources")){
            
        }
        else if(req.hasStereotype("ML.Features")){
            
        }
        else if(req.hasStereotype("ML.LiveMonitoring")){
            
        }

        return isIllegal;

    }

    private void deleteRelationship(IHyperlinkOwner owner, IEntity relatedEntity){

        IHyperlink[] hyperlinks = owner.getHyperlinks();
        INamedElement namedElement = (INamedElement) relatedEntity;

        for (IHyperlink hyperlink : hyperlinks) {
            String relatedElementId = hyperlink.getName();
            if(relatedElementId.equals(namedElement.getId())){
                System.out.println("deleting now!");
                ToolUtilities utilities = ToolUtilities.getToolUtilities();
                ITransactionManager transactionManager = utilities.getTransactionManager();
                try{
                    transactionManager.beginTransaction();
                    owner.deleteHyperlink(hyperlink);
                    transactionManager.endTransaction();
                } catch (InvalidEditingException  ex) {System.out.println(ex);}
            }
        }

    }

}
