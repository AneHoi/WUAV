package BLL.util;

import BE.Case;
import BE.Customer;
import BE.Report;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

public class PDFGenerator {
    private Document document;
    private Customer customer;
    private Case selectedCase;
    private PdfTextExtractor pdfTextExtractor;

    public void generateReport(Report report, Case selectedCase, Customer customer) throws DocumentException, SQLException {


        try {
            document = new Document(PageSize.A4, 20, 20, 50, 25);
            PdfWriter writer = null;
            writer.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();
            String imageFile = "data/Images/WUAV Logo.png";
            ImageData imgData = ImageDataFactory.create(imageFile);
            Image logoImage = new Image(imgData);
            logoImage.setFixedPosition(20, 790);
            //Table table = new Table(2);
            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + customer.getCustomerName() + "\n" + "Address: " + customer.getAddress() + "\n" + "Email: " + customer.getEmail() + "\n" + "Telefon: " + customer.getPhoneNumber());
            var paragraph2 = new Paragraph("Case name: " + selectedCase.getCaseName() + "\n" + "Case ID: " + selectedCase.getCaseID() + "\n" + "Case created; " + selectedCase.getCreatedDate() + "\n" + "Technician: " + selectedCase.getAssignedTechnician() + "\n" + "ContactPerson: " + selectedCase.getContactPerson() + "\n" + "Report description: " + report.getReportDescription());


            //document.add(logoImage);

            //document.add();


            document.add(paragraph1);

            document.add(paragraph2);
//            for (Section section : sections) {
//                    if (section.getSectionTitle() != null) {
//                        var paragraph4 = new Paragraph(section.getSectionTitle());
//                        document.add(paragraph4);
//                    }
//                    if (section.getSketch() != null) {
//                        var paragraph5 = new Paragraph(""+section.getSketch());
//                        document.add(paragraph5);
//                    }
//                    if (section.getSketchComment() != null) {
//                        var paragraph6 = new Paragraph(section.getSketchComment());
//                        document.add(paragraph6);
//                    }
//                    if (section.getImage() != null) {
//                        var paragraph7 = new Paragraph(""+section.getImage());
//                        document.add(paragraph7);
//                    }
//                    if (section.getImageComment() != null) {
//                        var paragraph8 = new Paragraph(section.getImageComment());
//                        document.add(paragraph8);
//                    }
//                    if (section.getDescription() != null) {
//                        var paragraph9 = new Paragraph(section.getDescription());
//                        document.add(paragraph9);
//                    }
//            }
            document.close();
        } catch (DocumentException e) {
            document.close();
            throw new DocumentException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void readPdfFile(Report report){
        try {
            PdfReader pdfReader = new PdfReader(report.getReportName() + ".PDF");
            PdfTextExtractor parser = new PdfTextExtractor(pdfReader);
            int numberOfPages = pdfReader.getNumberOfPages;

            for (int i = 1; i < numberOfPages ; i++) {
                parser.
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
