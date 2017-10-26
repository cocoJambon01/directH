package com.codeborne.selenide.inject.samples;

import javax.inject.Inject;

public class Bar {
  @Inject
  public Foo foo;

  public Foo nonInjected;
}
