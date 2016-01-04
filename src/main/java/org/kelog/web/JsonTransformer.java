package org.kelog.web;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Copied from spark-java tutorial.
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
