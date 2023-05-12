package BLL;

import BE.Section;
import DAL.SectionDAO;

import java.sql.SQLException;
import java.util.List;

public class SectionManager {
    private SectionDAO sectionDAO;

    public SectionManager() {
        sectionDAO = new SectionDAO();
    }


    public void  createNewSection(String sectionTitle, byte[] sketch, String sketchComment, byte[] image, String imageComment, String description, int madeByTech, int reportID) throws SQLException {
        sectionDAO.createNewSection(sectionTitle,sketch,sketchComment,image,imageComment,description,madeByTech,reportID);
    }
    public List<Section> getSections(int reportID) throws SQLException {
        return sectionDAO.getSections(reportID);
    }


    public List<Section> getAllSections(int currentReportID) throws SQLException {
        return sectionDAO.getAllSections(currentReportID);
    }

    public void updateCurrentSection(Section currentSection) throws SQLException {
        sectionDAO.updateCurrentSection(currentSection);
    }
    public void createSectionForReport(Section section) throws SQLException {
        sectionDAO.createSectionForReport(section);
    }

    public void createSectionForAddendum(Section section) throws SQLException {
        sectionDAO.createSectionForAddendum(section);
    }

    public void deleteSection(int sectionID) throws SQLException {
        sectionDAO.deleteSection(sectionID);
    }

}
