package org.caisi.PMmodule.service;

import java.util.Date;
import java.util.List;

import org.caisi.PMmodule.model.BedLog;
import org.caisi.PMmodule.model.BedLogSheet;

public interface BedLogManager {
	public void saveBedLog(BedLog bedlog);
	public List getBedLogsByProgram(String programId);
	public List getBedLogsByClient(String demographicNo);
	public List getBedLogs(String programId, Date day);
	
	public BedLogSheet getLastSheet(String programId);
	public BedLogSheet getSheet(String sheetId);
	public List getBedLogSheetsByProgram(String programId);
	public List getBedLogsBySheet(Long sheetId);
	public void saveBedLogSheet(BedLogSheet sheet);
	
	public List searchBedLogs(BedLog criteria);
}
