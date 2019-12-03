/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceDailyStockBySKU;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceOTD1OFR1ByWeek_PeriodeShipment;
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
public class OTD1OFR1ByWeek_PeriodeShipmentController {
 @Autowired
    ServiceOTD1OFR1ByWeek_PeriodeShipment serviceOTD1OFR1ByWeek_PeriodeShipment;

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_setFirstStatusCode")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_KategoriGroupProduct")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_KategoriGroupProduct(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_KategoriGroupProduct(extensionRequest);
    }

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_tanyaSKU")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_tanyaSKU(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_tanyaSKU(extensionRequest);
    }

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_Summary")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_Summary(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_Summary(extensionRequest);
    }

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_getReport")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_getReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_getReport(extensionRequest);
    }

    @RequestMapping("/otd1Ofr1ByWeek_PeriodeShipment_validasiReport")
    @PostMapping
    public ExtensionResult otd1Ofr1ByWeek_PeriodeShipment_validasiReport(@RequestBody ExtensionRequest extensionRequest) {
        return serviceOTD1OFR1ByWeek_PeriodeShipment.otd1Ofr1ByWeek_PeriodeShipment_validasiReport(extensionRequest);
    }

}
