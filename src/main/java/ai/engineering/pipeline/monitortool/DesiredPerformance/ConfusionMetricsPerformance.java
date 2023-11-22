package ai.engineering.pipeline.monitortool.DesiredPerformance;

import ai.engineering.pipeline.VersionFetcher;
import ai.engineering.pipeline.monitortool.Metric;
import ai.engineering.utilities.ToolUtilities;
import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

public class ConfusionMetricsPerformance extends DesiredPerformance{
    
    public ConfusionMetricsPerformance(IGoal monitoredEntity, String label, Metric metricsType, float desiredValue){
        super(monitoredEntity, label, metricsType, desiredValue);
        updateDescription();
    }

    @Override
    public boolean isSatisfying(){
        return realPerformance >= desiredValue;
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
        String logicLabel;

        if (label.equals("overall")) {
            logicLabel = "Overall";
        } else {
            int index = Integer.parseInt(label);
            logicLabel = labels[index+1];
        }
        
        String logicString = " [" + metricsType + "(" + logicLabel + ") >= " + desiredValue + "]";

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
