package de.plk.database.sql;

import java.sql.SQLException;
import java.util.Map;

public interface Result {

    Map<String, Object> getValues() throws SQLException;

    default boolean isInvalid() throws SQLException {
        return getValues() == null || getValues().isEmpty();
    }

}
