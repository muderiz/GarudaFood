/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceDailyStockByLOC;
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
public class DailyStockByLOCController {

    @Autowired
    ServiceDailyStockByLOC serviceDailyStockByLOC;

    @RequestMapping("/dailyStockByLOC_setFirstStatusCode")
    @PostMapping
    public ExtensionResult dailyStockByLOC_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_KategoriArea")
    @PostMapping
    public ExtensionResult dailyStockByLOC_KategoriArea(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_KategoriArea(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_KategoriRegion")
    @PostMapping
    public ExtensionResult dailyStockByLOC_KategoriRegion(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_KategoriRegion(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_tanyaSKU")
    @PostMapping
    public ExtensionResult dailyStockByLOC_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_Summary")
    @PostMapping
    public ExtensionResult dailyStockByLOC_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_Summary(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_getReport")
    @PostMapping
    public ExtensionResult dailyStockByLOC_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_getReport(extensionRequest);
    }

    @RequestMapping("/dailyStockByLOC_validasiReport")
    @PostMapping
    public ExtensionResult dailyStockByLOC_validasiReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockByLOC.dailyStockByLOC_validasiReport(extensionRequest);
    }
}
