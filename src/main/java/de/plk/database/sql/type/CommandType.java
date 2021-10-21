package de.plk.database.sql.type;

/**
 * Defines the type of the sql-command.
 */
public enum CommandType {

    SELECT("SELECT %s FROM %s"),
    UPDATE("UPDATE %s SET %s"),
    DELETE("DELETE FROM %s"),
    INSERT("INSERT INTO %s (%s) VALUES (%s)"),

    WHERE("WHERE %s %s %s");

    /**
     * The basic-sql-command structure.
     */
    private final String command;

    /**
     * Creates a new enum with the constants on top.
     *
     * @param command The basic-sql-command structure.
     */
    CommandType(String command) {
        this.command = command;
    }

    /**
     * Returns the basic-sql-command structure.
     *
     * @return The basic-sql-command structure.
     */
    public String getCommand() {
        return command;
    }
}
