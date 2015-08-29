package org.kelog.web;

import org.kelog.end.Client;
import spark.Spark;

import java.io.File;

import static spark.Spark.get;
import static spark.Spark.halt;

public class Main {

    public static void main(String[] args) {
        Spark.externalStaticFileLocation(System.getProperty("user.dir") + File.separator + "web-app");

        get("/ask", (req, res) -> {
            try {
                String snippet = req.queryParams("snippet");

                Client client = new Client();

                return client.match(snippet);
            } catch (Exception e) {
                halt(500);
                return "";
            }
        }, new JsonTransformer());
    }
}