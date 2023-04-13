package com.company.plugins;

import com.company.commands.Command;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.CommandResults;

import java.util.List;
import java.util.Map;

/**
 * The "DisplayResultsCommand" class is a Java class that implements the "Command" interface.
 * This class is responsible for displaying the results of a command executed on the server.
 * It takes an instance of ConsoleInputReader as a constructor parameter,
 * which is used to read the command result numbers from the user.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the requested command results.
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response and prints
 * the execution data associated with the requested command results to the console.
 * If there are no execution data for the requested results or the requested results don't exist,
 * an appropriate message is displayed.
 */
public class DisplayResultsCommand implements Command {

    public static final String PAYLOADS = "payloads results: %s";
    public static final String THERE_IS_NO_PAYLOAD = "There is no payload %s.";
    public static final String COMMAND_NAME = "DisplayResults";
    public static final String COMMAND_DESCRIPTION = "Display Result.";

    private final ConsoleInputReader consoleInputReader;

    public DisplayResultsCommand(ConsoleInputReader consoleInputReader) {
        this.consoleInputReader = consoleInputReader;
    }

    @Override
    public BaseCLIToServer getMessage() {
        BaseCLIToServer message = new BaseCLIToServer(getCommandName());

        message.setRequestIds(consoleInputReader.getResultsNumbersFromUser());

        return message;
    }

    @Override
    public void printResponse(BaseServerToCLI response) {
        CommandResults commandResults = (CommandResults) response;

        for (Map.Entry<Integer, List<ExecutionData>> entry : commandResults.getPayloadIdToResult().entrySet()) {
            if (entry.getValue() != null) {
                System.out.println(String.format(PAYLOADS, entry.getValue()));
            } else {
                System.out.println(String.format(THERE_IS_NO_PAYLOAD, entry.getKey()));
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
