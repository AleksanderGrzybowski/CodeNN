package org.kelog.core;

import lombok.RequiredArgsConstructor;

import java.util.EnumMap;

@RequiredArgsConstructor
public class LanguageDetector {
    
    private final EncogAdapter adapter;
    private final Parser parser;
    
    public EnumMap<Language, Double> match(String snippet) {
        double[] question = parser.histogram(snippet);
        double[] answer = adapter.ask(question);
        
        return arrayToMap(answer);
    }
    
    private static EnumMap<Language, Double> arrayToMap(double[] networkResponse) {
        EnumMap<Language, Double> map = new EnumMap<>(Language.class);
        
        for (Language lang : Language.values()) {
            map.put(lang, networkResponse[lang.ordinal()]);
        }
        
        return map;
    }
}
