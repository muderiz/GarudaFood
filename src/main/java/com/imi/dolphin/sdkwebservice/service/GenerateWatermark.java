/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.service;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class GenerateWatermark {

    private static final Logger log = LogManager.getLogger(GenerateWatermark.class);

    public String WatermarkPDF(String text, String pathPdfFrom) {
        log.debug("WatermarkPDF extension request : {} ", pathPdfFrom);
        String pathPdfTo = "";
        try {// read existing pdf
            PdfReader reader = new PdfReader(pathPdfFrom);
//        String pathPdfTo =  <baseUrl>+"/sop/"+<variable_namefile>;
            pathPdfTo = "./percobaan/watermarked-existing-pdf-BYSDK.pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathPdfTo));

            // text watermark
            Font FONT = new Font(Font.FontFamily.HELVETICA, 34, Font.BOLD, new GrayColor(0.5f));
            Phrase p1 = new Phrase(text, FONT);
            Phrase p2 = new Phrase("DOKUMEN TIDAK TERKENDALI", FONT);

            // image watermark
//            Image img = Image.getInstance(getResource("/memorynotfound-logo.jpg"));
//            float w = img.getScaledWidth();
//            float h = img.getScaledHeight();
            // properties
            PdfContentByte over;
            Rectangle pagesize;
            float x, y;

            // loop over every page
            int n = reader.getNumberOfPages();
            for (int i = 2; i <= n; i++) {

                // get page size and position
                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = stamper.getOverContent(i);
                over.saveState();

                // set transparency 
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.2f);
                over.setGState(state);

                // add watermark text and image
                // Line 1
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p1, x - 10, y + 10, 45);
                // Line 2
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p2, x + 20, y - 10, 45);

                over.restoreState();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            log.debug("WatermarkPDFEx() : {}", e.getMessage());
        }
        return pathPdfTo;
    }

    public String WatermarkImage(String text, File pathImageFrom) {
        String pathPdfTo = "";
        try {
            BufferedImage image = ImageIO.read(pathImageFrom);
            String text2 = "DOKUMEN TIDAK TERKENDALI";
            String type = "jpg";
            // determine image type and handle correct transparency
            int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

            // initializes necessary graphic properties
            Graphics2D w = (Graphics2D) watermarked.getGraphics();
            w.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            w.setComposite(alphaChannel);
            w.setColor(Color.GRAY);
            w.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 60));
            FontMetrics fontMetrics = w.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(text, w);
            Rectangle2D rect2 = fontMetrics.getStringBounds(text2, w);

            AffineTransform orig = w.getTransform();
            float width = image.getWidth() / 2;
            float height = image.getHeight() / 2;
            orig.rotate(Math.toRadians(-30), width, height);
            w.setTransform(orig);

            // calculate center of the image
            // Text 1
            float centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
            float centerY = image.getHeight() / 2;
            w.drawString(text, centerX, centerY - 30);

            // Text 2
            float centerX2 = (image.getWidth() - (int) rect2.getWidth()) / 2;
            float centerY2 = image.getHeight() / 2;
            w.drawString(text2, centerX2, centerY2 + 30);

//      String pathPdfTo =  <baseUrl>+"/report/"+<variable_namefile>;
            pathPdfTo = "./percobaan/GarudaFood-Watermark-BySdk.jpg";
            File output = new File(pathPdfTo);
            ImageIO.write(watermarked, type, output);
            w.dispose();
        } catch (IOException e) {
            log.debug("WatermarkImageEx() : {}", e.getMessage());
        }

        return pathPdfTo;

    }
}
