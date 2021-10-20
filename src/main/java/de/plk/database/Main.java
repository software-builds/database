package de.plk.database;

import de.plk.database.crud.PlayerModelCrud;
import de.plk.database.model.PlayerModel;
import de.plk.database.sql.ConcreteModelSqlBuilder;
import de.plk.database.sql.ReflectionHandler;

import java.util.Properties;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("hostname", "127.0.0.1");
        properties.setProperty("port", "3306");
        properties.setProperty("database", "nice");
        properties.setProperty("username", "root");
        properties.setProperty("password", "");

        DatabasePool pool = new DatabasePool(new DatabaseSource(DatabaseType.MARIADB, properties), 2);

        //new PlayerModelCrud(pool).create(new PlayerModel(2, "TIM"));
        new PlayerModelCrud(pool).delete(UUID.randomUUID());
    }

}
