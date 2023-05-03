package BLL.util;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.Section;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFGenerator {
    private Document document;
    private Customer customer;
    private Section section;

    private void generateReport(Report report) throws DocumentException {
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();

            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + this.customer.getCustomerName() + "\n" + "Address: " + this.customer.getAddress() + "\n" + "Email: " + this.customer.getEmail() + "\n" + "Telefon: " + this.customer.getPhoneNumber());
            var paragraph2 = new Paragraph(this.section.getSectionTitle() + "\n" + this.section.getSketch() + "\n" + this.section.getSketchComment()+ "\n"+this.section.getImage()+"\n"+this.section.getImageComment() + "\n"+this.section.getDescription());

        } catch (DocumentException e) {
            throw new DocumentException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
