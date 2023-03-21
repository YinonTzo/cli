package com.company.commands;

import com.company.commands.constantCommands.ExitCommand;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExitCommandTest {

    Command exitCommand;

    @BeforeEach
    void setUp() {
        exitCommand = new ExitCommand();
    }

    @Test
    void getMessage() {
        //no given

        //when
        BaseCLIToServer message = exitCommand.getMessage();

        //then
        assertEquals("ExitCommand", message.getType());
        assertEquals(List.of(-1), message.getRequestIds());
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
        exitCommand.printResponse(response);

        //then
        assertEquals(expectedText + "\r\n", outContent.toString());
    }

    @Test
    void getClassName() {
        //no given

        //when
        String className = exitCommand.getCommandName();

        //then
        assertEquals("ExitCommand", className);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = exitCommand.isKeepRunningCommand();

        //then
        assertFalse(isRunning);
    }
}