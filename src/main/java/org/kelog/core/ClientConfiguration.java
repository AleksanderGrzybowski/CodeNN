package org.kelog.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ClientConfiguration {
    
    @Bean
    public EncogAdapter encogAdapter(Trainer trainer) throws Exception {
        return new EncogAdapter(trainer.createNetwork("trdata/trdata.zip"));
    }
    
    @Bean
    public Client client(EncogAdapter encogAdapter, Parser parser) {
        return new Client(encogAdapter, parser);
    }
}
