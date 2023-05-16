package BLL.util;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.TextsAndImagesOnReport;
import com.itextpdf.io.font.constants.StandardFontFamilies;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class PDFGenerator {
    public void generateReport(Report report, Case selectedCase, Customer customer, List<TextsAndImagesOnReport> textsAndImagesOnReportList) {
        try {
            PdfFont font = PdfFontFactory.createFont(StandardFontFamilies.COURIER);
            String destination = "data/PDFs/"+report.getReportName()+".pdf";
            PdfWriter writer = new PdfWriter(destination);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            String imageFile = "data/Images/WUAV Logo.png";
            try {
                ImageData data = ImageDataFactory.create(imageFile);
                com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(data);
                image.setFixedPosition(450,750);
                image.scaleAbsolute(120,79);
                document.add(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load an image, following error occurred:\n" + e, ButtonType.CANCEL);
                alert.showAndWait();
            }
            String para1 = "Costumer name: " + customer.getCustomerName() + "\n" + "Address: " + customer.getAddress() + "\n" + "Email: " + customer.getEmail() + "\n" + "Telefon: " + customer.getPhoneNumber() +"\n\n";
            String para2 = "Case name: " + selectedCase.getCaseName() + "\n" + "Case ID: " + selectedCase.getCaseID() + "\n" + "Case created; " + selectedCase.getCreatedDate() + "\n" + "Technician: " + selectedCase.getAssignedTechnician() + "\n" + "ContactPerson: " + selectedCase.getContactPerson() + "\n" + "Report description: " + report.getReportDescription();

            Paragraph paragraph1 = new Paragraph(para1);
            paragraph1.setFontSize(10);
            paragraph1.setFont(font);
            paragraph1.setFixedPosition(1,270,735,200);
            document.add(paragraph1);

            Paragraph paragraph2 = new Paragraph(para2);
            paragraph2.setFontSize(10);
            paragraph2.setFont(font);
            paragraph2.setFixedPosition(1,20, 710,200);
            document.add(paragraph2);

            String paraBlank = "\n\n\n\n\n\n\n";
            Paragraph blank = new Paragraph(paraBlank);
            document.add(blank);

            for (TextsAndImagesOnReport textsAndImagesOnReport:textsAndImagesOnReportList) {
                if(textsAndImagesOnReport.getTextOrImage().equals("Text")){
                    String para3 = textsAndImagesOnReport.getText();
                    Paragraph paragraph3 = new Paragraph(para3);
                    paragraph3.setFontSize(12);
                    paragraph3.setFont(font);
                    document.add(paragraph3);

                }
                else {
                    ImageData imageData = ImageDataFactory.create(textsAndImagesOnReport.getImageData());
                    Image image = new Image(imageData);
                    document.add(image);
                    if(!textsAndImagesOnReport.getImageComment().equals(null)){
                        String imageComment = textsAndImagesOnReport.getImageComment()+"\n";
                        Paragraph paragraphComment = new Paragraph(imageComment);
                        paragraphComment.setFontSize(10);
                        paragraphComment.setFont(PdfFontFactory.createFont(StandardFontFamilies.COURIER));
                        document.add(paragraphComment);

                    }
                }
            }

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



