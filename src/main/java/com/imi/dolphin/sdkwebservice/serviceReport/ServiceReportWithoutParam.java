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
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportRequest;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ServiceReportWithoutParam {

    private static final Logger log = LogManager.getLogger(ServiceReportWithoutParam.class);

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

    public ExtensionResult ReportWithoutParam_getReport(ExtensionRequest extensionRequest) {
        log.debug("ReportWithoutParam_getReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        StringBuilder sb = new StringBuilder();

        String intention = sdkUtil.getEasyMapValueByName(extensionRequest, "intention");

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
        String kodereport = reportcode;
        String shortcutReport = kodereport;
        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        String text = fullName;
        if (!intention.equalsIgnoreCase("c1")) {
            reportcode = reportcode + "_M";
        }
        String summaryReport = "yes";
        int lengList = 0;
        int index = 0;
        String dialog1 = "";
        String dialog = "";

        // Set Param
        reportRequest.setSzReportName(reportcode);
        reportRequest.setLoopParam(loopparam);
        reportRequest.setSummary(summaryReport);
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
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Menu\" untuk melihat Menu Utama")
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

        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("ReportWithoutParam_getReport() extensionResult: {}", new Gson().toJson(extensionResult));
        return extensionResult;
    }
}
