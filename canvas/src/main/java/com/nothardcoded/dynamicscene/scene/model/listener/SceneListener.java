package com.nothardcoded.dynamicscene.scene.model.listener;

import com.nothardcoded.dynamicscene.scene.model.object.SceneObject;
import com.nothardcoded.dynamicscene.scene.model.property.SceneObjectProperty;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
public interface SceneListener {
  void newProperty(SceneObjectProperty sceneObjectProperty);
  void propertyUpdated (SceneObjectProperty objectProperty);
  void newObject(SceneObject object);
  void objectUpdated (SceneObject sceneObject);
}
