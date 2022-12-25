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

import com.change_vision.jude.api.stpa.model.IIdentifiedElement;

public class RelationChecker implements Runnable{

    private boolean isActive;
    private IWindow window;
    private MetamodelRelationship metamodel;

    public RelationChecker(IWindow window){
        this.window = window;
        
        MetamodelXMLParser metamodelParser = new MetamodelXMLParser();
        metamodel = metamodelParser.parseMetamodelXML();
    }

    public void activateChecking(){isActive = true;}

    public void deactivateChecking(){isActive = false;}

    public void run(){
        
       activateChecking();

       checkAllElementHyperlinks();

    }

    private void checkAllElementHyperlinks(){

        List<IEntity> invalidRelatedEntities = metamodel.checkAllElementHyperlinks();

        for (int i = 0; i < invalidRelatedEntities.size(); i+=2) {
            IHyperlinkOwner sourceEntity = (IHyperlinkOwner) invalidRelatedEntities.get(i);
            IEntity destinationEntity = invalidRelatedEntities.get(i+1);

            triggerErrorWindow(sourceEntity, destinationEntity);
            deleteRelationship(sourceEntity, destinationEntity);
        }
    }

    private void triggerErrorWindow(IHyperlinkOwner owner, IEntity relatedEntity){ 
        String errorMessage = generateErrorMessage(owner, relatedEntity);
        JOptionPane.showMessageDialog(window.getParent(), errorMessage, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private String generateErrorMessage(IHyperlinkOwner owner, IEntity relatedEntity){

        String ownerName;
        String relatedName;

        if (owner instanceof INamedElement) {
            INamedElement namedOwner = (INamedElement) owner;
            ownerName = namedOwner.getName();
        } else {
            IIdentifiedElement identifiedOwner = (IIdentifiedElement) owner;
            ownerName = identifiedOwner.getDescription();
        }
        
        if (relatedEntity instanceof INamedElement) {
            INamedElement namedRelatedElement = (INamedElement) relatedEntity;
            relatedName = namedRelatedElement.getName();
        } else {
            IIdentifiedElement identifiedRelated = (IIdentifiedElement) relatedEntity;
            relatedName = identifiedRelated.getDescription();
        }
        
        String elementNameInfo = "Illegal relationship found between " + ownerName + " and " + relatedName + ". \n The hyperlink will be deleted.";
        return elementNameInfo;
    }

    private void deleteRelationship(IHyperlinkOwner owner, IEntity relatedEntity){

        IHyperlink[] hyperlinks = owner.getHyperlinks();
        IElement namedElement = (IElement) relatedEntity;
        IElement ownerElement = (IElement) owner;

        for (IHyperlink hyperlink : hyperlinks) {
            String relatedElementId = hyperlink.getName();
            if(relatedElementId.equals(namedElement.getId())){
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
