package view;

public class MenuView {

    public void printWelcome() {
        System.out.println();
        System.out.println("=== Navigator ===");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println("1. Generate points and compute routes");
        System.out.println("2. View saved points");
        System.out.println("3. View saved routes");
        System.out.println("4. Exit");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.err.println(message);
    }
}
