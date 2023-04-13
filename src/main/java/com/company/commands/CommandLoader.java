package com.company.commands;

import com.company.inputReader.ConsoleInputReader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * The "CommandLoader" class is responsible for loading all available Command classes from the plugins directory
 * and instantiating them with an ConsoleInputReader object.
 * It has a single public method called loadCommands which takes an ConsoleInputReader as an argument
 * and returns a list of Command objects.
 * <p>
 * The loadCommands method first creates a File object for the plugins directory and
 * gets all the files in that directory.
 * Then, it loops through each file and tries to load the corresponding Command class by creating the fully
 * qualified class name from the file name, getting the class object using the
 * Class.getName method, and then getting the constructor that takes an ConsoleInputReader parameter.
 * Finally, it instantiates the Command object using the constructor and adds it to the list of Command objects.
 */

@Slf4j
public class CommandLoader {
    public static List<Command> loadCommands(ConsoleInputReader consoleInputReader) {
        List<Command> commands = new ArrayList<>();

        // Load all command classes in the plugins directory
        File pluginsDir = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\company\\plugins");
        File[] pluginFiles = pluginsDir.listFiles();
        if (pluginFiles != null) {
            for (File pluginFile : pluginFiles) {
                try {
                    String className = "com.company.plugins." + pluginFile.getName().replace(".java", "");
                    Class<?> commandClass = Class.forName(className);
                    Constructor<?> constructor = commandClass.getConstructor(ConsoleInputReader.class);
                    Command command = (Command) constructor.newInstance(consoleInputReader);
                    commands.add(command);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    log.error("An error occurred while load commands " + e);
                }
            }
        }
        return commands;
    }
}