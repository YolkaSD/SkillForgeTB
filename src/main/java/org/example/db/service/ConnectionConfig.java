package org.example.db.service;
import lombok.Getter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConnectionConfig {
    @Getter
    private final String url;
    @Getter
    private final String user;
    @Getter
    private final String password;

    public ConnectionConfig() {
        try (FileInputStream input = new FileInputStream("SkillForgeBot/src/main/resources/application.properties")){
            Properties properties = new Properties();
            properties.load(input);
            this.url = properties.getProperty("url");
            this.user = properties.getProperty("user");
            this.password = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading application properties", e);
        }
    }
}
