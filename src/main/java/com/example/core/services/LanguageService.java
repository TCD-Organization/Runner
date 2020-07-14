package com.example.core.services;

import com.example.core.models.Analysis;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.springframework.stereotype.Service;


@Service
public class LanguageService {

    public String detectLang(String text) {
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();
        LanguageResult result = languageDetector.detect(text);
        if(result.isUnknown()){
            return "Not acceptable";
        }else{
            return result.getLanguage()+"["+result.getRawScore()*100+"%]";
        }
    }

    public Analysis languageDetector(String text){
        Analysis lang = new Analysis();
        lang.setStart(Tools.instantTimestamp());
        lang.setA_id(0);
        lang.setFlag("Language Detection");
        lang.setResult(detectLang(text));
        lang.setEnd(Tools.instantTimestamp());
        lang.setDelay(lang.getEnd().getTime() - lang.getStart().getTime());
        return lang;
    }
}
