package ai.engineering;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;

public class ConfigJsonGenerator {
    
    public static String generateHyperparameterJSONString(String dataset, int batchSize, float divideRate, String model, int epoch, float validationSplit, boolean isUsingGpu){
        try(InputStream in = VersionFetcher.class.getResourceAsStream("/jsonTemplates/hp_temp.json")){
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = in.read()) != -1;) {
                sb.append((char) ch);
            }
            String jsonString = sb.toString();
            jsonString = jsonString.replace("<dataset>", dataset);
            jsonString = jsonString.replace("<batch_size>", batchSize + "");
            jsonString = jsonString.replace("<divide_rate>", divideRate + "");
            jsonString = jsonString.replace("<random_state>", "null");
            jsonString = jsonString.replace("<model>", model);
            jsonString = jsonString.replace("<epochs>", epoch + "");
            jsonString = jsonString.replace("<validation_split>", validationSplit + "");
            jsonString = jsonString.replace("<gpu>", isUsingGpu + "");

            return jsonString;
        }catch(Exception e){
            return null;
        }
    }

    public static void saveJSONFile(String dataset, int batchSize, float divideRate, String model, int epoch, float validationSplit, boolean isUsingGpu){
        String content = generateHyperparameterJSONString(dataset, batchSize, divideRate, model, epoch, validationSplit, isUsingGpu);

        try{
            FileWriter fileWriter = new FileWriter("hp_fresh.json");
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.print(content);
            writer.close();
        }catch(Exception e){
            System.out.println("file write failed");
        }
    }

    public static void generateRepairHPJSON() {
        String jsonString;
        
        try(InputStream in = VersionFetcher.class.getResourceAsStream("/jsonTemplates/hp_repair_temp.json")){
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = in.read()) != -1;) {
                sb.append((char) ch);
            }
            jsonString = sb.toString();
            System.out.println(jsonString);
        }catch(Exception e){
            jsonString = "";
        }

        jsonString = RepairConfiguration.generateJSONString(jsonString);
        
        try{
            FileWriter fileWriter = new FileWriter("hp_repair.json");
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.print(jsonString);
            writer.close();
        }catch(Exception e){
            System.out.println("file write failed");
        }
    }

    public static void generateLabelJSON() {
        String jsonString;
        
        try(InputStream in = VersionFetcher.class.getResourceAsStream("/jsonTemplates/labels_temp.json")){
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = in.read()) != -1;) {
                sb.append((char) ch);
            }
            jsonString = sb.toString();
        }catch(Exception e){
            jsonString = "";
        }

        jsonString = RepairConfiguration.generateJSONString(jsonString);

        System.out.println(jsonString);
        
        try{
            FileWriter fileWriter = new FileWriter("labels_temp.json");
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.print(jsonString);
            writer.close();
        }catch(Exception e){
            System.out.println("file write failed");
        }
    }

}
