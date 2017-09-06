package org.kelog.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
class TrainerConfiguration {
    
    @Bean
    public KeywordsList keywordsList() {
        return new KeywordsList(Utils.resourceToFile("/words.txt"));
    }
    
    @Bean
    public Trainer trainer(KeywordsList keywordsList, Parser parser) {
        return new Trainer(
                keywordsList,
                parser,
                10000,
                0.001,
                Logger.getLogger("TODO")
        );
    }
}
