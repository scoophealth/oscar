package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import oscar.oscarDB.DBHandler;

public class EctEChartBean {
	public EctEChartBean() {
	}
	public void setEChartBean(String demoNo) {
		demographicNo = demoNo;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql = "select * from eChart where demographicNo=" + demoNo
					+ " ORDER BY eChartId DESC limit 1";
			ResultSet rs = db.GetSQL(sql);
			if (rs.next()) {
				eChartTimeStamp = rs.getDate("timeStamp");
				socialHistory = rs.getString("socialHistory");
				familyHistory = rs.getString("familyHistory");
				medicalHistory = rs.getString("medicalHistory");
				ongoingConcerns = rs.getString("ongoingConcerns");
				reminders = rs.getString("reminders");
				encounter = rs.getString("encounter");
				subject = rs.getString("subject");
			} else {
				eChartTimeStamp = null;
				socialHistory = "";
				familyHistory = "";
				medicalHistory = "";
				ongoingConcerns = "";
				reminders = "";
				encounter = "";
				subject = "";
			}
			rs.close();
			db.CloseConn();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public Date eChartTimeStamp;
	public String providerNo;
	public String userName;
	public String demographicNo;
	public String socialHistory;
	public String familyHistory;
	public String medicalHistory;
	public String ongoingConcerns;
	public String reminders;
	public String encounter;
	public String subject;
}