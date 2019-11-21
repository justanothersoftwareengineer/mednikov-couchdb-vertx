package net.mednikov.tutorials.CouchHumans.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import net.mednikov.tutorials.CouchHumans.dao.IHumansDAO;
import net.mednikov.tutorials.CouchHumans.data.Human;

import java.util.List;

public class HumanRouter {

    private Router router;
    private IHumansDAO dao;
    private Vertx vertx;

    public HumanRouter(Vertx vertx, IHumansDAO dao){
        this.router = Router.router(vertx);
        this.vertx = vertx;
        this.dao = dao;
        attachRoutes();
    }

    private void attachRoutes(){
        router.route("/*").handler(BodyHandler.create());
        router.post("/").handler(this::addHuman);
        router.put("/:id").handler(this::updateHuman);
        router.delete("/:id").handler(this::removeHuman);
        router.get("/one/:id").handler(this::findOne);
        router.get("/all").handler(this::query);
    }

    public Router getRouter(){
        return router;
    }

    private void addHuman (RoutingContext context){
        JsonObject body = context.getBodyAsJson();
        Human payload = Json.decodeValue(body.encode(), Human.class);
        vertx.executeBlocking(promise -> {
            Human human = dao.add(payload);
            context.response().setStatusCode(200).end(JsonObject.mapFrom(human).encode());
            promise.complete();
        });
    }

    private void removeHuman(RoutingContext context){
        String id = context.pathParam("id");
        vertx.executeBlocking(promise -> {
            dao.remove(id);
            context.response().setStatusCode(200).end();
            promise.complete();
        });
    }

    private void updateHuman(RoutingContext context){
        JsonObject body = context.getBodyAsJson();
        Human payload = Json.decodeValue(body.encode(), Human.class);
        vertx.executeBlocking(promise -> {
            dao.update(payload);
            context.response().setStatusCode(200).end();
            promise.complete();
        });
    }

    private void findOne(RoutingContext context){
        String id = context.pathParam("id");
        vertx.executeBlocking(promise -> {
            dao.findById(id).ifPresentOrElse(human->{
                JsonObject response = JsonObject.mapFrom(human);
                context.response().setStatusCode(200).end(response.encode());
            }, ()-> context.response().setStatusCode(404).end());
            promise.complete();
        });
    }

    private void query(RoutingContext context){
        vertx.executeBlocking(promise -> {
            List<Human> results = dao.findAll();
            context.response().setStatusCode(200).end(JsonObject.mapFrom(results).encode());
            promise.complete();
        });
    }
}
