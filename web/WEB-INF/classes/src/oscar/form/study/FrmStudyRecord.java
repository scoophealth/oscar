package oscar.form.study;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import oscar.oscarDB.DBHandler;
import oscar.util.*;

public abstract class FrmStudyRecord {
    public abstract Properties getFormRecord(int demographicNo, int existingID) throws SQLException  ;
    public abstract int saveFormRecord(Properties props) throws SQLException ;
    public abstract String findActionValue(String submit) throws SQLException ;
    public abstract String createActionURL(String where, String action, String demoId, String formId, String studyId, String studyName) throws SQLException ;
}