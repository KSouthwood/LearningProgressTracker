package tracker;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CourseStatistics {
    private long   totalPoints;
    private double numOfEntries;
    double pointsToCompleteCourse;
    String courseName;

    private final Map<Integer, Integer> studentTotal;
    private final Map<Integer, Boolean> studentPassed;
    private final ArrayList<Integer> notifyStudent;

    CourseStatistics() {
        this.totalPoints = 0;
        this.numOfEntries = 0d;
        this.studentTotal = new HashMap<>();
        this.studentPassed = new HashMap<>();
        this.notifyStudent = new ArrayList<>();
    }

    String getCourseName() {
        return courseName;
    }

    void addPoints(final int id, final int points) {
        if (points > 0) {
            studentTotal.put(id, studentTotal.getOrDefault(id, 0) + points);
            studentPassed.put(id, studentPassed.getOrDefault(id, false));
            if (studentTotal.get(id) >= pointsToCompleteCourse && !studentPassed.get(id)) {
                notifyStudent.add(id);
            }
            totalPoints += points;
            numOfEntries++;
        }
    }

    /**
     * Used to determine average score of course. Higher average score equals easier course.
     *
     * @return average score
     */
    double getAverageScore() {
        return totalPoints / numOfEntries;
    }

    /**
     * Used to determine activity of a course. More entries equals more activity.
     *
     * @return number of scores entered in course for all students
     */
    double getNumOfEntries() {
        return numOfEntries;
    }

    /**
     * Used to determine popularity of a course. More students equals more popularity.
     *
     * @return number of students in the course
     */
    int getNumOfStudentsEnrolled() {
        return studentTotal.size();
    }

    void printStudentCompletionPercentages() {
        System.out.printf("%s%n  id   points completed%n", courseName);
        listStudentsByPercentageComplete();
    }

    protected void listStudentsByPercentageComplete() {
        if (!studentTotal.isEmpty()) {
            var sortedMap = studentTotal.entrySet().stream()
                                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k,
                                                                  LinkedHashMap::new));

            sortedMap.forEach((k, v) -> System.out.printf("%-6d  %4d    %4.1f%%%n", k, v,
                                                          (v / pointsToCompleteCourse) * 100));
        }
    }

    List<Integer> getNotifications() {
        var notifications = new ArrayList<>(notifyStudent);
        notifyStudent.clear();
        return notifications;
    }
}

class Java extends CourseStatistics {
    Java(final int pointsToPass) {
        super();
        this.pointsToCompleteCourse = pointsToPass;
        this.courseName = "Java";
    }

}

class DSA extends CourseStatistics {
    DSA(final int pointsToPass) {
        super();
        this.pointsToCompleteCourse = pointsToPass;
        this.courseName = "DSA";
    }

}

class Databases extends CourseStatistics {
    Databases(final int pointsToPass) {
        super();
        this.pointsToCompleteCourse = pointsToPass;
        this.courseName = "Databases";
    }

}

class Spring extends CourseStatistics {
    Spring(final int pointsToPass) {
        super();
        this.pointsToCompleteCourse = pointsToPass;
        this.courseName = "Spring";
    }

}
