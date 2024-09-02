package ai.engineering;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Icon;
import javax.swing.JButton;

import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IElement;

import com.change_vision.jude.api.gsn.model.IGoal;

import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.entypo.Entypo;

public class MonitoringConfigurations {
    
    private static List<DesiredPerformance> desiredPerformances;

    public static void initializeConfigurations(){
        if(desiredPerformances == null){
            desiredPerformances = new CopyOnWriteArrayList<DesiredPerformance>();
        }

        List<IGoal> goals = ElementPicker.getAllGoals();
        for (IGoal iGoal : goals) {
            DesiredPerformance desiredPerformance = GoalParser.parseGoal(iGoal);
            if (desiredPerformance != null) {
                desiredPerformances.add(desiredPerformance);
            }
        }
    }

    public static void addDesiredPerformance(DesiredPerformance newConfig){
        if(desiredPerformances == null){
            desiredPerformances = new CopyOnWriteArrayList<DesiredPerformance>();
        }

        if (newConfig == null) {
            return;
        }

        IGoal monitoredEntity = newConfig.getMonitoredEntity();
        DesiredPerformance oldConfig =  null;

        for (DesiredPerformance desiredPerformance : desiredPerformances) {
            if (desiredPerformance.getMonitoredEntity() == monitoredEntity){
                oldConfig = desiredPerformance;
            }
        }

        if (oldConfig != null) {
            desiredPerformances.remove(oldConfig);
        }

        desiredPerformances.add(newConfig);

    }

    public static List<DesiredPerformance> getDesiredPerformances(){
        if(desiredPerformances == null){
            desiredPerformances = new CopyOnWriteArrayList<DesiredPerformance>();
        }

        return desiredPerformances;
    }

    public static DesiredPerformance findDesiredPerformance(IPresentation presentation){

        if(desiredPerformances == null){
            desiredPerformances = new CopyOnWriteArrayList<DesiredPerformance>();
        }

        if (presentation == null) {
            return null;
        }

        if(!(presentation.getModel() instanceof IGoal)){
            return null;
        }

        IGoal goal = (IGoal) presentation.getModel();
        
        DesiredPerformance out = null;

        for (DesiredPerformance desiredPerformance : desiredPerformances) {
            if (desiredPerformance.getMonitoredEntity() == goal){
                out = desiredPerformance;
            }
        }

        return out;
    }    

    public static Object[][] createSummaryTable(boolean isRealValueIncluded){

        if(desiredPerformances == null){
            return new String[10][10];
        }

        Object[][] rowData = new Object[desiredPerformances.size()][8];

        String[] labels = VersionFetcher.GetLabels(true);
        
        for (int i = 0; i < desiredPerformances.size(); i++) {
            DesiredPerformance desiredPerformance = desiredPerformances.get(i);

            IElement element = desiredPerformance.getMonitoredEntity();

            if(element instanceof INamedElement){
                INamedElement namedElement = (INamedElement) element;
                rowData[i][0] = namedElement.getName();
            }

            String labelString = desiredPerformance.getLabel();
            Integer labelIndex = 0;

            if(!labelString.equalsIgnoreCase("overall")){
                labelIndex = Integer.parseInt(labelString);
                rowData[i][1] = labels[labelIndex+1];
            }else{
                rowData[i][1] = "Overall";
            }
          
            if (desiredPerformance instanceof MisclassificationPerformance) {
                MisclassificationPerformance misclassificationPerformance = (MisclassificationPerformance) desiredPerformance;
                labelIndex = Integer.parseInt(misclassificationPerformance.getTargetLabel());
                rowData[i][2] = desiredPerformance.getMetricsType() + " (" + labels[labelIndex+1] + ")";
            }else{
                rowData[i][2] = desiredPerformance.getMetricsType();
            }

            if(desiredPerformance.getDesiredValue() < 0.0f){
                rowData[i][3] = desiredPerformance.getDesiredValueRange();
            }else{
                rowData[i][3] = desiredPerformance.getDesiredValue();
            }            
            

            if(desiredPerformance.isTested()){
                rowData[i][4] = desiredPerformance.getRealPerformance(); 
            }else{
                rowData[i][4] = "Not tested yet";
            }

            if(desiredPerformance.isTested()){
                if (desiredPerformance.isSatisfying()) {
                    rowData[i][5] = FontIcon.of(Entypo.CHECK, Color.GREEN);
                } else {
                    rowData[i][5] = FontIcon.of(Entypo.CROSS, Color.RED);
                }
            }else{
                rowData[i][5] = "";;
            }

            if(desiredPerformance.isTested()){
                rowData[i][6] = false;
            }else{
                rowData[i][6] = false;
            }

            rowData[i][7] = desiredPerformance;
        }

        return rowData;
    }

}
