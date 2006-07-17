package org.caisi.PMmodule.dao;

import java.util.List;

import org.caisi.PMmodule.model.ProgramBedLog;

public interface ProgramBedLogDAO {
	public void saveBedLog(ProgramBedLog pbl);
	public ProgramBedLog getBedLog(Long id);
	public ProgramBedLog getBedLogByProgramId(Long programId);
	
	public List getBedLogStatuses(Long programId);
	public List getBedLogCheckTimes(Long programId);
}
