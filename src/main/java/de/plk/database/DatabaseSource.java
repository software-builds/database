package de.plk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Holds basic information for new database {@link Connection}`s.
 * <p>
 * Also this class will hold basic informations for the {@link DatabasePool}.
 */
public class DatabaseSource {

    /**
     * The sql-server engine type for the connection building.
     */
    private final DatabaseType databaseType;

    /**
     * The replacements (inc. the credentials) for the database connection-URI.
     */
    private final String[] replacements;

    /**
     * Creates a new new instance of this class.
     *
     * @param databaseType The sql-server engine type for the connection building.
     * @param properties   The properties with credentials for new databas conncetions.
     */
    public DatabaseSource(DatabaseType databaseType, Properties properties) {
        if (properties.isEmpty())
            throw new RuntimeException("The properties file cannot be clear.");

        final String[] requirements = databaseType.getRequirements();

        // Declare the replacements array (Size of requirements).
        this.replacements = new String[requirements.length];

        // Push information from the requirements variable to the replacement variable (credentials).
        for (int i = 0; i < requirements.length; i++) {
            if (properties.getProperty(requirements[i]) == null)
                throw new RuntimeException("The property " + requirements[i] + " are not set.");

            this.replacements[i] = properties.getProperty(requirements[i]);
        }

        this.databaseType = databaseType;
    }

    /**
     * Creates a new database {@link Connection}.
     *
     * @return The database {@link Connection}.
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException, ClassNotFoundException {
        String connectionURI = databaseType.getConnectionURI(replacements);

        Class.forName(databaseType.getDriver());
        return DriverManager.getConnection(connectionURI);
    }

    /**
     * Returns the sql-server engine type for the connection building.
     *
     * @return The sql-server engine type for the connection building.
     */
    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}