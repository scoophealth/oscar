/*
 * GDMLHandler.java
 *
 * Created on May 23, 2007, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

public class GDMLHandler implements MessageHandler {

	Logger logger = Logger.getLogger(GDMLHandler.class);

	public String parse(String serviceName, String fileName, int fileId) {

		int i = 0;
		try {
			ArrayList<String> messages = Utilities.separateMessages(fileName);
			for (i = 0; i < messages.size(); i++) {

				String msg = messages.get(i);
				MessageUploader.routeReport(serviceName, "GDML", msg, fileId);

			}

			// Since the gdml labs show more than one lab on the same page when grouped
			// by accession number their abnormal status must be updated to reflect the
			// other labs that they are grouped with aswell
			updateLabStatus(messages.size());
			logger.info("Parsed OK");
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message", e);
			return null;
		}
		return ("success");

	}

	// recheck the abnormal status of the last 'n' labs
	private void updateLabStatus(int n) throws SQLException {
		String sql = "SELECT lab_no, result_status FROM hl7TextInfo ORDER BY lab_no DESC";
		DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

		ResultSet rs = db.GetSQL(sql);
		while (rs.next() && n > 0) {

			// only recheck the result status if it is not already set to abnormal
			if (!db.getString(rs, "result_status").equals("A")) {
				oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler(db.getString(rs, "lab_no"));
				int i = 0;
				int j = 0;
				String resultStatus = "";
				while (resultStatus.equals("") && i < h.getOBRCount()) {
					j = 0;
					while (resultStatus.equals("") && j < h.getOBXCount(i)) {
						logger.info("obr(" + i + ") obx(" + j + ") abnormal ? : " + h.getOBXAbnormalFlag(i, j));
						if (h.isOBXAbnormal(i, j)) {
							resultStatus = "A";
							sql = "UPDATE hl7TextInfo SET result_status='A' WHERE lab_no='" + db.getString(rs, "lab_no") + "'";
							db.RunSQL(sql);
						}
						j++;
					}
					i++;
				}
			}

			n--;
		}
	}

}