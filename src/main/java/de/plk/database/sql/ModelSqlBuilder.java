package de.plk.database.sql;

import java.util.Map;

/**
 * Represents a sql-builder with the information from the model.
 */
public interface ModelSqlBuilder {

    /**
     * Select command for a SQL-Command.
     *
     * @param columns The columns you want to select.
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder select(String... columns);

    /**
     * Update a row in the database.
     *
     * @param columns The columns you want to update.
     * @param values  The values you want to update the columns with.
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder update(String[] columns, Object[] values);

    /**
     * Inserts a new row into the database-table.
     *
     * @param columns The columns you want to update.
     * @param values  The values you want to update the columns with.
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder insert(String[] columns, Object[] values);

    /**
     * Deletes an row in the database.
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder delete();

    /**
     * Add a condition to the sql-command.
     *
     * @param column  The column you want to check.
     * @param needle  The value you want to check the column with.
     * @param operand The operand you have to check with the condition (=, <>, !=).
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder where(String column, Object needle, String operand);

    /**
     * Add a condition with OR to the sql-command.
     *
     * Add this if you habe already added an {@link #where(String, Object, String)} clausel.
     *
     * @param column  The column you want to check.
     * @param needle  The value you want to check the column with.
     * @param operand The operand you have to check with the condition (=, <>, !=).
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder orWhere(String column, Object needle, String operand);

    /**
     * Add a condition with AND to the sql-command.
     *
     * Add this if you habe already added an {@link #where(String, Object, String)} clausel.
     *
     * @param column  The column you want to check.
     * @param needle  The value you want to check the column with.
     * @param operand The operand you have to check with the condition (=, <>, !=).
     *
     * @return The {@link ModelSqlBuilder}.
     */
    ModelSqlBuilder andWhere(String column, Object needle, String operand);

    /**
     * Run the SQL-Comand as an update.
     */
    void runUpdate();

    /**
     * Returns the map with values, if values are present.
     *
     * If the values are not present, the sql-command will be wrong or your request
     * had still a bad request.
     *
     * @return The map with row values if present.
     */
    Map<String, Object> runQuery();

}
