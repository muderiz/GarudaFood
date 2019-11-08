/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceSOP;

import com.imi.dolphin.sdkwebservice.GFmodel.SOP;
import com.imi.dolphin.sdkwebservice.param.ParamJSONSop;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class GetListJsonSOP {

    private static final String sopJson = "fileJson/sop/sop.json";

    @Autowired
    private ParamJSONSop paramJSON;

    public List<String> companyGeneral() {
        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
        List<SOP> listSop = listSopJson.stream()
                .sorted(Comparator.comparing(SOP::getCompany))
                .collect(Collectors.toList());
        List<String> listCompany = new ArrayList<>();
        int lengSop = listSop.size();
        for (int i = 0; i < lengSop; i++) {
            SOP companyArray = listSop.get(i);
            String companyName = companyArray.company;
            if (listCompany.contains(companyName)) {
            } else {
                listCompany.add(companyName);
            }
        }
        return listCompany;
    }

    public List<String> divisiGeneral(String company) {
        final String valueCompany = company;
        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);

        List<SOP> listByFilter = listSopJson.stream()
                .filter(sop -> sop.company.equalsIgnoreCase(valueCompany))
                .sorted(Comparator.comparing(SOP::getDivisi))
                .collect(Collectors.toList());
        List<String> listDivisi = new ArrayList<>();
        int lengDivisi = listByFilter.size();
        for (int i = 0; i < lengDivisi; i++) {
            SOP divisiArray = listByFilter.get(i);
            String Divisi = divisiArray.divisi;
            if (listDivisi.contains(Divisi)) {
            } else {
                listDivisi.add(Divisi);
            }
        }
        return listDivisi;
    }

    public List<String> jenisDokumenGeneral(String company, String divisi) {
        final String valueCompany = company;
        final String valueDivisi = divisi;
        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
        List<SOP> listByFilter = listSopJson.stream()
                .filter(sop -> sop.company.equalsIgnoreCase(valueCompany) && sop.divisi.equalsIgnoreCase(valueDivisi))
                .sorted(Comparator.comparing(SOP::getJenis_dokumen))
                .collect(Collectors.toList());
        List<String> listJenisDokumen = new ArrayList<>();
        int lengJenisDokumen = listByFilter.size();
        for (int i = 0; i < lengJenisDokumen; i++) {
            SOP jenisdokumenArray = listByFilter.get(i);
            String JenisDokumen = jenisdokumenArray.jenis_dokumen;
            if (listJenisDokumen.contains(JenisDokumen)) {
            } else {
                listJenisDokumen.add(JenisDokumen);
            }
        }
        return listJenisDokumen;
    }

    public List<String> namaDokumenGeneral(String company, String jenisdokumen, String divisi) {
        final String valueCompany = company;
        final String valueJenisDokumen = jenisdokumen;
        final String valueDivisi = divisi;
        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);

        List<SOP> listByFilter = listSopJson.stream()
                .filter(
                        sop -> sop.company.equalsIgnoreCase(valueCompany)
                        && sop.divisi.equalsIgnoreCase(valueDivisi)
                        && sop.jenis_dokumen.equalsIgnoreCase(valueJenisDokumen))
                .sorted(Comparator.comparing(SOP::getNama_doc))
                .collect(Collectors.toList());
        List<String> listNamaDokumen = new ArrayList<>();
        int lengNamaDokumen = listByFilter.size();
        for (int i = 0; i < lengNamaDokumen; i++) {
            SOP namaDokumenArray = listByFilter.get(i);
            String NamaDokumen = namaDokumenArray.nama_doc;
            if (listNamaDokumen.contains(NamaDokumen)) {
            } else {
                listNamaDokumen.add(NamaDokumen);
            }
        }
        return listNamaDokumen;
    }
}
