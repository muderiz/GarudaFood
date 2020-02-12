/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.service;

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.model.MailModel;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import static com.imi.dolphin.sdkwebservice.service.ServiceImp.OUTPUT;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
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
public class ServiceRatingFeedback {

    private static final Logger log = LogManager.getLogger(ServiceImp.class);

    public static final String OUTPUT = "output";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String roleJson = "fileJson/role.json";
    public static final String reportnameJson = "fileJson/report_name.json";
    public static final String departmentJson = "fileJson/masterdepartment.json";
    public static final String grouproductJson = "fileJson/master_group_product.json";
    public static final String productJson = "fileJson/product.json";
    public static final String groupJson = "fileJson/group.json";
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

    public ExtensionResult SendFeedback(ExtensionRequest extensionRequest) {
        log.debug("SendFeedback() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        String rating = sdkUtil.getEasyMapValueByName(extensionRequest, "rating");
        String feedback = sdkUtil.getEasyMapValueByName(extensionRequest, "feedback");
//        String rating = "Baik";
//        String feedback = "Sedikit lebih banyak tentang Produk";
        String subject = "Chatbot | Feedback From Chatbot";
        String pesan = "Rating : " + rating + "\n"
                + "Feedback : " + feedback;
        String mail = "m.dekarizky@gmail.com";
        try {
            MailModel mailModel = new MailModel(mail, subject, pesan);
            String sendMailResult = svcMailService.sendMail(mailModel);
        } catch (Exception e) {
            System.out.println(e); 
        }

        return extensionResult;
    }
}
