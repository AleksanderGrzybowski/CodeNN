package org.kelog.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.kelog.core.MainModule;
import org.kelog.end.Client;
import spark.Spark;

import java.io.File;

import static spark.Spark.get;
import static spark.Spark.halt;

public class Main {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new MainModule());
        Client client = injector.getInstance(Client.class);

        Spark.externalStaticFileLocation(System.getProperty("user.dir") + File.separator + "web-app");

        get("/ask", (req, res) -> {
            try {
                return client.match(req.queryParams("snippet"));
            } catch (Exception e) {
                halt(500);
                return "";
            }
        }, new JsonTransformer());
    }
}