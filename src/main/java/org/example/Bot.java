package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//
public class Bot  extends TelegramLongPollingBot {
    private final SendMessage msg=new SendMessage();
    private String stringAmount="";

    //test

    @Override
    public String getBotUsername() {
        return "akilatestBot";
    }

    @Override
    public String getBotToken() {
        return "7383338947:AAFgBjnUzbyQY1B0mN_w3ybIdlUpnKwfXTU";
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message txt;
        if (update.hasMessage() && update.getMessage().isCommand()){
            System.out.println(" Update method runing");
            txt =update.getMessage();
            msg.setText("Hello Select Your Country ");
            msg.setParseMode(ParseMode.MARKDOWN);
            msg.setChatId(String.valueOf(txt.getChatId()));
            msg.setReplyMarkup(createCountrySelectionInlineKeyboard());
            msg.setChatId(String.valueOf(update.getMessage().getChatId()));
//            createCountrySelectionReplyKeyboard(update.getMessage().getChatId());
//            System.out.println(msg.getChatId());
//            System.out.println(msg.getText());
//            System.out.println(msg.getReplyMarkup());
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }else if (update.hasCallbackQuery()){
            txt=update.getCallbackQuery().getMessage();
            msg.setChatId(String.valueOf(txt.getChatId()));
            msg.setParseMode(ParseMode.MARKDOWN);
            String data=update.getCallbackQuery().getData();

            switch (data) {
                case "usa":
                    msg.setText("Sorry USA You Are Not Eligible For This Service");
                    msg.setReplyMarkup(null);
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "other":
                    msg.setReplyMarkup(createNumberPadInlineKeyboard());
                    msg.setText("Enter Your Amount :");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    System.out.println("data :" + data);
                    stringAmount += data;

                    break;
                case "OK":
                    int value = Integer.parseInt(stringAmount);
                    if (value > 100) {
                        System.out.println("Sending Api request");
                        String response = null;
                        try {
                            response = sendApiRequest();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(response);
                        msg.setText("API Response: Click Here " + "https://www.google.com/");
                        msg.setReplyMarkup(null);
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        msg.setText("You entered amount is low :Your amount" + value);
                        msg.setReplyMarkup(createCountrySelectionInlineKeyboard());
                        try {
                            execute(msg);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    break;
            }

        }else if (update.hasMessage() && update.getMessage().hasText()) {
            msg.setReplyMarkup(createCountrySelectionInlineKeyboard());
            msg.setText("Please input number using given buttons");
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public String sendApiRequest() throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    private InlineKeyboardMarkup createNumberPadInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonList=new ArrayList<>();
        List<InlineKeyboardButton> buttons1 =new ArrayList<>();
        List<InlineKeyboardButton> buttons2 =new ArrayList<>();
        List<InlineKeyboardButton> buttons3 =new ArrayList<>();
        InlineKeyboardButton [] btnArray={
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton(),
                new InlineKeyboardButton()
        };

        for (int i=0;i<4;i++){
            btnArray[i].setText(String.valueOf(i+1));
            btnArray[i].setCallbackData(String.valueOf(i+1));
            buttons1.add(btnArray[i]);
        }

        for (int i=4;i<8;i++){
            btnArray[i].setText(String.valueOf(i+1));
            btnArray[i].setCallbackData(String.valueOf(i+1));
            buttons2.add(btnArray[i]);
        }
        btnArray[7].setText("9");
        btnArray[7].setCallbackData("9");
        buttons3.add(btnArray[7]);
        btnArray[8].setText("0");
        btnArray[8].setCallbackData("0");
        buttons3.add(btnArray[8]);
        btnArray[9].setText("OK");
        btnArray[9].setCallbackData("OK");
        buttons3.add(btnArray[9]);
        btnArray[10].setText("Cancel");
        btnArray[10].setCallbackData("Cancel");
        buttons3.add(btnArray[10]);

        buttonList.add(buttons1);
        buttonList.add(buttons2);
        buttonList.add(buttons3);
        inlineKeyboardMarkup.setKeyboard(buttonList);

        return inlineKeyboardMarkup;
    }
    private InlineKeyboardMarkup createCountrySelectionInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("USA");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("OTHER");

        inlineKeyboardButton1.setCallbackData("usa");
        inlineKeyboardButton2.setCallbackData("other");

        //      inlineKeyboardButton2.setUrl("https://www.youtube.com/");
        inlineKeyboardButtons.add(inlineKeyboardButton1);
        inlineKeyboardButtons.add(inlineKeyboardButton2);
        inlineButtons.add(inlineKeyboardButtons);
        inlineKeyboardMarkup.setKeyboard(inlineButtons);
        return inlineKeyboardMarkup;
    }
//    private void createCountrySelectionReplyKeyboard(long chatId){
//        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();
//        List<KeyboardRow>keyboardRowList=new ArrayList<>();
//        KeyboardRow keyboardRow=new KeyboardRow();
//
//        KeyboardButton button1 = new KeyboardButton();
//        button1.setText("USA");
//        KeyboardButton button2 = new KeyboardButton();
//        button1.setText("Other");
//        keyboardRow.add(button1);
//        keyboardRow.add(button2);
//        keyboardRowList.add(keyboardRow);
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//        msg.setChatId(String.valueOf(chatId));
//        msg.setReplyMarkup(replyKeyboardMarkup);
//        msg.setText("Hello Select Your Country ");
//        msg.setParseMode(ParseMode.HTML);
////        return msg;
//
//    }
}
