package net.mednikov.tutorials.CouchHumans.dao;

import io.vertx.core.json.JsonObject;
import net.mednikov.tutorials.CouchHumans.data.Human;

import java.util.List;
import java.util.Optional;

public interface IHumansDAO {

    void initialize (JsonObject config);

    Optional<Human> findById (String id);

    Human add (Human human);

    void remove (String id);

    void update (Human human);

    List<Human> findAll();
}
