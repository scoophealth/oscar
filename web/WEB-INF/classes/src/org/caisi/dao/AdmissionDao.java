package org.caisi.dao;

import java.util.Date;
import java.util.List;

public interface AdmissionDao
{
	public List getClientIdByProgramDate(int programId,Date dt);
}

