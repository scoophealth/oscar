
package oscar.form;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;

import oscar.util.UtilDateUtilities;

public class FrmBCBrithSumMoRecord extends FrmRecord {
	
	public FrmBCBrithSumMoRecord() {
		this.dateFormat = "dd/MM/yyyy";
	}

	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
		
        Properties props = new Properties();

        if(existingID <= 0) {
        	this.setDemoProperties(loggedInInfo, demographicNo, props);
        	
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), dateFormat));
                props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), dateFormat));
                props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(new Date(), dateFormat));
        } 
        else {
            String sql = "SELECT * FROM formBCBirthSumMo WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			FrmRecordHelp frh = new FrmRecordHelp();
			frh.setDateFormat(dateFormat);
			props = (frh).getFormRecord(sql);
			
			this.setDemoCurProperties(loggedInInfo, demographicNo, props);
        }
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formBCBirthSumMo WHERE demographic_no=" +demographic_no +" AND ID=0";

		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
		return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formBCBirthSumMo WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
		return ((frh).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
 		return ((frh).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
 		return ((frh).createActionURL(where, action, demoId, formId));
    }

}
