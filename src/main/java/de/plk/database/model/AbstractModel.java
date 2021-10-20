package de.plk.database.model;

import de.plk.database.DatabasePool;
import de.plk.database.sql.ConcreteModelSqlBuilder;
import de.plk.database.sql.ModelSqlBuilder;

/**
 * Defines that any subclass is a database model.
 */
public abstract class AbstractModel {

    /**
     * Returns the model sql builder to perform actions to related database table.
     *
     * @return The model sql builder to perform actions to related database table.
     */
    public static ModelSqlBuilder getModelSqlBuilder(Class<? extends AbstractModel> clazz, DatabasePool pool) {
        return new ConcreteModelSqlBuilder(pool, clazz);
    }

}
