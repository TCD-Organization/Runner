package com.example.core.services;

import com.example.core.config.Initializer;
import com.example.core.models.Analysis;
import com.example.core.models.Data;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HealthService {

    @Value("${runner.address.base}")
    private String serverURL;

    public long contentLength = 0;

    @Autowired
    Initializer initializer;

    public void updateStatus(Data data, Integer a_id, Integer status){
        if(!initializer.isTesting){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", initializer.bearerToken);
            JSONObject params = new JSONObject();
            findName(params, a_id);
            if(contentLength != 0 && initializer.bootingTime != 0 && initializer.bootingLength != 0){
                params.put("lasting_time", contentLength*(initializer.bootingTime/initializer.bootingLength));
            }else{
                params.put("lasting_time", 0);
            }
            if(status == 0) params.put("status", "TO_START");
            if(status == 1) params.put("status", "IN_PROGRESS");
            if(status == 3) params.put("status", "FINISHED");
            if(status == 2){
                params.put("status", "FINISHED");
                params.put("result", new Gson().toJson(data.getResult()));
            } else {
                params.put("result", null);
            }
            Tools.log(1, params.toString());
            HttpEntity<String> request = new HttpEntity<String>(params.toString(), headers);
            restTemplate.put(this.serverURL+"/analysis/"+data.getAnalysis_id()+"/progress",  request);
        }
    }

    private void findName(JSONObject params, Integer a_id){
        JSONParser jsonParser = new JSONParser();
        String workingDir = System.getProperty("user.dir");
        try (FileReader reader = new FileReader(workingDir+"/mods.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray modsList = (JSONArray) obj;
            JSONObject json = (JSONObject) modsList.get(a_id);
            params.put("step_name", json.get("name"));
            params.put("step_number", a_id+1);
            params.put("total_steps", modsList.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
