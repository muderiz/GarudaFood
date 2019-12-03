/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.serviceReport;

import com.google.gson.Gson;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.EasyParam;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.InfoUser;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.LoopParam;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterDepartment;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.MasterGroupProduct;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Product;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportName;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.ReportRequest;
import com.imi.dolphin.sdkwebservice.GarudafoodModel.Role;
import com.imi.dolphin.sdkwebservice.builder.DocumentBuilder;
import com.imi.dolphin.sdkwebservice.builder.QuickReplyBuilder;
import com.imi.dolphin.sdkwebservice.model.ButtonTemplate;
import com.imi.dolphin.sdkwebservice.model.Contact;
import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.imi.dolphin.sdkwebservice.model.UserToken;
import com.imi.dolphin.sdkwebservice.param.ParamJSONReport;
import com.imi.dolphin.sdkwebservice.property.AppProperties;
import com.imi.dolphin.sdkwebservice.service.AuthService;
import com.imi.dolphin.sdkwebservice.service.GenerateWatermark;
import com.imi.dolphin.sdkwebservice.service.IDolphinService;
import com.imi.dolphin.sdkwebservice.service.IMailService;
import com.imi.dolphin.sdkwebservice.util.OkHttpUtil;
import com.imi.dolphin.sdkwebservice.util.SdkUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class ServiceImpReportBackup {

    private static final Logger log = LogManager.getLogger(ServiceImpReport.class);

    private static final String OUTPUT = "output";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SPLIT = "&split&";
    private final String pathdir = System.getProperty("user.dir");
    private UserToken userToken;
    private static final String roleJson = "fileJson/report/role.json";
    private static final String reportnameJson = "fileJson/report/report_name.json";
    private static final String departmentJson = "fileJson/report/masterdepartment.json";
    private static final String grouproductJson = "fileJson/report/master_group_product.json";
    private static final String productJson = "fileJson/report/product.json";
    private static final String groupJson = "fileJson/report/group.json";
    private static final String skuJson = "fileJson/report/sku.json";
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
    private ParamJSONReport paramJSON;

    // Start Method for Each Report //
    public ExtensionResult pertanyaanPertama(ExtensionRequest extensionRequest) {
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder quickReply;

        userToken = svcDolphinService.getUserToken(userToken);
        String contactId = extensionRequest.getIntent().getTicket().getContactId();
        Contact contact = svcDolphinService.getCustomer(userToken, contactId);
        log.debug("getDolphinResponse() extension request: {} user token: {}", extensionRequest, new Gson().toJson(userToken));
        log.debug("getDolphinResponse() extension request: {} contact id: {}", extensionRequest, contactId);
        log.debug("getDolphinResponse() extension request: {} Contact: {}", extensionRequest, new Gson().toJson(contact));
        // ============== Get AdditionalField ============ //
        try {
            String b = contact.getAdditionalField().get(0);

            if (!b.equalsIgnoreCase("")) {
                InfoUser dataInfoUser = new Gson().fromJson(b, InfoUser.class);
                String usernameUser = dataInfoUser.getAccountName();

//        String usernameUser = sdkUtil.getEasyMapValueByName(extensionRequest, "username");
                clearEntities.put("username", usernameUser);

                List<Role> listRole = paramJSON.getListRolefromFileJson(roleJson);
                List<MasterDepartment> listDepartment = paramJSON.getListDepartmentfromFileJson(departmentJson);
                List<ReportName> listReportName = paramJSON.getListReportNamefromFileJson(reportnameJson);

                String title = "";
                String company = "";
                String division = "";
                String department = "";
                int lengList = listRole.size();
                for (int i = 0; i < lengList; i++) {
                    Role roleArray = listRole.get(i);
                    String roleUsername = roleArray.username;
                    if (roleUsername.equalsIgnoreCase(usernameUser)) {
                        title = roleArray.title;
                        company = roleArray.company;
                        division = roleArray.division;
                        department = roleArray.department;
                        break;
                    } else {
                        title = "management";
                        company = "GPPJ";
                        division = "SCM";
                        department = "All";
                    }
                }

                StringBuilder sb = new StringBuilder();
                String urutan = "";
                final String newdepartment = department;
                String dialog1 = "Baiklah, {first_name} sudah berada di menu report";

                switch (title.toLowerCase()) {
                    case "top management":
//                String ContCompany = "";
                        sb.append("{first_name} ingin melihat report dari Company apa?\n")
                                .append("1. GPPJ");
                        clearEntities.put("role", title);
                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString());

                        break;
                    case "management":
                        sb.append("{first_name} ingin melihat report dari Departemen apa?\n");
//                        .append("1. E2E");
//                        .append("2. Inbound")
//                        .append("3. Outbound");
//                        String dialog = "{first_name} ingin melihat report dari Departemen apa?";
//                        quickReply = new StringBuilder();
//                        quickReply.append(QUICK_REPLY_SYNTAX);
//                        quickReply.append(dialog).append(COMMA);
                        int k = 1;
                        int lengDepartment = listDepartment.size();
                        for (int j = 0; j < lengDepartment; j++) {
                            MasterDepartment departmentArray = listDepartment.get(j);
                            String departmentName = departmentArray.department;
                            urutan = k + ". " + departmentName + "\n";
                            sb.append(urutan);
//                            quickReply.append(departmentName).append("@===@").append(departmentName).append(COMMA);
                            k++;
                        }
//                        quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
//                        quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);

                        clearEntities.put("role", title);
                        clearEntities.put("company", company);
                        clearEntities.put("divisi", division);
                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString());

                        break;
                    case "staff":
//                        sb.append("{custormer_name} ingin melihat report apa?\n");
//                sb.append("1. daily production v sales \n\n");
                        String dialog = "{first_name} ingin melihat Report apa?";

                        quickReply = new StringBuilder();
                        quickReply.append(QUICK_REPLY_SYNTAX);
                        quickReply.append(dialog).append(COMMA);
                        List<ReportName> listByDepartment = listReportName.stream()
                                .filter(reportname -> reportname.department.equalsIgnoreCase(newdepartment))
                                .collect(Collectors.toList());
                        int h = 1;
                        int lengReportName = listByDepartment.size();
                        for (int j = 0; j < lengReportName; j++) {
                            ReportName reportnameArray = listByDepartment.get(j);
                            String reportName = reportnameArray.report_name;
//                            urutan = h + ". " + reportName + "\n";
//                            sb.append(urutan);
                            quickReply.append(reportName).append("@===@").append(reportName).append(COMMA);
                            h++;
                        }
                        quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
                        quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
//                        sb.append("\nSilakan ketik nomor report yang anda inginkan.");
                        clearEntities.put("role", title);
                        clearEntities.put("company", company);
                        clearEntities.put("divisi", division);
                        clearEntities.put("departement", department);
                        output.put(OUTPUT, quickReply.toString());

                        break;
                    default:
                        sb.append("{first_name} ingin melihat Report apa?\n");
//                sb.append("1. daily production v sales \n\n");
                        List<ReportName> listByDepartment2 = listReportName.stream()
                                .filter(reportname -> reportname.department.equalsIgnoreCase(newdepartment))
                                .collect(Collectors.toList());
                        h = 1;
                        int lengReportName2 = listByDepartment2.size();
                        for (int j = 0; j < lengReportName2; j++) {
                            ReportName reportnameArray = listByDepartment2.get(j);
                            String reportName = reportnameArray.report_name;
                            urutan = h + ". " + reportName + "\n";
                            sb.append(urutan);
                            h++;
                        }
                        sb.append("\nSilakan ketik nama report yang anda inginkan.");
                        clearEntities.put("role", title);
                        clearEntities.put("company", company);
                        clearEntities.put("divisi", division);
                        clearEntities.put("departement", department);
                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString());

                }

            } else {
                String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                        .add("Verifikasi Akun", "verifikasi").build();
                output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
                clearEntities.put("role", "verifikasi");
                clearEntities.put("username", "verifikasi");
                clearEntities.put("company", "verifikasi");
                clearEntities.put("divisi", "verifikasi");
                clearEntities.put("departement", "verifikasi");
                clearEntities.put("nama_report", "verifikasi");
                clearEntities.put("kategorigroup", "verifikasi");
                clearEntities.put("group", "verifikasi");
                clearEntities.put("kodegroup", "verifikasi");
                clearEntities.put("konfirmasi_group", "verifikasi");
                clearEntities.put("index_report", "verifikasi");

            }
        } catch (Exception ex) {
            String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini untuk melakukan konfirmasi.")
                    .add("Verifikasi Akun", "verifikasi").build();
            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
            clearEntities.put("role", "verifikasi");
            clearEntities.put("username", "verifikasi");
            clearEntities.put("company", "verifikasi");
            clearEntities.put("divisi", "verifikasi");
            clearEntities.put("departement", "verifikasi");
            clearEntities.put("nama_report", "verifikasi");
            clearEntities.put("kategorigroup", "verifikasi");
            clearEntities.put("group", "verifikasi");
            clearEntities.put("kodegroup", "verifikasi");
            clearEntities.put("konfirmasi_group", "verifikasi");
            clearEntities.put("index_report", "verifikasi");

        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        return extensionResult;
    }

    public ExtensionResult tanyaReportName(ExtensionRequest extensionRequest) {
        log.debug("tanyaReportName() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        String role = sdkUtil.getEasyMapValueByName(extensionRequest, "role");
        String departement = sdkUtil.getEasyMapValueByName(extensionRequest, "departement");
        System.out.println(departement);
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        StringBuilder quickReply;
        List<ReportName> listReportName = paramJSON.getListReportNamefromFileJson(reportnameJson);
        List<MasterDepartment> listDepartment = paramJSON.getListDepartmentfromFileJson(departmentJson);
        int lengList = listReportName.size();
        String urutan;
        String valueUrutan;
        String dialogmanagement;
        int indexDepartment = Integer.parseInt(departement) - 1;

        if (departement.equalsIgnoreCase("1") || departement.equalsIgnoreCase("e2e")) {
            if (role.equalsIgnoreCase("management")) {
                if (departement.equalsIgnoreCase("1")) {
                    departement = "e2e";
                } else if (departement.equalsIgnoreCase("2")) {
                    departement = "inbound";

                } else if (departement.equalsIgnoreCase("3")) {
                    departement = "outbound";
                }
                String dialog = "{first_name} ingin melihat Report apa?";

                quickReply = new StringBuilder();
                quickReply.append(QUICK_REPLY_SYNTAX);
                quickReply.append(dialog).append(COMMA);
                int j = 1;
                for (int i = 0; i < lengList; i++) {
                    ReportName departementArray = listReportName.get(i);
                    String departemenName = departementArray.department;
                    String reportName = departementArray.report_name;
                    if (departemenName.equalsIgnoreCase(departement)) {
                        urutan = j + ". ";
                        valueUrutan = reportName + "\n";

                        sb.append(urutan).append(valueUrutan);
                        quickReply.append(reportName).append("@===@").append(reportName).append(COMMA);

                        j++;
                    }
                }
                quickReply.replace(quickReply.toString().length() - 1, quickReply.toString().length(), "");
                quickReply.append(QUICK_REPLY_SYNTAX_SUFFIX);
                String dialog2 = "Berikut report sesuai Departemen Anda\n" + sb.toString();
                output.put(OUTPUT, dialog2 + SPLIT + quickReply.toString());

            } else if (role.equalsIgnoreCase("staff")) {
                int j = 1;
                for (int i = 0; i < lengList; i++) {
                    ReportName departementArray = listReportName.get(i);
                    String departemenName = departementArray.department;
                    String reportName = departementArray.report_name;
                    if (departemenName.equalsIgnoreCase(departement)) {
                        urutan = j + ". ";
                        valueUrutan = reportName + "\n";
                        sb.append(urutan).append(valueUrutan);
                        j++;
                    }
                }
                String dialog1 = "{first_name} ingin melihat Report apa?\n" + sb.toString();
                String dialog2 = "Silakan ketik nama report yang anda inginkan";
                output.put(OUTPUT, dialog1 + SPLIT + dialog2);
            }

            clearEntities.put("departement", "e2e");
        } else {
            sb.append("Mohon maaf untuk saat ini Departemen tersebut belum tersedia.")
                    .append("{first_name} ingin melihat report dari Departemen apa?\n")
                    .append("1. E2E");
//                        .append("2. Inbound")
//                        .append("3. Outbound");

            clearEntities.put("departement", "");

            output.put(OUTPUT, sb.toString());
        }

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult tanyaKategori(ExtensionRequest extensionRequest) {
        log.debug("tanyaKategori() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String role = sdkUtil.getEasyMapValueByName(extensionRequest, "role");
        String company = sdkUtil.getEasyMapValueByName(extensionRequest, "company");
        String departement = sdkUtil.getEasyMapValueByName(extensionRequest, "departement");
        String namaReport = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");

        if (namaReport.equalsIgnoreCase("1") || namaReport.equalsIgnoreCase("daily production v sales")) {
            if (namaReport.equalsIgnoreCase("1")) {
                namaReport = "daily production v sales";
//            case "2":
//                namaReport = "daily";
//            case "3":
//                namaReport = "outbound";
//                clearEntities.put("nama_report", "daily production v sales");

            }
            List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
//            List<Product> listGPPJ = listProduct.stream()
//                    .filter(product -> product.principal.equalsIgnoreCase(company))
//                    .collect(Collectors.toList());
            String urutan;
            String valueUrutan;
            int lengList = listProduct.size();
            String addGroup = "";
            int j = 1;

            for (int i = 0; i < lengList; i++) {
                Product productArray = listProduct.get(i);
                String groupProduct = productArray.group_category;
                if (!addGroup.equalsIgnoreCase(groupProduct)) {
                    addGroup = groupProduct;
                    urutan = j + ". ";
                    valueUrutan = groupProduct + "\n";
                    sb.append(urutan).append(valueUrutan);
                    j++;
                }
            }

            String dialog1 = "Ingin melihat group kategori apa?\n" + sb.toString();
            String dialog2 = "Silakan ketik group kategori yang anda inginkan.";
            output.put(OUTPUT, dialog1 + SPLIT + dialog2);
            clearEntities.put("nama_report", namaReport);
        } else {
            sb.append("Mohon maaf untuk saat ini Report tersebut belum tersedia.")
                    .append("{customer_name} ingin melihat Report apa?\n")
                    .append("1. Daily Production v Sales");
//                        .append("2. Inbound")
//                        .append("3. Outbound");

            clearEntities.put("nama_report", "");

            output.put(OUTPUT, sb.toString());
        }
        extensionResult.setEntities(clearEntities);

        extensionResult.setValue(output);
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult tanyaGroup(ExtensionRequest extensionRequest) {
        log.debug("tanyaGroup() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        StringBuilder sb = new StringBuilder();

        sb.append("Silakan ketikan kode SKU/Product yang anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.");
        output.put(OUTPUT, sb.toString());
        clearEntities.put("group", "group");

        extensionResult.setValue(output);
        extensionResult.setEntities(clearEntities);

        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setSuccess(true);
        extensionResult.setNext(true);
        return extensionResult;
    }

    public ExtensionResult konfirmasiGroup(ExtensionRequest extensionRequest) {
        log.debug("konfirmasiGroup() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        Map<String, String> output = new HashMap<>();
        ExtensionResult extensionResult = new ExtensionResult();
        Map<String, String> clearEntities = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        String kategoriGroup = sdkUtil.getEasyMapValueByName(extensionRequest, "kategorigroup");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "kodegroup");
        sku = sku.toLowerCase();
        String statussku = "";
        String NewGroup = "";

        List<MasterGroupProduct> listGroupProduct = paramJSON.getListGroupProductfromFileJson(grouproductJson);
        int lengListGroup = listGroupProduct.size();
        for (int i = 0; i < lengListGroup; i++) {
            MasterGroupProduct groupproductArray = listGroupProduct.get(i);
            String groupKategori = groupproductArray.group_category;
            String groupId = groupproductArray.id_group;
            if (groupId.equalsIgnoreCase(kategoriGroup)) {
                NewGroup = groupKategori;
                break;
            } else {
                NewGroup = kategoriGroup;
            }
        }

        clearEntities.put("kategorigroup", NewGroup);

        if (sku.equalsIgnoreCase("all")) {
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Apakah {customer_name} ingin report dalam bentuk summary?")
                    .add("Ya", "Yes").add("Tidak", "NO").build();

            output.put(OUTPUT, quickReplyBuilder.string());
        } else {
            final String kategoriGroup2 = NewGroup;
            System.out.println("Group : " + kategoriGroup2);

            List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
            List<Product> listByGroup = listProduct.stream()
                    .filter(product -> product.group_category.equalsIgnoreCase(kategoriGroup2))
                    .collect(Collectors.toList());
            String valueUrutan = "";
            int lengList = listByGroup.size();
            for (int i = 0; i < lengList; i++) {
                Product productArray = listByGroup.get(i);
                String skuProduct = productArray.sku;
                skuProduct = skuProduct.toLowerCase();
                if (skuProduct.equalsIgnoreCase(sku)) {
                    statussku = "tepat";
                    break;
                } else if (skuProduct.contains(sku)) {
                    statussku = "tidak";
                    valueUrutan = skuProduct;
                    sb.append(valueUrutan).append("\n");
                }
            }
            if (statussku.equalsIgnoreCase("tepat")) {
                clearEntities.put("konfirmasi_group", "yes");
            } else {
                clearEntities.put("kodegroup", "");
                if (sb.toString().equalsIgnoreCase("")) {
                    String dialog1 = "Maaf, tidak dapat menemukan SKU tersebut.";
                    String dialog2 = "Silakan ketik kembali kode SKU/Product yang anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.";
                    output.put(OUTPUT, dialog1 + SPLIT + dialog2);

                } else {
                    String dialog1 = "Apakah Kode SKU berikut yang Anda maksud?\n" + sb.toString();
                    String dialog2 = "Silakan ketik SKU yang Anda inginkan.";
                    output.put(OUTPUT, dialog1 + SPLIT + dialog2);
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

    /**
     *
     * @param extensionRequest
     * @return
     */
    public ExtensionResult getReport(ExtensionRequest extensionRequest) {
        log.debug("getReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));
        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setNext(true);
        extensionResult.setSuccess(true);

        String username = sdkUtil.getEasyMapValueByName(extensionRequest, "username");
        String namaReport = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");
        String kategorigroup = sdkUtil.getEasyMapValueByName(extensionRequest, "kategorigroup");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "kodegroup");
        String konfirmasi_group = sdkUtil.getEasyMapValueByName(extensionRequest, "konfirmasi_group");

        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        ReportRequest reportRequest = new ReportRequest();
        List<EasyParam> easyparam = new ArrayList<>();
        List<LoopParam> loopparam = new ArrayList<>();
        String text = username;
        String parameterKey = "";
        String parameterValue = "";
        String reportname = "";
        String summary = "";
        int lengList = 0;
        if (namaReport.equalsIgnoreCase("daily production v sales")) {
            parameterKey = "SKU";
            reportname = "r1_m";
        }
        if (konfirmasi_group.equalsIgnoreCase("Yes")) {
            summary = "yes";
        } else {
            summary = "no";
        }
        if (konfirmasi_group.equalsIgnoreCase("verifikasi")) {
            String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini atau ketik \"verifikasi\" untuk melakukan konfirmasi.")
                    .add("Verifikasi Akun", "verifikasi").build();

            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());

        } else {
            int index = 0;
            if (summary.equalsIgnoreCase("yes")) {
                if (sku.equalsIgnoreCase("all")) {
                    List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
                    List<Product> listByGroup = listProduct.stream()
                            .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                            .collect(Collectors.toList());

                    lengList = listByGroup.size();
                    for (int i = index; i < lengList;) {
                        Product productArray = listByGroup.get(i);
                        String skuProduct = productArray.sku;
                        if (i < 1) {
                            parameterValue = skuProduct;
                        } else {
                            parameterValue = parameterValue + "|" + skuProduct;
                        }
                        i++;
                        index = i;
                    }
                } else {
                    parameterValue = sku.toUpperCase();

                }
                EasyParam easyParam = new EasyParam();
                easyParam.setSzKey(parameterKey);
                easyParam.setSzValue(parameterValue);
                easyparam.add(easyParam);
            } else {
                if (sku.equalsIgnoreCase("all")) {
                    List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
                    List<Product> listByGroup = listProduct.stream()
                            .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                            .collect(Collectors.toList());

                    lengList = listByGroup.size();
                    int newleng;

                    if (lengList >= 5) {
                        newleng = 5;
                    } else {
                        newleng = lengList;
                    }
                    for (int i = index; i < newleng;) {
                        Product productArray = listByGroup.get(i);
                        String skuProduct = productArray.sku;
                        LoopParam loopParam = new LoopParam();
                        loopParam.setSzKey(parameterKey);
                        loopParam.setSzValue(skuProduct);

                        loopparam.add(loopParam);
                        i++;
                        index = i;
                    }
                } else {
                    LoopParam loopParam = new LoopParam();
                    parameterValue = sku.toUpperCase();
                    loopParam.setSzKey(parameterKey);
                    loopParam.setSzValue(parameterValue);
                    loopparam.add(loopParam);
                }
            }

            // Set Param
            reportRequest.setSzReportName(reportname);
            reportRequest.setLoopParam(loopparam);
            reportRequest.setSummary(summary);
            reportRequest.setParam(easyparam);
            JSONObject jsonReport = new JSONObject(reportRequest);
            String report = jsonReport.toString();
            String dialog1 = "";

            try {
                OkHttpUtil okHttpUtil = new OkHttpUtil();
                okHttpUtil.init(true);
                String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                System.out.println(report);
                RequestBody body = RequestBody.create(JSON, report);
                Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                Response response = okHttpUtil.getClient().newCall(request).execute();
                JSONObject jsonobj = new JSONObject(response.body().string());

                if (jsonobj.getString("error").equalsIgnoreCase("")) {
                    JSONArray arrayReport = jsonobj.getJSONArray("path");
                    int lengReport = arrayReport.length();

                    for (int i = 0; i < lengReport; i++) {
                        JSONObject jObj = arrayReport.getJSONObject(i);
                        String url = jObj.getString("url");
                        System.out.println(url);
                        URL input = new URL(url);
                        String reportAfterWatermark = generateWatermark.WatermarkImageReport(text, input);
                        System.out.println(reportAfterWatermark);
                        ButtonTemplate image = new ButtonTemplate();
                        image.setPictureLink(reportAfterWatermark);
                        image.setPicturePath(reportAfterWatermark);

                        DocumentBuilder documentBuilder = new DocumentBuilder(image);
                        String btnBuilder = documentBuilder.build();
                        sb.append(btnBuilder).append(SPLIT);

                    }
                    String pagereport = "(" + index + "/" + lengList + ")";
                    if (lengReport > 1) {
                        dialog1 = "Berikut adalah Report yang {first_name} ingin lihat. " + pagereport;
                    } else {
                        dialog1 = "Berikut adalah Report yang {first_name} ingin lihat. ";
                    }
                    if (lengList > 5 && summary.equalsIgnoreCase("no")) {
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    } else {
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    }
                    clearEntities.put("index_report", index + "");

                } else {
                    dialog1 = "Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan ketik \"Menu\" untuk melihat Menu Utama.";
                    output.put(OUTPUT, dialog1);

                }
            } catch (Exception e) {
                log.debug("Response Exception() extension request : {} ", e);
            }
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        log.debug("String Builder getReport() extension request : {} ", new Gson().toJson(output));

        return extensionResult;
    }

    public ExtensionResult validasiReport(ExtensionRequest extensionRequest) {
        log.debug("validasiReport() extension request: {}", new Gson().toJson(extensionRequest, ExtensionRequest.class));

        ExtensionResult extensionResult = new ExtensionResult();
        extensionResult.setAgent(false);
        extensionResult.setRepeat(false);
        extensionResult.setNext(true);
        extensionResult.setSuccess(true);

        String username = sdkUtil.getEasyMapValueByName(extensionRequest, "username");
        String namaReport = sdkUtil.getEasyMapValueByName(extensionRequest, "nama_report");
        String kategorigroup = sdkUtil.getEasyMapValueByName(extensionRequest, "kategorigroup");
        String sku = sdkUtil.getEasyMapValueByName(extensionRequest, "kodegroup");
        String konfirmasi_group = sdkUtil.getEasyMapValueByName(extensionRequest, "konfirmasi_group");
        String index_report = sdkUtil.getEasyMapValueByName(extensionRequest, "index_report");
        String report = sdkUtil.getEasyMapValueByName(extensionRequest, "report");

        Map<String, String> output = new HashMap<>();
        Map<String, String> clearEntities = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        if (konfirmasi_group.equalsIgnoreCase("verifikasi")) {
            String dialog1 = "Mohon maaf akun Anda belum terverifikasi.";
            QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Silakan klik di bawah ini atau ketik \"verifikasi\" untuk melakukan konfirmasi.")
                    .add("Verifikasi Akun", "verifikasi").build();

            output.put(OUTPUT, dialog1 + SPLIT + quickReplyBuilder.string());
            clearEntities.put("report", "");
            clearEntities.put("before_final", "");
        } else if (report.equalsIgnoreCase("next")) {
            ReportRequest reportRequest = new ReportRequest();
            List<EasyParam> easyparam = new ArrayList<>();
            List<LoopParam> loopparam = new ArrayList<>();
            String text = username;
            String parameterKey = "";
            String parameterValue = "";
            String reportname = "";
            String summary = "";
            int lengList = 0;
            if (namaReport.equalsIgnoreCase("daily production v sales")) {
                parameterKey = "SKU";
                reportname = "r1_m";
            }
            if (konfirmasi_group.equalsIgnoreCase("Yes")) {
                summary = "yes";
            } else {
                summary = "no";
            }
            int index = Integer.parseInt(index_report);
            if (summary.equalsIgnoreCase("yes")) {
                if (sku.equalsIgnoreCase("all")) {
                    List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
                    List<Product> listByGroup = listProduct.stream()
                            .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                            .collect(Collectors.toList());

                    lengList = listByGroup.size();
                    for (int i = index; i < lengList;) {
                        Product productArray = listByGroup.get(i);
                        String skuProduct = productArray.sku;
                        if (i < 1) {
                            parameterValue = skuProduct;
                        } else {
                            parameterValue = parameterValue + "|" + skuProduct;
                        }
                        i++;
                        index = i;
                    }
                } else {
                    parameterValue = sku.toUpperCase();

                }
                EasyParam easyParam = new EasyParam();
                easyParam.setSzKey(parameterKey);
                easyParam.setSzValue(parameterValue);
                easyparam.add(easyParam);
            } else {
                if (sku.equalsIgnoreCase("all")) {
                    List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
                    List<Product> listByGroup = listProduct.stream()
                            .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                            .collect(Collectors.toList());

                    lengList = listByGroup.size();
                    int newleng;
                    int addindex = lengList - index;
                    if (addindex >= 5) {
                        addindex = 5;
                        newleng = index + addindex;
                    } else {
                        newleng = lengList;
                    }
                    for (int i = index; i < newleng;) {
                        Product productArray = listByGroup.get(i);
                        String skuProduct = productArray.sku;
                        LoopParam loopParam = new LoopParam();
                        loopParam.setSzKey(parameterKey);
                        loopParam.setSzValue(skuProduct);

                        loopparam.add(loopParam);
                        i++;
                        index = i;
                    }
                } else {
                    LoopParam loopParam = new LoopParam();
                    parameterValue = sku.toUpperCase();
                    loopParam.setSzKey(parameterKey);
                    loopParam.setSzValue(parameterValue);
                    loopparam.add(loopParam);
                }
            }

            // Set Param
            reportRequest.setSzReportName(reportname);
            reportRequest.setLoopParam(loopparam);
            reportRequest.setSummary(summary);
            reportRequest.setParam(easyparam);
            JSONObject jsonReport = new JSONObject(reportRequest);
            String reports = jsonReport.toString();
            String dialog1 = "";

            try {
                OkHttpUtil okHttpUtil = new OkHttpUtil();
                okHttpUtil.init(true);
                String apiReport = appProp.getGARUDAFOOD_API_REPORT();
                System.out.println(reports);
                RequestBody body = RequestBody.create(JSON, reports);
                Request request = new Request.Builder().url(apiReport).post(body).addHeader("Content-Type", "application/json").build();
                Response response = okHttpUtil.getClient().newCall(request).execute();
                JSONObject jsonobj = new JSONObject(response.body().string());

                if (jsonobj.getString("error").equalsIgnoreCase("")) {
                    JSONArray arrayReport = jsonobj.getJSONArray("path");
                    int lengReport = arrayReport.length();

                    for (int i = 0; i < lengReport; i++) {
                        JSONObject jObj = arrayReport.getJSONObject(i);
                        String url = jObj.getString("url");
//                      
                        URL input = new URL(url);
                        String reportAfterWatermark = generateWatermark.WatermarkImageReport(text, input);
                        System.out.println(reportAfterWatermark);
                        ButtonTemplate image = new ButtonTemplate();
                        image.setPictureLink(reportAfterWatermark);
                        image.setPicturePath(reportAfterWatermark);

                        DocumentBuilder documentBuilder = new DocumentBuilder(image);
                        String btnBuilder = documentBuilder.build();
                        sb.append(btnBuilder).append(SPLIT);

                    }
                    String pagereport = "(" + index + "/" + lengList + ")";
                    if (lengReport > 1) {
                        dialog1 = "Berikut adalah Report yang {first_name} ingin lihat. " + pagereport;
                    } else {
                        dialog1 = "Berikut adalah Report yang {first_name} ingin lihat. ";
                    }
                    if (lengList < 5 || lengList == index) {
                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    } else if (lengList > 5 && summary.equalsIgnoreCase("no")) {

                        QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                                .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                        output.put(OUTPUT, dialog1 + SPLIT + sb.toString() + quickReplyBuilder.string());
                    }
                    clearEntities.put("index_report", index + "");
                    clearEntities.put("report", "");
                    clearEntities.put("before_final", "");
                } else {
                    dialog1 = "Maaf. File tidak ditemukan atau sedang terjadi kesalahan. Silakan ketik \"Menu\" untuk melihat Menu Utama.";
                    output.put(OUTPUT, dialog1);

                }
            } catch (Exception e) {
                log.debug("Response Exception() extension request : {} ", e);
            }
        } else if (report.equalsIgnoreCase("ganti sku")) {
            sb.append("Silakan ketikan kode SKU/Product yang anda inginkan. Atau ketik \"All\" untuk semua SKU berdasarkan Group.");
            output.put(OUTPUT, sb.toString());
            clearEntities.put("kodegroup", "");
            clearEntities.put("konfirmasi_group", "");
            clearEntities.put("index_report", "");
            clearEntities.put("report", "");
            clearEntities.put("before_final", "");
        } else {
            int index = Integer.parseInt(index_report);

            List<Product> listProduct = paramJSON.getListProductfromFileJson(productJson);
            List<Product> listByGroup = listProduct.stream()
                    .filter(product -> product.group_category.equalsIgnoreCase(kategorigroup))
                    .collect(Collectors.toList());

            int lengList = listByGroup.size();
            int newleng;
            int addindex = lengList - index;
            if (addindex >= 5) {
                addindex = 5;
                newleng = index + addindex;
            } else {
                newleng = lengList;
            }
            if (newleng < 5 || newleng == index) {
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                        .add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                output.put(OUTPUT, quickReplyBuilder.string());
            } else if (newleng > 5) {
                QuickReplyBuilder quickReplyBuilder = new QuickReplyBuilder.Builder("Klik \"Next\" untuk Report Selanjutnya. Klik \"Ganti SKU\" jika Anda ingin cek SKU lainnya. Atau klik \"Menu\" untuk Menu Utama")
                        .add("Next", "next").add("Ganti SKU", "ganti sku").add("Menu", "menu").build();

                output.put(OUTPUT, quickReplyBuilder.string());
            }

            clearEntities.put("report", "");
            clearEntities.put("before_final", "");
        }
        extensionResult.setEntities(clearEntities);
        extensionResult.setValue(output);
        log.debug("String Builder validasiReport() extension request : {} ", new Gson().toJson(output));

        return extensionResult;
    }
}
