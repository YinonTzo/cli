package com.company.inputReader;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputReaderTest {

    @Test
    void readClientsFromUser() {
        //given
        String input = "1\n2\n3\ndone\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        List<Integer> clients = consoleInputReader.readClientsIdsFromUser();

        //then
        assertEquals(3, clients.size());
        assertTrue(clients.contains(1));
        assertTrue(clients.contains(2));
        assertTrue(clients.contains(3));
    }

    @Test
    void readClientsFromUserInvalidInputs() {
        //given
        String input = "1\nbb\ndone\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        List<Integer> clients = consoleInputReader.readClientsIdsFromUser();

        //then
        assertEquals(1, clients.size());
        assertTrue(clients.contains(1));
    }

    @Test
    void getResultsNumbersFromUser() {
        readClientsFromUser();
        readClientsFromUserInvalidInputs();
    }

    @Test
    void readIntFromUser() {
        //given
        String input = "123\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        int result = consoleInputReader.readIntFromUser("Enter a number:");

        //then
        assertEquals(123, result);
    }

    @Test
    void testReadIntFromUserInvalidInput() {
        //given
        String input = "abc\n123\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        int result = consoleInputReader.readIntFromUser("Enter a number:");

        //then
        assertEquals(123, result);
    }

    @Test
    void getStringFromUser() {
        //given
        String input = "Hello World\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        String result = consoleInputReader.getStringFromUser("Enter a string:");

        //then
        assertEquals("Hello World", result);
    }

    @Test
    void yesNoQuestion() {
        //given
        String input = ConsoleInputReader.yes + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        boolean result = consoleInputReader.yesNoQuestion("Do you want to continue?");

        //then
        assertTrue(result);
    }

    @Test
    void testYesNoQuestionInvalidInput() {
        //given
        String input = "invalid\n" + ConsoleInputReader.no + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(scanner);

        //when
        boolean result = consoleInputReader.yesNoQuestion("Do you want to continue?");

        //then
        assertFalse(result);
    }
}