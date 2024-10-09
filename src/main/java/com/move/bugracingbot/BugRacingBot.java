package com.move.bugracingbot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class BugRacingBot extends TelegramLongPollingBot {
  InlineKeyboardButton li1 = createInlineKeyboardButton("Оплатить", "li1");
  InlineKeyboardButton USDT = createInlineKeyboardButton("USDT(TRC20)", "USDT");
  InlineKeyboardButton checkTransfer = createInlineKeyboardButton("Проверить перевод", "checkTransfer");
  InlineKeyboardButton back = createInlineKeyboardButton("Вернуться назад", "back");
  InlineKeyboardButton toMenu = createInlineKeyboardButton("Перейти в меню", "toMenu");

  private EditMessageText newTxt;
  private EditMessageReplyMarkup newKb;
  public static final Long myOwnId = someId;
  private String userName;
  public static final String HELP = """
          Если вы решили оплатить курсы через BugRacingBot, то вам необходимо выполнить следующие действия:
          1)Выбрать интересующий вас пак
          2)Перейти в меню оплаты, скопировать адрес кошелька или карты
          3)Перевести указанную сумму и подтвердить действие
          с помощью кнопки \"Проверить перевод\", после чего наши модераторы проверят перевод и вы получите обучение.
                    
          Напишите мне если остались вопросы, с радостью вам помогу @""";
  private static final String GREETING_MESSAGE = """
          Добро пожаловать в BugRacingBot!
          ⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⢲⣄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⣰⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⠄⢀⣴⣿⠟⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⣠⣾⡿⠋⢠⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⣸⣿⠋⠄⣼⡁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⢰⣿⠇⠄⠄⠘⢷⣄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠸⣿⠄⠄⠄⠄⠈⠻⣧⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠹⣧⡀⠄⠄⠄⠄⢸⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⠄⠉⠄⠄⠄⠠⠊⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠄⠄⣀⣠⣤⣤⣤⣄⢀⣀⢀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⣤⣶⣿⡿⠋⠉⠉⠉⠉⠉⠉⠙⠺⢬⡢⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠠⣻⡿⢿⣭⣒⣲⣶⣶⣶⣦⣤⣄⡀⣜⣵⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⡄⠄⠉⠛⠺⠿⠿⣭⣭⣭⣭⣿⣷⣿⣿⣿⢰⣂⣉⣒⣦⡀⠄⠄
          ⠄⠄⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢸⣿⣿⣿⢸⠟⠙⠻⣿⣵⡀⠄
          ⠄⠄⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢰⢸⣿⣿⣿⠈⠄⠄⠄⢸⣿⡇⠄
          ⠄⠄⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢸⢸⣿⣿⣿⠄⠄⠄⠄⢠⣽⡇⠄
          ⠄⠄⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⣿⢸⣿⣿⣿⢠⣀⠄⣀⣾⣿⠃⠄
          ⠄⠄⣷⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⣿⢸⣿⣿⣿⣤⣬⣭⣵⡿⠃⠄⠄
          ⠄⠄⣿⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⣿⢸⣿⣿⣿⠙⠛⠛⠉⠄⠄⠄⠄
          ⠄⠄⣿⣄⠄⠄⠄⠄⠄⠄⠄⠄⢸⣿⢸⣿⣿⣿⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠘⠿⣶⣤⣄⠄⠄⠄⠄⣀⣼⣿⣾⣿⣿⠟⠄⠄⠄⠄⠄⠄⠄⠄
          ⠄⠄⠄⠄⠄⠉⠙⠛⠛⠿⠿⠿⠟⠛⠛⠉⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄
          Сервис для продажи обучающих материалов по программированию.
          Выберите товар который вас интересует""";
  private static final String SECOND_MENU = """
          После оплаты вам придет ссылка на яндекс диск.
          Если возникли вопросы - /help          
          """;
  private static final String PAYMENT_CHOOSING = """
          Цена: 199руб(2$)
          Список реквизитов:
          USDT(TRC20): somePayment
          Если возникли вопросы - /help
          Выберите удобный для вас способ оплаты ↴
          """;
  public static final String PAYMENT_WINDOW = """
          Переведите монету на выбранные вами реквизиты и нажмите кнопку \"Проверить перевод\", после чего модератор скинет вам учебные материалы.
          """;
  public static final String THANKS = """
          Благодарим за покупку!
              
             ╭◜◝ ͡ ◜◝╮  ㅤ ╭◜◝ ͡ ◜◝╮.\s
             (             )  ☆   (      )☆ 
             ╰◟◞ ͜ ◟◞╭◜◝ ͡ ◜◝╮ ͜ ◟◞╯\s
             . ☆  ㅤㅤ(                )☆ 
              　      ╰◟◞ ͜ ◟◞╯
                           ☆             \s
                                   
                          (\\_(\\      /)_/)
                          (      )    (      )
                        ૮/ʚɞ  |ა ૮|  ʚɞ\\ა
                         ( ◌    |     |     ◌ )
             
          Наши модераторы обработают ваш заказ в ближайшее время =)
          """;

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasCallbackQuery()) {
      var callbackQuery = update.getCallbackQuery();
      if (callbackQuery != null) {
        processCallback(callbackQuery);
        return;
      }
    }
    Message message = update.getMessage();
    User user = message.getFrom();
    Long id = user.getId();
    userName = "@" + user.getUserName();
    System.out.println(user.getFirstName() + " " + message.getText());
    var messageText = message.getText();
    if (message.isCommand()) {
      switch (messageText) {
        case "/start" -> sendMenu(id, GREETING_MESSAGE, getKeyboard(getKeyboardRow(toMenu)));
        case "/help" -> sendMenu(id, HELP, getKeyboard(getKeyboardRow(toMenu)));
      }
    }
  }

  private void buttonTap(Long id, String queryId, String data, int msgId) {
    newTxt = EditMessageText.builder()
            .chatId(id.toString())
            .messageId(msgId).text("").build();
    newKb = EditMessageReplyMarkup.builder()
            .chatId(id.toString()).messageId(msgId).build();

    switch (data) {
      case "toMenu" -> setRepresentationOfWindows(SECOND_MENU, getKeyboard(getKeyboardRow(li1, back)));
      case "back" -> setRepresentationOfWindows(GREETING_MESSAGE, getKeyboard(getKeyboardRow(toMenu)));
      case "li1" -> setRepresentationOfWindows(PAYMENT_CHOOSING, getKeyboard(getKeyboardRow(USDT, toMenu)));
      case "USDT" -> setRepresentationOfWindows(PAYMENT_WINDOW, getKeyboard(getKeyboardRow(checkTransfer, toMenu)));
      case "checkTransfer" -> {
        purchaseNotification(myOwnId, userName);
        setRepresentationOfWindows(THANKS, getKeyboard(getKeyboardRow(toMenu)));
      }
    }
    AnswerCallbackQuery close = AnswerCallbackQuery.builder()
            .text("")
            .callbackQueryId(queryId).build();
    try {
      execute(close);
      execute(newTxt);
      execute(newKb);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private List<List<InlineKeyboardButton>> getUsdt() {
    return List.of(List.of(USDT), List.of(toMenu));
  }

  private List<List<InlineKeyboardButton>> getKeyboardRow(InlineKeyboardButton button1, InlineKeyboardButton button2) {
    return List.of(List.of(button1), List.of(button2));
  }

  private List<List<InlineKeyboardButton>> getKeyboardRow(InlineKeyboardButton button) {
    return List.of(List.of(button));
  }


  private void processCallback(CallbackQuery callbackQuery) {
    var queryId = callbackQuery.getId();
    var data = callbackQuery.getData();
    var messageId = callbackQuery.getMessage().getMessageId();
    var id = callbackQuery.getFrom().getId();
    buttonTap(id, queryId, data, messageId);
  }

  private InlineKeyboardMarkup getKeyboard(List<List<InlineKeyboardButton>> keyboardMarkup) {
    return new InlineKeyboardMarkup(keyboardMarkup);
  }

  private void setRepresentationOfWindows(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
    newTxt.setText(text);
    newKb.setReplyMarkup(inlineKeyboardMarkup);
  }

  public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
    SendMessage sm = SendMessage.builder().chatId(who.toString())
            .parseMode("HTML").text(txt)
            .replyMarkup(kb).build();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  private InlineKeyboardButton createInlineKeyboardButton(String text, String callbackName) {
    return InlineKeyboardButton.builder()
            .text(text).callbackData(callbackName)
            .build();
  }


  public void purchaseNotification(Long id, String notification) {
    SendMessage sm = SendMessage.builder()
            .chatId(id.toString())
            .text(notification).build();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getBotUsername() {
    return "bug_racing_bot";
  }

  @Override
  public String getBotToken() {
    return "someToken";
  }
}
