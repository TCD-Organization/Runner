package com.example.core.services;

import com.example.core.models.Analysis;
import com.example.core.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
    0 -> début de phrase
    1 -> maj
*/
@Service
public class ParserService {

    @Autowired
    SemanticService ss;

    public List<Token> Parser(String content){
        List<Token> tokens = tokenizer(content);
        cleanContent(tokens);
        ss.findProperNames(tokens);
        return tokens;
    }

    public List<Token> tokenizer(String text){
        String[] splittedContent = text.split(" ");
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < splittedContent.length; i++){
            if(i == 0){
                splittedContent[0] = splittedContent[0].toLowerCase();
            }
            tokens.add(new Token(splittedContent[i], 0));
        }
        return tokens;
    }


    public void cleanContent(List<Token> tokens){
        List<Token> toRemove = new ArrayList<>();
        for(int i = 0; i < tokens.size(); i++){
            if(i == 0){
                String noMaj = tokens.get(0).getContent().toLowerCase();
                tokens.get(0).setContent(noMaj);
            }
            if(tokens.get(i).getContent().matches(".*\\.") && (i+1) < tokens.size()){
                tokens.get(i+1).setContent(tokens.get(i+1).getContent().toLowerCase());
            }
            if(Character.isUpperCase(tokens.get(i).getContent().toCharArray()[0])){
                if((i+1) < tokens.size() && Character.isUpperCase(tokens.get(i+1).getContent().toCharArray()[0])){
                    if(!tokens.get(i).getContent().matches(".*,") &&  !tokens.get(i).getContent().matches("\\.")){
                        tokens.get(i).setContent(tokens.get(i).getContent() + " " + tokens.get(i+1).getContent());
                        tokens.remove(i+1);
                    }
                }
                tokens.get(i).setType(1);
            }
            tokens.get(i).setContent(tokens.get(i).getContent().replace(",",""));
            tokens.get(i).setContent(tokens.get(i).getContent().replace(".",""));
            tokens.get(i).setContent(tokens.get(i).getContent().replace(";",""));
            tokens.get(i).setContent(tokens.get(i).getContent().replace("?",""));
            tokens.get(i).setContent(tokens.get(i).getContent().replace("!",""));
            if(tokens.get(i).getContent().length() <= 3) toRemove.add(tokens.get(i));
        }
        for(int i = 0; i < toRemove.size(); i++){
            tokens.remove(toRemove.get(i));
        }
    }
}
