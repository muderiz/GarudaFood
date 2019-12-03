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
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Product;
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
public class ServiceDailyProductionVsSales {

    private static final Logger log = LogManager.getLogger(ServiceImpReport.class);

    private static final String OUTPUT = "output";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;

    private static final String productJson = "fileJson/report/product.json";

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

    public ExtensionResult dailyProductionVsSales_setFirstStatusCode(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_setFirstStatusCode() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
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

    public ExtensionResult dailyProductionVsSales_KategoriGroupProduct(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_KategoriGroupProduct() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
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

        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();

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
                String dialogsapa = "Baiklah, Bapak/Ibu " + fullName + " sudah berada di Report Daily Stock By SKU.";
                dialog = "Bapak/Ibu " + fullName + " ingin melihat report dari Group apa?\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengProduct = listGroupProduct.size();
                newlengProduct = 0;
                if (lengProduct > 5) {
                    newlengProduct = 5;
                } else {
                    newlengProduct = lengProduct;
                }
                for (i = i; i < newlengProduct; i++) {
                    String groupProduct = listGroupProduct.get(i);
                    sb.append(urutan + ". " + groupProduct + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(groupProduct);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengProduct > newlengProduct) {
                    title = "Silakan pilih angka yang Anda inginkan. Klik \"Next\" untuk melihat Group lainnya";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang Anda inginkan.";
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
                dialog = "Bapak/Ibu " + fullName + " ingin melihat report dari Group apa?\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = Integer.parseInt(index);
                urutan = Integer.parseInt(nomorurut);
                lengProduct = listGroupProduct.size();
                newlengProduct = lengProduct - i;
                if (newlengProduct > 5) {
                    newlengProduct = 5;
                    newlengProduct = i + newlengProduct;
                } else {
                    newlengProduct = lengProduct;
                }
                for (i = i; i < newlengProduct; i++) {
                    String groupProduct = listGroupProduct.get(i);
                    sb.append(urutan + ". " + groupProduct + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(groupProduct);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengProduct > newlengProduct) {
                    title = "Silakan pilih angka yang Anda inginkan. Klik \"Next\" untuk melihat Group lainnya";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang Anda inginkan.";

                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");

                break;
            case "2":
                dialog = "Maaf {bot_name} tidak dapat menemukan Group yang Bapak/Ibu " + fullName + " inginkan.\n";
                sb.append(dialog);

                button.setTitle("");
                button.setSubTitle("");
                i = 0;
                urutan = 1;
                lengProduct = listGroupProduct.size();
                newlengProduct = 0;
                if (lengProduct > 5) {
                    newlengProduct = 5;
                } else {
                    newlengProduct = lengProduct;
                }
                for (i = i; i < newlengProduct; i++) {
                    String groupProduct = listGroupProduct.get(i);
                    sb.append(urutan + ". " + groupProduct + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(groupProduct);
                    actions.add(bookAction);
                    urutan++;
                }
                if (lengProduct > newlengProduct) {
                    title = "Silakan pilih kembali Group yang Anda inginkan. Klik \"Next\" untuk melihat Group lainnya";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih kembali Group yang Anda inginkan.";
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
        log.debug("dailyProductionVsSales_KategoriGroupProduct() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult dailyProductionVsSales_tanyaSKU(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_tanyaSKU() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String group = sdkUtil.getEasyMapValueByName(extensionRequest, "group");

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
//        String fullName = "Deka";
        // =============================================== //

        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();

        String namegroup = "";
        boolean cekAngka = CekNumber(group);
        if (cekAngka == true) {
            int i = Integer.parseInt(group) - 1;
            namegroup = listGroupProduct.get(i);
        } else {
            int lengGroup = listGroupProduct.size();
            for (int i = 0; i < lengGroup; i++) {
                String groupCode = listGroupProduct.get(i);
                if (groupCode.equalsIgnoreCase(group)) {
                    namegroup = groupCode;
                    break;
                }
            }
        }
        List<String> listSKU = new ArrayList<>();
        listSKU = getListJsonReport.SKUProductGeneral(namegroup);

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
                if (group.equalsIgnoreCase("next")) {
                    clearEntities.put("group", "");
                    clearEntities.put("status_code", "1");
                } else if (namegroup.equalsIgnoreCase("")) {
                    clearEntities.put("group", "");
                    clearEntities.put("status_code", "2");
                } else {

                    dialog = "Baiklah, Bapak/Ibu " + fullName + " telah memilih " + namegroup + ". Selanjutnya ingin melihat report dari SKU apa?\n";
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
                        title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.\n-Klik \"Next\" untuk melihat SKU lainnya.";

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.";
                    }

                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());
                    clearEntities.put("group", namegroup);
                    clearEntities.put("status_code", "0");

                }

                break;
            case "1":
                String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
                dialog = "Bapak/Ibu " + fullName + " ingin melihat report dari SKU apa?\n";
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
                    title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.\n-Klik \"Next\" untuk melihat SKU lainnya.";

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName("Next");
                    bookAction.setValue("next");
                    actions.add(bookAction);

                    clearEntities.put("index", i + "");
                    clearEntities.put("nomorurut", urutan + "");
                } else {
                    title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.";
                }
                button.setButtonValues(actions);
                buttonBuilder = new ButtonBuilder(button);
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("status_code", "0");
                break;
            case "2":
                String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "sku");
                sku = sku.toLowerCase();
                int lengList = listSKU.size();
                for (i = 0; i < lengList; i++) {
                    String skuCode = listSKU.get(i);
                    skuCode = skuCode.toLowerCase();
                    if (skuCode.equalsIgnoreCase(sku)) {
                        break;
                    } else if (skuCode.contains(sku)) {
                        sb.append(skuCode).append("\n");
                    }
                }

                if (sb.toString().equalsIgnoreCase("")) {
                    String dialog1 = "Maaf, {bot_name} tidak dapat menemukan SKU tersebut.";
                    dialog = "Baiklah, Bapak/Ibu " + fullName + " telah memilih " + namegroup + ". Selanjutnya ingin melihat report dari SKU apa?\n";
                    sb.append(dialog1).append(dialog);

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
                        title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.\n-Klik \"Next\" untuk melihat SKU lainnya.";

                        EasyMap bookAction = new EasyMap();
                        bookAction.setName("Next");
                        bookAction.setValue("next");
                        actions.add(bookAction);

                        clearEntities.put("index", i + "");
                        clearEntities.put("nomorurut", urutan + "");
                    } else {
                        title = "Silakan pilih angka yang Anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.";
                    }

                    button.setButtonValues(actions);
                    buttonBuilder = new ButtonBuilder(button);
                    sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                    output.put(OUTPUT, sb.toString());

                } else {
                    String dialog1 = "Apakah kode SKU berikut yang Anda maksud?\n";
                    String dialog2 = "Silakan ketikan kode SKU yang Bapak/Ibu " + fullName + " inginkan.";
                    output.put(OUTPUT, dialog1 + sb.toString() + SPLIT + dialog2);
//                    clearEntities.put("sku", "");
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
        log.debug("dailyProductionVsSales_tanyaSKU() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult dailyProductionVsSales_Summary(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_Summary() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String group = sdkUtil.getEasyMapValueByName(extensionRequest, "group");
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
        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();
        String namegroup = "";
        boolean cekAngka = CekNumber(group);
        if (cekAngka == true) {
            int i = Integer.parseInt(group) - 1;
            namegroup = listGroupProduct.get(i);
        } else {
            int lengGroup = listGroupProduct.size();
            for (int i = 0; i < lengGroup; i++) {
                String groupCode = listGroupProduct.get(i);
                if (groupCode.equalsIgnoreCase(group)) {
                    namegroup = groupCode;
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
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Apakah Bapak/Ibu " + fullName + " ingin report dalam bentuk summary?")
                            .add("Ya", "Yes").add("Tidak", "NO").build();

                    output.put(OUTPUT, quickReplyBuilder.string());
                    clearEntities.put("sku", tanya_sku);
                    clearEntities.put("group", namegroup);
                    clearEntities.put("status_code", "0");
                    clearEntities.put("index", "0");
                    clearEntities.put("nomorurut", "1");

                } else {
                    List<String> listSKU = new ArrayList<>();
                    listSKU = getListJsonReport.SKUProductGeneral(namegroup);
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
                        clearEntities.put("group", namegroup);
                        clearEntities.put("status_code", "0");
                        clearEntities.put("index", "0");
                        clearEntities.put("nomorurut", "1");

                    } else {
                        clearEntities.put("group", namegroup);
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
        log.debug("dailyProductionVsSales_Summary() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult dailyProductionVsSales_getReport(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_getReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String group = sdkUtil.getEasyMapValueByName(extensionRequest, "group");
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

        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();
        int nomorurutgroup = 0;
        boolean cekAngka = CekNumber(group);
        if (cekAngka == true) {
            nomorurutgroup = Integer.parseInt(group);
        } else {
            int lengGroup = listGroupProduct.size();
            for (int i = 0; i < lengGroup; i++) {
                String groupCode = listGroupProduct.get(i);
                if (groupCode.equalsIgnoreCase(group)) {
                    nomorurutgroup = i + 1;
                    break;
                }
            }
        }
        String kodereport = appProp.getGARUDAFOOD_SHORTCUT_dailystockbysku();
        String shortcutReport = kodereport + " " + nomorurutgroup + " " + sku;

        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        String text = fullName;
        String parameterKey = "SKU";
        String parameterValue = "";
        String reportname = appProp.getGARUDAFOOD_REPORTCODE_dailystockbysku();
        String summaryReport = summary;
        int lengList = 0;
        int index = 0;
        String dialog1 = "";
        if (status_code.equals("")) {
            status_code = "0";
        }
        switch (status_code) {
            case "0":
                String dialog = "";

                if (summaryReport.equalsIgnoreCase("yes")) {
                    if (intention.equalsIgnoreCase(kodereport)) {
                        clearEntities.put("report", "bypass");
                        clearEntities.put("before_final", "SKIP");

                    } else {
                        if (sku.equalsIgnoreCase("all")) {
                            List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                            List<Product> listProductbyFilter = listProductJson.stream()
                                    .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                                    //                                .sorted(Comparator.comparing(Product::getSku))
                                    .collect(Collectors.toList());
                            lengList = listProductbyFilter.size();
                            for (int i = index; i < lengList;) {
                                Product productArray = listProductbyFilter.get(i);
                                String skuProduct = productArray.sku;
                                if (i < 1) {
                                    parameterValue = skuProduct;
                                } else {
                                    parameterValue = parameterValue + "|" + skuProduct;
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
                    }

                } else {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                        List<Product> listProductbyFilter = listProductJson.stream()
                                .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                                //                                .sorted(Comparator.comparing(Product::getSku))
                                .collect(Collectors.toList());
                        lengList = listProductbyFilter.size();
                        int newleng;
                        if (lengList >= 5) {
                            newleng = 5;
                        } else {
                            newleng = lengList;
                        }
                        for (int i = index; i < newleng;) {
                            Product productArray = listProductbyFilter.get(i);
                            String skuProduct = productArray.sku;
                            LoopParam loopParam = new LoopParam();
                            loopParam.setSzKey(parameterKey);
                            loopParam.setSzValue(skuProduct);

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
                reportRequest.setSzReportName(reportname);
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
                        if (lengReport > 1) {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. " + pagereport;
                        } else {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. ";
                        }
                        if (lengList > 5 && summary.equalsIgnoreCase("no")) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama.")
                                    .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        } else {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama.")
                                    .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        }
                        clearEntities.put("index", index + "");
                    } else {
                        dialog = "Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan klik \"Menu\" untuk melihat Menu Utama.";

                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                                .add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    }
                } catch (Exception e) {
                    log.debug("Response getReport Exception() extension request : {} ", e);
                }

                clearEntities.put("status_code", "0");

                break;
            case "1":
                indexReport = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                index = Integer.parseInt(indexReport);
                if (summaryReport.equalsIgnoreCase("yes")) {
                    if (sku.equalsIgnoreCase("all")) {
                        List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                        List<Product> listProductbyFilter = listProductJson.stream()
                                .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                                //                                .sorted(Comparator.comparing(Product::getSku))
                                .collect(Collectors.toList());
                        lengList = listProductbyFilter.size();
                        for (int i = index; i < lengList;) {
                            Product productArray = listProductbyFilter.get(i);
                            String skuProduct = productArray.sku;
                            if (i < 1) {
                                parameterValue = skuProduct;
                            } else {
                                parameterValue = parameterValue + "|" + skuProduct;
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
                        List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                        List<Product> listProductbyFilter = listProductJson.stream()
                                .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                                //                                .sorted(Comparator.comparing(Product::getSku))
                                .collect(Collectors.toList());
                        lengList = listProductbyFilter.size();
                        int newleng;
                        int addindex = lengList - index;
                        if (addindex >= 5) {
                            addindex = 5;
                            newleng = index + addindex;
                        } else {
                            newleng = lengList;
                        }
                        for (int i = index; i < newleng;) {
                            Product productArray = listProductbyFilter.get(i);
                            String skuProduct = productArray.sku;
                            LoopParam loopParam = new LoopParam();
                            loopParam.setSzKey(parameterKey);
                            loopParam.setSzValue(skuProduct);

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
                reportRequest.setSzReportName(reportname);
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
                        if (lengReport > 1) {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. " + pagereport;
                        } else {
                            dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. ";
                        }
                        if (lengList < 5 || lengList == index) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                        } else if (lengList > 5 && summary.equalsIgnoreCase("no")) {
                            dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                            sb.append(dialog).append(SPLIT);
                            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                    .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

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
            case "2":
                indexReport = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
                index = Integer.parseInt(indexReport);
                List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                List<Product> listProductbyFilter = listProductJson.stream()
                        .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                        //                        .sorted(Comparator.comparing(Product::getSku))
                        .collect(Collectors.toList());
                lengList = listProductbyFilter.size();
                int newleng;
                int addindex = lengList - index;
                if (addindex >= 5) {
                    addindex = 5;
                    newleng = index + addindex;
                } else {
                    newleng = lengList;
                }

                if (newleng < 5 || newleng == index) {
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                            .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                    output.put(OUTPUT, quickReplyBuilder.string());
                } else if (newleng > 5) {
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                            .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                    output.put(OUTPUT, quickReplyBuilder.string());
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
        log.debug("dailyProductionVsSales_getReport() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult dailyProductionVsSales_validasiReport(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_validasiReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();

        String report = sdkUtil.getEasyMapValueByName(extensionRequest, "report");

        if (report.equalsIgnoreCase("next")) {
            clearEntities.put("report", "");
            clearEntities.put("status_code", "1");
        } else if (report.equalsIgnoreCase("ganti sku")) {
            clearEntities.put("tanya_sku", "");
            clearEntities.put("sku", "");
            clearEntities.put("summary", "");
            clearEntities.put("report", "");
            clearEntities.put("status_code", "0");
        } else {
            clearEntities.put("report", "");
            clearEntities.put("status_code", "2");
        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("dailyProductionVsSales_validasiReport() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

    public ExtensionResult dailyProductionVsSales_bypassReport(ExtensionRequest extensionRequest) {
        log.debug("dailyProductionVsSales_bypassReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String reportUser = sdkUtil.getEasyMapValueByName(extensionRequest, "report");
        String status_code = sdkUtil.getEasyMapValueByName(extensionRequest, "status_code");
        String group = sdkUtil.getEasyMapValueByName(extensionRequest, "group");
        String summary = sdkUtil.getEasyMapValueByName(extensionRequest, "summary");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "sku");
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

        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();
        int nomorurutgroup = 0;
        boolean cekAngka = CekNumber(group);
        if (cekAngka == true) {
            nomorurutgroup = Integer.parseInt(group);
        } else {
            int lengGroup = listGroupProduct.size();
            for (int i = 0; i < lengGroup; i++) {
                String groupCode = listGroupProduct.get(i);
                if (groupCode.equalsIgnoreCase(group)) {
                    nomorurutgroup = i + 1;
                    break;
                }
            }
        }
        String kodereport = appProp.getGARUDAFOOD_SHORTCUT_dailystockbysku();
        String shortcutReport = kodereport + " " + nomorurutgroup + " " + sku;

        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        String text = fullName;
        String parameterKey = "SKU";
        String parameterValue = "";
        String reportname = appProp.getGARUDAFOOD_REPORTCODE_dailystockbysku();
        String summaryReport = summary;
        int lengList = 0;
        int index = 0;
        String dialog1 = "";
        String dialog = "";

        if (sku.equalsIgnoreCase("all")) {
            List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
            List<Product> listProductbyFilter = listProductJson.stream()
                    .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                    //                                .sorted(Comparator.comparing(Product::getSku))
                    .collect(Collectors.toList());
            lengList = listProductbyFilter.size();
            for (int i = index; i < lengList;) {
                Product productArray = listProductbyFilter.get(i);
                String skuProduct = productArray.sku;
                if (i < 1) {
                    parameterValue = skuProduct;
                } else {
                    parameterValue = parameterValue + "|" + skuProduct;
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

        if (sku.equalsIgnoreCase("all")) {
            List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
            List<Product> listProductbyFilter = listProductJson.stream()
                    .filter(product -> product.getGroup_category().equalsIgnoreCase(group))
                    //                                .sorted(Comparator.comparing(Product::getSku))
                    .collect(Collectors.toList());
            lengList = listProductbyFilter.size();
            int newleng;
            if (lengList >= 5) {
                newleng = 5;
            } else {
                newleng = lengList;
            }
            for (int i = index; i < newleng;) {
                Product productArray = listProductbyFilter.get(i);
                String skuProduct = productArray.sku;
                LoopParam loopParam = new LoopParam();
                loopParam.setSzKey(parameterKey);
                loopParam.setSzValue(skuProduct);

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
        // Set Param
        reportRequest.setSzReportName(reportname);
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
                if (lengReport > 1) {
                    dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. " + pagereport;
                } else {
                    dialog1 = "Berikut adalah Report yang Bapak/Ibu " + fullName + " ingin lihat. ";
                }
                dialog = "Tips : Jika ingin melihat kembali Report ini. Saat berada di Menu Utama atau Menu Report, Anda dapat langsung menggunakan keyword berikut:\n" + shortcutReport;
                sb.append(dialog).append(SPLIT);
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Menu\" untuk melihat Menu Utama.")
                        .add("Menu", "menu").build();
                output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());

            } else {
                dialog = "Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan klik \"Menu\" untuk melihat Menu Utama.";
                sb.append(dialog).append(SPLIT);
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                        .add("Menu", "menu").build();

                output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
            }
        } catch (Exception e) {
            log.debug("Response getReport Exception() extension request : {} ", e);
        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("dailyProductionVsSales_bypassReport() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }

}
