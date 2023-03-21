package com.company.plugins;

import com.company.commands.Command;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;

/**
 * The "RemoveClientCommand" class is a Java class that implements the Command interface.
 * This class is responsible for removing a client from the server.
 * It takes an instance of ConsoleInputReader as a constructor parameter,which is used to read the client IDs
 * to be removed from the user.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the client IDs to be removed.
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response and prints
 * the text message associated with the removal of the clients to the console.
 */
public class RemoveClientCommand implements Command {
    public static final String COMMAND_NAME = "RemoveClient";
    public static final String COMMAND_DESCRIPTION = "Remove Client.";
    private final ConsoleInputReader consoleInputReader;

    public RemoveClientCommand(ConsoleInputReader consoleInputReader) {
        this.consoleInputReader = consoleInputReader;
    }

    @Override
    public BaseCLIToServer getMessage() {
        BaseCLIToServer message = new BaseCLIToServer(getCommandName());

        message.setRequestIds(consoleInputReader.readClientsIdsFromUser());

        return message;
    }

    @Override
    public void printResponse(BaseServerToCLI response) {
        TextMessage textMessage = (TextMessage) response;

        System.out.println(textMessage.getText());
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    @Override
    public boolean isKeepRunningCommand() {
        return true;
    }
}
