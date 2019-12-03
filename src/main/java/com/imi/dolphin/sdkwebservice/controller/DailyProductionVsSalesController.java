/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceDailyProductionVsSales;
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
public class DailyProductionVsSalesController {

    @Autowired
    ServiceDailyProductionVsSales serviceDailyProductionVsSales;

    @RequestMapping("/dailyProductionVsSales_setFirstStatusCode")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_KategoriGroupProduct")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_KategoriGroupProduct(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_KategoriGroupProduct(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_tanyaSKU")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_Summary")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_Summary(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_getReport")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_getReport(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_validasiReport")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_validasiReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_validasiReport(extensionRequest);
    }

    @RequestMapping("/dailyProductionVsSales_bypassReport")
    @PostMapping
    public ExtensionResult dailyProductionVsSales_bypassReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyProductionVsSales.dailyProductionVsSales_bypassReport(extensionRequest);
    }

}
