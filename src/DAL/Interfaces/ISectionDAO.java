package DAL.Interfaces;

import com.itextpdf.text.Image;
import com.itextpdf.text.Section;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ISectionDAO {
    List<Section> getSections(int reportID) throws SQLException;
    void createNewSection(String sectionTitle, byte[] sketch, String sketchComment, byte[] image, String imageComment, String description, int madeByTech, int reportID) throws SQLException;
}
