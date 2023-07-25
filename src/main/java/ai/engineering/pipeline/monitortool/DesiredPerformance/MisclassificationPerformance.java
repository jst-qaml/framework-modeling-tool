package ai.engineering;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class MisclassificationPerformance extends DesiredPerformance{
    
    private String targetLabel;

    public MisclassificationPerformance(IGoal monitoredEntity, String label, String targetLabel, float desiredValue){
        super(monitoredEntity, label, "Misclassification", desiredValue);
        this.targetLabel = targetLabel;
        updateDescription();
        System.out.println(label);
        System.out.println(targetLabel);
    }

    @Override
    public boolean isSatisfying(){
        return realPerformance <= desiredValue;
    }

    @Override
    protected void updateDescription(){

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
        
        String logicString = " [" + metricsType + "(" + monitoredLabelString + ", " + targetLabelString + ") <= " + desiredValue + "]";

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
