package org.kelog.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Component
@RequiredArgsConstructor
public class Parser {
    
    private final CommentRemover commentRemover;
    private final KeywordsList keywordsList;
    
    public double[] histogram(String snippet) {
        double[] histogram = parse(snippet);
        
        return normalizedHistogram(histogram);
    }
    
    public double[] histogram(File file) {
        String snippet = readFromFile(file);
        snippet = commentRemover.removeComments(snippet);
        return histogram(snippet);
    }
    
    private String readFromFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError();
        }
    }
    
    private double[] parse(String snippet) {
        int index = 0;
        int length = snippet.length();
        double[] histogram = new double[keywordsList.size()];
        
        try {
            outer:
            while (index < length) {
                for (String guess : keywordsList) {
                    if (snippet.startsWith(guess, index)) {
                        index += guess.length();
                        histogram[keywordsList.indexOf(guess)] += 1;
                        continue outer;
                    }
                }
                index++;
            }
        } catch (IndexOutOfBoundsException ignored) { // not too pretty, but simpler
        }
        
        return histogram;
    }
    
    public double[] normalizedHistogram(double[] histogram) {
        double[] h = histogram.clone();
        double sum = 0.0;
        
        for (double val : h) {
            sum += val;
        }
        
        if (sum != 0) {
            for (int i = 0; i < h.length; ++i) {
                h[i] /= sum;
            }
        }
        
        return h;
    }
}
