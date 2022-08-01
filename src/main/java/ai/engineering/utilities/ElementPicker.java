package ai.engineering;

import java.util.List;
import java.util.ArrayList;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;


import com.change_vision.jude.api.inf.project.ModelFinder;

import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;



public class ElementPicker {
 
    public static INamedElement[] getAllNamedElements(){
        ToolUtilities utilities = ToolUtilities.getToolUtilities();
        ProjectAccessor projectAccessor = utilities.getProjectAccessor();
        
        try {
            return projectAccessor.findElements(new NamedElementPicker());
        } catch (ProjectNotFoundException e){
            String message = "Please open a project";
        }

        return null;
    }

    public static INamedElement searchNamedElements(String id){
        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();
        
        for (INamedElement iNamedElement : allNamedElements) {
            if(id.equals(iNamedElement.getId())){
                return iNamedElement;
            }
        }
        return null;
    }

    public static List<IRequirement> getAllRequirements(){
        List<IRequirement> requirements = new ArrayList<IRequirement>();

        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();

        if(allNamedElements != null){
            for (INamedElement iNamedElement : allNamedElements) {
                if(iNamedElement instanceof IRequirement){
                    IRequirement requirement = (IRequirement) iNamedElement;
                    requirements.add(requirement);
                }
            }
        }

        return requirements;
    }

    private static List<IRequirement> getCanvasElementsByPrefix(String prefix){
        List<IRequirement> requirements = ElementPicker.getAllRequirements();
        List<IRequirement> canvasElements = new ArrayList<IRequirement>();
        
        for (IRequirement requirement : requirements) {
            String[] stereotypes = requirement.getStereotypes();
            boolean isCanvasElement = false;

            for (String stereotype : stereotypes) {
                if(stereotype.startsWith(prefix)){
                    isCanvasElement = true;
                }
            }

            if(isCanvasElement){
                canvasElements.add(requirement);
            }
        }

        return canvasElements;
    }


    public static List<IRequirement> getMLCanvasElements(){
        return getCanvasElementsByPrefix("ML.");
    }

    public static List<IRequirement> getAIProjectCanvasElements(){
        return getCanvasElementsByPrefix("AI.");
    }

    public static List<IEntity> getRelatedEntities(IHyperlinkOwner selectedHyperlinkOwner){
        List<IEntity> relatedEntities = new ArrayList<IEntity>();     

        if(selectedHyperlinkOwner != null){
            IHyperlink[] hyperlinks = selectedHyperlinkOwner.getHyperlinks();
                
                try {
                    INamedElement[] elements = ElementPicker.getAllNamedElements();
                    for (int i = 0; i < hyperlinks.length; i++){
                        String id = hyperlinks[i].getName();                    
                        for (INamedElement element : elements) {
                            if(id.equals(element.getId())){
                                relatedEntities.add(element);
                            }
                        }
                    }
                } catch (Exception e) {}
            }

        return relatedEntities;
    }

    public static List<IEntity> getRelatedEntities(IEntity selectedEntity){
        List<IEntity> relatedEntities = new ArrayList<IEntity>();     

        if(selectedEntity != null){
            if(selectedEntity instanceof IHyperlinkOwner){
                IHyperlinkOwner selectedHyperlinkOwner = (IHyperlinkOwner) selectedEntity;
                
                relatedEntities = getRelatedEntities(selectedHyperlinkOwner);
            }
        }

        return relatedEntities;
    }

    public static List<IHyperlinkOwner> getAllHyperlinksOwner(){
        List<IHyperlinkOwner> hyperlinkOwners = new ArrayList<IHyperlinkOwner>();

        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();

        if(allNamedElements != null){
            for (INamedElement iNamedElement : allNamedElements) {
                if(iNamedElement instanceof IHyperlinkOwner){
                    IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) iNamedElement;
                    hyperlinkOwners.add(hyperlinkOwner);
                }
            }
        }

        return hyperlinkOwners;
    }

}
