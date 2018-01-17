package com.nothardcoded.sceneserver.server.model.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by nick.tarsillo on 1/9/18.
 */
public abstract class ServerConnectionListener extends Thread {
  private static Logger LOG = LoggerFactory.getLogger(ServerConnectionListener.class);

  private ServerSocket socket;

  public ServerConnectionListener (ServerSocket socket) throws IOException {
    this.socket = socket;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Socket connectionSocket = socket.accept();
        onConnection(connectionSocket);
      } catch (IOException e) {
        LOG.error("Failed to accept incoming client connection: ", e);
      }
    }
  }

  public abstract void onConnection(Socket socket);
}
