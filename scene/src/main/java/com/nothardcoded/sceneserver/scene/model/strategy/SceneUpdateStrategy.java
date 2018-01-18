package com.nothardcoded.sceneserver.scene.model.strategy;

import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;

/**
 * Created by nick.tarsillo on 1/13/18.
 */
public class SceneUpdateStrategy {
  private UpdateStrategy<SceneObject> newObject;
  private UpdateStrategy<SceneObject> objectUpdated;
  private UpdateStrategy<SceneObjectProperty> newProperty;
  private UpdateStrategy<SceneObjectProperty> propertyUpdateStrategy;

  public UpdateStrategy<SceneObject> getNewObject() {
    return newObject;
  }

  public void setNewObject(
      UpdateStrategy<SceneObject> newObject) {
    this.newObject = newObject;
  }

  public UpdateStrategy<SceneObject> getObjectUpdated() {
    return objectUpdated;
  }

  public void setObjectUpdated(
      UpdateStrategy<SceneObject> objectUpdated) {
    this.objectUpdated = objectUpdated;
  }

  public UpdateStrategy<SceneObjectProperty> getNewProperty() {
    return newProperty;
  }

  public void setNewProperty(
      UpdateStrategy<SceneObjectProperty> newProperty) {
    this.newProperty = newProperty;
  }

  public UpdateStrategy<SceneObjectProperty> getPropertyUpdateStrategy() {
    return propertyUpdateStrategy;
  }

  public void setPropertyUpdateStrategy(UpdateStrategy<SceneObjectProperty> propertyUpdateStrategy) {
    this.propertyUpdateStrategy = propertyUpdateStrategy;
  }
}
