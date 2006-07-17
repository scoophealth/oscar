package org.caisi.PMmodule.service.impl;

import java.util.Date;
import java.util.List;

import org.caisi.PMmodule.dao.BedLogDAO;
import org.caisi.PMmodule.model.BedLog;
import org.caisi.PMmodule.model.BedLogSheet;
import org.caisi.PMmodule.service.BedLogManager;

public class BedLogManagerImpl implements BedLogManager {

	private BedLogDAO bedLogDAO;
	
	public void setBedLogDAO(BedLogDAO dao) {
		this.bedLogDAO = dao;
	}
	
	public void saveBedLog(BedLog bedlog) {
		this.bedLogDAO.saveBedLog(bedlog);
	}

	public List getBedLogsByProgram(String programId) {
		return this.bedLogDAO.getBedLogsByProgram(Long.valueOf(programId));
	}

	public List getBedLogsByClient(String demographicNo) {
		return this.bedLogDAO.getBedLogsByClient(Long.valueOf(demographicNo));
	}

	public List getBedLogs(String programId, Date day) {
		return this.bedLogDAO.getBedLogs(Long.valueOf(programId),day);
	}

	public BedLogSheet getLastSheet(String programId) {
		return this.bedLogDAO.getLastSheet(Long.valueOf(programId));
	}
	
	public List getBedLogsBySheet(Long sheetId) {
		return this.bedLogDAO.getBedLogsBySheet(sheetId);
	}

	public void saveBedLogSheet(BedLogSheet sheet) {
		this.bedLogDAO.saveBedLogSheet(sheet);
	}
	
	public List searchBedLogs(BedLog criteria) {
		return this.bedLogDAO.searchBedLogs(criteria);
	}
	
	public List getBedLogSheetsByProgram(String programId) {
		return this.bedLogDAO.getBedLogSheetsByProgram(Long.valueOf(programId));
	}
	
	public BedLogSheet getSheet(String sheetId) {
		return this.bedLogDAO.getSheet(Long.valueOf(sheetId));
	}
}
