package com.example.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.example.core.DTO.dataDTO;
import com.example.core.services.CoreService;
import com.example.core.services.Tools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class Receiver {

    @Autowired
    CoreService cs;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "new_runner_analyses_q", errorHandler = "rabbitErrorHandler")
    public void receiveMessage(String message) {
        try{
            Tools.log(1, "Receiving a new message");
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(message);
            dataDTO dataOBJ = new dataDTO();
            dataOBJ.setContent(input.get("content").toString());
            dataOBJ.setAnalysis_id(input.get("analysis_id").toString());
            cs.core(dataOBJ);
        } catch(ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}