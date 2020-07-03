package com.example.core;

import java.util.concurrent.CountDownLatch;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Data;
import com.example.core.resources.CoreResource;
import com.example.core.services.Tools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.bind.DataObjectPropertyName;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    CoreResource cr;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "new_runner_analyses_q")
    public void receiveMessage(String message) {
        try{
            Tools.log(1, "Receiving a new message");
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(message);
            dataDTO dataOBJ = new dataDTO();
            dataOBJ.setContent(input.get("content").toString());
            dataOBJ.setAnalysis_id(input.get("id").toString());
            cr.core(dataOBJ);
        }catch(ParseException e){
            e.printStackTrace();
            System.exit(0);
        }
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}