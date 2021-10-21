package de.plk.database.crud;

import de.plk.database.DatabasePool;
import de.plk.database.model.AbstractModel;
import de.plk.database.model.PlayerModel;
import de.plk.database.sql.ModelSqlBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * The CRUD-Implementation for the {@link PlayerModel}.
 */
public class PlayerModelCrud implements CrudInterface<UUID, PlayerModel> {

    /**
     * The Model-SQL builder for {@link PlayerModel}´s.
     */
    private final ModelSqlBuilder modelSqlBuilder;

    /**
     * Creates a new instance of this class.
     *
     * @param pool The database pool.
     */
    public PlayerModelCrud(DatabasePool pool) {
        this.modelSqlBuilder = AbstractModel.getModelSqlBuilder(PlayerModel.class, pool);
    }

    @Override
    public void create(PlayerModel model) {
        this.modelSqlBuilder.insert(new String[]{
                "uuid", "name"
        }, new Object[]{
                model.getUuid().toString(), model.getName()
        }).runUpdate();
    }

    @Override
    public PlayerModel read(UUID key) {
        Map<String, Object> results = this.modelSqlBuilder.select("uuid", "name")
                .where("uuid", key.toString(), "LIKE")
                .runQuery();

        if (results.isEmpty()) {
            create(new PlayerModel(key, ""));
            return read(key);
        }

        // Return the object with specific databae data.
        return new PlayerModel(UUID.fromString(
                results.get("uuid").toString()
        ), (String) results.get("name"));
    }

    @Override
    public void update(UUID key, PlayerModel model) {
        this.modelSqlBuilder.update(new String[]{
                "name"
        }, new Object[]{
                model.getName()
        }).where("uuid", key.toString(), "LIKE").runUpdate();
    }

    @Override
    public void delete(UUID key) {
        this.modelSqlBuilder.delete().where("uuid", key.toString(), "LILE").runUpdate();
    }
}
