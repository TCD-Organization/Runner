package com.example.core;

import com.example.core.config.Initializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@SpringBootApplication
public class CoreApplication implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    Initializer initializer;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
            initializer.init(ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
