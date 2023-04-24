package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
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

class RemoveClientCommandTest {

    @Mock
    ConsoleInputReader mockConsoleInputReader;

    Command removeClientCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        removeClientCommand = new RemoveClientCommand(mockConsoleInputReader);
    }

    @Test
    void getMessage() {
        // given
        List<Integer> requestIds = Arrays.asList(1, 2, 3);
        when(mockConsoleInputReader.readClientsIdsFromUser()).thenReturn(requestIds);

        // when
        BaseCLIToServer message = removeClientCommand.getMessage();

        // then
        assertEquals("RemoveClient", message.getType());
        assertEquals(requestIds, message.getRequestIds());
    }

    @Test
    void testPrintResponseWithSuccessResponse() {
        // given
        Map<Long, String> clientIdToAck = new HashMap<>();
        long clientId1 = 1L;
        long clientId2 = 2L;
        String client_removed_successfully = "Client removed successfully";
        clientIdToAck.put(clientId1, client_removed_successfully);
        clientIdToAck.put(clientId2, null);

        SendPayload response = new SendPayload();
        response.setClientIdToAck(clientIdToAck);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        removeClientCommand.printResponse(response);

        // then
        String expectedOutput = String.format(
                RemoveClientCommand.REMOVE_CLIENT_SUCCESSFULLY, client_removed_successfully, clientId1
        ) + "\r\n"
                + String.format(RemoveClientCommand.CLIENT_DOES_NOT_EXIST_OR_CONNECTED, clientId2) + "\r\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testPrintResponseWithFailedResponse() {
        // given
        SendPayload response = new SendPayload();
        response.setClientIdToAck(null);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        removeClientCommand.printResponse(response);

        // then
        String expectedOutput = "Failed to send messages, Please do exit.\r\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void getClassName() {
        // no given

        // when
        String commandName = removeClientCommand.getCommandName();

        // then
        assertEquals("RemoveClient", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        // no given

        // when
        boolean isRunning = removeClientCommand.isKeepRunningCommand();

        // then
        assertTrue(isRunning);
    }
}
