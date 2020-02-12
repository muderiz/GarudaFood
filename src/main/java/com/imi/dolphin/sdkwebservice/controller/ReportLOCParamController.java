/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceReportLOCParam;
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
public class ReportLOCParamController {

    @Autowired
    ServiceReportLOCParam serviceReportLOCParam;

    @RequestMapping("/ReportLOCParam_KategoriArea")
    @PostMapping
    public ExtensionResult ReportLOCParam_KategoriArea(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportLOCParam.ReportLOCParam_KategoriArea(extensionRequest);
    }

    @RequestMapping("/ReportLOCParam_KategoriRegion")
    @PostMapping
    public ExtensionResult ReportLOCParam_KategoriRegion(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportLOCParam.ReportLOCParam_KategoriRegion(extensionRequest);
    }

    @RequestMapping("/ReportLOCParam_tanyaSKU")
    @PostMapping
    public ExtensionResult ReportLOCParam_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportLOCParam.ReportLOCParam_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/ReportLOCParam_Summary")
    @PostMapping
    public ExtensionResult ReportLOCParam_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportLOCParam.ReportLOCParam_Summary(extensionRequest);
    }

    @RequestMapping("/ReportLOCParam_getReport")
    @PostMapping
    public ExtensionResult ReportLOCParam_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceReportLOCParam.ReportLOCParam_getReport(extensionRequest);
    }
}
