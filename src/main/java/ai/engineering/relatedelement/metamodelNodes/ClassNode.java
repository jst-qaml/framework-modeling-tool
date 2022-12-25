package ai.engineering;

import java.lang.annotation.ElementType;
import java.util.LinkedList;

import org.w3c.dom.Element;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.stpa.model.*;

public class ClassNode{
    public String id;
    public String name;
    public LinkedList<StereotypeNode> stereotypes;

    public ClassNode(Element element){
       stereotypes = new LinkedList<StereotypeNode>();
       id = element.getAttribute("xmi.id");
       name = element.getAttribute("name").replace('+', ' ');
    }

    public ClassNode(IEntity entity){
        stereotypes = new LinkedList<StereotypeNode>();

        ModelType modelType = ElementTypeChecker.getModelType(entity);

        switch(modelType){
            case ML_CANVAS:
                stereotypes.add(new StereotypeNode("ML Project Canvas"));
                IRequirement canvasElement = (IRequirement) entity;
                MLCanvasType mlCanvasType = ElementTypeChecker.getMLCanvasElementType(canvasElement);
                switch(mlCanvasType){
                    case PREDICTION_TASK:
                        name = "Prediction Task";
                        break;
                    case IMPACT_SIMULATION:
                        name = "Impact Simulation";
                        break;
                    case DECISION:
                        name = "Decision";
                        break;
                    case MAKING_PREDICTION:
                        name = "Making Prediction";
                        break;
                    case VALUE_PROPOSITION:
                        name = "Value Proposition";
                        break;
                    case DATA_COLLECTION:
                        name = "Data Collection";
                        break;
                    case BUILDING_MODELS:
                        name = "Building Model";
                        break;
                    case DATA_SOURCES:
                        name = "Data Source";
                        break;
                    case FEATURES:
                        name = "Feature";
                        break;
                    case LIVE_MONITORING:
                        name = "Monitoring";
                        break;
                }
                break;
            case AI_PROJECT_CANVAS:
                stereotypes.add(new StereotypeNode("AI Project Canvas"));
                IRequirement aicanvasElement = (IRequirement) entity;
                AIProjectCanvasType aiProjectCanvasType = ElementTypeChecker.getAIProjectCanvasElementType(aicanvasElement);
                switch(aiProjectCanvasType){
                    case DATA:
                        name = "Data";
                        break;
                    case SKILLS:
                        name = "Skill";
                        break;
                    case OUTPUT:
                        name = "Output (Measure)";
                        break;
                    case VALUE_PROPOSITION:
                        name = "Value Proposition";
                        break;
                    case INTEGRATION:
                        name = "Integration";
                        break;
                    case STAKEHOLDERS:
                        name = "Stakeholder";
                        break;
                    case CUSTOMERS:
                        name = "Customer";
                        break;
                    case COST:
                        name = "Cost";
                        break;
                    case REVENUE:
                        name = "Revene";
                        break;
                }
                break;
            case KAOS:
                stereotypes.add(new StereotypeNode("Goal"));
                name = "Goal";
                break;
            case ARCHITECTURAL:
                stereotypes.add(new StereotypeNode("SysML"));
                name = "Component";
                break;
            case SAFETY_CASE:
                stereotypes.add(new StereotypeNode("Safety Case"));
                if(entity instanceof IGoal){
                    name = "Safety Goal";
                }else if(entity instanceof IStrategy){
                    name = "Argument";
                }else if(entity instanceof ISolution){
                    name = "Solution";
                }
                break;
            case STPA:
                stereotypes.add(new StereotypeNode("STAMP"));
                if(entity instanceof IAccident){
                    name = "Accident";
                }else if(entity instanceof IHazard){
                    name = "Hazard";
                }else if(entity instanceof ISafetyConstraint){
                    name = "Safety Constraint";
                }else if(entity instanceof IComponent){
                    name = "Entity";
                }else if(entity instanceof IHazardCausalFactor){
                    name = "Hazard Causal Factor";
                }else if(entity instanceof ICountermeasure){
                    name = "Countermeasure";
                }else if(entity instanceof IUnsafeControlAction){
                    name = "Unsafe Control Action";
                }
                break;
        }
    }

    public void addStereotype(StereotypeNode stereotype){
       stereotypes.add(stereotype);
    }

 }