// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarBilling.ca.on.pageUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.on.bean.BillingClaimBatchAcknowledgementReportBeanHandler;
import oscar.oscarBilling.ca.on.bean.BillingClaimsErrorReportBeanHandler;
import oscar.oscarBilling.ca.on.bean.BillingEDTOBECOutputSpecificationBean;
import oscar.oscarBilling.ca.on.bean.BillingEDTOBECOutputSpecificationBeanHandler;
import oscar.oscarBilling.ca.on.data.BillingClaimsErrorReportBeanHandlerSave;
import oscar.oscarDB.DBHandler;

public class BillingDocumentErrorReportUploadAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		BillingDocumentErrorReportUploadForm frm = (BillingDocumentErrorReportUploadForm) form;
		request.getSession().setAttribute("BillingDocumentErrorReportUploadForm", frm);
		FormFile file1 = frm.getFile1();
		ArrayList messages = new ArrayList();
		ActionMessages errors = new ActionMessages();

		if (!saveFile(file1)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.fileNotAdded"));
			saveErrors(request, errors);
			return (new ActionForward(mapping.getInput()));
		} else {
			if (getData(file1.getFileName(), request))
				return mapping.findForward("success");
			else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.incorrectFileFormat"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
		}
	}

	/**
	 * 
	 * Save a Jakarta FormFile to a preconfigured place.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean saveFile(FormFile file) {
		String retVal = null;
		boolean isAdded = true;

		try {
			// retrieve the file data
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream stream = file.getInputStream();
			OscarProperties props = OscarProperties.getInstance();

			// properties must exist
			String place = props.getProperty("DOCUMENT_DIR");

			if (!place.endsWith("/"))
				place = new StringBuilder(place).insert(place.length(), "/").toString();
			retVal = place + file.getFileName();
			MiscUtils.getLogger().debug(retVal);
			// write the file to the file specified
			OutputStream bos = new FileOutputStream(retVal);
			int bytesRead = 0;
			byte[] buffer = file.getFileData();
			while ((bytesRead = stream.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			bos.close();

			// close the stream
			stream.close();
		} catch (FileNotFoundException e) {
			MiscUtils.getLogger().error("File not found", e);
			return isAdded = false;

		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
			return isAdded = false;
		}

		return isAdded;
	}

	/**
	 * 
	 * Write to database
	 * 
	 * @param fileName -
	 *            the filename to store
	 * 
	 */
	private void write2Database(String fileName) {
		try {
			
			String sql = "INSERT INTO measurementCSSLocation(location) VALUES('" + fileName + "')";
			MiscUtils.getLogger().debug("Sql Statement: " + sql);
			DBHandler.RunSQL(sql);
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}

	/**
	 * 
	 * Get Data from the file.
	 * 
	 * @param file
	 * @return
	 */
	private boolean getData(String fileName, HttpServletRequest request) {
		boolean isGot = false;

		try {
			OscarProperties props = OscarProperties.getInstance();
			// properties must exist
			String filepath = props.getProperty("DOCUMENT_DIR");
			boolean bNewBilling = props.getProperty("isNewONbilling", "").equals("true") ? true : false;
			if (!filepath.endsWith("/"))
				filepath = new StringBuilder(filepath).insert(filepath.length(), "/").toString();
			FileInputStream file = new FileInputStream(filepath + fileName);
			MiscUtils.getLogger().debug("file path: " + filepath + fileName);
			// Assign associated report Name
			ArrayList messages = new ArrayList();
			String ReportName = "";
			String ReportFlag = "";

			if (fileName.substring(0, 1).compareTo("E") == 0) {
				ReportName = "Claims Error Report";
				BillingClaimsErrorReportBeanHandler hd = generateReportE(file, bNewBilling, fileName);
				request.setAttribute("claimsErrors", hd);
				isGot = hd.verdict;
			}
			if (fileName.substring(0, 1).compareTo("B") == 0) {
				ReportName = "Claim Batch Acknowledgement Report";
				BillingClaimBatchAcknowledgementReportBeanHandler hd = generateReportB(file);
				request.setAttribute("batchAcks", hd);
				isGot = hd.verdict;
			}
			if (fileName.substring(0, 1).compareTo("X") == 0) {
				ReportName = "Claim File Rejection Report";
				messages = generateReportX(file);
				request.setAttribute("messages", messages);
				isGot = reportXIsGenerated;
			}
			if (fileName.substring(0, 1).compareTo("R") == 0) {
				ReportName = "EDT OBEC Output Specification";
				BillingEDTOBECOutputSpecificationBeanHandler hd = generateReportR(file);
				request.setAttribute("outputSpecs", hd);
				isGot = hd.verdict;
			}
			request.setAttribute("ReportName", ReportName);
		} catch (FileNotFoundException fnfe) {

			MiscUtils.getLogger().debug("File not found");
			MiscUtils.getLogger().error("Error", fnfe);
			return isGot = false;

		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
			return isGot = false;
		}
		return isGot;
	}

	/**
	 * 
	 * Generate Claims Error Report (E).
	 * 
	 * @param file
	 * @return BillingClaimsErrorReportBeanHandler
	 */
	private BillingClaimsErrorReportBeanHandler generateReportE(FileInputStream file, boolean bB, String filename) {
		BillingClaimsErrorReportBeanHandler hd = null;
		if (bB) {
			hd = (new BillingClaimsErrorReportBeanHandlerSave(file, filename)).getErrorReportBeanObj(file);
		} else {
			hd = new BillingClaimsErrorReportBeanHandler(file);
		}

		return hd;
	}

	/**
	 * 
	 * Generate Claim Batch Acknowledgement Report (B).
	 * 
	 * @param file
	 * @return BillingClaimBatchAcknowlegementReportBeanHandler
	 */
	private BillingClaimBatchAcknowledgementReportBeanHandler generateReportB(FileInputStream file) {
		BillingClaimBatchAcknowledgementReportBeanHandler hd = new BillingClaimBatchAcknowledgementReportBeanHandler(
				file);

		return hd;
	}

	/**
	 * 
	 * Generate Claim File Rejection Report (X).
	 * 
	 * @param file
	 * @return
	 */
	private boolean reportXIsGenerated = true;

	private ArrayList generateReportX(FileInputStream file) {
		ArrayList messages = new ArrayList();
		messages.add("M01 | Message Reason         Length     Msg Type   Filler  Record Image");
		messages.add("M02 | File:    File Name    Date:   Mail Date   Time: Mail Time     Process Date");
		InputStreamReader reader = new InputStreamReader(file);
		BufferedReader input = new BufferedReader(reader);
		String nextline;
		try {
			while ((nextline = input.readLine()) != null) {
				String headerCount = nextline.substring(2, 3);

				if (headerCount.compareTo("1") == 0) {
					String recordLength = nextline.substring(23, 28);
					String msgType = nextline.substring(28, 31);
					String filler = nextline.substring(32, 39);
					String error = nextline.substring(39, 76);
					String explain = nextline.substring(3, 23);
					String msg = "M01 | " + explain + "   " + recordLength + "   " + msgType + "   " + filler + "   "
							+ URLEncoder.encode(error, "UTF-8");
					messages.add(msg);

				}
				if (headerCount.compareTo("2") == 0) {
					String mailFile = nextline.substring(8, 20);
					String mailDate = nextline.substring(25, 33);
					String mailTime = nextline.substring(38, 44);
					String batchProcessDate = nextline.substring(50, 58);
					String msg = "M02 | File:   " + mailFile + "    " + "Date:   " + mailDate + "   " + "Time: "
							+ mailTime + "     PDate: " + batchProcessDate;
					messages.add(msg);
				}
			}

		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
		} catch (StringIndexOutOfBoundsException ioe) {
			reportXIsGenerated = false;
		}
		return messages;
	}

	/**
	 * 
	 * Generate EDT OBEC Output Specification (R).
	 * 
	 * @param file
	 * @return BillingEDTOBECOutputSpecificationBeanHandler
	 */
	private BillingEDTOBECOutputSpecificationBeanHandler generateReportR(FileInputStream file) {
		BillingEDTOBECOutputSpecificationBeanHandler hd = new BillingEDTOBECOutputSpecificationBeanHandler(file);
		Vector outputSpecVector = hd.getEDTOBECOutputSecifiationBeanVector();
		try {
			

			for (int i = 0; i < outputSpecVector.size(); i++) {
				BillingEDTOBECOutputSpecificationBean bean = (BillingEDTOBECOutputSpecificationBean) outputSpecVector
						.elementAt(i);
				String hin = bean.getHealthNo();
				String responseCode = bean.getResponseCode();
				int responseCodeNum = -1;
				try {
					responseCodeNum = Integer.parseInt(responseCode);
				} catch (Exception e) {
				}

				if (responseCodeNum < 50 || responseCodeNum > 59) {

					String sql = "SELECT * FROM batchEligibility where responseCode='" + responseCode + "'";
					ResultSet rs = DBHandler.GetSQL(sql);

					String sqlDemo = "SELECT * FROM demographic WHERE hin='" + hin + "'";
					ResultSet rsDemo = DBHandler.GetSQL(sqlDemo);

					if (rsDemo.next()) {
						if (rsDemo.getString("ver").compareTo(bean.getVersion()) == 0) {
							String sqlVer = "UPDATE demographic SET ver ='##' WHERE hin='" + hin + "'";
							DBHandler.RunSQL(sqlVer);
							String sqlAlert = "SELECT * FROM demographiccust where demographic_no ='"
									+ rsDemo.getString("demographic_no") + "'";
							MiscUtils.getLogger().debug("Select Demo sql: " + sqlAlert);
							ResultSet rsAlert = DBHandler.GetSQL(sqlAlert);
							if (rsAlert.next() && rs.next()) {
								String newAlert = rsAlert.getString("cust3") + "\n" + "Invalid old version code: "                                                                                
										+ bean.getVersion() + "\nReason: " + rs.getString("MOHResponse") + "- "
										+ rs.getString("reason") + "\nResponse Code: " + responseCode;
								String newAlertSql = "UPDATE demographiccust SET cust3 = '" + newAlert
										+ "' where demographic_no='" + rsDemo.getString("demographic_no") + "'";
								MiscUtils.getLogger().debug("Update alert msg: " + newAlertSql);
								DBHandler.RunSQL(newAlertSql);
							}
							rsAlert.close();
						}
						rs.close();
						rsDemo.close();
					}
				}
			}
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return hd;
	}

}