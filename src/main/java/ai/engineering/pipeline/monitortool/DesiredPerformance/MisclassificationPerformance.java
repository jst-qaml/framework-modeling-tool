package ai.engineering;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class MisclassificationPerformance extends DesiredPerformance{
    
    private String targetLabel;

    public MisclassificationPerformance(IGoal monitoredEntity, String label, String targetLabel, float desiredValue, String desiredValueRange){
        super(monitoredEntity, label, "Misclassification", desiredValue, desiredValueRange);
        this.targetLabel = targetLabel;
        updateDescription();
        System.out.println(label);
        System.out.println(targetLabel);
    }

    @Override
    public boolean isSatisfying(){
        if (desiredValue < 0.0f) {
            return true;
        }

        return realPerformance <= desiredValue;
    }

    @Override
    public void updateDescription(){

        String goalStatement = monitoredEntity.getContent();

        int logicIndex = goalStatement.indexOf("[");
        if(logicIndex != -1){
            goalStatement = goalStatement.substring(0, logicIndex);
        }

        goalStatement = goalStatement.trim();

        String[] labels = VersionFetcher.GetLabels(true);
        String monitoredLabelString;

        if (label.equals("overall")) {
            monitoredLabelString = "Overall";
        } else {
            int index = Integer.parseInt(label);
            monitoredLabelString = labels[index+1];
        }

        String targetLabelString;
        if (targetLabel.equals("overall")) {
            targetLabelString = "Overall";
        } else {
            int index = Integer.parseInt(targetLabel);
            targetLabelString = labels[index+1];
        }

        String desiredValueString = desiredValueRange;

        if (desiredValue >= 0) {
            desiredValueString = desiredValue + "";
        }
        
        String logicString = " [" + metricsType + "(" + monitoredLabelString + ", " + targetLabelString + ") <= " + desiredValueString + "]";

        goalStatement = goalStatement + " " + logicString;

        ToolUtilities toolUtilities = ToolUtilities.getToolUtilities();
        ITransactionManager transactionManager = toolUtilities.getTransactionManager();

        try {
            transactionManager.beginTransaction();
            monitoredEntity.setContent(goalStatement);
            transactionManager.endTransaction();
        } catch (Exception e) {
            transactionManager.abortTransaction();
        }
    }

    @Override
    public String getLabel(){
        return label;
    }

    public String getTargetLabel(){
        return targetLabel;
    }

}
