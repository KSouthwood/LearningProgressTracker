package tracker;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Controller {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ID id;


    {
        id = new ID();
    }

    Controller() {

    }

    void start() {
        boolean getCommands = true;

        while (getCommands) {
            String command = SCANNER.nextLine().toLowerCase();
            if (command.isBlank()) {
                System.out.println("No input.");
                continue;
            }

            switch (command) {
                case "add students" -> addStudents();
                case "list" -> id.printStudentList();
                case "exit" -> getCommands = false;
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                default -> System.out.println("Unknown command!");
            }
        }
    }

    private void addStudents() {
        System.out.println("Enter student credentials or 'back' to return:");
        boolean addStudent = true;
        int added = 0;

        while (addStudent) {
            var studentName = Arrays.stream(SCANNER.nextLine().split("\\s+")).toList();
            if (studentName.size() == 1 && studentName.get(0).equals("back")) {
                addStudent = false;
                continue;
            }

            if (studentName.size() < 3) {
                System.out.println("Incorrect credentials.");
                continue;
            }

            if (validateCredentials(studentName)) {
                System.out.println("The student has been added.");
                added++;
            }
        }

        System.out.printf("Total %d students have been added.%n", added);
    }

    /**
     * Validate the name and email of the student
     *
     * Ensure that the first and last name as well as the email of the entered
     * student are valid according to our rules. (See validateName() and
     * validateEmail().)
     *
     * @param credentials a list of String's holding the name and email
     *
     * @return true if the name and email are valid according to our rules
     *         (see validateName() and validateEmail())
     */
    boolean validateCredentials(final List<String> credentials) {
        int listSize = credentials.size();
        String first = credentials.get(0);
        String last = String.join(" ", credentials.subList(1, listSize - 1));
        String email = credentials.get(listSize - 1);

        if (!validateName(first)) {
            System.out.println("Incorrect first name.");
            return false;
        }

        if (!validateName(last)) {
            System.out.println("Incorrect last name.");
            return false;
        }

        if (!validateEmail(email)) {
            System.out.println("Incorrect email.");
            return false;
        }

        if (id.addStudent(first, last, email)) {
            return true;
        }

        System.out.println("This email is already taken.");
        return false;
    }

    /**
     * Validate the name
     *
     * Takes first/last name as a String and validates it as follows:
     * 1. Only english alphabet letters A-Z are allowed along with a hyphen or apostrophe.
     * 2. Names cannot start or end with hyphen or apostrophe.
     * 3. Names cannot have a hyphen/apostrophe adjacent to each other.
     * 4. Names must be at least two letters long.
     *
     * @param name to be validated
     *
     * @return true if valid
     */
    boolean validateName(final String name) {
        Pattern notValid = Pattern.compile("'-|-'|--|''");

        for (var part : name.split("\\s")) {
            if (part.length() < 2) {
                return false;
            }

            if (notValid.matcher(part).find()) {
                return false;
            }

            if (!part.matches("(?i)[a-z][a-z'-]*[a-z]")) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param email to validate
     *
     * @return true if valid
     */
    boolean validateEmail(final String email) {
        return email.matches("[\\w.]+@[\\w]+\\.[\\w]+");
    }
}
