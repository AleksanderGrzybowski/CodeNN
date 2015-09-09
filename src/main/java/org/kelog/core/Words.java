package org.kelog.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class Words {
    public final List<String> list;

    @Inject
    public Words(@Named("words-file") File wordsFile) {
        try {
            list = Collections.unmodifiableList(
                    Files.readAllLines(wordsFile.toPath())
                            .stream()
                            .filter(word -> !word.startsWith("#"))
                            .collect(toList())
            );
        } catch (IOException e) {
            throw new AssertionError();
        }
    }
}
