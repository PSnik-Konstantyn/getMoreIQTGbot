package com.iq;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.parseInt;


public class IQGameController {
    public static void main(String[] args) {

        String TOKEN = "";

        try {
            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            TOKEN = prop.getProperty("TOKEN");

        } catch (IOException e) {
            e.printStackTrace();
        }


        TelegramBot bot = new TelegramBot(TOKEN);

        Map<String, PlayerInfo> players = new HashMap<>();
        Map<String, Integer> answers = new HashMap<>();


        bot.setUpdatesListener(updates -> {

            updates.forEach(update -> {
                System.out.println(update);
                if (!(update.message() == null) && !(update.message().text() == null)) {

                    Long playerId = update.message().from().id();
                    Long chatId = update.message().chat().id();
                    int newAttempts = 3;
                    String playerName = update.message().from().firstName();
                    String playerMessageText = update.message().text();
                    String uniquePlayerID = ((playerId.toString()) + "_" + (chatId.toString()));
                    String sourceDate = "2022-09-25";
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Random addingIQ = new Random();


                    if (!players.containsKey(uniquePlayerID)) {
                        PlayerInfo newPlayer = new PlayerInfo();
                        Random startIq = new Random();
                        newPlayer.setPlayerId(playerId);
                        newPlayer.setChatId(chatId);
                        newPlayer.setUserFirstName(playerName);
                        newPlayer.setCanAnswer(false);
                        newPlayer.setAllAnswers(0);
                        newPlayer.setCorrectAnswers(0);
                        newPlayer.setAttemptCounter(newAttempts);
                        newPlayer.setIqCounter(startIq.nextInt(30));
                        try {
                            Date myDate = format.parse(sourceDate);
                            newPlayer.setLastTimePlayed(myDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        players.put(uniquePlayerID, newPlayer);
                        answers.put(uniquePlayerID, null);
                        bot.execute(new SendMessage(chatId, "????????????, " + playerName + "! \n"
                                + "?? ??????, ???? ?????????? ???????? IQ! \n"
                                + "?????????? ???????? IQ: " + newPlayer.getIqCounter() + " \n"
                                + "?????? ???? ???????????? ???????? ??????????????????! \n"
                                + "?????? ???? ???????? ?????????? ?????????????? /get_more_iq \n"
                                + "?????? ???????????????? ???????????? ???????? ???????????? ?????????? /help \n"
                                + "??????, ???????? ???? ?????????????????? ??????????????????, ???? ???????? IQ ?????????????????????? \n"
                                + "???????? ??????????????????????, ???? ???? ???????????????? ?????? ???????????????? \n"
                                + "???????????? ??????!"));
                    } else if (playerMessageText.equals("/rules_iq_game") || playerMessageText.equals("/rules_iq_game@Game_get_IQ_bot")) {
                        bot.execute(new SendMessage(chatId,
                                "?????????? ?????????????? /get_more_iq ???? ?????????????? ???????????????? \n" +
                                        "???????????? ?????????????????? ?????????????????? (???????? ??????????) \n" +
                                        "???? ?????????????? ???????????????????? ?? ?????????????? IQ! \n" +
                                        "???????? ??????????????????????, ???? ???? ???????????????? ?????? ????????????????!\n"));

                    } else if (playerMessageText.equals("/help") || playerMessageText.equals("/help@Game_get_IQ_bot")) {
                        bot.execute(new SendMessage(chatId,
                                "/get_more_iq - ?????????????????? ???????? IQ\n" +
                                        "/rules_iq_game - ?????????????? ?????? \n" +
                                        "/my_iq - ???????? ?????????????????? IQ\n" +
                                        "/my_accuracy - ???????????????? ???????????????????? ???????????????????? \n" +
                                        "/list_all_players - ???????????? ???????? ??????????????\n " +
                                        "/top10_smartest - ?????? 10 ?????????????????????????? \n " +
                                        "/top10_correct_answers - ?????? 10 ???? ???????????????????? ????????????????????"));
                    } else if (playerMessageText.equals("/my_accuracy") || playerMessageText.equals("/my_accuracy@Game_get_IQ_bot")) {

                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        int allAnswers = currentPlayer.getAllAnswered();
                        int correctAnswers = currentPlayer.getCorrectAnswers();
                        String accuracyPercent = String.valueOf(((correctAnswers * 100) / allAnswers));
                        bot.execute(new SendMessage(chatId,
                                playerName + " ???????? ???????????????????????? ???????????????????? = " + accuracyPercent + "%"));

                    } else if (playerMessageText.equals("/top10_correct_answers") || playerMessageText.equals("/top10_correct_answers@Game_get_IQ_bot")) {

                        Map<Integer, String> listPlayersMap = new HashMap<>();
                        players.forEach((id, playerInfo) -> {
                            if (Objects.equals(playerInfo.getChatId(), chatId)) {
                                listPlayersMap.put(playerInfo.getCorrectAnswers(), playerInfo.getUserFirstName());
                            }
                        });

                        ArrayList<Integer> CorrectAnswersValues = new ArrayList<>(listPlayersMap.keySet());
                        ArrayList<Integer> repeatableValues = new ArrayList<>();

                        for (int i = 1; i < CorrectAnswersValues.size(); i++) {
                            int compElement = CorrectAnswersValues.get(i);
                            for (int j = 0; j < CorrectAnswersValues.size(); j++) {
                                int secondCompElement = CorrectAnswersValues.size();
                                if (compElement == secondCompElement) {
                                    CorrectAnswersValues.remove(j);
                                    CorrectAnswersValues.set(j, secondCompElement + 1);
                                    repeatableValues.add(secondCompElement + 1);
                                    i = 0;
                                    j = 0;
                                }
                            }
                        }

                        Collections.sort(CorrectAnswersValues);
                        Collections.reverse(CorrectAnswersValues);

                        if (CorrectAnswersValues.size() >= 10) {
                            CorrectAnswersValues.subList(0, 9);
                            String finalList = "???????????? ??????????????????????????: \n";
                            String firstStringValue = " ????????'????????(????) ";
                            String secondStringValues = " ?????????????????? \n";

                            finalList = finalList + formListOfPlayers(CorrectAnswersValues, repeatableValues, listPlayersMap, firstStringValue, secondStringValues);

                            bot.execute(new SendMessage(chatId,
                                    finalList
                            ));
                        } else {
                            bot.execute(new SendMessage(chatId,
                                    playerName + " ?????? ?????????????? ?????????????? 10 ??????????????, ?? ?????????? ?????? ???????????? ???????? ????????????????"
                            ));
                        }
                    } else if (playerMessageText.equals("/list_all_players") || playerMessageText.equals("/list_all_players@Game_get_IQ_bot")) {

                        Map<Integer, String> listPlayersMap = new HashMap<>();
                        players.forEach((id, playerInfo) -> {
                            if (Objects.equals(playerInfo.getChatId(), chatId)) {
                                listPlayersMap.put(playerInfo.getIqCounter(), playerInfo.getUserFirstName());
                            }
                        });
                        ArrayList<Integer> IQValues = new ArrayList<>(listPlayersMap.keySet());
                        ArrayList<Integer> repeatableValues = new ArrayList<>();

                        for (int i = 1; i < IQValues.size(); i++) {
                            int compElement = IQValues.get(i);
                            for (int j = 0; j < IQValues.size(); j++) {
                                int secondCompElement = IQValues.size();
                                if (compElement == secondCompElement) {
                                    IQValues.remove(j);
                                    IQValues.set(j, secondCompElement + 1);
                                    repeatableValues.add(secondCompElement + 1);
                                    i = 0;
                                    j = 0;
                                }
                            }
                        }


                        Collections.sort(IQValues);
                        Collections.reverse(IQValues);

                        String finalList = "???????????? ?????????????? ????????: \n";
                        String firstStringValue = " = ";
                        String secondStringValues = " IQ \n";

                        finalList = finalList + formListOfPlayers(IQValues, repeatableValues, listPlayersMap, firstStringValue, secondStringValues);

                        bot.execute(new SendMessage(chatId,
                                finalList
                        ));

                    } else if (playerMessageText.equals("/top10_smartest") || playerMessageText.equals("/top10_smartest@Game_get_IQ_bot")) {

                        Map<Integer, String> listPlayersMap = new HashMap<>();
                        players.forEach((id, playerInfo) -> {
                            if (Objects.equals(playerInfo.getChatId(), chatId)) {
                                listPlayersMap.put(playerInfo.getIqCounter(), playerInfo.getUserFirstName());
                            }
                        });

                        ArrayList<Integer> IQValues = new ArrayList<>(listPlayersMap.keySet());
                        ArrayList<Integer> repeatableValues = new ArrayList<>();

                        for (int i = 1; i < IQValues.size(); i++) {
                            int compElement = IQValues.get(i);
                            for (int j = 0; j < IQValues.size(); j++) {
                                int secondCompElement = IQValues.size();
                                if (compElement == secondCompElement) {
                                    IQValues.remove(j);
                                    IQValues.set(j, secondCompElement + 1);
                                    repeatableValues.add(secondCompElement + 1);
                                    i = 0;
                                    j = 0;
                                }
                            }
                        }
                        Collections.sort(IQValues);
                        Collections.reverse(IQValues);
                        if (IQValues.size() >= 10) {
                            IQValues.subList(0, 9);
                            String finalList = "???????????? ??????????????????????????: \n";
                            String firstStringValue = " = ";
                            String secondStringValues = " IQ \n";

                            finalList = finalList + formListOfPlayers(IQValues, repeatableValues, listPlayersMap, firstStringValue, secondStringValues);

                            bot.execute(new SendMessage(chatId,
                                    finalList
                            ));
                        } else {
                            bot.execute(new SendMessage(chatId,
                                    playerName + " ?????? ?????????????? ?????????????? 10 ??????????????, ?? ?????????? ?????? ???????????? ???????? ????????????????"
                            ));
                        }
                    } else if (playerMessageText.equals("/my_iq") || playerMessageText.equals("/my_iq@Game_get_IQ_bot")) {
                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        bot.execute(new SendMessage(chatId,
                                "??????????," + playerName + " ,???????? IQ: " + currentPlayer.getIqCounter() + " \n"
                        ));
                    } else if (playerMessageText.equals("/get_more_iq") || playerMessageText.equals("/get_more_iq@Game_get_IQ_bot")) {
                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        Long currentPlayerId = currentPlayer.getPlayerId();
                        Date lastTimePlusOneDay = DateUtil.addDays(currentPlayer.getLastTimePlayed(), 1);
                        Date currentDate = new Date();

                        if (lastTimePlusOneDay.getTime() < currentDate.getTime()) {

                            PlayerInfo updatedPlayer = new PlayerInfo();
                            updatedPlayer.setPlayerId(currentPlayer.getPlayerId());
                            updatedPlayer.setChatId(currentPlayer.getChatId());
                            updatedPlayer.setIqCounter(currentPlayer.getIqCounter());
                            updatedPlayer.setUserFirstName(playerName);
                            updatedPlayer.setAttemptCounter(newAttempts);
                            updatedPlayer.setAllAnswers(currentPlayer.getAllAnswered());
                            updatedPlayer.setCorrectAnswers(currentPlayer.getCorrectAnswers());
                            updatedPlayer.setCanAnswer(true);
                            updatedPlayer.setLastTimePlayed(currentDate);
                            players.replace(uniquePlayerID, currentPlayer, updatedPlayer);

                        }
                        if (currentPlayer.getAttemptCounter() > 0) {

                            int firstNumber = addingIQ.nextInt(15);
                            int secondNumber = addingIQ.nextInt(15);
                            int moreRandom = addingIQ.nextInt(15);

                            if (moreRandom < 5 && Objects.equals(currentPlayerId, playerId)) {
                                bot.execute(new SendMessage(chatId,
                                        playerName + "! ?????????????? ?????????????? " + firstNumber + " + " + secondNumber + " ="
                                ));
                                answers.replace(uniquePlayerID, null, (firstNumber + secondNumber));
                            } else if (5 <= moreRandom && moreRandom <= 10 && Objects.equals(currentPlayerId, playerId)) {
                                bot.execute(new SendMessage(chatId,
                                        playerName + "! ?????????????? ?????????????? " + firstNumber + " * " + secondNumber + " ="
                                ));
                                answers.replace(uniquePlayerID, null, (firstNumber * secondNumber));

                            } else if (moreRandom > 10 && Objects.equals(currentPlayerId, playerId)) {
                                bot.execute(new SendMessage(chatId,
                                        playerName + "! ?????????????? ?????????????? " + firstNumber + " - " + secondNumber + " ="
                                ));
                                answers.replace(uniquePlayerID, null, (firstNumber - secondNumber));
                            }
                            PlayerInfo updatedPlayer = new PlayerInfo();
                            updatedPlayer.setPlayerId(currentPlayer.getPlayerId());
                            updatedPlayer.setChatId(currentPlayer.getChatId());
                            updatedPlayer.setIqCounter(currentPlayer.getIqCounter());
                            updatedPlayer.setUserFirstName(playerName);
                            updatedPlayer.setAttemptCounter(currentPlayer.getAttemptCounter());
                            updatedPlayer.setAllAnswers(currentPlayer.getAllAnswered());
                            updatedPlayer.setCorrectAnswers(currentPlayer.getCorrectAnswers());
                            updatedPlayer.setCanAnswer(true);
                            updatedPlayer.setLastTimePlayed(currentDate);
                            players.replace(uniquePlayerID, currentPlayer, updatedPlayer);

                        } else {
                            bot.execute(new SendMessage(chatId,
                                    playerName + ", ???? ???????????????? ?????? ????????. ???????????? ??????????????????!"));
                        }

                    } else if (
                            players.get(uniquePlayerID).isCanAnswer()
                                    && answers.containsKey(uniquePlayerID)
                                    && answers.get(uniquePlayerID) != null
                                    && (playerMessageText.contains("0")
                                    || playerMessageText.contains("1")
                                    || playerMessageText.contains("2")
                                    || playerMessageText.contains("3")
                                    || playerMessageText.contains("4")
                                    || playerMessageText.contains("5")
                                    || playerMessageText.contains("6")
                                    || playerMessageText.contains("7")
                                    || playerMessageText.contains("8")
                                    || playerMessageText.contains("9")
                                    || playerMessageText.contains("10"))
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("!")
                                    && !playerMessageText.contains("+")
                                    && !playerMessageText.contains("*")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains(")")
                                    && !playerMessageText.contains("?")
                                    && !playerMessageText.contains("f")
                                    && !playerMessageText.contains("q")
                                    && !playerMessageText.contains("w")
                                    && !playerMessageText.contains("k")
                                    && !playerMessageText.contains("e")
                                    && !playerMessageText.contains("r")
                                    && !playerMessageText.contains("t")
                                    && !playerMessageText.contains("a")
                                    && !playerMessageText.contains("(")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && !playerMessageText.contains("??")
                                    && playerMessageText.length() <= 5
                    ) {
                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        int moreIQ;
                        int plusIQ = addingIQ.nextInt(10);
                        int minusIQ = addingIQ.nextInt(6);
                        int ifAnswerIsCorrect = 0;
                        int rightAnswer = answers.get(uniquePlayerID);
                        if (parseInt(playerMessageText) == rightAnswer) {
                            moreIQ = plusIQ;
                            bot.execute(new SendMessage(chatId, "??????????, " + playerName + "! ?????????????????? ??????????????????! \n"
                                    + "???????? IQ ???????????????????? ???? " + moreIQ + ". ?????????? ???????? IQ ??????????????????: " + (currentPlayer.getIqCounter() + moreIQ)));
                            answers.replace(uniquePlayerID, rightAnswer, null);
                            ifAnswerIsCorrect++;
                        } else {
                            moreIQ = -minusIQ;
                            bot.execute(new SendMessage(chatId, "???????????? ????????????, " + playerName + "! ?????????????????? ??????????????????????! \n"
                                    + "???????? IQ ???????????????????? ???? " + minusIQ + ". ?????????? ???????? IQ ??????????????????: " + (currentPlayer.getIqCounter() + moreIQ)));
                            answers.replace(uniquePlayerID, rightAnswer, null);
                        }
                        PlayerInfo updatedPlayer = new PlayerInfo();
                        updatedPlayer.setPlayerId(currentPlayer.getPlayerId());
                        updatedPlayer.setChatId(currentPlayer.getChatId());
                        updatedPlayer.setIqCounter((currentPlayer.getIqCounter() + moreIQ));
                        updatedPlayer.setUserFirstName(playerName);
                        updatedPlayer.setCanAnswer(false);
                        updatedPlayer.setAllAnswers(currentPlayer.getAllAnswered() + 1);
                        updatedPlayer.setCorrectAnswers((currentPlayer.getCorrectAnswers() + ifAnswerIsCorrect));
                        updatedPlayer.setAttemptCounter((currentPlayer.getAttemptCounter() - 1));
                        updatedPlayer.setLastTimePlayed(currentPlayer.getLastTimePlayed());
                        players.replace(uniquePlayerID, currentPlayer, updatedPlayer);
                    }
                }
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static String formListOfPlayers(ArrayList<Integer> correctAnswersValues, ArrayList<Integer> repeatableValues, Map<Integer, String> listPlayersMap, String firstStringValue, String secondStringValues) {
        StringBuilder finalList = new StringBuilder();
        for (int i = 1; i <= correctAnswersValues.size(); i++) {
            String newRow;
            if (repeatableValues.contains(correctAnswersValues.get((i - 1)))) {
                newRow = i + ".  " + listPlayersMap.get(correctAnswersValues.get((i - 1))) + firstStringValue + ((correctAnswersValues.get((i - 1))) - 1) + secondStringValues;
            } else {
                newRow = i + ".  " + listPlayersMap.get(correctAnswersValues.get((i - 1))) + firstStringValue + correctAnswersValues.get((i - 1)) + secondStringValues;
            }
            finalList.append(newRow);
        }
        return finalList.toString();
    }

}