/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.TestingC1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Deka
 */
public class TestingC1Controller {

    @Autowired
    TestingC1 testingC1;

    @RequestMapping("/TestingC1_getReport")
    @PostMapping
    public ExtensionResult TestingC1_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return testingC1.TestingC1_getReport(extensionRequest);
    }
}
