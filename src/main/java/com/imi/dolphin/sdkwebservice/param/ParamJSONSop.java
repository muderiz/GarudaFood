/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.param;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imi.dolphin.sdkwebservice.GFmodel.Group;
import com.imi.dolphin.sdkwebservice.GFmodel.MasterSop;
import com.imi.dolphin.sdkwebservice.GFmodel.SOP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deka
 */
@Component
public class ParamJSONSop {

    public String getJSONStringfromFile(String fileName) {

        File file = FileUtils.getFile(fileName);

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                contentBuilder.append(sCurrentLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public List<MasterSop> getListMasterCompanyFromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<MasterSop> list = gson.fromJson(strJson.toString(), new TypeToken<List<MasterSop>>() {
        }.getType());
        return list;
    }

    public List<SOP> getListSOPFromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<SOP> list = gson.fromJson(strJson.toString(), new TypeToken<List<SOP>>() {
        }.getType());
        return list;
    }

}
