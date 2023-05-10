package DAL.Interfaces;

import BE.Section;

import java.sql.SQLException;
import java.util.List;

public interface ISectionDAO {
    List<Section> getSections(int reportID) throws SQLException;
}