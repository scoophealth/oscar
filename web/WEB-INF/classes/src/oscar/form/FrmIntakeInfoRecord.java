package oscar.form;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmIntakeInfoRecord extends FrmRecord {
	private String _dateFormat = "yyyy/MM/dd";

	public Properties getFormRecord(int demographicNo, int existingID)
		throws SQLException {
		Properties props = new Properties();
                
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql;

		if (existingID <= 0) {			
			sql = "SELECT demographic_no, sex, year_of_birth, month_of_birth, date_of_birth, phone FROM demographic WHERE demographic_no = "
                              + demographicNo;
			rs = db.GetSQL(sql);
			if (rs.next()) {
                                java.util.Date dob = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"));
				props.setProperty(
					"demographic_no",
					rs.getString("demographic_no"));
				props.setProperty(
					"formCreated",
					UtilDateUtilities.DateToString(
						UtilDateUtilities.Today(),
						_dateFormat));	
                                props.setProperty("dob", UtilDateUtilities.DateToString(dob,"yyyy/MM/dd"));
                                props.setProperty("sex", rs.getString("sex"));
                                props.setProperty("phone", rs.getString("phone"));
			}
			rs.close();
                        sql = "SELECT studyID FROM rehabStudy2004 WHERE demographic_no='"+demographicNo + "'";
                        rs = db.GetSQL(sql);
                        if (rs.next()){
                            props.setProperty("studyID", rs.getString("studyID"));
                        }
                        else{
                            props.setProperty("studyID", "N/A");
                        }
                        rs.close();
			db.CloseConn();
		} else {
			sql =
				"SELECT * FROM formIntakeInfo WHERE demographic_no = "
					+ demographicNo
					+ " AND ID = "
					+ existingID;
			rs = db.GetSQL(sql);

                        if(rs.next())
                        {
                            System.out.println("getting metaData");
                            ResultSetMetaData md = rs.getMetaData();

                            for(int i=1; i<=md.getColumnCount(); i++)
                            {
                                String name = md.getColumnName(i);

                                String value;
                                    System.out.println(" name = "+name+" type = "+md.getColumnTypeName(i)+" scale = "+md.getScale(i));
                                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))           
                                {

                                    if(rs.getInt(i)==1)
                                    {
                                        value = "checked='checked'";
                                        System.out.println("checking "+name);
                                    }
                                    else
                                    {
                                        value = "";
                                        System.out.println("not checking "+name);
                                    }
                                }
                                else
                                {
                                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                                    {
                                        value = UtilDateUtilities.DateToString(rs.getDate(i),"yyyy/MM/dd");
                                    }
                                    else
                                    {
                                        value = rs.getString(i);
                                    }
                                }

                                if(value!=null)
                                {
                                    props.setProperty(name, value);
                                }
                            }
                        }
                        rs.close();
                    }
                    db.CloseConn();		

		return props;
	}

	public int saveFormRecord(Properties props) throws SQLException {
		String demographic_no = props.getProperty("demographic_no");
		String sql =
			"SELECT * FROM formIntakeInfo WHERE demographic_no="
				+ demographic_no
				+ " AND ID=0";

		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).saveFormRecord(props, sql));
	}

	public Properties getPrintRecord(int demographicNo, int existingID)
		throws SQLException {
		String sql =
			"SELECT * FROM formIntakeInfo WHERE demographic_no = "
				+ demographicNo
				+ " AND ID = "
				+ existingID;
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).getPrintRecord(sql));
	}

	public String findActionValue(String submit) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).findActionValue(submit));
	}

	public String createActionURL(
		String where,
		String action,
		String demoId,
		String formId)
		throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).createActionURL(where, action, demoId, formId));
	}

}
