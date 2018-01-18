package com.nothardcoded.sceneserver.scene.model.strategy;

import com.nothardcoded.sceneserver.scene.model.listener.SceneListener;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;

import java.util.*;

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
    public DefaultUpdateStrategy() {
      setNewObject(new NewObject());
      setObjectUpdated(new UpdateObject());
      setNewProperty(new NewProperty());
      setPropertyUpdateStrategy(new UpdateSyncProperty());
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
      private Set<SceneObjectProperty> asyncTimerSet = new HashSet<>();

      @Override
      public void updated(SceneObjectProperty property) {
        if (property.isAsync() && !asyncTimerSet.contains(property)) {
          asyncTimerSet.add(property);

          Timer timer = new Timer();
          timer.schedule(new TimerTask() {
            @Override
            public void run() {
              for (SceneListener listener : sceneListeners) {
                listener.propertyUpdated(property);
              }

              asyncTimerSet.remove(property);
            }
          }, property.getAsyncUpdateTime());
        } else if (!property.isAsync()) {
          for (SceneListener listener : sceneListeners) {
            listener.propertyUpdated(property);
          }
        }
      }
    }
  }
}
