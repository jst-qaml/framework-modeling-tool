package ai.engineering.pipeline.monitortool.DesiredPerformance;

import ai.engineering.pipeline.VersionFetcher;
import com.change_vision.jude.api.gsn.model.IGoal;

public class MisclassificationPerformance extends DesiredPerformance {

    private final String targetLabel;

    public MisclassificationPerformance(IGoal monitoredEntity, String label, String targetLabel, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
        this.targetLabel = targetLabel;
        updateDescription();
        System.out.println(label);
        System.out.println(targetLabel);
    }

    @Override
    public boolean isSatisfying() {
        return realPerformance <= desiredValue;
    }

    public String getTargetLabel() {
        String[] labels = VersionFetcher.GetLabels(true);
        String monitoredLabel;

        if (label.equals("overall")) {
            monitoredLabel = "Overall";
        } else {
            int index = Integer.parseInt(label);
            monitoredLabel = labels[index + 1];
        }

        String targetLabelString;
        if (targetLabel.equals("overall")) {
            targetLabelString = "Overall";
        } else {
            int index = Integer.parseInt(targetLabel);
            targetLabelString = labels[index + 1];
        }

        return " [" + getMetricsType() + "(" + monitoredLabel + ", " + targetLabelString + ") <= " + desiredValue + "]";
    }

    @Override
    public Metric getMetricsType() {
        return Metric.Misclassification;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm) {

    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm, int index) {

    }

}
