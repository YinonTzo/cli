package com.company.CLI;

import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

import java.io.IOException;

public interface SocketInputOutput {

    void sendMessage(BaseCLIToServer message) throws IOException;

    BaseServerToCLI receiveMessage() throws IOException, ClassNotFoundException;
}
