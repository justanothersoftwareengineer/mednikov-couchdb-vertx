package net.mednikov.tutorials.CouchHumans.app;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import net.mednikov.tutorials.CouchHumans.dao.CouchDbHumansDAOImpl;

public class CouchHumanApp {

    private static ConfigRetrieverOptions getConfigOptions(){
        ConfigStoreOptions options = new ConfigStoreOptions();
        options.setType("file");
        options.setFormat("properties");
        options.setConfig(new JsonObject().put("path", "test-config.properties"));
        return new ConfigRetrieverOptions().addStore(options);
    }


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new CouchHumanVerticle(new CouchDbHumansDAOImpl(), getConfigOptions()));
    }
}
