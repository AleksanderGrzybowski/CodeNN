package org.kelog.core;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.File;

public class TrainingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(File.class).annotatedWith(Names.named("words-file"))
                .toInstance(Utils.resourceToFile("/words.txt"));
        bind(Integer.class).annotatedWith(Names.named("maximum-epochs"))
                .toInstance(10000);
        bind(Double.class).annotatedWith(Names.named("maximum-error"))
                .toInstance(0.001);
    }
}
