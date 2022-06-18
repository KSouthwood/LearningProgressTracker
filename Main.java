package tracker;

import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        processCommands();
        System.out.println("Bye!");
    }

    private static void processCommands() {
        boolean getCommands = true;

        while (getCommands) {
            String command = getInput();
            if (command.isBlank()) {
                System.out.println("No input.");
                continue;
            }

            switch (command) {
                case "exit" -> getCommands = false;
                default -> System.out.println("Error: unknown command!");
            }
        }
    }

    private static String getInput() {
        return SCANNER.nextLine().toLowerCase();
    }
}
