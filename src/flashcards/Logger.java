package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logger {
    private static final List<String> logsList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    protected static void logOutput(String text) {
        System.out.println(text);
        logsList.add(text);
    }

    protected static String logInput() {
        String input = scanner.nextLine();
        logsList.add(input);
        return input;
    }

    protected static void saveLogs() {
        logOutput("File name:");
        String fileName = logInput();
        File file = new File(fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println("= = = Start of logs = = =");
            for (String log : logsList) {
                printWriter.println(log);
            }
            printWriter.println("= = = End of logs = = =");
            printWriter.close();
            logOutput("The log has been saved.");
        } catch (FileNotFoundException e) {
            logOutput("File not found");
        }
    }
}
