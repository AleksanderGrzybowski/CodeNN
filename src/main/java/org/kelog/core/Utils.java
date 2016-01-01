package org.kelog.core;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {
    
    public static File resourceToFile(String resourceFileName) {
        try {
            URL url = Utils.class.getResource(resourceFileName);
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError();
        }
    }
}
