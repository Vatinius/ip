import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

public class Evelyn {
    private static String chatbotName = "Evelyn";
    private static String horizontalLine = "-----------------------------------------";
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList lst = new ArrayList(100);
    private static boolean isChatting = true;
    private static String dataFilePath = "src/main/data/evelyn.txt";
    private static File file = new File(dataFilePath);

    public static void main(String[] args) throws IOException {
        String text = null;
        System.out.println(horizontalLine);
        System.out.println("Hi! I am Evelyn");
        System.out.println("Here are my keywords:");
        System.out.println("\n");
        System.out.println("todo [task description]");
        System.out.println("deadline [task description] /by [date in YYYY-MM-DD] [Optional: time]");
        System.out.println("event [task description] /from [date in YYYY-MM-DD] [Optional: time] /to [date in YYYY-MM-DD] [Optional: time]");
        System.out.println("\n");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);

        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(dataFilePath));
                String line;
                while ((line = br.readLine()) != null) {
                    fileDataToList(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }

        while (isChatting) {
            text = scanner.nextLine();

            try {
                decipher(text);
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
            } catch (IOException e) {
                System.out.println("Error reading/writing file");
            }
        }
    }

    private static void decipher(String text) throws NoInputException, IOException {
        if ((Objects.equals(text, "bye")) || (Objects.equals(text, "BYE")) || (Objects.equals(text, "Bye"))) {
            file.delete();
            file.createNewFile();
            for (int i = 0; i < lst.size(); i++) {
                Task task = (Task) lst.get(i);
                writeToFile(dataFilePath, task.toString() + System.lineSeparator());
            }
            System.out.println(horizontalLine);
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(horizontalLine);
            isChatting = false;
        } else if (text.startsWith("delete")) {
            int index = Integer.parseInt(text.substring(7)) - 1;
            Task task = (Task) lst.get(index);
            lst.remove(index);
            System.out.println(horizontalLine);
            System.out.println("Noted. I've removed this task:");
            System.out.println("   " + task.toString());
            System.out.println(lst.size() > 1 ? "Now you have " + lst.size() + " tasks in this list"
                    : "Now you have " + lst.size() + " task in this list");
        } else if (Objects.equals(text, "list")) {
            System.out.println(horizontalLine);
            System.out.println("Here are the tasks in your list: ");
            for (int i = 0; i < lst.size(); i++) {
                System.out.println((i + 1) + "." + lst.get(i));
            }
            System.out.println(horizontalLine);
        }  else if (text.startsWith("mark")) {
            int index = Integer.parseInt(text.substring(5)) - 1;
            Task task = (Task) lst.get(index);
            System.out.println(horizontalLine);
            task.mark();
            System.out.println(horizontalLine);
        } else if (text.startsWith("unmark")) {
            int index = Integer.parseInt(text.substring(7)) - 1;
            Task task = (Task) lst.get(index);
            System.out.println(horizontalLine);
            task.unmark();
            System.out.println(horizontalLine);
        } else if (text.startsWith("todo")) {
            if (text.length() <= 5 || Character.isWhitespace(text.charAt(5))) {
                throw new InvalidInputException("invalid input for todo");
            }
            String description = text.substring(5);
            Todo newTodo = new Todo(description, false);
            lst.add(newTodo);
            writeToFile(dataFilePath, newTodo.toString() + System.lineSeparator());
            System.out.println(horizontalLine);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newTodo.toString());
            System.out.println(lst.size() > 1 ? "Now you have " + lst.size() + " tasks in this list"
                    : "Now you have " + lst.size() + " task in this list");
            System.out.println(horizontalLine);
        } else if (text.startsWith("deadline")) {
            if (text.length() <= 9 || Character.isWhitespace(text.charAt(9))) {
                throw new InvalidInputException("invalid input for deadline");
            }
            String input = text.substring(9);
            String[] parts = input.split(" /by ");
            String description = parts[0];
            String deadline = parts[1];
            Deadline newDeadline = new Deadline(description, deadline, false);
            lst.add(newDeadline);
            writeToFile(dataFilePath, newDeadline.toString() + System.lineSeparator());
            System.out.println(horizontalLine);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newDeadline.toString());
            System.out.println(lst.size() > 1 ? "Now you have " + lst.size() + " tasks in this list"
                    : "Now you have " + lst.size() + " task in this list");
            System.out.println(horizontalLine);
        } else if (text.startsWith("event")) {
            if (text.length() <= 6 || Character.isWhitespace(text.charAt(6))) {
                throw new InvalidInputException("invalid input for event");
            }
            String input = text.substring(6);
            String[] partA = input.split(" /from " );
            String description = partA[0];
            String[] partB = partA[1].split(" /to ");
            String start = partB[0];
            String end = partB[1];
            Event newEvent = new Event(description, start, end, false);
            lst.add(newEvent);
            writeToFile(dataFilePath, newEvent.toString() + System.lineSeparator());
            System.out.println(horizontalLine);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newEvent.toString());
            System.out.println(lst.size() > 1 ? "Now you have " + lst.size() + " tasks in this list"
                    : "Now you have " + lst.size() + " task in this list");
            System.out.println(horizontalLine);
        } else {
            throw new NoInputException("no input!");
        }
    }

    private static void writeToFile(String filePath, String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(textToAdd);
        fw.close();
    }

    private static void fileDataToList(String data) throws IOException {
        boolean isMarked;
        if (data.startsWith("[T]")) {
            if (data.contains("[X]")) {
                isMarked = true;
            } else {
                isMarked = false;
            }
            System.out.println(isMarked ? "True" : "False");
            String description = data.substring(7);
            Todo newTodo = new Todo(description, isMarked);
            lst.add(newTodo);
        } else if (data.startsWith("[D]")) {
            if (data.contains("[X]")) {
                isMarked = true;
            } else {
                isMarked = false;
            }
            String descAndDate = data.substring(7);
            String[] parts = descAndDate.split(" \\(by: ");
            String description = parts[0];
            String deadline = parts[1].substring(0, parts[1].length() - 1);
            Deadline newDeadline = new Deadline(description, deadline, isMarked);
            lst.add(newDeadline);
        } else if (data.startsWith("[E]")) {
            if (data.contains("[X]")) {
                isMarked = true;
            } else {
                isMarked = false;
            }
            String descAndDate = data.substring(7);
            String[] parts1 = descAndDate.split(" \\(from: ");
            String description = parts1[0];
            String[] parts2 = parts1[1].split(" to: ");
            String start = parts2[0];
            String end = parts2[1].substring(0, parts2[1].length() - 1);

            Event newEvent = new Event(description, start, end, isMarked);
            lst.add(newEvent);
        } else {
            throw new IOException();
        }
    }

}
