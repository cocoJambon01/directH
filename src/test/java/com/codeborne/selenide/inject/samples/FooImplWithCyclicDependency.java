package com.codeborne.selenide.inject.samples;

import javax.inject.Inject;

public class FooImplWithCyclicDependency extends FooImpl {
  @Inject
  Bar bar;
}
