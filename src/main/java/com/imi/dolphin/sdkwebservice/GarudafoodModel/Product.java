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
public class Product {

    public String group_category;
    public String lob;
    public String product_category;
    public String sub_product_category;
    public String sku;
    public String principal;

    public String getGroup_category() {
        return group_category;
    }

    public void setGroup_category(String group_category) {
        this.group_category = group_category;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getSub_product_category() {
        return sub_product_category;
    }

    public void setSub_product_category(String sub_product_category) {
        this.sub_product_category = sub_product_category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    
    
    
}
