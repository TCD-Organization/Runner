package com.example.core.models;

import com.example.core.DTO.dataDTO;

import java.util.List;

@lombok.Data
public class Data {
    private String _id;
    private String content;
    private List<Analysis> result;

    public Data(){ }

    public Data(dataDTO data){
        this._id = data.get_id();
        this.content = data.getContent();
    }
}
