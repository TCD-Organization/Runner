package com.example.core.services;

import com.example.core.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/*
    0 -> début de phrase
    1 -> maj
*/
@Service
public class ParserService {

    @Autowired
    SemanticService ss;

    public List<Token> Parser(String content){
        String text = cleanContent(content);
        List<Token> tokens = tokenizer(text);
        return tokens;
    }

    public List<Token> tokenizer(String text){
        String[] splittedContent = text.split(" ");
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < splittedContent.length; i++){
            tokens.add(new Token(splittedContent[i], 0));
        }
        return tokens;
    }

    // Si début de phrase et pas de matching -> NOM propre
    // Si maj et pas début de phrase -> Nom propre
    // Si début de phrase et matching -> pas de nom propre

    public String cleanContent(String content){
       return content.replace(".", "").replace(",", "");
    }
}
