package com.example.core.services;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Analysis;
import com.example.core.models.Data;
import com.example.core.models.Token;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoreService {

    private final ParserService ps;
    @Autowired
    LanguageService ls;
    @Autowired
    SemanticService ss;
    @Autowired
    SentimentService ss2;
    @Autowired
    HealthService hs;

    public CoreService(ParserService ps) {
        this.ps = ps;
    }

    public void core(dataDTO data) {
        hs.contentLength = data.getContent().length();
        Data _input = new Data(data);

        try {
            hs.updateStatus(_input, 0, 0);
            List<Analysis> results = new ArrayList<>();
            hs.updateStatus(_input, 1, 1);
            results.add(ls.languageDetector(_input.getContent()));
            if (results.get(0).getResult().substring(0, 2).equals("fr")) {
                List<Token> tokens = ps.Parser(_input.getContent());
                results.add(ss.AnalyseNames(tokens));
                hs.updateStatus(_input, 2, 1);
                results.add(ss.DomainFinder(tokens));
                hs.updateStatus(_input, 3, 1);
                results.add(ss.SubjectFinder(tokens));
                hs.updateStatus(_input, 4, 1);
                results.add(ss2.SentimentFinder(tokens));
                hs.updateStatus(_input, 5, 1);
                _input.setResult(results);
                hs.updateStatus(_input, 6, 2);
            }else{
                hs.updateStatus(_input, 6, 3);
            }
            hs.contentLength = 0;
        } catch (Exception e) {
            e.printStackTrace();
            hs.cancelAnalysis(_input);
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
            //throw e;
        }
    }

    public void silentCore(dataDTO data){
        hs.contentLength = data.getContent().length();
        Data _input = new Data(data);
        List<Analysis> results = new ArrayList<>();
        results.add(ls.languageDetector(_input.getContent()));
        if (results.get(0).getResult().substring(0, 2).equals("fr")) {
            List<Token> tokens = ps.Parser(_input.getContent());
            results.add(ss.AnalyseNames(tokens));
            results.add(ss.DomainFinder(tokens));
            results.add(ss.SubjectFinder(tokens));
            results.add(ss2.SentimentFinder(tokens));
            _input.setResult(results);
        }
        hs.contentLength = 0;
    }
}
