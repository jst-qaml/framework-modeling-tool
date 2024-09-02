package ai.engineering;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.gsn.model.IJustification;

import java.util.List;

import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;

import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class TestEvidenceChecker implements IPluginActionDelegate{
    
    public Object run(IWindow window){
	    
        List<IJustification> justifications = ElementPicker.getAllJustifications();

        for (IJustification justification : justifications) {
            String content = justification.getContent();

            if (content.contains("[FAIL]")) {
                try {
                    IPresentation[] presentations = justification.getPresentations();
                
                    for (IPresentation iPresentation : presentations) {
                        String hexColor = "#FF0000";
                        setColor(iPresentation, hexColor);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }   
            }
        }

	    return null;
	}

    private void setColor(IPresentation iPresentation, String hexColor){

        String currentColor = iPresentation.getProperty("fill.color");
        if(currentColor.equals(hexColor)){
            return;
        }

        ToolUtilities utilities = ToolUtilities.getToolUtilities();

        IElement model = iPresentation.getModel();
        
        if (model instanceof IGoal) {
            IGoal goal = (IGoal) model;
            if (goal.isUndeveloped()) {
                boolean isOrNodeFailed = true;
                IDiagram diagram = iPresentation.getDiagram();
                try {
                    IPresentation[] presentations = diagram.getPresentations();
                    for (IPresentation presentation : presentations) {
                        if(presentation instanceof ILinkPresentation){
                            ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                            IPresentation source = linkPresentation.getSourceEnd();
                            if(source.getLabel() == iPresentation.getLabel()){
                                IPresentation subGoal = linkPresentation.getTargetEnd();
                                String subGoalColor = iPresentation.getProperty("fill.color");
                                isOrNodeFailed = isOrNodeFailed && subGoalColor.equals(hexColor);
                            }              
                        }
                    }
                if (!isOrNodeFailed) {
                    return;
                }

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        if(model instanceof IRequirement){
            IRequirement req = (IRequirement) iPresentation.getModel();
            CanvasElement canvasElement = CanvasElementCollection.findCanvasElement(req);
            canvasElement.setColor(hexColor);
        }else{
            ITransactionManager transactionManager = utilities.getTransactionManager();
            try {
                transactionManager.beginTransaction();
                iPresentation.setProperty("fill.color", hexColor);
                transactionManager.endTransaction();
            } catch (Exception exc) {
                transactionManager.abortTransaction();
            }
        }

        IDiagram openDiagram = iPresentation.getDiagram();

        try {
            IPresentation[] presentations = openDiagram.getPresentations();
            for (IPresentation presentation : presentations) {
                if(presentation instanceof ILinkPresentation){
                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                    IPresentation target = linkPresentation.getTargetEnd();
                    if(target.getLabel() == iPresentation.getLabel()){
                        IPresentation source = linkPresentation.getSourceEnd();
                        setColor(source, hexColor);
                    }              
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) iPresentation;

        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

        for (IEntity iEntity : relatedEntities) {
            if (iEntity instanceof IPresentation) {
                IPresentation presentation = (IPresentation) iEntity;
                setColor(presentation, hexColor);
            }

            if(iEntity instanceof IElement){
                IElement relatedElement = (IElement) iEntity;
                try {
                    IPresentation[] presentations = relatedElement.getPresentations();
                    for (int i = 0; i < presentations.length; i++) {
                        setColor(presentations[i], hexColor);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } 
            }
        }
    }

    private void setColor(IElement element, String hexColor){

        if(element instanceof IRequirement){
            IRequirement req = (IRequirement) element;
            CanvasElement canvasElement = CanvasElementCollection.findCanvasElement(req);
            if (canvasElement.isSameColor(hexColor)) {
                return;   
            } else {
                canvasElement.setColor(hexColor);
            }          
        }else{
            ToolUtilities utilities = ToolUtilities.getToolUtilities();
            ITransactionManager transactionManager = utilities.getTransactionManager();

            try {
                IPresentation[] presentations = null;

                if(element != null){
                    presentations = element.getPresentations();
                }

                if(element instanceof IGoal){
                    IGoal goal = (IGoal) element;
                    if(goal.isUndeveloped()){
                        boolean isOrNodeFailed = true;
                        for (IPresentation iPresentation : presentations) {
                            IDiagram diagram = iPresentation.getDiagram();

                            IPresentation[] relatedpresentations = diagram.getPresentations();
                            for (IPresentation presentation : relatedpresentations) {
                                if(presentation instanceof ILinkPresentation){
                                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                                    IPresentation source = linkPresentation.getSourceEnd();
                                    if(source.getLabel().equals(iPresentation.getLabel())){
                                        IPresentation subGoal = linkPresentation.getTargetEnd();
                                        String subGoalColor = subGoal.getProperty("fill.color");
                                        isOrNodeFailed = isOrNodeFailed && subGoalColor.equals(hexColor);
                                    }
                                }
                            }    
                        }
                        if (!isOrNodeFailed) {
                            return;
                        }
                    }
                }

                if (presentations != null) {
                    for (IPresentation iPresentation : presentations) {
                        if (iPresentation != null) {
                            String currentColor = iPresentation.getProperty("fill.color");
                        
                            if(currentColor.equals(hexColor)){
                                return;
                            }                      

                            transactionManager.beginTransaction();
                            iPresentation.setProperty("fill.color", hexColor);
                            transactionManager.endTransaction();
        
                            System.out.println("Colored element " + iPresentation.getLabel() + " with color " + iPresentation.getProperty("fill.color"));

                            IDiagram openDiagram = iPresentation.getDiagram();

                            IPresentation[] relatedpresentations = openDiagram.getPresentations();
                            for (IPresentation presentation : relatedpresentations) {
                                if(presentation instanceof ILinkPresentation){
                                    ILinkPresentation linkPresentation = (ILinkPresentation) presentation;
                                    IPresentation target = linkPresentation.getTargetEnd();
                                    IPresentation source = linkPresentation.getSourceEnd();
                                    if(openDiagram instanceof IInternalBlockDiagram){
                                        if(source.getLabel().equals(iPresentation.getLabel())){
                                            setColor(target.getModel(), hexColor);
                                        }
                                    }else{
                                        if(target.getLabel() == iPresentation.getLabel()){
                                            setColor(source.getModel(), hexColor);
                                        }
                                    }
                                }
                           }
                        }
                    }
                }
            } catch (Exception e) {
                transactionManager.abortTransaction();
            }
        }

        IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) element;
        List<IEntity> relatedEntities = ElementPicker.getRelatedEntities(hyperlinkOwner);

        for (IEntity iEntity : relatedEntities) {
            if(iEntity instanceof IElement){
                IElement relatedElement = (IElement) iEntity;
                setColor(relatedElement, hexColor);
            }    
        }

    }

}
