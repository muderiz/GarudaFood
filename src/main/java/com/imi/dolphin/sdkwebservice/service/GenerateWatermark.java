/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.service;

import com.imi.dolphin.sdkwebservice.property.AppProperties;
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
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deka
 */
@Service
public class GenerateWatermark {

    private static final Logger log = LogManager.getLogger(GenerateWatermark.class);
    private final String pathdir = System.getProperty("user.dir");

    public static final String UNDERLINE = "_";

    @Autowired
    AppProperties appProperties;

    public String WatermarkPDF(String text, String pathPdfFrom) {
        log.debug("WatermarkPDF extension request : {} ", pathPdfFrom);
        String pathPdfTo = "";
        try {// read existing pdf
            PdfReader reader = new PdfReader(pathPdfFrom);
//        String pathPdfTo =  <baseUrl>+"/sop/"+<variable_namefile>;
            pathPdfTo = "./percobaan/watermarked-existing-pdf-BYSDK.pdf";
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathPdfTo));

            // text watermark
            Font FONT = new Font(Font.FontFamily.HELVETICA, 50, Font.BOLD, new GrayColor(0.5f));
            Phrase p1 = new Phrase(text, FONT);
            Phrase p2 = new Phrase("DOKUMEN TIDAK TERKENDALI", FONT);

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

    public String WatermarkImageReport(String text, URL pathImageFrom) {
        String pathPdfTo = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = new Date();
        String datenow = formatter.format(date);
        text = text.toUpperCase();
        try {
            BufferedImage image = ImageIO.read(pathImageFrom);
            String[] spliturlreport = pathImageFrom.toString().split("/GeneratedFiles/");
            String reportname = spliturlreport[1];
            String text2 = datenow;
            String type = "jpg";
            // determine image type and handle correct transparency
            int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

            // initializes necessary graphic properties
            // TEXT 1
            Graphics2D w = (Graphics2D) watermarked.getGraphics();
            w.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
            w.setComposite(alphaChannel);
            w.setColor(Color.GRAY);
            w.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 120));
            FontMetrics fontMetrics = w.getFontMetrics();

            // TEXT 2
            Graphics2D w2 = (Graphics2D) watermarked.getGraphics();
            w2.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
            w2.setComposite(alphaChannel2);
            w2.setColor(Color.black);
            w2.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 40));
            FontMetrics fontMetrics2 = w2.getFontMetrics();

            Rectangle2D rect = fontMetrics.getStringBounds(text, w);
            Rectangle2D rect2 = fontMetrics2.getStringBounds(text2, w2);

            // Text 1
            AffineTransform orig = w.getTransform();
            float width = image.getWidth() / 2;
            float height = image.getHeight() / 2;
            orig.rotate(Math.toRadians(-30), width, height);
            w.setTransform(orig);

            // Text 2
            AffineTransform orig2 = w2.getTransform();
            float width2 = image.getWidth() / 2;
            float height2 = image.getHeight() / 2;
            orig2.rotate(Math.toRadians(0), width2, height2);
            w2.setTransform(orig2);

            // calculate center of the image
            // Text 1
            float centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
            float centerY = image.getHeight() / 2;
            w.drawString(text, centerX, centerY - 35);

            // Text 2
//            float centerX2 = (image.getWidth() - (int) rect2.getWidth()) / 2;
            float centerX2 = image.getWidth() / 12;
            float centerY2 = image.getHeight() / 36;
//            float centerY2 = image.getHeight();
            w2.drawString(text2, centerX2, centerY2);

            String pathImageGenerate = appProperties.getGARUDAFOOD_PATH_GENERATEDFILES() + appProperties.getGARUDAFOOD_WATERMARK_REPORT() + reportname;

            ImageIO.write(watermarked, type, new File(pathImageGenerate));
            pathPdfTo = appProperties.getGARUDAFOOD_URL_GENERATEDFILES() + appProperties.getGARUDAFOOD_WATERMARK_REPORT() + reportname;
            image.flush();
            w.dispose();
            w2.dispose();
        } catch (IOException e) {
            log.debug("WatermarkImageEx() : {}", e.getMessage());
        }

        return pathPdfTo;

    }

    public String WatermarkImageSOP(String text1, URL pathImageFrom, String sopname) {
        String pathPdfTo = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = new Date();
        String datenow = formatter.format(date);
        text1 = text1.toUpperCase();
        try {
            BufferedImage image = ImageIO.read(pathImageFrom);
            String text = "DOKUMEN TIDAK TERKENDALI";
            String text2 = datenow;
            String type = "jpg";
            // determine image type and handle correct transparency
            int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

            // initializes necessary graphic properties
            Graphics2D w = (Graphics2D) watermarked.getGraphics();
            w.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
            w.setComposite(alphaChannel);
            w.setColor(Color.GRAY);
            w.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 60));
            FontMetrics fontMetrics = w.getFontMetrics();

            // TEXT 2
            Graphics2D w2 = (Graphics2D) watermarked.getGraphics();
            w2.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            w2.setComposite(alphaChannel2);
            w2.setColor(Color.BLACK);
            w2.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 15));
            FontMetrics fontMetrics2 = w2.getFontMetrics();

            Rectangle2D rect = fontMetrics.getStringBounds(text, w);
            Rectangle2D rect2 = fontMetrics.getStringBounds(text1, w);

            AffineTransform orig = w.getTransform();
            float width = image.getWidth() / 2;
            float height = image.getHeight() / 2;
            orig.rotate(Math.toRadians(-30), width, height);
            w.setTransform(orig);

            // Text 2
            AffineTransform orig2 = w2.getTransform();
            float width2 = image.getWidth() / 2;
            float height2 = image.getHeight() / 2;
            orig2.rotate(Math.toRadians(0), width2, height2);
            w2.setTransform(orig2);

            // calculate center of the image
            // Text 1
            float centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
            float centerY = image.getHeight() / 2;
            w.drawString(text, centerX, centerY - 30);
            // Text 1.2
            float centerX1 = (image.getWidth() - (int) rect2.getWidth()) / 2;
            float centerY1 = image.getHeight() / 2;
            w.drawString(text1, centerX1, centerY1 + 30);

            // Text 2
            float centerX2 = image.getWidth() / -12;
            float centerY2 = image.getHeight() / -38;
            w2.drawString(text2, centerX2, centerY2);

            String pathImageGenerate = appProperties.getGARUDAFOOD_PATH_GENERATEDFILES() + appProperties.getGARUDAFOOD_WATERMARK_SOP() + sopname;

            ImageIO.write(watermarked, type, new File(pathImageGenerate));
            pathPdfTo = appProperties.getGARUDAFOOD_URL_GENERATEDFILES() + appProperties.getGARUDAFOOD_WATERMARK_SOP() + sopname;
            image.flush();
            w.dispose();
            w2.dispose();
        } catch (IOException e) {
            log.debug("WatermarkImageEx() : {}", e.getMessage());
        }

        return pathPdfTo;

    }
}
