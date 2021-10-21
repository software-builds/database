package de.plk.database.sql.type;

/**
 * Definies the column data-type of an sql-server engine.
 */
public enum ColumnDataType {

    VARCHAR, LONG,
    TEXT, INT, BOOL;

    /**
     * Returns The length of the data-type.
     *
     * @param size The length of the data-type.
     *
     * @return The string for the sql-command.
     */
    public String withSize(int size) {
        return name() + "(" + size + ")";
    }

}
