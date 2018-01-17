package com.nothardcoded.sceneserver.scene.model.property;

/**
 * Created by nick.tarsillo on 1/11/18.
 */
public interface Property <T> {
  T getValue ();

  T setValue (T val);

  String getName ();

  String setName (String name);
}
