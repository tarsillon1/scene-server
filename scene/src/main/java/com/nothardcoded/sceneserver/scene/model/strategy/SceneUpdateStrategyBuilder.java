package com.nothardcoded.sceneserver.scene.model.strategy;

import com.nothardcoded.sceneserver.scene.model.listener.SceneListener;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;

import java.util.Set;

/**
 * Created by nick.tarsillo on 1/13/18.
 */
public class SceneUpdateStrategyBuilder {
  private Set<SceneListener> sceneListeners;

  public void sceneListeners(Set<SceneListener> sceneListeners) {
    this.sceneListeners = sceneListeners;
  }

  public SceneUpdateStrategy build () {
    return new DefaultUpdateStrategy();
  }

  class DefaultUpdateStrategy extends SceneUpdateStrategy {
    public DefaultUpdateStrategy () {
      setNewObject(new NewObject());
      setObjectUpdated(new UpdateObject());
      setNewProperty(new NewProperty());
      setSyncPropertyUpdated(new UpdateSyncProperty());
      setAsyncPropertyUpdated(new UpdateAsyncProperty());
    }

    class NewObject implements UpdateStrategy<SceneObject> {
      @Override
      public void updated(SceneObject sceneObject) {
        for (SceneListener listener : sceneListeners) {
          listener.newObject(sceneObject);
        }
      }
    }

    class UpdateObject implements UpdateStrategy<SceneObject> {
      @Override
      public void updated(SceneObject object) {
        for (SceneListener listener : sceneListeners) {
          listener.objectUpdated(object);
        }
      }
    }

    class NewProperty implements UpdateStrategy<SceneObjectProperty> {
      @Override
      public void updated(SceneObjectProperty property) {
        for (SceneListener listener : sceneListeners) {
          listener.newProperty(property);
        }
      }
    }

    class UpdateSyncProperty implements UpdateStrategy<SceneObjectProperty> {
      @Override
      public void updated(SceneObjectProperty property) {
        for (SceneListener listener : sceneListeners) {
          listener.propertyUpdated(property);
        }
      }
    }

    class UpdateAsyncProperty implements UpdateStrategy<SceneObjectProperty> {
      @Override
      public void updated(SceneObjectProperty property) {
        for (SceneListener listener : sceneListeners) {
          listener.propertyUpdated(property);
        }
      }
    }
  }
}
