package org.kelog.web;

import org.kelog.core.Language;
import org.kelog.end.Client;
import spark.Spark;

import java.util.EnumMap;

import static spark.Spark.get;

public class Main {

    public static void main(String[] args) {
        Spark.staticFileLocation("web-app");

        get("/ask", (req, res) -> {
            String snippet = req.queryParams("snippet");

            Client client = new Client();
            EnumMap<Language, Double> result = client.match(snippet);

            return result;
        }, new JsonTransformer());

    }
}
