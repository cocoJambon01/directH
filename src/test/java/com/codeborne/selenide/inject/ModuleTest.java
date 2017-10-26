package com.codeborne.selenide.inject;

import com.codeborne.selenide.inject.samples.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModuleTest {
  Module module = new Module();

  @Test
  public void publicConstructor() {
    assertNotNull(module.instance(PublicFoo.class));
  }

  @Test
  public void packagePrivateConstructor() {
    assertNotNull(module.instance(Foo.class));
  }

  @Test
  public void classWithDependencies() {
    Foo foo = module.instance(Foo.class);
    Bar bar = module.instance(Bar.class);
    assertSame(foo, bar.foo);
  }

  @Test
  public void shouldNotSetFields_notMarkedWithInjectAnnotation() {
    Bar bar = module.instance(Bar.class);
    assertNull(bar.nonInjected);
  }

  @Test
  public void canBindSpecificImplementations() {
    module.bind(Foo.class, FooImpl.class);
    assertTrue(module.instance(Foo.class) instanceof FooImpl);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotBindTwice() {
    module.bind(Foo.class, FooImpl.class);
    module.bind(Foo.class, Foo.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void doesNotAllowCyclicDependencies() {
    module.bind(Foo.class, FooImplWithCyclicDependency.class);
    module.bind(Bar.class, BarImplWithCyclicDependency.class);

    // expected error message:
    // "Cyclic dependency: com.codeborne.selenide.inject.samples.FooImplWithCyclicDependency ->
    //      com.codeborne.selenide.inject.samples.Bar ->
    //      com.codeborne.selenide.inject.samples.FooImplWithCyclicDependency"
    module.instance(Foo.class);
  }
}