package com.example.core.config;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Data;
import com.example.core.resources.CoreResource;
import com.example.core.services.Tools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Initializer {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    CoreResource coreResource;

    private final RestTemplate restTemplate = new RestTemplate();

    private String runnerName = "my-runner";
    private String ip;
    private Integer port;
    private String ID;
    public String bearerToken;
    public Integer bootingLength = 0;
    public long bootingTime = 0;
    public boolean isTesting = true;

    @Value("${runner.address.base}")
    private String registerURL;

    public void init(String ip,Integer port){
        this.port = port;
        this.ip = ip;
        verifyRunnerKey();
        Tools.log(1,"Runner n°"+this.ID+" running on "+this.ip+":"+this.port);
        registerRunner();
        booting();
        this.isTesting = false;
        Tools.log(1,"System ready");
    }

    public void registerRunner(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject();
        params.put("runnername", this.runnerName);
        params.put("key", "test123");
        HttpEntity<String> request = new HttpEntity<String>(params.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(this.registerURL+"/runner/connect", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Tools.log(1, "Registering request Successful");
            this.bearerToken = response.getHeaders().get("Authorization").get(0);
            Tools.log(1, "Token : "+this.bearerToken);
        } else {
            Tools.log(2, "Registering request failed");
            Tools.log(3, "Stopping the application");
            SpringApplication.exit(appContext, () -> 0);
        }
    }

    public void booting(){
        Timestamp start = Tools.instantTimestamp();
        dataDTO dataTest = new dataDTO();
        dataTest.set_id("BOOTING");
        String content = "Il était une fois, Hervé Patrak, Christine et leurs enfants Sophie et Jean. Ils vivèrent heureux dans un chalet avec des voisins fort sympathiques. Ils étaient heureux, la vie était paisible.";
        dataTest.setContent(content);
        this.bootingLength = content.length();
        Tools.log(1, "Boot test length = "+this.bootingLength);
        coreResource.core(dataTest);
        Timestamp end = Tools.instantTimestamp();
        this.bootingTime = end.getTime() - start.getTime();
        Tools.log(1, "Boot test delay = "+this.bootingTime);
    }

    public void verifyRunnerKey(){
        JSONParser parser = new JSONParser();
        try{
            String workingDir = System.getProperty("user.dir");
            Path p = Paths.get(workingDir+"/config.json");
            if(!Files.exists(p)){
                JSONObject obj = new JSONObject();
                UUID coreID = UUID.randomUUID();
                String generatedString = coreID.toString().substring(0,5);
                Tools.log(1, "ID generated");
                obj.put("ID", generatedString);
                try{
                    FileWriter config = new FileWriter(p.toString());
                    config.write(obj.toJSONString());
                    config.close();
                    Tools.log(1, "File generation was successful");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            Object obj = parser.parse(new FileReader(p.toString()));
            JSONObject object = (JSONObject) obj;
            String ID = (String) object.get("ID");
            this.ID = ID;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
