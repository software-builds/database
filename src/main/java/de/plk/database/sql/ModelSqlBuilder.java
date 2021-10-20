package de.plk.database.sql;

import de.plk.database.sql.type.CommandType;

import java.sql.ResultSet;
import java.util.Map;

public interface ModelSqlBuilder {

    ModelSqlBuilder select(String... columns);

    ModelSqlBuilder update(String[] columns, Object[] values);

    ModelSqlBuilder insert(String[] columns, Object[] values);

    ModelSqlBuilder delete();

    ModelSqlBuilder where(String column, Object needle, String operand);

    ModelSqlBuilder orWhere(String column, Object needle, String operand);

    ModelSqlBuilder andWhere(String column, Object needle, String operand);

    void runUpdate();

    Map<String, Object> runQuery();

}
