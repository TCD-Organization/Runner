package com.example.core.services;

import com.example.core.models.Analysis;
import org.springframework.stereotype.Service;


@Service
public class SourceService {
    public Analysis findSource(String content){
        Analysis analysis = new Analysis();
        analysis.setStart(Tools.instantTimestamp());
        analysis.setA_id(6);
        analysis.setFlag("Source Finder");
        // TODO
        analysis.setResult("Not found");
        analysis.setEnd(Tools.instantTimestamp());
        analysis.setDelay(analysis.getEnd().getTime() - analysis.getStart().getTime());
        return analysis;
    }
}
