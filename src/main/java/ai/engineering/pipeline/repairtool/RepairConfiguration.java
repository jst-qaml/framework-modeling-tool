package ai.engineering;

import java.util.List;
import java.util.ArrayList;

import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IElement;

public class RepairConfiguration {
    
    private static List<LabelConfiguration> configurations;

    public static String[] parameters = {"Repair Priority", "Prevent Degradation"};
    public static String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    public static void addConfiguration(LabelConfiguration newConfig){
        if (configurations == null) {
            configurations = new ArrayList<LabelConfiguration>();
        }

        String label = newConfig.getLabel();
        LabelConfiguration oldConfig = null;

        for (LabelConfiguration config : configurations) {
            if (label.equals(config.getLabel())) {
                oldConfig = config;
            }
        }

        if (oldConfig != null) {
            configurations.remove(oldConfig);
        }

        configurations.add(newConfig);
    }

    public static String generateJSONString(String baseJSONString){
        String jsonString = baseJSONString.replace("exit-status: 0", "");

        String[] split = jsonString.split("\n");

        if(split == null){
            return "";
        }

        for (LabelConfiguration config : configurations) {
            String label = config.getLabel();
            for (int i = 0; i < split.length; i++) {
                if(split[i].contains("\"" + label + "\"")){
                    String[] repairInfoSplit = split[i+2].split(":");
                    split[i+2] = repairInfoSplit[0].concat(": "+ config.getRepairPriority() +",");
                    String[] preventInfoSplit = split[i+3].split(":");
                    split[i+3] = preventInfoSplit[0].concat(": "+ config.getPreventDegradation());
                }
            }

        }

        jsonString = String.join("\n", split);

        return jsonString;

    }

    public static LabelConfiguration findLabelConfiguration(IPresentation presentation){
        if (configurations == null) {
            configurations = new ArrayList<LabelConfiguration>();
        }

        for (LabelConfiguration labelConfiguration : configurations) {
            if(labelConfiguration.isEqual(presentation)){
                return labelConfiguration;
            }
        }

        return null;
    }

    public static Object[][] generateTableData(){

        if (configurations == null) {
            return new String[10][4];
        }

        Object[][] rowData = new Object[configurations.size()][4];

        String[] labels = VersionFetcher.GetLabels(false);
        
        for (int i = 0; i < configurations.size(); i++) {
            LabelConfiguration config = configurations.get(i);
            Integer labelIndex = Integer.parseInt(config.getLabel());
            
            IElement element = config.getPresentation().getModel();

            if (labelIndex == null) {
                return new String[10][4];
            }

            if(element instanceof INamedElement){
                INamedElement namedElement = (INamedElement) element;
                rowData[i][0] = namedElement.getName();
            }
                        
            rowData[i][1] = labels[labelIndex];
            rowData[i][2] = config.getRepairPriority();
            rowData[i][3] = config.getPreventDegradation();
        }

        return rowData;
    }

}
