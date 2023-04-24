package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.inputReader.ConsoleInputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        when(mockConsoleInputReader.yesNoQuestion(SendPayloadCommand.ADD_ARGUMENTS_PROMPT_MESSAGE)).thenReturn(true, true, false);
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
    void testPrintResponseWithSuccessResponse() {
        // given
        SendPayload sendPayload = new SendPayload();
        Map<Long, String> clientIdToAck = new HashMap<>();
        long clientId1 = 1L;
        long clientId2 = 2L;
        long clientId3 = 3L;
        String ack1 = "1";
        String ack2 = "2";
        clientIdToAck.put(clientId1, ack1);
        clientIdToAck.put(clientId2, ack2);
        clientIdToAck.put(clientId3, null);
        sendPayload.setClientIdToAck(clientIdToAck);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        sendPayloadCommand.printResponse(sendPayload);

        // then
        String expectedOutput = String.format(SendPayloadCommand.SENT_PAYLOAD_ACK_MESSAGE, ack1, clientId1) + "\r\n"
                + String.format(SendPayloadCommand.SENT_PAYLOAD_ACK_MESSAGE, ack2, clientId2) + "\r\n"
                + String.format(SendPayloadCommand.CLIENT_DOES_NOT_EXIST_OR_CONNECTED, clientId3) + "\r\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrintResponseWithFailedResponse() {
        // given
        SendPayload sendPayload = new SendPayload();
        sendPayload.setClientIdToAck(null);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        sendPayloadCommand.printResponse(sendPayload);

        // then
        String expectedOutput = "Failed to send messages, Please do exit.\r\n";
        assertEquals(expectedOutput, outContent.toString());
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