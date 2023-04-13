package com.company.plugins;

import com.company.commands.Command;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientIdAndPayloadsIds;

import java.util.Map;

/**
 * The "DisplayClientAndItsPayloadsIdsCommand" class is a Java class that implements the "Command" interface.
 * This class is responsible for displaying the payloads associated with a specific client ID.
 * It takes an instance of ConsoleInputReader as a constructor parameter,
 * which is used to read the client ID from the user.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the wanted clients.
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response and prints
 * the payloads associated with the client ID to the console.
 * If there are no payloads for the client ID or there isn't a client with the given ID,
 * an appropriate message is displayed.
 * If there are no clients and payloads, another message is displayed.
 */
public class DisplayClientAndItsPayloadsIdsCommand implements Command {

    public static final String CLIENT_AND_PAYLOADS_NUMBERS = "Client Id: %s has payloads numbers: %s";
    public static final String THERE_ARE_NO_PAYLOADS = "To client %s there are no payloads.";
    public static final String THERE_IS_NOT_CLIENT = "There isn't client with Id %s yet.";
    public static final String THERE_ARE_NO_CLIENTS_AND_PAYLOADS = "There are no clients and payloads yet.";
    public static final String COMMAND_NAME = "DisplayClientAndItsPayloadsIds";
    public static final String COMMAND_DESCRIPTION = "Display Client And Its Payloads Ids.";
    public static final String EMPTY_LIST = "[]";

    private final ConsoleInputReader consoleInputReader;

    public DisplayClientAndItsPayloadsIdsCommand(ConsoleInputReader consoleInputReader) {
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
        ClientIdAndPayloadsIds clientIdAndPayloadsIds = (ClientIdAndPayloadsIds) response;
        if (clientIdAndPayloadsIds.getClientAndPayloads().isEmpty()) {
            System.out.println(THERE_ARE_NO_CLIENTS_AND_PAYLOADS);
        } else {
            for (Map.Entry<String, String> entry : clientIdAndPayloadsIds.getClientAndPayloads().entrySet()) {
                String wantedClientId = entry.getKey();

                if (entry.getValue().isEmpty()) {
                    System.out.println(String.format(THERE_IS_NOT_CLIENT, wantedClientId));
                } else if (entry.getValue().equals(EMPTY_LIST)) {
                    System.out.println(String.format(THERE_ARE_NO_PAYLOADS, wantedClientId));
                } else {
                    System.out.println(String.format(CLIENT_AND_PAYLOADS_NUMBERS, wantedClientId, entry.getValue()));
                }
            }
        }
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
