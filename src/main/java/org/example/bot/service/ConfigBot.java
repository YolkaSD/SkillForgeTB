package org.example.bot.service;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigBot {
    @Getter
    private final String botName;
    @Getter
    private final String botToken;

    public ConfigBot(){
        try (FileInputStream input = new FileInputStream("src/main/resources/application.properties")){
            Properties properties = new Properties();
            properties.load(input);
            this.botName = properties.getProperty("bot_name");
            this.botToken = properties.getProperty("bot_token");
        } catch (IOException e) {
            throw new RuntimeException("Error loading application properties", e);
        }
    }


}
