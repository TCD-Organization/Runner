package com.example.core.services;

import java.sql.Timestamp;

public class Tools {

    public static Timestamp instantTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }
}
