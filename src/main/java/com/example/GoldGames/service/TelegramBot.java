package com.example.GoldGames.service;

import com.example.GoldGames.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final String mainUrl = "https://i.ibb.co/0FFJwBG/photo1675628353-1.jpg";
    final String managerUrl = "https://i.ibb.co/gjqd9Th/manager.jpg";
    final String guaranteesUrl = "https://ibb.co/FJyr0Y0";
    final String shopUrl = "https://i.ibb.co/rvm8nNr/photo1675628353.jpg";
    final String faqUrl = "https://i.ibb.co/KNbGKRZ/photo1675628353-2.jpg";

    public TelegramBot(BotConfig config) {

        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if ("/start".equals(messageText)) {
                sendStartMessage(chatId);
            } else {
                sendAnotherMessage(chatId);
            }
        }
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (callBackData.equals("MANAGER_BUTTON")) {                                        //кнопка менеджер
                EditMessageMedia editMessageMedia = new EditMessageMedia();
                EditMessageCaption editMessageCaption = new EditMessageCaption();

                editMessageCaption.setChatId(chatId);
                editMessageCaption.setCaption("Тут ты можешь задавать свой вопрос нам в личные сообщения и мы тебе обязательно поможем! @GoIdshopmanager");
                editMessageCaption.setMessageId((int) messageId);

                editMessageMedia.setChatId(chatId);
                editMessageMedia.setMedia(new InputMediaPhoto(managerUrl));
                editMessageMedia.setMessageId((int) messageId);

                execute(editMessageMedia);
                execute(editMessageCaption);

                inlineBackButton(editMessageMedia, editMessageCaption);
            }
            if (callBackData.equals("guarantees_BUTTON")) {                              //кнопка гарантии
                EditMessageMedia editMessageMedia = new EditMessageMedia();
                EditMessageCaption editMessageCaption = new EditMessageCaption();


                editMessageCaption.setChatId(chatId);
                editMessageCaption.setCaption("Максимально надёжно, без банов и скама");
                editMessageCaption.setMessageId((int) messageId);

                editMessageMedia.setChatId(chatId);
                editMessageMedia.setMedia(new InputMediaPhoto(guaranteesUrl));
                editMessageMedia.setMessageId((int) messageId);

                execute(editMessageMedia);
                execute(editMessageCaption);



                inlineLearnButton("https://telegra.ph/Garantii-02-06-2", editMessageCaption, editMessageMedia);
            }

            if (callBackData.equals("shop_BUTTON")) {

                EditMessageMedia editMessageMedia = new EditMessageMedia();
                EditMessageCaption editMessageCaption = new EditMessageCaption();


                editMessageCaption.setChatId(chatId);
                editMessageCaption.setCaption("Магазин");
                editMessageCaption.setMessageId((int) messageId);

                editMessageMedia.setChatId(chatId);
                editMessageMedia.setMedia(new InputMediaPhoto(shopUrl));
                editMessageMedia.setMessageId((int) messageId);

                execute(editMessageMedia);
                execute(editMessageCaption);

                shopInlineButtons(editMessageMedia, editMessageCaption);
            }
            if (callBackData.equals("questions_BUTTON")){
                EditMessageMedia editMessageMedia = new EditMessageMedia();
                EditMessageCaption editMessageCaption = new EditMessageCaption();


                editMessageCaption.setChatId(chatId);
                editMessageCaption.setCaption("Ответы на частозадаваемые вопросы");
                editMessageCaption.setMessageId((int) messageId);

                editMessageMedia.setChatId(chatId);
                editMessageMedia.setMedia(new InputMediaPhoto(faqUrl));
                editMessageMedia.setMessageId((int) messageId);

                execute(editMessageMedia);
                execute(editMessageCaption);



                inlineLearnButton("https://telegra.ph/FAQ-CHasto-zadavaemye-voprosy-02-06-2", editMessageCaption, editMessageMedia);

            }

            if (callBackData.equals("BACK_BUTTON")) {
                editSecondOnMain(chatId, messageId);
            }
        }

    }

    @SneakyThrows
    private void inlineLearnButton(String url, EditMessageCaption editMessageCaption, EditMessageMedia editMessageMedia) {

        InlineKeyboardMarkup learnInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> learnRowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> secondRowInLine = new ArrayList<>();

        var learn = new InlineKeyboardButton();
        learn.setText("Ознакомиться \uD83D\uDCDC");
        learn.setUrl(url);
        learn.setCallbackData("learn_BUTTON");

        var back = new InlineKeyboardButton();
        back.setText("Назад ⬅️");
        back.setCallbackData("BACK_BUTTON");


        firstRowInLine.add(learn);
        secondRowInLine.add(back);
        learnRowsInLine.add(firstRowInLine);
        learnRowsInLine.add(secondRowInLine);
        learnInline.setKeyboard(learnRowsInLine);

        editMessageCaption.setReplyMarkup(learnInline);
        editMessageMedia.setReplyMarkup(learnInline);
        execute(editMessageMedia);
        execute(editMessageCaption);
    }

    @SneakyThrows
    private void editSecondOnMain(long chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId((int) messageId);
        execute(deleteMessage);
        sendStartMessage(chatId);

    }

    @SneakyThrows
    private void sendStartMessage(long chatId) {

        String answer = "Привет! Рады тебя видеть в нашем боте! Надеемся, что здесь ты сможешь найти то, что искал!";
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setCaption(answer);
        photo.setPhoto(new InputFile(mainUrl));

        inlineMainButtons(photo);

    }

    @SneakyThrows
    private void inlineMainButtons(SendPhoto photo) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> mainRowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> secondRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> thirdRowInLine = new ArrayList<>();

        var shop = new InlineKeyboardButton();
        shop.setText("Магазин \uD83C\uDFAE");
        shop.setCallbackData("shop_BUTTON");

        var guarantees = new InlineKeyboardButton();
        guarantees.setText("Гарантии ✅");
        guarantees.setCallbackData("guarantees_BUTTON");

        var questions = new InlineKeyboardButton();
        questions.setText("FAQ ⁉️");
        questions.setCallbackData("questions_BUTTON");

        var manager = new InlineKeyboardButton();
        manager.setText("Менеджер \uD83E\uDDD1\u200D\uD83D\uDCBB");
        manager.setCallbackData("MANAGER_BUTTON");

        var reviews = new InlineKeyboardButton();
        reviews.setText("Отзывы  \uD83D\uDDE3");
        reviews.setUrl("https://t.me/Goldshopotzovik");
        reviews.setCallbackData("reviews_BUTTON");


        firstRowInLine.add(shop);        //первый ряд главное меню
        firstRowInLine.add(guarantees);  //первый ряд главное меню
        secondRowInLine.add(questions);   //второй ряд главное меню
        secondRowInLine.add(manager);   //второй ряд главное меню
        thirdRowInLine.add(reviews);    //третий ряд главное меню


        mainRowsInLine.add(firstRowInLine);
        mainRowsInLine.add(secondRowInLine);
        mainRowsInLine.add(thirdRowInLine);
        markupInline.setKeyboard(mainRowsInLine);

        photo.setReplyMarkup(markupInline);
        execute(photo);
    }

    @SneakyThrows
    private void shopInlineButtons(EditMessageMedia editMessageMedia, EditMessageCaption editMessageCaption) {

        InlineKeyboardMarkup shopMarkupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> shopRowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> secondRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> thirdRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> fourthRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> fifthRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> sixthRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> mlAndAnother = new ArrayList<>();
        List<InlineKeyboardButton> seventhRowInLine = new ArrayList<>();


        var fortnite = new InlineKeyboardButton();
        fortnite.setText("Fortnite");
        fortnite.setUrl("https://t.me/Fortnitesshop");
        fortnite.setCallbackData("FORTNITE_BUTTON");

        var brawlStars = new InlineKeyboardButton();
        brawlStars.setText("Brawl Stars");
        brawlStars.setUrl("https://t.me/BrawIstars_shop");
        brawlStars.setCallbackData("BRAWLSTARS_BUTTON");

        var wofB = new InlineKeyboardButton();
        wofB.setText(" World of Tanks Blitz");
        wofB.setUrl("https://t.me/Worldoftanksblitz_shop");
        wofB.setCallbackData("WOF_BUTTON");

        var pbgM = new InlineKeyboardButton();
        pbgM.setText("Pubg Mobile");
        pbgM.setUrl("https://t.me/PubgmobiIeshop");
        pbgM.setCallbackData("pbgM_BUTTON");

        var valorant = new InlineKeyboardButton();
        valorant.setText("Valorant");
        valorant.setUrl("https://t.me/VaIorantshop");
        valorant.setCallbackData("valorant_BUTTON");

        var so2 = new InlineKeyboardButton();
        so2.setText("Standoff 2");
        so2.setUrl("https://t.me/Standoff_sshop");
        so2.setCallbackData("so2_BUTTON");

        var fifaMobile = new InlineKeyboardButton();
        fifaMobile.setText("FIFA Mobile");
        fifaMobile.setUrl("https://t.me/FifamobiIeshop");
        fifaMobile.setCallbackData("fifaMobile_BUTTON");

        var genIm = new InlineKeyboardButton();
        genIm.setText("Genshin Impact");
        genIm.setUrl("https://t.me/Genshin_impactshop");
        genIm.setCallbackData("genIm_BUTTON");

        var coc = new InlineKeyboardButton();
        coc.setText("Clash of Clans");
        coc.setUrl("https://t.me/Clash_of_cIans_shop");
        coc.setCallbackData("coc_BUTTON");

        var cr = new InlineKeyboardButton();
        cr.setText("Clash Royale");
        cr.setUrl("https://t.me/Clash_royaIe_shop");
        cr.setCallbackData("cr_BUTTON");

        var asphalt9 = new InlineKeyboardButton();
        asphalt9.setText("Asphalt 9");
        asphalt9.setUrl("https://t.me/Asphalt_9_shop");
        asphalt9.setCallbackData("asphalt9_BUTTON");

        var roblox = new InlineKeyboardButton();
        roblox.setText("Roblox");
        roblox.setUrl("https://t.me/RobIoxshop");
        roblox.setCallbackData("roblox_BUTTON");

        var ml = new InlineKeyboardButton();
        ml.setText("Mobile Legends");
        ml.setUrl("https://t.me/Mobile_Iegends_shop");
        ml.setCallbackData("ml_button");

        var lol = new InlineKeyboardButton();
        lol.setText("League of Legends");
        lol.setUrl("https://t.me/League_of_Legends_shop");
        lol.setCallbackData("lol_button");

        var back = new InlineKeyboardButton();
        back.setText("Назад ⬅️");
        back.setCallbackData("BACK_BUTTON");

        firstRowInLine.add(fortnite);
        firstRowInLine.add(brawlStars);

        secondRowInLine.add(wofB);
        secondRowInLine.add(pbgM);

        thirdRowInLine.add(valorant);
        thirdRowInLine.add(so2);

        fourthRowInLine.add(fifaMobile);
        fourthRowInLine.add(genIm);

        fifthRowInLine.add(coc);
        fifthRowInLine.add(cr);

        sixthRowInLine.add(asphalt9);
        sixthRowInLine.add(roblox);

        mlAndAnother.add(ml);
        mlAndAnother.add(lol);

        seventhRowInLine.add(back);

        shopRowsInLine.add(firstRowInLine);
        shopRowsInLine.add(secondRowInLine);
        shopRowsInLine.add(thirdRowInLine);
        shopRowsInLine.add(fourthRowInLine);
        shopRowsInLine.add(fifthRowInLine);
        shopRowsInLine.add(sixthRowInLine);
        shopRowsInLine.add(mlAndAnother);
        shopRowsInLine.add(seventhRowInLine);

        shopMarkupInline.setKeyboard(shopRowsInLine);

        editMessageCaption.setReplyMarkup(shopMarkupInline);
        editMessageMedia.setReplyMarkup(shopMarkupInline);
        execute(editMessageMedia);
        execute(editMessageCaption);
    }

    @SneakyThrows
    private void inlineBackButton(EditMessageMedia editMessageMedia, EditMessageCaption editMessageCaption) {

        InlineKeyboardMarkup backMarkupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> backRowInLine = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInLine = new ArrayList<>();

        var back = new InlineKeyboardButton();
        back.setText("Назад ⬅️");
        back.setCallbackData("BACK_BUTTON");

        firstRowInLine.add(back);
        backRowInLine.add(firstRowInLine);
        backMarkupInline.setKeyboard(backRowInLine);

        editMessageCaption.setReplyMarkup(backMarkupInline);
        editMessageMedia.setReplyMarkup(backMarkupInline);
        execute(editMessageMedia);
        execute(editMessageCaption);
    }

    @SneakyThrows
    private void sendAnotherMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Неизвестная команда");
        execute(message);
    }

}