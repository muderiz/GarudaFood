/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceSOP;

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.InfoUser;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SOP;
import com.imi.dolphin.sdkwebservice.builder.ButtonBuilder;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
import com.imi.dolphin.sdkwebservice.model.EasyMap;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamJSONSop;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.AuthService;
import com.imi.dolphin.sdkwebservice.service.GenerateWatermark;
import com.imi.dolphin.sdkwebservice.service.IDolphinService;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceImpReport;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ServiceSOP {

    private static final Logger log = LogManager.getLogger(ServiceImpReport.class);

    private static final String OUTPUT = "output";
//    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    private static final String sopJson = "fileJson/sop/sop.json";
    private static final String snsJson = "fileJson/sop/sns.json";
    private static final String hoJson = "fileJson/sop/ho.json";
    private static final String bcuJson = "fileJson/sop/bc u.json";
    private static final String mastercompanyJson = "fileJson/sop/mastercompany.json";
    private static final String QUICK_REPLY_SYNTAX = "{replies:title=";
    private static final String QUICK_REPLY_SYNTAX_SUFFIX = "}";
    private static final String COMMA = ",";

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
    private ParamJSONSop paramJSON;

    @Autowired
    GetListJsonSOP getListJsonSOP;

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

    public ExtensionResult sop_setFirstStatusCode(ExtensionRequest extensionRequest) {
        log.debug("sop_setFirstStatusCode() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
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

    public ExtensionResult sop_Company(ExtensionRequest extensionRequest) {
        log.debug("sop_Company() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));

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
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //
        String fullName = "";
        List<String> listCompany = new ArrayList<>();
        listCompany = getListJsonSOP.companyGeneral();
        switch (status_code) {
            case "0":
                try {
                    String b = contact.getAdditionalField().get(0);
                    if (!b.equalsIgnoreCase("")) {
                        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                        fullName = dataInfoUser.getFullName();

                        StringBuilder sb = new StringBuilder();
                        String dialog = "Baiklah, Bapak/Ibu " + fullName + " sudah berada di menu SOP\n";
                        sb.append(dialog);

                        ButtonTemplate button = new ButtonTemplate();
                        button.setTitle("");
                        button.setSubTitle("");
                        List<EasyMap> actions = new ArrayList<>();
                        int i = 0;
                        int urutan = 1;
                        int lengCompany = listCompany.size();
                        for (i = 0; i < lengCompany; i++) {
                            String companyName = listCompany.get(i);
                            sb.append(urutan + ". " + companyName + "\n");

                            EasyMap bookAction = new EasyMap();
                            bookAction.setName(urutan + "");
                            bookAction.setValue(companyName);
                            actions.add(bookAction);
                            urutan++;
                        }
                        button.setButtonValues(actions);
                        ButtonBuilder buttonBuilder = new ButtonBuilder(button);
                        String title = "Sekarang Bapak/Ibu " + fullName + " ingin melihat SOP dari Company apa?";
                        sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                        output.put(OUTPUT, sb.toString());

                    } else {
                        String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                                .add("Verifikasi Akun", "verifikasi").build();
                        output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                        clearEntities.put("company", "verifikasi");
                        clearEntities.put("divisi", "verifikasi");
                        clearEntities.put("jenisdokumen", "verifikasi");
                        clearEntities.put("namadokumen", "verifikasi");
                        clearEntities.put("sop", "verifikasi");
                    }
                } catch (Exception ex) {
                    log.debug("sop_Company() Exception: {}", ex);

                    String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                            .add("Verifikasi Akun", "verifikasi").build();
                    output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                    clearEntities.put("company", "verifikasi");
                    clearEntities.put("divisi", "verifikasi");
                    clearEntities.put("jenisdokumen", "verifikasi");
                    clearEntities.put("namadokumen", "verifikasi");
                    clearEntities.put("sop", "verifikasi");

                }
                break;
            case "2":
                String b = contact.getAdditionalField().get(0);
                InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                fullName = dataInfoUser.getFullName();

                StringBuilder sb = new StringBuilder();
                String dialog = "Maaf Kami tidak dapat menemukan Company yang Anda inginkan.\n";
                sb.append(dialog);

                ButtonTemplate button = new ButtonTemplate();
                button.setTitle("");
                button.setSubTitle("");
                List<EasyMap> actions = new ArrayList<>();
                int i = 0;
                int urutan = 1;
                int lengCompany = listCompany.size();
                for (i = i; i < lengCompany; i++) {
                    String companyName = listCompany.get(i);
                    sb.append(urutan + ". " + companyName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(companyName);
                    actions.add(bookAction);
                    urutan++;
                }
                button.setButtonValues(actions);
                ButtonBuilder buttonBuilder = new ButtonBuilder(button);
                String title = "Silakan pilih kembali angka yang Anda inginkan.";
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        return extensionResult;
    }

    public ExtensionResult sop_Divisi(ExtensionRequest extensionRequest) {
        log.debug("sop_Divisi() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        StringBuilder sb = new StringBuilder();
        // ============== Get AdditionalField ============ //
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
        // ============================================== //
        String namecompany = "";

        List<String> listCompany = new ArrayList<>();
        listCompany = getListJsonSOP.companyGeneral();
        boolean cekAngka = CekNumber(company);
        if (cekAngka == true) {
            int i = Integer.parseInt(company) - 1;
            namecompany = listCompany.get(i);
        } else {
            int lengCompany = listCompany.size();
            for (int i = 0; i < lengCompany; i++) {
                String companyName = listCompany.get(i);
                if (companyName.equalsIgnoreCase(company)) {
                    namecompany = companyName;
                    break;
                }
            }
        }
        List<String> listDivisi = new ArrayList<>();
        listDivisi = getListJsonSOP.divisiGeneral(namecompany);
        String dialog = "";
        String title = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();
        int i = 0;
        int urutan = 1;
        int lengDivisi = 0;
        int newlengDivisi = 0;
        ButtonBuilder buttonBuilder;
        switch (status_code) {
            case "0":
                if (namecompany.equalsIgnoreCase("")) {
                    clearEntities.put("company", "");
                    clearEntities.put("status_code", "2");
                } else {
                    dialog = "Anda telah memilih Company " + namecompany + ". Selanjutnya Divisi apa yang ingin Bapak/Ibu " + fullName + " lihat?\n";
                    sb.append(dialog);

                    i = 0;
                    urutan = 1;
                    lengDivisi = listDivisi.size();
                    newlengDivisi = 0;
                    if (lengDivisi > 5) {
                        newlengDivisi = 5;
                    } else {
                        newlengDivisi = lengDivisi;
                    }
                    button.setTitle("");
                    button.setSubTitle("");
                    for (i = i; i < newlengDivisi; i++) {
                        String divisiName = listDivisi.get(i);
                        sb.append(urutan + ". " + divisiName + "\n");

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName(urutan + "");
                        bookAction.setValue(divisiName);
                        actions.add(bookAction);
                        urutan++;
                    }

                    if (lengDivisi > newlengDivisi) {
                        title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Divisi lainnya.";
                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = "Silakan pilih angka yang anda inginkan.";

                    }
                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());
                    clearEntities.put("company", namecompany);
                }
                break;
            case "1":
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");

                urutan = Integer.parseInt(nomorurut);
                i = Integer.parseInt(index);
                lengDivisi = listDivisi.size();
                newlengDivisi = lengDivisi - i;

                if (newlengDivisi > 5) {
                    newlengDivisi = 5;
                    newlengDivisi = i + newlengDivisi;
                } else {
                    newlengDivisi = lengDivisi;
                }
                dialog = "Anda telah memilih Company " + namecompany + ". Selanjutnya Divisi apa yang ingin Bapak/Ibu " + fullName + " lihat?\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                for (i = i; i < newlengDivisi; i++) {
                    String divisiName = listDivisi.get(i);
                    sb.append(urutan + ". " + divisiName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(divisiName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengDivisi > newlengDivisi) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Divisi lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                dialog = "Maaf Kami tidak dapat menemukan Divisi yang Anda inginkan.\n";
                sb.append(dialog);

                i = 0;
                urutan = 1;
                lengDivisi = listDivisi.size();
                newlengDivisi = 0;
                if (lengDivisi > 5) {
                    newlengDivisi = 5;
                } else {
                    newlengDivisi = lengDivisi;
                }
                button.setTitle("");
                button.setSubTitle("");
                for (i = i; i < newlengDivisi; i++) {
                    String divisiName = listDivisi.get(i);
                    sb.append(urutan + ". " + divisiName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(divisiName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengDivisi > newlengDivisi) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Divisi lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_JenisDokumen(ExtensionRequest extensionRequest) {
        log.debug("sop_JenisDokumen() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");

        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        // ============== Get AdditionalField ============ //
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
        // ============================================== //
        List<String> listDivisi = new ArrayList<>();
        listDivisi = getListJsonSOP.divisiGeneral(company);
        String nameDivisi = "";
        boolean cekAngka = CekNumber(divisi);
        if (cekAngka == true) {
            int i = Integer.parseInt(divisi) - 1;
            nameDivisi = listDivisi.get(i);
        } else {
            int lengDivisi = listDivisi.size();
            for (int i = 0; i < lengDivisi; i++) {
                String divisiName = listDivisi.get(i);
                if (divisiName.equalsIgnoreCase(divisi)) {
                    nameDivisi = divisiName;
                    break;
                }
            }
        }
        List<String> listJenisDokumen = new ArrayList<>();
        listJenisDokumen = getListJsonSOP.jenisDokumenGeneral(company, nameDivisi);
        String dialog = "";
        String title = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();
        int i = 0;
        int urutan = 1;
        int lengJenisDokumen = 0;
        int newlengJenisDokumen = 0;
        ButtonBuilder buttonBuilder;
        switch (status_code) {
            case "0":
                if (divisi.equalsIgnoreCase("next")) {
                    clearEntities.put("divisi", "");
                    clearEntities.put("status_code", "1");
                } else if (nameDivisi.equalsIgnoreCase("")) {
                    clearEntities.put("divisi", "");
                    clearEntities.put("status_code", "2");
                } else {

                    dialog = "Anda telah memilih Divisi " + nameDivisi + ". Selanjutnya Jenis Dokumen apa yang ingin Bapak/Ibu " + fullName + " lihat?\n";
                    sb.append(dialog);

                    i = 0;
                    urutan = 1;
                    lengJenisDokumen = listJenisDokumen.size();
                    newlengJenisDokumen = 0;
                    if (lengJenisDokumen > 5) {
                        newlengJenisDokumen = 5;
                    } else {
                        newlengJenisDokumen = lengJenisDokumen;
                    }
                    button.setTitle("");
                    button.setSubTitle("");
                    for (i = i; i < newlengJenisDokumen; i++) {
                        String jenisDokumen = listJenisDokumen.get(i);
                        sb.append(urutan + ". " + jenisDokumen + "\n");

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName(urutan + "");
                        bookAction.setValue(jenisDokumen);
                        actions.add(bookAction);
                        urutan++;
                    }

                    if (lengJenisDokumen > newlengJenisDokumen) {
                        title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Jenis Dokumen lainnya.";
                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = "Silakan pilih angka yang anda inginkan.";

                    }
                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());
                    clearEntities.put("divisi", nameDivisi);
                }
                break;
            case "1":
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");

                urutan = Integer.parseInt(nomorurut);
                i = Integer.parseInt(index);
                lengJenisDokumen = listJenisDokumen.size();
                newlengJenisDokumen = lengJenisDokumen - i;

                if (newlengJenisDokumen > 5) {
                    newlengJenisDokumen = 5;
                    newlengJenisDokumen = i + newlengJenisDokumen;
                } else {
                    newlengJenisDokumen = lengJenisDokumen;
                }
                dialog = "Anda telah memilih Divisi " + nameDivisi + ". Selanjutnya Jenis Dokumen apa yang ingin " + fullName + " lihat?\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                for (i = i; i < newlengJenisDokumen; i++) {
                    String divisiName = listDivisi.get(i);
                    sb.append(urutan + ". " + divisiName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(divisiName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengJenisDokumen > newlengJenisDokumen) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Jenis Dokumen lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                dialog = "Maaf Kami tidak dapat menemukan Jenis Dokumen yang Anda inginkan.\n";
                sb.append(dialog);

                i = 0;
                urutan = 1;
                lengJenisDokumen = listJenisDokumen.size();
                newlengJenisDokumen = 0;
                if (lengJenisDokumen > 5) {
                    newlengJenisDokumen = 5;
                } else {
                    newlengJenisDokumen = lengJenisDokumen;
                }
                button.setTitle("");
                button.setSubTitle("");
                for (i = i; i < newlengJenisDokumen; i++) {
                    String jenisDokumen = listJenisDokumen.get(i);
                    sb.append(urutan + ". " + jenisDokumen + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(jenisDokumen);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengJenisDokumen > newlengJenisDokumen) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Jenis Dokumen lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_tanyaNamaDokumen(ExtensionRequest extensionRequest) {
        log.debug("sop_NamaDokumen() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
        String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");
        StringBuilder sb = new StringBuilder();

//         ============== Get AdditionalField ============ //
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
//        String fullName = "Deka";
        // ============================================== //

        List<String> listJenisDokumen = new ArrayList<>();
        listJenisDokumen = getListJsonSOP.jenisDokumenGeneral(company, divisi);
        String nameJenisDokumen = "";
        boolean cekAngka = CekNumber(jenisdokumen);
        if (cekAngka == true) {
            int i = Integer.parseInt(jenisdokumen) - 1;
            nameJenisDokumen = listJenisDokumen.get(i);
        } else {
            int lengJenisDokumen = listJenisDokumen.size();
            for (int i = 0; i < lengJenisDokumen; i++) {
                String jenisdokumenName = listJenisDokumen.get(i);
                if (jenisdokumenName.equalsIgnoreCase(jenisdokumen)) {
                    nameJenisDokumen = jenisdokumenName;
                    break;
                }
            }
        }
        List<String> listNamaDokumen = new ArrayList<>();
        listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, nameJenisDokumen, divisi);
        String dialog = "";
        String title = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();
        int i = 0;
        int urutan = 1;
        int lengNamaDokumen = 0;
        int newlengNamaDokumen = 0;
        ButtonBuilder buttonBuilder;
        switch (status_code) {
            case "0":
                if (jenisdokumen.equalsIgnoreCase("next")) {
                    clearEntities.put("jenisdokumen", "");
                    clearEntities.put("status_code", "1");
                } else if (nameJenisDokumen.equalsIgnoreCase("")) {
                    clearEntities.put("jenisdokumen", "");
                    clearEntities.put("status_code", "2");
                } else {
                    title = "Silakan ketik " + nameJenisDokumen.toUpperCase() + " yang anda inginkan. Atau klik dibawah ini untuk melihat list " + nameJenisDokumen.toUpperCase() + " yang ada di kategori ini.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder(title)
                            .add("View ALL", "view all").build();

                    output.put(OUTPUT, quickReplyBuilder.string());
                    clearEntities.put("jenisdokumen", nameJenisDokumen);
                    clearEntities.put("tanya_namadokumen", "SKIP");
                    clearEntities.put("nomorurut", "1");
                    clearEntities.put("index", "0");
                }
                break;
            case "1":
                dialog = "Berikut adalah pilihan Dokumen " + jenisdokumen + " yang bisa Bapak/Ibu " + fullName + " lihat :\n";
                sb.append(dialog);

                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengNamaDokumen = listNamaDokumen.size();
                newlengNamaDokumen = lengNamaDokumen - i;
                if (newlengNamaDokumen >= 5) {
                    newlengNamaDokumen = 5;
                    newlengNamaDokumen = i + newlengNamaDokumen;
                } else {
                    newlengNamaDokumen = lengNamaDokumen;
                }
                button.setTitle("");
                button.setSubTitle(" ");
                for (i = i; i < newlengNamaDokumen; i++) {
                    String namaDokumenName = listNamaDokumen.get(i);
                    sb.append(urutan + ". " + namaDokumenName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(namaDokumenName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengNamaDokumen > newlengNamaDokumen) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Nama Dokumen lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";
                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("tanya_namadokumen", "SKIP");
                clearEntities.put("status_code", "0");
                break;
            case "2":
                String namadokumen = nomorurut;
                namadokumen = namadokumen.toLowerCase();
                String nameDokumen = "";

                urutan = 1;
                int lengDokumen = listNamaDokumen.size();
                button.setTitle("");
                button.setSubTitle(" ");
                for (i = 0; i < lengDokumen; i++) {
                    String dokumenname = listNamaDokumen.get(i);
                    dokumenname = dokumenname.toLowerCase();
                    String namaDoc = listNamaDokumen.get(i);
                    if (dokumenname.equalsIgnoreCase(namadokumen)) {
                        nameDokumen = dokumenname;
                        break;
                    } else if (dokumenname.contains(namadokumen)) {
                        nameDokumen = namaDoc;
                        sb.append(urutan + ". " + nameDokumen + "\n");

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName(urutan + "");
                        bookAction.setValue(nameDokumen);
                        actions.add(bookAction);
                        urutan++;

                    }
                }

                if (sb.toString().equalsIgnoreCase("")) {
                    dialog = "Maaf, tidak dapat menemukan Nama Dokumen tersebut.";
                    title = "Silakan ketik kembali Dokumen " + jenisdokumen + " yang anda inginkan. Atau klik dibawah ini untuk melihat list Dokumen " + jenisdokumen + " yang ada di kategori ini.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder(title)
                            .add("View ALL", "view all").build();

                    output.put(OUTPUT, dialog + SPLIT + quickReplyBuilder.string());

                } else {
                    dialog = "Apa Dokumen " + jenisdokumen + " berikut yang anda maksud ?\n";
                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    title = "Silakan pilih nama Dokumen " + jenisdokumen + " sesuai yang Anda inginkan.";
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, dialog + SPLIT + sb.toString());
                }

                clearEntities.put("tanya_namadokumen", "SKIP");
                clearEntities.put("status_code", "0");
                break;
            case "3":
                dialog = "Berikut adalah pilihan Dokumen " + jenisdokumen + " yang bisa Bapak/Ibu " + fullName + " lihat :\n";
                sb.append(dialog);

                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengNamaDokumen = listNamaDokumen.size();
                newlengNamaDokumen = lengNamaDokumen - i;
                if (newlengNamaDokumen >= 5) {
                    newlengNamaDokumen = 5;
                    newlengNamaDokumen = i + newlengNamaDokumen;
                } else {
                    newlengNamaDokumen = lengNamaDokumen;
                }

                button.setTitle("");
                button.setSubTitle(" ");
                for (i = i; i < newlengNamaDokumen; i++) {
                    String namaDokumenName = listNamaDokumen.get(i);
                    sb.append(urutan + ". " + namaDokumenName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(namaDokumenName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengNamaDokumen > newlengNamaDokumen) {
                    title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Nama Dokumen lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("tanya_namadokumen", "SKIP");
                clearEntities.put("status_code", "0");

                break;
            case "4":
                dialog = "Maaf Dokumen yang Bapak/Ibu " + fullName + " ingin lihat belum tersedia :\n\n";
                sb.append(dialog);

                i = 0;
                urutan = 1;
                lengNamaDokumen = listNamaDokumen.size();
                newlengNamaDokumen = lengNamaDokumen - i;
                if (newlengNamaDokumen >= 5) {
                    newlengNamaDokumen = 5;
                    newlengNamaDokumen = i + newlengNamaDokumen;
                } else {
                    newlengNamaDokumen = lengNamaDokumen;
                }

                button.setTitle("");
                button.setSubTitle(" ");
                for (i = i; i < newlengNamaDokumen; i++) {
                    String namaDokumenName = listNamaDokumen.get(i);
                    sb.append(urutan + ". " + namaDokumenName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(namaDokumenName);
                    actions.add(bookAction);
                    urutan++;
                }

                if (lengNamaDokumen > newlengNamaDokumen) {
                    title = "Silakan pilih kembali angka yang anda inginkan. Atau klik \"Next\" untuk Nama Dokumen lainnya.";
                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih kembali angka yang anda inginkan.";
                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("tanya_namadokumen", "SKIP");
                clearEntities.put("status_code", "0");

                break;
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_getSOP(ExtensionRequest extensionRequest) {
        log.debug("sop_getSOP() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        String namadokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "namadokumen");
        namadokumen = namadokumen.toLowerCase();
//        String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
//        String indexDokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "index");

        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
//        String fullName = "Deka";
        String text = fullName;

        String statussku = "";
        switch (status_code) {
            case "0":
                if (namadokumen.equalsIgnoreCase("view all")) {
                    clearEntities.put("tanya_namadokumen", "");
                    clearEntities.put("namadokumen", "");
                    clearEntities.put("status_code", "3");
                } else if (namadokumen.equalsIgnoreCase("next")) {
                    clearEntities.put("tanya_namadokumen", "");
                    clearEntities.put("namadokumen", "");
                    clearEntities.put("status_code", "1");
                } else {
                    List<String> listNamaDokumen = new ArrayList<>();
                    listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
                    boolean cekAngka = CekNumber(namadokumen);
                    if (cekAngka == true) {
                        statussku = "tepat";
                    } else {
                        int lengDokumen = listNamaDokumen.size();
                        for (int i = 0; i < lengDokumen; i++) {
                            String dokumenname = listNamaDokumen.get(i);
                            dokumenname = dokumenname.toLowerCase();
                            if (dokumenname.equalsIgnoreCase(namadokumen)) {
                                statussku = "tepat";
                                break;
                            } else if (dokumenname.contains(namadokumen)) {
                                statussku = "tidak";
                            }
                        }
                    }
                    if (statussku.equalsIgnoreCase("tepat")) {
                        final String companyFinal = company;
                        final String jenisdokumenFinal = jenisdokumen;
                        final String divisiFinal = divisi;
                        if (cekAngka == true) {
                            int i = Integer.parseInt(namadokumen) - 1;
                            namadokumen = listNamaDokumen.get(i);
                        } else {
                            int lengDokumen = listNamaDokumen.size();
                            for (int i = 0; i < lengDokumen; i++) {
                                String dokumenname = listNamaDokumen.get(i);
                                if (dokumenname.equalsIgnoreCase(namadokumen)) {
                                    namadokumen = dokumenname;
                                    break;
                                }
                            }
                        }
                        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
                        List<SOP> listByFilter = listSopJson.stream()
                                .filter(
                                        sop -> sop.company.equalsIgnoreCase(companyFinal)
                                        && sop.jenis_dokumen.equalsIgnoreCase(jenisdokumenFinal)
                                        && sop.divisi.equalsIgnoreCase(divisiFinal))
                                .collect(Collectors.toList());

                        try {
                            int lengNamaDokumen = listByFilter.size();
                            String linkDoc = "";
                            String namaDoc = "";
                            String fileDoc = "";
                            for (int i = 0; i < lengNamaDokumen; i++) {
                                SOP sopArray = listByFilter.get(i);
                                namaDoc = sopArray.nama_doc;

                                String inputUrl = "";
                                if (namaDoc.equalsIgnoreCase(namadokumen)) {
                                    linkDoc = sopArray.link;
                                    fileDoc = sopArray.file_name.replace(".pdf", "").replace(".jepg", "").replace(".jpg", "");

                                    String dir = appProp.getGARUDAFOOD_PATH_GENERATEDFILES() + appProp.getGARUDAFOOD_BASE_SOP() + company.toUpperCase() + "/" + fileDoc + "/";
                                    File f = new File(dir);
                                    System.out.println("File dan direktori dalam " + dir);
                                    // java.util.Arrays.sort(daftar);
                                    if (f.list() == null) {

                                    } else {
                                        String[] daftar = f.list();
                                        System.out.println(daftar);
                                        System.out.println("List Directory : " + daftar);
                                        Arrays.sort(daftar);

                                        for (int j = 0; j < daftar.length; j++) {
                                            // File fTemp = new File(dir + daftar[i]);
                                            String dir2 = appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_BASE_SOP() + company.toUpperCase() + "/" + fileDoc + "/";
                                            System.out.println(dir2 + daftar[j]);
                                            inputUrl = dir2 + daftar[j];
                                            // inputUrl = "https://autobot.garudafood.co.id/GeneratedFiles/baseSop/09102019_11251473.jpeg";
                                            URL input = new URL(inputUrl);
                                            String reportAfterWatermark = generateWatermark.WatermarkImageSOP(text, input, daftar[j]);
                                            System.out.println(reportAfterWatermark);
                                            ButtonTemplate image = new ButtonTemplate();
                                            image.setPictureLink(reportAfterWatermark);
                                            image.setPicturePath(reportAfterWatermark);

                                            DocumentBuilder documentBuilder = new DocumentBuilder(image);
                                            String btnBuilder = documentBuilder.build();
                                            sb.append(btnBuilder).append(SPLIT);
                                        }
                                        break;
                                    }
                                }
                            }
                            if (sb.toString().equalsIgnoreCase("")) {
                                clearEntities.put("tanya_namadokumen", "");
                                clearEntities.put("namadokumen", "");
                                clearEntities.put("status_code", "4");
                            } else {
                                String dialog1 = "Berikut adalah " + fileDoc + " yang {first_name} ingin lihat.\n";
                                String title = "- Klik \"Detail\" jika membutuhkan " + jenisdokumen.toUpperCase() + " lebih detail\n"
                                        + "- Klik \"Dokumen Lain\" untuk melihat Dokumen " + jenisdokumen + " lainnya\n"
                                        + "- Klik \"Menu\" untuk melihat Menu yang ada.";
                                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                                        .add("Detail", linkDoc).add("Dokumen Lain", "dokumen lain").add("Menu", "menu utama").build();

                                output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + title + SPLIT + quickReplyBuilder.string());
                            }

                        } catch (MalformedURLException e) {
                            System.out.println(e);
                        }
                    } else {
                        clearEntities.put("tanya_namadokumen", "");
                        clearEntities.put("namadokumen", "");
                        clearEntities.put("nomorurut", namadokumen);
                        clearEntities.put("status_code", "2");

                    }
                }
                break;

            case "2":
                List<String> listNamaDokumen = new ArrayList<>();
                listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
                boolean cekAngka = CekNumber(namadokumen);
                if (cekAngka == true) {
                    int i = Integer.parseInt(namadokumen) - 1;
                    namadokumen = listNamaDokumen.get(i);
                } else {
                    int lengDokumen = listNamaDokumen.size();
                    for (int i = 0; i < lengDokumen; i++) {
                        String dokumenname = listNamaDokumen.get(i);
                        if (dokumenname.equalsIgnoreCase(namadokumen)) {
                            namadokumen = dokumenname;
                            break;
                        }
                    }
                }
                List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
                List<SOP> listByFilter = listSopJson.stream()
                        .filter(sop -> sop.company.equalsIgnoreCase(company)
                        && sop.jenis_dokumen.equalsIgnoreCase(jenisdokumen)
                        && sop.divisi.equalsIgnoreCase(divisi))
                        .collect(Collectors.toList());
                try {
                    int lengNamaDokumen = listByFilter.size();
                    String linkDoc = "";
                    String namaDoc = "";
                    for (int i = 0; i < lengNamaDokumen; i++) {
                        SOP sopArray = listByFilter.get(i);
                        namaDoc = sopArray.nama_doc;
                        if (namaDoc.equalsIgnoreCase(namadokumen)) {
                            linkDoc = sopArray.link;
                            break;
                        }
                    }
                    String title = "- Klik \"Detail\" jika membutuhkan " + jenisdokumen.toUpperCase() + " lebih detail\n"
                            + "- Klik \"Dokumen Lain\" untuk melihat Dokumen " + jenisdokumen + " lainnya\n"
                            + "- Klik \"Menu\" untuk melihat Menu yang ada.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                            .add("Detail", linkDoc).add("Dokumen Lain", "dokumen lain").add("Menu", "menu utama").build();

                    output.put(OUTPUT, title + SPLIT + quickReplyBuilder.string());
                } catch (Exception e) {
                    System.out.println(e);
                }
                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setEntities(clearEntities);

        extensionResult.setValue(output);

        extensionResult.setAgent(
                false);
        extensionResult.setRepeat(
                false);
        extensionResult.setSuccess(
                true);
        extensionResult.setNext(
                true);
        log.debug(
                "String Builder getSOP() extension request : {} ", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult
            sop_validasiSOP(ExtensionRequest extensionRequest) {
        log.debug("sop_validasiSOP() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class
        ));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        String sopbyUser = sdkUtil.getEasyMapValueByName(extensionRequest, "sop");

        if (sopbyUser.equalsIgnoreCase("dokumen lain")) {
            clearEntities.put("status_code", "0");
            clearEntities.put("jenisdokumen", "");
            clearEntities.put("tanya_namadokumen", "");
            clearEntities.put("namadokumen", "");
            clearEntities.put("sop", "");
            clearEntities.put("before_final", "");
        } else {
            clearEntities.put("status_code", "2");
            clearEntities.put("sop", "");
            clearEntities.put("before_final", "");
        }
        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }
}
