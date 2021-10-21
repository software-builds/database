package de.plk.database;

/**
 * Represents the type (engine) of the sql-server.
 *
 * e.g.: MariaDB, MYSQL, SQLite
 */
public enum DatabaseType {

    /**
     * SQLite.
     *
     * @Todo Driver implementation.
     */
    SQLITE("sqlite:%s", "", new String[] {
            "database"
    }),

    /**
     * MariaDB/ MySQL.
     */
    MARIADB("mysql://%s:%s/%s?user=%s&password=%s", "com.mysql.cj.jdbc.Driver", new String[] {
            "hostname",
            "port",
            "database",
            "username",
            "password",
    });

    /**
     * The connection uri for the {@link java.sql.DriverManager#getConnection(String)} method.
     */
    private final String connectionURI;

    /**
     * The driver class of the sql-server engine.
     */
    private final String driver;

    /**
     * The requirements for the Authentication to the sql-daemon.
     */
    private final String[] requirements;


    /**
     * This constructor will create an instance of an sql-server-engine type.
     *
     * You can explicit distinguish the server-engine.
     *
     * @param connectionURI The connection uri for the {@link java.sql.DriverManager#getConnection(String)} method.
     * @param driver        The driver class of the sql-server engine.
     * @param requirements  The requirements for the Authentication to the sql-daemon.
     */
    DatabaseType(String connectionURI, String driver, String... requirements) {
        this.connectionURI = connectionURI;
        this.driver = driver;
        this.requirements = requirements;
    }

    /**
     * Get the connection URI with the specific data.
     *
     * @param replacements The specific data as a replacement.
     *
     * @return The connection URI value as string.
     */
    public String getConnectionURI(String... replacements) {
        return "jdbc:" + String.format(connectionURI, replacements);
    }

    /**
     * Returns the driver class of the sql-server engine.
     *
     * @return The driver class of the sql-server engine.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Returns the requirements for the sql-engine-type.
     *
     * @return The requirements for the sql-engine-type.
     */
    public String[] getRequirements() {
        return requirements;
    }
}