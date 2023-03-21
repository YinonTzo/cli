package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
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
        //given
        List<Integer> requestIds = Arrays.asList(1, 2, 3);
        when(mockConsoleInputReader.readClientsIdsFromUser()).thenReturn(requestIds);

        //when
        BaseCLIToServer message = removeClientCommand.getMessage();

        //then
        assertEquals("RemoveClient", message.getType());
        assertEquals(requestIds, message.getRequestIds());
    }

    @Test
    void printResponse() {
        //given
        String expectedText = "Goodbye!";
        TextMessage response = new TextMessage();
        response.setText(expectedText);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        removeClientCommand.printResponse(response);

        //then
        assertEquals(expectedText + "\r\n", outContent.toString());
    }

    @Test
    void getClassName() {
        //no given

        //when
        String commandName = removeClientCommand.getCommandName();

        //then
        assertEquals("RemoveClient", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = removeClientCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}