package ai.engineering;

import java.util.LinkedList;

import org.w3c.dom.Element;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.IHyperlinkOwner;
import com.change_vision.jude.api.inf.model.IRequirement;

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
                break;
            case SAFETY_CASE:
                stereotypes.add(new StereotypeNode("Safety Case"));
                name = "Safety Case Element";
                break;
        }
    }

    public void addStereotype(StereotypeNode stereotype){
       stereotypes.add(stereotype);
    }

 }