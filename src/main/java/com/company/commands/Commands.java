package com.company.commands;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of available commands that can be executed by a command-line interface.
 * <p>
 * Commands are identified by a unique integer ID and a string command name.
 * New commands can be added to the collection, and existing commands can be retrieved by their ID or name.
 */

@Slf4j
public class Commands {

    public static final String ID_AND_DESCRIPTION = "%d. %s";

    private final Map<Integer, Command> commandIdToCommand;
    private final Map<String, Command> commandNameToCommand;
    Integer commandId;

    public Commands() {
        this.commandIdToCommand = new HashMap<>();
        this.commandNameToCommand = new HashMap<>();
        commandId = 0;
    }

    public Integer addCommand(Command command) {
        commandIdToCommand.put(++commandId, command);
        commandNameToCommand.put(command.getCommandName(), command);
        log.info("Added new command: <{}, {}>", commandId, command.getCommandDescription());
        return commandId;
    }

    public List<Integer> addCommands(List<Command> commands) {
        List<Integer> addedIds = new ArrayList<>();
        for (Command command : commands) {
            addedIds.add(addCommand(command));
        }
        return addedIds;
    }

    public Command getCommand(Integer id) {
        if (id == null)
            return null;

        return commandIdToCommand.get(id);
    }

    public Command getCommand(String commandName) {
        if (commandName == null)
            return null;

        return commandNameToCommand.get(commandName);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, Command> mapEntry : commandIdToCommand.entrySet()) {
            stringBuilder.append(String.format(ID_AND_DESCRIPTION, mapEntry.getKey(), mapEntry.getValue().getCommandDescription()));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
