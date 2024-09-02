package ai.engineering;

import com.change_vision.jude.api.gsn.model.IGoal;

public class GoalParser {

    private static String[] metricsStrings  = {"Accuracy", "Precision", "Recall", "Misclassification", "IoU"};

    public static DesiredPerformance parseGoal(IGoal goal){
        
        if(!isParsable(goal)){return null;}

        String goalStatement = goal.getContent();
        if(goalStatement.isEmpty()){return null;}

        String logicalStatement = getLogicalStatement(goalStatement);
        if(logicalStatement.isEmpty()){return null;}

        String monitoredMetrics = getMonitoredMetrics(logicalStatement);
        if(monitoredMetrics.isEmpty()){return null;}

        Float desiredValue = getMinimumValue(logicalStatement);
        
        String desiredValueRange = "";
        if(desiredValue < 0.0f){
            desiredValueRange = getValueRange(logicalStatement);
            System.out.println(desiredValueRange);
        }

        if (monitoredMetrics.equals("Misclassification")) {

            System.out.println("Parsing misclassification");

            String monitoredLabel = getMisclassificationMonitoredLabel(logicalStatement);
            System.out.println("Monitored: " + monitoredLabel);
            if(monitoredLabel.isEmpty()){return null;} 

            String targetLabel = getMisrecognitionTargetLabel(logicalStatement);
            System.out.println("Target: " + targetLabel);
            if (targetLabel.isEmpty()){return null;}

            return new MisclassificationPerformance(goal, monitoredLabel, targetLabel, desiredValue, desiredValueRange);
        } else {
            String monitoredLabel = getMonitoredLabel(logicalStatement);
            if(monitoredLabel.isEmpty()){return null;} 
            return new ConfusionMetricsPerformance(goal, monitoredLabel, monitoredMetrics, desiredValue, desiredValueRange);
        }
    }

    public static boolean isParsable(IGoal goal){
        String goalStatement = goal.getContent();
        return goalStatement.contains("[") && goalStatement.contains("]");
    }

    private static String getLogicalStatement(String goalStatement){
        int startIndex = goalStatement.indexOf("[");

        if(startIndex == -1)
        {
            return "";
        }

        int endIndex = goalStatement.indexOf("]");

        if(endIndex == -1)
        {
            return "";
        }

        return goalStatement.substring(startIndex + 1, endIndex);
    }

    private static String getMonitoredMetrics(String logicalStatement){
        int endIndex = logicalStatement.indexOf("(");
        if (endIndex < 0) {
            return "";
        }

        String monitoredMetrics = logicalStatement.substring(0, endIndex);
        monitoredMetrics = monitoredMetrics.toLowerCase();
        
        for (String metricString : metricsStrings) {
            if(metricString.toLowerCase().equals(monitoredMetrics)){
                return metricString;
            }   
        }

        return "";
    }

    private static String getMonitoredLabel(String logicalStatement){
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf("(") + 1;
        int endIndex = logicalStatement.indexOf(")");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);

        for (int i = 0; i < labels.length; i++) {
            if(labels[i].toLowerCase().equals(logicalStatement.toLowerCase())){
                return (i-1)+"";
            }
        }

        return "";
    }

    private static String getMisclassificationMonitoredLabel(String logicalStatement){
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf("(") + 1;
        int endIndex = logicalStatement.indexOf(",");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);

        for (int i = 0; i < labels.length; i++) {
            if(labels[i].toLowerCase().equals(logicalStatement.toLowerCase())){
                return (i-1)+"";
            }
        }

        return "";
    }

    private static String getMisrecognitionTargetLabel(String logicalStatement){
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf(",") + 1;
        int endIndex = logicalStatement.indexOf(")");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);
        logicalStatement = logicalStatement.replaceAll("\\s", "");

        for (int i = 0; i < labels.length; i++) {
            String cleanLabel = labels[i].replaceAll("\\s", "");
            if(cleanLabel.toLowerCase().equals(logicalStatement.toLowerCase())){
                return (i-1)+"";
            }
        }

        return "";
    }

    private static float getMinimumValue(String logicalStatement){

        int startIndex = logicalStatement.indexOf("=");

        if(startIndex == -1){
            startIndex = logicalStatement.indexOf(">");
        }

        String valueString = logicalStatement.substring(startIndex + 1);

        float out = -1.0f;

        try {
            out = Float.parseFloat(valueString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return out;
    }

    private static String getValueRange(String logicalStatement){

        int startIndex = logicalStatement.indexOf("=");

        if(startIndex == -1){
            startIndex = logicalStatement.indexOf(">");
        }

        String valueString = logicalStatement.substring(startIndex + 1);
        
        return valueString.replaceFirst(" ", "");
    }

}
