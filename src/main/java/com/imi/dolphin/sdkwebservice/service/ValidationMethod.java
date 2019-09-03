/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ValidationMethod {

    private static final Logger log = LogManager.getLogger(ServiceImp.class);

    public String valEmail(String email) {
        log.debug("valEmailKaryawan() dari user di tangkap BOT : {} ", email);
        String regx = "^([A-Za-z][A-Za-z0-9\\-\\.\\_]*)\\@([A-Za-z][A-Za-z0-9\\-\\_]*)(\\.[A-Za-z][A-Za-z0-9\\-\\_]*)+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        String result = "";
        String[] splitemail = email.split("@");
        String domainemail = splitemail[1];

        if (matcher.find() && domainemail.equalsIgnoreCase("garudafood.co.id")) {
            result = "true";
        } else {
            result = "false";
        }
        System.out.println(result);

        return result;
    }
}
