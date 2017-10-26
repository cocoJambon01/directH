package com.codeborne.selenide.inject;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.joining;

public class Module {
  private final Map<Class, Class> bindings = new ConcurrentHashMap<>();
  private final Map<Class, Object> instances = new ConcurrentHashMap<>();
  private final Deque<Class> classesBeingInitialized = new ArrayDeque<>();

  public <T> T instance(Class<T> clazz) {
    return injectFields(create(clazz));
  }

  private <T> T injectFields(T object) {
    Class<?> clazz = object.getClass();
    do {
      injectFields(object, clazz);
      clazz = clazz.getSuperclass();
    }
    while (clazz != Object.class);
    return object;
  }

  private <T> void injectFields(T object, Class<?> clazz) {
    if (classesBeingInitialized.contains(clazz)) {
      CharSequence chain = classesBeingInitialized.stream().map(Class::getName).collect(joining(" -> "));
      throw new IllegalArgumentException("Cyclic dependency: " + chain + " -> " + clazz.getName());
    }

    classesBeingInitialized.add(clazz);

    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Inject.class)) {
        field.setAccessible(true);
        Object fieldValue = instance(field.getType());

        try {
          field.set(object, fieldValue);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Failed to initialize field " + field, e);
        }
      }
    }

    classesBeingInitialized.remove(clazz);
  }

  private <T> T create(Class<T> clazz) {
    return (T) instances.computeIfAbsent(clazz, this::newInstance);
  }

  private <T> T newInstance(Class<T> clazz) {
    Class<T> implementation = bindings.getOrDefault(clazz, clazz);
    try {
      Constructor<T> constructor = implementation.getDeclaredConstructor();
      constructor.setAccessible(true);
      return constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException("Failed to create instance of " + implementation.getName(), e);
    }
  }

  public <INTERFACE, IMPLEMENTATION> void bind(Class<INTERFACE> interfaceClass, Class<IMPLEMENTATION> implementationClass) {
    if (bindings.containsKey(interfaceClass)) {
      throw new IllegalArgumentException("Class " + interfaceClass + " is already bound to implementation " + implementationClass);
    }
    bindings.put(interfaceClass, implementationClass);
  }
}
