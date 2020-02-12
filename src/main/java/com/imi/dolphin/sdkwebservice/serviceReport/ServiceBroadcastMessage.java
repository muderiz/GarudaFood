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
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Region;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportRequest;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SendDocumentTelegram;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SendMessageTelegram;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamJSONReport;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.AuthService;
import com.imi.dolphin.sdkwebservice.service.GenerateWatermark;
import com.imi.dolphin.sdkwebservice.service.IDolphinService;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import static com.imi.dolphin.sdkwebservice.service.ServiceImp.JSON;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ServiceBroadcastMessage {

    private static final Logger log = LogManager.getLogger(ServiceBroadcastMessage.class);

    public static final String OUTPUT = "output";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private UserToken userToken;
    public static final String roleJson = "fileJson/role.json";
    public static final String reportnameJson = "fileJson/report_name.json";
    public static final String departmentJson = "fileJson/masterdepartment.json";
    public static final String grouproductJson = "fileJson/master_group_product.json";
    public static final String productJson = "fileJson/product.json";
    public static final String groupJson = "fileJson/group.json";
    private static final String regionJson = "fileJson/report/region.json";
    public static final String skuJson = "fileJson/sku.json";

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
    GetListJsonReport getListJsonReport;

    @Autowired
    private ParamJSONReport paramJSON;

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

    public String BroadcastMessage(ExtensionRequest extensionRequest) {
        log.debug("BroadcastMessage() extension request: {}", extensionRequest);
        String report = extensionRequest.getReport();
        String group = extensionRequest.getGroup();
        String sku = extensionRequest.getCode();
        String caption = extensionRequest.getCaption();

        String chatid = "";
        // ============== Get Social ID Telegram All User ============ //
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = "";
        JSONObject jsonObject = svcDolphinService.getAllCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, jsonObject);

        // ============================================== //
        String document = "";
        SendDocumentTelegram documentTelegram = new SendDocumentTelegram();
        SendMessageTelegram messageTelegram = new SendMessageTelegram();

        String responseSendDocument = "";

        String statusreport = "";
        String statusapigarudafood = "";

        List<String> listRoleBroadcast = new ArrayList<>();
        listRoleBroadcast = getListJsonReport.roleBroadcastMessage();

        List<String> listGroupProduct = new ArrayList<>();
        listGroupProduct = getListJsonReport.groupProductGeneral();

        int lengGroup = listGroupProduct.size();
        for (int i = 0; i < lengGroup; i++) {
            String groupCode = listGroupProduct.get(i);
            if (groupCode.equalsIgnoreCase(group)) {
                statusreport = "SKU";
                break;
            }
        }

        List<String> listRegionCode = new ArrayList<>();
        listRegionCode = getListJsonReport.regionGeneral("");

        int lengRegion = listRegionCode.size();
        for (int i = 0; i < lengRegion; i++) {
            String regionCode = listRegionCode.get(i);
            if (regionCode.equalsIgnoreCase(group)) {
                statusreport = "LOC";
                break;
            }
        }
        if (statusreport.equals("")) {
            statusreport = "pass";
        }
        List<String> listReportName = new ArrayList<>();
        listReportName = getListJsonReport.reportNameGeneral();
        int lengReportName = listReportName.size();
        String reportcode = "";
        String reportname = "";
        String text = "";
        JSONArray arrayReport = new JSONArray();
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.init(true);
        switch (statusreport) {
            case "SKU":
                System.out.println("SKU");
                for (int i = 0; i < lengReportName; i++) {
                    String reportcodename = listReportName.get(i);
                    String[] splitcodename = reportcodename.split("_M");
                    String splitreportcode = splitcodename[0];
                    String splitreportname = splitcodename[1];
                    if (report.equalsIgnoreCase(splitreportcode) || report.equalsIgnoreCase(splitreportname)) {
                        reportcode = splitreportcode;
                        reportname = splitreportname;
                    }
                }
                final String finalgroupname = group;
                ReportRequest reportRequest = new ReportRequest();
                List<EasyParam> easyparam = new ArrayList<>();
                List<LoopParam> loopparam = new ArrayList<>();
                String parameterKey = "SKU";
                String parameterValue = "";
                reportcode = reportcode + "_M";
                String summaryReport = "Yes";
                int lengList = 0;
                int index = 0;
                String dialog1 = "";
                String dialog = "";
                String next = "";

                if (sku.equalsIgnoreCase("all")) {
                    List<Product> listProductJson = paramJSON.getListProductfromFileJson(productJson);
                    List<Product> listProductbyFilter = listProductJson.stream()
                            .filter(product -> product.getGroup_category().equalsIgnoreCase(finalgroupname))
                            .sorted(Comparator.comparing(Product::getSku))
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

                reportRequest.setSzReportName(reportcode);
                reportRequest.setLoopParam(loopparam);
                reportRequest.setSummary(summaryReport);
                reportRequest.setParam(easyparam);
                JSONObject jsonReport = new JSONObject(reportRequest);
                String jsonreport = jsonReport.toString();
                try {

                    String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                    System.out.println(jsonreport);
                    RequestBody body = RequestBody.create(JSON, jsonreport);
                    Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                    Response response = okHttpUtil.getClient().newCall(request).execute();
                    JSONObject jsonobjhitapisku = new JSONObject(response.body().string());
                    if (jsonobjhitapisku.getString("error").equalsIgnoreCase("")) {
                        arrayReport = jsonobjhitapisku.getJSONArray("path");
                        statusapigarudafood = "Success";
                    } else {
                        statusapigarudafood = "Failed";
                    }
                } catch (Exception e) {
                    log.debug("Response getReport Exception() extension request : {} ", e);
                    statusapigarudafood = "Failed";
                }
                break;
            case "LOC":
                System.out.println("LOC");
                for (int i = 0; i < lengReportName; i++) {
                    String reportcodename = listReportName.get(i);
                    String[] splitcodename = reportcodename.split("_M");
                    String splitreportcode = splitcodename[0];
                    String splitreportname = splitcodename[1];
                    if (report.equalsIgnoreCase(splitreportcode) || report.equalsIgnoreCase(splitreportname)) {
                        reportcode = splitreportcode;
                        reportname = splitreportname;
                    }
                }
                final String finalregionname = group;

                reportRequest = new ReportRequest();
                easyparam = new ArrayList<>();
                loopparam = new ArrayList<>();
                parameterKey = "";
                if (reportcode.equalsIgnoreCase("r2")) {
                    parameterKey = "SKU";
                } else {
                    parameterKey = "LOC";

                }

                parameterValue = "";
                reportcode = reportcode + "_M";
                summaryReport = "Yes";
                lengList = 0;
                index = 0;
                dialog1 = "";
                next = "";
                if (sku.equalsIgnoreCase("all")) {
                    List<Region> listRegion = paramJSON.getListRegionfromFileJson(regionJson);
                    List<Region> listByRegion = listRegion.stream()
                            .filter(region -> region.region.equalsIgnoreCase(finalregionname))
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
                easyParam = new EasyParam();
                easyParam.setSzKey(parameterKey);
                easyParam.setSzValue(parameterValue);
                easyparam.add(easyParam);

                reportRequest.setSzReportName(reportcode);
                reportRequest.setLoopParam(loopparam);
                reportRequest.setSummary(summaryReport);
                reportRequest.setParam(easyparam);

                jsonReport = new JSONObject(reportRequest);
                jsonreport = jsonReport.toString();
                System.out.println("START HIT API Garudafood");

                try {
                    String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                    System.out.println(report);
                    RequestBody body = RequestBody.create(JSON, report);
                    Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                    Response response = okHttpUtil.getClient().newCall(request).execute();
                    JSONObject jsonobjhitapiloc = new JSONObject(response.body().string());
                    if (jsonobjhitapiloc.getString("error").equalsIgnoreCase("")) {
                        arrayReport = jsonobjhitapiloc.getJSONArray("path");
                        statusapigarudafood = "Success";
                    } else {
                        statusapigarudafood = "Failed";
                    }
                } catch (IOException | JSONException e) {
                    log.debug("Response getReport Exception() extension request : {} ", e);
                    statusapigarudafood = "Failed";
                }
                break;
            case "pass":
                for (int i = 0; i < lengReportName; i++) {
                    String reportcodename = listReportName.get(i);
                    String[] splitcodename = reportcodename.split("_M");
                    String splitreportcode = splitcodename[0];
                    String splitreportname = splitcodename[1];
                    if (report.equalsIgnoreCase(splitreportcode) || report.equalsIgnoreCase(splitreportname)) {
                        reportcode = splitreportcode;
                        reportname = splitreportname;
                    }
                }
                break;
        }
        System.out.println(statusapigarudafood);

        System.out.println("Response afer hit api");
        if (statusapigarudafood.equalsIgnoreCase("Success")) {
            System.out.println("Response afer hit api success");
            try {
                JSONObject jObj = arrayReport.getJSONObject(0);
                String url = jObj.getString("url");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                int leng = jsonArray.length();
//                int leng = 1;
                for (int i = 0; i < leng; i++) {
                    JSONObject dataobject = jsonArray.getJSONObject(i);
//                    String firstname = dataobject.getString("contactFirstName");
//                    String firstname = "Deka";
//                    String lastname = "";
                    JSONArray additionalfieldarray = new JSONArray();
                    String additionalfield = "";
                    String fullname = "";
                    String accountName = "";
                    try {
//                        lastname = dataobject.getString("contactLastName");
                        additionalfieldarray = dataobject.getJSONArray("additionalField");
                        additionalfield = additionalfieldarray.get(0).toString();
                        InfoUser dataInfoUser = new Gson().fromJson(additionalfield, InfoUser.class);
                        fullname = dataInfoUser.getFullName();
                        accountName = dataInfoUser.getAccountName();

                    } catch (Exception e) {
                        System.out.println(e);
//                        lastname = "";
                    }
                    String statusrole = "";
                    int lengBroadcast = listRoleBroadcast.size();
                    for (int b = 0; b < lengBroadcast; b++) {
                        String username = listRoleBroadcast.get(b);
                        if (username.equalsIgnoreCase(accountName)) {
                            statusrole = "Oke";
                            break;
                        }
                    }
                    if (statusrole.equalsIgnoreCase("Oke")) {
                        JSONArray socialIdTelegram = new JSONArray();
                        try {
                            socialIdTelegram = dataobject.getJSONArray("socialIdTelegram");
                            chatid = socialIdTelegram.get(0).toString();
                            text = fullname;
                            System.out.println(url);
                            URL input = new URL(url);
                            String reportAfterWatermark = generateWatermark.WatermarkImageReport(text, input);
                            System.out.println(reportAfterWatermark);
                            document = reportAfterWatermark;

                            JSONObject jsonSendDocument = new JSONObject(messageTelegram);
                            String bodyjson = jsonSendDocument.toString();
                            // Send Document 
                            documentTelegram.setChat_id(chatid);
                            documentTelegram.setDocument(document);
                            documentTelegram.setCaption(caption);
//                            documentTelegram.setCaption("TEST BROADCAST MESSAGE. Terima Kasih.");
                            jsonSendDocument = new JSONObject(documentTelegram);
                            bodyjson = jsonSendDocument.toString();
                            String urlApiTelegram = "https://api.telegram.org/bot850895190:AAHkqqUAFj6NeP5UrKkTe07l2fdBXDGT10U/sendDocument";
                            System.out.println(bodyjson);
                            RequestBody body = RequestBody.create(JSON, bodyjson);
                            Request request = new Request.Builder().url(urlApiTelegram).post(body).addHeader("Content-Type", "application/json").build();
                            Response response = okHttpUtil.getClient().newCall(request).execute();
                            JSONObject jsonobjBM = new JSONObject(response.body().string());
                            if (jsonobjBM.getBoolean("ok") == true) {
                                responseSendDocument = "Success Broadcast Document";
                            } else {
                                responseSendDocument = "Failed Broadcast Message. Please Try Again";
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    }
                }

            } catch (Exception e) {
                log.debug("Response BroadcastMessage Exception() extension request : {} ", e);
                responseSendDocument = "Failed Broadcast Message. Please Try Again";
            }
        } else {
            responseSendDocument = "File Report tidak ditemukan atau sedang terjadi kesalahan. Silahkan Cep API Generate Report Terlebih dahulu.";
        }

        return responseSendDocument;
    }

    // Send Message
//            messageTelegram.setChat_id(chatid);
//            messageTelegram.setText(caption);
//            String url = "https://api.telegram.org/bot850895190:AAHkqqUAFj6NeP5UrKkTe07l2fdBXDGT10U/sendMessage";
//            System.out.println(bodyjson);
//            RequestBody body = RequestBody.create(JSON, bodyjson);
//            Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json").build();
//            Response response = okHttpUtil.getClient().newCall(request).execute();
}
