package ai.engineering;

import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.gsn.model.IGoal;

public abstract class DesiredPerformance {
    
    protected IGoal monitoredEntity;
    protected String label;
    protected String metricsType;
    protected float desiredValue;
    protected float realPerformance;
    protected String desiredValueRange;

    public DesiredPerformance(IGoal monitoredEntity, String label, String metricsType, float desiredValue, String desiredValueRange){
        this.label = label;
        this.monitoredEntity = monitoredEntity;
        this.metricsType = metricsType;
        this.desiredValue = desiredValue;
        this.desiredValueRange = desiredValueRange;

        realPerformance = -1.0f;
    }

    public void updateDescription(){}

    public IGoal getMonitoredEntity(){
        return monitoredEntity;
    }

    public String getLabel(){
        return "";
    }

    public void setRealPerformance(float realPerformance){
        this.realPerformance = realPerformance;
    }

    public void setDesiredValue(float desiredValue){
        this.desiredValue = desiredValue;
    }

    public boolean isTested(){
        return realPerformance >= 0.0;
    }

    public boolean isSatisfying(){
        if (desiredValue < 0.0f) {
            return true;
        }

        return realPerformance > desiredValue;
    }

    public boolean isSame(IGoal selectedEntity){
        return monitoredEntity == selectedEntity;
    }

    public String getMetricsType(){
        return metricsType;
    }

    public float getDesiredValue(){
        return desiredValue;
    }

    public float getRealPerformance(){
        return realPerformance;
    }

    public String getDesiredValueRange(){
        return desiredValueRange;
    }

}
