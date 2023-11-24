package ai.engineering.pipeline.monitortool.DesiredPerformance;

import com.change_vision.jude.api.gsn.model.IGoal;

public class DesiredPrecision extends ConfusionMetricsPerformance {
    public DesiredPrecision(IGoal monitoredEntity, String label, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
    }

    @Override
    public Metric getMetricsType() {
        return Metric.Precision;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm) {
        this.realPerformance = (float) cm.getTP() / (cm.getTP() + cm.getFN());
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm, int index) {
        this.realPerformance = (float) cm.getTP(index) / (cm.getTP(index) + cm.getFN(index));
    }
}
