package org.kelog.web;

import org.kelog.core.Client;
import org.kelog.core.Language;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;

@RestController
public class AppController {
    
    private final Client client;
    
    public AppController(Client client) {
        this.client = client;
    }
    
    @RequestMapping(value = "/ask", method = RequestMethod.GET)
    public EnumMap<Language, Double> ask(@RequestParam("snippet") String snippet) {
        return client.match(snippet);
    }
}
