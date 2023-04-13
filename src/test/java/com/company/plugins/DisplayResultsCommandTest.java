package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.CommandResults;
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

class DisplayResultsCommandTest {

    @Mock
    ConsoleInputReader mockConsoleInputReader;

    Command displayCommandResultCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        displayCommandResultCommand = new DisplayResultsCommand(mockConsoleInputReader);
    }

    @Test
    void getMessage() {
        //given
        List<Integer> requestIds = Arrays.asList(1, 2, 3);
        when(mockConsoleInputReader.getResultsNumbersFromUser()).thenReturn(requestIds);

        //when
        BaseCLIToServer message = displayCommandResultCommand.getMessage();

        //then
        assertEquals("DisplayResults", message.getType());
        assertEquals(requestIds, message.getRequestIds());
    }

    @Test
    void printResponseWithExecutionData() {
        //given
        Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();
        int messageId1 = 1;

        ExecutionData executionData1 = new ExecutionData(messageId1); //received
        ExecutionData executionData2 = new ExecutionData(messageId1); //finished
        List<ExecutionData> executionDataList = Arrays.asList(executionData1, executionData2);

        payloadIdToResult.put(messageId1, executionDataList);
        CommandResults response = new CommandResults();
        response.setPayloadIdToResult(payloadIdToResult);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayCommandResultCommand.printResponse(response);

        //then
        assertEquals(
                String.format(DisplayResultsCommand.PAYLOADS, executionDataList) + "\r\n",
                outContent.toString()
        );
    }

    @Test
    void printResponseWithNoExecutionData() {
        //given
        Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();
        int messageId = 1;
        payloadIdToResult.put(messageId, null);
        CommandResults response = new CommandResults();
        response.setPayloadIdToResult(payloadIdToResult);

        // redirect output to a stream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //when
        displayCommandResultCommand.printResponse(response);

        //then
        assertEquals(
                String.format(DisplayResultsCommand.THERE_IS_NO_PAYLOAD, 1) + "\r\n"
                , outContent.toString()
        );
    }

    @Test
    void getClassName() {
        //no given

        //when
        String commandName = displayCommandResultCommand.getCommandName();

        //then
        assertEquals("DisplayResults", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayCommandResultCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}