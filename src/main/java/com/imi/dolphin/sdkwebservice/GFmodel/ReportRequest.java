/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.GFmodel;

import java.util.List;

/**
 *
 * @author Deka
 */
public class ReportRequest {

    private String szReportName;
    private List<LoopParam> loopParam;
    private String summary;
    private List<EasyParam> param;

    public String getSzReportName() {
        return szReportName;
    }

    public void setSzReportName(String szReportName) {
        this.szReportName = szReportName;
    }

    public List<LoopParam> getLoopParam() {
        return loopParam;
    }

    public void setLoopParam(List<LoopParam> loopParam) {
        this.loopParam = loopParam;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<EasyParam> getParam() {
        return param;
    }

    public void setParam(List<EasyParam> param) {
        this.param = param;
    }

}
