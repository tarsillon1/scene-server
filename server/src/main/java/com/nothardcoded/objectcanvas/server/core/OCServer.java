package com.nothardcoded.objectcanvas.server.core;

import com.nothardcoded.dynamicscene.scene.model.listener.SceneListener;
import com.nothardcoded.dynamicscene.scene.model.object.SceneObject;
import com.nothardcoded.dynamicscene.scene.model.property.SceneObjectProperty;
import com.nothardcoded.objectcanvas.server.core.session.OCServerSessionManager;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by nick.tarsillo on 1/9/18.
 */
public class OCServer implements SceneListener {
  private ServerSocket socket;
  private OCServerSessionManager sessionManager;

  public void start (int port) throws IOException {
    socket = new ServerSocket(port);
    sessionManager = new OCServerSessionManager(socket);
  }

  @Override
  public void propertyUpdated(SceneObjectProperty objectProperty) {

  }

  @Override
  public void newObject(SceneObject object) {

  }
}
