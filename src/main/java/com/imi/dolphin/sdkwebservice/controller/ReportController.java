/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import com.imi.dolphin.sdkwebservice.service.IService;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceImpReport;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceImpReportBackup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ReportController {

    @Autowired
    ServiceImpReportBackup svcService1;

    @Autowired
    ServiceImpReport svcService;

    @Autowired
    IMailService svcMailService;

    @RequestMapping("/report_setFirstStatusCode")
    @PostMapping
    public ExtensionResult report_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return svcService.report_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/report_namaReport")
    @PostMapping
    public ExtensionResult report_namaReport(@RequestBody ExtensionRequest extensionRequest) {
        return svcService.report_namaReport(extensionRequest);
    }

    @RequestMapping("/report_validasiNamaReport")
    @PostMapping
    public ExtensionResult report_validasiNamaReport(@RequestBody ExtensionRequest extensionRequest) {
        return svcService.report_validasiNamaReport(extensionRequest);
    }

    @RequestMapping("/getReport")
    @PostMapping
    public ExtensionResult getReport(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.getReport(extensionRequest);
    }

    @RequestMapping("/validasiReport")
    @PostMapping
    public ExtensionResult validasiReport(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.validasiReport(extensionRequest);
    }

    @RequestMapping("/pertanyaanPertama")
    @PostMapping
    public ExtensionResult pertanyaanPertama(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.pertanyaanPertama(extensionRequest);
    }

    @RequestMapping("/tanyaReportName")
    @PostMapping
    public ExtensionResult tanyaReportName(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.tanyaReportName(extensionRequest);
    }

    @RequestMapping("/tanyaKategori")
    @PostMapping
    public ExtensionResult tanyaKategori(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.tanyaKategori(extensionRequest);
    }

    @RequestMapping("/tanyaGroup")
    @PostMapping
    public ExtensionResult tanyaGroup(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.tanyaGroup(extensionRequest);
    }

    @RequestMapping("/konfirmasiGroup")
    @PostMapping
    public ExtensionResult konfirmasiGroup(@RequestBody ExtensionRequest extensionRequest) {
        return svcService1.konfirmasiGroup(extensionRequest);
    }

}
