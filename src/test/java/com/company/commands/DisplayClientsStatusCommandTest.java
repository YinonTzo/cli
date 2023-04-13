package com.company.commands;

import com.company.commands.constantCommands.DisplayClientsStatusCommand;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.ClientsAndStatuses;
import com.company.common.statuses.ClientAndServerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DisplayClientsStatusCommandTest {

    Command displayClientsStatusCommand;

    @BeforeEach
    void setUp() {
        displayClientsStatusCommand = new DisplayClientsStatusCommand();
    }

    @Test
    void getMessage() {
        //no given

        //when
        BaseCLIToServer message = displayClientsStatusCommand.getMessage();

        //then
        assertEquals("DisplayClientsStatus", message.getType());
    }

    @Test
    void printResponseThereAreClients() {
        //given
        Map<Long, ClientAndServerStatus> clientsAndStatusesMap = new HashMap<>();
        clientsAndStatusesMap.put(1L, ClientAndServerStatus.AVAILABLE);
        clientsAndStatusesMap.put(2L, ClientAndServerStatus.UNAVAILABLE);
        ClientsAndStatuses response = new ClientsAndStatuses();
        response.setClientsAndStatuses(clientsAndStatusesMap);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayClientsStatusCommand.printResponse(response);

        //then
        String expectedOutput = DisplayClientsStatusCommand.SERVER_STATUS + "\r\n" +
                DisplayClientsStatusCommand.ALL_CLIENTS_AND_THEIR_STATUSES + "\r\n" +
                String.format(
                        DisplayClientsStatusCommand.CLIENT_AND_STATUS, 1, ClientAndServerStatus.AVAILABLE) + "\r\n" +
                String.format(
                        DisplayClientsStatusCommand.CLIENT_AND_STATUS, 2, ClientAndServerStatus.UNAVAILABLE) + "\r\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void printResponseNoClients() {
        // given
        ClientsAndStatuses response = new ClientsAndStatuses();
        Map<Long, ClientAndServerStatus> clientsAndStatusesMap = new HashMap<>();
        response.setClientsAndStatuses(clientsAndStatusesMap);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        displayClientsStatusCommand.printResponse(response);

        // then
        String expectedOutput = DisplayClientsStatusCommand.SERVER_STATUS + "\r\n" +
                DisplayClientsStatusCommand.THERE_ARE_NO_CLIENTS_YET + "\r\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void getClassName() {
        //no given

        //when
        String className = displayClientsStatusCommand.getCommandName();

        //then
        assertEquals("DisplayClientsStatus", className);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayClientsStatusCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}