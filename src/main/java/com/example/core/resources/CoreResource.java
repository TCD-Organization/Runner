package com.example.core.resources;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Analysis;
import com.example.core.models.Data;
import com.example.core.models.Token;
import com.example.core.services.LanguageService;
import com.example.core.services.ParserService;
import com.example.core.services.SemanticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core")
public class CoreResource {

    private final ParserService ps;
    @Autowired
    LanguageService ls;

    public CoreResource(ParserService ps) {
        this.ps = ps;
    }

    @PostMapping
    public ResponseEntity<Data> core(@RequestBody dataDTO data) {
        Data _input = new Data(data);
        List<Analysis> results = new ArrayList<>();
        results.add(ls.languageDetector(_input.getContent()));
        if (results.get(0).getResult().substring(0, 2).equals("fr")) {
            List<Token> tokens = ps.Parser(_input.getContent());
            //_input.setResult(results);
        }
        return new ResponseEntity<>(_input, HttpStatus.OK);
    }
}
