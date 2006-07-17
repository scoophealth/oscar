package org.caisi.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.caisi.PMmodule.model.BedLog;
import org.caisi.PMmodule.model.BedLogSheet;

public interface BedLogDAO {
	public void saveBedLog(BedLog bedlog);
	public List getBedLogsByProgram(Long programId);
	public List getBedLogsByClient(Long demographicNo);
	public List getBedLogs(Long programId, Date day);
	
	public BedLogSheet getLastSheet(Long programId);
	public List getBedLogsBySheet(Long sheetId);
	public void saveBedLogSheet(BedLogSheet sheet);
	
	public List searchBedLogs(BedLog criteria);
	public List getBedLogSheetsByProgram(Long programId);
	public BedLogSheet getSheet(Long sheetId);
}
