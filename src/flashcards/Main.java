package flashcards;

public class Main extends FlashCard {

    public static void main(String[] args) {
        // add
        // -import "filename.txt" -export "filename.txt"
        // to Main CLI arguments
        String exportFile = null;

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-import":
                    if (args[i + 1] != null) {
                        importCardTry(args[i + 1]);
                    }
                    break;
                case "-export":
                    exportFile = args[i + 1];
                    break;
            }
        }

        while (true) {
            logOutput("Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):");

            switch (logInput()) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCardMain();
                    break;
                case "export":
                    exportCardMain();
                    break;
                case "ask":
                    ask();
                    break;
                case "log":
                    saveLogs();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                case "exit":
                    logOutput("Bye bye!");
                    if (exportFile != null) {
                        exportCardTry(exportFile);
                    }
                    return;
            }
        }
    }
}