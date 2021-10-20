package de.plk.database.crud;

import de.plk.database.DatabasePool;
import de.plk.database.model.AbstractModel;
import de.plk.database.model.PlayerModel;
import de.plk.database.sql.ModelSqlBuilder;
import de.plk.database.sql.ReflectionHandler;
import de.plk.database.sql.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class PlayerModelCrud implements CrudInterface<UUID, PlayerModel> {

    private final ModelSqlBuilder modelSqlBuilder;

    public PlayerModelCrud(DatabasePool pool) {
        this.modelSqlBuilder = AbstractModel.getModelSqlBuilder(PlayerModel.class, pool);
    }

    @Override
    public void create(PlayerModel model) {
        this.modelSqlBuilder.insert(new String[]{"id", "name"}, new Object[]{model.getId(), model.getName()}).runUpdate();
    }

    @Override
    public PlayerModel read(UUID key) {
        Map<String, Object> result = this.modelSqlBuilder.select("id", "name").where("id", "2", "=").runQuery();

        return new PlayerModel((Integer) result.get("id"), (String) result.get("name"));

    }

    @Override
    public void update(UUID key, PlayerModel model) {

    }

    @Override
    public void delete(UUID key) {
        this.modelSqlBuilder.delete().where("id", "'2'", "=").runUpdate();

    }
}
