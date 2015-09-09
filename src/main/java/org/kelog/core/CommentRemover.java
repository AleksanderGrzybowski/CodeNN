package org.kelog.core;

import com.google.inject.Singleton;

@Singleton
public class CommentRemover {

    public String removeComments(String fileContent) {
        // if sth fails, just return original content xD
        // cheap but does not do too much harm xD

        try {
            fileContent = stripC(fileContent);
            fileContent = stripCPPandRuby(fileContent);
        } catch (Exception ignored) {
        }
        return fileContent;
    }

    private String stripC(String fileContent) {
        StringBuilder sb = new StringBuilder(fileContent);

        // remove from /* to */
        while (sb.indexOf("/*") != -1) {
            int left = sb.indexOf("/*");
            int right = sb.indexOf("*/");
            sb.delete(left, right + 2);
        }

        return sb.toString();
    }

    private String stripCPPandRuby(String fileContent) {
        String[] lines = fileContent.split("\n"); // mutable!

        for (int i = 0; i < lines.length; i++) {
            lines[i] = stripCPPandRubySingleLine(lines[i]);
        }

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    private String stripCPPandRubySingleLine(String line) {
        // remove // and # to the end of line

        StringBuilder sb = new StringBuilder(line);
        int i;

        while ((i = sb.indexOf("//")) != -1) {
            sb.delete(i, sb.length());
        }

        while ((i = sb.indexOf("#")) != -1) {
            sb.delete(i, sb.length());
        }

        return sb.toString();
    }
}
