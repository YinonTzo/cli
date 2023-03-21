package com.company;

import com.company.CLI.CLI;
import com.company.config.Configuration;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = Configuration.load();
        String serverIp = properties.getProperty("SERVER_IP");
        String serverPortAsString = properties.getProperty("SERVER_CLI_PORT");
        int serverPort = Integer.parseInt(serverPortAsString);
        CLI cli = new CLI(serverIp, serverPort);
        cli.run();
    }
}
