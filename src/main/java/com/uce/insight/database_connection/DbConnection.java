package com.uce.insight.database_connection;
import java.io.IOException;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private static DbConnection instance;
    private HikariDataSource dataSource;

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo config.properties en resources");
            }

            Properties prop = new Properties();
            prop.load(input);

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

        } catch (IOException ex) {
            throw new RuntimeException("Error al leer el archivo db.properties", ex);
        }
    }

    private DbConnection() {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Variables de entorno DB_URL, DB_USER o DB_PASSWORD no están definidas");
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 🔑 IMPORTANTE
        config.setConnectionTimeout(30000);
        dataSource = new HikariDataSource(config);
    }

    public static DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo obtener una conexión a la base de datos", e);
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
