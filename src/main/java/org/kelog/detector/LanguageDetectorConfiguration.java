package org.kelog.detector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
class LanguageDetectorConfiguration {
    
    @Bean
    public KeywordsList keywordsList(@Value("${codenn.keywordsResourceFilename}") String keywordsFilename) {
        return new KeywordsList(resourceToFile(keywordsFilename));
    }
    
    @Bean
    public Trainer trainer(
            KeywordsList keywordsList,
            Parser parser,
            @Value("${codenn.maximumEpochs}") int maximumEpochs,
            @Value("${codenn.maximumError}") double maximumError,
            @Value("${codenn.trainingDataResourceFilename}") String trainingDataFilename
    ) {
        return new Trainer(keywordsList, parser, maximumEpochs, maximumError, resourceToFile(trainingDataFilename));
    }
    
    @Bean
    public LanguageDetector languageDetector(Trainer trainer, Parser parser) throws Exception {
        return new LanguageDetector(new EncogAdapter(trainer.createNetwork()), parser);
    }
    
    private static File resourceToFile(String resourceFileName) {
        try {
            URL url = LanguageDetectorConfiguration.class.getResource(resourceFileName);
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError();
        }
    }
}
