/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.GarudafoodModel;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Deka
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterSop {

    public String name;
    public String id_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

}
