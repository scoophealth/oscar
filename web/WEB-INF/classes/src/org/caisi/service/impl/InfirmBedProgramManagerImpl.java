package org.caisi.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.caisi.dao.AdmissionDao;
import org.caisi.dao.BedProgramDao;
import org.caisi.dao.DemographicDAO;
import org.caisi.dao.ProgramProviderDAO;
import org.caisi.dao.ProviderDefaultProgramDao;
import org.caisi.dao.ProviderRoleProgramDao;
import org.caisi.model.Demographic;
import org.caisi.model.Program;
import org.caisi.model.ProgramProvider;
import org.caisi.model.ProviderDefaultProgram;
import org.caisi.model.ProviderRoleProgram;
import org.caisi.service.InfirmBedProgramManager;

public class InfirmBedProgramManagerImpl implements InfirmBedProgramManager{
	private BedProgramDao bedProgramDao;
	private AdmissionDao admissionDao;
	private DemographicDAO demographicDAOT;
	private ProgramProviderDAO programProviderDAOT;
	//private ProviderRoleProgramDao providerRoleProgramDao;
	private ProviderDefaultProgramDao providerDefaultProgramDao;
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(InfirmBedProgramManagerImpl.class);
	
	public void setBedProgramDao(BedProgramDao dao)
	{
		this.bedProgramDao=dao;
	}
	
	public void setProviderDefaultProgramDao(ProviderDefaultProgramDao dao)
	{
		this.providerDefaultProgramDao=dao;
	}
	
	public void setAdmissionDao(AdmissionDao dao)
	{
		this.admissionDao=dao;
	}
	
	public void setDemographicDao(DemographicDAO dao)
	{
		this.demographicDAOT=dao;
	}
	
	public List getPrgramNameID()
	{
		List rs=bedProgramDao.getAllProgramNameID();
		
		return rs;
	}
	
	public List getPrgramName()
	{	
		List rs=bedProgramDao.getAllProgramName();
		return rs;
	}
	
	public List getProgramBeans() {
		Iterator iter = bedProgramDao.getAllProgram().iterator();
		ArrayList pList = new ArrayList();
		while (iter.hasNext())
		{
			Program p = (Program) iter.next();
			if (p!=null){
				logger.debug("programName="+p.getName()+"::"+"programId="+p.getId().toString());
				pList.add(new LabelValueBean(p.getName(),p.getId().toString()));
			}
		}
		return pList;
	}
	
	public List getProgramBeans(String providerNo) {
		if (providerNo==null||"".equalsIgnoreCase(providerNo.trim())) return new ArrayList();
		Iterator iter = programProviderDAOT.getProgramProvidersByProvider(new Long(providerNo)).iterator();
		ArrayList pList = new ArrayList();
		while (iter.hasNext())
		{
			ProgramProvider p = (ProgramProvider) iter.next();
			if (p!=null && p.getProgram()!=null){
				logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString());
				pList.add(new LabelValueBean(p.getProgram().getName(),p.getProgram().getId().toString()));
			}
		}
		return pList;
	}
	
	public List getDemographicByBedProgramIdBeans(int programId,Date dt)
	{
		/*default time is Oscar default null time 0001-01-01.*/
		Date defdt=new GregorianCalendar(1,0,1,23,59,59).getTime();
		Iterator iter=demographicDAOT.getActiveDemographicByProgram(programId,dt,defdt).iterator();
		ArrayList demographicList=new ArrayList();
		Demographic de=null;
		while (iter.hasNext())
		{
			de=(Demographic)iter.next();
			logger.info("demoName="+de.getLastName()+","+de.getFirstName()+"::"+"demoID="+de.getDemographicNo().toString());
			demographicList.add(new LabelValueBean(de.getLastName()+", "+de.getFirstName(),de.getDemographicNo().toString()));
			
		}
		return demographicList;
	}

	public int getDefaultProgramId()
	{
		String defProgramName="Annex";
		List rs=bedProgramDao.getProgramIdByName(defProgramName);
		if (rs.isEmpty()) return 1;
		else return ((Integer) rs.get(0)).intValue();
		
	}
	public int getDefaultProgramId(String providerNo)
	{
		int defProgramId=0;
		List rs=providerDefaultProgramDao.getProgramByProviderNo(providerNo);
		if (rs.isEmpty()) {
			//setDefaultProgramId(providerNo,defProgramId);
			return defProgramId;
		}
		else return ((ProviderDefaultProgram) rs.get(0)).getProgramId().intValue();
		
	}
	public void setDefaultProgramId(String providerNo, int programId)
	{
		providerDefaultProgramDao.setDefaultProgram(providerNo,programId);
	}

	public Boolean getProviderSig(String providerNo)
	{
		List list=providerDefaultProgramDao.getProgramByProviderNo(providerNo);
		if (list.isEmpty()){
			ProviderDefaultProgram pdp=new ProviderDefaultProgram();
			pdp.setProgramId(new Integer(0));
			pdp.setProviderNo(providerNo);
			pdp.setSignnote(false);
			providerDefaultProgramDao.saveProviderDefaultProgram(pdp);
			return (new Boolean(false));
		}
		ProviderDefaultProgram pro=(ProviderDefaultProgram) list.get(0);
		return new Boolean(pro.isSignnote());
	
	}

	public void toggleSig(String providerNo)
	{
		providerDefaultProgramDao.toggleSig(providerNo);
		
	}

	public ProgramProviderDAO getProgramProviderDAOT()
	{
		return programProviderDAOT;
	}

	public void setProgramProviderDAOT(ProgramProviderDAO programProviderDAOT)
	{
		this.programProviderDAOT = programProviderDAOT;
	}

	
}

