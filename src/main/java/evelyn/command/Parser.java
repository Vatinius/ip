package evelyn.command;

import evelyn.command.ui.textbased.TextBasedUi;
import evelyn.exception.InvalidInputException;
import evelyn.exception.NoInputException;
import evelyn.task.Deadline;
import evelyn.task.Event;
import evelyn.task.Todo;

import java.time.DateTimeException;
import java.util.Objects;

/**
 * Houses all the logic for parsing words that have been inputted
 */
public class Parser {
    private TaskList lst;
    private String horizontalLine;
    public Parser(TaskList taskList) {
        this.lst = taskList;
        this.horizontalLine = TextBasedUi.horizontalLine;
    }

    public String parse(String text) throws NoInputException, DateTimeException {
        if ((Objects.equals(text, "bye")) || (Objects.equals(text, "BYE")) || (Objects.equals(text, "Bye"))) {
            this.lst.saveTasks();
            return "Bye Bye!!";
        } else if (text.startsWith("delete")) {
            int index = Integer.parseInt(text.substring(7)) - 1;
            return lst.removeTask(index);
        } else if (Objects.equals(text, "list")) {
            return lst.listTask();
        }  else if (text.startsWith("mark")) {
            int index = Integer.parseInt(text.substring(5)) - 1;
            return lst.markTask(index);
        } else if (text.startsWith("unmark")) {
            int index = Integer.parseInt(text.substring(7)) - 1;
            return lst.unmarkTask(index);
        } else if (text.startsWith("find")) {
            if (text.length() <= 5 || Character.isWhitespace(text.charAt(5))) {
                throw new InvalidInputException("Invalid input for find command");
            }
            String keyword = text.substring(5);
            return lst.find(keyword);
        } else if (text.startsWith("todo")) {
            if (text.length() <= 5 || Character.isWhitespace(text.charAt(5))) {
                throw new InvalidInputException("Invalid input for todo");
            }
            String description = text.substring(5);
            Todo newTodo = new Todo(description, false);
            return lst.addTask(newTodo);
        } else if (text.startsWith("deadline")) {
            if (text.length() <= 9 || Character.isWhitespace(text.charAt(9))) {
                throw new InvalidInputException("invalid input for deadline");
            }
            String input = text.substring(9);
            String[] parts = input.split(" /by ");
            String description = parts[0];
            String deadline = parts[1];
            Deadline newDeadline = new Deadline(description, deadline, false);
            return lst.addTask(newDeadline);
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
            return lst.addTask(newEvent);
        } else {
            throw new NoInputException("no input!");
        }
    }
}
