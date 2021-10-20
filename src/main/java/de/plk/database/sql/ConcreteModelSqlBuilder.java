package de.plk.database.sql;

import de.plk.database.DatabasePool;
import de.plk.database.meta.Column;
import de.plk.database.meta.Table;
import de.plk.database.model.AbstractModel;
import de.plk.database.sql.type.CommandType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ConcreteModelSqlBuilder implements ModelSqlBuilder {

    private final DatabasePool pool;

    private final Class<? extends AbstractModel> modelClass;

    private final ReflectionHandler handler;

    private CommandType commandType;

    private String[] columns;

    private Object[] values;

    private String where;

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
            throw new IllegalArgumentException("The length of the parameter must be equal.");

        this.commandType = CommandType.UPDATE;
        this.columns = columns;
        this.values = values;

        return this;
    }

    @Override
    public ModelSqlBuilder insert(String[] columns, Object[] values) {
        if (columns.length != values.length)
            throw new IllegalArgumentException("The length of the parameter must be equal.");

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
        Connection connection = pool.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(buildSQL())) {
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

                ResultSet resultSet = statement.executeQuery();
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
            exception.printStackTrace();
            return new HashMap<>();
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private String buildSQL() {
        String tableName = handler.getTable().tableName();
        String command = commandType.getCommand();

        switch (commandType) {
            case DELETE:
                command = String.format(command, tableName);
                break;
            case SELECT:
                String selectColumnList = String.join(", ", columns);
                command = String.format(command, selectColumnList, tableName);
                break;
            case INSERT:
                String insertColumnList = String.join(", ", columns);

                String[] values2 = new String[columns.length];
                for (int i = 0; i < values.length; i++) {
                    values2[i] = values[i].toString();
                }

                String insertValuesList = "'" + String.join("', '", Arrays.stream(values2).toArray(String[]::new)) + "'";
                command = String.format(command, tableName, insertColumnList, insertValuesList);
                break;
            case UPDATE:
                StringBuilder updateList = new StringBuilder();

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
