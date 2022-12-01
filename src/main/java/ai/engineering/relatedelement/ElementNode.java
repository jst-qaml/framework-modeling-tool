package ai.engineering;

import java.awt.*;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.gsn.model.IGoal;

public class ElementNode {
    
    private Dimension position;
    private Dimension blockSize;
    private Dimension textSize;
    private Dimension typeSize;
    
    private IEntity entity;
    private ModelType modelType;
    private String text;

    public ElementNode(IEntity entity){
        modelType = ModelType.ML_CANVAS;
        this.entity = entity;
        processEntityData();
        position = new Dimension(200,200);
    }

    private void processEntityData(){
        modelType = ElementTypeChecker.getModelType(entity);
        
        if(modelType == ModelType.AI_PROJECT_CANVAS || modelType == ModelType.ML_CANVAS){
            IRequirement canvasElement = (IRequirement) entity;
            text = canvasElement.getName();
        }

        if(modelType == ModelType.KAOS || modelType == ModelType.SAFETY_CASE){
            IGoal goal = (IGoal) entity;
            text = goal.getContent();
        }

    }

    private Dimension calculateStringDimension(String text, FontMetrics fontMetrics){
        int stringWidth = fontMetrics.stringWidth(text);
        int stringHeight = fontMetrics.getHeight();
        return new Dimension(stringWidth,stringHeight);
    }

    private void setBlockSize(){
        int blockWidth = (int) textSize.getWidth() + 30;
        int blockHeight = (int) textSize.getHeight() * 2;
        blockSize = new Dimension(blockWidth, blockHeight);
    }

    public void setxPosition(int xPosition){
        position.width = xPosition;
    }

    public void setyPosition(int yPosition){
        position.height = yPosition;
    }

    public int getxPosition(){
        return position.width;
    }

    public int getyPosition(){
        return position.height;
    }

    public int getBlockxSize(){
        return blockSize.width;
    }

    public int getBlockySize(){
        return blockSize.height;
    }

    public String getText(){
        return text;
    }

    public int getTextxPosition(){
        return (int) (getxPosition() + (getBlockxSize()/2) - (textSize.getWidth()/2));
    }

    public int getTextyPosition(){
        return getTypeyPosition() + 15;
    }

    public ModelType getModelType(){
        return modelType;
    }


    public String getModelTypeText(){
        return "<" + modelType + ">";
    }

    public int getTypexPosition(){
        return (int) (getxPosition() + (getBlockxSize()/2) - (typeSize.getWidth()/2));
    }

    public int getTypeyPosition(){
        return getyPosition() + 15;
    }

    public int getLeftAnchorx(){
        return getxPosition();
    }

    public int getRightAnchorx(){
        return getxPosition() + getBlockxSize();
    }

    public int getAnchory(){
        return getyPosition() + (getBlockySize()/2);
    }

    public void paint(Graphics g, ElementNode centralNode){
        calculatePosition(centralNode);
        paint(g);
    }

    public void paint(Graphics g){

        textSize = calculateStringDimension(text, g.getFontMetrics());
        typeSize = calculateStringDimension(getModelTypeText(), g.getFontMetrics());

        setBlockSize();

        g.setColor(Color.RED);
        g.fillRect(getxPosition(), getyPosition(), getBlockxSize(), getBlockySize());
        g.setColor(Color.WHITE);
        g.drawString(getModelTypeText(), getTypexPosition(), getTypeyPosition());
        g.drawString(getText(), getTextxPosition(), getTextyPosition());
    }

    private void calculatePosition(ElementNode centralNode){

        int modelDistance = (int) modelType.getModelTypeasInt() - (int) centralNode.getModelType().getModelTypeasInt();
        
        if(modelDistance < 0){
            position = new Dimension(centralNode.getxPosition() - getBlockxSize() + 10, centralNode.getyPosition());
        }else{
            position = new Dimension(centralNode.getxPosition() + centralNode.getBlockxSize() + 10, centralNode.getyPosition());
        }


    }

}
