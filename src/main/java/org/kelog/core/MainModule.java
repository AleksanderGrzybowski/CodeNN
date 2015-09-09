package org.kelog.core;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(File.class).annotatedWith(Names.named("words-file"))
                .toInstance(resourceToFile("/words.txt"));
        bind(File.class).annotatedWith(Names.named("network-file"))
                .toInstance(resourceToFile("/network.eg"));

        bind(Integer.class).annotatedWith(Names.named("maximum-epochs"))
                .toInstance(10000);
        bind(Double.class).annotatedWith(Names.named("maximum-error"))
                .toInstance(0.001);
    }

    private File resourceToFile(String resourceFileName) {
        URL url = MainModule.class.getResource(resourceFileName);
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError();
        }
    }
}
