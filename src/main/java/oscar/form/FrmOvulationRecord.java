
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmOvulationRecord  extends FrmRecord {
    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
            throws SQLException {
        Properties props = new Properties();

        if(existingID <= 0) {
			
            String sql = "SELECT demographic_no, last_name, first_name, " +
						 "year_of_birth, month_of_birth, date_of_birth, hin, ver " +
						 "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);

            if(rs.next()) {
                    java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));

                    props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                    props.setProperty("clientFirstName", oscar.Misc.getString(rs, "first_name"));
                    props.setProperty("clientLastName", oscar.Misc.getString(rs, "last_name"));
                    props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                    //props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                    props.setProperty("dob", String.valueOf(UtilDateUtilities.calcAge(dob)));
                    props.setProperty("healthNum", oscar.Misc.getString(rs, "hin") + oscar.Misc.getString(rs, "ver"));
            }
            rs.close();

        } else {
            String sql = "SELECT * FROM formovulation WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formovulation WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
	}

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formovulation WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		return ((new FrmRecordHelp()).getPrintRecord(sql));
    }


    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
