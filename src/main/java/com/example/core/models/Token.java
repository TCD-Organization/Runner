package com.example.core.models;

import lombok.Data;

/**
 * Type = -1 : Not analysed yet
 * Type = 0 : Analysed but nothing to say
 * Type = 1 : has a Uppercase first letter
 *
 */

@Data
public class Token {
    private String content;
    private Integer type;

    public Token(String content, Integer type){
        this.content = content;
        this.type = type;
    }
}
