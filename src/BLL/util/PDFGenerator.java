package BLL.util;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.Section;
import BLL.Manager;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import javafx.scene.control.Cell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

public class PDFGenerator {
    private Manager manager;
    private Document document;
    private Customer customer;
    private Section section;
    private Case selectedCase;
    private PdfTextExtractor pdfTextExtractor;

    public void generateReport(Report report, Case selectedCase, Customer customer) throws DocumentException, SQLException {
        manager = new Manager();
        List<Section> sections = manager.getSections(report.getReportID());
        try {
            document = new Document(PageSize.A4, 20, 20, 50, 25);
            PdfWriter writer = null;
            writer.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();
            String imageFile = "data/Images/WUAV Logo.png";
            ImageData imgData = ImageDataFactory.create(imageFile);
            Image logoImage = new Image(imgData);
            logoImage.setFixedPosition(20, 790);
            Table table = new Table(2);
            Cell cell1 = new Cell();
            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + customer.getCustomerName() + "\n" + "Address: " + customer.getAddress() + "\n" + "Email: " + customer.getEmail() + "\n" + "Telefon: " + customer.getPhoneNumber());
            var paragraph2 = new Paragraph("Case name: " + selectedCase.getCaseName() + "\n" + "Case ID: " + selectedCase.getCaseID() + "\n" + "Case created; " + selectedCase.getCreatedDate() + "\n" + "Technician: " + selectedCase.getAssignedTechnician() + "\n" + "ContactPerson: " + selectedCase.getContactPerson() + "\n" + "Report description: " + report.getReportDescription());
            var paragraph3 = new Paragraph(this.section.getSectionTitle() + "\n" + this.section.getSketch() + "\n" + this.section.getSketchComment() + "\n" + this.section.getImage() + "\n" + this.section.getImageComment() + "\n" + this.section.getDescription() + "\n");
            var paragraph4 = new Paragraph(""+this.section.getSectionTitle());
            var paragraph5 = new Paragraph(""+this.section.getSketch());
            var paragraph6 = new Paragraph(""+this.section.getSketchComment());
            var paragraph7 = new Paragraph(""+this.section.getImage());
            var paragraph8 = new Paragraph(""+this.section.getImageComment());
            var paragraph9 = new Paragraph(""+this.section.getDescription());
            table.addCell(String.valueOf(paragraph1));
            table.addCell(String.valueOf(paragraph2));

            document.add((Element) logoImage);

            document.add((Element) table);

            //document.add(addLogo(writer, this.document));

            document.add(paragraph1);

            document.add(paragraph2);
            for (Section section : sections) {

                if (section.getSectionTitle() != null && section.getSketch() != null && section.getSketchComment() != null && section.getImage() != null && section.getImageComment() != null && section.getDescription() != null) {
                    document.add(paragraph3);
                } else {
                    if (section.getSectionTitle() != null) {
                        document.add(paragraph4);
                    }
                    if (section.getSketch() != null) {
                        document.add(paragraph5);
                    }
                    if (section.getSketchComment() != null) {
                        document.add(paragraph6);
                    }
                    if (section.getImage() != null) {
                        document.add(paragraph7);
                    }
                    if (section.getImageComment() != null) {
                        document.add(paragraph8);
                    }
                    if (section.getDescription() != null) {
                        document.add(paragraph9);
                    }
                }
            }
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
