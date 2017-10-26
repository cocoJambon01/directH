package com.codeborne.selenide;

import com.codeborne.selenide.impl.Navigator;
import com.codeborne.selenide.impl.ScreenShotLaboratory;
import org.openqa.selenium.JavascriptExecutor;

import javax.inject.Inject;
import java.net.URL;
import java.util.logging.Logger;

import static com.codeborne.selenide.Configuration.dismissModalDialogs;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.supportsModalDialogs;
import static com.codeborne.selenide.WebDriverRunner.url;

public class SelenideDriver {
  private static final Logger log = Logger.getLogger(SelenideDriver.class.getName());

  @Inject private ScreenShotLaboratory screenshots;
  @Inject private Navigator navigator;

  public void open(String relativeOrAbsoluteUrl) {
    open(relativeOrAbsoluteUrl, "", "", "");
  }

  public void open(URL absoluteUrl) {
    open(absoluteUrl, "", "", "");
  }

  public void open(String relativeOrAbsoluteUrl, String domain, String login, String password) {
    navigator.open(relativeOrAbsoluteUrl, domain, login, password);
    mockModalDialogs();
  }

  public void open(URL absoluteUrl, String domain, String login, String password) {
    navigator.open(absoluteUrl, domain, login, password);
    mockModalDialogs();
  }

  public void updateHash(String hash) {
    String localHash = (hash.charAt(0) == '#') ? hash.substring(1) : hash;
    executeJavaScript("window.location.hash='" + localHash + "'");
  }

  private void mockModalDialogs() {
    if (doDismissModalDialogs()) {
      String jsCode =
          "  window._selenide_modalDialogReturnValue = true;\n" +
              "  window.alert = function(message) {};\n" +
              "  window.confirm = function(message) {\n" +
              "    return window._selenide_modalDialogReturnValue;\n" +
              "  };";
      try {
        executeJavaScript(jsCode);
      }
      catch (UnsupportedOperationException cannotExecuteJsAgainstPlainTextPage) {
        log.warning(cannotExecuteJsAgainstPlainTextPage.toString());
      }
    }
  }

  private boolean doDismissModalDialogs() {
    return !supportsModalDialogs() || dismissModalDialogs;
  }

  @SuppressWarnings("unchecked")
  public <T> T executeJavaScript(String jsCode, Object... arguments) {
    return (T) ((JavascriptExecutor) getWebDriver()).executeScript(jsCode, arguments);
  }

  public void refresh() {
    navigator.open(url());
  }

  public void back() {
    navigator.back();
  }

  public void forward() {
    navigator.forward();
  }

  public String screenshot(String fileName) {
    return screenshots.takeScreenShot(fileName);
  }
}
