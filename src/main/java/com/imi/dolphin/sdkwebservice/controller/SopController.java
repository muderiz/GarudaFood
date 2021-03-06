/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.controller;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceImpReport;
import com.imi.dolphin.sdkwebservice.serviceSOP.ServiceImpSOP;
import com.imi.dolphin.sdkwebservice.serviceSOP.ServiceSOP;
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
public class SopController {

    @Autowired
    ServiceImpSOP svcService;

    @Autowired
    ServiceSOP svcServiceSOP;

    @Autowired
    IMailService svcMailService;

    @RequestMapping("/sop_testGetList")
    @PostMapping
    public ExtensionResult sop_testGetList(@RequestBody ExtensionRequest extensionRequest) {
        return svcService.sop_testGetList(extensionRequest);
    }
//
//    @RequestMapping("/sop_PertanyaanPertama")
//    @PostMapping
//    public ExtensionResult sop_PertanyaanPertama(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_PertanyaanPertama(extensionRequest);
//    }
//  
//    @RequestMapping("/sop_tanyaJenisDokumen")
//    @PostMapping
//    public ExtensionResult sop_tanyaJenisDokumen(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_tanyaJenisDokumen(extensionRequest);
//    }
//
//    @RequestMapping("/sop_tanyaDivisi")
//    @PostMapping
//    public ExtensionResult sop_tanyaDivisi(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_tanyaDivisi(extensionRequest);
//    }
//
//    @RequestMapping("/sop_tNamaDokumen")
//    @PostMapping
//    public ExtensionResult sop_tanyaNamaDokumen(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_tanyaNamaDokumen(extensionRequest);
//    }
//
//    @RequestMapping("/sop_konfirmasiNamaDokumen")
//    @PostMapping
//    public ExtensionResult sop_konfirmasiNamaDokumen(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_konfirmasiNamaDokumen(extensionRequest);
//    }
//
//    @RequestMapping("/sop_konfirmasiSOP")
//    @PostMapping
//    public ExtensionResult sop_konfirmasiSOP(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_konfirmasiSOP(extensionRequest);
//    }
//
//    @RequestMapping("/sop_validasiSOP")
//    @PostMapping
//    public ExtensionResult sop_validasiSOP(@RequestBody ExtensionRequest extensionRequest) {
//        return svcService.sop_validasiSOP(extensionRequest);
//    }

    // New SOP //
    @RequestMapping("/sop_setFirstStatusCode")
    @PostMapping
    public ExtensionResult sop_setFirstStatusCode(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_setFirstStatusCode(extensionRequest);
    }

    @RequestMapping("/sop_Company")
    @PostMapping
    public ExtensionResult sop_Company(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_Company(extensionRequest);
    }

    @RequestMapping("/sop_Divisi")
    @PostMapping
    public ExtensionResult sop_Divisi(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_Divisi(extensionRequest);
    }

    @RequestMapping("/sop_JenisDokumen")
    @PostMapping
    public ExtensionResult sop_JenisDokumen(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_JenisDokumen(extensionRequest);
    }

    @RequestMapping("/sop_tanyaNamaDokumen")
    @PostMapping
    public ExtensionResult sop_tanyaNamaDokumen(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_tanyaNamaDokumen(extensionRequest);
    }

    @RequestMapping("/sop_getSOP")
    @PostMapping
    public ExtensionResult sop_getSOP(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_getSOP(extensionRequest);
    }

    @RequestMapping("/sop_validasiSOP")
    @PostMapping
    public ExtensionResult sop_validasiSOP(@RequestBody ExtensionRequest extensionRequest) {
        return svcServiceSOP.sop_validasiSOP(extensionRequest);
    }
    // ================================ //
}
