package org.example.db.connection;
import lombok.Getter;
import org.example.db.service.ConnectionConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static ConnectionDB connectionDB;
    @Getter
    private final Connection connection;
    ConnectionConfig config = new ConnectionConfig();

    private ConnectionDB() throws SQLException{
        this.connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    }

    public static ConnectionDB create() throws SQLException{
        if (connectionDB == null) {
            connectionDB = new ConnectionDB();
        }
        return connectionDB;
    }
}
