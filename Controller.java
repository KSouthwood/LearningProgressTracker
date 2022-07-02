package tracker;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Controller {
    private static Scanner SCANNER;

    private final ID id;


    {
        id = new ID();
    }

    Controller(Scanner scanner) {
        SCANNER = scanner;
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
                case "add points" -> addPoints();
                case "find" -> findStudent();
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
     * student are valid according to our rules. (See isValidName() and
     * isValidEmail().)
     *
     * @param credentials a list of String's holding the name and email
     *
     * @return true if the name and email are valid according to our rules
     *         (see isValidName() and isValidEmail())
     */
    private boolean validateCredentials(final List<String> credentials) {
        int listSize = credentials.size();
        String first = credentials.get(0);
        String last = String.join(" ", credentials.subList(1, listSize - 1));
        String email = credentials.get(listSize - 1);

        if (!isValidName(first)) {
            System.out.println("Incorrect first name.");
            return false;
        }

        if (!isValidName(last)) {
            System.out.println("Incorrect last name.");
            return false;
        }

        if (!isValidEmail(email)) {
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
    private boolean isValidName(final String name) {
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
    private boolean isValidEmail(final String email) {
        return email.matches("[\\w.]+@[\\w]+\\.[\\w]+");
    }

    /**
     * Add points to a student by their ID
     *
     * Gets a line of five tokens: student ID, four non-negative integers for points
     * and adds the points to the specified student.
     */
    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");
        boolean addMore = true;

        while (addMore) {
            String input = SCANNER.nextLine();

            if (input.matches("back")) {
                addMore = false;
                continue;
            }

            // would rather use the first regex which ensures the ID portion matches our
            // ID class limit. Hyperskill testing uses strings there and wants an
            // "id not found" message for it, so the second regex is needed.
//            if (input.matches("^\\d{4}(\s+([0-9]|[1-9][0-9]|100)){4}$")) {
            if (input.matches("^\\w*(\s+([0-9]|[1-9][0-9]|100)){4}$")) {
                var inputs = input.split("\\s+");
                if (!inputs[0].matches("\\d*")) {
                    System.out.printf("No student is found for id=%s%n", inputs[0]);
                    continue;
                }

                var ints = Arrays.stream(inputs).mapToInt(Integer::parseInt).toArray();
                Student student = id.getStudent(ints[0]);
                if (null == student) {
                    continue;
                }

                student.addPoints(ints[1], ints[2], ints[3], ints[4]);
                continue;
            }

            System.out.println("Incorrect points format.");
        }
    }

    /**
     * Prints the points for a student.
     */
    void findStudent() {
        System.out.println("Enter an id or 'back' to return:");
        boolean find = true;

        while (find) {
            String input = SCANNER.nextLine();
            if (input.equals("back")) {
                find = false;
                continue;
            }

            if (input.matches("\\d+")) {
                int studentID = Integer.parseInt(input);
                Student student = id.getStudent(studentID);
                if (null != student) {
                    student.printPoints();
                }
                continue;
            }

            System.out.printf("No student is found for id=%s%n", input);
        }

    }
}
