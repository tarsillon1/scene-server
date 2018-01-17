package com.nothardcoded.sceneserver.scene.model.property;

/**
 * Created by nick.tarsillo on 1/14/18.
 */
public class SceneProperty<T> implements Property<T> {
  private String propertyName;
  private T propertyValue;

  public SceneProperty (String propertyName, T propertyValue) {
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
  }

  @Override
  public T getValue() {
    return propertyValue;
  }

  @Override
  public T setValue(Object val) {
    return propertyValue;
  }

  @Override
  public String getName() {
    return propertyName;
  }

  @Override
  public String setName(String propertyName) {
    this.propertyName = propertyName;
    return propertyName;
  }
}
