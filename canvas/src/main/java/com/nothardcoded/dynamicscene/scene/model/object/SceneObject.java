package com.nothardcoded.dynamicscene.scene.model.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by nick.tarsillo on 1/10/18.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SceneObject {
  private static Logger LOG = LoggerFactory.getLogger(SceneObject.class);
}
