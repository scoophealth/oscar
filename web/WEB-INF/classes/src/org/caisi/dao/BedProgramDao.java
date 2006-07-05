package org.caisi.dao;

import java.util.List;

public interface BedProgramDao {

	public List getAllBedProgram();
	public List getAllProgramNameID();
	public List getAllProgramName();
	public List getAllProgram();
	public List getProgramIdByName(String name);
}
