package ai.engineering.pipeline.monitortool.DesiredPerformance;

import ai.engineering.pipeline.VersionFetcher;
import com.change_vision.jude.api.gsn.model.IGoal;

public abstract class ConfusionMetricsPerformance extends DesiredPerformance {

    public ConfusionMetricsPerformance(IGoal monitoredEntity, String label, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
        updateDescription();
    }

    @Override
    public boolean isSatisfying() {
        return realPerformance >= desiredValue;
    }

    @Override
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

    @Override
    public String getLabel() {
        return label;
    }

}
