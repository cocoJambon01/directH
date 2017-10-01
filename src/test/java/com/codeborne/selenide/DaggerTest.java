package com.codeborne.selenide;

import com.codeborne.selenide.commands.DownloadFile;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * See https://github.com/google/dagger/tree/master/examples/simple/src/main/java/coffee
 */
public class DaggerTest {
  @Test
  public void simpleService() {
//    SelenideBuilder selenide = DaggerSelenideBuilder.create();
//    System.out.println(selenide.downloadFileWithHttpRequest());
  }

  @Test
  public void serviceWithDependency() {
    SelenideBuilder selenide = DaggerSelenideBuilder.create();
    DownloadFile downloadFile = selenide.downloadFile();
    assertNotNull(downloadFile.downloadFileWithHttpRequest);
    System.out.println(downloadFile);
  }
}
