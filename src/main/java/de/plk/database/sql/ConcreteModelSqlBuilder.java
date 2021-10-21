package de.plk.database.sql;

import de.plk.database.DatabasePool;

import de.plk.database.model.AbstractModel;
import de.plk.database.sql.type.CommandType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents the implementation from {@link ModelSqlBuilder} interface.
 */
public class ConcreteModelSqlBuilder implements ModelSqlBuilder {

    /**
     * The database pool of {@link Connection}`s.
     */
    private final DatabasePool pool;

    /**
     * The class reference of the model we should handle with.
     */
    private final Class<? extends AbstractModel> modelClass;

    /**
     * The handler, who read the meta information (Annotations).
     *
     * This will return the information about the table
     * and columns from the annotations.
     */
    private final ReflectionHandler handler;

    /**
     * The command-type of this SQL-Command.
     *
     * e.g.: SELECT, UPDATE, DELETE, INSERT
     */
    private CommandType commandType;

    /**
     * The columns you want to update.
     */
    private String[] columns;

    /**
     * The values you want to update the columns with.
     */
    private Object[] values;

    /**
     * The conditions for the sql-commands.
     *
     * synonyms: where-clausel
     */
    private String where;

    /**
     * Creates a new instance of this class.
     *
     * @param pool       The database pool of {@link Connection}`s.
     * @param modelClass The class reference of the model we should handle with.
     */
    public ConcreteModelSqlBuilder(DatabasePool pool, Class<? extends AbstractModel> modelClass) {
        this.pool = pool;
        this.modelClass = modelClass;

        handler = new ReflectionHandler(modelClass);
    }

    @Override
    public ModelSqlBuilder select(String... columns) {
        this.commandType = CommandType.SELECT;
        this.columns = columns;

        return this;
    }

    @Override
    public ModelSqlBuilder update(String[] columns, Object[] values) {
        if (columns.length != values.length)
            throw new IllegalArgumentException("The length of the parameters must be have same size.");

        this.commandType = CommandType.UPDATE;
        this.columns = columns;
        this.values = values;

        return this;
    }

    @Override
    public ModelSqlBuilder insert(String[] columns, Object[] values) {
        if (columns.length != values.length)
            throw new IllegalArgumentException("The length of the parameters must be have same size.");

        this.commandType = CommandType.INSERT;
        this.columns = columns;
        this.values = values;

        return this;
    }

    @Override
    public ModelSqlBuilder delete() {
        this.commandType = CommandType.DELETE;
        return this;
    }

    @Override
    public ModelSqlBuilder where(String column, Object needle, String operand) {
        if (column == null || needle == null || operand == null)
            return this;

        String command = CommandType.WHERE.getCommand();
        where = String.format(command, column, operand, needle);

        return this;
    }

    @Override
    public ModelSqlBuilder andWhere(String column, Object needle, String operand) {
        if (column == null || needle == null || operand == null)
            return this;

        String command = CommandType.WHERE.getCommand();
        command = command.replace("WHERE", "AND");

        where += " " + String.format(command, column, operand, String.valueOf(needle));

        return this;
    }

    @Override
    public ModelSqlBuilder orWhere(String column, Object needle, String operand) {
        if (column == null || needle == null || operand == null)
            return this;

        String command = CommandType.WHERE.getCommand();
        command = command.replace("WHERE", "OR");

        where += " " + String.format(command, column, operand, String.valueOf(needle));

        return this;
    }

    @Override
    public void runUpdate() {
        final Connection connection = pool.getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(buildSQL())) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public Map<String, Object> runQuery() {
        Connection connection = pool.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(buildSQL())) {
            Result result = () -> {
                final Map<String, Object> values = new HashMap<>();

                // Get the result-set and put the queried columns to the map with each values.
                final ResultSet resultSet = statement.executeQuery();
                if (resultSet == null) return values;

                if (resultSet.next()) {
                    for (int i = 0; i < columns.length; i++) {
                        values.put(columns[i], resultSet.getObject(columns[i]));
                    }
                }

                return values;
            };

            return result.getValues();
        } catch (SQLException exception) {
            // Returns a clear hashmap, if this query will be interrupted.
            exception.printStackTrace();
            return new HashMap<>();
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private String buildSQL() {
        final String tableName = handler.getTable().tableName();
        String command = commandType.getCommand();

        switch (commandType) {
            case DELETE:
                command = String.format(command, tableName);
                break;

            case SELECT:
                final String selectColumnList = String.join(", ", columns);
                command = String.format(command, selectColumnList, tableName);
                break;

            case INSERT:
                final String insertColumnList = String.join(", ", columns);
                final String[] stringValues = new String[columns.length];

                for (int i = 0; i < values.length; i++) {
                    stringValues[i] = values[i].toString();
                }

                final String insertValuesList = "'" + String.join("', '", stringValues) + "'";
                command = String.format(command, tableName, insertColumnList, insertValuesList);
                break;

            case UPDATE:
                final StringBuilder updateList = new StringBuilder();

                for (int i = 0; i < columns.length; i++) {
                    updateList.append(columns[i])
                            .append(" = ")
                            .append(String.valueOf(values[i]))
                            .append(", ");
                }

                updateList.setLength(updateList.length() - 2);
                command = String.format(tableName, updateList.toString());
                break;
        }

        if (where != null)
            command += " " + where;

        return command += ";";
    }

}
