package org.kelog.core;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This is cheap and dirty way, but hey, it works
 * and doesn't do too much harm if it fails
 */
@Component
class CommentRemover {
    
    private static final String C_COMMENT_START = "/*";
    private static final String C_COMMENT_END = "*/";
    private static final String CPP_COMMENT_START = "//";
    private static final String RUBY_COMMENT_START = "#";
    
    public String removeComments(String fileContent) {
        try {
            fileContent = stripCPPandRuby(stripC(fileContent));
        } catch (Exception ignored) {
        }
        return fileContent;
    }
    
    private static String stripC(String fileContent) {
        StringBuilder sb = new StringBuilder(fileContent);
        
        int left, right;
        while ((left = sb.indexOf(C_COMMENT_START)) != -1) {
            right = sb.indexOf(C_COMMENT_END);
            sb.delete(left, right + C_COMMENT_END.length());
        }
        
        return sb.toString();
    }
    
    private static String stripCPPandRuby(String fileContent) {
        return Arrays.stream(fileContent.split("\n"))
                .map(CommentRemover::stripCPPandRubySingleLine)
                .collect(Collectors.joining("\n"));
    }
    
    private static String stripCPPandRubySingleLine(String line) {
        StringBuilder sb = new StringBuilder(line);
        int i;
        
        while ((i = sb.indexOf(CPP_COMMENT_START)) != -1) {
            sb.delete(i, sb.length());
        }
        
        while ((i = sb.indexOf(RUBY_COMMENT_START)) != -1) {
            sb.delete(i, sb.length());
        }
        
        return sb.toString();
    }
}
