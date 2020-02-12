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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.dolphin.sdkwebservice.model.MailModel;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.util.MailUtil;
import javax.mail.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author reja
 *
 */
@Service
public class MailServiceImp implements IMailService {

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);
    @Autowired
    MailUtil mailUtil;

    @Autowired
    AppProperties appProperties;

    @Override
    public String sendMail(MailModel mailModel) {
        try {
            MimeMessage message = new MimeMessage(mailUtil.getMailSession());
            message.setFrom(new InternetAddress("no-reply@garudafood.co.id"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailModel.getRecipient()));
            message.setSubject(mailModel.getSubject());
            message.setText(mailModel.getText());
//            message.setContent(mailModel.getText(), "text/html; charset=utf-8");
            Transport.send(message);
            log.debug("============== Send to Mail : SUCCESS ============");

            return "success";
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            log.debug("============== Send to Mail : FAILED ============", new Gson().toJson(e));
        }
        return "failed";

    }

}
