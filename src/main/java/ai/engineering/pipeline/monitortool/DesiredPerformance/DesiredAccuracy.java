package ai.engineering.pipeline.monitortool.DesiredPerformance;

import com.change_vision.jude.api.gsn.model.IGoal;

public class DesiredAccuracy extends ConfusionMetricsPerformance {
    public DesiredAccuracy(IGoal monitoredEntity, String label, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
    }

    @Override
    public Metric getMetricsType() {
        return Metric.Accuracy;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm) {
        this.realPerformance = (float) cm.getTP() / cm.getTotal();
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm, int index) {
        this.realPerformance = (float) cm.getTP(index) / cm.getActualTotal(index);
    }
}
