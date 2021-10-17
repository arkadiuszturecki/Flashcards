package flashcards;

import java.io.*;
import java.util.*;

public class FlashCard extends Logger {
    private static final Map<String, String> flashCards = new HashMap<>();
    private static final Map<String, Integer> hardestCards = new HashMap<>();

    protected static void addCard() {
        logOutput("The card:");
        String term = logInput();

        if (flashCards.containsKey(term)) {
            logOutput("The card \"" + term + "\" already exists. Try again:");
            return;
        }

        logOutput("The definition of the card:");
        String definition = logInput();

        if (flashCards.containsValue(definition)) {
            logOutput("The definition \"" + definition + "\" already exists. Try again:");
            return;
        }

        flashCards.put(term, definition);
        hardestCards.put(term, 0);
        logOutput("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
    }

    protected static void removeCard() {
        logOutput("Which card?");
        String removeInput = logInput();

        if (flashCards.containsKey(removeInput)) {
            flashCards.remove(removeInput);
            hardestCards.remove(removeInput);
            logOutput("The card has been removed.");
        } else {
            logOutput("Can't remove \"" + removeInput + "\": there is no such card.");
        }
    }

    protected static void importCardMain() {
        logOutput("File name:");
        String fileName = logInput();
        importCardTry(fileName);
    }

    protected static void importCardTry(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            List<String[]> cardList = (ArrayList) ois.readObject();
            for (var card : cardList) {
                flashCards.put(card[0], card[1]);
                hardestCards.put(card[0], Integer.parseInt(card[2]));
            }
            logOutput(cardList.size() + " cards have been loaded.");
        } catch (IOException e) {
            logOutput("File not found.");
        } catch (ClassNotFoundException e) {
            logOutput("Class not found");
        }
    }

    protected static void exportCardMain() {
        logOutput("File name:");
        String fileName = logInput();
        exportCardTry(fileName);
    }

    protected static void exportCardTry(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<String[]> cardList = new ArrayList<>();
            for (var entry : flashCards.entrySet()) {
                String term = entry.getKey();
                String definition = entry.getValue();
                cardList.add(new String[]{term, definition, Integer.toString(hardestCards.get(term))});
            }
            oos.writeObject(cardList);
            logOutput(flashCards.size() + " cards have been saved.");
        } catch (IOException e) {
            logOutput("File not found.");
        }
    }

    protected static void ask() {
        Object[] keys = flashCards.keySet().toArray();
        Random random = new Random();

        if (keys.length > 0) {
            logOutput("How many times to ask?");
            int numOfQuestions = Integer.parseInt(logInput());

            for (int i = 0; i < numOfQuestions; i++) {
                String randomKey = (String) keys[random.nextInt(keys.length)];
                logOutput("Print the definition of \"" + randomKey + "\":");
                String answer = logInput();

                if (answer.equals(flashCards.get(randomKey))) {
                    logOutput("Correct!");
                } else if (flashCards.containsValue(answer)) {
                    for (var entry : flashCards.entrySet()) {
                        if (entry.getValue().equals(answer)) {
                            logOutput("Wrong. The right answer is \"" + flashCards.get(randomKey) + "\", but your definition is correct for \"" + entry.getKey() + "\".");
                            hardestCards.put(entry.getKey(), hardestCards.get(entry.getKey()) + 1);
                        }
                    }
                } else {
                    logOutput("Wrong. The right answer is \"" + flashCards.get(randomKey) + "\".");
                    hardestCards.put(randomKey, hardestCards.get(randomKey) + 1);
                }
            }
        } else {
            logOutput("There are no cards.");
        }
    }

    protected static void hardestCard() {
        List<String> hardestCardsList = new ArrayList<>();
        int maxWrongAnswers = 1;

        for (String term : hardestCards.keySet()) {
            int wrongAnswers = hardestCards.get(term);
            if (wrongAnswers > maxWrongAnswers) {
                maxWrongAnswers = wrongAnswers;
                hardestCardsList.clear();
                hardestCardsList.add(term);
            } else if (wrongAnswers == maxWrongAnswers) {
                hardestCardsList.add(term);
            }
        }

        if (hardestCardsList.size() == 0) {
            logOutput("There are no cards with errors.");
        } else if (hardestCardsList.size() == 1) {
            logOutput("The hardest card is \"" + hardestCardsList.get(0) + "\". You have " + maxWrongAnswers + " errors answering it.");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String card : hardestCardsList) {
                stringBuilder.append(" \"").append(card).append("\",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            logOutput("The hardest cards are" + stringBuilder + ". You have " + maxWrongAnswers + " errors answering them.");
        }
    }

    protected static void resetStats() {
        hardestCards.replaceAll((key, value) -> 0);
        logOutput("Card statistics have been reset.");
    }
}