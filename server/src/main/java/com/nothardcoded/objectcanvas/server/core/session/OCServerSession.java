package com.nothardcoded.objectcanvas.server.core.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by nick.tarsillo on 1/9/18.
 */
public class OCServerSession extends Thread {
  private static Logger LOG = LoggerFactory.getLogger(OCServerSession.class);

  private Socket socket;
  private BufferedReader inputStream;
  private PrintStream outputStream;

  public OCServerSession(Socket socket) throws IOException {
    this.socket = socket;
    inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    outputStream = new PrintStream(socket.getOutputStream());
  }

  @Override
  public void run() {
    while (true) {
      try {
        String line = inputStream.readLine();
      } catch (IOException e) {
        LOG.error("Failed to read line for server session: ", e);
      }
    }
  }
}
