package de.plk.database.model;

import de.plk.database.DatabasePool;
import de.plk.database.sql.ConcreteModelSqlBuilder;
import de.plk.database.sql.ModelSqlBuilder;

import java.sql.Connection;

/**
 * Defines that any subclass is a database model.
 */
public abstract class AbstractModel {

    /**
     * Returns the model sql builder to perform actions to related database table
     *
     * @param modelClass The class of the model.
     * @param pool       The database pool, to hit a {@link Connection}.
     *
     * @return The model sql builder to perform actions to related database table.
     */
    public static ModelSqlBuilder getModelSqlBuilder(Class<? extends AbstractModel> modelClass, DatabasePool pool) {
        return new ConcreteModelSqlBuilder(pool, modelClass);
    }

}
