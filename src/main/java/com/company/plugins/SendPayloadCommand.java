package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.inputReader.ConsoleInputReader;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The SendPayloadCommand class is a Java class that implements the Command interface.
 * It takes an instance of ConsoleInputReader as a constructor parameter, which is used to read user input for creating
 * a Java command payload to be sent to the server.
 * <p>
 * The class contains methods for creating the Java command payload, reading the file name and code from the user,
 * and reading program arguments.
 * The getMessage() method returns a BaseCLIToServer object that represents the payload to be sent to the
 * server along with any program arguments and client IDs that are read from the user input.
 * The printResponse() method takes a BaseServerToCLI object that represents the server's response and prints the text
 * message associated with the payload submission to the console.
 * <p>
 * Overall, the SendPayloadCommand class is responsible for collecting user input and formatting it as a Java command
 * payload to be sent to the server. It allows users to create and submit a custom payload along with any
 * necessary program arguments for server processing.
 */
public class SendPayloadCommand implements Command {
    public static final String FILE_NAME_DELIMITER = "file_name";
    public static final String EOF_DELIMITER = "EOF";
    public static final String KEEP_READING_PROMPT_MESSAGE = "Do you want to add another file? (y/n) ";
    public static final String ADD_ARGUMENTS_PROMPT_MESSAGE = "Do you want to add program arguments? (y/n)";
    public static final String ENTER_ARGUMENTS_PROMPT_MESSAGE = "Please enter an argument: ";
    public static final String COMMENT = "//";
    public static final String ENTER_THE_JAVA_CODE_PROMPT_MESSAGE = "Enter the java code or done to finish:";
    public static final String PLEASE_ENTER_FILE_NAME_PROMPT_MESSAGE = "Please Enter the file name: ";
    public static final String ARGUMENT_DELIMITER = " ";
    public static final String COMMAND_NAME = "Send Payload.";
    public static final String COMMAND_DESCRIPTION = "SendPayloadCommand";

    private final ConsoleInputReader consoleInputReader;

    public SendPayloadCommand(ConsoleInputReader consoleInputReader) {
        this.consoleInputReader = consoleInputReader;
    }

    @Override
    public BaseCLIToServer getMessage() {
        PayloadCLIToServer message = new PayloadCLIToServer(getCommandName());
        message.setPayload(getPayload());
        message.setArguments(readArguments());
        message.setRequestIds(consoleInputReader.readClientsIdsFromUser());
        return message;
    }

    @Override
    public void printResponse(BaseServerToCLI response) {
        SendPayload sendPayloadResponse = (SendPayload) response;

        Map<Long, String> clientIdToAck = sendPayloadResponse.getClientIdToAck();
        if (clientIdToAck == null) {
            System.out.println("Failed to send messages, Please do exit.");
        } else {
            for (Map.Entry<Long, String> entry : clientIdToAck.entrySet()) {
                if (entry.getValue() != null) {
                    System.out.println(entry.getValue() + " to client number " + entry.getKey() + ".");
                } else {
                    System.out.println(entry.getKey() + " does not exist or connected.");
                }
            }
        }
    }

    @Override
    public String getCommandDescription() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandName() {
        return COMMAND_DESCRIPTION;
    }

    @Override
    public boolean isKeepRunningCommand() {
        return true;
    }

    private byte[] getPayload() {
        return createJavaCommand().getBytes(StandardCharsets.UTF_8);
    }

    private String createJavaCommand() {
        StringBuilder files = new StringBuilder();

        do {
            files.append(readFileName());
            files.append(FILE_NAME_DELIMITER);

            files.append(readCodeIntoStringBuilder());
            files.append(EOF_DELIMITER);

        } while (consoleInputReader.yesNoQuestion(KEEP_READING_PROMPT_MESSAGE));

        return files.toString();
    }

    private String readFileName() {
        return consoleInputReader.getStringFromUser(PLEASE_ENTER_FILE_NAME_PROMPT_MESSAGE);
    }

    private StringBuilder readCodeIntoStringBuilder() {
        StringBuilder code = new StringBuilder();

        String input = consoleInputReader.getStringFromUser(ENTER_THE_JAVA_CODE_PROMPT_MESSAGE);

        while (!input.equals(ConsoleInputReader.DONE)) {
            if (!input.trim().startsWith(COMMENT))
                code.append(input);

            input = consoleInputReader.getStringFromUser();
        }
        return code;
    }

    private String readArguments() {
        List<String> arguments = new ArrayList<>();

        while (consoleInputReader.yesNoQuestion(ADD_ARGUMENTS_PROMPT_MESSAGE)) {
            String argument = consoleInputReader.getStringFromUser(ENTER_ARGUMENTS_PROMPT_MESSAGE);
            arguments.add(argument);
        }
        return String.join(ARGUMENT_DELIMITER, arguments);
    }
}