package com.company.commands.constantCommands;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientsAndStatuses;
import com.company.common.statuses.ClientAndServerStatus;

import java.util.Map;

/**
 * The "DisplayClientsStatusCommand" class is a Java class that implements the "Command" interface.
 * This class is responsible for displaying the status of all clients currently connected to the server.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the message sent by the client to request
 * the status of all connected clients.
 * <p>
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response
 * and prints the status of all connected clients to the console.
 * If no clients are connected, a message indicating that there are no clients yet is displayed.
 * If there are connected clients, a message indicating that all clients
 * and their statuses will be displayed is shown followed by the status of each client.
 */
public class DisplayClientsStatusCommand implements Command {

    public static final String CLIENT_AND_STATUS = "Client Id: %d. status: %s";
    public static final String THERE_ARE_NO_CLIENTS_YET = "There are no clients yet.";
    public static final String SERVER_STATUS = "Server Status: online";
    public static final String ALL_CLIENTS_AND_THEIR_STATUSES = "All clients and their statuses:";
    public static final String COMMAND_NAME = "DisplayClientsStatus";
    public static final String COMMAND_DESCRIPTION = "Display Clients Status Command.";

    @Override
    public BaseCLIToServer getMessage() {
        return new BaseCLIToServer(getCommandName());
    }

    @Override
    public void printResponse(BaseServerToCLI response) {
        ClientsAndStatuses clientsAndStatuses = (ClientsAndStatuses) response;

        System.out.println(SERVER_STATUS);

        Map<Long, ClientAndServerStatus> clientsAndStatusesMap = clientsAndStatuses.getClientsAndStatuses();
        if (clientsAndStatusesMap.isEmpty())
            System.out.println(THERE_ARE_NO_CLIENTS_YET);
        else {
            System.out.println(ALL_CLIENTS_AND_THEIR_STATUSES);
            for (Map.Entry<Long, ClientAndServerStatus> entry : clientsAndStatusesMap.entrySet()) {
                System.out.println(String.format(CLIENT_AND_STATUS, entry.getKey(), entry.getValue()));
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
