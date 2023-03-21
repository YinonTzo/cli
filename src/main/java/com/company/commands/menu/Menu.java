package com.company.commands.menu;

import com.company.CLI.SocketInputOutput;
import com.company.commands.Command;
import com.company.commands.CommandLoader;
import com.company.commands.Commands;
import com.company.commands.constantCommands.DisplayClientsStatusCommand;
import com.company.commands.constantCommands.ExitCommand;
import com.company.common.messages.serverToCLI.TextMessage;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Menu class represents a menu of available commands that a user can choose from.
 * It loads a list of available commands, displays them to the user,
 * and allows the user to select a command by entering a number.
 * Once a command is selected, the Menu class sends a message to a server (using an SocketInputOutput object),
 * receives a response, and prints the response to the console.
 * The menu continues to run until the user chooses to exit.
 * <p>
 * The class has a DisplayClientsStatusCommand that can be used to display the status of connected clients.
 * To add a new functionality, the operator must create a class in the 'plugins' directory,
 * after which it will be automatically uploaded to the menu.
 */

@Getter
@Setter
@Slf4j
public class Menu {
    public static final String AVAILABLE_COMMANDS = "Available commands: ";
    public static final String PLEASE_CHOOSE_VALID_NUMBER = "Please choose a valid command.";
    public static final String PLEASE_ENTER_YOUR_COMMAND = "Please enter your command: ";
    public static final String COMMAND_NOT_FOUND = "Server sent %s class, but in the CLI there is no command %s." +
            " Read the README again and check how to add new commands.";
    public static final String COMMAND_NOT_FOUND_COMMAND = "CommandNotFoundCommand";

    private final Commands commands = new Commands();

    private Command displayClientsStatusCommand;

    private final SocketInputOutput inputOutput;
    private final ConsoleInputReader consoleInputReader;

    public Menu(SocketInputOutput inputOutput, ConsoleInputReader consoleInputReader) {
        this.inputOutput = inputOutput;
        this.consoleInputReader = consoleInputReader;
        commands.addCommands(loadConstantCommands());
        commands.addCommands(CommandLoader.loadCommands(consoleInputReader));
    }

    private List<Command> loadConstantCommands() {
        List<Command> constantCommands = new ArrayList<>();
        this.displayClientsStatusCommand = new DisplayClientsStatusCommand();
        Command exitCommand = new ExitCommand();
        constantCommands.add(displayClientsStatusCommand);
        constantCommands.add(exitCommand);
        return constantCommands;
    }

    public void run() throws IOException, ClassNotFoundException {
        int option = 0;
        do {
            displayStatuses();
            System.out.println();
            show();
            option = getOptionFromUser();
            BaseCLIToServer message = createMessage(option);
            sendMessage(message);
            BaseServerToCLI response = receiveMessage();
            Command responseCommandType = getCommand(response.getType());
            printResponse(response, responseCommandType);

            System.out.println("===============================================\n");
        }
        while (isKeepRunningCommand(option));
    }

    private void printResponse(BaseServerToCLI response, Command responseCommandType) {
        if (responseCommandType == null) {
            if (response.getType().equals(COMMAND_NOT_FOUND_COMMAND)) {
                TextMessage errorMessage = (TextMessage) response;
                log.error(errorMessage.getText());
            } else {
                commandNotFound(response);
            }
        } else {
            responseCommandType.printResponse(response);
        }
    }

    private void commandNotFound(BaseServerToCLI response) {
        log.error(String.format(COMMAND_NOT_FOUND, response.getType(), response.getType()));
    }

    private BaseCLIToServer createMessage(int option) {
        return commands.getCommand(option).getMessage();
    }

    private Command getCommand(String commandType) {
        return commands.getCommand(commandType);
    }

    private void sendMessage(BaseCLIToServer message) throws IOException {
        inputOutput.sendMessage(message);
    }

    private BaseServerToCLI receiveMessage() throws IOException, ClassNotFoundException {
        return inputOutput.receiveMessage();
    }

    private int getOptionFromUser() {
        int option = consoleInputReader.readIntFromUser(PLEASE_ENTER_YOUR_COMMAND);
        while (commands.getCommand(option) == null) {
            option = consoleInputReader.readIntFromUser(PLEASE_CHOOSE_VALID_NUMBER);
        }
        return option;
    }

    private void show() {
        System.out.println(AVAILABLE_COMMANDS);
        System.out.println(commands);
    }

    private void displayStatuses() throws IOException, ClassNotFoundException {
        BaseCLIToServer displayStatusesRequest = displayClientsStatusCommand.getMessage();
        inputOutput.sendMessage(displayStatusesRequest);
        BaseServerToCLI Statuses = inputOutput.receiveMessage();
        displayClientsStatusCommand.printResponse(Statuses);
    }

    private boolean isKeepRunningCommand(int option) {
        return commands.getCommand(option).isKeepRunningCommand();
    }
}
