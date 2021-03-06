/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceReport;

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.EasyParam;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.InfoUser;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.LoopParam;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Region;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportRequest;
import com.imi.dolphin.sdkwebservice.builder.ButtonBuilder;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
import com.imi.dolphin.sdkwebservice.model.EasyMap;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamJSONReport;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.AuthService;
import com.imi.dolphin.sdkwebservice.service.GenerateWatermark;
import com.imi.dolphin.sdkwebservice.service.IDolphinService;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import com.imi.dolphin.sdkwebservice.util.DialogUtil;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ServiceReportLOCParam {

    private static final Logger log = LogManager.getLogger(ServiceReportLOCParam.class);

    private static final String OUTPUT = "output";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    private static final String regionJson = "fileJson/report/region.json";
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
    private ParamJSONReport paramJSON;

    @Autowired
    GetListJsonReport getListJsonReport;

    @Autowired
    DialogUtil dialogUtil;

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

    public ExtensionResult ReportLOCParam_KategoriArea(ExtensionRequest extensionRequest) {
        log.debug("ReportLOCParam_KategoriArea() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String intention = sdkUtil.getEasyMapValueByName(extensionRequest, "intention");
        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");

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

        List<String> listReportName = new ArrayList<>();
        listReportName = getListJsonReport.reportNameGeneral();
        int lengReportName = listReportName.size();
        String reportcode = "";
        String reportname = "";
        for (int i = 0; i < lengReportName; i++) {
            String reportcodename = listReportName.get(i);
            String[] splitcodename = reportcodename.split("_M");
            String splitreportcode = splitcodename[0];
            String splitreportname = splitcodename[1];
            if (intention.equalsIgnoreCase(splitreportcode) || intention.equalsIgnoreCase(splitreportname)) {
                reportcode = splitreportcode;
                reportname = splitreportname;
            }
        }

        Map<String, String> param = new HashMap<>();
        param.put("fullname", fullName);
        param.put("namareport", reportname);

        List<String> listArea = new ArrayList<>();
        listArea = getListJsonReport.areaGeneral();
        String dialog = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();
        int i;
        int urutan;
        int lengArea;
        int newlengArea;
        ButtonBuilder buttonBuilder;
        String title = "";
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                String dialogsapa = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 1, param);
                dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 2, param);
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengArea = listArea.size();
                newlengArea = 0;
                if (lengArea > 5) {
                    newlengArea = 5;
                } else {
                    newlengArea = lengArea;
                }
                for (i = i; i < newlengArea; i++) {
                    String areaName = listArea.get(i);
                    sb.append(urutan + ". " + areaName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(areaName);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengArea > newlengArea) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 3, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 4, null);
                }

                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, dialogsapa + SPLIT + sb.toString());
                clearEntities.put("status_code", "0");

                break;
            case "1":
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 2, param);
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengArea = listArea.size();
                newlengArea = lengArea - i;
                if (newlengArea > 5) {
                    newlengArea = 5;
                    newlengArea = i + newlengArea;
                } else {
                    newlengArea = lengArea;
                }
                for (i = i; i < newlengArea; i++) {
                    String areaName = listArea.get(i);
                    sb.append(urutan + ". " + areaName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(areaName);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengArea > newlengArea) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 3, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 4, null);

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");

                break;
            case "2":
                dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 5, param);
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengArea = listArea.size();
                newlengArea = 0;
                if (lengArea > 5) {
                    newlengArea = 5;
                } else {
                    newlengArea = lengArea;
                }
                for (i = i; i < newlengArea; i++) {
                    String areaName = listArea.get(i);
                    sb.append(urutan + ". " + areaName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(areaName);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengArea > newlengArea) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 3, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriArea", 4, null);
                }

                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportLOCParam_KategoriArea() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult ReportLOCParam_KategoriRegion(ExtensionRequest extensionRequest) {
        log.debug("ReportLOCParam_KategoriRegion() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String area = sdkUtil.getEasyMapValueByName(extensionRequest, "area");
        System.out.println("Area di tangkep :" + area);
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
        // =============================================== //

        List<String> listArea = new ArrayList<>();
        listArea = getListJsonReport.areaGeneral();

        String namearea = "";
        boolean cekAngka = CekNumber(area);
        if (cekAngka == true) {
            int i = Integer.parseInt(area) - 1;
            namearea = listArea.get(i);
        } else {
            int lengArea = listArea.size();
            for (int i = 0; i < lengArea; i++) {
                String areaName = listArea.get(i);
                if (areaName.equalsIgnoreCase(area)) {
                    namearea = areaName;
                    break;
                }
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("fullname", fullName);
        param.put("namearea", namearea);

        String dialog = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();

        List<String> listRegionCode = new ArrayList<>();
        listRegionCode = getListJsonReport.regionGeneral(namearea);
        int i;
        int urutan;
        int lengRegion;
        int newlengRegion;
        ButtonBuilder buttonBuilder;
        String title = "";
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                if (area.equalsIgnoreCase("next")) {
                    clearEntities.put("area", "");
                    clearEntities.put("status_code", "1");
                } else if (namearea.equalsIgnoreCase("")) {
                    clearEntities.put("area", "");
                    clearEntities.put("status_code", "2");
                } else {
                    dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 1, param);
                    sb.append(dialog);

                    button.setTitle("");
                    button.setSubTitle("");
                    i = 0;
                    urutan = 1;
                    lengRegion = listRegionCode.size();
                    newlengRegion = 0;
                    if (lengRegion > 5) {
                        newlengRegion = 5;
                    } else {
                        newlengRegion = lengRegion;
                    }
                    for (i = i; i < newlengRegion; i++) {
                        String regionCode = listRegionCode.get(i);
                        sb.append(urutan + ". " + regionCode + "\n");

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName(urutan + "");
                        bookAction.setValue(regionCode);
                        actions.add(bookAction);
                        urutan++;
                    }
                    if (lengRegion > newlengRegion) {
                        title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 2, null);

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 3, null);
                    }
                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());
                    clearEntities.put("area", namearea);
                    clearEntities.put("status_code", "0");

                }
                break;
            case "1":
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");

                dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 1, param);
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengRegion = listRegionCode.size();
                newlengRegion = lengRegion - i;
                if (newlengRegion > 5) {
                    newlengRegion = 5;
                    newlengRegion = i + newlengRegion;
                } else {
                    newlengRegion = lengRegion;
                }

                for (i = i; i < newlengRegion; i++) {
                    String regionCode = listRegionCode.get(i);
                    sb.append(urutan + ". " + regionCode + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(regionCode);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengRegion > newlengRegion) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 2, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 3, null);

                }

                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                dialog = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 4, param);
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengRegion = listRegionCode.size();
                newlengRegion = 0;
                if (lengRegion > 5) {
                    newlengRegion = 5;
                } else {
                    newlengRegion = lengRegion;
                }
                for (i = i; i < newlengRegion; i++) {
                    String regionCode = listRegionCode.get(i);
                    sb.append(urutan + ". " + regionCode + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(regionCode);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengRegion > newlengRegion) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 2, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_KategoriRegion", 3, null);
                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportLOCParam_KategoriRegion() extension request: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult ReportLOCParam_tanyaSKU(ExtensionRequest extensionRequest) {
        log.debug("ReportLOCParam_tanyaSKU() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String area = sdkUtil.getEasyMapValueByName(extensionRequest, "area");
        String region = sdkUtil.getEasyMapValueByName(extensionRequest, "region");

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
        // =============================================== //
        List<String> listArea = new ArrayList<>();
        listArea = getListJsonReport.areaGeneral();

        String namearea = "";
        boolean cekAngkaArea = CekNumber(area);
        if (cekAngkaArea == true) {
            int i = Integer.parseInt(area) - 1;
            namearea = listArea.get(i);
        } else {
            int lengArea = listArea.size();
            for (int i = 0; i < lengArea; i++) {
                String areaName = listArea.get(i);
                if (areaName.equalsIgnoreCase(area)) {
                    namearea = areaName;
                    break;
                }
            }
        }
        List<String> listRegionCode = new ArrayList<>();
        listRegionCode = getListJsonReport.regionGeneral(namearea);

        String nameregion = "";
        boolean cekAngka = CekNumber(region);
        if (cekAngka == true) {
            int i = Integer.parseInt(region) - 1;
            nameregion = listRegionCode.get(i);
        } else {
            int lengRegion = listRegionCode.size();
            for (int i = 0; i < lengRegion; i++) {
                String regionCode = listRegionCode.get(i);
                if (regionCode.equalsIgnoreCase(region)) {
                    nameregion = regionCode;
                    break;
                }
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("fullname", fullName);
        param.put("nameregion", nameregion);
        List<String> listSKU = new ArrayList<>();
        listSKU = getListJsonReport.SKURegionGeneral(namearea, nameregion);
        String dialog = "";
        ButtonTemplate button = new ButtonTemplate();
        List<EasyMap> actions = new ArrayList<>();
        int i;
        int urutan;
        int lengProduct;
        int newlengProduct;
        ButtonBuilder buttonBuilder;
        String title = "";
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                if (region.equalsIgnoreCase("next")) {
                    clearEntities.put("region", "");
                    clearEntities.put("status_code", "1");
                } else if (nameregion.equalsIgnoreCase("")) {
                    clearEntities.put("region", "");
                    clearEntities.put("status_code", "2");
                } else {
//                    dialog = "Silakan ketikan kode LOC yang Bapak/Ibu " + fullName + " inginkan. Atau ketik \"All\" untuk semua LOC berdasarkan Region.";
//                    sb.append(dialog);
//
//                    output.put(OUTPUT, sb.toString());
//                    clearEntities.put("region", nameregion);
//                    clearEntities.put("area", namearea);
//
//                    clearEntities.put("status_code", "0");

                    dialog = dialogUtil.CreateBubble("dialogParamLOC_tanyaSKU", 1, param);
                    sb.append(dialog);

                    button.setTitle("");
                    button.setSubTitle("");
                    i = 0;
                    urutan = 1;
                    lengProduct = listSKU.size();
                    newlengProduct = 0;
                    if (lengProduct > 5) {
                        newlengProduct = 5;
                    } else {
                        newlengProduct = lengProduct;
                    }
                    for (i = i; i < newlengProduct; i++) {
                        String skuProduct = listSKU.get(i);
                        sb.append(urutan + ". " + skuProduct + "\n");

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName(urutan + "");
                        bookAction.setValue(skuProduct);
                        actions.add(bookAction);
                        urutan++;
                    }
                    if (lengProduct > newlengProduct) {
                        title = dialogUtil.CreateBubble("dialogParamLOC_tanyaSKU", 2, null);

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = dialogUtil.CreateBubble("dialogParamLOC_tanyaSKU", 3, null);
                    }

                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());
                    clearEntities.put("region", nameregion);
                    clearEntities.put("area", namearea);

                    clearEntities.put("status_code", "0");
                }
                break;
            case "1":
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                dialog = "Bapak/Ibu " + fullName + " ingin melihat report dari LOC apa?\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengProduct = listSKU.size();
                newlengProduct = lengProduct - i;
                if (newlengProduct > 5) {
                    newlengProduct = 5;
                    newlengProduct = i + newlengProduct;
                } else {
                    newlengProduct = lengProduct;
                }
                for (i = i; i < newlengProduct; i++) {
                    String skuProduct = listSKU.get(i);
                    sb.append(urutan + ". " + skuProduct + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(skuProduct);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengProduct > newlengProduct) {
                    title = dialogUtil.CreateBubble("dialogParamLOC_tanyaSKU", 2, null);

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = dialogUtil.CreateBubble("dialogParamLOC_tanyaSKU", 3, null);
                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "sku");

                sku = sku.toUpperCase();
                int lengList = listSKU.size();
                for (i = 0; i < lengList; i++) {
                    String skuCode = listSKU.get(i);
                    skuCode = skuCode.toUpperCase();
                    if (skuCode.equalsIgnoreCase(sku)) {
                        break;
                    } else if (skuCode.contains(sku)) {
                        sb.append(skuCode).append("\n");
                    }
                }

                if (sb.toString().equalsIgnoreCase("")) {
                    String dialog1 = "Maaf, {bot_name} tidak dapat menemukan LOC tersebut.";
                    String dialog2 = "Silakan ketikan kembali kode LOC yang Bapak/Ibu " + fullName + " inginkan. Atau ketik \"All\" untuk semua LOC berdasarkan Region.";
                    output.put(OUTPUT, dialog1 + SPLIT + dialog2);

                } else {
                    String dialog1 = "Apakah kode LOC berikut yang Anda maksud?\n";
                    String dialog2 = "Silakan ketikan kode LOC yang Bapak/Ibu " + fullName + " inginkan.";
                    output.put(OUTPUT, dialog1 + sb.toString() + SPLIT + dialog2);
                }

                clearEntities.put("status_code", "0");
                break;
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportLOCParam_tanyaSKU() extensionResult: {}", new Gson().toJson(extensionResult));

        return extensionResult;
    }

    public ExtensionResult ReportLOCParam_Summary(ExtensionRequest extensionRequest) {
        log.debug("ReportLOCParam_Summary() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String area = sdkUtil.getEasyMapValueByName(extensionRequest, "area");
        String region = sdkUtil.getEasyMapValueByName(extensionRequest, "region");
        String tanya_sku = sdkUtil.getEasyMapValueByName(extensionRequest, "tanya_sku");

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
        // =============================================== //
        Map<String, String> param = new HashMap<>();
        param.put("fullname", fullName);

        List<String> listArea = new ArrayList<>();
        listArea = getListJsonReport.areaGeneral();

        String namearea = "";
        boolean cekAngkaArea = CekNumber(area);
        if (cekAngkaArea == true) {
            int i = Integer.parseInt(area) - 1;
            namearea = listArea.get(i);
        } else {
            int lengArea = listArea.size();
            for (int i = 0; i < lengArea; i++) {
                String areaName = listArea.get(i);
                if (areaName.equalsIgnoreCase(area)) {
                    namearea = areaName;
                    break;
                }
            }
        }
        List<String> listRegionCode = new ArrayList<>();
        listRegionCode = getListJsonReport.regionGeneral(namearea);

        String nameregion = "";
        boolean cekAngka = CekNumber(region);
        if (cekAngka == true) {
            int i = Integer.parseInt(region) - 1;
            nameregion = listRegionCode.get(i);
        } else {
            int lengRegion = listRegionCode.size();
            for (int i = 0; i < lengRegion; i++) {
                String regionCode = listRegionCode.get(i);
                if (regionCode.equalsIgnoreCase(region)) {
                    nameregion = regionCode;
                    break;
                }
            }
        }
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                if (tanya_sku.equalsIgnoreCase("next")) {
                    clearEntities.put("tanya_sku", "");
                    clearEntities.put("status_code", "1");
                } else if (tanya_sku.equalsIgnoreCase("all")) {

                    String quickreply = dialogUtil.CreateBubble("dialogParamLOC_Summary", 1, param);
                    output.put(OUTPUT, quickreply);
                    clearEntities.put("sku", tanya_sku);
                    clearEntities.put("status_code", "0");
                    clearEntities.put("area", namearea);
                    clearEntities.put("region", nameregion);
                    clearEntities.put("index", "0");
                    clearEntities.put("nomorurut", "1");

                } else {
                    List<String> listSKU = new ArrayList<>();
                    listSKU = getListJsonReport.SKURegionGeneral(namearea, nameregion);
                    String statussku = "";
                    int lengList = listSKU.size();
                    for (int i = 0; i < lengList; i++) {
                        String skuCode = listSKU.get(i);
                        skuCode = skuCode.toLowerCase();
                        if (skuCode.equalsIgnoreCase(tanya_sku)) {
                            statussku = "tepat";
                            break;
                        } else if (skuCode.contains(tanya_sku)) {
                            statussku = "tidak";
                        }
                    }
                    if (statussku.equalsIgnoreCase("tepat")) {
                        clearEntities.put("summary", "yes");
                        clearEntities.put("sku", tanya_sku);
                        clearEntities.put("status_code", "0");
                        clearEntities.put("index", "0");
                        clearEntities.put("nomorurut", "1");
                    } else {
                        clearEntities.put("tanya_sku", "");
                        clearEntities.put("status_code", "2");
                        clearEntities.put("sku", tanya_sku);
                    }
                }
                break;

        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportLOCParam_Summary() extensionResult: {}", new Gson().toJson(extensionResult));

        return extensionResult;
    }

    public ExtensionResult ReportLOCParam_getReport(ExtensionRequest extensionRequest) {
        log.debug("ReportLOCParam_getReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String area = sdkUtil.getEasyMapValueByName(extensionRequest, "area");
        String regionCode = sdkUtil.getEasyMapValueByName(extensionRequest, "region");
//        String tanya_sku = sdkUtil.getEasyMapValueByName(extensionRequest, "tanya_sku");
        String summary = sdkUtil.getEasyMapValueByName(extensionRequest, "summary");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "sku");
        String intention = sdkUtil.getEasyMapValueByName(extensionRequest, "intention");

        String indexReport = "";

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
//        String fullName = "Deka Rizky";
        // =============================================== //

        List<String> listReportName = new ArrayList<>();
        listReportName = getListJsonReport.reportNameGeneral();
        int lengReportName = listReportName.size();
        String reportcode = "";
        String reportname = "";
        for (int i = 0; i < lengReportName; i++) {
            String reportcodename = listReportName.get(i);
            String[] splitcodename = reportcodename.split("_M");
            String splitreportcode = splitcodename[0];
            String splitreportname = splitcodename[1];
            if (intention.equalsIgnoreCase(splitreportcode) || intention.equalsIgnoreCase(splitreportname)) {
                reportcode = splitreportcode;
                reportname = splitreportname;
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("fullname", fullName);
        param.put("namareport", reportname);

        List<String> listArea = new ArrayList<>();
        listArea = getListJsonReport.areaGeneral();

        String namearea = "";
        int nomorurutarea = 0;
        boolean cekAngkaArea = CekNumber(area);
        if (cekAngkaArea == true) {
            int i = Integer.parseInt(area) - 1;
            namearea = listArea.get(i);
            nomorurutarea = Integer.parseInt(area);

        } else {
            int lengArea = listArea.size();
            for (int i = 0; i < lengArea; i++) {
                String areaName = listArea.get(i);
                if (areaName.equalsIgnoreCase(area)) {
                    namearea = areaName;
                    nomorurutarea = i + 1;

                    break;
                }
            }
        }
        List<String> listRegionCode = new ArrayList<>();
        listRegionCode = getListJsonReport.regionGeneral(namearea);
        int nomorurutregion = 0;
        String nameregion = "";
        boolean cekAngka = CekNumber(regionCode);
        if (cekAngka == true) {
            int i = Integer.parseInt(regionCode) - 1;
            nomorurutregion = Integer.parseInt(regionCode);
            nameregion = listRegionCode.get(i);

        } else {
            int lengRegion = listRegionCode.size();
            for (int i = 0; i < lengRegion; i++) {
                String regionName = listRegionCode.get(i);
                if (regionName.equalsIgnoreCase(regionCode)) {
                    nomorurutregion = i + 1;
                    nameregion = regionName;
                    break;
                }
            }
        }
        final String finalregionname = nameregion;
        final String finalareaname = namearea;
        String kodereport = reportcode;
        String shortcutReport = kodereport + " " + nomorurutarea + " " + nomorurutregion + " " + sku;
        String shortcutGantiLOC = kodereport + " " + nomorurutarea + " " + nomorurutregion;

        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        String text = fullName;
        String parameterKey = "";
        if (reportcode.equalsIgnoreCase("r2")) {
            parameterKey = "SKU";
        } else {
            parameterKey = "LOC";

        }

        String parameterValue = "";
        reportcode = reportcode + "_M";
        String summaryReport = summary;
        int lengList = 0;
        int index = 0;
        String dialog1 = "";
        String next = "";
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                String dialog = "";

                if (summaryReport.equalsIgnoreCase("yes")) {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Region> listRegion = paramJSON.getListRegionfromFileJson(regionJson);
                        List<Region> listByRegion = listRegion.stream()
                                .filter(region -> region.area.equalsIgnoreCase(finalareaname) && region.region.equalsIgnoreCase(finalregionname))
                                .sorted(Comparator.comparing(Region::getLoc))
                                .collect(Collectors.toList());
                        lengList = listByRegion.size();
                        for (int i = index; i < lengList;) {
                            Region regionArray = listByRegion.get(i);
                            String skuRegion = regionArray.loc;
                            if (i < 1) {
                                parameterValue = skuRegion;
                            } else {
                                parameterValue = parameterValue + "|" + skuRegion;
                            }
                            i++;
                            index = i;
                        }
                    } else {
                        parameterValue = sku.toUpperCase();
                    }
                    EasyParam easyParam = new EasyParam();
                    easyParam.setSzKey(parameterKey);
                    easyParam.setSzValue(parameterValue);
                    easyparam.add(easyParam);

                } else {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Region> listRegion = paramJSON.getListRegionfromFileJson(regionJson);
                        List<Region> listByRegion = listRegion.stream()
                                .filter(region -> region.area.equalsIgnoreCase(finalareaname) && region.region.equalsIgnoreCase(finalregionname))
                                .sorted(Comparator.comparing(Region::getLoc))
                                .collect(Collectors.toList());
                        lengList = listByRegion.size();
                        int newleng;
                        if (lengList >= 5) {
                            newleng = 5;
                        } else {
                            newleng = lengList;
                        }
                        for (int i = index; i < newleng;) {
                            Region regionArray = listByRegion.get(i);
                            String skuRegion = regionArray.loc;
                            LoopParam loopParam = new LoopParam();
                            loopParam.setSzKey(parameterKey);
                            loopParam.setSzValue(skuRegion);

                            loopparam.add(loopParam);
                            i++;
                            index = i;
                        }
                    } else {
                        LoopParam loopParam = new LoopParam();
                        parameterValue = sku.toUpperCase();
                        loopParam.setSzKey(parameterKey);
                        loopParam.setSzValue(parameterValue);
                        loopparam.add(loopParam);
                    }
                }
                // Set Param
                reportRequest.setSzReportName(reportcode);
                reportRequest.setLoopParam(loopparam);
                reportRequest.setSummary(summary);
                reportRequest.setParam(easyparam);
                JSONObject jsonReport = new JSONObject(reportRequest);
                String report = jsonReport.toString();
                dialog1 = "";
                try {
                    OkHttpUtil okHttpUtil = new OkHttpUtil();
                    okHttpUtil.init(true);
                    String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                    System.out.println(report);
                    RequestBody body = RequestBody.create(JSON, report);
                    Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                    Response response = okHttpUtil.getClient().newCall(request).execute();
                    JSONObject jsonobj = new JSONObject(response.body().string());

                    if (jsonobj.getString("error").equalsIgnoreCase("")) {
                        JSONArray arrayReport = jsonobj.getJSONArray("path");
                        int lengReport = arrayReport.length();

                        for (int i = 0; i < lengReport; i++) {
                            JSONObject jObj = arrayReport.getJSONObject(i);
                            String url = jObj.getString("url");
                            System.out.println(url);
                            URL input = new URL(url);
                            String reportAfterWatermark = generateWatermark.WatermarkImageReport(text, input);
                            System.out.println(reportAfterWatermark);
                            ButtonTemplate image = new ButtonTemplate();
                            image.setPictureLink(reportAfterWatermark);
                            image.setPicturePath(reportAfterWatermark);

                            DocumentBuilder documentBuilder = new DocumentBuilder(image);
                            String btnBuilder = documentBuilder.build();
                            sb.append(btnBuilder).append(SPLIT);

                        }
                        String pagereport = "(" + index + "/" + lengList + ")";
                        next = kodereport + " " + nomorurutarea + " " + nomorurutregion + " " + sku + " " + summary + " " + sku + " " + "1" + " " + index + " " + "1";
                        if (lengReport > 1) {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat, berdasarkan Area: " + finalareaname
                                    + " dan Region " + finalregionname + ". " + pagereport;
                        } else {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat, berdasarkan Area: " + finalareaname
                                    + " dan Region " + finalregionname + ". ";
                        }
                        if (lengList > 5 && summary.equalsIgnoreCase("no")) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);

                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti LOC\" jika Anda ingin cek LOC lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Next", next).add("Ganti LOC", shortcutGantiLOC).add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        } else {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti LOC\" jika Anda ingin cek LOC lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Ganti LOC", shortcutGantiLOC).add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        }
                        clearEntities.put("index", index + "");
                    } else {
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan klik \"Menu\" untuk melihat Menu Utama.")
                                .add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    }
                } catch (Exception e) {
                    log.debug("Response getReport Exception() extension request : {} ", e);
                }
                break;
            case "1":
                indexReport = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                index = Integer.parseInt(indexReport);
                if (summaryReport.equalsIgnoreCase("yes")) {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Region> listRegion = paramJSON.getListRegionfromFileJson(regionJson);
                        List<Region> listByRegion = listRegion.stream()
                                .filter(region -> region.area.equalsIgnoreCase(finalareaname) && region.region.equalsIgnoreCase(finalregionname))
                                .sorted(Comparator.comparing(Region::getLoc))
                                .collect(Collectors.toList());
                        lengList = listByRegion.size();
                        for (int i = index; i < lengList;) {
                            Region regionArray = listByRegion.get(i);
                            String skuRegion = regionArray.loc;
                            if (i < 1) {
                                parameterValue = skuRegion;
                            } else {
                                parameterValue = parameterValue + "|" + skuRegion;
                            }
                            i++;
                            index = i;
                        }
                    } else {
                        parameterValue = sku.toUpperCase();
                    }
                    EasyParam easyParam = new EasyParam();
                    easyParam.setSzKey(parameterKey);
                    easyParam.setSzValue(parameterValue);
                    easyparam.add(easyParam);
                } else {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Region> listRegion = paramJSON.getListRegionfromFileJson(regionJson);
                        List<Region> listByRegion = listRegion.stream()
                                .filter(region -> region.area.equalsIgnoreCase(finalareaname) && region.region.equalsIgnoreCase(finalregionname))
                                .sorted(Comparator.comparing(Region::getLoc))
                                .collect(Collectors.toList());
                        lengList = listByRegion.size();
                        int newleng;
                        int addindex = lengList - index;
                        if (addindex >= 5) {
                            addindex = 5;
                            newleng = index + addindex;
                        } else {
                            newleng = lengList;
                        }
                        for (int i = index; i < newleng;) {
                            Region regionArray = listByRegion.get(i);
                            String skuRegion = regionArray.loc;
                            LoopParam loopParam = new LoopParam();
                            loopParam.setSzKey(parameterKey);
                            loopParam.setSzValue(skuRegion);

                            loopparam.add(loopParam);
                            i++;
                            index = i;
                        }
                    } else {
                        LoopParam loopParam = new LoopParam();
                        parameterValue = sku.toUpperCase();
                        loopParam.setSzKey(parameterKey);
                        loopParam.setSzValue(parameterValue);
                        loopparam.add(loopParam);
                    }
                }
                // Set Param
                reportRequest.setSzReportName(reportcode);
                reportRequest.setLoopParam(loopparam);
                reportRequest.setSummary(summary);
                reportRequest.setParam(easyparam);
                JSONObject jsonReports = new JSONObject(reportRequest);
                String reports = jsonReports.toString();
                dialog1 = "";
                try {
                    OkHttpUtil okHttpUtil = new OkHttpUtil();
                    okHttpUtil.init(true);
                    String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                    System.out.println(reports);
                    RequestBody body = RequestBody.create(JSON, reports);
                    Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                    Response response = okHttpUtil.getClient().newCall(request).execute();
                    JSONObject jsonobj = new JSONObject(response.body().string());

                    if (jsonobj.getString("error").equalsIgnoreCase("")) {
                        JSONArray arrayReport = jsonobj.getJSONArray("path");
                        int lengReport = arrayReport.length();

                        for (int i = 0; i < lengReport; i++) {
                            JSONObject jObj = arrayReport.getJSONObject(i);
                            String url = jObj.getString("url");
                            System.out.println(url);
                            URL input = new URL(url);
                            String reportAfterWatermark = generateWatermark.WatermarkImageReport(text, input);
                            System.out.println(reportAfterWatermark);
                            ButtonTemplate image = new ButtonTemplate();
                            image.setPictureLink(reportAfterWatermark);
                            image.setPicturePath(reportAfterWatermark);

                            DocumentBuilder documentBuilder = new DocumentBuilder(image);
                            String btnBuilder = documentBuilder.build();
                            sb.append(btnBuilder).append(SPLIT);

                        }
                        String pagereport = "(" + index + "/" + lengList + ")";
                        next = kodereport + " " + nomorurutarea + " " + nomorurutregion + " " + sku + " " + summary + " " + sku + " " + "1" + " " + index + " " + "1";
                        if (lengReport > 1) {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat, berdasarkan Area: " + finalareaname
                                    + " dan Region " + finalregionname + ". " + pagereport;
                        } else {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat, berdasarkan Area: " + finalareaname
                                    + " dan Region " + finalregionname + ". ";
                        }
                        if (lengList < 5 || lengList == index) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti LOC\" jika Anda ingin cek LOC lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Ganti LOC", shortcutGantiLOC).add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        } else if (lengList > 5 && summary.equalsIgnoreCase("no")) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti LOC\" jika Anda ingin cek LOC lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Next", next).add("Ganti LOC", shortcutGantiLOC).add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        }
                        clearEntities.put("index", index + "");
                    } else {
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan klik \"Menu\" untuk melihat Menu Utama.")
                                .add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    }
                } catch (Exception e) {
                    log.debug("Response getReport Exception() extension request : {} ", e);
                }
                clearEntities.put("status_code", "0");
                break;

        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportLOCParam_getReport() extensionResult: {}", new Gson().toJson(extensionResult));

        return extensionResult;
    }
}
