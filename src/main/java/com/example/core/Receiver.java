package com.example.core;

import java.util.concurrent.CountDownLatch;

import com.example.core.DTO.dataDTO;
import com.example.core.services.CoreService;
import com.example.core.services.Tools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    CoreService cs;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "new_runner_analyses_q")
    public void receiveMessage(String message) {
        try{
            Tools.log(1, "Receiving a new message");
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(message);
            JSONObject analysis = (JSONObject) input.get("analysis");
            dataDTO dataOBJ = new dataDTO();
            dataOBJ.setContent(input.get("content").toString());
            dataOBJ.set_id(analysis.get("id").toString());
            cs.core(dataOBJ);
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