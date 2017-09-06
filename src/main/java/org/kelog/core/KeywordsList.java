package org.kelog.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * This extends is for simplified iteration.
 */
public class KeywordsList extends AbstractCollection<String> {
    private List<String> keywords;
    
    public KeywordsList(File keywordsFile) {
        try {
            keywords = Collections.unmodifiableList(
                    Files.readAllLines(keywordsFile.toPath())
                            .stream()
                            .filter(keyword -> !keyword.startsWith("#"))
                            .collect(toList())
            );
        } catch (IOException e) {
            throw new AssertionError();
        }
    }
    
    @Override
    public Iterator<String> iterator() {
        return keywords.iterator();
    }
    
    @Override
    public int size() {
        return keywords.size();
    }
    
    int indexOf(String guess) {
        return keywords.indexOf(guess);
    }
}
