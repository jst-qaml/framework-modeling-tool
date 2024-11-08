package ai.engineering.patternApplication.internal;

import java.util.*;
import ai.engineering.*;

public class SolutionValueSupport {
    public boolean isSolutionSelectionSupportPattern(int patternN){
        //現状はSelectiveRepairPatternのみで行う
        if (patternN == 0) {
            return true;
        }
        return false;
    }

    public int GetRepeatN(int patternN, int repeatN){
        if(!isSolutionSelectionSupportPattern(patternN)){
            return repeatN;
        }

        String[] importantClassNames = GetImportantClassNames();
        if(importantClassNames.length == 0){
            return 1;
        }else{
            return importantClassNames.length;
        }
    }

    public String ReplaceSolutionValue(int patternN, String content, int loopN){
        if(!isSolutionSelectionSupportPattern(patternN)){
            return content;
        }

        //SelectRepairPatternの場合
        if(patternN == 0){
            String[] importantClassNames = GetImportantClassNames();
            content = content.replace("RepairPriority for {important class {X[0]}} = {5}", String.valueOf("RepairPriority for {important class {X[0]}} = {"+ GetPriorityValuesFromRiskValue()[loopN]+"}"));
            content = content.replace("PreventDegradation for {important class {X[0]}} = {5}", String.valueOf("PreventDegradation for {important class {X[0]}} = {"+ GetPreventValuesFromRiskValue()[loopN]+"}"));
            content = content.replace("{important class {X[0]}}", String.valueOf(importantClassNames[loopN]+" class"));
            return content;
        }else{
            return content;
        }
    }

    private int[] GetPriorityValuesFromRiskValue(){
        int[] array = Arrays.stream(GetRiskValues())
                .mapToInt(d -> (int) Math.floor(d)) // 小数点以下を切り捨て
                .toArray();
        //10以上の場合は10にして、0以下の場合は0にする
        for(int i = 0; i < array.length; i++){
            if(array[i] >= 10){
                array[i] = 10;
            }else if(array[i] <= 0){
                array[i] = 0;
            }
        }
        return array;
    }

    private int[] GetPreventValuesFromRiskValue(){
        int[] array = Arrays.stream(GetRiskValues())
                .mapToInt(d -> (int) Math.floor(d)) // 小数点以下を切り捨て
                .toArray();
        //10以上の場合は10にして、0以下の場合は0にする
        for(int i = 0; i < array.length; i++){
            if(array[i] >= 10){
                array[i] = 10;
            }else if(array[i] <= 0){
                array[i] = 0;
            }
        }
        return array;
    }

    private String[] GetImportantClassNames(){
        return VersionFetcher.GetLabels(false);
//        String[] importantClassNames = {"Mobility", "Drivable", "Obstacle"};
//        return importantClassNames;
    }

    private double[] GetRiskValues(){
        double[] riskValues = {6.5, 2.3, 12.4, 3.7, 1.0, 9.8};
        return riskValues;
    }
}
