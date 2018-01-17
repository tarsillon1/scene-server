package com.nothardcoded.sceneserver.scene;

import com.nothardcoded.sceneserver.scene.exception.NotAssignableException;
import com.nothardcoded.sceneserver.scene.model.annotation.AsyncSceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.annotation.SceneInject;
import com.nothardcoded.sceneserver.scene.model.listener.SceneListener;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.AsyncSceneObjectPropertyHandler;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.property.SceneProperty;
import com.nothardcoded.sceneserver.scene.model.strategy.SceneUpdateStrategy;
import com.nothardcoded.sceneserver.scene.model.strategy.SceneUpdateStrategyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
@Component
public class Scene {
  private static Logger LOG = LoggerFactory.getLogger(Scene.class);

  @Autowired
  private GenericApplicationContext context;

  private Set<SceneListener> sceneListeners = new HashSet<>();
  private SceneUpdateStrategy sceneUpdateStrategy;

  public Scene() {
    SceneUpdateStrategyBuilder strategyBuilder = new SceneUpdateStrategyBuilder();
    strategyBuilder.sceneListeners(sceneListeners);
    this.sceneUpdateStrategy = strategyBuilder.build();

    loadContext();
    beforeInjection();
    inject();
  }

  public SceneObject createSceneObject(Class clazz) throws NotAssignableException {
    if (!SceneObject.class.isAssignableFrom(clazz)) {
      throw new NotAssignableException();
    }

    if (!context.getBeanFactory().containsBean(clazz.getName())) {
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
      builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
      factory.registerBeanDefinition(clazz.getName(), builder.getBeanDefinition());
    }

    SceneObject object = (SceneObject) context.getBean(clazz);
    construct(object);

    return object;
  }

  public SceneObject registerSceneObject (SceneObject sceneObject) {
    context.getBeanFactory().autowireBean(sceneObject);
    construct(sceneObject);

    return sceneObject;
  }

  public SceneProperty registerSceneProperty (SceneProperty sceneProperty) {
    if (!context.getBeanFactory().containsBean(sceneProperty.getName())) {
      context.getBeanFactory().registerSingleton(sceneProperty.getName(), sceneProperty.getValue());
    }

    return sceneProperty;
  }

  public void registerListener(SceneListener sceneListener) {
    sceneListeners.add(sceneListener);
  }

  public void unregisteredListener(SceneListener sceneListener) {
    sceneListeners.remove(sceneListener);
  }

  protected void beforeInjection () {}

  private void loadContext() {
    if (context == null) {
      context = new GenericApplicationContext();
    }

    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
    xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
    xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
    context.refresh();
  }

  private void inject() {
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (isInjectableField(field)) {
        injectSceneObjectField(this, field);
      }
    }
  }

  private void construct(SceneObject sceneObject) {
    Field[] fields = sceneObject.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getType().isAssignableFrom(SceneObjectProperty.class)) {
        injectSceneObjectPropertyField(sceneObject, field);
      } else if (isInjectableField(field)) {
        injectSceneObjectField(sceneObject, field);
      }

      injectAsyncSceneObjectProperty(sceneObject,field);
    }
  }

  private void injectSceneObjectField(Object object, Field field) {
    field.setAccessible(true);
    try {
      SceneObject sceneObject = (SceneObject) field.get(object);
      if (sceneObject == null) {
        sceneObject = createSceneObject(field.getType());
        field.set(object, sceneObject);
      } else {
        construct(sceneObject);
      }

      sceneUpdateStrategy.getNewObject().updated(sceneObject);
    } catch (NotAssignableException | IllegalAccessException e) {
      LOG.error("Could not inject scene object: ", e);
    }
  }

  private void injectSceneObjectPropertyField(Object object, Field field) {
    field.setAccessible(true);
    try {
      SceneObjectProperty property = (SceneObjectProperty) field.get(object);
      if (property == null) {
        property = (SceneObjectProperty) Class.forName(field.getType().getName()).newInstance();
        field.set(object, property);
      }

      if (field.getDeclaredAnnotation(AsyncSceneObjectProperty.class) == null) {
        property.setUpdateStrategy(sceneUpdateStrategy.getSyncPropertyUpdated());
      }

      sceneUpdateStrategy.getNewProperty().updated(property);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      LOG.error("Could not inject scene object property: ", e);
    }
  }

  private void injectAsyncSceneObjectProperty(SceneObject sceneObject, Field field) {
    AsyncSceneObjectProperty annotation = field.getDeclaredAnnotation(AsyncSceneObjectProperty.class);
    if (annotation != null) {
      AsyncSceneObjectPropertyHandler handler = new AsyncSceneObjectPropertyHandler(
          annotation.updateTime(), sceneUpdateStrategy.getAsyncPropertyUpdated(), sceneObject,
          field.getName());

      sceneUpdateStrategy.getNewProperty().updated(handler.getProperty());
    }
  }

  private boolean isInjectableField (Field field) {
    return SceneObject.class.isAssignableFrom(field.getType())
        && field.getDeclaredAnnotation(SceneInject.class) != null;
  }
}
