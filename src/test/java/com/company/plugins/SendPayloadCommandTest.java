package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToCLI.TextMessage;
import com.company.inputReader.ConsoleInputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SendPayloadCommandTest {

    @Mock
    ConsoleInputReader mockConsoleInputReader;

    Command sendPayloadCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sendPayloadCommand = new SendPayloadCommand(mockConsoleInputReader);
    }

    @Test
    void getMessageReturnsExpectedMessage() {
        //given
        List<Integer> requestIds = Arrays.asList(1, 2, 3);
        String fileName = "test";
        String fileContent = "public class Test { public static void main(String[] args) { System.out.println(\"Test\"); } }";
        when(mockConsoleInputReader.getStringFromUser(SendPayloadCommand.PLEASE_ENTER_FILE_NAME_PROMPT_MESSAGE)).thenReturn(fileName);
        when(mockConsoleInputReader.getStringFromUser(SendPayloadCommand.ENTER_THE_JAVA_CODE_PROMPT_MESSAGE)).thenReturn(fileContent);
        when(mockConsoleInputReader.getStringFromUser()).thenReturn("done");
        when(mockConsoleInputReader.yesNoQuestion(SendPayloadCommand.KEEP_READING_PROMPT_MESSAGE)).thenReturn(false);
        when(mockConsoleInputReader.yesNoQuestion(SendPayloadCommand.ADD_ARGUMENTS_PROMPT_MESSAGE)).thenReturn(true, true ,false);
        when(mockConsoleInputReader.getStringFromUser(SendPayloadCommand.ENTER_ARGUMENTS_PROMPT_MESSAGE)).thenReturn("arg1", "arg2", "");
        when(mockConsoleInputReader.readClientsIdsFromUser()).thenReturn(requestIds);

        //when
        BaseCLIToServer message = sendPayloadCommand.getMessage();

        //then
        assertEquals("SendPayloadCommand", message.getType());
        PayloadCLIToServer payloadMessage = (PayloadCLIToServer) message;
        assertEquals(Arrays.asList(1, 2, 3), payloadMessage.getRequestIds());
        String expectedPayload = fileName + SendPayloadCommand.FILE_NAME_DELIMITER + fileContent + "\n" + SendPayloadCommand.EOF_DELIMITER;
        assertEquals("arg1 arg2", payloadMessage.getArguments());
    }

    @Test
    void printResponse() {
        //given
        String expectedText = "sent payloads";
        TextMessage response = new TextMessage();
        response.setText(expectedText);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        sendPayloadCommand.printResponse(response);

        //then
        assertEquals(expectedText + "\r\n", outContent.toString());
    }

    @Test
    void getClassName() {
        //no given

        //when
        String commandName = sendPayloadCommand.getCommandName();

        //then
        assertEquals("SendPayloadCommand", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = sendPayloadCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}