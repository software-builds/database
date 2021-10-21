package de.plk.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Defines a pool of {@link Connection}`s to support useless watings.
 */
public class DatabasePool {

    /**
     * The pool of active connections to support useless waitings.
     */
    private static final Stack<Connection> POOL = new Stack<>();

    /**
     * The database informations with credentials to create new {@link Connection}`s.
     */
    private final DatabaseSource databaseSource;

    /**
     * The size of the active connections in the pool.
     */
    private final int pool_size;

    /**
     * Creates a new database pool with a specified pool size.
     *
     * @param databaseSource The database informations with credentials to create new {@link Connection}`s.
     * @param pool_size      The size of the active {@link Connection}`s in the pool.
     */
    public DatabasePool(DatabaseSource databaseSource, int pool_size) {
        this.databaseSource = databaseSource;
        this.pool_size = pool_size;

        fill();
    }

    /**
     * Creates a new database pool with a default pool size of five.
     *
     * @see DatabasePool#DatabasePool(DatabaseSource, int)
     *
     * @param databaseSource The database informations with credentials to create new {@link Connection}`s.
     */
    public DatabasePool(DatabaseSource databaseSource) {
        this(databaseSource, 5);
    }

    /**
     * Get one of the {@link Connection} from the {@link #POOL}.
     *
     * @return An {@link Connection} of the {@link #POOL}.
     */
    public Connection getConnection() {
        Connection connection = null;

        if (POOL.isEmpty())
            fill();

        // Check if the connection is valid or bad.
        connection = POOL.pop();
        if (isValid(connection))
            return connection;

        fill();
        return getConnection();
    }

    /**
     * Releases a connection and put it back to {@link #POOL} if their is valid.
     *
     * If this {@link Connection} is not valid, this function will add a
     * new {@link Connection} to {@link #POOL}.

     * @param connection The {@link Connection} you want to release.
     */
    public void releaseConnection(Connection connection) {
        if (POOL.size() < pool_size && isValid(connection)) {
            POOL.push(connection);
        } else if (POOL.size() < pool_size && !isValid(connection)) {
            POOL.push(createConnection());
        }
    }

    /**
     * Check if an {@link Connection} is valid.
     *
     * @param connection The {@link Connection} to check if their is valid.
     *
     * @return The boolean if the {@link Connection} is valid.
     */
    private boolean isValid(Connection connection) {
        boolean state = true;

        try {
            state = !(connection == null && connection.isClosed()) && connection.isValid(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return state;
    }

    /**
     * Creates a new {@link Connection} with the information from the {@link DatabaseSource}.
     *
     * @return The new {@link Connection}.
     */
    private Connection createConnection() {
        try  {
            return databaseSource.createConnection();
        } catch (SQLException exception) {
            throw new RuntimeException("Connection #1 cannot be created.");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Database-Driver cannot be founded.");
        }
    }

    /**
     * This method will fillup the {@link #POOL} with new {@link Connection}`s.
     */
    private void fill() {
        int difference = pool_size - POOL.size();

        for (int i = 0; i < difference; i++)
            POOL.push(createConnection());
    }

}