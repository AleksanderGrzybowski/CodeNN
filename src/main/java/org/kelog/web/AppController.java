package org.kelog.web;

import org.kelog.detector.LanguageDetector;
import org.kelog.detector.Language;
import org.springframework.web.bind.annotation.*;

import java.util.EnumMap;

@RestController
@CrossOrigin
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
