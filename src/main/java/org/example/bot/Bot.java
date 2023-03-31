package org.example.bot;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.db.connection.ConnectionDB;
import org.example.db.statement.StatementDB;
import org.example.bot.service.ConfigBot;
import org.example.bot.service.UserDTO;
import java.sql.SQLException;

public class Bot {
    final private TelegramBot telegramBot;
    public Bot(ConfigBot configBot) {
        this.telegramBot = new TelegramBot(configBot.getBotToken());
        runListener();
    }

private void runListener() {
    this.telegramBot.setUpdatesListener(updates -> {
        updates.forEach(this::toStart);
        updates.forEach(this::listenCallBack);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    });
}

private void toStart(Update update){
    if (update.message() != null) {
        Message message = update.message();
        telegramBot.execute(new SendMessage(message.chat().id(), "").replyMarkup(createMenuKeyboard()));

        if (message.text().equals("/start")) {
            UserDTO userDTO = createUserDTO(message);
            try {
                StatementDB statement = new StatementDB(ConnectionDB.create().getConnection(), userDTO);
                if (statement.isRecorder()) {
                    telegramBot.execute(new SendMessage(userDTO.getId(), "С возвращением, " + userDTO.getUserName()));
                    statement.upsert();
                } else {
                    telegramBot.execute(new SendMessage(userDTO.getId(), "Добро пожаловать, " + userDTO.getUserName()));
                    statement.insert();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (message.text().equals("\uD83D\uDED2 Товары")) {
            telegramBot.execute(new SendMessage(message.chat().id(), "\uD83D\uDED2 Все доступные товары: ")
                    .replyMarkup(createToListProduct()));
        }

        if (message.text().equals("\uD83E\uDEAA Профиль")) {
            telegramBot.execute(new SendMessage(message.chat().id(),
                    "\uD83E\uDDD1\u200D\uD83D\uDCBBПользователь: " + message.chat().username() + "\n"
                            + "\uD83C\uDD94ID: " + message.from().id()
                            + "\n" + "➖➖➖➖➖➖➖➖➖➖"
                            + "\n" + "\uD83D\uDED2Количество покупок: !TODO!."
                            + "\n" +  "\uD83D\uDCB6Общая сумма: !TODO!."
                            + "\n" + "➖➖➖➖➖➖➖➖➖➖"
                            + "\n" + "\uD83D\uDCDAПоследние 10 покупок: !TODO!")
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("\uD83D\uDEAB Закрыть").callbackData("close_message"))));

        }
    }

}
private void listenCallBack(Update update) {
        if (update.callbackQuery() !=  null) {
            String string = update.callbackQuery().data();
            if (string.equals("close_message")) {
                telegramBot.execute(new DeleteMessage(update.callbackQuery().from().id(), update.callbackQuery().message().messageId()));
            }
        }
    }

    private ReplyKeyboardMarkup createMenuKeyboard(){
        return new ReplyKeyboardMarkup(
                new String[]{"\uD83D\uDED2 Товары"},
                new String[]{"\uD83E\uDEAA Профиль", "\uD83E\uDD21 О нас"});
    }

    private InlineKeyboardMarkup createToListProduct() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("Товар 1").callbackData("1"))
                .addRow(new InlineKeyboardButton("Товар 2").callbackData("2"))
                .addRow(new InlineKeyboardButton("Товар 3").callbackData("3"))
                .addRow(new InlineKeyboardButton("Товар 4").callbackData("4"))
                .addRow(new InlineKeyboardButton("Товар 5").callbackData("5"))
                .addRow(new InlineKeyboardButton("\uD83D\uDEAB Закрыть").callbackData("close_message"));
    }

    private UserDTO createUserDTO(Message message) {
        Long id = message.from().id();
        String firstName = message.from().firstName();
        String lastName = message.from().lastName();
        String username = message.from().username();
        String languageCode = message.from().languageCode();
        long dateInSeconds = message.date();

        return new UserDTO.Builder()
                .withId(id)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withUserName(username)
                .withLanguageCode(languageCode)
                .withDate(dateInSeconds)
                .builder();
    }
}
