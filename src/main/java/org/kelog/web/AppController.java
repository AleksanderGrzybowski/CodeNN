package org.kelog.web;

import org.kelog.detector.LanguageDetector;
import org.kelog.detector.Language;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;

@RestController
public class AppController {
    
    private final LanguageDetector languageDetector;
    
    public AppController(LanguageDetector languageDetector) {
        this.languageDetector = languageDetector;
    }
    
    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public EnumMap<Language, Double> ask(@RequestParam("snippet") String snippet) {
        return languageDetector.match(snippet);
    }
}
