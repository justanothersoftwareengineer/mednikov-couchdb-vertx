package net.mednikov.tutorials.CouchHumans.dao;

import io.vertx.core.json.JsonObject;
import net.mednikov.tutorials.CouchHumans.data.Human;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;
import org.lightcouch.Response;

import java.util.List;
import java.util.Optional;

public class CouchDbHumansDAOImpl implements IHumansDAO {

    private CouchDbClient couchDbClient;

    @Override
    public void initialize(JsonObject config) {
        CouchDbProperties properties = new CouchDbProperties();
        properties.setDbName(config.getString("db_name"));
        properties.setProtocol(config.getString("db_protocol"));
        properties.setCreateDbIfNotExist(false);
        properties.setHost(config.getString("db_host"));
        properties.setPort(config.getInteger("db_port"));
        properties.setUsername(config.getString("db_user"));
        properties.setPassword(config.getString("db_secret"));
        couchDbClient = new CouchDbClient(properties);
    }

    @Override
    public Optional<Human> findById(String id) {
        try {
            return Optional.of(couchDbClient.find(Human.class, id));
        } catch (NoDocumentException ex){
            return Optional.empty();
        }
    }

    @Override
    public Human add(Human human) {
        Response response = couchDbClient.save(human);
        String id = response.getId();
        human.setId(id);
        return human;
    }

    @Override
    public void remove(String id) {
        couchDbClient.remove(id, ""); /// TODO
    }

    @Override
    public void update(Human human) {
        couchDbClient.update(human);
    }

    @Override
    public List<Human> findAll() {
        couchDbClient.view("_all_docs").query(Human.class);
    }
}
