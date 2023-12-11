package ai.engineering.pipeline.monitortool.DesiredPerformance;

import com.change_vision.jude.api.gsn.model.IGoal;

public class DesiredRobustness extends DesiredPerformance {
    public DesiredRobustness(IGoal monitoredEntity, String label, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
        updateDescription();
    }

    @Override
    public Metric getMetricsType() {
        return Metric.Robustness;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm) {
        this.realPerformance = 3;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm, int index) {
        this.realPerformance = 3;
    }

    @Override
    public boolean isSatisfying() {
        return realPerformance >= desiredValue;
    }
}
