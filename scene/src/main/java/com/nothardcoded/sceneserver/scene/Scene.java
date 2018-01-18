package com.nothardcoded.sceneserver.scene;

import com.nothardcoded.sceneserver.scene.exception.NotAssignableException;
import com.nothardcoded.sceneserver.scene.model.annotation.AsyncSceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.annotation.ScenePropertyQualifier;
import com.nothardcoded.sceneserver.scene.model.annotation.SceneRegister;
import com.nothardcoded.sceneserver.scene.model.listener.SceneListener;
import com.nothardcoded.sceneserver.scene.model.object.SceneObject;
import com.nothardcoded.sceneserver.scene.model.property.SceneObjectProperty;
import com.nothardcoded.sceneserver.scene.model.property.SceneProperty;
import com.nothardcoded.sceneserver.scene.model.strategy.SceneUpdateStrategy;
import com.nothardcoded.sceneserver.scene.model.strategy.SceneUpdateStrategyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
@Component
public class Scene implements ApplicationContextAware {
  private static Logger LOG = LoggerFactory.getLogger(Scene.class);

  private GenericApplicationContext context;

  private Set<SceneListener> sceneListeners = new HashSet<>();
  private SceneUpdateStrategy sceneUpdateStrategy;

  public SceneObject createSceneObject(Class clazz) throws NotAssignableException {
    registerBean(clazz);

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

  @PostConstruct
  public void init () {
    SceneUpdateStrategyBuilder strategyBuilder = new SceneUpdateStrategyBuilder();
    strategyBuilder.sceneListeners(sceneListeners);
    this.sceneUpdateStrategy = strategyBuilder.build();

    loadContext();
    register();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = (GenericApplicationContext) applicationContext;
    scanForSceneObjects(this.getClass());
  }

  private void loadContext() {
    if (context == null) {
      setApplicationContext(new GenericApplicationContext());
      XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
      xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
      xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
      context.refresh();
      registerSceneProperty(new SceneProperty("sampleInt", new Integer (10)));
      registerSceneProperty(new SceneProperty("sampleInt2", new Integer (11)));
      registerSceneProperty(new SceneProperty("sampleInt3", new Integer (12)));
      registerSceneProperty(new SceneProperty("someOtherValue", "This is before."));
      registerSceneProperty(new SceneProperty("someOtherValue1", "This is before 1."));
      registerSceneProperty(new SceneProperty("someOtherValue2", "This is before 2."));
      context.getBeanFactory().autowireBean(this);
    }
  }

  private void registerBean (Class clazz) throws NotAssignableException {
    if (!SceneObject.class.isAssignableFrom(clazz)) {
      throw new NotAssignableException();
    }

    if (!context.getBeanFactory().containsBean(clazz.getName())) {
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
      builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
      factory.registerBeanDefinition(clazz.getName(), builder.getBeanDefinition());
      scanForSceneObjects(clazz);
    }
  }

  private void scanForSceneObjects (Class clazz) {
    for (Field field : clazz.getDeclaredFields()) {
      if (SceneObject.class.isAssignableFrom(field.getType())) {
        try {
          registerBean(field.getType());
        } catch (NotAssignableException e) {
          LOG.error("Could not register scene object bean for class: ", e);
        }
      }
    }
  }

  private void register() {
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (isRegisterField(field)) {
        registerSceneObjectField(this, field);
      }
    }
  }

  private void construct(SceneObject sceneObject) {
    Field[] fields = sceneObject.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getType().isAssignableFrom(SceneObjectProperty.class)) {
        injectSceneObjectPropertyField(sceneObject, field);
      } else if (isRegisterField(field)) {
        registerSceneObjectField(sceneObject, field);
      }
    }
  }

  private void registerSceneObjectField(Object object, Field field) {
    field.setAccessible(true);
    try {
      SceneObject sceneObject = (SceneObject) field.get(object);
      construct(sceneObject);

      sceneUpdateStrategy.getNewObject().updated(sceneObject);
    } catch (IllegalAccessException e) {
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

      if (field.getDeclaredAnnotation(ScenePropertyQualifier.class) != null) {
        Object sceneProp = context.getBeanFactory().getBean(
                field.getDeclaredAnnotation(ScenePropertyQualifier.class).value());

        if (field.getType().isAssignableFrom(sceneProp.getClass())) {
          property = (SceneObjectProperty) sceneProp;
          field.set(object, property);
        } else {
          property.setValue(sceneProp);
        }
      }

      property.addUpdateStrategy(sceneUpdateStrategy.getPropertyUpdateStrategy());

      if (field.getDeclaredAnnotation(AsyncSceneObjectProperty.class) != null) {
        property.setAsync(true);
        property.setAsyncUpdateTime(field.getDeclaredAnnotation(AsyncSceneObjectProperty.class).updateTime());
      }

      sceneUpdateStrategy.getNewProperty().updated(property);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      LOG.error("Could not inject scene object property: ", e);
    }
  }

  private boolean isRegisterField(Field field) {
    return SceneObject.class.isAssignableFrom(field.getType())
        && field.getDeclaredAnnotation(SceneRegister.class) != null;
  }
}
