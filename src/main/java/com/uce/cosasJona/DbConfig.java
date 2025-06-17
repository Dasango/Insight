package com.uce.cosasJona;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConfig {
    //SINGLETON

    private static DbConfig instance;


    private HikariDataSource dataSource;


    private static final String URL = "jdbc:postgresql://your-database-host.com:5432/your_database_name";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";


    private DbConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        //config.setDriverClassName("org.postgresql.Driver");
        //config.setMaximumPoolSize(10);  // Tamaño máximo
        config.setConnectionTimeout(30000);  // Timeout
        dataSource = new HikariDataSource(config);
    }


    public static DbConfig getInstance() {
        if (instance == null) {
            instance = new DbConfig();
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
