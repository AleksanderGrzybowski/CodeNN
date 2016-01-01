package org.kelog.core;

import com.google.inject.Singleton;

import java.util.Arrays;
import java.util.stream.Collectors;

@Singleton
public class CommentRemover {

    private static final String C_COMMENT_START = "/*";
    private static final String C_COMMENT_END = "*/";
    private static final String CPP_COMMENT_START = "//";
    private static final String RUBY_COMMENT_START = "#";

    public String removeComments(String fileContent) {
        // if sth fails, just return original content xD
        // cheap but does not do too much harm xD

        try {
            fileContent = stripCPPandRuby(stripC(fileContent)); // streams for poor!
        } catch (Exception ignored) {
        }
        return fileContent;
    }

    private String stripC(String fileContent) {
        StringBuilder sb = new StringBuilder(fileContent);

        int left, right;
        while ((left = sb.indexOf(C_COMMENT_START)) != -1) {
            right = sb.indexOf(C_COMMENT_END);
            sb.delete(left, right + C_COMMENT_END.length());
        }

        return sb.toString();
    }

    private String stripCPPandRuby(String fileContent) {
        return Arrays.stream(fileContent.split("\n"))
                .map(this::stripCPPandRubySingleLine)
                .collect(Collectors.joining("\n"));
    }

    private String stripCPPandRubySingleLine(String line) {
        // remove // and # to the end of line

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
