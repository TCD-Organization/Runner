package com.example.core.models;

import com.example.core.DTO.dataDTO;

import java.util.List;

@lombok.Data
public class Data {
    private String analysis_id;
    private String content;
    private List<Analysis> result;

    public Data(){ }

    public Data(dataDTO data){
        this.analysis_id = data.getAnalysis_id();
        this.content = data.getContent();
    }
}
