package ai.engineering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.SNIMatcher;
import javax.swing.CellEditor;

import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.lang.ClassLoader;
import java.lang.reflect.Array;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ConfigRepository.Config;

public class VersionFetcher {

    private static String[] labels;

    public static String[] GetLabels(boolean includeOverall){

        if(labels != null){
            if (!includeOverall) {
                return Arrays.copyOfRange(labels, 1, labels.length);
            }
            return labels;
        }

        String labelInfo;

        try(InputStream in = VersionFetcher.class.getResourceAsStream("/label.csv")){
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = in.read()) != -1;) {
                sb.append((char) ch);
            }
             labelInfo = sb.toString();
        }catch(Exception e){
            labelInfo = "";
        }
        
        labelInfo = labelInfo.replaceAll("\\r?\\n", ",");
        String[] splitInfo = labelInfo.split(",");
        
        labels = new String[splitInfo.length/2];
        labels[0] = "Overall";
        for (int i = 3; i < splitInfo.length; i+=2) {
            labels[i/2] = splitInfo[i];
        }

        if (!includeOverall) {
            return Arrays.copyOfRange(labels, 1, labels.length);
        }

        return labels;
    }

    private static List<String[]> GetRepairVersions(String freshModelVersion){
        String command = "/home/code/miniconda3/envs/eai-dvc/bin/python /home/code/eAI-Repair/experiments/astah/get_exp.py -m"; 

        String listString = SSHConnector.ExecuteSSH(command + " \"R\" -b \"" + freshModelVersion + "\"");

        if (listString.isEmpty()) {
            return null;
        }

        String[] versionInfos = listString.split("\\r?\\n");
        List<String[]> versionMap = new ArrayList<String[]>();

        for (int i = 0; i < versionInfos.length; i++) {
            String[] splittedString = versionInfos[i].split(",");
            splittedString[0] = "[Repaired] "+ splittedString[0].replaceAll("\\s+","");
            splittedString[1] = freshModelVersion + ":" + splittedString[1].replaceAll("\\s+","");
            versionMap.add(splittedString);
        }

        return versionMap;
    }

    public static String[][] GetVersions(){
        String command = "/home/code/miniconda3/envs/eai-dvc/bin/python /home/code/eAI-Repair/experiments/astah/get_exp.py -m"; 

        String listString = SSHConnector.ExecuteSSH(command + "\"F\"");

        String[] versionInfos = listString.split("\\r?\\n");
        List<String[]> versionMap = new ArrayList<String[]>();

        for (int i = 0; i < versionInfos.length; i++) {
            String[] splittedString = versionInfos[i].split(",");
            splittedString[0] = splittedString[0].replaceAll("\\s+","");
            splittedString[1] = splittedString[1].replaceAll("\\s+","");

            versionMap.add(splittedString);

            List<String[]> derivativeVersions = GetRepairVersions(splittedString[1]);
            if (derivativeVersions != null) {
                versionMap.addAll(derivativeVersions);
            }
        }

        String[][] out = new String[2][versionMap.size()];
        for (int i = 0; i < versionMap.size(); i++) {
            String[] map = versionMap.get(i);
            out[0][i] = map[0];
            out[1][i] = map[1];
        }

        return out;
    }

    public static int[][] GetConfusionMatrix(String versionString){
        int[][] out = null;

        String commandString = null;

        if (versionString.contains(":")) {
            String[] dirs = versionString.split(":");
            commandString = "cat /home/code/eAI-Repair/experiments/" + dirs[0] + "/" + dirs[1] + "/repair_result.json";
        } else {
            commandString = "cat /home/code/eAI-Repair/experiments/" + versionString + "/fresh_result.json";
        }

        try{
            String jsonString = SSHConnector.ExecuteSSH(commandString);
            jsonString = jsonString.replaceAll("\\s", "");
            String[] rows = jsonString.split("]");
            String[][] stringCells = new String[rows.length][rows.length];
            int[][] cells = new int[rows.length][rows.length];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = rows[i].replace("[", "");
                rows[i] = rows[i].replace("\n", "").replace("\r", "");
                
                if(rows[i].startsWith(",")){
                    rows[i] = rows[i].replaceFirst(",", "");
                }

                stringCells[i] = rows[i].split(",");
                for (int j = 0; j < stringCells[i].length; j++) {
                    cells[i][j] = Integer.parseInt(stringCells[i][j]);
                }
            }
            return cells;
        }catch(Exception e){
            System.out.println(e.toString());
        }

        return out;
    }

    public static float GetPerformanceResult(DesiredPerformance desiredPerformance, String selectedVersion){
        String performanceInfo = SSHConnector.ExecuteSSH("bash Main/ssh/performance.ssh " + selectedVersion);
        performanceInfo = performanceInfo.replaceAll("\\r?\\n", ",");
        String[] splitInfo = performanceInfo.split(",");

        for (int i = 0; i < splitInfo.length; i++) {
            if(splitInfo[i].equals("Label")){
                if(splitInfo[i+1].equals(desiredPerformance.getLabel())){
                    System.out.println("Label Found");
                    for (int j = i+2; j < i+8; j++) {
                        if(splitInfo[j].equals(desiredPerformance.getMetricsType())){
                            float out = Float.parseFloat(splitInfo[j+1]);
                            return out;
                        }
                    }
                }
            }
        }
        return -1.f;
    }

    public static Object[][] GetPerformanceResult(String selectedVersion){

        String performanceInfo = SSHConnector.ExecuteSSH("cat /home/code/eAI-Repair/experiments/" + selectedVersion + "/test_result.json");
        
        performanceInfo = performanceInfo.replaceAll("\\r?\\n", ",");
        String[] splitInfo = performanceInfo.split(",");
        
        Object[][] out = new String[splitInfo.length/8][4];
        int j = 0;

        for (int i = 0; i < splitInfo.length; i++) {
            if(splitInfo[i].equals("Label")){
                out[j][0] = splitInfo[i+1];
                out[j][1] = splitInfo[i+3];
                out[j][2] = splitInfo[i+5];
                out[j][3] = splitInfo[i+7];
                j++;
            }
        }

        String[] labels = GetLabels(true);

        for (int i = 0; i < out.length; i++) {
            out[i][0] = labels[i];
        }

        return out;
    }

    public static void TrainNewModel(String expName, String datasetVersion, String modelType, String batchSize, String epochString, String splitString){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter experimentDateFormat = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String formattedDate = experimentDateFormat.format(localDateTime);
        String separator = " ";

        String commandString = "bash -f Main/ssh/train.ssh";
        commandString = commandString + separator + formattedDate;
        commandString = commandString + separator + datasetVersion;
        commandString = commandString + separator + "GTSRB";
        commandString = commandString + separator + modelType;
        commandString = commandString + separator + batchSize;
        commandString = commandString + separator + epochString;
        commandString = commandString + separator + splitString;

        ConfigJsonGenerator.saveJSONFile("GTSRB", Integer.parseInt(batchSize), Float.parseFloat(splitString), modelType, Integer.parseInt(epochString), Float.parseFloat(splitString), false);
        try {
            Session session = SSHConnector.openConnection();
            SSHConnector.copyLocalToRemote(session, "D:/temp/", "/home/code/eAI-Repair/experiments/", "hp_fresh.json");
            String command = "/bin/bash -c 'source /home/code/miniconda3/etc/profile.d/conda.sh && conda activate eai-dvc && /home/code/miniconda3/envs/eai-dvc/bin/python /home/code/eAI-Repair/experiments/astah/trigger.py -m \"F\" -v \"" + expName + "\"'";
            String out = SSHConnector.ExecuteSSH(command);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void TestModel(String modelVersion, String datasetVersion){

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter experimentDateFormat = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String formattedDate = experimentDateFormat.format(localDateTime);

        SSHConnector.ExecuteSSH("bash Main/ssh/test.ssh " + " " + "GTSRB" + " " + formattedDate + " " + modelVersion + " " + datasetVersion);

    }

    public static void RepairModel(String versionName, String baseModelVersion){

        try {
            Session session1 = SSHConnector.openConnection();
            ConfigJsonGenerator.generateRepairHPJSON();
            SSHConnector.copyLocalToRemote(session1, "D:/temp/", "/home/code/eAI-Repair/experiments/", "hp_repair.json");
            
            Session session2 = SSHConnector.openConnection();
            ConfigJsonGenerator.generateLabelJSON();
            SSHConnector.copyLocalToRemote(session2, "D:/temp/", "/home/code/eAI-Repair/experiments/" + baseModelVersion, "labels_temp.json");
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        String command = "/bin/bash -c 'source /home/code/miniconda3/etc/profile.d/conda.sh && conda activate eai-dvc && /home/code/miniconda3/envs/eai-dvc/bin/python /home/code/eAI-Repair/experiments/astah/trigger.py -m \"R\" -v \"" + versionName + "\" -b \""+ baseModelVersion + "\"'";
            
        String out = SSHConnector.ExecuteSSH(command);
        System.out.println(out);
        
        // jsonString = SSHConnector.ExecuteSSH("bash Main/ssh/target.ssh " + " " + "GTSRB" + " " + formattedDate + " " + baseModelVersion + " " + baseModelVersion);
        // SSHConnector.ExecuteSSH("bash Main/ssh/localize.ssh " + " " + "GTSRB" + " " + formattedDate + " " + baseModelVersion + " " + jsonString);
        // SSHConnector.ExecuteSSH("bash Main/ssh/test.ssh " + " " + "GTSRB" + " " + formattedDate + " " + formattedDate + " " + baseModelVersion);

    }

}