package com.nothardcoded.sceneserver.server.core.session;

import com.nothardcoded.sceneserver.server.model.listener.ServerConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick.tarsillo on 1/9/18.
 */
public class OCServerSessionManager extends ServerConnectionListener {
  private static Logger LOG = LoggerFactory.getLogger(OCServerSessionManager.class);

  private Set<OCServerSession> sessions = new HashSet<OCServerSession>();

  public OCServerSessionManager(ServerSocket socket) throws IOException {
    super(socket);
  }

  public void onConnection(Socket socket) {
    try {
      OCServerSession objectServerSession = new OCServerSession(socket);
      objectServerSession.start();
      sessions.add(objectServerSession);
    } catch (IOException e) {
      LOG.error("Failed to create session for server session manager: ", e);
    }
  }
}
