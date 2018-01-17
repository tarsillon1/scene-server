package com.nothardcoded.sceneserver.scene.model.property;

import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.strategy.UpdateStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by nick.tarsillo on 1/13/18.
 */
public final class AsyncSceneObjectPropertyHandler {
  private static Logger LOG = LoggerFactory.getLogger(AsyncSceneObjectPropertyHandler.class);

  private static Timer timer = new Timer();
  private static Map<Long, ConcurrentLinkedQueue<AsyncSceneObjectPropertyHandler>> asyncProps =
      new ConcurrentHashMap<>();

  static {
    timer.scheduleAtFixedRate(new TimerTask() {
      private long time;

      @Override
      public void run() {
        Long maxVal = new Long(0);
        Set<Long> updateNeeded = new HashSet<>();
        for (Long val : asyncProps.keySet()) {
          if (time != 0 && time % val == 0) {
            updateNeeded.add(val);
          }

          if (maxVal < val) {
            maxVal = val;
          }
        }

        for (Long update : updateNeeded) {
          for (AsyncSceneObjectPropertyHandler handler : asyncProps.get(update)) {
            Object value = null;
            try {
              SceneObject sceneObject = handler.getProperty().getSceneObject();
              Field field = sceneObject.getClass().getDeclaredField(handler.getProperty().getName());
              field.setAccessible(true);
              if (field.getType().isAssignableFrom(SceneObjectProperty.class)) {
                value = ((SceneObjectProperty) field.get(sceneObject)).getValue();
              } else {
                value = field.get(sceneObject);
              }
            } catch (IllegalAccessException | NoSuchFieldException e) {
              LOG.error("Failed to update async property: ", e);
            }

            if ((value == null && handler.getProperty().getValue() != null) ||
                (value != null && !value.equals(handler.getProperty().getValue()))) {
              handler.getProperty().setValue(value);
            }
          }
        }

        time += 1;

        if (time > maxVal) {
          time = 0;
        }
      }
    }, 0, 1);
  }

  private static void registerAsync(Long updateTime, AsyncSceneObjectPropertyHandler property) {
    if (!asyncProps.containsKey(updateTime)) {
      asyncProps.put(updateTime, new ConcurrentLinkedQueue<>());
    }

    asyncProps.get(updateTime).add(property);
  }

  private SceneObjectProperty property = new SceneObjectProperty();

  public AsyncSceneObjectPropertyHandler(Long updateTime, UpdateStrategy<SceneObjectProperty> asyncStrategy, SceneObject sceneObject, String propertyName) {
    property.setSceneObject(sceneObject);
    property.setName(propertyName);

    try {
      Field field = sceneObject.getClass().getDeclaredField(propertyName);
      field.setAccessible(true);
      if (field.getType().isAssignableFrom(SceneObjectProperty.class)) {
        property.setValue(((SceneObjectProperty) field.get(sceneObject)).getValue());
      } else {
        property.setValue(field.get(sceneObject));
      }
    } catch (IllegalAccessException | NoSuchFieldException e) {
      LOG.error("Failed to set initial value for async property: ", e);
    }

    property.setUpdateStrategy(asyncStrategy);

    registerAsync(updateTime, this);
  }

  public SceneObjectProperty getProperty() {
    return property;
  }

  public void setProperty(SceneObjectProperty property) {
    this.property = property;
  }
}
