/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.GarudafoodModel;

/**
 *
 * @author Deka
 */
public class LoopParam {

    private String szKey;
    private String szValue;

    public LoopParam() {
    }

    public LoopParam(String key, String value) {
        this.szKey = key;
        this.szValue = value;
    }

    public String getSzKey() {
        return szKey;
    }

    public void setSzKey(String szKey) {
        this.szKey = szKey;
    }

    public String getSzValue() {
        return szValue;
    }

    public void setSzValue(String szValue) {
        this.szValue = szValue;
    }

}
