package de.plk.database;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.setProperty("hostname", "127.0.0.1");
        properties.setProperty("port", "3306");
        properties.setProperty("database", "nice");
        properties.setProperty("username", "root");
        properties.setProperty("password", "");

        DatabasePool pool = new DatabasePool(new DatabaseSource(DatabaseType.MARIADB, properties), 2);

        // new PlayerModelCrud(pool).create(new PlayerModel(2, "TIM"));
        // new PlayerModelCrud(pool).delete(UUID.randomUUID());
    }

}
