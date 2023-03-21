package com.company.commands;

import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

/**
 * This interface provides a common structure for all commands in the system
 * and allows for easy implementation of new commands as needed.
 * <p>
 * The getMessage() method returns a BaseCLIToServer object that represents the user's request,
 * while the printResponse() method takes a BaseServerToCLI object that represents the server's
 * response and prints it to the console.
 * <p>
 * The getClassName() method returns a String representing the name of the class that implements
 * the Command interface.
 * The getCommandName() method returns a String representing the name of the command,
 * and the isKeepRunningCommand() method returns a boolean value indicating whether the
 * command should continue running after it has been executed.
 */
public interface Command {

    BaseCLIToServer getMessage();

    void printResponse(BaseServerToCLI response);

    String getCommandName();

    String getCommandDescription();

    boolean isKeepRunningCommand();
}
