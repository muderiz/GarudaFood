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
package com.imi.dolphin.sdkwebservice.service;

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
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SendDocumentTelegram;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SendMessageTelegram;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamSdk;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.serviceSOP.ServiceImpSOP;
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
public class ServiceImp implements IService {

    private static final Logger log = LogManager.getLogger(ServiceImp.class);

    public static final String OUTPUT = "output";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    public static final String roleJson = "fileJson/role.json";
    public static final String reportnameJson = "fileJson/report_name.json";
    public static final String departmentJson = "fileJson/masterdepartment.json";
    public static final String grouproductJson = "fileJson/master_group_product.json";
    public static final String productJson = "fileJson/product.json";
    public static final String groupJson = "fileJson/group.json";
    public static final String skuJson = "fileJson/sku.json";
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

    /*
	 * Sample Srn status with static result
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getSrnResult(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getSrnResult(ExtensionRequest extensionRequest) {
        log.debug("getSrnResult() extension request: {}", extensionRequest);
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> output = new HashMap<>();
        StringBuilder respBuilder = new StringBuilder();
        respBuilder.append(
                "20-July-2018 16:10:32 Ahmad Mahatir Ridwan - PIC sudah onsite cek problem(printer nyala-mati)\n");
        respBuilder.append("PIC troubleshoot. restart printer(NOK), ganti kabel power(NOK)\n");
        respBuilder.append("PIC akan eskalasi ke vendor terkait.");
        output.put(OUTPUT, respBuilder.toString());
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Sample Customer Info with static result
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.imi.dolphin.sdkwebservice.service.IService#getCustomerInfo(com.imi.
	 * dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getCustomerInfo(ExtensionRequest extensionRequest) {
        log.debug("getCustomerInfo() extension request: {}", extensionRequest);
        String account = sdkUtil.getEasyMapValueByName(extensionRequest, "akun");
        String name = sdkUtil.getEasyMapValueByName(extensionRequest, "name");
        Map<String, String> output = new HashMap<>();
        StringBuilder respBuilder = new StringBuilder();
        if (account.substring(0, 1).equals("1")) {
            respBuilder.append("Ticket Number : " + extensionRequest.getIntent().getTicket().getTicketNumber() + "\n");
            respBuilder.append(" Data Customer Account " + account + "\n");
            respBuilder.append("Nama: " + name + "\n");
            respBuilder.append("Setoran tiap bulan : Rp. 500,000\n");
            respBuilder.append("Jatuh tempo berikutnya : 15 Agustus 2018");
        } else {
            respBuilder.append("Ticket Number : " + extensionRequest.getIntent().getTicket().getTicketNumber() + "\n");
            respBuilder.append(appProp.getFormId() + " Data Customer Account " + account + "\n");
            respBuilder.append("Nama: " + name + "\n");
            respBuilder.append("Setoran tiap bulan : Rp. 1,000,000\n");
            respBuilder.append("Jatuh tempo berikutnya : 27 Agustus 2018");
        }
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        output.put(OUTPUT, respBuilder.toString());
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Modify Customer Name Entity
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#clearCustomerName(com.imi.
	 * dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult modifyCustomerName(ExtensionRequest extensionRequest) {
        log.debug("modifyCustomerName() extension request: {}", extensionRequest);
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        Map<String, String> clearEntities = new HashMap<>();
        String name = sdkUtil.getEasyMapValueByName(extensionRequest, "name");
        if (name.equalsIgnoreCase("reja")) {
            clearEntities.put("name", "budi");
            extensionResult.setEntities(clearEntities);
        }
        return extensionResult;
    }

    /*
	 * Sample Product info with static value
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getProductInfo(com.imi.dolphin
	 * .sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getProductInfo(ExtensionRequest extensionRequest) {
        log.debug("getProductInfo() extension request: {}", extensionRequest);
        String model = sdkUtil.getEasyMapValueByName(extensionRequest, "model");
        String type = sdkUtil.getEasyMapValueByName(extensionRequest, "type");

        Map<String, String> output = new HashMap<>();
        StringBuilder respBuilder = new StringBuilder();

        respBuilder.append("Untuk harga mobil " + model + " tipe " + type + " adalah 800,000,000\n");
        respBuilder.append("Jika kak {customer_name} tertarik, bisa klik tombol dibawah ini. \n");
        respBuilder.append("Maka nanti live agent kami akan menghubungi kakak ;)");

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        output.put(OUTPUT, respBuilder.toString());
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Get messages from third party service
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getMessageBody(com.imi.dolphin
	 * .sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getMessageBody(ExtensionRequest extensionRequest) {
        log.debug("getMessageBody() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        StringBuilder respBuilder = new StringBuilder();

        try {
            okHttpUtil.init(true);
            Request request = new Request.Builder().url("https://jsonplaceholder.typicode.com/comments").get().build();
            Response response = okHttpUtil.getClient().newCall(request).execute();

            JSONArray jsonArray = new JSONArray(response.body().string());

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String message = jsonObject.getString("body");
            respBuilder.append(message);
        } catch (Exception e) {
            log.debug("getMessageBody() {}", e.getMessage());
        }

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        output.put(OUTPUT, respBuilder.toString());
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Generate quick replies output
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IFormService#getQuickReplies(com.imi.
	 * dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getQuickReplies(ExtensionRequest extensionRequest) {
        log.debug("getQuickReplies() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Hello", "World");
        map.put("Java", "Coffee");

        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Hello").addAll(map).build();
        output.put(OUTPUT, quickReplyBuilder.string());
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Generate Forms
	 *
	 * (non-Javadoc)
	 * 
	 * @see com.imi.dolphin.sdkwebservice.service.IService#getForms(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getForms(ExtensionRequest extensionRequest) {
        log.debug("getForms() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        FormBuilder formBuilder = new FormBuilder(appProp.getFormId());

        ButtonTemplate button = new ButtonTemplate();
        button.setTitle("Title is here");
        button.setSubTitle("Subtitle is here");
        button.setPictureLink(ParamSdk.SAMPLE_IMAGE_PATH);
        button.setPicturePath(ParamSdk.SAMPLE_IMAGE_PATH);
        List<EasyMap> actions = new ArrayList<>();
        EasyMap bookAction = new EasyMap();
        bookAction.setName("Label here");
        bookAction.setValue(formBuilder.build());
        actions.add(bookAction);
        button.setButtonValues(actions);
        ButtonBuilder buttonBuilder = new ButtonBuilder(button);

        output.put(OUTPUT, buttonBuilder.build());
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Generate buttons output
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getButtons(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getButtons(ExtensionRequest extensionRequest) {
        log.debug("getButtons() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        String dialog1 = "Berikut adalah nama-nama spesialisasi yang ada di RS Siloam. Silahkan menggeser menu dari kiri ke kanan untuk menampilkan semua opsi.  ";

        ButtonTemplate button = new ButtonTemplate();
        button.setTitle("");
        button.setSubTitle(" ");
        List<EasyMap> actions = new ArrayList<>();
        EasyMap bookAction = new EasyMap();
        bookAction.setName("List Spesialis");
        bookAction.setValue("Test");
        actions.add(bookAction);
        button.setButtonValues(actions);
        ButtonBuilder buttonBuilder = new ButtonBuilder(button);
        String imagebuilder = buttonBuilder.build().toString();

        ButtonTemplate button2 = new ButtonTemplate();
        button2.setTitle("");
        button2.setSubTitle(" ");
        List<EasyMap> actions2 = new ArrayList<>();
        EasyMap bookAction2 = new EasyMap();
        bookAction2.setName("Menu Utama");
        bookAction2.setValue("menu utama");
        actions2.add(bookAction2);
        button2.setButtonValues(actions2);
        ButtonBuilder buttonBuilder2 = new ButtonBuilder(button2);
        String imagebuilder2 = buttonBuilder2.build().toString();
        // ----------//
        output.put(OUTPUT, dialog1 + SPLIT + imagebuilder + SPLIT + imagebuilder2);
//        log.debug("getButtons2() extension request: {}", documentBuilder.build());

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Generate Carousel
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getCarousel(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getCarousel(ExtensionRequest extensionRequest) {
        log.debug("getCarousel() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();

        List<String> buttonList = new ArrayList<>();
        ButtonTemplate button;
        ButtonBuilder buttonBuilder;
        for (int i = 0; i < 6; i++) {
            button = new ButtonTemplate();
            button.setPictureLink(appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_WATERMARK_REPORT() + "graph1.jpeg");
            button.setPicturePath(appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_WATERMARK_REPORT() + "graph1.jpeg");
            button.setTitle(ParamSdk.SAMPLE_TITLE.concat(String.valueOf(i)));
            button.setSubTitle(ParamSdk.SAMPLE_SUBTITLE.concat(String.valueOf(i)));
            List<EasyMap> actions = new ArrayList<>();
            EasyMap bookAction = new EasyMap();
            bookAction.setName(ParamSdk.SAMPLE_LABEL);
            bookAction.setValue(ParamSdk.SAMPLE_PAYLOAD);
            actions.add(bookAction);
            button.setButtonValues(actions);
            buttonBuilder = new ButtonBuilder(button);
            buttonList.add(buttonBuilder.build());
        }
        CarouselBuilder carouselBuilder = new CarouselBuilder(buttonList);
        output.put(OUTPUT, carouselBuilder.build());

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Transfer ticket to agent
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#doTransferToAgent(com.imi.
	 * dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult doTransferToAgent(ExtensionRequest extensionRequest) {
        log.debug("doTransferToAgent() extension request: {}", extensionRequest);
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(true);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(false);
        return extensionResult;
    }

    /*
	 * Send Location
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#doSendLocation(com.imi.dolphin
	 * .sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult doSendLocation(ExtensionRequest extensionRequest) {
        log.debug("doSendLocation() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Kirim lokasi kakak ya")
                .add("Location", "location").build();
        output.put(OUTPUT, quickReplyBuilder.string());
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Generate Image
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.imi.dolphin.sdkwebservice.service.IService#getImage(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getImage(ExtensionRequest extensionRequest) {
        log.debug("getImage() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
//        String url = "https://autobot.garudafood.co.id/GeneratedFiles/watermarkReport/103735347.jpeg";
        String url = "https://cybertron.garudafood.co.id/GeneratedFiles/watermarkReport/84424992.jpeg";
//        ServiceImpSOP sOP = new ServiceImpSOP();
//        StringBuilder sb = new StringBuilder();
//
//        ButtonTemplate button = new ButtonTemplate();
//        button.setTitle("");
//        button.setSubTitle(" ");
//        List<EasyMap> actions = sOP.actionEasyMaps();
//        int i = 0;
//        for (i = 0; i < 5; i++) {
//            EasyMap bookAction = new EasyMap();
//            bookAction.setName(i + 1 + "");
//            bookAction.setValue(i + 1 + "");
//            actions.add(bookAction);
//        }
//        if (i == 5) {
//            EasyMap bookAction = new EasyMap();
//            bookAction.setName("Next");
//            bookAction.setValue("next");
//            actions.add(bookAction);
//
//            EasyMap bookAction2 = new EasyMap();
//            bookAction2.setName("Menu");
//            bookAction2.setValue("Menu");
//            actions.add(bookAction2);
//
//        }
//        button.setButtonValues(actions);
//        ButtonBuilder buttonBuilder = new ButtonBuilder(button);
//        sb.append(buttonBuilder.build()).append(SPLIT);
//
//        String dialog = "Test Button with 5 Action";
//        output.put(OUTPUT, dialog + SPLIT + sb.toString());

//        ButtonTemplate image = new ButtonTemplate();
////        image.setPictureLink("https://github.com/muderiz/image/blob/master/Siloam%20Logo.png?raw=true");
//        image.setPictureLink(url);
//        image.setPicturePath(url);
////        image.setTitle(""");
////        image.setSubTitle("Test");
//        ImageBuilder imageBuilder = new ImageBuilder(image);
//        output.put(OUTPUT, imageBuilder.build());
        ButtonTemplate image = new ButtonTemplate();
        image.setPictureLink(url);
        image.setPicturePath(url);

        DocumentBuilder documentBuilder = new DocumentBuilder(image);
        String btnBuilder = documentBuilder.build();
//        sb.append(btnBuilder).append(SPLIT);
        output.put(OUTPUT, btnBuilder);

        System.out.println(output);
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Split bubble chat conversation
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#getSplitConversation(com.imi.
	 * dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getSplitConversation(ExtensionRequest extensionRequest) {
        log.debug("getSplitConversation() extension request: {}", extensionRequest);
        String firstLine = "Terima kasih {customer_name}";
        String secondLine = "Data telah kami terima dan agent kami akan proses terlebih dahulu ya kak";
        Map<String, String> output = new HashMap<>();
        output.put(OUTPUT, firstLine + ParamSdk.SPLIT_CHAT + secondLine);

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*
	 * Send mail configuration on application.properties file
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imi.dolphin.sdkwebservice.service.IService#doSendMail(com.imi.dolphin.
	 * sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult doSendMail(ExtensionRequest extensionRequest) {
        log.debug("doSendMail() extension request: {}", extensionRequest);
//        String recipient = sdkUtil.getEasyMapValueByName(extensionRequest, "recipient");
        String recipient = "muhammad.rizky@mii.co.id";

        int max = 999999;
        int min = 111111;
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;

        System.out.println(randomNumber);
        String Pesan = "Untuk mengkonfirmasi email Anda, silahkan gunakan kode verifikasi(OTP) berikut : \n\n" + randomNumber;

        MailModel mailModel = new MailModel(recipient, "Test Email", Pesan);
//        String newResult = svcMailService.sendMail(mailModel);
        String sendMailResult = svcMailService.sendMail(mailModel);

        Map<String, String> output = new HashMap<>();
        output.put(OUTPUT, sendMailResult);
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /* (non-Javadoc)
	 * @see com.imi.dolphin.sdkwebservice.service.IService#getDolphinResponse(com.imi.dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getDolphinResponse(ExtensionRequest extensionRequest) {
        userToken = svcDolphinService.getUserToken(userToken);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String accName = dataInfoUser.getAccountName();
        String fullName = dataInfoUser.getFullName();
        String email = dataInfoUser.getMail();
//        InfoUser infouser = new InfoUser();
//        infouser.setAccountName("Test ACC Name");
//        infouser.setFullName("Test Full Name");
//        infouser.setMail("Test Mail");
//        List<String> listData = new ArrayList<>();
//        listData.add("" + new Gson().toJson(infouser, InfoUser.class) + "");
//        contact.setContactFirstName("Deka");
//        contact.setAdditionalField(listData);
//        contact = svcDolphinService.updateCustomer(userToken, contact);

        Map<String, String> output = new HashMap<>();
        output.put(OUTPUT, "PING " + contact.getContactFirstName() + "Additional :" + contact.getAdditionalField());
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /*  
	 * SDK with Authorization example
	 * 
	 * (non-Javadoc)
	 * @see com.imi.dolphin.sdkwebservice.service.IService#getPingResponse(com.imi.dolphin.sdkwebservice.model.ExtensionRequest)
     */
    @Override
    public ExtensionResult getPingResponse(ExtensionRequest extensionRequest) {
        log.debug("getPingResponse() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        if (!svcAuthService.isValidAuthToken(extensionRequest)) {
            extensionResult.setSuccess(false);
        } else {
            extensionResult.setSuccess(true);
            userToken = svcDolphinService.getUserToken(userToken);
            log.debug("getPingResponse() extension request: {} user token: {}", extensionRequest, userToken);
            String result = svcDolphinService.getPingResponse(userToken);
            output.put(OUTPUT, result);
            extensionResult.setValue(output);
        }
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult tanyaUsername(ExtensionRequest extensionRequest) {
        log.debug("tanyaUsername() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();

        String dialog = "Halo selamat datang di {bot_name}. Silahkan ketikan Username Anda terlebih dahulu ya.";
        clearEntities.put("tanya_username", "SKIP");

        output.put(OUTPUT, dialog);
        extensionResult.setValue(output);
        return extensionResult;
    }

    private SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
//        String[] attrIDs = {"objectGUID"};
//        searchControls.setReturningAttributes(attrIDs);
        return searchControls;
    }

    public ExtensionResult getUserLdap(ExtensionRequest extensionRequest) {
        log.debug("getUserInfo() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> output = new HashMap<>();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        String usernameUser = sdkUtil.getEasyMapValueByName(extensionRequest, "username");
        int randomNumber = 0;
        Map<String, String> clearEntities = new HashMap<>();
        LdapContext ctxx = null;
        String sAMAccountName = "";
        String name = "";
        try {
//            extensionResult = getInfo(usernameUser, ldap, extensionRequest);
            Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, appProp.getGARUDAFOOD_LDAP_LOGINPRINCIPAL());
            env.put(Context.SECURITY_CREDENTIALS, appProp.getGARUDAFOOD_LDAP_PASSWORD());
            env.put(Context.PROVIDER_URL, appProp.getGARUDAFOOD_LDAP_HOST());
            log.debug("getEnvLDAP() extension request: {}", new Gson().toJson(env));

            ctxx = new InitialLdapContext(env, null);
            log.debug("============== LDAP Connection: COMPLETE ============");
            ctxx.setRequestControls(null);
//            log.debug("ctxxLdap() extension request: {}", new Gson().toJson(ctxx));

            String filterBase = appProp.getGARUDAFOOD_LDAP_SEARCHATTRUSERNAME();

            NamingEnumeration<?> namingEnum = ctxx.search(appProp.getGARUDAFOOD_LDAP_DIRECTORYPATH(), filterBase + "=" + usernameUser, getSimpleSearchControls());
            SearchResult result = (SearchResult) namingEnum.next();
            Attributes attrs = result.getAttributes();
            sAMAccountName = attrs.get("sAMAccountName").get().toString();
            name = attrs.get("name").get().toString();
            String mail = attrs.get("mail").get().toString();
//            String mail = "m.dekarizky@gmail.com";
            mail = mail.toLowerCase();

            if (sAMAccountName.equalsIgnoreCase(usernameUser)) {
                // ============== Send OTP to Mail ============ //
                log.debug("============== Send OTP to Mail ============");
                int max = 999999;
                int min = 111111;
                Random random = new Random();
                randomNumber = random.nextInt(max + 1 - min) + min;

                System.out.println(randomNumber);
                String subject = "Chatbot | OTP From Chatbot";
                String pesan = "Untuk mengkonfirmasi email Anda, silahkan gunakan kode verifikasi(OTP) berikut : \n\n" + randomNumber;
//
//                MailModel mailModel = new MailModel(mail, subject, pesan);
//                String sendMailResult = svcMailService.sendMail(mailModel);
//                log.debug("============== Status Send Email : ", sendMailResult);

                String dialog1 = "Baiklah " + name + ". Silahkan cek email kantor anda untuk melihat OTP yang sudah dikirimkan.\n"
                        + "Ketikan OTP yang sudah dikirimkan ke Email Anda dengan benar.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Atau klik di bawah ini jika Anda tidak mendapatkan OTP tersebut.")
                        .add("Kirim OTP", "otp ulang").build();
                output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                clearEntities.put("otp_kesempatan", "3");
                clearEntities.put("otp", randomNumber + "");
                MailModel mailModel = new MailModel(mail, subject, pesan);
                String sendMailResult = svcMailService.sendMail(mailModel);
            }
        } catch (NamingException nex) {
            log.debug("============== LDAP Connection: FAILED ============", new Gson().toJson(nex));
            if (name.equals("")) {
                log.debug("============== User Not Found ============");
                String dialog = "Maaf Username Anda tidak ditemukan atau tidak terdaftar. Silahkan ketikan kembali Username Anda dengan benar.";
                output.put(OUTPUT, dialog);
                clearEntities.put("username", "");

            }
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        return extensionResult;
    }

    public ExtensionResult validasiOtp(ExtensionRequest extensionRequest) {
        log.debug("validasiOtp() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        String otpKesempatan = sdkUtil.getEasyMapValueByName(extensionRequest, "otp_kesempatan");
        String otp = sdkUtil.getEasyMapValueByName(extensionRequest, "otp");
        String otpFromUser = sdkUtil.getEasyMapValueByName(extensionRequest, "otp_kode");
        String usernameUser = sdkUtil.getEasyMapValueByName(extensionRequest, "username");

        Map<String, String> output = new HashMap<>();
        int randomNumber = 0;
        int kesempatanotp = Integer.parseInt(otpKesempatan);
        Map<String, String> clearEntities = new HashMap<>();

        if (kesempatanotp == 0) {
            userToken = svcDolphinService.getUserToken(userToken);
            String contactId = extensionRequest.getIntent().getTicket().getContactId();
            Contact contact = svcDolphinService.getCustomer(userToken, contactId);
            log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
            log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
            log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
            // ============== Get AdditionalField ============ //
            String b = contact.getAdditionalField().get(0);
            InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
            String mail = dataInfoUser.getMail();
//            String mail = "muhammad.rizky@mii.co.id";

            int max = 999999;
            int min = 111111;
            Random random = new Random();
            randomNumber = random.nextInt(max + 1 - min) + min;

            System.out.println(randomNumber);
            String subject = "Chatbot OTP Konfirmasi Akun";
            String pesan = "Untuk mengkonfirmasi email Anda, silahkan gunakan kode verifikasi(OTP) berikut : \n\n" + randomNumber;

            MailModel mailModel = new MailModel(mail, subject, pesan);
            String sendMailResult = svcMailService.sendMail(mailModel);
            log.debug("============== Status Send Email :", new Gson().toJson(sendMailResult));
            String dialog1 = "Anda sudah mencapai batas percobaan. Silahkan cek email kembali untuk melihat OTP terbaru.\n\n "
                    + "Ketikan OTP yang sudah dikirimkan ke Email Anda dengan benar.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Atau klik di bawah ini jika Anda tidak mendapatkan OTP tersebut.")
                    .add("Kirim OTP", "otp ulang").build();
            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
            kesempatanotp = 3;
            clearEntities.put("otp_kode", "");

        } else {
            if (otpFromUser.equalsIgnoreCase(otp)) {
                LdapContext ctxx = null;
                String name = "";
                try {
                    Hashtable<String, Object> env = new Hashtable<String, Object>();
                    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                    env.put(Context.SECURITY_AUTHENTICATION, "simple");
                    env.put(Context.SECURITY_PRINCIPAL, appProp.getGARUDAFOOD_LDAP_LOGINPRINCIPAL());
                    env.put(Context.SECURITY_CREDENTIALS, appProp.getGARUDAFOOD_LDAP_PASSWORD());
                    env.put(Context.PROVIDER_URL, appProp.getGARUDAFOOD_LDAP_HOST());
                    log.debug("getEnvLDAP() extension request: {}", new Gson().toJson(env));

                    ctxx = new InitialLdapContext(env, null);
                    log.debug("============== LDAP Connection: COMPLETE ============");
                    ctxx.setRequestControls(null);
//            log.debug("ctxxLdap() extension request: {}", new Gson().toJson(ctxx));

                    String filterBase = appProp.getGARUDAFOOD_LDAP_SEARCHATTRUSERNAME();

                    NamingEnumeration<?> namingEnum = ctxx.search(appProp.getGARUDAFOOD_LDAP_DIRECTORYPATH(), filterBase + "=" + usernameUser, getSimpleSearchControls());
                    SearchResult result = (SearchResult) namingEnum.next();
                    Attributes attrs = result.getAttributes();
                    String sAMAccountName = attrs.get("sAMAccountName").get().toString();
                    name = attrs.get("name").get().toString();
                    String mail = attrs.get("mail").get().toString();
                    // String mail = "m.dekarizky@gmail.com";
                    mail = mail.toLowerCase();

                    String dialog1 = "Hai " + name + ". Anda sudah berhasil melakukan konfirmasi akun.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Sekarang silahkan pilih Menu berikut yang Anda inginkan.")
                            .add("Report", "report").add("SOP", "sop").add("Konsumsi Bahan Bakar", "fuel").build();
                    output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());

                    // ============== Set AdditionalField ============ //
                    log.debug("============== Set AdditionalField ============");
                    userToken = svcDolphinService.getUserToken(userToken);
                    String contactId = extensionRequest.getIntent().getTicket().getContactId();
                    Contact contact = svcDolphinService.getCustomer(userToken, contactId);
                    log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
                    log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
                    log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));

                    InfoUser infouser = new InfoUser();
                    infouser.setAccountName(sAMAccountName);
                    infouser.setFullName(name);
                    infouser.setMail(mail);
                    infouser.setTitle("");
                    infouser.setDepartment("");
                    infouser.setCompany("");
                    infouser.setDivision("");
                    List<String> listData = new ArrayList<>();
                    listData.add("" + new Gson().toJson(infouser, InfoUser.class) + "");
                    contact.setAdditionalField(listData);
                    contact = svcDolphinService.updateCustomer(userToken, contact);
                    clearEntities.put("before_final", "yes");
                } catch (NamingException nex) {
                    log.debug("============== LDAP Connection: FAILED ============", new Gson().toJson(nex));
                    if (name.equals("")) {
                        log.debug("============== User Not Found ============");
                        String dialog = "Maaf Username Anda tidak ditemukan atau tidak terdaftar. Silahkan ketikan kembali Username Anda dengan benar.";
                        output.put(OUTPUT, dialog);
                        clearEntities.put("username", "");
                        clearEntities.put("otp_kesempatan", "");
                        clearEntities.put("otp", "");
                        clearEntities.put("otp_kode", "");

                    }
                }
            } else {
                kesempatanotp = kesempatanotp - 1;
                String dialog1 = "OTP yang Anda masukan salah. \n\nSilahkan ketikan kembali dengan benar.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Atau klik di bawah ini jika Anda tidak mendapatkan OTP tersebut.")
                        .add("Kirim OTP", "otp ulang").build();
                output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                clearEntities.put("otp_kode", "");

            }
        }

        clearEntities.put("otp_kesempatan", kesempatanotp + "");
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    public ExtensionResult menuUtama(ExtensionRequest extensionRequest) {
        log.debug("menuUtama() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //

        String b = contact.getAdditionalField().get(0);
        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
        String dialog1 = "Hai " + fullName + ". Anda sudah berhasil melakukan konfirmasi akun.";
        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Sekarang silahkan pilih Menu berikut yang Anda inginkan.")
                .add("Report", "report").add("SOP", "sop").build();
        output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    public ExtensionResult menuUtamaGeneral(ExtensionRequest extensionRequest) {
        log.debug("menuUtamaGeneral() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //
        try {
            String b = contact.getAdditionalField().get(0);
            if (!b.equalsIgnoreCase("")) {

                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Sekarang silahkan pilih Menu berikut yang Anda inginkan.")
                        .add("Report", "report").add("SOP", "sop").build();
                output.put(OUTPUT, quickReplyBuilder.string());
                InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                String usernameUser = dataInfoUser.getAccountName();
                String fullname = dataInfoUser.getFullName();
                String mail = dataInfoUser.getMail();
                String title = "";
                String company = "";
                String division = "";
                String department = "";
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

            }
        } catch (Exception ex) {
            System.out.println(ex);
            String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                    .add("Verifikasi Akun", "verifikasi").build();

            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());

        }

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

}
