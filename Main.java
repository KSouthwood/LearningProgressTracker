package tracker;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller(new Scanner(System.in));
        System.out.println("Learning Progress Tracker");
        controller.start();
        System.out.println("Bye!");
    }
}
