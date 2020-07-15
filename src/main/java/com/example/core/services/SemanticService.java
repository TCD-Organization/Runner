package com.example.core.services;


import com.example.core.models.Analysis;
import com.example.core.models.Token;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.StringHelper.join;

@Service
public class SemanticService {
    private String pathDEM = "/DEM-1_0.json";
    private JSONArray dem;
    private JSONParser parser = new JSONParser();

    public SemanticService(){
        try{
            String contents = new String(this.getClass().getResourceAsStream(pathDEM).readAllBytes(), StandardCharsets.UTF_8);
            JSONParser parser = new JSONParser();
            this.dem = (JSONArray) parser.parse(contents);
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public boolean matchingDEMNames(Token token){
        for(int i = 0; i < this.dem.size(); i++){
            JSONObject json = (JSONObject) this.dem.get(i);
            JSONObject word = (JSONObject) json.get("M");

            if(token.getContent().matches(".*s$")){
                String noMore = token.getContent().substring(0, token.getContent().length()-1);
                if(noMore.toLowerCase().equals(word.get("mot").toString())){
                    return true;
                }
            }else{
                if(token.getContent().toLowerCase().equals(word.get("mot").toString())){
                    return true;
                }
            }
        }

        return false;
    }

    public void findProperNames(List<Token> tokens){
        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i).getContent().matches("[A-Z].*")){
                tokens.get(i).setType(1);
            }
        }
    }

    public Analysis AnalyseNames(List<Token> tokens){
        Analysis analysis = new Analysis();
        analysis.setStart(Tools.instantTimestamp());
        analysis.setA_id(1);
        analysis.setFlag("Proper names");
        List<String> names = new ArrayList<String>();
        for(int i = 0; i < tokens.size();i++){
            if(tokens.get(i).getType() == 1){
                names.add(tokens.get(i).getContent());
            }

        }
        analysis.setResult(join(names, ", "));
        analysis.setEnd(Tools.instantTimestamp());
        analysis.setDelay(analysis.getEnd().getTime() - analysis.getStart().getTime());
        return analysis;
    }

    public String DEMFinderLite(String search, String key){
        String text = "";
        for(int i = 0; i < this.dem.size(); i++) {
            JSONObject json = (JSONObject) this.dem.get(i);
            JSONObject word = (JSONObject) json.get("M");
            JSONObject ikey = (JSONObject) json.get("DOM");
            if(search.toLowerCase().equals(word.get("mot").toString()) && (word.get("no") == null) && !ikey.get("nom").equals("linguistique")){
                return json.get(key).toString();
            }
        }
        return "0";
    }

    public String DEMFinder(String search, String key, String value){
        for(int i = 0; i < this.dem.size(); i++) {
            JSONObject json = (JSONObject) this.dem.get(i);
            JSONObject word = (JSONObject) json.get("M");
            JSONObject ikey = (JSONObject) json.get(key);
            if(search.toLowerCase().equals(word.get("mot").toString()) && (word.get("no") == null)){
                return ikey.get(value).toString();
            }
        }
        return "0";
    }

    public Analysis DomainFinder(List<Token> tokens){
        Analysis analysis = new Analysis();
        analysis.setStart(Tools.instantTimestamp());
        analysis.setA_id(2);
        analysis.setFlag("Domain");
        Map<String, Integer> weights = new HashMap<String, Integer>();
        int sum = 0;
        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i).getType() != 1){
                String res = DEMFinder(tokens.get(i).getContent(), "DOM", "nom");
                if(!res.equals("0") && !res.equals("linguistique")){
                    sum ++;
                    if(weights.containsKey(res)){
                        Integer nb = weights.get(res);
                        nb++;
                        weights.put(res, nb);
                    }else{
                        weights.put(res, 1);
                    }
                }
            }
        }
        Map<String, Integer> ordered = Tools.sortByValue(weights, false);
        String result = "";
        for (String key: ordered.keySet()) {
            double d = (double)ordered.get(key)/sum;
            result += key + "["+(Math.round(d * 100.0))+"%];";
        }
        analysis.setResult(result);
        analysis.setEnd(Tools.instantTimestamp());
        analysis.setDelay(analysis.getEnd().getTime() - analysis.getStart().getTime());
        return analysis;
    }

    public Analysis SubjectFinder(List<Token> tokens){
        Analysis analysis = new Analysis();
        analysis.setStart(Tools.instantTimestamp());
        analysis.setA_id(3);
        analysis.setFlag("Subject");
        Map<String, Integer> weights = new HashMap<String, Integer>();
        int sum = 0;
        for(int i = 0; i < tokens.size(); i++){

            if(tokens.get(i).getType() != 1){
                //String res = DEMFinder(tokens.get(i).getContent().toLowerCase(), "CA", "categorie");
                String res = DEMFinderLite(tokens.get(i).getContent().toLowerCase(), "SENS");
                if(!res.equals("0")){
                    sum ++;
                    if(weights.containsKey(tokens.get(i).getContent())){
                        Integer nb = weights.get(tokens.get(i).getContent());
                        nb++;
                        weights.put(tokens.get(i).getContent(), nb);
                    }else{
                        weights.put(tokens.get(i).getContent(), 1);
                    }
                }
            }
        }

        Map<String, Integer> ordered = Tools.sortByValue(weights, false);
        String result = "";
        for (String key: ordered.keySet()) {
            double d = (double)ordered.get(key)/sum;
            result += key + "["+(Math.round(d * 100.0))+"%];";
        }
        analysis.setResult(result);
        analysis.setEnd(Tools.instantTimestamp());
        analysis.setDelay(analysis.getEnd().getTime() - analysis.getStart().getTime());
        return analysis;
    }
}
