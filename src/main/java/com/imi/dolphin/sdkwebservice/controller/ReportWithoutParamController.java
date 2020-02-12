/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceReportWithoutParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Deka
 */
@RestController
public class ReportWithoutParamController {

    @Autowired
    ServiceReportWithoutParam serviceReportWithoutParam;

    @RequestMapping("/ReportWithoutParam_getReport")
    @PostMapping
    public ExtensionResult ReportWithoutParam_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportWithoutParam.ReportWithoutParam_getReport(extensionRequest);
    }
}
