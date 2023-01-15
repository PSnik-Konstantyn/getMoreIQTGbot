package com.iq;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        PlayerInfo Kostya = new PlayerInfo();
        Kostya.setPlayerId(2L);
        Kostya.setChatId(-743367483L);
        Kostya.setUserFirstName("Kostya");
        Kostya.setCanAnswer(false);
        Kostya.setAllAnswers(0);
        Kostya.setCorrectAnswers(8);
        Kostya.setAttemptCounter(3);
        Kostya.setIqCounter(10);
        players.put("1", Kostya);

        PlayerInfo Ivanna = new PlayerInfo();
        Ivanna.setPlayerId(83L);
        Ivanna.setChatId(-743367483L);
        Ivanna.setUserFirstName("Ivanna");
        Ivanna.setCanAnswer(false);
        Ivanna.setAllAnswers(0);
        Ivanna.setCorrectAnswers(8);
        Ivanna.setAttemptCounter(3);
        Ivanna.setIqCounter(20);
        players.put("2", Ivanna);

        PlayerInfo Misha = new PlayerInfo();
        Misha.setPlayerId(9L);
        Misha.setChatId(-743367483L);
        Misha.setUserFirstName("Misha");
        Misha.setCanAnswer(false);
        Misha.setAllAnswers(0);
        Misha.setCorrectAnswers(7);
        Misha.setAttemptCounter(3);
        Misha.setIqCounter(30);
        players.put("3", Misha);

        PlayerInfo Igor = new PlayerInfo();
        Igor.setPlayerId(7L);
        Igor.setChatId(-743367483L);
        Igor.setUserFirstName("Igor");
        Igor.setCanAnswer(false);
        Igor.setAllAnswers(0);
        Igor.setCorrectAnswers(6);
        Igor.setAttemptCounter(3);
        Igor.setIqCounter(50);
        players.put("4", Igor);

        PlayerInfo Artem = new PlayerInfo();
        Artem.setPlayerId(12L);
        Artem.setChatId(-743367483L);
        Artem.setUserFirstName("Artem");
        Artem.setCanAnswer(false);
        Artem.setAllAnswers(0);
        Artem.setCorrectAnswers(5);
        Artem.setAttemptCounter(3);
        Artem.setIqCounter(50);
        players.put("5", Artem);

        PlayerInfo Den = new PlayerInfo();
        Den.setPlayerId(14L);
        Den.setChatId(-743367483L);
        Den.setUserFirstName("Den");
        Den.setCanAnswer(false);
        Den.setAllAnswers(0);
        Den.setCorrectAnswers(4);
        Den.setAttemptCounter(3);
        Den.setIqCounter(55);
        players.put("6", Den);

        PlayerInfo Zelenskiy = new PlayerInfo();
        Zelenskiy.setPlayerId(15L);
        Zelenskiy.setChatId(-743367483L);
        Zelenskiy.setUserFirstName("Zelenskiy");
        Zelenskiy.setCanAnswer(false);
        Zelenskiy.setAllAnswers(0);
        Zelenskiy.setCorrectAnswers(3);
        Zelenskiy.setAttemptCounter(3);
        Zelenskiy.setIqCounter(70);
        players.put("7", Zelenskiy);

        PlayerInfo Poroh = new PlayerInfo();
        Poroh.setPlayerId(115L);
        Poroh.setChatId(-743367483L);
        Poroh.setUserFirstName("Poroh");
        Poroh.setCanAnswer(false);
        Poroh.setAllAnswers(0);
        Poroh.setCorrectAnswers(3);
        Poroh.setAttemptCounter(3);
        Poroh.setIqCounter(7);
        players.put("8", Poroh);

        PlayerInfo Jesus = new PlayerInfo();
        Jesus.setPlayerId(123L);
        Jesus.setChatId(-743367483L);
        Jesus.setUserFirstName("Jesus");
        Jesus.setCanAnswer(false);
        Jesus.setAllAnswers(0);
        Jesus.setCorrectAnswers(2);
        Jesus.setAttemptCounter(3);
        Jesus.setIqCounter(69);
        players.put("9", Jesus);

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
                        bot.execute(new SendMessage(chatId, "Привіт, " + playerName + "! \n"
                                + "Я бот, що рахує твій IQ! \n"
                                + "Зараз твій IQ: " + newPlayer.getIqCounter() + " \n"
                                + "Але ти можешь його збільшити! \n"
                                + "Раз на добу вводи команду /get_more_iq \n"
                                + "Аби побачити список усіх команд вводи /help \n"
                                + "Так, якщо ти відповіси правильно, то твій IQ збільшиться \n"
                                + "Якщо неправильно, то ти втратишь вже отримане \n"
                                + "Гарної гри!"));
                    } else if (playerMessageText.equals("/rules_iq_game") || playerMessageText.equals("/rules_iq_game@Game_get_IQ_bot")) {
                        bot.execute(new SendMessage(chatId,
                                "Вводи команду /get_more_iq та отримуй рівняння \n" +
                                        "Запиши правильну відповідь (лише цифру) \n" +
                                        "Та отримуй винагороду у вигляді IQ! \n" +
                                        "Якщо неправильно, то ти втратишь вже отримане!\n"));

                    } else if (playerMessageText.equals("/help") || playerMessageText.equals("/help@Game_get_IQ_bot")) {
                        bot.execute(new SendMessage(chatId,
                                "/get_more_iq - збільшити свій IQ\n" +
                                        "/rules_iq_game - правила гри \n" +
                                        "/my_iq - ваша кількість IQ\n" +
                                        "/my_accuracy - відсоток правильних відповідей \n" +
                                        "/list_all_players - список всіх гравців\n " +
                                        "/top10_smartest - топ 10 найрозумніших \n " +
                                        "/top10_correct_answers - топ 10 по правильним відповідям"));
                    } else if (playerMessageText.equals("/my_accuracy") || playerMessageText.equals("/my_accuracy@Game_get_IQ_bot")) {

                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        int allAnswers = currentPlayer.getAllAnswered();
                        int correctAnswers = currentPlayer.getCorrectAnswers();
                        String accuracyPercent = String.valueOf(((correctAnswers * 100) / allAnswers));
                        bot.execute(new SendMessage(chatId,
                                playerName + " твоя правильність відповідей = " + accuracyPercent + "%"));

//                    } else if (playerMessageText.equals("/top10_correct_answers") || playerMessageText.equals("/top10_correct_answers@Game_get_IQ_bot")) {
//
//                        Map<Integer, String> listPlayersMap = new HashMap<>();
//                        players.forEach((id, playerInfo) -> {
//                            if (Objects.equals(playerInfo.getChatId(), chatId)) {
//                                listPlayersMap.put(playerInfo.getCorrectAnswers(), playerInfo.getUserFirstName());
//                            }
//                        });
//
//                        ArrayList<Integer> CorrectAnswersValues = new ArrayList<>(listPlayersMap.keySet());
//                        ArrayList<Integer> repeatableValues = new ArrayList<>();
//
//                        for (int i = 1; i < CorrectAnswersValues.size(); i++) {
//                            int compElement = CorrectAnswersValues.get(i);
//                            for (int j = 0; j < CorrectAnswersValues.size(); j++) {
//                                int secondCompElement = CorrectAnswersValues.size();
//                                if (compElement == secondCompElement) {
//                                    CorrectAnswersValues.remove(j);
//                                    CorrectAnswersValues.set(j, secondCompElement + 1);
//                                    repeatableValues.add(secondCompElement + 1);
//                                    i = 0;
//                                    j = 0;
//                                }
//                            }
//                        }
//
//                        Collections.sort(CorrectAnswersValues);
//                        Collections.reverse(CorrectAnswersValues);
//
//                        if (CorrectAnswersValues.size() >= 10) {
//                            CorrectAnswersValues.subList(0, 9);
//                            String finalList = "Список найрозумніших: \n";
//                            String firstStringValue = " розв'язав(ла) ";
//                            String secondStringValues = " прикладів \n";
//
//                            finalList = finalList + formListOfPlayers(CorrectAnswersValues, repeatableValues, listPlayersMap, firstStringValue, secondStringValues);
//
//                            bot.execute(new SendMessage(chatId,
//                                    finalList
//                            ));
//                        } else {
//                            bot.execute(new SendMessage(chatId,
//                                    playerName + " для початку зберіть 10 гравців, а потім вже будуть такі рейтинги"
//                            ));
//                        }
//                    }
                    }






                    else if (playerMessageText.equals("/list_all_players") || playerMessageText.equals("/list_all_players@Game_get_IQ_bot")) {

                        Map<String, Integer> listPlayersMap = new HashMap<>();
                        players.forEach((id, playerInfo) -> {
                            if (Objects.equals(playerInfo.getChatId(), chatId)) {
                                listPlayersMap.put( playerInfo.getUserFirstName(), playerInfo.getIqCounter());
                            }
                        });

                        Map<String,Integer> sortedMap =
                                listPlayersMap.entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                        StringBuilder finalList = new StringBuilder("Список гравців чату: \n");
                        ArrayList<String> namesOfPlayers = new ArrayList<>(sortedMap.keySet());
                        for (int i = 0; i < namesOfPlayers.size(); i++) {
                            String newRow = "";
                            newRow = i+1+". "+ namesOfPlayers.get(i)+" має айкью "+ sortedMap.get(namesOfPlayers.get(i)) + " \n";
                            finalList.append(newRow);
                        }

                        bot.execute(new SendMessage(chatId,
                                finalList.toString()
                        ));

                    }




                    else if (playerMessageText.equals("/top10_smartest") || playerMessageText.equals("/top10_smartest@Game_get_IQ_bot")) {

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
                            String finalList = "Список найрозумніших: \n";
                            String firstStringValue = " = ";
                            String secondStringValues = " IQ \n";

                            //finalList = finalList + formListOfPlayers(IQValues, repeatableValues, listPlayersMap, firstStringValue, secondStringValues);

                            bot.execute(new SendMessage(chatId,
                                    finalList
                            ));
                        } else {
                            bot.execute(new SendMessage(chatId,
                                    playerName + " для початку зберіть 10 гравців, а потім вже будуть такі рейтинги"
                            ));
                        }
                    } else if (playerMessageText.equals("/my_iq") || playerMessageText.equals("/my_iq@Game_get_IQ_bot")) {
                        PlayerInfo currentPlayer = players.get(uniquePlayerID);
                        bot.execute(new SendMessage(chatId,
                                "Зараз," + playerName + " ,твій IQ: " + currentPlayer.getIqCounter() + " \n"
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
                                        playerName + "! Введіть рішення " + firstNumber + " + " + secondNumber + " ="
                                ));
                                answers.replace(uniquePlayerID, null, (firstNumber + secondNumber));
                            } else if (5 <= moreRandom && moreRandom <= 10 && Objects.equals(currentPlayerId, playerId)) {
                                bot.execute(new SendMessage(chatId,
                                        playerName + "! Введіть рішення " + firstNumber + " * " + secondNumber + " ="
                                ));
                                answers.replace(uniquePlayerID, null, (firstNumber * secondNumber));

                            } else if (moreRandom > 10 && Objects.equals(currentPlayerId, playerId)) {
                                bot.execute(new SendMessage(chatId,
                                        playerName + "! Введіть рішення " + firstNumber + " - " + secondNumber + " ="
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
                                    playerName + ", ти сьогодні вже грав. Спроби вичерпано!"));
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
                                    && !playerMessageText.contains("й")
                                    && !playerMessageText.contains("!")
                                    && !playerMessageText.contains("+")
                                    && !playerMessageText.contains("*")
                                    && !playerMessageText.contains("ф")
                                    && !playerMessageText.contains("а")
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
                                    && !playerMessageText.contains("п")
                                    && !playerMessageText.contains("р")
                                    && !playerMessageText.contains("ц")
                                    && !playerMessageText.contains("о")
                                    && !playerMessageText.contains("к")
                                    && !playerMessageText.contains("л")
                                    && !playerMessageText.contains("м")
                                    && !playerMessageText.contains("н")
                                    && !playerMessageText.contains("д")
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
                            bot.execute(new SendMessage(chatId, "Вітаю, " + playerName + "! Відповідь правильна! \n"
                                    + "Твій IQ збільшився на " + moreIQ + ". Тепер твій IQ становить: " + (currentPlayer.getIqCounter() + moreIQ)));
                            answers.replace(uniquePlayerID, rightAnswer, null);
                            ifAnswerIsCorrect++;
                        } else {
                            moreIQ = -minusIQ;
                            bot.execute(new SendMessage(chatId, "Прикрі новини, " + playerName + "! Відповідь неправильна! \n"
                                    + "Твій IQ зменьшився на " + minusIQ + ". Тепер твій IQ становить: " + (currentPlayer.getIqCounter() + moreIQ)));
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

//    private static String formListOfPlayers(ArrayList<Integer> correctAnswersValues, ArrayList<Integer> repeatableValues, Map<Integer, String> listPlayersMap, String firstStringValue, String secondStringValues) {
//        StringBuilder finalList = new StringBuilder();
//        for (int i = 1; i <= correctAnswersValues.size(); i++) {
//            String newRow;
//            if (repeatableValues.contains(correctAnswersValues.get((i - 1)))) {
//                newRow = i + ".  " + listPlayersMap.get(correctAnswersValues.get((i - 1))) + firstStringValue + ((correctAnswersValues.get((i - 1))) - 1) + secondStringValues;
//            } else {
//                newRow = i + ".  " + listPlayersMap.get(correctAnswersValues.get((i - 1))) + firstStringValue + correctAnswersValues.get((i - 1)) + secondStringValues;
//            }
//            finalList.append(newRow);
//        }
//        return finalList.toString();
//    }

}