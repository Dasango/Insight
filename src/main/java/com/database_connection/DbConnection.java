package com.database_connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection  {

    //SINGLETON

    private static DbConnection instance;


    private HikariDataSource dataSource;


    private static final String URL = "jdbc:postgresql://your-database-host.com:5432/your_database_name";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";


    private DbConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        //config.setDriverClassName("org.postgresql.Driver");
        //config.setMaximumPoolSize(10);  // Tamaño máximo
        config.setConnectionTimeout(30000);  // Timeout
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