package com.nothardcoded.dynamicscene.scene.model.property;

import com.nothardcoded.dynamicscene.scene.model.object.SceneObject;
import com.nothardcoded.dynamicscene.scene.model.strategy.UpdateStrategy;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
public final class SceneObjectProperty <T> implements Property<T> {
  private transient SceneObject sceneObject;
  private transient String propertyName;
  private transient UpdateStrategy<SceneObjectProperty<T>> updateStrategy;

  private T propertyValue;

  @Override
  public SceneObjectProperty<T> clone () {
    SceneObjectProperty<T> clone = new SceneObjectProperty<>();
    clone.setValue(propertyValue);
    clone.setName(propertyName);
    clone.setSceneObject(sceneObject);
    clone.setUpdateStrategy(updateStrategy);

    return clone;
  }

  public void setUpdateStrategy(UpdateStrategy<SceneObjectProperty<T>>  updateStrategy) {
    this.updateStrategy = updateStrategy;
  }

  public SceneObject getSceneObject() {
    return sceneObject;
  }

  public void setSceneObject(SceneObject sceneObject) {
    this.sceneObject = sceneObject;
  }

  @Override
  public T getValue() {
    return propertyValue;
  }

  @Override
  public T setValue(T val) {
    this.propertyValue = val;
    if (updateStrategy != null) {
      updateStrategy.updated(this);
    }
    return val;
  }

  public String getName() {
    return propertyName;
  }

  public String setName(String propertyName) {
    this.propertyName = propertyName;
    return propertyName;
  }
}
