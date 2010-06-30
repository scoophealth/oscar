/*
 * MessageUploader.java
 *
 * Created on June 18, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.util.UtilDateUtilities;

public final class MessageUploader {

	private static final Logger logger = MiscUtils.getLogger();

	private MessageUploader() {
		// there's no reason to instantiate a class with no fields.
	}

	/**
	 * Insert the lab into the proper tables of the database
	 */
	public static String routeReport(String serviceName, String type, String hl7Body, int fileId) throws Exception {

		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
		Hl7TextMessageDao hl7TextMessageDao = (Hl7TextMessageDao) SpringUtils.getBean("hl7TextMessageDao");

		String retVal = "";
		try {
			MessageHandler h = Factory.getHandler(type, hl7Body);
			Base64 base64 = new Base64();

			String firstName = h.getFirstName();
			String lastName = h.getLastName();
			String dob = h.getDOB();
			String sex = h.getSex();
			String hin = h.getHealthNum();
			String resultStatus = "";
			String priority = h.getMsgPriority();
			String requestingClient = h.getDocName();
			String reportStatus = h.getOrderStatus();
			String accessionNum = h.getAccessionNum();
			ArrayList docNums = h.getDocNums();
			int finalResultCount = h.getOBXFinalResultCount();
			String obrDate = h.getMsgDate();

			// reformat date
			String format = "yyyy-MM-dd HH:mm:ss".substring(0, obrDate.length() - 1);
			obrDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(obrDate, format), "yyyy-MM-dd HH:mm:ss");

			int i = 0;
			int j = 0;
			while (resultStatus.equals("") && i < h.getOBRCount()) {
				j = 0;
				while (resultStatus.equals("") && j < h.getOBXCount(i)) {
					if (h.isOBXAbnormal(i, j)) resultStatus = "A";
					j++;
				}
				i++;
			}

			ArrayList<String> disciplineArray = h.getHeaders();
			String next = "";
			if (disciplineArray != null && disciplineArray.size() > 0) next = disciplineArray.get(0);

			int sepMark;
			if ((sepMark = next.indexOf("<br />")) < 0) {
				if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
			}
			String discipline = next.substring(0, sepMark).trim();

			for (i = 1; i < disciplineArray.size(); i++) {

				next = disciplineArray.get(i);
				if ((sepMark = next.indexOf("<br />")) < 0) {
					if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
				}

				if (!next.trim().equals("")) discipline = discipline + "/" + next.substring(0, sepMark);
			}

			Hl7TextMessage hl7TextMessage = new Hl7TextMessage();
			hl7TextMessage.setFileUploadCheckId(fileId);
			hl7TextMessage.setType(type);
			hl7TextMessage.setBase64EncodedeMessage(DataTypeUtils.encodeToBase64String(hl7Body));
			hl7TextMessage.setServiceName(serviceName);
			hl7TextMessageDao.persist(hl7TextMessage);

			int insertID = hl7TextMessage.getId();

			Hl7TextInfo hl7TextInfo = new Hl7TextInfo();
			hl7TextInfo.setLabNumber(insertID);
			hl7TextInfo.setLastName(lastName);
			hl7TextInfo.setFirstName(firstName);
			hl7TextInfo.setSex(sex);
			hl7TextInfo.setHealthNumber(hin);
			hl7TextInfo.setResultStatus(resultStatus);
			hl7TextInfo.setFinalResultCount(finalResultCount);
			hl7TextInfo.setObrDate(obrDate);
			hl7TextInfo.setPriority(priority);
			hl7TextInfo.setRequestingProvider(requestingClient);
			hl7TextInfo.setDiscipline(discipline);
			hl7TextInfo.setReportStatus(reportStatus);
			hl7TextInfo.setAccessionNumber(accessionNum);
			hl7TextInfoDao.persist(hl7TextInfo);

			String demProviderNo = patientRouteReport(insertID, lastName, firstName, sex, dob, hin, DBHandler.getConnection());
			providerRouteReport(String.valueOf(insertID), docNums, DBHandler.getConnection(), demProviderNo, type);
			retVal = h.audit();
		} catch (Exception e) {
			logger.error("Error uploading lab to database");
			throw e;
		}

		return (retVal);

	}

	/**
	 * Attempt to match the doctors from the lab to a provider
	 */
	private static void providerRouteReport(String labId, ArrayList docNums, Connection conn, String altProviderNo, String labType) throws Exception {

		ArrayList providerNums = new ArrayList();
		PreparedStatement pstmt;
		String sql = "";
		if (docNums != null) {
			for (int i = 0; i < docNums.size(); i++) {

				if (!((String) docNums.get(i)).trim().equals("")) {
					sql = "select provider_no from provider where ohip_no = '" + ((String) docNums.get(i)) + "'";
					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						providerNums.add(oscar.Misc.getString(rs, "provider_no"));
					}
					rs.close();
					pstmt.close();
				}
			}
		}

		ProviderLabRouting routing = new ProviderLabRouting();
		if (providerNums.size() > 0) {
			for (int i = 0; i < providerNums.size(); i++) {
				String provider_no = (String) providerNums.get(i);
				routing.route(labId, provider_no, conn, "HL7");
			}
		} else {
			routing.route(labId, "0", conn, "HL7");
			routing.route(labId, altProviderNo, conn, "HL7");
		}

	}

	/**
	 * Attempt to match the patient from the lab to a demographic, return the patients provider which is to be used then no other provider can be found to match the patient to.
	 */
	private static String patientRouteReport(int labId, String lastName, String firstName, String sex, String dob, String hin, Connection conn) throws SQLException {

		String sql;
		String demo = "0";
		String provider_no = "0";
		// 19481015
		String dobYear = "%";
		String dobMonth = "%";
		String dobDay = "%";
		String hinMod = "%";

		int count = 0;
		try {

			if (hin != null) {
				hinMod = new String(hin);
				if (hinMod.length() == 12) {
					hinMod = hinMod.substring(0, 10);
				}
			}

			if (dob != null && !dob.equals("")) {
				String[] dobArray = dob.split("-");
				dobYear = dobArray[0];
				dobMonth = dobArray[1];
				dobDay = dobArray[2];
			}

			if (!firstName.equals("")) firstName = firstName.substring(0, 1);
			if (!lastName.equals("")) lastName = lastName.substring(0, 1);

			if (hinMod.equals("%")) {
				sql = "select demographic_no, provider_no from demographic where" + " last_name like '" + lastName + "%' and " + " first_name like '" + firstName + "%' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
			} else if (OscarProperties.getInstance().getBooleanProperty("LAB_NOMATCH_NAMES", "yes")) {
				sql = "select demographic_no, provider_no from demographic where hin='" + hinMod + "' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
			} else {
				sql = "select demographic_no, provider_no from demographic where hin='" + hinMod + "' and " + " last_name like '" + lastName + "%' and " + " first_name like '" + firstName + "%' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
			}

			logger.info(sql);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				count++;
				demo = oscar.Misc.getString(rs, "demographic_no");
				provider_no = oscar.Misc.getString(rs, "provider_no");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException sqlE) {
			throw sqlE;
		}

		try {
			if (count != 1) {
				demo = "0";
				logger.info("Could not find patient for lab: " + labId + " # of possible matches :" + count);
			} else {
				Hl7textResultsData rd = new Hl7textResultsData();
				rd.populateMeasurementsTable("" + labId, demo);
			}

			sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('" + demo + "', '" + labId + "','HL7')";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException sqlE) {
			logger.info("NO MATCHING PATIENT FOR LAB id =" + labId);
			throw sqlE;
		}

		return provider_no;
	}

	/**
	 * Used when errors occur to clean the database of labs that have not been inserted into all of the necessary tables
	 */
	public static void clean(int fileId) {

		try {

			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			Connection conn = DBHandler.getConnection();
			PreparedStatement pstmt;

			ResultSet rs;
			String sql;

			sql = "SELECT lab_id FROM hl7TextMessage WHERE fileUploadCheck_id='" + fileId + "'";
			pstmt = conn.prepareStatement(sql);
			ResultSet labId_rs = pstmt.executeQuery();

			while (labId_rs.next()) {
				int lab_id = labId_rs.getInt("lab_id");

				try {
					sql = "SELECT * FROM hl7TextInfo WHERE lab_no='" + lab_id + "'";
					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'hl7TextInfo', '" + lab_id + "', " + "'<id>" + db.getString(rs, "id") + "</id>" + "<lab_no>" + lab_id + "</lab_no>" + "<sex>" + db.getString(rs, "sex") + "</sex>" + "<health_no>" + db.getString(rs, "health_no") + "</health_no>" + "<result_status>" + db.getString(rs, "result_status") + "</result_status>"
						        + "<final_result_count>" + db.getString(rs, "final_result_count") + "</final_result_count>" + "<obr_date>" + db.getString(rs, "obr_date") + "</obr_date>" + "<priority>" + db.getString(rs, "priority") + "</priority>" + "<requesting_client>" + db.getString(rs, "requesting_client") + "</requesting_client>" + "<discipline>" + db.getString(rs, "discipline") + "</discipline>" + "<last_name>" + db.getString(rs, "last_name") + "</last_name>" + "<first_name>"
						        + db.getString(rs, "first_name") + "</first_name>" + "<report_status>" + db.getString(rs, "report_status") + "</report_status>" + "<accessionNum>" + db.getString(rs, "accessionNum") + "</accessionNum>')";

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

						sql = "DELETE FROM hl7TextInfo where lab_no='" + lab_id + "'";
						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
					}
				} catch (SQLException e) {
					logger.error("Error cleaning hl7TextInfo table for lab_no '" + lab_id + "'", e);
				}

				try {
					sql = "SELECT * FROM hl7TextMessage WHERE lab_id='" + lab_id + "'";
					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'hl7TextMessage', '" + lab_id + "', " + "'<lab_id>" + db.getString(rs, "lab_id") + "</lab_id>" + "<message>" + db.getString(rs, "message") + "</message>" + "<type>" + db.getString(rs, "type") + "</type>" + "<fileUploadCheck_id>" + db.getString(rs, "fileUploadCheck_id") + "</fileUploadCheck_id>')";

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

						sql = "DELETE FROM hl7TextMessage where lab_id='" + lab_id + "'";
						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
					}
				} catch (SQLException e) {
					logger.error("Error cleaning hl7TextMessage table for lab_id '" + lab_id + "'", e);
				}

				try {
					sql = "SELECT * FROM providerLabRouting WHERE lab_no='" + lab_id + "'";
					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'providerLabRouting', '" + lab_id + "', " + "'<provider_no>" + db.getString(rs, "provider_no") + "</provider_no>" + "<lab_no>" + db.getString(rs, "lab_no") + "</lab_no>" + "<status>" + db.getString(rs, "status") + "</status>" + "<comment>" + db.getString(rs, "comment") + "</comment>" + "<timestamp>"
						        + db.getString(rs, "timestamp") + "</timestamp>" + "<lab_type>" + db.getString(rs, "lab_type") + "</lab_type>" + "<id>" + db.getString(rs, "id") + "</id>')";

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

						sql = "DELETE FROM providerLabRouting where lab_no='" + lab_id + "'";
						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
					}
				} catch (SQLException e) {
					logger.error("Error cleaning providerLabRouting table for lab_no '" + lab_id + "'", e);
				}

				try {
					sql = "SELECT * FROM patientLabRouting WHERE lab_no='" + lab_id + "'";
					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'patientLabRouting', '" + lab_id + "', " + "'<demographic_no>" + db.getString(rs, "demographic_no") + "</demographic_no>" + "<lab_no>" + db.getString(rs, "lab_no") + "</lab_no>" + "<lab_type>" + db.getString(rs, "lab_type") + "</lab_type>" + "<id>" + db.getString(rs, "id") + "</id>')";

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

						sql = "DELETE FROM patientLabRouting where lab_no='" + lab_id + "'";
						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();
					}
				} catch (SQLException e) {
					logger.error("Error cleaning patientLabRouting table for lab_no '" + lab_id + "'", e);
				}

				try {
					sql = "SELECT measurement_id FROM measurementsExt WHERE keyval='lab_no' and val='" + lab_id + "'";
					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();

					while (rs.next()) {

						int meas_id = rs.getInt("measurement_id");
						sql = "SELECT * FROM measurements WHERE id='" + meas_id + "'";
						pstmt = conn.prepareStatement(sql);
						ResultSet rs2 = pstmt.executeQuery();

						if (rs2.next()) {
							sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'measurements', '" + meas_id + "', " + "'<id>" + rs2.getString("id") + "</id>" + "<type>" + rs2.getString("type") + "</type>" + "<demographicNo>" + rs2.getString("demographicNo") + "</demographicNo>" + "<providerNo>" + rs2.getString("providerNo") + "</providerNo>" + "<dataField>"
							        + rs2.getString("dataField") + "</dataField>" + "<measuringInstruction>" + rs2.getString("measuringInstruction") + "</measuringInstruction>" + "<comments>" + rs2.getString("comments") + "</comments>" + "<dateObserved>" + rs2.getString("dateObserved") + "</dateObserved>" + "<dateEntered>" + rs2.getString("dateEntered") + "</dateEntered>')";

							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();

							sql = "DELETE FROM measurements WHERE id='" + meas_id + "'";
							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();
						}

						sql = "SELECT * FROM measurementsExt WHERE measurement_id='" + meas_id + "'";
						pstmt = conn.prepareStatement(sql);
						rs2 = pstmt.executeQuery();

						while (rs2.next()) {
							sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'measurementsExt', '" + meas_id + "', " + "'<id>" + rs2.getString("id") + "</id>" + "<measurement_id>" + rs2.getString("measurement_id") + "</measurement_id>" + "<keyval>" + rs2.getString("keyval") + "</keyval>" + "<val>" + rs2.getString("val") + "</val>')";

							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();

							sql = "DELETE FROM measurementsExt WHERE measurement_id='" + meas_id + "'";
							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();
						}
					}

				} catch (SQLException e) {
					logger.error("Error cleaning measuremnts or measurementsExt table for lab_no '" + lab_id + "'", e);
				}

			}

			try {
				sql = "SELECT * FROM fileUploadCheck WHERE id = '" + fileId + "'";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					sql = "INSERT INTO recyclebin (provider_no, updatedatetime, table_name, keyword, table_content) " + "VALUES ('0', '" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "', 'fileUploadCheck', '" + fileId + "', " + "'<id>" + db.getString(rs, "id") + "</id>" + "<provider_no>" + db.getString(rs, "provider_no") + "</provider_no>" + "<filename>" + db.getString(rs, "filename") + "</filename>" + "<md5sum>" + db.getString(rs, "md5sum") + "</md5sum>" + "<datetime>"
					        + db.getString(rs, "date_time") + "</datetime>')";

					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

					sql = "DELETE FROM fileUploadCheck where id = '" + fileId + "'";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

				}
			} catch (SQLException e) {
				logger.error("Error cleaning fileUploadCheck table for id '" + fileId + "'", e);
			}

			pstmt.close();
			logger.info("Successfully cleaned the database");

		} catch (SQLException e) {
			logger.error("Could not clean database: ", e);
		}
	}
}
