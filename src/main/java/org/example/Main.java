package org.example;
import org.example.bot.Bot;
import org.example.bot.service.ConfigBot;

public class Main {
    public static void main(String[] args) {
        ConfigBot configBot = new ConfigBot();
        new Bot(configBot);
    }
}