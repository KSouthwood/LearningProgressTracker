package tracker;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ID {
    private static int nextID = 1000;

    private LinkedHashMap<String, Integer> emails; // maps an email to an ID
    private HashMap<Integer, Student>      students; // maps an ID to a student

    {
        emails = new LinkedHashMap<>();
        students = new HashMap<>();
    }

    ID() {

    }

    /**
     * Add a student
     *
     * Adds a student if the email is unique (not already used).
     *
     * @param first first name of student
     * @param last last name of student
     * @param email address of student
     * @return true if email was unique
     */
    boolean addStudent(final String first, final String last, final String email) {
        if (isNew(email)) {
            emails.put(email, nextID);
            students.put(nextID, new Student(first, last, email));
            nextID++;
            return true;
        }

        return false;
    }

    /**
     * Check for an existing email
     *
     * @param email to check
     * @return true if email is NOT in map
     */
    private boolean isNew(final String email) {
        return !emails.containsKey(email);
    }
}
