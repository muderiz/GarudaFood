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
package com.imi.dolphin.sdkwebservice.serviceReport;

import com.imi.dolphin.sdkwebservice.service.*;
import com.imi.dolphin.sdkwebservice.param.ParamJSONReport;
import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.dolphin.sdkwebservice.builder.ButtonBuilder;
import com.imi.dolphin.sdkwebservice.builder.CarouselBuilder;
import com.imi.dolphin.sdkwebservice.builder.FormBuilder;
import com.imi.dolphin.sdkwebservice.builder.ImageBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Depo;
import com.imi.dolphin.sdkwebservice.model.EasyMap;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.EasyParam;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Group;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.InfoUser;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.LdapConnection;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.LdapModel;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.LoopParam;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterDepartment;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterGroupProduct;
import com.imi.dolphin.sdkwebservice.model.MailModel;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Region;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportRequest;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportResult;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Role;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Product;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamSdk;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.io.FileOutputStream;
import java.net.URL;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import javax.naming.ldap.LdapContext;
import okhttp3.MediaType;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;

/**
 *
 * @author reja
 *
 */
@Service
public class ServiceImpReport {

    private static final Logger log = LogManager.getLogger(ServiceImpReport.class);

    private static final String OUTPUT = "output";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    private static final String roleJson = "fileJson/report/role.json";

    @Autowired
    AppProperties appProp;

    @Autowired
    IMailService svcMailService;

    @Autowired
    IDolphinService svcDolphinService;

    @Autowired
    AuthService svcAuthService;

    @Autowired
    OkHttpUtil okHttpUtil;

    @Autowired
    SdkUtil sdkUtil;

    @Autowired
    GenerateWatermark generateWatermark;

    @Autowired
    Gson gson;

    @Autowired
    private ParamJSONReport paramJSON;

    @Autowired
    GetListJsonReport getListJsonReport;

    // Start Method for Each Report //
    private Boolean CekNumber(String a) {
        log.debug("CekNumber() dari user di tangkap BOT {} ", a);
        boolean result = true;
        if (a.matches("[0-9]*")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public ExtensionResult report_setFirstStatusCode(ExtensionRequest extensionRequest) {
        log.debug("report_setFirstStatusCode() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        clearEntities.put("status_code", "0");
        clearEntities.put("index", "0");
        clearEntities.put("nomorurut", "1");

        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult report_namaReport(ExtensionRequest extensionRequest) {
        log.debug("report_namaReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> clearEntities = new HashMap<>();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");

        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getUserToken() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getContactId() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getContact() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //
        String b = "";
        switch (status_code) {
            case "0":
                try {
                    b = contact.getAdditionalField().get(0);
                    System.out.println("Print AdditionalField Contact");
                    System.out.println(b);
                    if (!b.equalsIgnoreCase("")) {
                        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                        String usernameUser = dataInfoUser.getAccountName();
                        String fullname = dataInfoUser.getFullName();
                        String mail = dataInfoUser.getMail();
                        String title = "";
                        String company = "";
                        String division = "";
                        String department = "";
                        if (!dataInfoUser.getCompany().equalsIgnoreCase("")
                                || !dataInfoUser.getTitle().equalsIgnoreCase("")
                                || !dataInfoUser.getDivision().equalsIgnoreCase("")
                                || !dataInfoUser.getDepartment().equalsIgnoreCase("")) {
                            company = dataInfoUser.getCompany();
                            title = dataInfoUser.getTitle();
                            division = dataInfoUser.getDivision();
                            department = dataInfoUser.getDepartment();
                        }

                        List<Role> listRole = paramJSON.getListRolefromFileJson(roleJson);

                        int lengList = listRole.size();
                        for (int i = 0; i < lengList; i++) {
                            Role roleArray = listRole.get(i);
                            String roleUsername = roleArray.username;
                            if (roleUsername.equalsIgnoreCase(usernameUser)) {
                                title = roleArray.title;
                                company = roleArray.company;
                                division = roleArray.division;
                                department = roleArray.department;
                                break;
                            } else {
                                title = "management";
                                company = "GPPJ";
                                division = "SCM";
                                department = "All";
                            }
                        }

                        StringBuilder sb = new StringBuilder();
                        String dialog = "Baiklah, Bapak/Ibu " + fullname + " sudah berada di menu Report.";
                        String dialogopsi = "Sekarang Bapak/Ibu " + fullname + " ingin melihat Report apa?\n";
                        sb.append(dialogopsi);
                        String dialogButton = "";

                        List<String> listReportNamebyFilter = new ArrayList<>();
                        listReportNamebyFilter = getListJsonReport.reportNameGeneral();

                        ButtonTemplate button = new ButtonTemplate();
                        List<EasyMap> actions = new ArrayList<>();

                        ButtonBuilder buttonBuilder;
                        button.setTitle("");
                        button.setSubTitle("");
                        int i = 0;
                        int urutan = 1;
                        int lengReportName = listReportNamebyFilter.size();
                        int newlengReportName;
                        newlengReportName = 0;
                        if (lengReportName
                                > 5) {
                            newlengReportName = 5;
                        } else {
                            newlengReportName = lengReportName;
                        }
                        for (i = 0; i < newlengReportName; i++) {
                            String NameReport = listReportNamebyFilter.get(i);
                            String[] splitname = NameReport.split("_M");
                            String reportcode = splitname[0];
                            String reportname = splitname[1];
                            sb.append(urutan + ". " + reportname + "\n");

                            EasyMap bookAction = new EasyMap();
                            bookAction.setName(urutan + "");
                            bookAction.setValue(reportcode.toLowerCase());
                            actions.add(bookAction);
                            urutan++;
                        }
                        if (lengReportName > newlengReportName) {
                            dialogButton = "Silakan pilih angka yang di inginkan. Atau klik \"Next\" untuk melihat Report lainnya";

                            EasyMap bookAction = new EasyMap();
                            bookAction.setName("Next");
                            bookAction.setValue("next");
                            actions.add(bookAction);

                            clearEntities.put("index", i + "");
                            clearEntities.put("nomorurut", urutan + "");
                        } else {
                            dialogButton = "Silakan pilih angka yang di inginkan.";
                        }

                        button.setButtonValues(actions);
                        buttonBuilder = new ButtonBuilder(button);

                        sb.append(SPLIT).append(dialogButton).append(SPLIT).append(buttonBuilder.build());
                        output.put(OUTPUT, dialog + SPLIT + sb.toString());

                        // ============== Set AdditionalField ============ //
                        InfoUser infouser = new InfoUser();
                        infouser.setAccountName(usernameUser);
                        infouser.setFullName(fullname);
                        infouser.setMail(mail);
                        infouser.setTitle(title);
                        infouser.setDepartment(department);
                        infouser.setCompany(company);
                        infouser.setDivision(division);
                        List<String> listData = new ArrayList<>();

                        listData.add("" + new Gson().toJson(infouser, InfoUser.class) + "");
                        contact.setAdditionalField(listData);
                        contact = svcDolphinService.updateCustomer(userToken, contact);
                        // ============================================== //

                    } else {
                        String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                                .add("Verifikasi Akun", "verifikasi").build();

                        output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                        clearEntities.put("status_code", "2");

                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                            .add("Verifikasi Akun", "verifikasi").build();

                    output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                    clearEntities.put("status_code", "2");

                }
                break;
            case "1":
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                b = contact.getAdditionalField().get(0);
                InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                String fullname = dataInfoUser.getFullName();

                StringBuilder sb = new StringBuilder();
                String dialogopsi = "Bapak/Ibu " + fullname + " ingin melihat Report apa?\n";
                sb.append(dialogopsi);
                String dialogButton = "";
                List<String> listReportNamebyFilter = new ArrayList<>();
                listReportNamebyFilter = getListJsonReport.reportNameGeneral();

                ButtonTemplate button = new ButtonTemplate();
                List<EasyMap> actions = new ArrayList<>();

                ButtonBuilder buttonBuilder;
                button.setTitle("");
                button.setSubTitle("");
                int i = Integer.parseInt(index);
                int urutan = Integer.parseInt(nomorurut);
                int lengReportName = listReportNamebyFilter.size();
                int newlengReportName = lengReportName - i;
                newlengReportName = 0;
                if (newlengReportName > 5) {
                    newlengReportName = 5;
                    newlengReportName = i + newlengReportName;
                } else {
                    newlengReportName = lengReportName;
                }
                for (i = i; i < newlengReportName; i++) {

                    String NameReport = listReportNamebyFilter.get(i);
                    String[] splitname = NameReport.split("_M");
                    String reportcode = splitname[0];
                    String reportname = splitname[1];
                    sb.append(urutan + ". " + reportname + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(reportcode.toLowerCase());
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengReportName > newlengReportName) {
                    dialogButton = "Silakan pilih angka yang di inginkan. Atau klik \"Next\" untuk melihat Report lainnya";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    dialogButton = "Silakan pilih angka yang di inginkan.";
                }

                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);

                sb.append(SPLIT).append(dialogButton).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan Verifikasi Akun.")
                        .add("Verifikasi Akun", "verifikasi").build();

                output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                break;
            case "3":
                b = contact.getAdditionalField().get(0);
                dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                fullname = dataInfoUser.getFullName();
                sb = new StringBuilder();
                String dialog = "Baiklah, Bapak/Ibu " + fullname + " sudah berada di menu Report.";
                dialogopsi = "Sekarang Bapak/Ibu " + fullname + " ingin melihat Report apa?\n";
                sb.append(dialogopsi);
                dialogButton = "";

                listReportNamebyFilter = getListJsonReport.reportNameGeneral();

                button = new ButtonTemplate();
                actions = new ArrayList<>();

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengReportName = listReportNamebyFilter.size();
                newlengReportName = 0;
                if (lengReportName
                        > 5) {
                    newlengReportName = 5;
                } else {
                    newlengReportName = lengReportName;
                }
                for (i = 0; i < newlengReportName; i++) {
                    String NameReport = listReportNamebyFilter.get(i);
                    String[] splitname = NameReport.split("_M");
                    String reportcode = splitname[0];
                    String reportname = splitname[1];
                    sb.append(urutan + ". " + reportname + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(reportcode.toLowerCase());
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengReportName > newlengReportName) {
                    dialogButton = "Silakan pilih angka yang di inginkan. Atau klik \"Next\" untuk melihat Report lainnya";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    dialogButton = "Silakan pilih angka yang di inginkan.";
                }

                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);

                sb.append(SPLIT).append(dialogButton).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, dialog + SPLIT + sb.toString());

                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        return extensionResult;
    }

    public ExtensionResult report_validasiNamaReport(ExtensionRequest extensionRequest) {
        log.debug("report_validasiNamaReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String nama_report = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");

        if (nama_report.equalsIgnoreCase("next")) {
            clearEntities.put("nama_report", "");
            clearEntities.put("status_code", "1");
        } else if (status_code.equalsIgnoreCase("2")) {
            clearEntities.put("nama_report", "");
        } else {
            clearEntities.put("nama_report", "");
            clearEntities.put("status_code", "3");
        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

}
