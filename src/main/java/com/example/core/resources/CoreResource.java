package com.example.core.resources;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Analysis;
import com.example.core.models.Data;
import com.example.core.models.Token;
import com.example.core.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core")
public class CoreResource {

    private final ParserService ps;
    @Autowired
    LanguageService ls;
    @Autowired
    SemanticService ss;
    @Autowired
    SentimentService ss2;
    @Autowired
    HealthService hs;

    public CoreResource(ParserService ps) {
        this.ps = ps;
    }

//    @PostMapping
//    public ResponseEntity<Data> coreAPI(@RequestBody dataDTO data) {
//        return new ResponseEntity<>(core(data), HttpStatus.OK);
//    }

    public void core(dataDTO data){
        hs.contentLength = data.getContent().length();
        Data _input = new Data(data);
        hs.updateStatus(_input, 0, 0);
        List<Analysis> results = new ArrayList<>();
        hs.updateStatus(_input, 1, 1);
        results.add(ls.languageDetector(_input.getContent()));
        if (results.get(0).getResult().substring(0, 2).equals("fr")) {
            List<Token> tokens = ps.Parser(_input.getContent());
            hs.updateStatus(_input, 1, 1);
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
    }
}
