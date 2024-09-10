import java.util.Scanner;

public class Bean {
    // Constants
    private final static String SEPARATOR_LINE = "_".repeat(60) + "\n";
    private final static String INDENT = "  ";
    private final static int MAX_LIST_COUNT = 100;
    private final static String LOGO = "  ┏━┓\n" +
            "  ┃ ┃\n" +
            "  ┃ ┗━━┳━━━┳━━━━┳━━━┓\n" +
            "  ┃  ┏┓┃ ┃━┫ ┏┓ ┃ ┏┓ ┓ ┏━━━━━━┓\n" +
            "  ┃  ┗┛┃ ┃━┫ ┏┓ ┃ ┃┃ ┃ ┃• ᴗ • ┫\n" +
            "  ┗━━ ━┻━━━┻━┛┗━┻━┛┗━┛ ┗━━━━━━┛\n";

    private static Task[] toDoList = new Task[MAX_LIST_COUNT];

    // Print logo with greeting message
    public static void greet() {
        System.out.println(SEPARATOR_LINE +
                LOGO +
                INDENT + "Howdy! :) I'm bean, your personal assistant.\n" +
                INDENT + "Let me help you keep track of your tasks!\n" +
                SEPARATOR_LINE
        );
    }

    // Print logo with exit message
    public static void exit() {
        System.out.println(SEPARATOR_LINE +
                LOGO +
                INDENT + "Bye bye, glad I could help! See you soon? :'(\n" +
                SEPARATOR_LINE);
    }

    // Return command (taken as first word) from given user input
    public static String extractCommand(String userInput) {
        // Take first word of input as command
        return userInput.split(" ")[0];
    }

    // Print (single line) message with separator line above and below message
    public static void printFormattedReply(String reply) {
        System.out.println(SEPARATOR_LINE +
                reply + "\n" +
                SEPARATOR_LINE);
    }

    public static void printToDoList() throws EmptyListException {
        if (toDoList[0] == null) {
            throw new EmptyListException();
        }

        System.out.println(SEPARATOR_LINE +
                INDENT + "TO DO LIST:\n");

        for (int i = 0; i < toDoList.length; i++) {
            if (toDoList[i] == null) {
                break;
            }
            System.out.println(INDENT + (i + 1) + ". " + toDoList[i].toString());
        }
        System.out.println(SEPARATOR_LINE);
    }

    // Extract task number as int from user input for mark and unmark commands
    public static int obtainTaskNum(String userInput) {
        // Obtain task number by taking second word of input and trim any spaces, then parse as int
        String[] words = userInput.split(" ");
        return Integer.parseInt(words[1].trim());
    }

    public static void markTaskAsDone(int taskNum) {
        int taskIndex = taskNum - 1;
        toDoList[taskIndex].setStatus(true);
        // Confirmation message
        printFormattedReply(INDENT + "Task " + taskNum + " has been marked as DONE:\n" +
                INDENT + INDENT + toDoList[taskIndex].toString());
    }

    public static void unmarkTaskAsDone(int taskNum) {
        int taskIndex = taskNum - 1;
        toDoList[taskIndex].setStatus(false);
        // Confirmation message
        printFormattedReply(INDENT + "Task " + taskNum + " has been marked as UNDONE:\n" +
                INDENT + INDENT + toDoList[taskIndex].toString());
    }

    public static void addToDo(String userInput) {
        // Extract description
        String description = userInput.split("todo ")[1].trim();

        toDoList[Task.getNumberOfTasks()] = new Todo(description);
    }

    public static void addDeadline(String userInput) {
        // Extract description and by
        String[] parts = userInput.split("/by ");
        // parts: [0] = "deadline {description} ", [1] = " {by}"
        String description = parts[0].substring("deadline ".length()).trim();
        String by = parts[1].trim();

        toDoList[Task.getNumberOfTasks()] = new Deadline(description, by);
    }

    public static void addEvent(String userInput) {
        // Extract description, from and to
        String[] splitDescription = userInput.split("/from");
        // splitDescription: [0] = "event {description} ", [1] = "{from} /to {to}"
        String description = splitDescription[0].substring("events".length()).trim();
        String[] splitFromTo = splitDescription[1].split("/to");
        // splitFromTo: [0] = "{from} ", [1] = "{to}"
        String from = splitFromTo[0].trim();
        String to = splitFromTo[1].trim();

        toDoList[Task.getNumberOfTasks()] = new Event(description, from, to);
    }

    public static void printInvalidInputMessage() {
        printFormattedReply(INDENT + "Sorry, I am not equipped to respond to that yet... :(\n" +
                INDENT + "These are the commands I understand:\n" +
                INDENT + "1. To add a new task:\n" +
                INDENT + INDENT + "a. todo [description]\n" +
                INDENT + INDENT + "b. deadline [description] /by [by]\n" +
                INDENT + INDENT + "c. event [description] /from [from] /to [to]\n" +
                INDENT + INDENT + INDENT + "example: event dinner /from 6pm /to 8pm\n" +
                INDENT + "2. To view your to do list: list\n" +
                INDENT + "3. To mark a task as done: mark [task number]\n" +
                INDENT + "4. To mark a task as undone: unmark [task number]");
    }

    public static void processUserInput() throws InvalidInputException {
        String userInput;
        Scanner in = new Scanner(System.in);

        while (Task.getNumberOfTasks() < MAX_LIST_COUNT) {
            userInput = in.nextLine();
            String userCommand = extractCommand(userInput);

            try {
                switch (userCommand) {
                case "bye":
                    // To exit
                    return;

                case "list":
                    printToDoList();
                    break;

                case "mark":
                    markTaskAsDone(obtainTaskNum(userInput));
                    break;

                case "unmark":
                    unmarkTaskAsDone(obtainTaskNum(userInput));
                    break;

                case "todo":
                    addToDo(userInput);
                    break;

                case "deadline":
                    addDeadline(userInput);
                    break;

                case "event":
                    addEvent(userInput);
                    break;

                default:
                    throw new InvalidInputException();
                }
            } catch (InvalidInputException e) {
                printInvalidInputMessage();
            } catch (EmptyListException e) {
                printFormattedReply(INDENT + "Nothing in your to do list yet!");
            }
        }
    }

    public static void main(String[] args) throws InvalidInputException {
        greet();
        processUserInput();
        exit();
    }
}