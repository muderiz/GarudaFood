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

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GFmodel.Department;
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
import com.imi.dolphin.sdkwebservice.GFmodel.Depo;
import com.imi.dolphin.sdkwebservice.model.EasyMap;
import com.imi.dolphin.sdkwebservice.GFmodel.EasyParam;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.GFmodel.Group;
import com.imi.dolphin.sdkwebservice.GFmodel.InfoUser;
import com.imi.dolphin.sdkwebservice.GFmodel.LdapConnection;
import com.imi.dolphin.sdkwebservice.GFmodel.LdapModel;
import com.imi.dolphin.sdkwebservice.GFmodel.LoopParam;
import com.imi.dolphin.sdkwebservice.model.MailModel;
import com.imi.dolphin.sdkwebservice.GFmodel.Region;
import com.imi.dolphin.sdkwebservice.GFmodel.ReportRequest;
import com.imi.dolphin.sdkwebservice.GFmodel.ReportResult;
import com.imi.dolphin.sdkwebservice.GFmodel.Role;
import com.imi.dolphin.sdkwebservice.GFmodel.Product;
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
public class ServiceImp implements IService {

    private static final Logger log = LogManager.getLogger(ServiceImp.class);

    public static final String OUTPUT = "output";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;

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
    private ParamJSON paramJSON;


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

        ButtonTemplate button = new ButtonTemplate();
        button.setTitle(ParamSdk.SAMPLE_TITLE);
        button.setSubTitle(ParamSdk.SAMPLE_SUBTITLE);
        button.setPictureLink(appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_WATERMARK_REPORT() + "02102019_123610516.jpeg");
        button.setPicturePath(appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_WATERMARK_REPORT() + "02102019_123610516.jpeg");
        List<EasyMap> actions = new ArrayList<>();
        EasyMap bookAction = new EasyMap();
        bookAction.setName(ParamSdk.SAMPLE_LABEL);
        bookAction.setValue(ParamSdk.SAMPLE_PAYLOAD);
        actions.add(bookAction);
        button.setButtonValues(actions);

//        ButtonBuilder buttonBuilder = new ButtonBuilder(button);
        DocumentBuilder documentBuilder = new DocumentBuilder(button);

//        output.put(OUTPUT, buttonBuilder.build());
        output.put(OUTPUT, documentBuilder.build());
        log.debug("getButtons2() extension request: {}", documentBuilder.build());

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
        log.debug("getImage() extension request: {}", extensionRequest);
        Map<String, String> output = new HashMap<>();
//        String imagemap = sdkUtil.getEasyMapValueByName(extensionRequest, "param");

        ButtonTemplate image = new ButtonTemplate();
//        image.setPictureLink("https://github.com/muderiz/image/blob/master/Siloam%20Logo.png?raw=true");
//        image.setPictureLink("manual.pdf");
//        image.setPicturePath("manual.pdf");
        image.setPictureLink("https://autobot.garudafood.co.id/GeneratedFiles/watermarkReport/manual.pdf");
        image.setPicturePath("https://autobot.garudafood.co.id/GeneratedFiles/watermarkReport/manual.pdf");

//        image.setPicturePath(appProperties.getGARUDAFOOD_URL_GENERATEDFILES() + appProperties.getGARUDAFOOD_WATERMARK_REPORT() + "graph1.jpeg");
        image.setTitle("Test");
//        image.setSubTitle("Test");
//        ImageBuilder imageBuilder = new ImageBuilder(image);
//        output.put(OUTPUT, imageBuilder.build());
        DocumentBuilder documentBuilder = new DocumentBuilder(image);
        output.put(OUTPUT, documentBuilder.build());

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        log.debug("Output Image() extension request: {}", output);
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
        String recipient = sdkUtil.getEasyMapValueByName(extensionRequest, "recipient");

        int max = 999999;
        int min = 111111;
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;

        System.out.println(randomNumber);
        String Pesan = "Untuk mengkonfirmasi email Anda, silahkan gunakan kode verifikasi(OTP) berikut : \n" + randomNumber;

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
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        contact.setContactFirstName("YOUR NAME");
        contact = svcDolphinService.updateCustomer(userToken, contact);

        Map<String, String> output = new HashMap<>();
        output.put(OUTPUT, "PING " + contact.getContactFirstName());
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

    // -----------------------------------Method Start------------------------------------------------------ //
//    public ExtensionResult getUserInfo(ExtensionRequest extensionRequest) {
//        log.debug("getUserInfo() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
//
//        String contactId = extensionRequest.getIntent().getTicket().getContactId();
//        userToken = svcDolphinService.getUserToken(userToken, extensionRequest);
//        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
//        String b = contact.getAdditionalField().get(0);
//        InfoUser infoUser = new Gson().fromJson(b, InfoUser.class);
//        
//    }
    private SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
        //String[] attrIDs = {"objectGUID"};
        //searchControls.setReturningAttributes(attrIDs);
        return searchControls;
    }

    private ExtensionResult getInfo(String keyValue, LdapModel ldap, ExtensionRequest extensionRequest) throws NamingException, ParseException {
        log.debug("getInfo() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
//        Map<String, String> output = new HashMap<>();
        final String ldapAdServer = ldap.getServerInfo();
        final String ldapSearchBase = ldap.getSearchBase();
        final String ldapUsername = ldap.getUsername();
        final String ldapPassword = ldap.getPassword();
        final String ldapLoginPrincipal = ldap.getLoginPrincipal();
//
//        Hashtable<String, Object> env = new Hashtable<String, Object>();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
//        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
//        env.put(Context.PROVIDER_URL, ldapAdServer);
//
//        //ensures that objectSID attribute values
//        //will be returned as a byte[] instead of a String
////        env.put("java.naming.ldap.attributes.binary", "objectSID");
//        DirContext ldapContext = new InitialDirContext(env);
//
////        LdapContext ctxx = new InitialLdapContext(env, null);
////        ldapContext.setRequestControls(null);
////            ldapContext.setRequestControls(null);
//        String filterBase = appProp.getGARUDAFOOD_LDAP_SEARCHATTRUSERNAME();
//
//        NamingEnumeration<?> namingEnum = ldapContext.search(ldapSearchBase, filterBase + "=" + keyValue, getSimpleSearchControls());
//        SearchResult result = (SearchResult) namingEnum.next();
//        Attributes attrs = result.getAttributes();
//        String sAMAccountName = attrs.get("sAMAccountName").get().toString();
        String sAMAccountName = keyValue;
//        String mail = attrs.get("mail").get().toString();
//        System.out.println(mail);
//            mail = mail.toLowerCase();
        if (sAMAccountName.equalsIgnoreCase(keyValue)) {
//                String name = attrs.get("name").get().toString();
//                String name = keyValue;
//                System.out.println(name);
//                name = name.toLowerCase();
//                name = toTitleCase(name);
//                dn = result.getName() + ",dc=development03,dc=co,dc=id";

            // Set AdditionalField //
            userToken = svcDolphinService.getUserToken(userToken);
            log.debug("userToken() extension request: {}", userToken);
            String contactId = extensionRequest.getIntent().getTicket().getContactId();
            log.debug("getContactID() extension request: {}", contactId);

            Contact contact = svcDolphinService.getCustomer(userToken, contactId);
            log.debug("Contact() data contact: {}", new Gson().toJson(contact, Contact.class));
            InfoUser infoUser = new InfoUser();
            infoUser.setAccountName(sAMAccountName);
            infoUser.setFullName(sAMAccountName);
            infoUser.setMail("test@gmail.com");

            List<String> listData = new ArrayList<>();
            listData.add("" + new Gson().toJson(infoUser, InfoUser.class) + "");
            contact.setAdditionalField(listData);
            contact = svcDolphinService.updateCustomer(userToken, contact);
            // ------------------ //

//                extensionResult.setValue(output);
        }
//            namingEnum.close();
//            extensionResult.setValue(output);
        return extensionResult;
    }

    @Override
    public ExtensionResult getUserLdap(ExtensionRequest extensionRequest) {
        log.debug("getUserInfo() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> output = new HashMap<>();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

        String usernameUser = sdkUtil.getEasyMapValueByName(extensionRequest, "username");

        LdapModel ldap = new LdapModel();
        ldap.setUsername(appProp.getGARUDAFOOD_LDAP_USERNAME());
        ldap.setPassword(appProp.getGARUDAFOOD_LDAP_PASSWORD());
        ldap.setServerInfo(appProp.getGARUDAFOOD_LDAP_HOST());
        ldap.setSearchBase(appProp.getGARUDAFOOD_LDAP_DIRECTORYPATH());
        ldap.setLoginPrincipal(appProp.getGARUDAFOOD_LDAP_LOGINPRINCIPAL());
        Map<String, String> clearEntities = new HashMap<>();

//        Hashtable<String, Object> env = new Hashtable<String, Object>();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, appProp.getGARUDAFOOD_LDAP_USERNAME());
//        env.put(Context.SECURITY_CREDENTIALS, appProp.getGARUDAFOOD_LDAP_PASSWORD());
//        env.put(Context.PROVIDER_URL, appProp.getGARUDAFOOD_LDAP_HOST());
        try {
            extensionResult = getInfo(usernameUser, ldap, extensionRequest);
//            Map<String, String> clearEntities = new HashMap<>();
//            clearEntities.put("otp_kode", "12345");
//            clearEntities.put("otp_kesempatan", "3");
//            extensionResult.setEntities(clearEntities);
            //ensures that objectSID attribute values
            //will be returned as a byte[] instead of a String
//        env.put("java.naming.ldap.attributes.binary", "objectSID");
//            DirContext ldapContext = new InitialDirContext(env);

//            LdapContext ctxx = new InitialLdapContext(env, null);
//        ldapContext.setRequestControls(null);
//            ldapContext.setRequestControls(null);
//            String filterBase = appProp.getGARUDAFOOD_LDAP_SEARCHATTRUSERNAME();
//
//            NamingEnumeration<?> namingEnum = ldapContext.search(appProp.getGARUDAFOOD_LDAP_DIRECTORYPATH(), filterBase + "=" + usernameUser, getSimpleSearchControls());
//            SearchResult result = (SearchResult) namingEnum.next();
//            Attributes attrs = result.getAttributes();
//            String sAMAccountName = attrs.get("sAMAccountName").get().toString();
////            String sAMAccountName = keyValue;
//            String mail = attrs.get("mail").get().toString();
//            System.out.println(mail);
//            System.out.println(sAMAccountName);
        } catch (NamingException e) {

        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
//

        clearEntities.put("otp_kode", "12345");
        clearEntities.put("otp_kesempatan", "3");
        extensionResult.setEntities(clearEntities);
        String dialog = "Test";
        output.put(OUTPUT, dialog);
        extensionResult.setValue(output);
        return extensionResult;
    }

    public ExtensionResult menuUtama(ExtensionRequest extensionRequest) {
        log.debug("getUserInfo() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();

        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silahkan klik Menu berikut yang kamu inginkan.")
                .add("Report", "report").add("SOP", "sop").add("Konsumsi Bahan Bakar", "fuel").build();
        output.put(OUTPUT, quickReplyBuilder.string());
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        extensionResult.setValue(output);
        return extensionResult;
    }

    /**
     * Method General Execute API
     *
     * Bertujuan untuk mendapatkan Body Message dari API
     *
     * @param link berisi URL API yang ingin di Cek Body Messagenya
     * @return jsonobj : Return berupa JSon Object
     */
    private JSONObject GeneralExecuteAPI(String link) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.init(true);
        JSONObject jsonobj = null;
        try {

            Request request = new Request.Builder().url(link).get().build();
            Response response = okHttpUtil.getClient().newCall(request).execute();
            jsonobj = new JSONObject(response.body().string());
        } catch (IOException ex) {
        }

        return jsonobj;
    }

    @Override
    public ExtensionResult pertanyaanPertama(ExtensionRequest extensionRequest) {
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> clearEntities = new HashMap<>();

        String usernameUser = sdkUtil.getEasyMapValueByName(extensionRequest, "username");

        List<Role> listRole = paramJSON.getListRolefromFileJson("role.json");

        String title = "";
        String company = "";
        String division = "";
        String department = "";
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
            }
        }

        StringBuilder sb = new StringBuilder();
        switch (title.toLowerCase()) {
            case "top management":
//                String ContCompany = "";
                sb.append("{first_name} ingin melihat report dari Company apa?\n")
                        .append("1. GPPJ");
                clearEntities.put("role", title);
                break;
            case "management":
                sb.append("{first_name} ingin melihat report dari Departemen apa?\n")
                        .append("1. E2E");
//                        .append("2. Inbound")
//                        .append("3. Outbound");

                clearEntities.put("role", title);
                clearEntities.put("company", company);
                clearEntities.put("divisi", division);
                break;
            case "staff":
                sb.append("{custormer_name} ingin melihat report apa?\n");
                sb.append(" \n\n");
                sb.append("Silahkan pilih angka pada report yang anda inginkan.");
                clearEntities.put("role", title);
                clearEntities.put("company", company);
                clearEntities.put("divisi", division);
                clearEntities.put("departement", department);
                break;
        }

        String dialog1 = "Baiklah, {first_name} sudah berada di menu report";

        output.put(OUTPUT, dialog1 + SPLIT + sb.toString());
        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        return extensionResult;
    }

    @Override
    public ExtensionResult tanyaReportName(ExtensionRequest extensionRequest) {
        log.debug("tanyaReportName() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        String role = sdkUtil.getEasyMapValueByName(extensionRequest, "role");
        String departement = sdkUtil.getEasyMapValueByName(extensionRequest, "departement");
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        List<Department> listDepartment = paramJSON.getListDepartmentfromFileJson("department.json");
        int lengList = listDepartment.size();
        String urutan;
        String valueUrutan;
        String dialogmanagement;
        if (departement.equalsIgnoreCase("1") || departement.equalsIgnoreCase("e2e")) {
            if (role.equalsIgnoreCase("management")) {
                if (departement.equalsIgnoreCase("1")) {
                    departement = "e2e";
                } else if (departement.equalsIgnoreCase("2")) {
                    departement = "inbound";

                } else if (departement.equalsIgnoreCase("3")) {
                    departement = "outbound";
                }
                int j = 1;
                for (int i = 0; i < lengList; i++) {
                    Department departementArray = listDepartment.get(i);
                    String departemenName = departementArray.department;
                    String reportName = departementArray.report_name;
                    if (departemenName.equalsIgnoreCase(departement)) {
                        urutan = j + ". ";
                        valueUrutan = reportName + "\n";

                        sb.append(urutan).append(valueUrutan);
                        j++;
                    }
                }
            } else if (role.equalsIgnoreCase("staff")) {
                int j = 1;
                for (int i = 0; i < lengList; i++) {
                    Department departementArray = listDepartment.get(i);
                    String departemenName = departementArray.department;
                    String reportName = departementArray.report_name;
                    if (departemenName.equalsIgnoreCase(departement)) {
                        urutan = j + ". ";
                        valueUrutan = reportName + "\n";
                        sb.append(urutan).append(valueUrutan);
                        j++;
                    }
                }
            }

            String dialog1 = "{first_name} ingin melihat Report apa?\n";
            String dialog2 = "Silahkan pilih angka yang anda inginkan";

            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + SPLIT + dialog2);
            clearEntities.put("departement", "e2e");
        } else {
            sb.append("Mohon maaf untuk saat ini Departemen tersebut belum tersedia.")
                    .append("{first_name} ingin melihat report dari Departemen apa?\n")
                    .append("- E2E");
//                        .append("2. Inbound")
//                        .append("3. Outbound");

            clearEntities.put("departement", "");

            output.put(OUTPUT, sb.toString());
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    @Override
    public ExtensionResult tanyaKategori(ExtensionRequest extensionRequest) {
        log.debug("tanyaKategori() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String role = sdkUtil.getEasyMapValueByName(extensionRequest, "role");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String departement = sdkUtil.getEasyMapValueByName(extensionRequest, "departement");
        String namaReport = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");

        if (namaReport.equalsIgnoreCase("1") || namaReport.equalsIgnoreCase("daily production v sales")) {
            if (namaReport.equalsIgnoreCase("1")) {
                namaReport = "daily production v sales";
//            case "2":
//                namaReport = "daily";
//            case "3":
//                namaReport = "outbound";
//                clearEntities.put("nama_report", "daily production v sales");

            }
            List<Product> listProduct = paramJSON.getListProductfromFileJson("product.json");
            List<Product> listGPPJ = listProduct.stream()
                    .filter(product -> product.principal.equalsIgnoreCase(company))
                    .collect(Collectors.toList());
            String urutan;
            String valueUrutan;
            int lengList = listGPPJ.size();
            String addGroup = "";
            int j = 1;

            for (int i = 0; i < lengList; i++) {
                Product productArray = listGPPJ.get(i);
                String groupProduct = productArray.group_category;
                if (!addGroup.equalsIgnoreCase(groupProduct)) {
                    addGroup = groupProduct;
                    urutan = j + ". ";
                    valueUrutan = groupProduct + "\n";
                    sb.append(urutan).append(valueUrutan);
                    j++;
                }
            }
            String dialog1 = "Ingin melihat group kategori apa?\n" + sb.toString();
            String dialog2 = "Silahkan pilih angka yang anda inginkan.";
            output.put(OUTPUT, dialog1 + SPLIT + dialog2);
            clearEntities.put("nama_report", namaReport);
        } else {
            sb.append("Mohon maaf untuk saat ini Report tersebut belum tersedia.")
                    .append("{customer_name} ingin melihat Report apa?\n")
                    .append("1. Daily Production v Sales");
//                        .append("2. Inbound")
//                        .append("3. Outbound");

            clearEntities.put("nama_report", "");

            output.put(OUTPUT, sb.toString());
        }
        extensionResult.setEntities(clearEntities);

        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    @Override
    public ExtensionResult tanyaGroup(ExtensionRequest extensionRequest) {
        log.debug("tanyaGroup() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        StringBuilder sb = new StringBuilder();

        sb.append("Silahkan ketikan kode SKU/Product yang anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group. ");
        output.put(OUTPUT, sb.toString());
        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    @Override
    public ExtensionResult konfirmasiGroup(ExtensionRequest extensionRequest) {
        log.debug("konfirmasiGroup() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String kategoriGroup = sdkUtil.getEasyMapValueByName(extensionRequest, "kategorigroup");
        String NewGroup = "";
        if (kategoriGroup.equalsIgnoreCase("1")) {
            NewGroup = "Delist";
        } else if (kategoriGroup.equalsIgnoreCase("2")) {
            NewGroup = "Hero";

        } else if (kategoriGroup.equalsIgnoreCase("3")) {
            NewGroup = "NPL";

        } else if (kategoriGroup.equalsIgnoreCase("4")) {
            NewGroup = "Seasonal";

        } else if (kategoriGroup.equalsIgnoreCase("5")) {
            NewGroup = "Delist";

        } else if (kategoriGroup.equalsIgnoreCase("6")) {
            NewGroup = "Tier 2";

        } else if (kategoriGroup.equalsIgnoreCase("7")) {
            NewGroup = "Tier 3";
        }
        final String kategoriGroup2 = NewGroup;
        System.out.println(kategoriGroup2);
        clearEntities.put("kategorigroup", NewGroup);
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "group");
        if (sku.equalsIgnoreCase("all")) {
            clearEntities.put("before_final", "No");

        } else {
            List<Product> listProduct = paramJSON.getListProductfromFileJson("product.json");
            List<Product> listGPPJ = listProduct.stream()
                    .filter(product -> product.principal.equalsIgnoreCase(company))
                    .collect(Collectors.toList());
            List<Product> listByGroup = listGPPJ.stream()
                    .filter(product -> product.group_category.equalsIgnoreCase(kategoriGroup2))
                    .collect(Collectors.toList());
            String statussku = "tidak";
            String valueUrutan;
            int lengList = listByGroup.size();
            for (int i = 0; i < lengList; i++) {
                Product productArray = listByGroup.get(i);
                String skuProduct = productArray.sku;

                if (skuProduct.equalsIgnoreCase(sku)) {
                    statussku = "tepat";
                    break;
                } else if (skuProduct.contains(sku)) {
                    valueUrutan = skuProduct + "\n";
                    sb.append(valueUrutan);

                }
            }
            if (statussku.equalsIgnoreCase("tepat")) {
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Apakah anda ingin me-looping SKU tersebut?")
                        .add("Ya", "Yes").add("Tidak", "NO").build();

                output.put(OUTPUT, quickReplyBuilder.string());
            } else {
                clearEntities.put("group", "");
                String dialog1 = "Apakah Kode SKU berikut yang anda maksud?\n" + sb.toString();
                String dialog2 = "Silahkan ketik SKU yang anda inginkan.";
                output.put(OUTPUT, dialog1 + SPLIT + dialog2);
            }

        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
//        log.debug("konfirmasiGroup() extension result: {}", new Gson().toJson(extensionResult, ExtensionRequest.class));
        return extensionResult;
    }

    public ExtensionResult JenisGroup(ExtensionRequest extensionRequest) {
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        String jenis_group = sdkUtil.getEasyMapValueByName(extensionRequest, "jenis_group");

        String dialog1 = "Baiklah, sekarang silahkan ketik " + jenis_group + " yang ingin di cari.";
        output.put(OUTPUT, dialog1);
        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    @Override
    public ExtensionResult validasiJenisGroup(ExtensionRequest extensionRequest) {
        Map<String, String> output = new HashMap<>();
        Map<String, String> map = new HashMap<>();

        ExtensionResult extensionResult = new ExtensionResult();
        String kategori = sdkUtil.getEasyMapValueByName(extensionRequest, "kategori");
        String jenis_group = sdkUtil.getEasyMapValueByName(extensionRequest, "jenis_group");
        String konfirmasi_group = sdkUtil.getEasyMapValueByName(extensionRequest, "konfirmasi_group");
        if (kategori.equalsIgnoreCase("sku")) {
            kategori = kategori.toUpperCase();
        }
        String statusitem = "Tidak Ada";
        List<Group> listGroup = paramJSON.getListGroupfromFileJson("group.json");
        List<Product> listSKU = paramJSON.getListProductfromFileJson("sku.json");
        List<Region> listRegion = paramJSON.getListRegionfromFileJson("sku.json");
        List<Depo> listDepo = paramJSON.getListDepofromFileJson("sku.json");

        switch (jenis_group.toLowerCase()) {
            case "group":
                int lengListGroup = listGroup.size();
                for (int i = 0; i < lengListGroup; i++) {
                    Group groupArray = listGroup.get(i);
                    String sku = groupArray.SKU;
                    String region = groupArray.Region;
                    String depo = groupArray.Depo;
                    switch (kategori) {
                        case "SKU":
                            if (konfirmasi_group.equalsIgnoreCase(sku)) {
                                statusitem = "Ada";
                                break;
                            }
                        case "Region":
                            if (konfirmasi_group.equalsIgnoreCase(region)) {
                                statusitem = "Ada";
                                break;
                            }
                        case "Depo":
                            if (konfirmasi_group.equalsIgnoreCase(depo)) {
                                statusitem = "Ada";
                                break;
                            }
                        default:
                            statusitem = "Tidak Ada";
                    }
                }
            case "nama":
                switch (kategori) {
                    case "SKU":
                        int lengListSKU = listSKU.size();
//                        for (int i = 0; i < lengListSKU; i++) {
//                            Product skuArray = listSKU.get(i);
//                            String id = skuArray.id;
//                            String produk = skuArray.Produk;
//                            String Group = skuArray.Group;
//                            if (konfirmasi_group.equalsIgnoreCase(id)) {
//                                statusitem = "Ada";
//                                break;
//                            }
//                        }
                    case "Region":
                        int lengListRegion = listRegion.size();
                        for (int i = 0; i < lengListRegion; i++) {
                            Region regionArray = listRegion.get(i);
                            String id = regionArray.id;
                            String Region = regionArray.Region;
                            String Group = regionArray.Group;
                            if (konfirmasi_group.equalsIgnoreCase(id)) {
                                statusitem = "Ada";
                                break;
                            }
                        }
                    case "Depo":
                        int lengListDepo = listDepo.size();
                        for (int i = 0; i < lengListDepo; i++) {
                            Depo depoArray = listDepo.get(i);
                            String id = depoArray.id;
                            String Depo = depoArray.Depo;
                            String Group = depoArray.Group;
                            if (konfirmasi_group.equalsIgnoreCase(id)) {
                                statusitem = "Ada";
                                break;
                            }
                        }

                    default:
                        statusitem = "Tidak Ada";
                }

        }

        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("{customer_name} ingin mencari berdasarkan apa?").addAll(map).build();
        output.put(OUTPUT, quickReplyBuilder.string());
        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    /**
     *
     * @param extensionRequest
     * @return
     */
    @Override
    public ExtensionResult getReport(ExtensionRequest extensionRequest) {
        log.debug("getReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setNext(true);
        extensionResult.setSuccess(true);

        String username = sdkUtil.getEasyMapValueByName(extensionRequest, "username");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String namaReport = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");
        String kategorigroup = sdkUtil.getEasyMapValueByName(extensionRequest, "kategorigroup");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "group");
        String beforeFinal = sdkUtil.getEasyMapValueByName(extensionRequest, "before_final");

        String text = username;
        String parameterKey = "";
        String parameterValue = "";
        String reportname = "";
        String summary = "";
        if (namaReport.equalsIgnoreCase("daily production v sales")) {
            parameterKey = "SKU";
            reportname = "r1_m";
        }
        if (beforeFinal.equalsIgnoreCase("Yes")) {
            summary = "No";
        } else {
            summary = "Yes";
        }
        if (sku.equalsIgnoreCase("all")) {
            List<Product> listProduct = paramJSON.getListProductfromFileJson("product.json");
            List<Product> listGPPJ = listProduct.stream()
                    .filter(product -> product.principal.equalsIgnoreCase(company))
                    .collect(Collectors.toList());
            List<Product> listByGroup = listGPPJ.stream()
                    .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                    .collect(Collectors.toList());

            int lengList = listByGroup.size();
            for (int i = 0; i < lengList; i++) {
                Product productArray = listByGroup.get(i);
                String skuProduct = productArray.sku;
                if (i < 1) {
                    parameterValue = skuProduct;
                } else {
                    parameterValue = parameterValue + "|" + skuProduct;
                }
            }
            summary = "Yes";

        } else {
            parameterValue = sku;
        }

        Map<String, String> output = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        if (summary.equalsIgnoreCase("Yes")) {
            // Easy Param
            EasyParam easyParam = new EasyParam();
            easyParam.setSzKey(parameterKey);
            easyParam.setSzValue(parameterValue);
            easyparam.add(easyParam);
        } else {
            // Loop Param
            LoopParam loopParam = new LoopParam();
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
//        ReportResult reportResult = new ReportResult();
        String dialog1 = "";

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
//                   
                    URL input = new URL(url);
                    String reportAfterWatermark = generateWatermark.WatermarkImage(text, input);
                    System.out.println(reportAfterWatermark);
                    String[] spliturlreport = reportAfterWatermark.split(appProp.getGARUDAFOOD_WATERMARK_REPORT());
                    String reportnameTitle = spliturlreport[1];
                    ButtonTemplate image = new ButtonTemplate();

                    image.setTitle(reportnameTitle);
                    image.setPictureLink(reportAfterWatermark);
                    image.setPicturePath(reportAfterWatermark);
                    ImageBuilder imageBuilder = new ImageBuilder(image);
                    String btnBuilder = imageBuilder.build();
                    sb.append(btnBuilder).append(SPLIT);
                }
                dialog1 = "Berikut adalah Report yang {first_name} ingin lihat.";
            } else {
                dialog1 = "Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silahkan ketik \"Menu\" untuk melihat Menu Utama.";
            }
        } catch (Exception e) {
            System.out.println(e);
            log.debug("Response Exception() extension request : {} ", e);
        }
        output.put(OUTPUT, dialog1 + ParamSdk.SPLIT_CHAT + sb.toString());

        extensionResult.setValue(output);
        log.debug("String Builder Report() extension request : {} ", output);

        return extensionResult;
    }

    /**
     *
     * @param extensionRequest
     * @return
     */
    @Override
    public ExtensionResult getSOP(ExtensionRequest extensionRequest
    ) {
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setNext(true);
        extensionResult.setSuccess(true);
        String idKaryawan = sdkUtil.getEasyMapValueByName(extensionRequest, "idkaryawan");
        String namaKaryawan = sdkUtil.getEasyMapValueByName(extensionRequest, "namakaryawan");
        String text = idKaryawan + " | " + namaKaryawan;
        String pathFrom = "/example.pdf";
//        String pathFrom = "https://github.com/muderiz/image/blob/master/example.pdf?raw=true";
//        String pathFrom = "https://drive.google.com/uc?authuser=0&id=1CxAD3yGu8oaZTSxLGLoqaU6oPSLnYQYG&export=download";
        String sopAfterWatermark = generateWatermark.WatermarkPDF(text, pathFrom);
        System.out.println(sopAfterWatermark);
        return extensionResult;
    }

}
