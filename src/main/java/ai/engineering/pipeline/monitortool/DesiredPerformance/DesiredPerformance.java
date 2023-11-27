package ai.engineering.pipeline.monitortool.DesiredPerformance;

import ai.engineering.pipeline.VersionFetcher;
import ai.engineering.utilities.ToolUtilities;
import com.change_vision.jude.api.gsn.model.IGoal;
import com.change_vision.jude.api.inf.editor.ITransactionManager;

public abstract class DesiredPerformance {

    protected IGoal monitoredEntity;
    protected String label;
    protected float desiredValue;
    protected float realPerformance;

    public DesiredPerformance(IGoal monitoredEntity, String label, float desiredValue) {
        this.monitoredEntity = monitoredEntity;
        this.label = label;
        this.desiredValue = desiredValue;

        realPerformance = -1.0f;
    }

    protected String getTargetLabel() {
        String[] labels = VersionFetcher.GetLabels(true);
        String monitoredLabel;
        if (label.equals("overall")) {
            monitoredLabel = "Overall";
        } else {
            int index = Integer.parseInt(label);
            monitoredLabel = labels[index + 1];
        }

        return " [" + getMetricsType() + "(" + monitoredLabel + ") >= " + desiredValue + "]";
    }

    protected void updateDescription() {
        String goalStatement = monitoredEntity.getContent();

        int logicIndex = goalStatement.indexOf("[");
        if (logicIndex != -1) {
            goalStatement = goalStatement.substring(0, logicIndex);
        }

        goalStatement = goalStatement.trim() + " " + getTargetLabel();

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

    public IGoal getMonitoredEntity() {
        return monitoredEntity;
    }

    public String getLabel() {
        return label;
    }

    public abstract Metric getMetricsType();

    public abstract void setRealPerformance(ConfusionMatrix cm);

    public abstract void setRealPerformance(ConfusionMatrix cm, int index);

    public boolean isTested() {
        return realPerformance >= 0.0;
    }

    public abstract boolean isSatisfying();

    public float getDesiredValue() {
        return desiredValue;
    }

    public float getRealPerformance() {
        return realPerformance;
    }

}
