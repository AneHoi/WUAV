package BLL.util;

import BE.*;
import BE.Section;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGenerator {
    private Document document;
    private Customer customer;
    private Section section;
    private Addendum addendum;

    private void generateReport(Report report) throws DocumentException {
        try {
            document = new Document(PageSize.A4,20,20,50,25);
            PdfWriter writer = null;
            writer.getInstance(document, new FileOutputStream(report.getReportName() + ".PDF"));

            document.open();

            var normal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
            var paragraph1 = new Paragraph("Costumer name: " + this.customer.getCustomerName() + "\n" + "Address: " + this.customer.getAddress() + "\n" + "Email: " + this.customer.getEmail() + "\n" + "Telefon: " + this.customer.getPhoneNumber()+"\n");
            var paragraph2 = new Paragraph(this.section.getSectionTitle() + "\n" + this.section.getSketch() + "\n" + this.section.getSketchComment()+ "\n"+this.section.getImage()+"\n"+this.section.getImageComment() + "\n"+this.section.getDescription()+"\n");
            var paragraph3 = new Paragraph();
            document.add(addLogo(writer, this.document));
            document.add(paragraph1);

            document.add(paragraph2);

            document.add(paragraph3);

            document.close();
        } catch (DocumentException e) {
            document.close();
            throw new DocumentException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private Element addLogo(PdfWriter writer, Document document){
        String img = "data/Images/WUAV Logo.png";
        Image image = null;
        try {
            image = Image.getInstance(img);
            image.setAlignment(Element.ALIGN_RIGHT);
            image.setAbsolutePosition(20, 790);
            image.scalePercent(7.5f, 7.5f);
            writer.getDirectContent().addImage(image, true);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Alert alert =  new Alert(Alert.AlertType.ERROR, "Image could not be added to pdf", ButtonType.CLOSE);
        }
        return image;
    }
}
