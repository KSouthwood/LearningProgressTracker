package tracker;

public class Student {
    private final String firstName;
    private final String lastName;
    private final String email;

    private final int id;

    private int java;
    private int dsa;
    private int dbs;
    private int spring;

    Student(final int id, final String firstName, final String lastName, final String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        java = 0;
        dsa = 0;
        dbs = 0;
        spring = 0;
    }

    void addPoints(final int java, final int dsa, final int dbs, final int spring) {
        this.java += java;
        this.dsa += dsa;
        this.dbs += dbs;
        this.spring += spring;
        System.out.println("Points updated.");
    }

    void printPoints() {
        System.out.printf("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d%n",
                          id, java, dsa, dbs, spring);
    }
}
