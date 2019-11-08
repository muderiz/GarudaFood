/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.model;

import java.io.Serializable;

/**
 *
 * @author Deka
 */
public class Dialog implements Serializable {

    public Dialog(String Step, int Index) {
        this.Index = Index;
        this.Step = Step;
    }
    private String Action;

    private String Dialog;

    private int Index;

    private String Step;

    private String Map;

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public String getDialog() {
        return Dialog;
    }

    public void setDialog(String Dialog) {
        this.Dialog = Dialog;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int Index) {
        this.Index = Index;
    }

    public String getStep() {
        return Step;
    }

    public void setStep(String Step) {
        this.Step = Step;
    }

    public String getMap() {
        return Map;
    }

    public void setMap(String Map) {
        this.Map = Map;
    }
}
