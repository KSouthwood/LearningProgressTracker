package tracker;

import java.util.*;
import java.util.regex.Pattern;

public class Controller {
    private static Scanner SCANNER;

    private final ID id;

    private final Java      java;
    private final DSA       dsa;
    private final Databases dbs;
    private final Spring    spring;

    private final List<CourseStatistics> courses;



    {
        id = new ID();
        java = new Java(600);
        dsa = new DSA(400);
        dbs = new Databases(480);
        spring = new Spring(550);
        courses = List.of(java, dsa, dbs, spring);
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
                case "notify" -> notifyStudents();
                case "statistics" -> statistics();
                case "exit" -> getCommands = false;
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                default -> System.out.println("Unknown command!");
            }
        }
    }

    private void addStudents() {
        System.out.println("Enter student credentials or 'back' to return:");
        boolean addStudent = true;
        int     added      = 0;

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
     * <p>
     * Ensure that the first and last name as well as the email of the entered student are valid according to our rules.
     * (See isValidName() and isValidEmail().)
     *
     * @param credentials
     *         a list of String's holding the name and email
     *
     * @return true if the name and email are valid according to our rules (see isValidName() and isValidEmail())
     */
    private boolean validateCredentials(final List<String> credentials) {
        int    listSize = credentials.size();
        String first    = credentials.get(0);
        String last     = String.join(" ", credentials.subList(1, listSize - 1));
        String email    = credentials.get(listSize - 1);

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
     * <p>
     * Takes first/last name as a String and validates it as follows: 1. Only english alphabet letters A-Z are allowed
     * along with a hyphen or apostrophe. 2. Names cannot start or end with hyphen or apostrophe. 3. Names cannot have a
     * hyphen/apostrophe adjacent to each other. 4. Names must be at least two letters long.
     *
     * @param name
     *         to be validated
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
     * @param email
     *         to validate
     *
     * @return true if valid
     */
    private boolean isValidEmail(final String email) {
        return email.matches("[\\w.]+@[\\w]+\\.[\\w]+");
    }

    /**
     * Add points to a student by their ID
     * <p>
     * Gets a line of five tokens: student ID, four non-negative integers for points and adds the points to the
     * specified student.
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
            // ID class limit and limits points to the range 0-100.
            // Hyperskill testing uses strings for an ID and wants an "id not found" message for it,
            // so the second regex is needed up through stage 4. Stage 5 needs the third regex to allow
            // for quick testing of the student notifications.

//            if (input.matches("^\\d{4}(\s+([0-9]|[1-9][0-9]|100)){4}$")) {
//            if (input.matches("^\\w*(\\s+([0-9]|[1-9][0-9]|100)){4}$")) {
            if (input.matches("^\\w*(\\s+(\\d{1,3})){4}$")) {
                var inputs = input.split("\\s+");
                if (!inputs[0].matches("\\d*")) {
                    System.out.printf("No student is found for id=%s%n", inputs[0]);
                    continue;
                }

                var     ints    = Arrays.stream(inputs).mapToInt(Integer::parseInt).toArray();
                Student student = id.getStudent(ints[0]);
                if (null == student) {
                    continue;
                }

                student.addPoints(ints[1], ints[2], ints[3], ints[4]);
                java.addPoints(ints[0], ints[1]);
                dsa.addPoints(ints[0], ints[2]);
                dbs.addPoints(ints[0], ints[3]);
                spring.addPoints(ints[0], ints[4]);
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
                int     studentID = Integer.parseInt(input);
                Student student   = id.getStudent(studentID);
                if (null != student) {
                    student.printPoints();
                }
                continue;
            }

            System.out.printf("No student is found for id=%s%n", input);
        }
    }

    private void notifyStudents() {
        Set<Integer> notifications = new HashSet<>();
        for (var course : courses) {
            var studentsToNotify = course.getNotifications();
            for (var student : studentsToNotify) {
                notifications.add(student);
                Student studentInfo = id.getStudent(student);
                System.out.printf("""
                                  To: %s
                                  Re: Your Learning Progress
                                  Hello, %s %s! You have accomplished our %s course!
                                  """,
                                  studentInfo.getEmail(), studentInfo.getFirstName(),
                                  studentInfo.getLastName(), course.getCourseName());
            }
        }
        System.out.printf("Total %d students have been notified.%n", notifications.size());
    }

    private void statistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        printCourseStatistics();

        boolean getCourse = true;
        while (getCourse) {
            var course = SCANNER.nextLine().toLowerCase();
            switch (course) {
                case "back" -> getCourse = false;
                case "java" -> java.printStudentCompletionPercentages();
                case "dsa" -> dsa.printStudentCompletionPercentages();
                case "databases" -> dbs.printStudentCompletionPercentages();
                case "spring" -> spring.printStudentCompletionPercentages();
                default -> System.out.println("Unknown course.");
            }
        }
    }

    void printCourseStatistics() {

        var                    result  = getPopularCourses(courses);
        System.out.printf("Most popular: %s%nLeast popular: %s%n", result.get(0), result.get(1));

        if (!result.get(0).equals("n/a")) {
            result = getActiveCourses(courses);
        }
        System.out.printf("Highest activity: %s%nLowest activity: %s%n", result.get(0), result.get(1));

        if (!result.get(0).equals("n/a")) {
            result = getCourseDifficulty(courses);
        }
        System.out.printf("Easiest course: %s%nHardest course: %s%n", result.get(0), result.get(1));

    }

    private List<String> getPopularCourses(final List<CourseStatistics> courses) {
        TreeMap<Integer, ArrayList<String>> popular = new TreeMap<>();
        for (var course : courses) {
            popular.computeIfAbsent(course.getNumOfStudentsEnrolled(), k -> new ArrayList<>()).add(course.getCourseName());
        }

        // handle case where no students have completed any assignments
        if (popular.size() == 1 && popular.containsKey(0)) {
            return List.of("n/a", "n/a");
        }

        return List.of(String.join(", ", popular.lastEntry().getValue()),
                       popular.size() > 1 ? String.join(", ", popular.firstEntry().getValue()) : "n/a");
    }

    private List<String> getActiveCourses(final List<CourseStatistics> courses) {
        TreeMap<Double, ArrayList<String>> active = new TreeMap<>();
        for (var course : courses) {
            active.computeIfAbsent(course.getNumOfEntries(), k -> new ArrayList<>()).add(course.getCourseName());
        }

        return List.of(String.join(", ", active.lastEntry().getValue()),
                       active.size() > 1 ? String.join(", ", active.firstEntry().getValue()) : "n/a");

    }

    private List<String> getCourseDifficulty(final List<CourseStatistics> courses) {
        TreeMap<Double, ArrayList<String>> difficulty = new TreeMap<>();
        for (var course : courses) {
            difficulty.computeIfAbsent(course.getAverageScore(), k -> new ArrayList<>()).add(course.getCourseName());
        }

        return List.of(String.join(", ", difficulty.lastEntry().getValue()),
                       difficulty.size() > 1 ? String.join(", ", difficulty.firstEntry().getValue()) : "n/a");

    }
}
