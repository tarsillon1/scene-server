package com.nothardcoded.sceneserver.scene.model.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nick.tarsillo on 1/14/18.
 */
@Autowired
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SceneRegister {}
