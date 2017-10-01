package com.codeborne.selenide;

import com.codeborne.selenide.commands.Commands;
import com.codeborne.selenide.commands.DownloadFile;
import com.codeborne.selenide.impl.DownloadFileWithHttpRequest;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = SelenideModule.class)
public interface SelenideBuilder {
//  Commands commands();
//  DownloadFileWithHttpRequest downloadFileWithHttpRequest();
  DownloadFile downloadFile();
}
