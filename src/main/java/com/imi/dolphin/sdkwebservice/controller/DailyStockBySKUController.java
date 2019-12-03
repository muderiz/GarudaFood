/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceDailyStockByLOC;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceDailyStockBySKU;
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
public class DailyStockBySKUController {

    @Autowired
    ServiceDailyStockBySKU serviceDailyStockBySKU;

    @RequestMapping("/dailyStockBySKU_setFirstStatusCode")
    @PostMapping
    public ExtensionResult dailyStockBySKU_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_KategoriGroupProduct")
    @PostMapping
    public ExtensionResult dailyStockBySKU_KategoriGroupProduct(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_KategoriGroupProduct(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_tanyaSKU")
    @PostMapping
    public ExtensionResult dailyStockBySKU_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_Summary")
    @PostMapping
    public ExtensionResult dailyStockBySKU_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_Summary(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_getReport")
    @PostMapping
    public ExtensionResult dailyStockBySKU_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_getReport(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_validasiReport")
    @PostMapping
    public ExtensionResult dailyStockBySKU_validasiReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_validasiReport(extensionRequest);
    }

    @RequestMapping("/dailyStockBySKU_bypassReport")
    @PostMapping
    public ExtensionResult dailyStockBySKU_bypassReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceDailyStockBySKU.dailyStockBySKU_bypassReport(extensionRequest);
    }

}
