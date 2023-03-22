package com.company.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommandsTest {

    private Commands commands;

    @Mock
    private Command command1;

    @Mock
    private Command command2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commands = new Commands();
    }

    @Test
    void addCommand() {
        //no given
        String command1Name = "command1";
        when(command1.getCommandDescription()).thenReturn("Command 1");
        when(this.command1.getCommandName()).thenReturn(command1Name);

        //when
        Integer commandId = commands.addCommand(this.command1);

        //then
        assertNotNull(commandId);
        assertEquals(this.command1, commands.getCommand(commandId));
        assertEquals(command1, commands.getCommand(command1Name));
        assertEquals("1. Command 1", commands.toString().trim());
    }

    @Test
    void addCommands() {
        //given
        when(command1.getCommandName()).thenReturn("command1");
        when(command1.getCommandDescription()).thenReturn("Command 1");
        when(command2.getCommandName()).thenReturn("command2");
        when(command2.getCommandDescription()).thenReturn("Command 2");
        List<Command> commandList = new ArrayList<>();
        commandList.add(command1);
        commandList.add(command2);

        //when
        List<Integer> addedIds = commands.addCommands(commandList);

        //then
        assertNotNull(addedIds);
        assertEquals(2, addedIds.size());
        assertEquals("1. Command 1\n2. Command 2", commands.toString().trim());
    }

    @Test
    void getCommandById() {
        //given
        when(command1.getCommandName()).thenReturn("command1");
        Integer commandId = commands.addCommand(command1);

        //when
        Command retrievedCommand = commands.getCommand(commandId);

        //then
        assertEquals(command1, retrievedCommand);
    }

    @Test
    void getCommandByName() {
        //given
        when(command1.getCommandName()).thenReturn("command1");
        commands.addCommand(command1);

        //when
        Command retrievedCommand = commands.getCommand("command1");

        //then
        assertEquals(command1, retrievedCommand);
    }
}