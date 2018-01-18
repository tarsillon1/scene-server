package com.nothardcoded.sceneserver.scene.model.property;

import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.strategy.UpdateStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
public final class SceneObjectProperty <T> implements Property<T> {
  private transient SceneObject sceneObject;
  private transient String propertyName;
  private transient Boolean isAsync = false;
  private transient Long asyncUpdateTime;
  private transient Set<UpdateStrategy<SceneObjectProperty<T>>> updateStrategies = new HashSet<>();

  private T propertyValue;

  public void addUpdateStrategy(UpdateStrategy<SceneObjectProperty<T>> updateStrategy) {
    updateStrategies.add(updateStrategy);
  }

  public void removeUpdateStrategy(UpdateStrategy<SceneObjectProperty<T>> updateStrategy) {
    updateStrategies.remove(updateStrategy);
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
    for (UpdateStrategy updateStrategy : updateStrategies) {
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

  public Boolean isAsync() {
    return isAsync;
  }

  public void setAsync(Boolean async) {
    isAsync = async;
  }

  public Long getAsyncUpdateTime() {
    return asyncUpdateTime;
  }

  public void setAsyncUpdateTime(Long asyncUpdateTime) {
    this.asyncUpdateTime = asyncUpdateTime;
  }
}
