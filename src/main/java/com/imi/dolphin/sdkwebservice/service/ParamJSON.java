/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imi.dolphin.sdkwebservice.GFmodel.Department;
import com.imi.dolphin.sdkwebservice.GFmodel.Depo;
import com.imi.dolphin.sdkwebservice.GFmodel.Group;
import com.imi.dolphin.sdkwebservice.GFmodel.Region;
import com.imi.dolphin.sdkwebservice.GFmodel.Role;
import com.imi.dolphin.sdkwebservice.GFmodel.Product;
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
public class ParamJSON {

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

    public List<Group> getListGroupfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Group> list = gson.fromJson(strJson.toString(), new TypeToken<List<Group>>() {
        }.getType());
        return list;
    }

    public List<Product> getListProductfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Product> list = gson.fromJson(strJson.toString(), new TypeToken<List<Product>>() {
        }.getType());
        return list;
    }

    public List<Region> getListRegionfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Region> list = gson.fromJson(strJson.toString(), new TypeToken<List<Region>>() {
        }.getType());
        return list;
    }

    public List<Depo> getListDepofromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Depo> list = gson.fromJson(strJson.toString(), new TypeToken<List<Depo>>() {
        }.getType());
        return list;
    }

    public List<Role> getListRolefromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Role> list = gson.fromJson(strJson.toString(), new TypeToken<List<Role>>() {
        }.getType());
        return list;
    }

    public List<Department> getListDepartmentfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<Department> list = gson.fromJson(strJson.toString(), new TypeToken<List<Department>>() {
        }.getType());
        return list;
    }
}
