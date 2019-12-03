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
public class SOP {

    public String company;
    public String jenis_dokumen;
    public String divisi;
    public String nama_doc;
    public String link;
    public String file_name;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJenis_dokumen() {
        return jenis_dokumen;
    }

    public void setJenis_dokumen(String jenis_dokumen) {
        this.jenis_dokumen = jenis_dokumen;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getNama_doc() {
        return nama_doc;
    }

    public void setNama_doc(String nama_doc) {
        this.nama_doc = nama_doc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    
    
}
