package de.plk.database.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Represents the result of an Query.
 *
 * @see ResultSet
 */
public interface Result {

    /**
     * The map of results if present.
     *
     * @return The results as an <K, V> map.
     * @throws SQLException
     */
    Map<String, Object> getValues() throws SQLException;

    /**
     * Checks if a result is invalid because of no results or interupption.
     *
     * @return The information is this request is still invalid or not.
     * @throws SQLException
     */
    default boolean isInvalid() throws SQLException {
        return getValues() == null || getValues().isEmpty();
    }

}
