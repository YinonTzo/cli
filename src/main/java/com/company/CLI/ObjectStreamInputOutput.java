package com.company.CLI;

import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The "ObjectStreamInputOutput" class is a proxy class for the socket's input and output.
 */
public class ObjectStreamInputOutput implements SocketInputOutput {
    public final ObjectInputStream in;
    private final ObjectOutputStream out;

    public ObjectStreamInputOutput(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void sendMessage(BaseCLIToServer message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public BaseServerToCLI receiveMessage() throws IOException, ClassNotFoundException {
        return (BaseServerToCLI) in.readObject();
    }
}
