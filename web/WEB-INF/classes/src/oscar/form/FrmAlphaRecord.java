package oscar.form;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import oscar.oscarDB.DBHandler;
import oscar.util.*;

public class FrmAlphaRecord extends FrmRecord {
	//FrmRecordHelp recordHlp = null;

    public Properties getFormRecord(int demographicNo, int existingID)
        throws SQLException  {
        Properties props = new Properties();

        if(existingID <= 0) {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName FROM demographic WHERE demographic_no = " +demographicNo ;
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props.setProperty("pName", rs.getString("pName"));
                props.setProperty("formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
            }
            rs.close();
			db.CloseConn();
        } else {
            String sql = "SELECT * FROM formAlpha WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props)  throws SQLException  {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formAlpha WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formAlpha WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		return ((new FrmRecordHelp()).getPrintRecord(sql));
    }


    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }
}