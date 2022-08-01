package ai.engineering;

import com.change_vision.jude.api.inf.exception.*;

import com.change_vision.jude.api.inf.presentation.IPresentation;

import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.gsn.model.IGoal;

public class ElementTypeChecker {
    
    public static enum MLCanvasType{
        PREDICTION_TASK,
        IMPACT_SIMULATION,
        DECISION,
        MAKING_PREDICTION,
        VALUE_PROPOSITION,
        DATA_COLLECTION,
        BUILDING_MODELS,
        DATA_SOURCES,
        FEATURES,
        LIVE_MONITORING,
        UNIDENTIFIED
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

    public static boolean isKAOSGoal(IEntity entity){
        
        boolean isGoal = false;

        if(entity instanceof IGoal){
            IGoal goalElement = (IGoal) entity;
        
            try{
                IPresentation[] presentations = goalElement.getPresentations();
                IDiagram diagram = presentations[0].getDiagram();
                String diagramName = diagram.getName();
                if(diagramName.equals("KAOS")){
                    isGoal = true;
                }
            }catch(Exception e){}
        }
        
        return isGoal;
    }

    public static boolean isSafetyGoal(IEntity entity){
        
        boolean isSafetyGoal = false;
        
        if(entity instanceof IGoal){
            IGoal goalElement = (IGoal) entity;
            
            if(!isKAOSGoal(entity)){
                isSafetyGoal = true;
            }
        }

        return isSafetyGoal;
    }

}
