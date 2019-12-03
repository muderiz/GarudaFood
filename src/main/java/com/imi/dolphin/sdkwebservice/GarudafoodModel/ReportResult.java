/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.GarudafoodModel;

import java.util.List;

/**
 *
 * @author Deka
 */
public class ReportResult {

    private List<ValuePath> path;
    private String error;

    public List<ValuePath> getPath() {
        return path;
    }

    public void setPath(List<ValuePath> path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
