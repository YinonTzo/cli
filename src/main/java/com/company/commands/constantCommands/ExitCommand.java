package com.company.commands.constantCommands;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The ExitCommand class is a Java class that implements the Command interface.
 * This class is responsible for handling the "Exit" command, which is used to terminate the application.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the command to the server to terminate
 * the client application.
 * It sets the requestIds to -1, indicating that the command is targeting all clients.
 * <p>
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response,
 * which is a TextMessage object.
 * The method prints the text message received from the server to the console.
 */
public class ExitCommand implements Command {

    public static final String COMMAND_NAME = "ExitCommand";
    public static final String COMMAND_DESCRIPTION = "Exit.";

    @Override
    public BaseCLIToServer getMessage() {
        BaseCLIToServer message = new BaseCLIToServer(getCommandName());

        message.setRequestIds(getAllClients());

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
        return false;
    }

    private List<Integer> getAllClients() {
        List<Integer> allClients = new ArrayList<>();
        allClients.add(-1);
        return allClients;
    }
}
