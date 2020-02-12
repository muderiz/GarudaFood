/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceReportSKUParam;
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
public class ReportSKUParamController {

    @Autowired
    ServiceReportSKUParam serviceReportSKUParam;

    @RequestMapping("/ReportSKUParam_KategoriGroupProduct")
    @PostMapping
    public ExtensionResult ReportSKUParam_KategoriGroupProduct(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportSKUParam.ReportSKUParam_KategoriGroupProduct(extensionRequest);
    }

    @RequestMapping("/ReportSKUParam_tanyaSKU")
    @PostMapping
    public ExtensionResult ReportSKUParam_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportSKUParam.ReportSKUParam_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/ReportSKUParam_Summary")
    @PostMapping
    public ExtensionResult ReportSKUParam_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportSKUParam.ReportSKUParam_Summary(extensionRequest);
    }

    @RequestMapping("/ReportSKUParam_getReport")
    @PostMapping
    public ExtensionResult ReportSKUParam_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportSKUParam.ReportSKUParam_getReport(extensionRequest);
    }
}
