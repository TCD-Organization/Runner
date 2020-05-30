package com.example.core;

import com.example.core.services.LanguageService;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.simple.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) throws IOException {
        init();
        SpringApplication.run(CoreApplication.class, args);
    }

    public static void init(){

        JSONParser parser = new JSONParser();
        try{
            String workingDir = System.getProperty("user.dir");
            Path p = Paths.get(workingDir+"/config.json");
            if(!Files.exists(p)){
                JSONObject obj = new JSONObject();
                UUID coreID = UUID.randomUUID();
                String generatedString = coreID.toString().substring(0,5);
                System.out.println("ID generated");
                obj.put("ID", generatedString);
                try{
                    FileWriter config = new FileWriter(p.toString());
                    config.write(obj.toJSONString());
                    config.close();
                    System.out.println("File generation was successful");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            Object obj = parser.parse(new FileReader(p.toString()));
            JSONObject object = (JSONObject) obj;
            String ID = (String) object.get("ID");
            System.out.println("Core ID = " + ID);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
