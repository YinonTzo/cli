package com.company.commands;

import com.company.commands.constantCommands.ExitCommand;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.SendPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    void testPrintResponseWithSuccessResponse() {
        // given
        Map<Long, String> clientIdToAck = new HashMap<>();
        clientIdToAck.put(1L, "Client removed successfully");
        clientIdToAck.put(2L, null);

        SendPayload response = new SendPayload();
        response.setClientIdToAck(clientIdToAck);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        exitCommand.printResponse(response);

        // then
        String expectedOutput = "Client removed successfully. Client id: 1.\r\n";
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
        exitCommand.printResponse(response);

        // then
        String expectedOutput = "Failed to send messages, Please do exit.\r\n";
        assertEquals(expectedOutput, outContent.toString());
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