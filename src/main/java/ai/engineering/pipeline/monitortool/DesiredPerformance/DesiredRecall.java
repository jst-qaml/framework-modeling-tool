package ai.engineering.pipeline.monitortool.DesiredPerformance;

import com.change_vision.jude.api.gsn.model.IGoal;

public class DesiredRecall extends ConfusionMetricsPerformance {
    public DesiredRecall(IGoal monitoredEntity, String label, float desiredValue) {
        super(monitoredEntity, label, desiredValue);
    }

    @Override
    public Metric getMetricsType() {
        return Metric.Recall;
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm) {
        this.realPerformance = (float) cm.getTP() / (cm.getTP() + cm.getFP());
    }

    @Override
    public void setRealPerformance(ConfusionMatrix cm, int index) {
        this.realPerformance = (float) cm.getTP(index) / (cm.getTP(index) + cm.getFP(index));
    }

}
