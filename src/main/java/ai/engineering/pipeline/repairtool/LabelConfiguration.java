package ai.engineering;

import com.change_vision.jude.api.inf.presentation.IPresentation;

public class LabelConfiguration {
    
    private IPresentation presentation;
    private String label, repairPriority, preventDegradation;

    public LabelConfiguration(IPresentation presentation, String label, String repairPriority, String preventDegradation){
        this.presentation = presentation;
        this.label = label;
        this.repairPriority = repairPriority;
        this.preventDegradation = preventDegradation;
    }

    public void updateValue(String repairPriority, String preventDegradation){
        this.repairPriority = repairPriority;
        this.preventDegradation = preventDegradation;
    }

    public boolean isEqual(IPresentation presentation){
        return this.presentation == presentation;
    }

    public IPresentation getPresentation(){
        return presentation;
    }

    public String getLabel(){
        return label;
    }

    public String getRepairPriority(){
        return repairPriority;
    }

    public String getPreventDegradation(){
        return preventDegradation;
    }

}
