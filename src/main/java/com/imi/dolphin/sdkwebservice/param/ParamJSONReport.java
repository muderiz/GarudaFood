/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.param;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportName;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Depo;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Group;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterDepartment;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterGroupProduct;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Region;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Role;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Product;
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
public class ParamJSONReport {

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

    public List<MasterDepartment> getListDepartmentfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<MasterDepartment> list = gson.fromJson(strJson.toString(), new TypeToken<List<MasterDepartment>>() {
        }.getType());
        return list;
    }

    public List<MasterGroupProduct> getListGroupProductfromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<MasterGroupProduct> list = gson.fromJson(strJson.toString(), new TypeToken<List<MasterGroupProduct>>() {
        }.getType());
        return list;
    }

    public List<ReportName> getListReportNamefromFileJson(String fileJson) {
        String strJson = getJSONStringfromFile(fileJson);
        Gson gson = new Gson();

        List<ReportName> list = gson.fromJson(strJson.toString(), new TypeToken<List<ReportName>>() {
        }.getType());
        return list;
    }
}
