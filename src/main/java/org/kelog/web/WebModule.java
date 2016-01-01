package org.kelog.web;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.encog.neural.networks.BasicNetwork;
import org.kelog.core.Utils;

import java.io.File;

public class WebModule extends AbstractModule {

    private final BasicNetwork network;

    public WebModule(BasicNetwork network) {
        this.network = network;
    }
    
    @Override
    protected void configure() {
        bind(BasicNetwork.class)
                .toInstance(network);
        bind(File.class).annotatedWith(Names.named("words-file"))
                .toInstance(Utils.resourceToFile("/words.txt"));
    }
}
