package de.adrian.ok.ddb.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private final MysqlDataSource dataSource;
    private static DatabaseManager manager;

    private DatabaseManager() {
        dataSource = new MysqlDataSource();
    }

    public void setConnectionInfo(String hostname, int port, String user, String password) {
        dataSource.setServerName(hostname);
        dataSource.setPort(port);
        dataSource.setUser(user);
        dataSource.setPassword(password);
    }

    public void setDatabase(String db) {
        dataSource.setDatabaseName(db);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DatabaseManager get() {
        return manager == null ? (manager = new DatabaseManager()) : manager;
    }

}
