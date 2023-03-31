package org.example.db.statement;

import org.example.bot.service.UserDTO;
import java.sql.*;

public class StatementDB implements Insert, RecordChecker, Upsert {
    private final Connection connection;
    private final UserDTO userDTO;

    public StatementDB(Connection connection, UserDTO userDTO) {
        this.connection = connection;
        this.userDTO = userDTO;
    }
    @Override
    public void insert() {
        String query = "INSERT INTO users (id, first_name, last_name, username, language_code, registration_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1,  userDTO.getId());
            statement.setString(2, userDTO.getFirstName());
            statement.setString(3, userDTO.getLastName());
            statement.setString(4, userDTO.getUserName());
            statement.setString(5, userDTO.getLanguageCode());
            statement.setDate(6, Date.valueOf(userDTO.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void upsert() {
        String query = "UPDATE users SET first_name = ?, last_name = ?, username = ?, language_code = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, userDTO.getFirstName());
            statement.setString(2, userDTO.getLastName());
            statement.setString(3, userDTO.getUserName());
            statement.setString(4, userDTO.getLanguageCode());
            statement.setLong(5,  userDTO.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isRecorder() {
        String query = "SELECT id  FROM users WHERE id =  ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, userDTO.getId());
            try (ResultSet resultSet = statement.executeQuery()){
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
