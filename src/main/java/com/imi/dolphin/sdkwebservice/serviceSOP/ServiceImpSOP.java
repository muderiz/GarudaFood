/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceSOP;

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.InfoUser;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.SOP;
import com.imi.dolphin.sdkwebservice.builder.ButtonBuilder;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
import com.imi.dolphin.sdkwebservice.model.EasyMap;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamJSONSop;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.AuthService;
import com.imi.dolphin.sdkwebservice.service.GenerateWatermark;
import com.imi.dolphin.sdkwebservice.service.IDolphinService;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import static com.imi.dolphin.sdkwebservice.service.ServiceImp.OUTPUT;
import com.imi.dolphin.sdkwebservice.serviceReport.ServiceImpReport;

import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ServiceImpSOP {

    private static final Logger log = LogManager.getLogger(ServiceImpReport.class);

    private static final String OUTPUT = "output";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    private static final String sopJson = "fileJson/sop/sop.json";
    private static final String snsJson = "fileJson/sop/sns.json";
    private static final String hoJson = "fileJson/sop/ho.json";
    private static final String bcuJson = "fileJson/sop/bc u.json";
    private static final String mastercompanyJson = "fileJson/sop/mastercompany.json";
    private static final String QUICK_REPLY_SYNTAX = "{replies:title=";
    private static final String QUICK_REPLY_SYNTAX_SUFFIX = "}";
    private static final String COMMA = ",";

    @Autowired
    AppProperties appProp;

    @Autowired
    IMailService svcMailService;

    @Autowired
    IDolphinService svcDolphinService;

    @Autowired
    AuthService svcAuthService;

    @Autowired
    OkHttpUtil okHttpUtil;

    @Autowired
    SdkUtil sdkUtil;

    @Autowired
    GenerateWatermark generateWatermark;

    @Autowired
    Gson gson;

    @Autowired
    private ParamJSONSop paramJSON;

    @Autowired
    GetListJsonSOP getListJsonSOP;

    public ExtensionResult sop_testGetList(ExtensionRequest extensionRequest) {
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();
        String intention = sdkUtil.getEasyMapValueByName(extensionRequest, "intention");
        System.out.println("Yang Bot Tangkap :" + intention);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);

//        List<SOP> listSop = paramJSON.getListSOPFromFileJson(sopJson);
//        List<SOP> listByFilter = listSop.stream()
//                .filter(sop -> sop.company.equalsIgnoreCase("sns") && sop.jenis_dokumen.equalsIgnoreCase("sop"))
//                .sorted(Comparator.comparing(SOP::getDivisi))
//                .collect(Collectors.toList());
////        List<SOP> listByDok = listByCompany.stream()
////                .filter(sop -> sop.jenis_dokumen.equalsIgnoreCase("sop"))
////                .collect(Collectors.toList());
////        List<SOP> listSNS = paramJSON.getListSOPFromFileJson(snsJson);
////        List<SOP> listHO = paramJSON.getListSOPFromFileJson(hoJson);
////        List<SOP> listBCU = paramJSON.getListSOPFromFileJson(bcuJson);
//        List<String> listJenisDokumen = new ArrayList<>();
//
//        int lengCompany = listByFilter.size();
//        for (int i = 0; i < lengCompany; i++) {
//            SOP companyArray = listByFilter.get(i);
//            String jenisDoc = companyArray.divisi;
//            if (listJenisDokumen.contains(jenisDoc)) {
//            } else {
//                listJenisDokumen.add(jenisDoc);
//            }
//        }
//        String dir = "./percobaan/";
//        File f = new File(dir);
//
//        String[] daftar = f.list();
//        java.util.Arrays.sort(daftar);
//
//        System.out.println("File dan direktori dalam ./percobaan");
//        System.out.println();
//
//        for (int i = 0; i < daftar.length; i++) {
//            File fTemp = new File(dir + "/" + daftar[i]);
////            if (fTemp.isDirectory()) {
////                System.out.println(daftar[i] + "\t\t<DIR>");
////            } else {
//            System.out.println(daftar[i]);
////                String dir2 = dir + daftar[i];
////                File hapus = new File(dir2);
////                hapus.delete();
////            }
//        }
//        System.out.println(listJenisDokumen);
        String dialog1 = "~~Hai Anda sudah berhasil melakukan konfirmasi akun.~~";
//        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Sekarang silahkan pilih Menu berikut yang kamu inginkan.")
//                .add("Report", "report").add("SOP", "sop").add("Konsumsi Bahan Bakar", "fuel").build();
        output.put(OUTPUT, dialog1);
//        clearEntities.put("param", "SKIP");

        extensionResult.setValue(output);
//        extensionResult.setEntities(clearEntities);

        return extensionResult;
    }

    private Boolean CekNumber(String a) {
        log.debug("CekNumber() dari user di tangkap BOT {} ", a);
        boolean result = true;
        if (a.matches("[0-9]*")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private StringBuilder ButtonGeneral(List<String> listJson, String title) {
        StringBuilder sb = new StringBuilder();
        ButtonTemplate button = new ButtonTemplate();
        button.setTitle("");
        button.setSubTitle("");
        List<EasyMap> actions = new ArrayList<>();
        int i = 0;
        int urutan = 1;
        int lengJson = listJson.size();
        for (i = 0; i < lengJson; i++) {
            String JsonName = listJson.get(i);
            sb.append(urutan + ". " + JsonName + "\n");

            EasyMap bookAction = new EasyMap();
            bookAction.setName(urutan + "");
            bookAction.setValue(JsonName);
            actions.add(bookAction);
            urutan++;

        }
        button.setButtonValues(actions);
        ButtonBuilder buttonBuilder = new ButtonBuilder(button);
        sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());

        return sb;
    }

    public ExtensionResult sop_PertanyaanPertama(ExtensionRequest extensionRequest) {
        log.debug("sop_PertanyaanPertama() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));

        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> clearEntities = new HashMap<>();
//        StringBuilder quickReply;

        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //
        try {
            String b = contact.getAdditionalField().get(0);
            InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
            String fullName = dataInfoUser.getFullName();
            if (!b.equalsIgnoreCase("")) {
                List<String> listCompany = new ArrayList<>();
                listCompany = getListJsonSOP.companyGeneral();

                StringBuilder sb = new StringBuilder();
                String dialog = "Baiklah, Bapak/Ibu " + fullName + " sudah berada di menu SOP\n";
                sb.append(dialog);

                ButtonTemplate button = new ButtonTemplate();
                button.setTitle("");
                button.setSubTitle("");
                List<EasyMap> actions = new ArrayList<>();
                int i = 0;
                int urutan = 1;
                int lengCompany = listCompany.size();
                for (i = 0; i < lengCompany; i++) {
                    String companyName = listCompany.get(i);
                    sb.append(urutan + ". " + companyName + "\n");

                    EasyMap bookAction = new EasyMap();
                    bookAction.setName(urutan + "");
                    bookAction.setValue(companyName);
                    actions.add(bookAction);
                    urutan++;

                }
                button.setButtonValues(actions);
                ButtonBuilder buttonBuilder = new ButtonBuilder(button);
                String title = "Sekarang Bapak/Ibu " + fullName + " ingin melihat SOP dari Company apa?";
                sb.append(SPLIT).append(title).append(SPLIT).append(buttonBuilder.build());
                output.put(OUTPUT, sb.toString());
                clearEntities.put("tanya_company", "SKIP");

            } else {
                String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                        .add("Verifikasi Akun", "verifikasi").build();
                output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                clearEntities.put("tanya_company", "verifikasi");
                clearEntities.put("company", "verifikasi");
                clearEntities.put("tanya_divisi", "verifikasi");
                clearEntities.put("divisi", "verifikasi");
                clearEntities.put("tanya_jenisdokumen", "verifikasi");
                clearEntities.put("jenisdokumen", "verifikasi");
                clearEntities.put("tanya_namadokumen", "verifikasi");
                clearEntities.put("namadokumen", "verifikasi");
                clearEntities.put("nomorurut", "verifikasi");
                clearEntities.put("index", "verifikasi");
                clearEntities.put("konfirmasi_dokumen", "verifikasi");
                clearEntities.put("konfirmasi_sop", "verifikasi");
                clearEntities.put("sop", "verifikasi");
                clearEntities.put("before_final", "verifikasi");
            }
        } catch (Exception ex) {
            String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                    .add("Verifikasi Akun", "verifikasi").build();
            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
            clearEntities.put("tanya_company", "verifikasi");
            clearEntities.put("company", "verifikasi");
            clearEntities.put("tanya_divisi", "verifikasi");
            clearEntities.put("divisi", "verifikasi");
            clearEntities.put("tanya_jenisdokumen", "verifikasi");
            clearEntities.put("jenisdokumen", "verifikasi");
            clearEntities.put("tanya_namadokumen", "verifikasi");
            clearEntities.put("namadokumen", "verifikasi");
            clearEntities.put("nomorurut", "verifikasi");
            clearEntities.put("index", "verifikasi");
            clearEntities.put("konfirmasi_dokumen", "verifikasi");
            clearEntities.put("konfirmasi_sop", "verifikasi");
            clearEntities.put("sop", "verifikasi");
            clearEntities.put("before_final", "verifikasi");
        }
        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        return extensionResult;
    }

    public ExtensionResult sop_tanyaDivisi(ExtensionRequest extensionRequest) {
        log.debug("sop_tanyaDivisi() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");

        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder quickReply;
        String namecompany = "";

        List<String> listCompany = new ArrayList<>();
        listCompany = getListJsonSOP.companyGeneral();

        boolean cekAngka = CekNumber(company);
        if (cekAngka == true) {
            int i = Integer.parseInt(company) - 1;
            namecompany = listCompany.get(i);
        } else {
            int lengCompany = listCompany.size();
            for (int i = 0; i < lengCompany; i++) {
                String companyName = listCompany.get(i);
                if (companyName.equalsIgnoreCase(company)) {
                    namecompany = companyName;
                    break;
                }
            }
        }
        if (namecompany.equalsIgnoreCase("")) {
            String dialog = "Maaf Kami tidak dapat menemukan Company yang Anda inginkan.\n";
            String title = "Silakan pilih kembali angka yang Anda inginkan.";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);
            int urutan = 1;
            int lengCompany = listCompany.size();
            for (int i = 0; i < lengCompany; i++) {
                String companyName = listCompany.get(i);
                sb.append(urutan + ". " + companyName + "\n");
                quickReply.append(urutan).append("@===@").append(companyName).append(COMMA);
                urutan++;
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("company", "");
            clearEntities.put("tanya_divisi", "");

        } else {
            List<String> listDivisi = new ArrayList<>();
            listDivisi = getListJsonSOP.divisiGeneral(namecompany);
            int urutan = 1;
            int i;
            int lengDivisi = listDivisi.size();
            String title = "";
            if (lengDivisi > 5) {
                lengDivisi = 5;
                title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Divisi lainnya.";
            } else {
                title = "Silakan pilih angka yang anda inginkan.";
            }

            String dialog = "Anda telah memilih Company " + namecompany + ". Selanjutnya Divisi apa yang ingin {first_name} lihat?\n";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);

            for (i = 0; i < lengDivisi; i++) {
                String divisiName = listDivisi.get(i);
                sb.append(urutan + ". " + divisiName + "\n");
                quickReply.append(urutan).append("@===@").append(divisiName).append(COMMA);
                urutan++;
            }
            if (lengDivisi >= 5) {
                quickReply.append("Next").append("@===@").append("next").append(COMMA);
                clearEntities.put("nomorurut", urutan + "");
                clearEntities.put("index", i + "");

            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("company", namecompany);
            clearEntities.put("tanya_divisi", "SKIP");
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_tanyaJenisDokumen(ExtensionRequest extensionRequest) {
        log.debug("sop_tanyaJenisDokumen() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");

        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder quickReply;

        List<String> listDivisi = new ArrayList<>();
        listDivisi = getListJsonSOP.divisiGeneral(company);
        String nameDivisi = "";
        boolean cekAngka = CekNumber(divisi);
        if (cekAngka == true) {
            int i = Integer.parseInt(divisi) - 1;
            nameDivisi = listDivisi.get(i);
        } else {
            int lengDivisi = listDivisi.size();
            for (int i = 0; i < lengDivisi; i++) {
                String divisiName = listDivisi.get(i);
                if (divisiName.equalsIgnoreCase(divisi)) {
                    nameDivisi = divisiName;
                    break;
                }
            }
        }

        if (divisi.equalsIgnoreCase("next")) {
            String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
            String index = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
            int urutan = Integer.parseInt(nomorurut);
            int i = Integer.parseInt(index);
            int lengDivisi = listDivisi.size();
            int newlengDivisi = lengDivisi - i;
            String title = "";
            if (newlengDivisi > 5) {
                newlengDivisi = 5;
                newlengDivisi = i + newlengDivisi;
                title = "Silakan pilih angka yang anda inginkan. Atau klik \"Next\" untuk Divisi lainnya.";
            } else {
                newlengDivisi = lengDivisi;
                title = "Silakan pilih angka yang anda inginkan.";
            }

            String dialog = "Anda telah memilih Company " + company + ". Selanjutnya Divisi apa yang ingin {first_name} lihat?\n";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);

            for (i = i; i < newlengDivisi; i++) {
                String divisiName = listDivisi.get(i);
                sb.append(urutan + ". " + divisiName + "\n");
                quickReply.append(urutan).append("@===@").append(divisiName).append(COMMA);
                urutan++;
            }
            if (lengDivisi > newlengDivisi) {
                quickReply.append("Next").append("@===@").append("next").append(COMMA);
                clearEntities.put("nomorurut", urutan + "");
                clearEntities.put("index", i + "");
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("divisi", "");
            clearEntities.put("tanya_jenisdokumen", "");

        } else if (nameDivisi.equalsIgnoreCase("")) {
            String dialog = "Maaf Kami tidak dapat menemukan Divisi yang Anda inginkan.\n";
            String title = "Silakan pilih kembali angka yang Anda inginkan.";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);
            int urutan = 1;
            int lengDivisi = listDivisi.size();
            for (int i = 0; i < lengDivisi; i++) {
                String divisiName = listDivisi.get(i);
                sb.append(urutan + ". " + divisiName + "\n");
                quickReply.append(urutan).append("@===@").append(divisiName).append(COMMA);
                urutan++;
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("divisi", "");
            clearEntities.put("tanya_jenisdokumen", "");
        } else {
            List<String> listJenisDokumen = new ArrayList<>();
            listJenisDokumen = getListJsonSOP.jenisDokumenGeneral(company, nameDivisi);

            String dialog = "Anda telah memilih Divisi " + nameDivisi + ". Selanjutnya Jenis Dokumen apa yang ingin {first_name} lihat?\n";
            String title = "Silakan pilih angka yang anda inginkan.";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);
            int urutan = 1;
            int lengJenisDokumen = listJenisDokumen.size();
            for (int i = 0; i < lengJenisDokumen; i++) {
                String jenisdokumenName = listJenisDokumen.get(i);
                sb.append(urutan + ". " + jenisdokumenName + "\n");
                quickReply.append(urutan).append("@===@").append(jenisdokumenName).append(COMMA);
                urutan++;
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("divisi", nameDivisi);
            clearEntities.put("tanya_jenisdokumen", "SKIP");
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_tanyaNamaDokumen(ExtensionRequest extensionRequest) {
        log.debug("sop_tanyaNamaDokumen() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");

        StringBuilder sb = new StringBuilder();
        StringBuilder quickReply;

        List<String> listJenisDokumen = new ArrayList<>();
        listJenisDokumen = getListJsonSOP.jenisDokumenGeneral(company, divisi);
        String nameJenisDokumen = "";
        boolean cekAngka = CekNumber(jenisdokumen);
        if (cekAngka == true) {
            int i = Integer.parseInt(jenisdokumen) - 1;
            nameJenisDokumen = listJenisDokumen.get(i);
        } else {
            int lengJenisDokumen = listJenisDokumen.size();
            for (int i = 0; i < lengJenisDokumen; i++) {
                String jenisdokumenName = listJenisDokumen.get(i);
                if (jenisdokumenName.equalsIgnoreCase(jenisdokumen)) {
                    nameJenisDokumen = jenisdokumenName;
                    break;
                }
            }
        }

        if (nameJenisDokumen.equalsIgnoreCase("")) {
            String dialog = "Maaf Kami tidak dapat menemukan Jenis Dokumen yang Anda inginkan.\n";
            String title = "Silakan pilih kembali angka yang Anda inginkan.";
            quickReply = new StringBuilder();
            quickReply.append(QUICK_REPLY_SYNTAX);
            quickReply.append(title).append(COMMA);
            int urutan = 1;
            int lengJenisDokumen = listJenisDokumen.size();
            for (int i = 0; i < lengJenisDokumen; i++) {
                String jenisdokumenName = listJenisDokumen.get(i);
                sb.append(urutan + ". " + jenisdokumenName + "\n");
                quickReply.append(urutan).append("@===@").append(jenisdokumenName).append(COMMA);
                urutan++;
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("jenisdokumen", "");
            clearEntities.put("tanya_namadokumen", "");
        } else {
            String title = "Silakan ketik " + nameJenisDokumen.toUpperCase() + " yang anda inginkan. Atau klik dibawah ini untuk melihat list " + nameJenisDokumen.toUpperCase() + " yang ada di kategori ini.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder(title)
                    .add("View ALL", "view all").build();

            output.put(OUTPUT, quickReplyBuilder.string());
            clearEntities.put("jenisdokumen", nameJenisDokumen);
            clearEntities.put("tanya_namadokumen", "SKIP");
            clearEntities.put("nomorurut", "1");
            clearEntities.put("index", "0");
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_konfirmasiNamaDokumen(ExtensionRequest extensionRequest) {
        log.debug("sop_konfirmasiNamaDokumen() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();

        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");
        String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        System.out.println(divisi);
        String namadokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "namadokumen");
        namadokumen = namadokumen.toLowerCase();
        System.out.println(namadokumen);
        String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
        String indexDokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "index");

        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder quickReply = new StringBuilder();
        quickReply.append(QUICK_REPLY_SYNTAX);
        String statussku = "";
        int index = Integer.parseInt(indexDokumen);
        if (namadokumen.equalsIgnoreCase("view all")) {
            List<String> listNamaDokumen = new ArrayList<>();
            listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
            String dialog = "Berikut adalah pilihan Dokumen " + jenisdokumen + " yang bisa {first_name} lihat :\n";
            String title = "Silakan pilih angka yang Anda inginkan. Klik \"Next\" untuk melihat pilihan lainnya";
            quickReply.append(title).append(COMMA);

            int urutan = Integer.parseInt(nomorurut);
            int lengNamaDokumen = listNamaDokumen.size();
            int newlengNamaDokumen;
            int addindex = lengNamaDokumen - index;
            if (addindex >= 5) {
                addindex = 5;
                newlengNamaDokumen = index + addindex;
            } else {
                newlengNamaDokumen = lengNamaDokumen;
            }
            int i = 0;
            for (i = index; i < newlengNamaDokumen; i++) {
                String namaDokumenName = listNamaDokumen.get(i);
                sb.append(urutan + ". " + namaDokumenName + "\n");
                quickReply.append(urutan).append("@===@").append(namaDokumenName).append(COMMA);
                urutan++;
            }
            if (newlengNamaDokumen < lengNamaDokumen) {
                quickReply.append("Next").append("@===@").append("next").append(COMMA);
                quickReply.append("Menu").append("@===@").append("menu utama").append(COMMA);
            } else {
                quickReply.append("Menu").append("@===@").append("menu utama").append(COMMA);
            }
            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("nomorurut", urutan + "");
            clearEntities.put("index", i + "");
            clearEntities.put("namadokumen", "");
        } else if (namadokumen.equalsIgnoreCase("next")) {
            List<String> listNamaDokumen = new ArrayList<>();
            listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
            String dialog = "Berikut adalah pilihan Dokumen " + jenisdokumen + " yang bisa {first_name} lihat :\n";
            String title = "Silakan pilih kembali angka yang Anda inginkan. Klik \"Next\" untuk melihat pilihan lainnya";
            quickReply.append(title).append(COMMA);

            int urutan = Integer.parseInt(nomorurut);
            int lengNamaDokumen = listNamaDokumen.size();
            int newlengNamaDokumen;
            int addindex = lengNamaDokumen - index;
            if (addindex >= 5) {
                addindex = 5;
                newlengNamaDokumen = index + addindex;
            } else {
                newlengNamaDokumen = lengNamaDokumen;
            }
            int i = 0;
            for (i = index; i < newlengNamaDokumen; i++) {
                String namaDokumenName = listNamaDokumen.get(i);
                sb.append(urutan + ". " + namaDokumenName + "\n");
                quickReply.append(urutan).append("@===@").append(namaDokumenName).append(COMMA);
                urutan++;
            }
            if (newlengNamaDokumen < lengNamaDokumen) {
                quickReply.append("Next").append("@===@").append("next").append(COMMA);
                quickReply.append("Menu").append("@===@").append("menu utama").append(COMMA);
            } else {
                quickReply.append("Menu").append("@===@").append("menu utama").append(COMMA);
            }

            quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
            quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
            output.put(OUTPUT, dialog + sb.toString() + SPLIT + quickReply.toString());
            clearEntities.put("nomorurut", urutan + "");
            clearEntities.put("index", i + "");
            clearEntities.put("namadokumen", "");
        } else {
            List<String> listNamaDokumen = new ArrayList<>();
            listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
            boolean cekAngka = CekNumber(namadokumen);
            String nameDokumen = "";

            if (cekAngka == true) {
                int i = Integer.parseInt(namadokumen) - 1;
                nameDokumen = listNamaDokumen.get(i);
                statussku = "tepat";
            } else {
                String title = "Silakan pilih nama Dokumen " + jenisdokumen + " sesuai yang Anda inginkan.";
                quickReply.append(title).append(COMMA);
                int urutan = 1;
                int lengDokumen = listNamaDokumen.size();
                for (int i = 0; i < lengDokumen; i++) {
                    String dokumenname = listNamaDokumen.get(i);
                    dokumenname = dokumenname.toLowerCase();
                    String namaDoc = listNamaDokumen.get(i);
                    if (dokumenname.equalsIgnoreCase(namadokumen)) {
                        nameDokumen = dokumenname;
                        statussku = "tepat";
                        break;
                    } else if (dokumenname.contains(namadokumen)) {
                        statussku = "tidak";
                        nameDokumen = namaDoc;
                        sb.append(urutan + ". " + nameDokumen + "\n");
                        quickReply.append(urutan).append("@===@").append(nameDokumen).append(COMMA);
                        urutan++;

                    }
                }
            }

            if (statussku.equalsIgnoreCase("tepat")) {
                clearEntities.put("konfirmasi_dokumen", "yes");
            } else {
                clearEntities.put("namadokumen", "");
                if (sb.toString().equalsIgnoreCase("")) {
                    String dialog1 = "Maaf, tidak dapat menemukan Nama Dokumen tersebut.";
                    String title = "Silakan ketik kembali Dokumen " + jenisdokumen + " yang anda inginkan. Atau klik dibawah ini untuk melihat list Dokumen " + jenisdokumen + " yang ada di kategori ini.";
                    QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder(title)
                            .add("View ALL", "view all").build();

                    output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());

                } else {
                    String dialog1 = "Apa Dokumen " + jenisdokumen + " berikut yang anda maksud ?\n";
                    quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
                    quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
                    output.put(OUTPUT, dialog1 + sb.toString() + SPLIT + quickReply.toString());
                }
            }
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult sop_konfirmasiSOP(ExtensionRequest extensionRequest) {
        log.debug("sop_konfirmasiSOP() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        String b = contact.getAdditionalField().get(0);

        InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
        String fullName = dataInfoUser.getFullName();
        String text = fullName;
//        String text = "DekaRizky";

        final String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        final String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");
        final String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        String namadokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "namadokumen");
        String nomorurut = sdkUtil.getEasyMapValueByName(extensionRequest, "nomorurut");
        String indexDokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "index");
        List<String> listNamaDokumen = new ArrayList<>();
        listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
        boolean cekAngka = CekNumber(namadokumen);
        if (cekAngka == true) {
            int i = Integer.parseInt(namadokumen) - 1;
            namadokumen = listNamaDokumen.get(i);
        } else {
            int lengDokumen = listNamaDokumen.size();
            for (int i = 0; i < lengDokumen; i++) {
                String dokumenname = listNamaDokumen.get(i);
                if (dokumenname.equalsIgnoreCase(namadokumen)) {
                    namadokumen = dokumenname;
                    break;
                }
            }
        }
        List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
        List<SOP> listByFilter = listSopJson.stream()
                .filter(
                        sop -> sop.company.equalsIgnoreCase(company)
                        && sop.jenis_dokumen.equalsIgnoreCase(jenisdokumen)
                        && sop.divisi.equalsIgnoreCase(divisi))
                .collect(Collectors.toList());

        try {
//            List<String> listFileName = new ArrayList<>();
//            int lengNamaDokumen = listByFilter.size();
//            for (int i = 0; i < lengNamaDokumen; i++) {
//                SOP namaDokumenArray = listByFilter.get(i);
//                String filename = namaDokumenArray.file_name;
//                if (listFileName.contains(filename)) {
//                } else {
//                    listFileName.add(filename);
//                }
//            }

            int lengNamaDokumen = listByFilter.size();
            String linkDoc = "";
            String namaDoc = "";
            String fileDoc = "";
            for (int i = 0; i < lengNamaDokumen; i++) {
                SOP sopArray = listByFilter.get(i);
                namaDoc = sopArray.nama_doc;

                String inputUrl = "";
                if (namaDoc.equalsIgnoreCase(namadokumen)) {
                    linkDoc = sopArray.link;
                    fileDoc = sopArray.file_name.replace(".pdf", "").replace(".jepg", "").replace(".jpg", "");

                    String dir = appProp.getGARUDAFOOD_PATH_GENERATEDFILES() + appProp.getGARUDAFOOD_BASE_SOP() + company.toUpperCase() + "/" + fileDoc + "/";
                    File f = new File(dir);
                    System.out.println("File dan direktori dalam " + dir);
                    String[] daftar = f.list();
//                    java.util.Arrays.sort(daftar);
                    System.out.println(daftar);
                    Arrays.sort(daftar);

                    for (int j = 0; j < daftar.length; j++) {
//                        File fTemp = new File(dir + daftar[i]);
                        String dir2 = appProp.getGARUDAFOOD_URL_GENERATEDFILES() + appProp.getGARUDAFOOD_BASE_SOP() + company.toUpperCase() + "/" + fileDoc + "/";
                        System.out.println(dir2 + daftar[j]);
                        inputUrl = dir2 + daftar[j];
//                      inputUrl = "https://autobot.garudafood.co.id/GeneratedFiles/baseSop/09102019_11251473.jpeg";
                        URL input = new URL(inputUrl);
                        String reportAfterWatermark = generateWatermark.WatermarkImageSOP(text, input, daftar[j]);
                        System.out.println(reportAfterWatermark);
                        ButtonTemplate image = new ButtonTemplate();
                        image.setPictureLink(reportAfterWatermark);
                        image.setPicturePath(reportAfterWatermark);

                        DocumentBuilder documentBuilder = new DocumentBuilder(image);
                        String btnBuilder = documentBuilder.build();
                        sb.append(btnBuilder).append(SPLIT);
                    }
                    break;
                }
            }
            String dialog1 = "Berikut adalah " + fileDoc + " yang {first_name} ingin lihat.\n";
//            String dialog2 = "Jika membutuhkan SOP lebih detail silahkan klik link berikut :\n" + linkDoc;

            String title = "- Klik \"Detail\" jika membutuhkan " + jenisdokumen.toUpperCase() + " lebih detail\n"
                    + "- Klik \"Dokumen Lain\" untuk melihat Dokumen " + jenisdokumen + " lainnya\n"
                    + "- Klik \"Menu\" untuk melihat Menu yang ada.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                    .add("Detail", linkDoc).add("Dokumen Lain", "dokumen lain").add("Menu", "menu utama").build();

            output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + title + SPLIT + quickReplyBuilder.string());
//            output.put(OUTPUT, dialog2);
            clearEntities.put("konfirmasi_sop", "yes");
        } catch (MalformedURLException e) {
            System.out.println(e);
        }

        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        log.debug("String Builder getSOP() extension request : {} ", new Gson().toJson(extensionResult));

        return extensionResult;
    }

    public List<EasyMap> actionEasyMaps() {
        List<EasyMap> actions = new ArrayList<>();
        int i = 0;
        for (i = 0; i < 5; i++) {
            EasyMap bookAction = new EasyMap();
            bookAction.setName(i + 1 + "");
            bookAction.setValue(i + 1 + "");
            actions.add(bookAction);
        }
        if (i == 5) {
            EasyMap bookAction = new EasyMap();
            bookAction.setName("Next");
            bookAction.setValue("next");
            actions.add(bookAction);

            EasyMap bookAction2 = new EasyMap();
            bookAction2.setName("Menu");
            bookAction2.setValue("Menu");
            actions.add(bookAction2);

        }
        return actions;
    }

    public ExtensionResult sop_validasiSOP(ExtensionRequest extensionRequest) {
        log.debug("sop_validasiSOP() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        final String jenisdokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "jenisdokumen");
        final String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        final String divisi = sdkUtil.getEasyMapValueByName(extensionRequest, "divisi");
        String sopbyUser = sdkUtil.getEasyMapValueByName(extensionRequest, "sop");

        if (sopbyUser.equalsIgnoreCase("dokumen lain")) {
            String title = "Silakan ketik Dokumen " + jenisdokumen.toUpperCase() + " yang anda inginkan. Atau klik dibawah ini untuk melihat list Dokumen " + jenisdokumen.toUpperCase() + " yang ada di kategori ini.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder(title)
                    .add("View ALL", "view all").build();

            output.put(OUTPUT, quickReplyBuilder.string());
            clearEntities.put("nomorurut", "1");
            clearEntities.put("index", "0");
            clearEntities.put("namadokumen", "");
            clearEntities.put("konfirmasi_dokumen", "");
            clearEntities.put("konfirmasi_sop", "");
            clearEntities.put("sop", "");
            clearEntities.put("before_final", "");
        } else {
            String namadokumen = sdkUtil.getEasyMapValueByName(extensionRequest, "namadokumen");
            List<String> listNamaDokumen = new ArrayList<>();
            listNamaDokumen = getListJsonSOP.namaDokumenGeneral(company, jenisdokumen, divisi);
            boolean cekAngka = CekNumber(namadokumen);
            if (cekAngka == true) {
                int i = Integer.parseInt(namadokumen) - 1;
                namadokumen = listNamaDokumen.get(i);
            } else {
                int lengDokumen = listNamaDokumen.size();
                for (int i = 0; i < lengDokumen; i++) {
                    String dokumenname = listNamaDokumen.get(i);
                    if (dokumenname.equalsIgnoreCase(namadokumen)) {
                        namadokumen = dokumenname;
                        break;
                    }
                }
            }
            List<SOP> listSopJson = paramJSON.getListSOPFromFileJson(sopJson);
            List<SOP> listByFilter = listSopJson.stream()
                    .filter(sop -> sop.company.equalsIgnoreCase(company)
                    && sop.jenis_dokumen.equalsIgnoreCase(jenisdokumen)
                    && sop.divisi.equalsIgnoreCase(divisi))
                    .collect(Collectors.toList());

            try {
                int lengNamaDokumen = listByFilter.size();
                String linkDoc = "";
                String namaDoc = "";
                for (int i = 0; i < lengNamaDokumen; i++) {
                    SOP sopArray = listByFilter.get(i);
                    namaDoc = sopArray.nama_doc;
                    if (namaDoc.equalsIgnoreCase(namadokumen)) {
                        linkDoc = sopArray.link;
                        break;
                    }
                }
                String title = "- Klik \"Detail\" jika membutuhkan " + jenisdokumen.toUpperCase() + " lebih detail\n"
                        + "- Klik \"Dokumen Lain\" untuk melihat Dokumen " + jenisdokumen + " lainnya\n"
                        + "- Klik \"Menu\" untuk melihat Menu yang ada.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("")
                        .add("Detail", linkDoc).add("Dokumen Lain", "dokumen lain").add("Menu", "menu utama").build();

                output.put(OUTPUT, title + SPLIT + quickReplyBuilder.string());
                clearEntities.put("sop", "");
                clearEntities.put("before_final", "");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }
}
