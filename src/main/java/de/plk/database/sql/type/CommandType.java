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

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
