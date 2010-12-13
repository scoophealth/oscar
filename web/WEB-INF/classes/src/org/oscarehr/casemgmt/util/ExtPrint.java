package org.oscarehr.casemgmt.util;

import java.io.IOException;

import org.oscarehr.casemgmt.service.CaseManagementPrintPdf;

import com.lowagie.text.DocumentException;

public interface ExtPrint  {

	public void printExt(CaseManagementPrintPdf engine) throws IOException, DocumentException;
}
