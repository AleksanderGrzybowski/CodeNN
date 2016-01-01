package org.kelog.web;

import com.google.inject.Guice;
import org.encog.neural.networks.BasicNetwork;
import org.kelog.core.Trainer;
import org.kelog.core.TrainingModule;
import org.kelog.end.Client;
import spark.Spark;

import java.io.File;

import static spark.Spark.get;
import static spark.Spark.halt;

public class Main {

    public static void main(String[] args) throws Exception {
        String zipFilename = args[0];
        BasicNetwork network = Guice.createInjector(new TrainingModule())
                .getInstance(Trainer.class).createNetwork(zipFilename);

        Client client = Guice.createInjector(new WebModule(network))
                .getInstance(Client.class);

        
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