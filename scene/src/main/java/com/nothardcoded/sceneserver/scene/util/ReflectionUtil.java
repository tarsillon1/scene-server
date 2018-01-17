package com.nothardcoded.sceneserver.scene.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * Created by nick.tarsillo on 1/12/18.
 */
public class ReflectionUtil {
  private static Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

  public static boolean hasObject (Object container, Object object) {
    return hasObject(container, object, new HashSet<>());
  }

  private static boolean hasObject (Object container, Object object,  HashSet<Field> checked) {
    if (object == null || container == null) {
      return false;
    } else if (container == object) {
      return true;
    }

    Field[] fields = container.getClass().getDeclaredFields();
    try {
      for (Field field : fields) {
        if (checked.contains(field)) {
          continue;
        }

        if (Iterable.class.isAssignableFrom(field.getType())) {
          field.setAccessible(true);
          Iterable iterable = (Iterable) field.get(container);
          checked.add(field);
          for (Object otherObject : iterable) {
            if (hasObject(otherObject, object, checked)) {
              return true;
            }
          }
        } else if (field.getType().isArray()) {
          Object[] objects = (Object[]) field.get(container);
          checked.add(field);
          for (Object otherObject : objects) {
            if (hasObject(otherObject, object, checked)) {
              return true;
            }
          }
        } else {
          field.setAccessible(true);
          Object otherObject = field.get(container);
          checked.add(field);
          if (hasObject(otherObject, object, checked)) {
            return true;
          }
        }
      }
    } catch (IllegalAccessException e) {
      LOG.error("Failed to check if scene has object: ", e);
    }

    return false;
  }
}
