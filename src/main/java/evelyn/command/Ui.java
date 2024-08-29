package evelyn.command;

import evelyn.exception.InvalidInputException;
import evelyn.exception.NoInputException;

import java.util.Objects;
import java.util.Scanner;


public class Ui {
    public static String horizontalLine = "-----------------------------------------";
    private boolean isChatting;
    private String text = null;
    private Parser parser;
    private Scanner scanner;

    public Ui(Parser parser) {
        this.isChatting = false;
        this.scanner = new Scanner(System.in);
        this.parser = parser;
    }

    public void start() {
        this.isChatting = true;
        System.out.println(horizontalLine);
        System.out.println("Hi! I am evelyn.Evelyn");
        System.out.println("Here are my keywords:");
        System.out.println("\n");
        System.out.println("todo [task description]");
        System.out.println("deadline [task description] /by [date in YYYY-MM-DD] [Optional: time]");
        System.out.println("event [task description] /from [date in YYYY-MM-DD] [Optional: time] /to [date in YYYY-MM-DD] [Optional: time]");
        System.out.println("\n");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);

        while(this.isChatting) {
            text = scanner.nextLine();

            try {
                if ((Objects.equals(text, "bye")) || (Objects.equals(text, "BYE")) || (Objects.equals(text, "Bye"))) {
                    this.parser.parse(text);
                    System.out.println(horizontalLine);
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(horizontalLine);
                    this.isChatting = false;
                } else {
                    this.parser.parse(text);
                }
            } catch (InvalidInputException e) {
                System.out.println(horizontalLine);
                System.out.println("You did not use the keywords properly!");
                System.out.println("Please use the following key words:");
                System.out.println("todo [task description]");
                System.out.println("deadline [task description] /by [date]");
                System.out.println("event [task description] /from [start date and time] /to [end date and time");
                System.out.println(horizontalLine);
            } catch (NoInputException e) {
                System.out.println(horizontalLine);
                System.out.println("Invalid");
                System.out.println("Please use the following key words:");
                System.out.println("todo [task description]");
                System.out.println("deadline [task description] /by [date]");
                System.out.println("event [task description] /from [start date and time] /to [end date and time");
                System.out.println(horizontalLine);
            }
        }
    }

    public void startChatting() {
        this.isChatting = true;
    }

    public void stopChatting() {
        this.isChatting = false;
    }
}
