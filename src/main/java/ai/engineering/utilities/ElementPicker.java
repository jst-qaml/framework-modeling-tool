package ai.engineering;

import java.util.List;
import java.util.ArrayList;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IHyperlink;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;

import com.change_vision.jude.api.stpa.model.*;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.gsn.model.IJustification;

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

    public static List<IIdentifiedElement> getAllIdentifiedElements(){
        INamedElement[] namedElements = getAllNamedElements();

        for (int i = 0; i < namedElements.length; i++) {
            if(namedElements[i] instanceof IStpaAnalysis){
                IStpaAnalysis stpa = (IStpaAnalysis) namedElements[i];

                List<IIdentifiedElement> identifiedElements = new ArrayList<IIdentifiedElement>();
                
                List<IAccident> accidents = stpa.getAccidents();
                for (IAccident accident : accidents) {
                    identifiedElements.add((IIdentifiedElement) accident);
                }
                
                List<ICountermeasure> countermeasures = stpa.getCountermeasures();
                for (ICountermeasure countermeasure : countermeasures) {
                    identifiedElements.add((IIdentifiedElement) countermeasure);
                }

                List<IHazardCausalFactor> hcfs = stpa.getHazardCausalFactors();
                for (IHazardCausalFactor hcf : hcfs) {
                    identifiedElements.add((IIdentifiedElement) hcf);
                }

                List<IHazard> hazards = stpa.getHazards();
                for (IHazard hazard : hazards) {
                    identifiedElements.add((IIdentifiedElement) hazard);
                }

                List<ISafetyConstraint> safetyConstraints = stpa.getSafetyConstraints();
                for (ISafetyConstraint safetyConstraint : safetyConstraints) {
                    identifiedElements.add((IIdentifiedElement) safetyConstraint);
                }

                List<IUnsafeControlAction> unsafeControlActions = stpa.getUnsafeControlActions();
                for (IUnsafeControlAction unsafeControlAction : unsafeControlActions) {
                    identifiedElements.add((IIdentifiedElement) unsafeControlAction);
                }

                return identifiedElements;
            }
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

    public static List<IGoal> getAllGoals(){
        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();

        List<IGoal> goals = new ArrayList<IGoal>();

        for (INamedElement iNamedElement : allNamedElements) {
            if(iNamedElement instanceof IGoal){
                IGoal iGoal = (IGoal) iNamedElement;
                goals.add(iGoal);
            }
        }

        return goals;
    }

    public static List<IJustification> getAllJustifications(){
        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();

        List<IJustification> justifications = new ArrayList<IJustification>();

        for (INamedElement iNamedElement : allNamedElements) {
            if(iNamedElement instanceof IJustification){
                IJustification iJustification = (IJustification) iNamedElement;
                justifications.add(iJustification);
            }
        }

        return justifications;
    }

    public static IGoal getGoalbyId(String id){
        INamedElement[] allNamedElements = ElementPicker.getAllNamedElements();

        for (INamedElement iNamedElement : allNamedElements) {
            if(iNamedElement instanceof IGoal){
                IGoal iGoal = (IGoal) iNamedElement;
                if(id.equals(iGoal.getIdentifier())){
                    return iGoal;
                }
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

    public static List<IRequirement> getMLCanvasElements(String stereotype){
        return getCanvasElementsByPrefix(stereotype);
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
                                System.out.println("found: " + id);   
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

        List<IIdentifiedElement> identifiedElements = getAllIdentifiedElements();

        for (IIdentifiedElement iIdentifiedElement : identifiedElements) {
            if(iIdentifiedElement instanceof IHyperlinkOwner){
                IHyperlinkOwner hyperlinkOwner = (IHyperlinkOwner) iIdentifiedElement;
                hyperlinkOwners.add(hyperlinkOwner);
            }
        }

        return hyperlinkOwners;
    }

}
