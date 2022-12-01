package ai.engineering;

import java.lang.annotation.ElementType;
import java.util.List;

import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.project.*;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.view.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.gsn.model.*;

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

        if(owner instanceof IGoal){
            IGoal goal = (IGoal) owner;
            return isGoalRelationshipIllegal(goal, relatedEntity);
        }

        if(owner instanceof IRequirement){
            IRequirement req = (IRequirement) owner;
            return isCanvasRelationshipIllegal(req, relatedEntity);
        }

        return false;
    }

    private boolean isGoalRelationshipIllegal(IGoal sourceEntity, IEntity relatedEntity){
        
        if(relatedEntity instanceof IRequirement){
            IRequirement req = (IRequirement) relatedEntity;
            if(req.hasStereotype("ML.DataSources")){
                return true;
            }
            if(req.hasStereotype("ML.Features")){
                return true;
            }
            if(req.hasStereotype("ML.BuildingModels")){
                return true;
            }
            if(req.hasStereotype("ML.DataCollection")){
                return true;
            }
            if(req.hasStereotype("AI.Stakeholders")){
                return true;
            }
            if(req.hasStereotype("AI.Customers")){
                return true;
            }
            if(req.hasStereotype("AI.Revenue")){
                return true;
            }
            if(req.hasStereotype("AI.Cost")){
                return true;
            }
            if(req.hasStereotype("AI.Skills")){
                return true;
            }
            if(req.hasStereotype("AI.Integration")){
                return true;
            }
            if(req.hasStereotype("AI.Output")){
                return true;
            }
            if(req.hasStereotype("AI.Data")){
                return true;
            }            
        }
        
        return false;

    }

    private boolean isCanvasRelationshipIllegal(IRequirement req, IEntity relatedEntity){
        
        if(relatedEntity instanceof IGoal){
            if(req.hasStereotype("ML.DataSources")){
                return true;
            }
            if(req.hasStereotype("ML.Features")){
                return true;
            }
            if(req.hasStereotype("ML.BuildingModels")){
                return true;
            }
            if(req.hasStereotype("ML.DataCollection")){
                return true;
            }
            if(req.hasStereotype("AI.Stakeholders")){
                return true;
            }
            if(req.hasStereotype("AI.Customers")){
                return true;
            }
            if(req.hasStereotype("AI.Revenue")){
                return true;
            }
            if(req.hasStereotype("AI.Cost")){
                return true;
            }
            if(req.hasStereotype("AI.Skills")){
                return true;
            }
            if(req.hasStereotype("AI.Integration")){
                return true;
            }
            if(req.hasStereotype("AI.Output")){
                return true;
            }
            if(req.hasStereotype("AI.Data")){
                return true;
            }            
        }

        return false;

    }

    private void deleteRelationship(IHyperlinkOwner owner, IEntity relatedEntity){

        IHyperlink[] hyperlinks = owner.getHyperlinks();
        INamedElement namedElement = (INamedElement) relatedEntity;
        INamedElement ownerElement = (INamedElement) owner;

        for (IHyperlink hyperlink : hyperlinks) {
            String relatedElementId = hyperlink.getName();
            if(relatedElementId.equals(namedElement.getId())){
                System.out.println("deleting now!");
                ToolUtilities utilities = ToolUtilities.getToolUtilities();
                ITransactionManager transactionManager = utilities.getTransactionManager();
                try{
                    ProjectAccessor projectAccessor = utilities.getProjectAccessor();
                    IFacet facet = projectAccessor.getFacet(IGsnFacet.FACET_SYMBOLIC_NAME);
                    IModule module = facet.getRootElement(IModule.class);
                    IProjectViewManager pvm = projectAccessor.getViewManager().getProjectViewManager();
                    pvm.showInPropertyView(namedElement);

                    Thread.sleep(10);

                    transactionManager.beginTransaction();
                    owner.deleteHyperlink(hyperlink);
                    transactionManager.endTransaction();

                    Thread.sleep(10);
                    pvm.showInPropertyView(ownerElement);

                } catch (Exception  ex) {System.out.println(ex);}
            }
        }      

    }

}
