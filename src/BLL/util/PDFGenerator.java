package BLL.util;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.TextsAndImagesOnReport;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

public class PDFGenerator {
    private Document document;
    private TextsAndImagesOnReport textsAndImagesOnReport;
    private PdfTextExtractor pdfTextExtractor;

    public void generateReport(Report report, Case selectedCase, Customer customer) throws DocumentException, SQLException {
        try {
            document = new Document(PageSize.A4, 20, 20, 50, 25);
            PdfWriter writer = null;
            writer.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();
            String imageFile = "data/Images/WUAV Logo.png";
            Image logoImage = new Image(imageFile);
            Table table = new Table(2);
            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + customer.getCustomerName() + "\n" + "Address: " + customer.getAddress() + "\n" + "Email: " + customer.getEmail() + "\n" + "Telefon: " + customer.getPhoneNumber());
            var paragraph2 = new Paragraph("Case name: " + selectedCase.getCaseName() + "\n" + "Case ID: " + selectedCase.getCaseID() + "\n" + "Case created; " + selectedCase.getCreatedDate() + "\n" + "Technician: " + selectedCase.getAssignedTechnician() + "\n" + "ContactPerson: " + selectedCase.getContactPerson() + "\n" + "Report description: " + report.getReportDescription());
            //var paragraph3 = new Paragraph(textsAndImagesOnReport.getTextOrImage());

            //document.add(logoImage);

            document.add(paragraph1);

            document.add(paragraph2);

            //document.add(paragraph3);

            document.close();
        } catch (DocumentException e) {
            document.close();
            throw new DocumentException(e);
        } catch (FileNotFoundException e) {
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
