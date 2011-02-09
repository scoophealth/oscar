package org.oscarehr.learning.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.learning.StudentImporter;
import org.oscarehr.learning.StudentInfo;
import org.oscarehr.util.MiscUtils;

import com.Ostermiller.util.ExcelCSVParser;

public class StudentImportAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();
	
	/*
	 * Import
	 * 
	 * Create student provider
	 * Create login
	 * Assign Role
	 * Student number in practitioner_no field
	 * 
	 * 
	 */
	public ActionForward doImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			return null;
		}
	
	public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		logger.info("upload student data");
    	StudentImportBean f = (StudentImportBean)form;
    	FormFile formFile = f.getFile();
    	String[][] data = ExcelCSVParser.parse(new InputStreamReader(formFile.getInputStream()));
    	
    	List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
    	
    	for(int x=0;x<data.length;x++) {
    		if(data[x].length != 6) {
    			logger.warn("skipping line..invalid number of fields");
    			continue;
    		}
    		String lastName = data[x][0];
    		String firstName = data[x][1];
    		String login = data[x][2];
    		String password = data[x][3];
    		String pin = data[x][4];
    		String studentNumber = data[x][5];
    	
    		StudentInfo studentInfo = new StudentInfo();
    		studentInfo.setLastName(lastName);
    		studentInfo.setFirstName(firstName);
    		studentInfo.setUsername(login);
    		studentInfo.setPassword(password);
    		studentInfo.setPin(pin);
    		studentInfo.setStudentNumber(studentNumber);
    		
    		studentInfoList.add(studentInfo);    		
    		logger.info("importing: " + lastName + "," + firstName + "," + login + "," + password + "," + pin +  "," + studentNumber);    		    		    	
    	}
    	
    	int recordsImported = StudentImporter.importStudentInfo(studentInfoList);
    	
    	
    	request.setAttribute("total_imported", recordsImported);
    
    	ActionForward forward = new ActionForward();
    	forward.setPath("/oscarLearning/StudentImport.jsp?r="+recordsImported);
    	forward.setRedirect(true);
		return forward;
	}
	
}