package com.company.CLI;

import com.company.commands.menu.Menu;
import com.company.inputReader.ConsoleInputReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * The "CLI" class represents a command-line interface that connects to a remote server
 * using a TCP socket and communicates with it through object streams.
 * It provides methods for starting and stopping the socket connection,
 * as well as running a menu of commands that can be executed on the server.
 * The class uses the Menu class to implement the menu of commands.
 */

@Slf4j
@Getter
@Setter
public class CLI {

    private final String serverIp;
    private final int serverPort;

    private Menu menu;
    private Socket socket;

    public CLI(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    protected void startConnection() throws IOException {
        socket = new Socket(serverIp, serverPort);
        log.info("connected to {}:{}", serverIp, serverPort);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        SocketInputOutput inputOutput = new ObjectStreamInputOutput(in, out);
        ConsoleInputReader consoleInputReader = new ConsoleInputReader(new Scanner(System.in));
        menu = new Menu(inputOutput, consoleInputReader);
    }

    protected void stopConnection() throws IOException {
        log.info("Closing the CLI socket");
        socket.close();
    }

    public void run() {
        try {
            startConnection();
            menu.run();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        } finally {
            try {
                stopConnection();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        log.info("CLI has been finished");
    }
}
