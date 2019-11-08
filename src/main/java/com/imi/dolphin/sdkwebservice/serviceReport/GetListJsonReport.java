/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceReport;

import com.imi.dolphin.sdkwebservice.GFmodel.Region;
import com.imi.dolphin.sdkwebservice.GFmodel.ReportName;
import com.imi.dolphin.sdkwebservice.GFmodel.SOP;
import com.imi.dolphin.sdkwebservice.param.ParamJSONReport;
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
public class GetListJsonReport {

    private static final String regionJson = "fileJson/report/region.json";
    private static final String reportnameJson = "fileJson/report/report_name.json";

    @Autowired
    private ParamJSONReport paramJSON;

    // General Report //
    public List<String> reportNameGeneral() {
        List<ReportName> listReportName = paramJSON.getListReportNamefromFileJson(reportnameJson);
        List<ReportName> listReportNameSorted = listReportName.stream()
                .sorted(Comparator.comparing(ReportName::getReport_name))
                .collect(Collectors.toList());

        List<String> listNameReport = new ArrayList<>();
        int lengRegion = listReportNameSorted.size();
        for (int i = 0; i < lengRegion; i++) {
            ReportName reportnameArray = listReportNameSorted.get(i);
            String reportname = reportnameArray.report_name;
            if (listNameReport.contains(reportname)) {
            } else {
                listNameReport.add(reportname);
            }
        }
        return listNameReport;
    }

    // ===============//
    // Region Report //
    public List<String> areaGeneral() {
        List<Region> listRegionJson = paramJSON.getListRegionfromFileJson(regionJson);
        List<Region> listRegion = listRegionJson.stream()
                .sorted(Comparator.comparing(Region::getArea))
                .collect(Collectors.toList());
        List<String> listArea = new ArrayList<>();
        int lengRegion = listRegion.size();
        for (int i = 0; i < lengRegion; i++) {
            Region regionArray = listRegion.get(i);
            String area = regionArray.area;
            if (listArea.contains(area)) {
            } else {
                listArea.add(area);
            }
        }
        return listArea;
    }

    public List<String> regionGeneral(String area) {
        final String valueArea = area;
        List<Region> listRegionJson = paramJSON.getListRegionfromFileJson(regionJson);
        List<Region> listRegion = listRegionJson.stream()
                .filter(region -> region.area.equalsIgnoreCase(valueArea))
                .sorted(Comparator.comparing(Region::getRegion))
                .collect(Collectors.toList());
        List<String> listRegionCode = new ArrayList<>();
        int lengRegion = listRegion.size();
        for (int i = 0; i < lengRegion; i++) {
            Region regionArray = listRegion.get(i);
            String regionCode = regionArray.region;
            if (listRegionCode.contains(regionCode)) {
            } else {
                listRegionCode.add(regionCode);
            }
        }
        return listRegionCode;
    }

    public List<String> SKUGeneral(String area, String regionCode) {
        final String valueArea = area;
        final String valueRegion = regionCode;
        List<Region> listRegionJson = paramJSON.getListRegionfromFileJson(regionJson);
        List<Region> listSKU = listRegionJson.stream()
                .filter(region -> region.area.equalsIgnoreCase(valueArea) && region.region.equalsIgnoreCase(valueRegion))
                .sorted(Comparator.comparing(Region::getLoc))
                .collect(Collectors.toList());
        List<String> listSKUCode = new ArrayList<>();
        int lengSKU = listSKU.size();
        for (int i = 0; i < lengSKU; i++) {
            Region skuArray = listSKU.get(i);
            String SKU = skuArray.getLoc();
            if (listSKUCode.contains(SKU)) {
            } else {
                listSKUCode.add(SKU);
            }
        }
        return listSKUCode;
    }
    // ----------------------------------------------------------------- //
}
