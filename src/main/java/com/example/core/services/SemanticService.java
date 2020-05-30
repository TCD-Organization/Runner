package com.example.core.services;


import com.example.core.models.Token;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.json.simple.parser.JSONParser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class SemanticService {
    private String pathDEM = System.getProperty("user.dir")+"/DEM-1_0.json";
    private JSONArray dem;
    private JSONParser parser = new JSONParser();

    public SemanticService(){
        try{
            String contents = new String((Files.readAllBytes(Paths.get(this.pathDEM))));
            JSONParser parser = new JSONParser();
            this.dem = (JSONArray) parser.parse(contents);
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public int matchingDEM(Token token){
        System.out.println("CALLL");
        return -1;
    }

    public void findProperNames(List<Token> tokens){
        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i).getContent().matches("[A-Z].*")){
                tokens.get(i).setType(1);
                matchingDEM(tokens.get(i));
            }
        }
    }
}
