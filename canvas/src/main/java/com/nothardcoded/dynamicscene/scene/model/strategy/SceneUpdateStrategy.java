package com.nothardcoded.dynamicscene.scene.model.strategy;

import com.nothardcoded.dynamicscene.scene.model.object.SceneObject;
import com.nothardcoded.dynamicscene.scene.model.property.SceneObjectProperty;

/**
 * Created by nick.tarsillo on 1/13/18.
 */
public class SceneUpdateStrategy {
  private UpdateStrategy<SceneObject> newObject;
  private UpdateStrategy<SceneObject> objectUpdated;
  private UpdateStrategy<SceneObjectProperty> newProperty;
  private UpdateStrategy<SceneObjectProperty> syncPropertyUpdated;
  private UpdateStrategy<SceneObjectProperty> asyncPropertyUpdated;

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

  public UpdateStrategy<SceneObjectProperty> getSyncPropertyUpdated() {
    return syncPropertyUpdated;
  }

  public void setSyncPropertyUpdated(UpdateStrategy<SceneObjectProperty> syncPropertyUpdated) {
    this.syncPropertyUpdated = syncPropertyUpdated;
  }

  public UpdateStrategy<SceneObjectProperty> getAsyncPropertyUpdated() {
    return asyncPropertyUpdated;
  }

  public void setAsyncPropertyUpdated(UpdateStrategy<SceneObjectProperty> asyncPropertyUpdated) {
    this.asyncPropertyUpdated = asyncPropertyUpdated;
  }
}
