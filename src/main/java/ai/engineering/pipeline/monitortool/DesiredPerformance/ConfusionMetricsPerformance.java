package ai.engineering;

import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class ConfusionMetricsPerformance extends DesiredPerformance{
    
    public ConfusionMetricsPerformance(IGoal monitoredEntity, String label, String metricsType, float desiredValue, String desiredValueRange){
        super(monitoredEntity, label, metricsType, desiredValue, desiredValueRange);
        updateDescription();
    }

    @Override
    public boolean isSatisfying(){
        return realPerformance >= desiredValue;
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
        String logicLabel;

        if (label.equals("overall")) {
            logicLabel = "Overall";
        } else {
            int index = Integer.parseInt(label);
            logicLabel = labels[index+1];
        }
        
        String desiredValueString = desiredValueRange;

        if (desiredValue >= 0) {
            desiredValueString = desiredValue + "";
        }

        String logicString = " [" + metricsType + "(" + logicLabel + ") >= " + desiredValueString + "]";

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

}
