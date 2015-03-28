
package oscar.form.study;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmStudyPING_DiabetesRecord extends FrmStudyRecord {

    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
            throws SQLException {
        Properties props = new Properties();
        if (existingID <= 0) {
            
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
				Date dob = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"));
				props.setProperty("demographic_no", rs.getString("demographic_no"));
				props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
				props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
				props.setProperty("pName", oscar.Misc.getString(rs, "pName"));
			}
            rs.close();
        } else {
            String sql = "SELECT * FROM formType2Diabetes WHERE demographic_no = "
                + demographicNo + " AND ID = " + existingID;
			props = (new oscar.form.FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }
    
    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formType2Diabetes WHERE demographic_no="
            + demographic_no + " AND ID=0";

		return ((new oscar.form.FrmRecordHelp()).saveFormRecord(props, sql));
    }


    public String findActionValue(String submit) throws SQLException {
        if (submit != null && submit.equalsIgnoreCase("print")) {
            return "print";
        } else if (submit != null && submit.equalsIgnoreCase("save")) {
            return "save";
        } else if (submit != null && submit.equalsIgnoreCase("exit")) {
            return "exit";
        } else {
            return "failure";
        }
    }

    public String createActionURL(String where, String action, String demoId,
            String formId, String studyId, String studyLink)
            throws SQLException {
        String temp = null;

        if (action.equalsIgnoreCase("print")) {
            temp = where + "?demoNo=" + demoId + "&formId=" + formId
                    + "&study_no=" + studyId + "&study_link" + studyLink;
        } else if (action.equalsIgnoreCase("save")) {
            temp = where + "?demographic_no=" + demoId + "&study_no=" + studyId
                    + "&study_link" + studyLink; //"&formId=" + formId +
        } else if (action.equalsIgnoreCase("exit")) {
            temp = where;
        } else {
            temp = where;
        }

        return temp;
    }

}
