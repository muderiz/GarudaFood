/**
 * Copyright (c) 2014 InMotion Innovation Technology. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of InMotion
 * Innovation Technology. ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall only be
 * used in accordance with the terms of the license agreement entered into with
 * IMI; other than in accordance with the written permission of IMI. <BR>
 *
 *
 */
package com.imi.dolphin.sdkwebservice.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author reja
 *
 */
@Component
public class AppProperties {

    @Value("${server.port}")
    String servicePort;

    @Value("${app.form.id}")
    String formId;

    @Value("${mail.username}")
    String mailUsername;

    @Value("${mail.password}")
    String mailPassword;

    @Value("${mail.smtp.auth}")
    String mailStmpAuth;

    @Value("${mail.smtp.starttls.enable}")
    String mailSmtpTls;

    @Value("${mail.smtp.host}")
    String mailSmtpHost;

    @Value("${mail.smtp.port}")
    String mailSmtpPort;

    @Value("${sdk.connectTimeout}")
    String sdkConnectTimeout;

    @Value("${sdk.readTimeout}")
    String sdkReadTimeout;

    @Value("${sdk.username}")
    String sdkDolphinUsername;

    @Value("${sdk.password}")
    String sdkDolphinPassword;

    @Value("${sdk.dolphin.base.url}")
    String sdkDolphinBaseUrl;

    @Value("${sdk.dolphin.graph.ping}")
    String sdkDolphinGraphPing;

    @Value("${sdk.dolphin.graph.auth}")
    String sdkDolphinGraphAuth;

    @Value("${sdk.dolphin.graph.auth.token}")
    String sdkDolphinGraphAuthToken;

    @Value("${sdk.dolphin.graph.contacts}")
    String sdkDolphinGraphContacts;

    @Value("${sdk.dolphin.graph.contacts.update}")
    String sdkDolphinGraphContactsUpdate;

    // Garudafood
    @Value("${garudafood.base.url}")
    String GARUDAFOOD_BASE_URL;

    @Value("${garudafood.api.report}")
    String GARUDAFOOD_API_REPORT;

    @Value("${garudafood.watermark.sop}")
    String GARUDAFOOD_WATERMARK_SOP;

    @Value("${garudafood.base.sop}")
    String GARUDAFOOD_BASE_SOP;

    @Value("${garudafood.url.generatedfiles}")
    String GARUDAFOOD_URL_GENERATEDFILES;

    @Value("${garudafood.path.generatedfiles}")
    String GARUDAFOOD_PATH_GENERATEDFILES;

    @Value("${garudafood.watermark.report}")
    String GARUDAFOOD_WATERMARK_REPORT;

    @Value("${garudafood.ldap.host}")
    String GARUDAFOOD_LDAP_HOST;

    @Value("${garudafood.ldap.directorypath}")
    String GARUDAFOOD_LDAP_DIRECTORYPATH;

    @Value("${garudafood.ldap.loginprincipal}")
    String GARUDAFOOD_LDAP_LOGINPRINCIPAL;

    @Value("${garudafood.ldap.username}")
    String GARUDAFOOD_LDAP_USERNAME;

    @Value("${garudafood.ldap.password}")
    String GARUDAFOOD_LDAP_PASSWORD;

    @Value("${garudafood.ldap.searchAttrMail}")
    String GARUDAFOOD_LDAP_SEARCHATTRMAIL;

    @Value("${garudafood.ldap.searchAttrUsername}")
    String GARUDAFOOD_LDAP_SEARCHATTRUSERNAME;

    // Kode Report Garudafood
    @Value("${garudafood.reportcode.dailystockbysku}")
    String GARUDAFOOD_REPORTCODE_dailystockbysku;

    @Value("${garudafood.reportcode.dailystockbyloc}")
    String GARUDAFOOD_REPORTCODE_dailystockbyloc;

    @Value("${garudafood.reportcode.dailyproductionvssales}")
    String GARUDAFOOD_REPORTCODE_dailyproductionvssales;

    @Value("${garudafood.reportcode.otd1ofr1byweek.shipment}")
    String GARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment;

    @Value("${garudafood.reportcode.otd1ofr1byweek.arrival}")
    String GARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival;

    @Value("${garudafood.reportcode.otif1ofr1byweek.shipment}")
    String GARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment;

    @Value("${garudafood.reportcode.otif1ofr1byweek.arrival}")
    String GARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival;

    @Value("${garudafood.reportcode.warehouseutilization}")
    String GARUDAFOOD_REPORTCODE_warehouseutilization;

    @Value("${garudafood.reportcode.selloutsellinproductstock}")
    String GARUDAFOOD_REPORTCODE_selloutsellinproductstock;

    @Value("${garudafood.reportcode.dailytrend3m}")
    String GARUDAFOOD_REPORTCODE_dailytrend3m;

    @Value("${garudafood.shortcut.dailystockbysku}")
    String GARUDAFOOD_SHORTCUT_dailystockbysku;

    @Value("${garudafood.shortcut.dailystockbyloc}")
    String GARUDAFOOD_SHORTCUT_dailystockbyloc;

    @Value("${garudafood.shortcut.dailyproductionvssales}")
    String GARUDAFOOD_SHORTCUT_dailyproductionvssales;

    @Value("${garudafood.shortcut.otd1ofr1byweek.shipment}")
    String GARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment;

    @Value("${garudafood.shortcut.otd1ofr1byweek.arrival}")
    String GARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival;

    @Value("${garudafood.shortcut.otif1ofr1byweek.shipment}")
    String GARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment;

    @Value("${garudafood.shortcut.otif1ofr1byweek.arrival}")
    String GARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival;

    @Value("${garudafood.shortcut.warehouseutilization}")
    String GARUDAFOOD_SHORTCUT_warehouseutilization;

    @Value("${garudafood.shortcut.selloutsellinproductstock}")
    String GARUDAFOOD_SHORTCUT_selloutsellinproductstock;

    @Value("${garudafood.shortcut.dailytrend3m}")
    String GARUDAFOOD_SHORTCUT_dailytrend3m;

    //=======================//
    public String getGARUDAFOOD_BASE_URL() {
        return GARUDAFOOD_BASE_URL;
    }

    public void setGARUDAFOOD_BASE_URL(String GARUDAFOOD_BASE_URL) {
        this.GARUDAFOOD_BASE_URL = GARUDAFOOD_BASE_URL;
    }

    public String getGARUDAFOOD_API_REPORT() {
        return GARUDAFOOD_API_REPORT;
    }

    public void setGARUDAFOOD_API_REPORT(String GARUDAFOOD_API_REPORT) {
        this.GARUDAFOOD_API_REPORT = GARUDAFOOD_API_REPORT;
    }

    public String getGARUDAFOOD_URL_GENERATEDFILES() {
        return GARUDAFOOD_URL_GENERATEDFILES;
    }

    public void setGARUDAFOOD_URL_GENERATEDFILES(String GARUDAFOOD_URL_GENERATEDFILES) {
        this.GARUDAFOOD_URL_GENERATEDFILES = GARUDAFOOD_URL_GENERATEDFILES;
    }

    public String getGARUDAFOOD_PATH_GENERATEDFILES() {
        return GARUDAFOOD_PATH_GENERATEDFILES;
    }

    public void setGARUDAFOOD_PATH_GENERATEDFILES(String GARUDAFOOD_PATH_GENERATEDFILES) {
        this.GARUDAFOOD_PATH_GENERATEDFILES = GARUDAFOOD_PATH_GENERATEDFILES;
    }

    public String getGARUDAFOOD_WATERMARK_SOP() {
        return GARUDAFOOD_WATERMARK_SOP;
    }

    public void setGARUDAFOOD_WATERMARK_SOP(String GARUDAFOOD_WATERMARK_SOP) {
        this.GARUDAFOOD_WATERMARK_SOP = GARUDAFOOD_WATERMARK_SOP;
    }

    public String getGARUDAFOOD_BASE_SOP() {
        return GARUDAFOOD_BASE_SOP;
    }

    public void setGARUDAFOOD_BASE_SOP(String GARUDAFOOD_BASE_SOP) {
        this.GARUDAFOOD_BASE_SOP = GARUDAFOOD_BASE_SOP;
    }

    public String getGARUDAFOOD_WATERMARK_REPORT() {
        return GARUDAFOOD_WATERMARK_REPORT;
    }

    public void setGARUDAFOOD_WATERMARK_REPORT(String GARUDAFOOD_WATERMARK_REPORT) {
        this.GARUDAFOOD_WATERMARK_REPORT = GARUDAFOOD_WATERMARK_REPORT;
    }

    public String getGARUDAFOOD_LDAP_HOST() {
        return GARUDAFOOD_LDAP_HOST;
    }

    public void setGARUDAFOOD_LDAP_HOST(String GARUDAFOOD_LDAP_HOST) {
        this.GARUDAFOOD_LDAP_HOST = GARUDAFOOD_LDAP_HOST;
    }

    public String getGARUDAFOOD_LDAP_DIRECTORYPATH() {
        return GARUDAFOOD_LDAP_DIRECTORYPATH;
    }

    public void setGARUDAFOOD_LDAP_DIRECTORYPATH(String GARUDAFOOD_LDAP_DIRECTORYPATH) {
        this.GARUDAFOOD_LDAP_DIRECTORYPATH = GARUDAFOOD_LDAP_DIRECTORYPATH;
    }

    public String getGARUDAFOOD_LDAP_LOGINPRINCIPAL() {
        return GARUDAFOOD_LDAP_LOGINPRINCIPAL;
    }

    public void setGARUDAFOOD_LDAP_LOGINPRINCIPAL(String GARUDAFOOD_LDAP_LOGINPRINCIPAL) {
        this.GARUDAFOOD_LDAP_LOGINPRINCIPAL = GARUDAFOOD_LDAP_LOGINPRINCIPAL;
    }

    public String getGARUDAFOOD_LDAP_USERNAME() {
        return GARUDAFOOD_LDAP_USERNAME;
    }

    public void setGARUDAFOOD_LDAP_USERNAME(String GARUDAFOOD_LDAP_USERNAME) {
        this.GARUDAFOOD_LDAP_USERNAME = GARUDAFOOD_LDAP_USERNAME;
    }

    public String getGARUDAFOOD_LDAP_PASSWORD() {
        return GARUDAFOOD_LDAP_PASSWORD;
    }

    public void setGARUDAFOOD_LDAP_PASSWORD(String GARUDAFOOD_LDAP_PASSWORD) {
        this.GARUDAFOOD_LDAP_PASSWORD = GARUDAFOOD_LDAP_PASSWORD;
    }

    public String getGARUDAFOOD_LDAP_SEARCHATTRMAIL() {
        return GARUDAFOOD_LDAP_SEARCHATTRMAIL;
    }

    public void setGARUDAFOOD_LDAP_SEARCHATTRMAIL(String GARUDAFOOD_LDAP_SEARCHATTRMAIL) {
        this.GARUDAFOOD_LDAP_SEARCHATTRMAIL = GARUDAFOOD_LDAP_SEARCHATTRMAIL;
    }

    public String getGARUDAFOOD_LDAP_SEARCHATTRUSERNAME() {
        return GARUDAFOOD_LDAP_SEARCHATTRUSERNAME;
    }

    public void setGARUDAFOOD_LDAP_SEARCHATTRUSERNAME(String GARUDAFOOD_LDAP_SEARCHATTRUSERNAME) {
        this.GARUDAFOOD_LDAP_SEARCHATTRUSERNAME = GARUDAFOOD_LDAP_SEARCHATTRUSERNAME;
    }

    public String getGARUDAFOOD_REPORTCODE_dailystockbysku() {
        return GARUDAFOOD_REPORTCODE_dailystockbysku;
    }

    public void setGARUDAFOOD_REPORTCODE_dailystockbysku(String GARUDAFOOD_REPORTCODE_dailystockbysku) {
        this.GARUDAFOOD_REPORTCODE_dailystockbysku = GARUDAFOOD_REPORTCODE_dailystockbysku;
    }

    public String getGARUDAFOOD_REPORTCODE_dailystockbyloc() {
        return GARUDAFOOD_REPORTCODE_dailystockbyloc;
    }

    public void setGARUDAFOOD_REPORTCODE_dailystockbyloc(String GARUDAFOOD_REPORTCODE_dailystockbyloc) {
        this.GARUDAFOOD_REPORTCODE_dailystockbyloc = GARUDAFOOD_REPORTCODE_dailystockbyloc;
    }

    public String getGARUDAFOOD_REPORTCODE_dailyproductionvssales() {
        return GARUDAFOOD_REPORTCODE_dailyproductionvssales;
    }

    public void setGARUDAFOOD_REPORTCODE_dailyproductionvssales(String GARUDAFOOD_REPORTCODE_dailyproductionvssales) {
        this.GARUDAFOOD_REPORTCODE_dailyproductionvssales = GARUDAFOOD_REPORTCODE_dailyproductionvssales;
    }

    public String getGARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment() {
        return GARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment;
    }

    public void setGARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment(String GARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment) {
        this.GARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment = GARUDAFOOD_REPORTCODE_otd1ofr1byweekshipment;
    }

    public String getGARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival() {
        return GARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival;
    }

    public void setGARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival(String GARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival) {
        this.GARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival = GARUDAFOOD_REPORTCODE_otd1ofr1byweekarrival;
    }

    public String getGARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment() {
        return GARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment;
    }

    public void setGARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment(String GARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment) {
        this.GARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment = GARUDAFOOD_REPORTCODE_otif1ofr1byweekshipment;
    }

    public String getGARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival() {
        return GARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival;
    }

    public void setGARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival(String GARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival) {
        this.GARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival = GARUDAFOOD_REPORTCODE_otif1ofr1byweekarrival;
    }

    public String getGARUDAFOOD_REPORTCODE_warehouseutilization() {
        return GARUDAFOOD_REPORTCODE_warehouseutilization;
    }

    public void setGARUDAFOOD_REPORTCODE_warehouseutilization(String GARUDAFOOD_REPORTCODE_warehouseutilization) {
        this.GARUDAFOOD_REPORTCODE_warehouseutilization = GARUDAFOOD_REPORTCODE_warehouseutilization;
    }

    public String getGARUDAFOOD_REPORTCODE_selloutsellinproductstock() {
        return GARUDAFOOD_REPORTCODE_selloutsellinproductstock;
    }

    public void setGARUDAFOOD_REPORTCODE_selloutsellinproductstock(String GARUDAFOOD_REPORTCODE_selloutsellinproductstock) {
        this.GARUDAFOOD_REPORTCODE_selloutsellinproductstock = GARUDAFOOD_REPORTCODE_selloutsellinproductstock;
    }

    public String getGARUDAFOOD_REPORTCODE_dailytrend3m() {
        return GARUDAFOOD_REPORTCODE_dailytrend3m;
    }

    public void setGARUDAFOOD_REPORTCODE_dailytrend3m(String GARUDAFOOD_REPORTCODE_dailytrend3m) {
        this.GARUDAFOOD_REPORTCODE_dailytrend3m = GARUDAFOOD_REPORTCODE_dailytrend3m;
    }

    public String getGARUDAFOOD_SHORTCUT_dailystockbysku() {
        return GARUDAFOOD_SHORTCUT_dailystockbysku;
    }

    public void setGARUDAFOOD_SHORTCUT_dailystockbysku(String GARUDAFOOD_SHORTCUT_dailystockbysku) {
        this.GARUDAFOOD_SHORTCUT_dailystockbysku = GARUDAFOOD_SHORTCUT_dailystockbysku;
    }

    public String getGARUDAFOOD_SHORTCUT_dailystockbyloc() {
        return GARUDAFOOD_SHORTCUT_dailystockbyloc;
    }

    public void setGARUDAFOOD_SHORTCUT_dailystockbyloc(String GARUDAFOOD_SHORTCUT_dailystockbyloc) {
        this.GARUDAFOOD_SHORTCUT_dailystockbyloc = GARUDAFOOD_SHORTCUT_dailystockbyloc;
    }

    public String getGARUDAFOOD_SHORTCUT_dailyproductionvssales() {
        return GARUDAFOOD_SHORTCUT_dailyproductionvssales;
    }

    public void setGARUDAFOOD_SHORTCUT_dailyproductionvssales(String GARUDAFOOD_SHORTCUT_dailyproductionvssales) {
        this.GARUDAFOOD_SHORTCUT_dailyproductionvssales = GARUDAFOOD_SHORTCUT_dailyproductionvssales;
    }

    public String getGARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment() {
        return GARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment;
    }

    public void setGARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment(String GARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment) {
        this.GARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment = GARUDAFOOD_SHORTCUT_otd1ofr1byweekshipment;
    }

    public String getGARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival() {
        return GARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival;
    }

    public void setGARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival(String GARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival) {
        this.GARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival = GARUDAFOOD_SHORTCUT_otd1ofr1byweekarrival;
    }

    public String getGARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment() {
        return GARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment;
    }

    public void setGARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment(String GARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment) {
        this.GARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment = GARUDAFOOD_SHORTCUT_otif1ofr1byweekshipment;
    }

    public String getGARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival() {
        return GARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival;
    }

    public void setGARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival(String GARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival) {
        this.GARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival = GARUDAFOOD_SHORTCUT_otif1ofr1byweekarrival;
    }

    public String getGARUDAFOOD_SHORTCUT_warehouseutilization() {
        return GARUDAFOOD_SHORTCUT_warehouseutilization;
    }

    public void setGARUDAFOOD_SHORTCUT_warehouseutilization(String GARUDAFOOD_SHORTCUT_warehouseutilization) {
        this.GARUDAFOOD_SHORTCUT_warehouseutilization = GARUDAFOOD_SHORTCUT_warehouseutilization;
    }

    public String getGARUDAFOOD_SHORTCUT_selloutsellinproductstock() {
        return GARUDAFOOD_SHORTCUT_selloutsellinproductstock;
    }

    public void setGARUDAFOOD_SHORTCUT_selloutsellinproductstock(String GARUDAFOOD_SHORTCUT_selloutsellinproductstock) {
        this.GARUDAFOOD_SHORTCUT_selloutsellinproductstock = GARUDAFOOD_SHORTCUT_selloutsellinproductstock;
    }

    public String getGARUDAFOOD_SHORTCUT_dailytrend3m() {
        return GARUDAFOOD_SHORTCUT_dailytrend3m;
    }

    public void setGARUDAFOOD_SHORTCUT_dailytrend3m(String GARUDAFOOD_SHORTCUT_dailytrend3m) {
        this.GARUDAFOOD_SHORTCUT_dailytrend3m = GARUDAFOOD_SHORTCUT_dailytrend3m;
    }

    // --------- //
    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailStmpAuth() {
        return mailStmpAuth;
    }

    public void setMailStmpAuth(String mailStmpAuth) {
        this.mailStmpAuth = mailStmpAuth;
    }

    public String getMailSmtpTls() {
        return mailSmtpTls;
    }

    public void setMailSmtpTls(String mailSmtpTls) {
        this.mailSmtpTls = mailSmtpTls;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public String getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(String mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    /**
     * @return the sdkConnectTimeout
     */
    public String getSdkConnectTimeout() {
        return sdkConnectTimeout;
    }

    /**
     * @param sdkConnectTimeout the sdkConnectTimeout to set
     */
    public void setSdkConnectTimeout(String sdkConnectTimeout) {
        this.sdkConnectTimeout = sdkConnectTimeout;
    }

    /**
     * @return the sdkReadTimeout
     */
    public String getSdkReadTimeout() {
        return sdkReadTimeout;
    }

    /**
     * @param sdkReadTimeout the sdkReadTimeout to set
     */
    public void setSdkReadTimeout(String sdkReadTimeout) {
        this.sdkReadTimeout = sdkReadTimeout;
    }

    /**
     * @return the sdkDolphinUsername
     */
    public String getSdkDolphinUsername() {
        return sdkDolphinUsername;
    }

    /**
     * @param sdkDolphinUsername the sdkDolphinUsername to set
     */
    public void setSdkDolphinUsername(String sdkDolphinUsername) {
        this.sdkDolphinUsername = sdkDolphinUsername;
    }

    /**
     * @return the sdkDolphinPassword
     */
    public String getSdkDolphinPassword() {
        return sdkDolphinPassword;
    }

    /**
     * @param sdkDolphinPassword the sdkDolphinPassword to set
     */
    public void setSdkDolphinPassword(String sdkDolphinPassword) {
        this.sdkDolphinPassword = sdkDolphinPassword;
    }

    /**
     * @return the sdkDolphinBaseUrl
     */
    public String getSdkDolphinBaseUrl() {
        return sdkDolphinBaseUrl;
    }

    /**
     * @param sdkDolphinBaseUrl the sdkDolphinBaseUrl to set
     */
    public void setSdkDolphinBaseUrl(String sdkDolphinBaseUrl) {
        this.sdkDolphinBaseUrl = sdkDolphinBaseUrl;
    }

    /**
     * @return the sdkDolphinGraphPing
     */
    public String getSdkDolphinGraphPing() {
        return sdkDolphinGraphPing;
    }

    /**
     * @param sdkDolphinGraphPing the sdkDolphinGraphPing to set
     */
    public void setSdkDolphinGraphPing(String sdkDolphinGraphPing) {
        this.sdkDolphinGraphPing = sdkDolphinGraphPing;
    }

    /**
     * @return the sdkDolphinGraphAuth
     */
    public String getSdkDolphinGraphAuth() {
        return sdkDolphinGraphAuth;
    }

    /**
     * @param sdkDolphinGraphAuth the sdkDolphinGraphAuth to set
     */
    public void setSdkDolphinGraphAuth(String sdkDolphinGraphAuth) {
        this.sdkDolphinGraphAuth = sdkDolphinGraphAuth;
    }

    /**
     * @return the sdkDolphinGraphAuthToken
     */
    public String getSdkDolphinGraphAuthToken() {
        return sdkDolphinGraphAuthToken;
    }

    /**
     * @param sdkDolphinGraphAuthToken the sdkDolphinGraphAuthToken to set
     */
    public void setSdkDolphinGraphAuthToken(String sdkDolphinGraphAuthToken) {
        this.sdkDolphinGraphAuthToken = sdkDolphinGraphAuthToken;
    }

    /**
     * @return the sdkDolphinGraphContacts
     */
    public String getSdkDolphinGraphContacts() {
        return sdkDolphinGraphContacts;
    }

    /**
     * @param sdkDolphinGraphContacts the sdkDolphinGraphContacts to set
     */
    public void setSdkDolphinGraphContacts(String sdkDolphinGraphContacts) {
        this.sdkDolphinGraphContacts = sdkDolphinGraphContacts;
    }

    /**
     * @return the sdkDolphinGraphContactsUpdate
     */
    public String getSdkDolphinGraphContactsUpdate() {
        return sdkDolphinGraphContactsUpdate;
    }

    /**
     * @param sdkDolphinGraphContactsUpdate the sdkDolphinGraphContactsUpdate to
     * set
     */
    public void setSdkDolphinGraphContactsUpdate(String sdkDolphinGraphContactsUpdate) {
        this.sdkDolphinGraphContactsUpdate = sdkDolphinGraphContactsUpdate;
    }

}
