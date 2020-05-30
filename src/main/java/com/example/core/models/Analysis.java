package com.example.core.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Analysis {
    private String flag;
    private int a_id;
    private Timestamp start;
    private Timestamp end;
    private long delay;
    private String result;
}
