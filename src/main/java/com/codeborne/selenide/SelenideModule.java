package com.codeborne.selenide;

import com.codeborne.selenide.commands.Commands;
import com.codeborne.selenide.commands.DownloadFile;
import com.codeborne.selenide.impl.DownloadFileWithHttpRequest;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class SelenideModule {
//  @Provides
//  @Singleton
//  @Inject Commands provideCommands(DownloadFile downloadFile) {
//    return new Commands(downloadFile);
//  }
/*
  @Provides
  @Singleton
  @Inject
  DownloadFileWithHttpRequest provideDownloadFileWithHttpRequest() {
    return new DownloadFileWithHttpRequest();
  }
*/

  @Provides
  @Singleton
  @Inject
  DownloadFile provideDownloadFile() {
    return new DownloadFile();
  }
}
