/**
 * Copyright (c) 2014 InMotion Innovation Technology. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of InMotion
 * Innovation Technology. ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall only be
 * used in accordance with the terms of the license agreement entered into with
 * IMI; other than in accordance with the written permission of IMI. <BR>
 *
 *
 */
package com.imi.dolphin.sdkwebservice.service;

import com.imi.dolphin.sdkwebservice.model.ExtensionRequest;
import com.imi.dolphin.sdkwebservice.model.ExtensionResult;
import com.itextpdf.text.DocumentException;
import java.io.IOException;

/**
 *
 * @author reja
 *
 */
public interface IService {

    ExtensionResult getSrnResult(ExtensionRequest extensionRequest);

    ExtensionResult getCustomerInfo(ExtensionRequest extensionRequest);

    ExtensionResult modifyCustomerName(ExtensionRequest extensionRequest);

    ExtensionResult getProductInfo(ExtensionRequest extensionRequest);

    ExtensionResult getMessageBody(ExtensionRequest extensionRequest);

    ExtensionResult getQuickReplies(ExtensionRequest extensionRequest);

    ExtensionResult getForms(ExtensionRequest extensionRequest);

    ExtensionResult getButtons(ExtensionRequest extensionRequest);

    ExtensionResult getCarousel(ExtensionRequest extensionRequest);

    ExtensionResult doTransferToAgent(ExtensionRequest extensionRequest);

    ExtensionResult doSendLocation(ExtensionRequest extensionRequest);

    ExtensionResult getImage(ExtensionRequest extensionRequest);

    ExtensionResult getSplitConversation(ExtensionRequest extensionRequest);

    ExtensionResult doSendMail(ExtensionRequest extensionRequest);

    ExtensionResult getDolphinResponse(ExtensionRequest extensionRequest);

    ExtensionResult getPingResponse(ExtensionRequest extensionRequest);
    // ====================================================================== //
//
//    ExtensionResult getUserLdap(ExtensionRequest extensionRequest);
//
//    ExtensionResult validasiOtp(ExtensionRequest extensionRequest);
//
//    ExtensionResult menuUtama(ExtensionRequest extensionRequest);
//
//    ExtensionResult menuUtamaGeneral(ExtensionRequest extensionRequest);
//
//    ExtensionResult tanyaReportName(ExtensionRequest extensionRequest);
//
//    ExtensionResult tanyaKategori(ExtensionRequest extensionRequest);
//
//    ExtensionResult tanyaGroup(ExtensionRequest extensionRequest);
//
//    ExtensionResult konfirmasiGroup(ExtensionRequest extensionRequest);
//
//    ExtensionResult validasiJenisGroup(ExtensionRequest extensionRequest);
//
//    ExtensionResult getReport(ExtensionRequest extensionRequest);
//
//    ExtensionResult validasiReport(ExtensionRequest extensionRequest);
//
//    ExtensionResult getSOP(ExtensionRequest extensionRequest);
//
//    ExtensionResult pertanyaanPertama(ExtensionRequest extensionRequest);

}
