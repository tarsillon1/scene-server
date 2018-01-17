package com.nothardcoded.sceneserver.scene.model.strategy;

/**
 * Created by nick.tarsillo on 1/12/18.
 */
public interface UpdateStrategy<T> {
  void updated (T object);
}
