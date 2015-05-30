package org.kelog.core;

public class CommentRemover {

    public static String removeComments(String fileContent) {
        //TODO if sth fails, just return original content xD

        try {
            fileContent = stripC(fileContent);
            fileContent = stripCPPandRuby(fileContent);
        } catch (Exception e) {
            System.out.println("Error while removing comments, returning original content!");
        }
        return fileContent;
    }

    private static String stripC(String fileContent) {
        StringBuilder sb = new StringBuilder(fileContent);

        // remove from /* to */

        while (sb.indexOf("/*") != -1) {
            int left = sb.indexOf("/*");
            int right = sb.indexOf("*/");
            sb.delete(left, right + 2);
        }

        return sb.toString();
    }

    private static String stripCPPandRuby(String fileContent) {
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

    private static String stripCPPandRubySingleLine(String line) {
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
