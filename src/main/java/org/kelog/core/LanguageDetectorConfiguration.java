package org.kelog.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LanguageDetectorConfiguration {
    
    @Bean
    public KeywordsList keywordsList() {
        return new KeywordsList(Utils.resourceToFile("/words.txt"));
    }
    
    @Bean
    public Trainer trainer(
            KeywordsList keywordsList,
            Parser parser,
            @Value("${codenn.maximumEpochs}") int maximumEpochs,
            @Value("${codenn.maximumError}") double maximumError
    ) {
        return new Trainer(keywordsList, parser, maximumEpochs, maximumError);
    }
    
    @Bean
    public EncogAdapter encogAdapter(Trainer trainer) throws Exception {
        return new EncogAdapter(trainer.createNetwork("trdata/trdata.zip"));
    }
    
    @Bean
    public LanguageDetector languageDetector(EncogAdapter encogAdapter, Parser parser) {
        return new LanguageDetector(encogAdapter, parser);
    }
}
