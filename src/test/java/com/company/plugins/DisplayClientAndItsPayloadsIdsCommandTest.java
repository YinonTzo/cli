package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.ClientIdAndPayloadsIds;
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

class DisplayClientAndItsPayloadsIdsCommandTest {

    @Mock
    ConsoleInputReader mockConsoleInputReader;

    Command displayClientAndItsPayloadsIdsCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        displayClientAndItsPayloadsIdsCommand = new DisplayClientAndItsPayloadsIdsCommand(mockConsoleInputReader);
    }

    @Test
    void getMessage() {
        //given
        List<Integer> requestIds = Arrays.asList(1, 2, 3);
        when(mockConsoleInputReader.readClientsIdsFromUser()).thenReturn(requestIds);

        //when
        BaseCLIToServer message = displayClientAndItsPayloadsIdsCommand.getMessage();

        //then
        assertEquals("DisplayClientAndItsPayloadsIds", message.getType());
        assertEquals(requestIds, message.getRequestIds());
    }

    @Test
    void printResponseClientWithPayloads() {
        //given
        Map<String, String> clientAndPayloads = new HashMap<>();
        String client1 = "client1";
        String payloads1 = "payload1,payload2,payload3";
        clientAndPayloads.put(client1, payloads1);
        ClientIdAndPayloadsIds response = new ClientIdAndPayloadsIds();
        response.setClientAndPayloads(clientAndPayloads);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayClientAndItsPayloadsIdsCommand.printResponse(response);

        //then
        assertEquals(
                String.format(DisplayClientAndItsPayloadsIdsCommand.CLIENT_AND_PAYLOADS_NUMBERS
                        , client1
                        , payloads1
                )+ "\r\n"
                , outContent.toString()
        );
    }

    @Test
    void printResponseWithWrongNoClient() {
        //given
        Map<String, String> clientAndPayloads = new HashMap<>();
        String client1 = "client1";
        String missingClient = "";
        clientAndPayloads.put(client1, missingClient);
        ClientIdAndPayloadsIds response = new ClientIdAndPayloadsIds();
        response.setClientAndPayloads(clientAndPayloads);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayClientAndItsPayloadsIdsCommand.printResponse(response);

        //then
        assertEquals(
                String.format(DisplayClientAndItsPayloadsIdsCommand.THERE_IS_NOT_CLIENT,
                        client1) + "\r\n"
                , outContent.toString()
        );
    }

    @Test
    void printResponseNoClients() {
        //given
        Map<String, String> clientAndPayloads = new HashMap<>();
        String client1 = "client1";
        String noPayload = "[]";
        clientAndPayloads.put(client1, noPayload);
        ClientIdAndPayloadsIds response = new ClientIdAndPayloadsIds();
        response.setClientAndPayloads(clientAndPayloads);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayClientAndItsPayloadsIdsCommand.printResponse(response);

        //then
        assertEquals(
                String.format(DisplayClientAndItsPayloadsIdsCommand.THERE_ARE_NO_PAYLOADS,
                        client1) + "\r\n"
                , outContent.toString()
        );
    }

    @Test
    void printResponseClientWithNoPayloads() {
        //given
        Map<String, String> emptyClientAndPayloads = new HashMap<>();
        ClientIdAndPayloadsIds response = new ClientIdAndPayloadsIds();
        response.setClientAndPayloads(emptyClientAndPayloads);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayClientAndItsPayloadsIdsCommand.printResponse(response);

        //then
        assertEquals(
                DisplayClientAndItsPayloadsIdsCommand.THERE_ARE_NO_CLIENTS_AND_PAYLOADS + "\r\n"
                , outContent.toString()
        );
    }

    @Test
    void getClassName() {
        //no given

        //when
        String commandName = displayClientAndItsPayloadsIdsCommand.getCommandName();

        //then
        assertEquals("DisplayClientAndItsPayloadsIds", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayClientAndItsPayloadsIdsCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}