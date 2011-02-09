package org.oscarehr.casemgmt.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.casemgmt.service.CaseManagementPrintPdf;

import com.lowagie.text.DocumentException;

public interface ExtPrint  {

	public void printExt(CaseManagementPrintPdf engine, HttpServletRequest request) throws IOException, DocumentException;
}
