package ai.engineering;

public class MetricsCalculator {
    
    public static void calculateAchievement(DesiredPerformance desiredPerformance, int[][] confusionMatrix){
        String metricsType = desiredPerformance.getMetricsType();
        
        // for (int i = 0; i < confusionMatrix.length; i++) {
        //     System.out.print("[ ");
        //     for (int j = 0; j < confusionMatrix[i].length; j++) {
        //         System.out.print(confusionMatrix[i][j] + ", ");
        //     }
        //     System.out.println("]");
        // }

        if(metricsType.equals("Accuracy")){
            float accuracy = -1.0f;

            if (desiredPerformance.getLabel().equals("overall") || desiredPerformance.getLabel().equals("-1")) {
                accuracy = calculateAccuracy(confusionMatrix);
            } else {
                accuracy = calculateAccuracy(confusionMatrix, desiredPerformance.getLabel());
            }

            desiredPerformance.setRealPerformance(accuracy);

            return;
        }

        if(metricsType.equals("Precision")){
            float precision = -1.0f;

            if (desiredPerformance.getLabel().equals("overall") || desiredPerformance.getLabel().equals("-1")) {
                precision = calculatePrecision(confusionMatrix);
            } else {
                precision = calculatePrecision(confusionMatrix, desiredPerformance.getLabel());
            }

            desiredPerformance.setRealPerformance(precision);

            return;
        }

        if(metricsType.equals("Recall")){
            float recall = -1.0f;

            if (desiredPerformance.getLabel().equals("overall") || desiredPerformance.getLabel().equals("-1")) {
                recall = calculateRecall(confusionMatrix);
            } else {
                recall = calculateRecall(confusionMatrix, desiredPerformance.getLabel());
            }

            desiredPerformance.setRealPerformance(recall);

            return;
        }

        if(metricsType.equals("Misclassification")){
            float misclassificationRate = -1.0f;
            MisclassificationPerformance misclassificationPerformance = (MisclassificationPerformance) desiredPerformance;

            if (misclassificationPerformance.getTargetLabel().equals("-1")) {
                misclassificationRate = calculateMisclassificationRate(confusionMatrix, misclassificationPerformance.getLabel());
            } else {
                misclassificationRate = calculateMisclassificationRate(confusionMatrix, misclassificationPerformance.getLabel(), misclassificationPerformance.getTargetLabel());
            }
            misclassificationPerformance.setRealPerformance(misclassificationRate);

            return;
        }

        if(metricsType.equals("IoU")){
            float iou = -1.0f;

            if (desiredPerformance.getLabel().equals("overall") || desiredPerformance.getLabel().equals("-1")) {
                iou = calculateIoU(confusionMatrix);
            } else {
                iou = calculateIoU(confusionMatrix, desiredPerformance.getLabel());
            }
    
            desiredPerformance.setRealPerformance(iou);

            return;
        }
    }

    public static float calculateAccuracy(int[][] confusionMatrix, String labelIndex){
        float accuracy = -1.0f;
        int index = Integer.parseInt(labelIndex);

        int TP = calculateTruePositive(confusionMatrix, index);
        int TN = calculateTrueNegative(confusionMatrix, index);
        int FP = calculateFalsePositive(confusionMatrix, index);
        int FN = calculateFalseNegative(confusionMatrix, index);

        accuracy = (float) (TP + TN) / (TP + TN + FP + FN);

        return accuracy * 100;
    }

    public static float calculateAccuracy(int[][] confusionMatrix){
        float accuracy = -1.0f;

        int TP = 0;
        int total = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                total += confusionMatrix[i][j];
                if (i == j) {
                    TP += calculateTruePositive(confusionMatrix, i);
                }
            }
        }
        
        accuracy = (float) TP / total;

        return accuracy * 100;
    }

    public static float calculatePrecision(int[][] confusionMatrix, String labelIndex){
        float precision = -1.0f;
        int index = Integer.parseInt(labelIndex);

        int TP = calculateTruePositive(confusionMatrix, index);
        int FP = calculateFalsePositive(confusionMatrix, index);

        precision = (float) TP / (TP + FP);

        return precision * 100;
    }

    public static float calculatePrecision(int[][] confusionMatrix){
        float precision = 0.0f;

        for (int i = 0; i < confusionMatrix.length; i++) {
            precision += calculatePrecision(confusionMatrix, i+"");
        }

        precision = precision / confusionMatrix.length;

        return precision;
    }

    public static float calculateRecall(int[][] confusionMatrix, String labelIndex){
        float recall = -1.0f;
        int index = Integer.parseInt(labelIndex);

        int TP = calculateTruePositive(confusionMatrix, index);
        int FN = calculateFalseNegative(confusionMatrix, index);

        recall = (float) TP / (TP + FN);

        return recall * 100;
    }

    public static float calculateRecall(int[][] confusionMatrix){
        float recall = 0.0f;

        for (int i = 0; i < confusionMatrix.length; i++) {
            recall += calculateRecall(confusionMatrix, i+"");
        }

        recall = recall / confusionMatrix.length;

        return recall;
    }

    public static float calculateIoU(int[][] confusionMatrix, String labelIndex){
        float iou = -1.0f;

        int index = Integer.parseInt(labelIndex);

        int TP = calculateTruePositive(confusionMatrix, index);
        int FN = calculateFalseNegative(confusionMatrix, index);
        int FP = calculateFalsePositive(confusionMatrix, index);

        if (TP == 0) {
            return 0;
        }

        System.out.println(TP);
        System.out.println(TP+FN+FP);

        iou = (float) TP / (TP+FN+FP);

        return iou;
    }

    public static float calculateIoU(int[][] confusionMatrix){
        return calculateAccuracy(confusionMatrix);
    }

    public static float calculateMisclassificationRate(int[][] confusionMatrix, String labelIndex){
        return 100 - calculateRecall(confusionMatrix, labelIndex);
    }

    public static float calculateMisclassificationRate(int[][] confusionMatrix, String expectedLabelIndex, String mispredictedLabelIndex){
        int expected = Integer.parseInt(expectedLabelIndex);
        int mispredict = Integer.parseInt(mispredictedLabelIndex);

        int total = calculateTruePositive(confusionMatrix, expected) + calculateFalseNegative(confusionMatrix, expected);

        int mispredictedNumber = confusionMatrix[expected][mispredict];

        return (float) mispredictedNumber / total * 100;
    }

    private static int calculateTruePositive(int[][] confusionMatrix, int index){
        return confusionMatrix[index][index];        
    }

    private static int calculateFalseNegative(int[][] confusionMatrix, int index){
        int out = 0;

        for (int i = 0; i < confusionMatrix[index].length; i++) {
            out += confusionMatrix[index][i];
        }

        return out - confusionMatrix[index][index];
    }

    private static int calculateFalsePositive(int[][] confusionMatrix, int index){
        int out = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            out += confusionMatrix[i][index];
        }

        return out - confusionMatrix[index][index];
    }

    private static int calculateTrueNegative(int[][] confusionMatrix, int index){
        int out = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                if (i != index && j != index) {
                    out += confusionMatrix[i][index];
                }
            }
        }

        return out;
    }

    private static int calculateTotalData(int[][] confusionMatrix){
        int out = 0;

        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                out += confusionMatrix[i][j];
            }
        }

        return out;
    }

}
