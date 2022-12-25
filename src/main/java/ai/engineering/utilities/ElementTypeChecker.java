package ai.engineering;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.presentation.IPresentation;

import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.gsn.model.*;
import com.change_vision.jude.api.stpa.model.*;

public class ElementTypeChecker {

    public static ModelType getModelType(IEntity entity){
        if(isAIProjectCanvasElement(entity)){
            return ModelType.AI_PROJECT_CANVAS;
        }

        if(isMLCanvasElement(entity)){
            return ModelType.ML_CANVAS;
        }

        if(isKAOSGoal(entity)){
            return ModelType.KAOS;
        }

        if(isArchitectureElement(entity)){
            return ModelType.ARCHITECTURAL;
        }

        if(isSafetyCaseElement(entity)){
            return ModelType.SAFETY_CASE;
        }

        if(isStpaElement(entity)){
            return ModelType.STPA;
        }

        return ModelType.UNKNOWN;
    }

    public static String getMLCanvasElementName(IRequirement req){
        if(req.hasStereotype("ML.PredictionTask")){
            return "ML Canvas - Prediction Task";
        }
        else if(req.hasStereotype("ML.ImpactSimulation")){
            return "ML Canvas - Impact Simulation";
        }
        else if(req.hasStereotype("ML.Decision")){
            return "ML Canvas - Decision";
        }
        else if(req.hasStereotype("ML.MakingPrediction")){
            return "ML Canvas - Making Prediction";
        }
        else if(req.hasStereotype("ML.ValueProposition")){
            return "ML Canvas - Value Proposition";
        }
        else if(req.hasStereotype("ML.DataCollection")){
            return "ML Canvas - Data Collection";
        }
        else if(req.hasStereotype("ML.BuildingModels")){
            return "ML Canvas - Building Models";
        }
        else if(req.hasStereotype("ML.DataSources")){
            return "ML Canvas - Data Sources";
        }
        else if(req.hasStereotype("ML.Features")){
            return "ML Canvas - Features";
        }
        else if(req.hasStereotype("ML.LiveMonitoring")){
            return "ML Canvas - Live Monitoring";
        }

        return "";
    }

    public static MLCanvasType getMLCanvasElementType(IRequirement req){
        if(req.hasStereotype("ML.PredictionTask")){
            return MLCanvasType.PREDICTION_TASK;
        }
        else if(req.hasStereotype("ML.ImpactSimulation")){
            return MLCanvasType.IMPACT_SIMULATION;
        }
        else if(req.hasStereotype("ML.Decision")){
            return MLCanvasType.DECISION;
        }
        else if(req.hasStereotype("ML.MakingPrediction")){
            return MLCanvasType.MAKING_PREDICTION;
        }
        else if(req.hasStereotype("ML.ValueProposition")){
            return MLCanvasType.VALUE_PROPOSITION;
        }
        else if(req.hasStereotype("ML.DataCollection")){
            return MLCanvasType.DATA_COLLECTION;
        }
        else if(req.hasStereotype("ML.BuildingModels")){
            return MLCanvasType.BUILDING_MODELS;
        }
        else if(req.hasStereotype("ML.DataSources")){
            return MLCanvasType.DATA_SOURCES;
        }
        else if(req.hasStereotype("ML.Features")){
            return MLCanvasType.FEATURES;
        }
        else if(req.hasStereotype("ML.LiveMonitoring")){
            return MLCanvasType.LIVE_MONITORING;
        }

        return MLCanvasType.UNIDENTIFIED;
    }

    public static boolean isMLCanvasElement(IEntity entity){

        boolean isMLCanvasElement = false;

        if(entity instanceof IRequirement){
            IRequirement requirement = (IRequirement) entity;

            String[] stereotypes = requirement.getStereotypes();

            for (String stereotype : stereotypes) {
                if(stereotype.startsWith("ML.")){
                    isMLCanvasElement = true;
                }
            }
        }

        return isMLCanvasElement;
    }

    public static boolean isAIProjectCanvasElement(IEntity entity){

        boolean isAIProjectCanvasElement = false;

        if(entity instanceof IRequirement){
            IRequirement requirement = (IRequirement) entity;
            String[] stereotypes = requirement.getStereotypes();

            for (String stereotype : stereotypes) {
                if(stereotype.startsWith("AI.")){
                    isAIProjectCanvasElement = true;
                }
            }
        }

        return isAIProjectCanvasElement;
    }

    public static AIProjectCanvasType getAIProjectCanvasElementType(IRequirement req){
        if(req.hasStereotype("AI.Data")){
            return AIProjectCanvasType.DATA;
        }
        else if(req.hasStereotype("AI.Skills")){
            return AIProjectCanvasType.SKILLS;
        }
        else if(req.hasStereotype("AI.Output")){
            return AIProjectCanvasType.OUTPUT;
        }
        else if(req.hasStereotype("AI.ValueProposition")){
            return AIProjectCanvasType.VALUE_PROPOSITION;
        }
        else if(req.hasStereotype("AI.Integration")){
            return AIProjectCanvasType.INTEGRATION;
        }
        else if(req.hasStereotype("AI.Stakeholders")){
            return AIProjectCanvasType.STAKEHOLDERS;
        }
        else if(req.hasStereotype("AI.Customers")){
            return AIProjectCanvasType.CUSTOMERS;
        }
        else if(req.hasStereotype("AI.Cost")){
            return AIProjectCanvasType.COST;
        }
        else if(req.hasStereotype("AI.Revenue")){
            return AIProjectCanvasType.REVENUE;
        }

        return AIProjectCanvasType.UNIDENTIFIED;
    }

    public static boolean isKAOSGoal(IEntity entity){
        
        boolean isGoal = false;

        if(entity instanceof IGoal){
            IGoal goalElement = (IGoal) entity;
        
            try{
                IPresentation[] presentations = goalElement.getPresentations();
                IDiagram diagram = presentations[0].getDiagram();
                String diagramName = diagram.getName();
                if(diagramName.contains("KAOS")){
                    isGoal = true;
                }
            }catch(Exception e){}
        }
        
        return isGoal;
    }

    public static boolean isSafetyCaseElement(IEntity entity){
        
        if(isKAOSGoal(entity)){
            return false;
        }

        return (entity instanceof IGsnElement);
        
    }

    public static boolean isArchitectureElement(IEntity entity){
        return (entity instanceof IBlock);
    }

    public static boolean isStpaElement(IEntity entity){

        if(entity instanceof IStampElement){
            return true;
        }

        return false;
    }

}
