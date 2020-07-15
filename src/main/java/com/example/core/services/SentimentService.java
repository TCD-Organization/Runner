package com.example.core.services;

import com.example.core.models.Analysis;
import com.example.core.models.Token;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentimentService {
    private String pathEmotions = "/emotions.csv";

    public SentimentService(){
    }

    public Analysis SentimentFinder(List<Token> tokens){
        Analysis analysis = new Analysis();
        analysis.setStart(Tools.instantTimestamp());
        analysis.setA_id(4);
        analysis.setFlag("Sentiment analysis");
        String line = "";
        String cvsSplitBy = ";";
        Map<String, Integer> polarity = new HashMap<String, Integer>();
        Map<String, Integer> weights = new HashMap<String, Integer>();
        int sumPolarity = 0;
        int sumGlobal = 0;
        for(int i = 0; i < tokens.size(); i++){
            try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(pathEmotions)))){

                while ((line = br.readLine()) != null) {
                    String[] text = line.split(cvsSplitBy);
                    if(text[0].equals(tokens.get(i).getContent().toLowerCase())){
                        if(text[1].equals("1")){
                            sumPolarity++;
                            if(polarity.containsKey("Positif")){
                                Integer nb = polarity.get("Positif");
                                nb++;
                                polarity.put("Positif", nb);
                            }else{
                                polarity.put("Positif", 1);
                            }
                        }
                        if(text[2].equals("1")){
                            sumPolarity++;
                            if(polarity.containsKey("Négatif")){
                                Integer nb = polarity.get("Négatif");
                                nb++;
                                polarity.put("Négatif", nb);
                            }else{
                                polarity.put("Négatif", 1);
                            }
                        }
                        if(text[3].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Colère")){
                                Integer nb = weights.get("Colère");
                                nb++;
                                weights.put("Colère", nb);
                            }else{
                                weights.put("Colère", 1);
                            }
                        }
                        if(text[4].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Anticipation")){
                                Integer nb = weights.get("Anticipation");
                                nb++;
                                weights.put("Anticipation", nb);
                            }else{
                                weights.put("Anticipation", 1);
                            }
                        }
                        if(text[5].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Dégoût")){
                                Integer nb = weights.get("Dégoût");
                                nb++;
                                weights.put("Dégoût", nb);
                            }else{
                                weights.put("Dégoût", 1);
                            }
                        }
                        if(text[6].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Peur")){
                                Integer nb = weights.get("Peur");
                                nb++;
                                weights.put("Peur", nb);
                            }else{
                                weights.put("Peur", 1);
                            }
                        }
                        if(text[7].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Joie")){
                                Integer nb = weights.get("Joie");
                                nb++;
                                weights.put("Joie", nb);
                            }else{
                                weights.put("Joie", 1);
                            }
                        }
                        if(text[8].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Tristesse")){
                                Integer nb = weights.get("Tristesse");
                                nb++;
                                weights.put("Tristesse", nb);
                            }else{
                                weights.put("Tristesse", 1);
                            }
                        }
                        if(text[9].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Surprise")){
                                Integer nb = weights.get("Surprise");
                                nb++;
                                weights.put("Surprise", nb);
                            }else{
                                weights.put("Surprise", 1);
                            }
                        }
                        if(text[10].equals("1")){
                            sumGlobal++;
                            if(weights.containsKey("Confiance")){
                                Integer nb = weights.get("Confiance");
                                nb++;
                                weights.put("Confiance", nb);
                            }else{
                                weights.put("Confiance", 1);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Integer> orderedPolarity = Tools.sortByValue(polarity, false);
        Map<String, Integer> orderedGlobal = Tools.sortByValue(weights, false);
        String result = "";
        for (String key: orderedPolarity.keySet()) {
            double d = (double)orderedPolarity.get(key)/sumPolarity;
            result += key + "["+(Math.round(d * 100.0))+"%];";
        }
        for (String key: orderedGlobal.keySet()) {
            double d = (double)orderedGlobal.get(key)/sumGlobal;
            result += key + "["+(Math.round(d * 100.0))+"%];";
        }
        analysis.setResult(result);
        analysis.setEnd(Tools.instantTimestamp());
        analysis.setDelay(analysis.getEnd().getTime() - analysis.getStart().getTime());
        return analysis;
    }
}
