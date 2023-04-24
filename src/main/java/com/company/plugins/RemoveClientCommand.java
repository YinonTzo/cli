package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

import java.util.Map;

/**
 * The "RemoveClientCommand" class is a Java class that implements the "Command" interface.
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
    public static final String FAILED_TO_SEND_MESSAGES_PLEASE_DO_EXIT = "Failed to send messages, Please do exit.";
    public static final String CLIENT_DOES_NOT_EXIST_OR_CONNECTED = "Client id: %s does not exist or connected.";
    public static final String REMOVE_CLIENT_SUCCESSFULLY = "%s Client id: %s.";
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
        SendPayload removeClientResponse = (SendPayload) response;

        Map<Long, String> clientIdToAck = removeClientResponse.getClientIdToAck();
        if (clientIdToAck == null) {
            System.out.println(FAILED_TO_SEND_MESSAGES_PLEASE_DO_EXIT);
        } else {
            for (Map.Entry<Long, String> entry : clientIdToAck.entrySet()) {
                if (entry.getValue() != null) {
                    System.out.println(String.format(REMOVE_CLIENT_SUCCESSFULLY, entry.getValue(), entry.getKey()));
                } else {
                    System.out.println(String.format(CLIENT_DOES_NOT_EXIST_OR_CONNECTED, entry.getKey()));
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
