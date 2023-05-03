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

    private void generateReport(Case c, Report report, Section section) throws DocumentException {
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();

            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + customer.getCustomerName() );

        } catch (DocumentException e) {
            throw new DocumentException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
