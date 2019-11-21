package net.mednikov.tutorials.CouchHumans.app;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import net.mednikov.tutorials.CouchHumans.dao.IHumansDAO;
import net.mednikov.tutorials.CouchHumans.router.HumanRouter;

public class CouchHumanVerticle extends AbstractVerticle {

    private ConfigRetrieverOptions configRetrieverOptions;
    private IHumansDAO dao;

    public CouchHumanVerticle(IHumansDAO dao, ConfigRetrieverOptions configRetrieverOptions){
        this.configRetrieverOptions = configRetrieverOptions;
        this.dao = dao;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
        configRetriever.getConfig(conf->{
            if (conf.succeeded()){
                JsonObject config = conf.result();
                dao.initialize(config);
                HttpServer server = vertx.createHttpServer();
                HumanRouter router = new HumanRouter(vertx, dao);
                server.requestHandler(router.getRouter());
                int port = config.getInteger("app_port");
                server.listen(port, res->{
                    if (res.succeeded()){
                        promise.complete();
                    } else {
                        promise.fail(res.cause());
                    }
                });
            } else {
                promise.fail(conf.cause());
            }
        });
    }

}
