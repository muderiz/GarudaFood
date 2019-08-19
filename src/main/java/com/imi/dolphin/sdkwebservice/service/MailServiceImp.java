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
import java.util.Properties;
import javax.mail.Session;

/**
 *
 * @author reja
 *
 */
@Service
public class MailServiceImp implements IMailService {

    @Autowired
    MailUtil mailUtil;

    @Autowired
    AppProperties appProperties;

    MimeMessage message;
    Properties mailServerProperties;
    Session getMailSession;

    @Override
    public String sendMail(MailModel mailModel) {
        try {
//            MimeMessage message = new MimeMessage(mailUtil.getMailSession());
//            message.setFrom(new InternetAddress(appProperties.getMailUsername()));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailModel.getRecipient()));
//            message.setSubject(mailModel.getSubject());
//            message.setText(mailModel.getText());
            System.out.println("\n 1st ===> setup Mail Server Properties..");
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            System.out.println("\n 2st ===> setup Mail Session.. ");
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            message = new MimeMessage(getMailSession);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailModel.getRecipient()));
//            message.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
            message.setSubject(mailModel.getSubject());
            message.setContent(mailModel.getText(), "text/html");

            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");
            transport.connect(appProperties.getMailSmtpHost(), appProperties.getMailUsername(), appProperties.getMailPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            return "Sent message successfully....";
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
        return "Sent message failed...";

    }

}
