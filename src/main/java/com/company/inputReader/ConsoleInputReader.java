package com.company.inputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The "ConsoleInputReader" class provides methods for reading various types of input from the user through the console.
 * It uses a Scanner object to read input from System.in,
 * and provides methods to handle user input in different formats, such as integers and strings.
 * The class also includes error handling for invalid user input, such as parsing errors and invalid 'yes/no' input.
 */
public class ConsoleInputReader {

    private final Scanner scanner;

    public ConsoleInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public static final String INVALID_INPUT_PLEASE_ENTER_A_VALID_NUMBER = "Invalid input." +
            " Please enter a valid number. ";
    public static final String INVALID_INPUT_PROMPT_MESSAGE = "Invalid input. Please enter 'y' or 'n'. ";
    public static final String yes = "y";
    public static final String no = "n";
    public static final String DONE = "done";
    public static final String ENTER_PAYLOAD_IDES_PROMPT_MESSAGE = "Enter payload ides (-1 for all)" +
            " numbers or type 'done' to finish: ";
    public static final String ENTER_A_CLIENT_NUMBER_PROMPT_MESSAGE = "Enter a client number" +
            " (-1 for all clients) or type 'done' to finish: ";

    public List<Integer> readClientsIdsFromUser() {
        return readNumbersUntilDoneFromUser(ENTER_A_CLIENT_NUMBER_PROMPT_MESSAGE);
    }

    public List<Integer> getResultsNumbersFromUser() {
        return readNumbersUntilDoneFromUser(ENTER_PAYLOAD_IDES_PROMPT_MESSAGE);
    }

    private List<Integer> readNumbersUntilDoneFromUser(String promptMessage) {
        List<Integer> numbers = new ArrayList<>();
        while (true) {
            String input = getStringFromUser(promptMessage);
            if (input.equalsIgnoreCase(DONE)) {
                return numbers;
            }
            try {
                int clientNumber = Integer.parseInt(input);
                numbers.add(clientNumber);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_PLEASE_ENTER_A_VALID_NUMBER);
            }
        }
    }

    public int readIntFromUser(String promptMessage) {
        while (true) {
            String input = getStringFromUser(promptMessage);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_PLEASE_ENTER_A_VALID_NUMBER);
            }
        }
    }

    public String getStringFromUser(String promptMessage) {
        System.out.println(promptMessage);
        return scanner.nextLine();
    }

    public String getStringFromUser() {
        return scanner.nextLine();
    }

    public boolean yesNoQuestion(String promptMessage) {
        String input = getStringFromUser(promptMessage);

        while (!input.equalsIgnoreCase(yes) && !input.equalsIgnoreCase(no)) {
            input = getStringFromUser(INVALID_INPUT_PROMPT_MESSAGE);
        }

        return input.equalsIgnoreCase(yes);
    }
}
