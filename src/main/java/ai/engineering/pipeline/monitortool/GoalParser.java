package ai.engineering.pipeline.monitortool;

import ai.engineering.pipeline.VersionFetcher;
import ai.engineering.pipeline.monitortool.DesiredPerformance.*;
import com.change_vision.jude.api.gsn.model.IGoal;

public class GoalParser {
    public static DesiredPerformance parseGoal(IGoal goal) {

        if (!isParsable(goal)) {
            return null;
        }

        String goalStatement = goal.getContent();
        if (goalStatement.isEmpty()) {
            return null;
        }

        String logicalStatement = getLogicalStatement(goalStatement);
        if (logicalStatement.isEmpty()) {
            return null;
        }

        Metric monitoredMetrics = getMonitoredMetrics(logicalStatement);
        if (monitoredMetrics == null) {
            return null;
        }

        float desiredValue = getMinimumValue(logicalStatement);
        if (desiredValue < 0.0f) {
            return null;
        }

        if (monitoredMetrics == Metric.Misclassification) {

            System.out.println("Parsing misclassification");

            String monitoredLabel = getMisclassificationMonitoredLabel(logicalStatement);
            System.out.println("Monitored: " + monitoredLabel);
            if (monitoredLabel.isEmpty()) {
                return null;
            }

            String targetLabel = getMisrecognitionTargetLabel(logicalStatement);
            System.out.println("Target: " + targetLabel);
            if (targetLabel.isEmpty()) {
                return null;
            }

            return new MisclassificationPerformance(goal, monitoredLabel, targetLabel, desiredValue);
        } else {
            String monitoredLabel = getMonitoredLabel(logicalStatement);
            if (monitoredLabel.isEmpty()) {
                return null;
            }

            switch (monitoredMetrics) {
                case Accuracy:
                    return new DesiredAccuracy(goal, monitoredLabel, desiredValue);
                case Recall:
                    return new DesiredRecall(goal, monitoredLabel, desiredValue);
                case Precision:
                    return new DesiredPrecision(goal, monitoredLabel, desiredValue);
                default:
                    return null;
            }
        }
    }

    public static boolean isParsable(IGoal goal) {
        String goalStatement = goal.getContent();
        return goalStatement.contains("[") && goalStatement.contains("]");
    }

    private static String getLogicalStatement(String goalStatement) {
        int startIndex = goalStatement.indexOf("[");

        if (startIndex == -1) {
            return "";
        }

        int endIndex = goalStatement.indexOf("]");

        if (endIndex == -1) {
            return "";
        }

        return goalStatement.substring(startIndex + 1, endIndex);
    }

    private static Metric getMonitoredMetrics(String logicalStatement) {
        int endIndex = logicalStatement.indexOf("(");
        String monitoredMetrics = logicalStatement.substring(0, endIndex);

        for (Metric metric : Metric.values()) {
            if (metric.toString().equalsIgnoreCase(monitoredMetrics)) {
                return metric;
            }
        }

        return null;
    }

    private static String getMonitoredLabel(String logicalStatement) {
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf("(") + 1;
        int endIndex = logicalStatement.indexOf(")");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);

        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equalsIgnoreCase(logicalStatement)) {
                return (i - 1) + "";
            }
        }

        return "";
    }

    private static String getMisclassificationMonitoredLabel(String logicalStatement) {
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf("(") + 1;
        int endIndex = logicalStatement.indexOf(",");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);

        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equalsIgnoreCase(logicalStatement)) {
                return (i - 1) + "";
            }
        }

        return "";
    }

    private static String getMisrecognitionTargetLabel(String logicalStatement) {
        String[] labels = VersionFetcher.GetLabels(true);

        int startIndex = logicalStatement.indexOf(",") + 1;
        int endIndex = logicalStatement.indexOf(")");

        logicalStatement = logicalStatement.substring(startIndex, endIndex);
        logicalStatement = logicalStatement.replaceAll("\\s", "");

        for (int i = 0; i < labels.length; i++) {
            String cleanLabel = labels[i].replaceAll("\\s", "");
            if (cleanLabel.equalsIgnoreCase(logicalStatement)) {
                return (i - 1) + "";
            }
        }

        return "";
    }

    private static float getMinimumValue(String logicalStatement) {

        int startIndex = logicalStatement.indexOf("=");

        if (startIndex == -1) {
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

}
