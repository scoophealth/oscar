/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
/**
 * This sample code is an example of how to use the Business Objects APIs. 
 * Because the sample code is designed for demonstration only, it is 
 * unsupported.  You are free to modify and distribute the sample code as needed.   
 */
package com.businessobjects.samples;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crystaldecisions.report.web.viewer.ReportExportControl;
import com.crystaldecisions.reports.sdk.DataDefController;
import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.data.FieldDisplayNameType;
import com.crystaldecisions.sdk.occa.report.data.IConnectionInfo;
import com.crystaldecisions.sdk.occa.report.data.ITable;
import com.crystaldecisions.sdk.occa.report.data.ParameterField;
import com.crystaldecisions.sdk.occa.report.data.ParameterFieldDiscreteValue;
import com.crystaldecisions.sdk.occa.report.data.ParameterFieldRangeValue;
import com.crystaldecisions.sdk.occa.report.data.RangeValueBoundType;
import com.crystaldecisions.sdk.occa.report.data.Tables;
import com.crystaldecisions.sdk.occa.report.data.Values;
import com.crystaldecisions.sdk.occa.report.lib.IStrings;
import com.crystaldecisions.sdk.occa.report.lib.PropertyBag;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKExceptionBase;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKPrinterException;
import com.crystaldecisions.sdk.occa.report.reportsource.IReportSource;
import com.crystaldecisions.sdk.occa.report.document.PaperSize;
import com.crystaldecisions.sdk.occa.report.document.PaperSource;
import com.crystaldecisions.sdk.occa.report.document.PrintReportOptions;
import com.crystaldecisions.sdk.occa.report.document.PrinterDuplex;
import com.crystaldecisions.sdk.occa.report.exportoptions.CharacterSeparatedValuesExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.PDFExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.RTFWordExportFormatOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;

/**
 * JRC Helper Sample
 * ************************
 * Please note that you need to define a runtime server in order for this class
 * to compile.
 * ************************
 * @author Business Objects
 */
public class JRCHelperSample {

	/**
	 * Logs on to all existing datasource
	 * 
	 * @param clientDoc The reportClientDocument representing the report being used
	 * @param username	The DB logon user name
	 * @param password	The DB logon password
	 * @throws ReportSDKException
	 */
	public static void logonDataSource(ReportClientDocument clientDoc, 
				String username, String password) throws ReportSDKException {
		clientDoc.getDatabaseController().logon(username, password);
	}

	/**
	 * Changes the DataSource for each Table
	 * @param clientDoc The reportClientDocument representing the report being used
	 * @param username  The DB logon user name
	 * @param password  The DB logon password
	 * @param connectionURL  The connection URL
	 * @param driverName	The driver Name
	 * @param jndiName		The JNDI name
	 * @throws ReportSDKException
	 */
	public static void changeDataSource(ReportClientDocument clientDoc,
				String username, String password, String connectionURL,
				String driverName,String jndiName) throws ReportSDKException {

		changeDataSource(clientDoc, null, null, username, password, connectionURL, driverName, jndiName);
	}

	/**
	 * Changes the DataSource for a specific Table
	 * @param clientDoc The reportClientDocument representing the report being used
	 * @param reportName	"" for main report, name of subreport for subreport, null for all reports
	 * @param tableName		name of table to change.  null for all tables.
	 * @param username  The DB logon user name
	 * @param password  The DB logon password
	 * @param connectionURL  The connection URL
	 * @param driverName	The driver Name
	 * @param jndiName		The JNDI name
	 * @throws ReportSDKException
	 */
	public static void changeDataSource(ReportClientDocument clientDoc,
				String reportName, String tableName,
				String username, String password, String connectionURL,
				String driverName,String jndiName) throws ReportSDKException {

		PropertyBag propertyBag = null;
		IConnectionInfo connectionInfo = null;
		ITable origTable = null;
		ITable newTable = null;

		// Declare variables to hold ConnectionInfo values.
		// Below is the list of values required to switch to use a JDBC/JNDI
		// connection
		String TRUSTED_CONNECTION = "false";
		String SERVER_TYPE = "JDBC (JNDI)";
		String USE_JDBC = "true";
		String DATABASE_DLL = "crdb_jdbc.dll";
		String JNDI_OPTIONAL_NAME = jndiName;
		String CONNECTION_URL = connectionURL;
		String DATABASE_CLASS_NAME = driverName;

		// The next few parameters are optional parameters which you may want to
		// uncomment
		// You may wish to adjust the arguments of the method to pass these
		// values in if necessary
		// String TABLE_NAME_QUALIFIER = "new_table_name";
		// String SERVER_NAME = "new_server_name";
		// String CONNECTION_STRING = "new_connection_string";
		// String DATABASE_NAME = "new_database_name";
		// String URI = "new_URI";

		// Declare variables to hold database User Name and Password values
		String DB_USER_NAME = username;
		String DB_PASSWORD = password;

		// Obtain collection of tables from this database controller
		if (reportName == null || reportName.equals("")) {
			Tables tables = clientDoc.getDatabaseController().getDatabase().getTables();
			for(int i = 0;i < tables.size();i++){
				origTable = tables.getTable(i);
				if (tableName == null || origTable.getName().equals(tableName)) {
					newTable = (ITable)origTable.clone(true);

					// We set the Fully qualified name to the Table Alias to keep the
					// method generic
					// This workflow may not work in all scenarios and should likely be
					// customized to work
					// in the developer's specific situation. The end result of this
					// statement will be to strip
					// the existing table of it's db specific identifiers. For example
					// Xtreme.dbo.Customer becomes just Customer
					newTable.setQualifiedName(origTable.getAlias());

					// Change properties that are different from the original datasource
					// For example, if the table name has changed you will be required
					// to change it during this routine
					// table.setQualifiedName(TABLE_NAME_QUALIFIER);

					// Change connection information properties
					connectionInfo = newTable.getConnectionInfo();

					// Set new table connection property attributes
					propertyBag = new PropertyBag();

					// Overwrite any existing properties with updated values
					propertyBag.put("Trusted_Connection", TRUSTED_CONNECTION);
					propertyBag.put("Server Type", SERVER_TYPE);
					propertyBag.put("Use JDBC", USE_JDBC);
					propertyBag.put("Database DLL",DATABASE_DLL );
					propertyBag.put("JNDIOptionalName",JNDI_OPTIONAL_NAME );
					propertyBag.put("Connection URL", CONNECTION_URL);
					propertyBag.put("Database Class Name", DATABASE_CLASS_NAME);
					// propertyBag.put("Server Name", SERVER_NAME); //Optional property
					// propertyBag.put("Connection String", CONNECTION_STRING); //Optional property
					// propertyBag.put("Database Name", DATABASE_NAME); //Optional property
					// propertyBag.put("URI", URI); //Optional property
					connectionInfo.setAttributes(propertyBag);

					// Set database username and password
					// NOTE: Even if the username and password properties do not change
					// when switching databases, the
					// database password is *not* saved in the report and must be set at
					// runtime if the database is secured.
					connectionInfo.setUserName(DB_USER_NAME);
					connectionInfo.setPassword(DB_PASSWORD);

					// Update the table information
					clientDoc.getDatabaseController().setTableLocation(origTable, newTable);
				}
			}
		}
		// Next loop through all the subreports and pass in the same
		// information. You may consider
		// creating a separate method which accepts
		if (reportName == null || !(reportName.equals(""))) {
			IStrings subNames = clientDoc.getSubreportController().getSubreportNames();
			for (int subNum=0;subNum<subNames.size();subNum++) {
				Tables tables = clientDoc.getSubreportController().getSubreport(subNames.getString(subNum)).getDatabaseController().getDatabase().getTables();
				for(int i = 0;i < tables.size();i++){
					origTable = tables.getTable(i);
					if (tableName == null || origTable.getName().equals(tableName)) {
						newTable = (ITable)origTable.clone(true);

						// We set the Fully qualified name to the Table Alias to keep
						// the method generic
						// This workflow may not work in all scenarios and should likely
						// be customized to work
						// in the developer's specific situation. The end result of this
						// statement will be to strip
						// the existing table of it's db specific identifiers. For
						// example Xtreme.dbo.Customer becomes just Customer
						newTable.setQualifiedName(origTable.getAlias());

						// Change properties that are different from the original
						// datasource
						// table.setQualifiedName(TABLE_NAME_QUALIFIER);

						// Change connection information properties
						connectionInfo = newTable.getConnectionInfo();

						// Set new table connection property attributes
						propertyBag = new PropertyBag();

						// Overwrite any existing properties with updated values
						propertyBag.put("Trusted_Connection", TRUSTED_CONNECTION);
						propertyBag.put("Server Type", SERVER_TYPE);
						propertyBag.put("Use JDBC", USE_JDBC);
						propertyBag.put("Database DLL",DATABASE_DLL );
						propertyBag.put("JNDIOptionalName",JNDI_OPTIONAL_NAME );
						propertyBag.put("Connection URL", CONNECTION_URL);
						propertyBag.put("Database Class Name", DATABASE_CLASS_NAME);
						// propertyBag.put("Server Name", SERVER_NAME); //Optional property
						// propertyBag.put("Connection String", CONNECTION_STRING); //Optional property
						// propertyBag.put("Database Name", DATABASE_NAME); //Optional property
						// propertyBag.put("URI", URI); //Optional property
						connectionInfo.setAttributes(propertyBag);

						// Set database username and password
						// NOTE: Even if the username and password properties do not
						// change when switching databases, the
						// database password is *not* saved in the report and must be
						// set at runtime if the database is secured.
						connectionInfo.setUserName(DB_USER_NAME);
						connectionInfo.setPassword(DB_PASSWORD);

						// Update the table information
						clientDoc.getSubreportController().getSubreport(subNames.getString(subNum)).getDatabaseController().setTableLocation(origTable, newTable);
					}
				}
			}
		}
	}
	
	/**
	 * Passes a populated java.sql.Resultset object to a Table object
	 * 
	 * @param clientDoc The reportClientDocument representing the report being used
	 * @param rs		The java.sql.Resultset used to populate the Table
	 * @param tableName	The name of the table
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @throws ReportSDKException
	 */
	public static void passResultSet(ReportClientDocument clientDoc, java.sql.ResultSet rs,
			String tableName, String reportName) throws ReportSDKException {
		if(reportName.equals(""))
			clientDoc.getDatabaseController().setDataSource(rs, tableName,tableName+"_ResultSet");
		else
			clientDoc.getSubreportController().getSubreport(reportName).getDatabaseController().setDataSource(rs, tableName,tableName+"_ResultSet");

	}

	/**
	 * Passes a populated collection of a Java class to a Table object
	 * 
	 * @param clientDoc 	The reportClientDocument representing the report being used
	 * @param dataSet		The java.sql.Resultset used to populate the Table
	 * @param className		The fully-qualified class name of the POJO objects being passed
	 * @param tableName		The name of the table
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @throws ReportSDKException
	 */
	public static void passPOJO(ReportClientDocument clientDoc, Collection dataSet, 
			String className, String tableName, String reportName) throws ReportSDKException,ClassNotFoundException{
		if(reportName.equals(""))
			clientDoc.getDatabaseController().setDataSource(dataSet, Class.forName(className),tableName,tableName+"_POJO");
		else
			clientDoc.getSubreportController().getSubreport(reportName).getDatabaseController().setDataSource(dataSet, Class.forName(className),tableName,tableName+"_POJO");

	}

	/**
	 * Passes a single discrete parameter value to a report parameter
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param newValue		The new value of the parameter 
	 * @throws ReportSDKException
	 */
	public static void addDiscreteParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object newValue) throws ReportSDKException{
		DataDefController dataDefController = null;
		if(reportName.equals(""))
			dataDefController = clientDoc.getDataDefController();
		else
			dataDefController = clientDoc.getSubreportController().getSubreport(reportName).getDataDefController();

		ParameterFieldDiscreteValue newDiscValue = new ParameterFieldDiscreteValue();
		newDiscValue.setValue(newValue);

		ParameterField paramField = (ParameterField)dataDefController.getDataDefinition().getParameterFields().findField(parameterName, FieldDisplayNameType.fieldName, Locale.getDefault());
		boolean multiValue = paramField.getAllowMultiValue();

		if(multiValue) {
			Values newVals = (Values)paramField.getCurrentValues().clone(true);
			newVals.add(newDiscValue);
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName ,newVals);
		} else {
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName , newValue);
		}
	}

	/**
	 * Passes multiple discrete parameter values to a report parameter
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param newValues		An array of new values to get set on the parameter
	 * @throws ReportSDKException
	 */
	public static void addDiscreteParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object[] newValues) throws ReportSDKException{
		clientDoc.getDataDefController().getParameterFieldController().setCurrentValues(reportName, parameterName ,newValues);
	}

	/**
	 * Passes a single range parameter value to a report parameter.  The range is assumed to
	 * be inclusive on beginning and end.
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param beginValue	The value of the beginning of the range
	 * @param endValue		The value of the end of the range
	 * @throws ReportSDKException
	 */
	public static void addRangeParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object beginValue, Object endValue) throws ReportSDKException{
		addRangeParameterValue(clientDoc, parameterName, reportName, beginValue, RangeValueBoundType.inclusive, endValue, RangeValueBoundType.inclusive);
	}

	/**
	 * Passes multiple range parameter values to a report parameter.
	 *
	 * This overload of the addRangeParameterValue will only work if the
	 * parameter is setup to accept multiple values.
	 * 
	 * If the Parameter does not accept multiple values then it is expected that
	 * this version of the method will return an error
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param beginValues	Array of beginning values.  Must be same length as endValues.
	 * @param endValues		Array of ending values.  Must be same length as beginValues.
	 * @throws ReportSDKException
	 */
	public static void addRangeParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object[] beginValues, Object[] endValues) throws ReportSDKException{
		addRangeParameterValue(clientDoc, parameterName, reportName, beginValues, RangeValueBoundType.inclusive, endValues, RangeValueBoundType.inclusive);
	}
	
	/**
	 * Passes a single range parameter value to a report parameter
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param beginValue	The value of the beginning of the range
	 * @param lowerBoundType	The inclusion/exclusion range of the start of range.
	 * @param endValue		The value of the end of the range
	 * @param upperBoundType	The inclusion/exclusion range of the end of range.
	 * @throws ReportSDKException
	 */
	public static void addRangeParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object beginValue, RangeValueBoundType lowerBoundType,Object endValue, RangeValueBoundType upperBoundType) throws ReportSDKException{
		DataDefController dataDefController = null;
		if(reportName.equals(""))
			dataDefController = clientDoc.getDataDefController();
		else
			dataDefController = clientDoc.getSubreportController().getSubreport(reportName).getDataDefController();

		ParameterFieldRangeValue newRangeValue = new ParameterFieldRangeValue();
		newRangeValue.setBeginValue(beginValue);
		newRangeValue.setLowerBoundType(lowerBoundType);
		newRangeValue.setEndValue(endValue);
		newRangeValue.setUpperBoundType(upperBoundType);

		ParameterField paramField = (ParameterField)dataDefController.getDataDefinition().getParameterFields().findField(parameterName, FieldDisplayNameType.fieldName, Locale.getDefault());
		boolean multiValue = paramField.getAllowMultiValue();

		if (multiValue) {
			Values newVals = (Values)paramField.getCurrentValues().clone(true);
			newVals.add(newRangeValue);
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName , newVals);
		} else {
			clientDoc.getDataDefController().getParameterFieldController().setCurrentValue(reportName, parameterName , newRangeValue);
		}
	}

	/**
	 * Passes multiple range parameter values to a report parameter.
	 *
	 * This overload of the addRangeParameterValue will only work if the
	 * parameter is setup to accept multiple values.
	 * 
	 * If the Parameter does not accept multiple values then it is expected that
	 * this version of the method will return an error
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param parameterName	The name of the parameter
	 * @param reportName	The name of the subreport.  If tables in the main report
	 * 						is to be used, "" should be passed
	 * @param beginValues	Array of beginning values.  Must be same length as endValues.
	 * @param lowerBoundType	The inclusion/exclusion range of the start of range.
	 * @param endValues		Array of ending values.  Must be same length as beginValues.
	 * @param upperBoundType	The inclusion/exclusion range of the end of range.
	 * 
	 * @throws ReportSDKException
	 */
	public static void addRangeParameterValue(ReportClientDocument clientDoc, String parameterName, String reportName, Object[] beginValues,RangeValueBoundType lowerBoundType, Object[] endValues, RangeValueBoundType upperBoundType) throws ReportSDKException{
		// it is expected that the beginValues array is the same size as the
		// endValues array
		ParameterFieldRangeValue[] newRangeValues = new ParameterFieldRangeValue[beginValues.length];
		for(int i=0;i<beginValues.length;i++){
			newRangeValues[i] = new ParameterFieldRangeValue();
			newRangeValues[i].setBeginValue(beginValues[i]);
			newRangeValues[i].setLowerBoundType(lowerBoundType);
			newRangeValues[i].setEndValue(endValues[i]);
			newRangeValues[i].setUpperBoundType(upperBoundType);
		}
		clientDoc.getDataDefController().getParameterFieldController().setCurrentValues(reportName, parameterName , newRangeValues);

	}
	
	/**
	 * Exports a report to PDF
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param request		The HttpServletRequest object
	 * @param response		The HttpServletResponse object
	 * @param context		The ServletContext object
	 * @param startPage		Starting page
	 * @param endPage		Ending page
	 * @param attachment	true to prompts for open or save; false opens the report
	 * 						in the specified format after exporting.
	 * @throws ReportSDKExceptionBase
	 */
	public static void exportPDF(IReportSource reportSource, HttpServletRequest request, HttpServletResponse response, ServletContext  context, boolean attachment)throws ReportSDKExceptionBase{
		// PDF export allows page range export. The following routine ensures
		// that the requested page range is valid
		PDFExportFormatOptions  pdfOptions = new PDFExportFormatOptions();
		ExportOptions exportOptions = new ExportOptions();
		exportOptions.setExportFormatType(ReportExportFormat.PDF);		
		exportOptions.setFormatOptions(pdfOptions);

		ReportExportControl reportExportControl = new ReportExportControl();
		reportExportControl.setReportSource(reportSource);
		reportExportControl.setExportOptions(exportOptions);
		// ExportAsAttachment(true) prompts for open or save; false opens the report
		// in the specified format after exporting.
		reportExportControl.setExportAsAttachment(attachment);
		// Export the report
		reportExportControl.processHttpRequest(request, response, context, null);

	}
	
	/**
	 * Exports a report to PDF for a range of pages
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param request		The HttpServletRequest object
	 * @param response		The HttpServletResponse object
	 * @param context		The ServletContext object
	 * @param startPage		Starting page
	 * @param endPage		Ending page
	 * @param attachment	true to prompts for open or save; false opens the report
	 * 						in the specified format after exporting.
	 * @throws ReportSDKExceptionBase
	 */
	public static void exportPDF(IReportSource reportSource, HttpServletRequest request, HttpServletResponse response, ServletContext  context, int startPage, int endPage,boolean attachment)throws ReportSDKExceptionBase{
		// PDF export allows page range export. The following routine ensures
		// that the requested page range is valid
		PDFExportFormatOptions  pdfOptions = new PDFExportFormatOptions();
		pdfOptions.setStartPageNumber(startPage);
		pdfOptions.setEndPageNumber(endPage);
		ExportOptions exportOptions = new ExportOptions();
		exportOptions.setExportFormatType(ReportExportFormat.PDF);		
		exportOptions.setFormatOptions(pdfOptions);

		ReportExportControl reportExportControl = new ReportExportControl();
		reportExportControl.setReportSource(reportSource);
		reportExportControl.setExportOptions(exportOptions);
		// ExportAsAttachment(true) prompts for open or save; false opens the report
		// in the specified format after exporting.
		reportExportControl.setExportAsAttachment(attachment);
		// Export the report
		reportExportControl.processHttpRequest(request, response, context, null);

	}
	
	/**
	 * Exports a report to RTF
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param request		The HttpServletRequest object
	 * @param response		The HttpServletResponse object
	 * @param context		The ServletContext object
	 * @param attachment	true to prompts for open or save; false opens the report
	 * 						in the specified format after exporting.
	 * @throws ReportSDKExceptionBase
	 */
	public static void exportRTF(IReportSource reportSource, HttpServletRequest request, HttpServletResponse response, ServletContext  context, boolean attachment)throws ReportSDKExceptionBase{
		// RTF export allows page range export. The following routine ensures
		// that the requested page range is valid
		RTFWordExportFormatOptions  rtfOptions = new RTFWordExportFormatOptions();
		ExportOptions exportOptions = new ExportOptions();
		exportOptions.setExportFormatType(ReportExportFormat.RTF);		
		exportOptions.setFormatOptions(rtfOptions);

		ReportExportControl reportExportControl = new ReportExportControl();
		reportExportControl.setReportSource(reportSource);
		reportExportControl.setExportOptions(exportOptions);
		// ExportAsAtttachment(true) prompts for open or save; false opens the report
		// in the specified format after exporting.
		reportExportControl.setExportAsAttachment(attachment);
		// Export the report
		reportExportControl.processHttpRequest(request, response, context, null);

	}
	
	/**
	 * Exports a report to RTF for a range of pages
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param request		The HttpServletRequest object
	 * @param response		The HttpServletResponse object
	 * @param context		The ServletContext object
	 * @param startPage		Starting page
	 * @param endPage		Ending page.  
	 * @param attachment	true to prompts for open or save; false opens the report
	 * 						in the specified format after exporting.
	 * @throws ReportSDKExceptionBase
	 */
	public static void exportRTF(IReportSource reportSource, HttpServletRequest request, HttpServletResponse response, ServletContext  context, int startPage, int endPage,boolean attachment)throws ReportSDKExceptionBase{
		// RTF export allows page range export. The following routine ensures
		// that the requested page range is valid
		RTFWordExportFormatOptions  rtfOptions = new RTFWordExportFormatOptions();
		rtfOptions.setStartPageNumber(startPage);
		rtfOptions.setEndPageNumber(endPage);
		ExportOptions exportOptions = new ExportOptions();
		exportOptions.setExportFormatType(ReportExportFormat.RTF);		
		exportOptions.setFormatOptions(rtfOptions);

		ReportExportControl reportExportControl = new ReportExportControl();
		reportExportControl.setReportSource(reportSource);
		reportExportControl.setExportOptions(exportOptions);
		// ExportAsAtttachment(true) prompts for open or save; false opens the report
		// in the specified format after exporting.
		reportExportControl.setExportAsAttachment(attachment);
		// Export the report
		reportExportControl.processHttpRequest(request, response, context, null);

	}

	/**
	 * Exports a report to CSV
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param request		The HttpServletRequest object
	 * @param response		The HttpServletResponse object
	 * @param context		The ServletContext object
	 * @param attachment	true to prompts for open or save; false opens the report
	 * 						in the specified format after exporting.
	 * @throws ReportSDKExceptionBase
	 */
	public static void exportCSV(IReportSource reportSource, HttpServletRequest request, HttpServletResponse response, ServletContext  context, boolean attachment)throws ReportSDKExceptionBase {
		CharacterSeparatedValuesExportFormatOptions csvOptions = new CharacterSeparatedValuesExportFormatOptions();
		csvOptions.setSeparator(",");
		csvOptions.setDelimiter("\n");
		ExportOptions exportOptions = new ExportOptions();
		exportOptions.setExportFormatType(ReportExportFormat.characterSeparatedValues);		
		exportOptions.setFormatOptions(csvOptions);
		ReportExportControl reportExportControl = new ReportExportControl();
		reportExportControl.setReportSource(reportSource);
		reportExportControl.setExportOptions(exportOptions);
		// ExportAsAtttachment(true) prompts for open or save; false opens the report
		// in the specified format after exporting.
		reportExportControl.setExportAsAttachment(attachment);
		// Export the report
		reportExportControl.processHttpRequest(request, response, context, null);

	}
	
	/**
	 * Prints to the server printer
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param printerName	Name of printer used to print the report
	 * @throws ReportSDKPrinterException
	 */
	public static void printToServer(ReportClientDocument clientDoc,String printerName)throws ReportSDKPrinterException{
		PrintReportOptions printOptions = new PrintReportOptions();
		// Note: Printer with the <printer name> below must already be
		// configured.
		printOptions.setPrinterName(printerName);
		printOptions.setJobTitle("Sample Print Job from JRC.");
		printOptions.setPrinterDuplex(PrinterDuplex.useDefault);
		printOptions.setPaperSource(PaperSource.auto);
		printOptions.setPaperSize(PaperSize.paperLetter);
		printOptions.setNumberOfCopies(1);
		printOptions.setCollated(false);

		// Print report
		clientDoc.getPrintOutputController().printReport(printOptions);
	}
	
	/**
	 * Prints a range of pages to the server printer
	 * 
	 * @param clientDoc		The reportClientDocument representing the report being used
	 * @param printerName	Name of printer used to print the report
	 * @param startPage		Starting page
	 * @param endPage		Ending page.
	 * @throws ReportSDKPrinterException
	 */
	public static void printToServer(ReportClientDocument clientDoc,String printerName,int startPage, int endPage)throws ReportSDKPrinterException{
		PrintReportOptions printOptions = new PrintReportOptions();
		// Note: Printer with the <printer name> below must already be
		// configured.
		printOptions.setPrinterName(printerName);
		printOptions.setJobTitle("Sample Print Job from JRC.");
		printOptions.setPrinterDuplex(PrinterDuplex.useDefault);
		printOptions.setPaperSource(PaperSource.auto);
		printOptions.setPaperSize(PaperSize.paperLetter);
		printOptions.setNumberOfCopies(1);
		printOptions.setCollated(false);
		PrintReportOptions.PageRange printPageRange = new PrintReportOptions.PageRange(startPage,endPage);
		printOptions.addPrinterPageRange(printPageRange);

		// Print report
		clientDoc.getPrintOutputController().printReport(printOptions);
	}

}
