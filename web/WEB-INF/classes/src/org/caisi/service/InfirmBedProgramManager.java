/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.caisi.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.caisi.dao.BedProgramDao;
import org.oscarehr.common.dao.DemographicDao;
import org.caisi.dao.ProviderDefaultProgramDao;
import org.caisi.model.ProviderDefaultProgram;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.springframework.beans.factory.annotation.Required;

public class InfirmBedProgramManager {
    private BedProgramDao bedProgramDao;
    private DemographicDao demographicDaoT;
    private ProgramProviderDAO programProviderDAOT;
    private ProviderDefaultProgramDao providerDefaultProgramDao;
    private ProgramDao programDao;

    private static Logger logger = Logger.getLogger(InfirmBedProgramManager.class);

    @Required
    public void setBedProgramDao(BedProgramDao dao) {
        this.bedProgramDao = dao;
    }

    @Required
    public void setProgramDao(ProgramDao dao) {
        this.programDao = dao;
    }

    @Required
    public void setProviderDefaultProgramDao(ProviderDefaultProgramDao dao) {
        this.providerDefaultProgramDao = dao;
    }

    @Required
    public void setDemographicDao(DemographicDao dao) {
        this.demographicDaoT = dao;
    }

    public List getPrgramNameID() {
        List rs = bedProgramDao.getAllProgramNameID();

        return rs;
    }

    public List getPrgramName() {
        List rs = bedProgramDao.getAllProgramName();
        return rs;
    }

    public List getProgramBeans() {
        Iterator iter = bedProgramDao.getAllProgram().iterator();
        ArrayList pList = new ArrayList();
        while (iter.hasNext()) {
            Program p = (Program)iter.next();
            if (p != null) {
                //logger.debug("programName="+p.getName()+"::"+"programId="+p.getId().toString());
                pList.add(new LabelValueBean(p.getName(), p.getId().toString()));
            }
        }
        return pList;
    }

    public List getProgramBeans(String providerNo, Integer facilityId) {
        if (providerNo == null || "".equalsIgnoreCase(providerNo.trim())) return new ArrayList();
        Iterator iter = programProviderDAOT.getProgramProvidersByProvider(providerNo).iterator();
        ArrayList pList = new ArrayList();
        while (iter.hasNext()) {
            ProgramProvider p = (ProgramProvider)iter.next();
            if (p != null && p.getProgramId() != null && p.getProgramId().longValue() > 0) {
                //logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString());
                Program program = programDao.getProgram(new Integer(p.getProgramId().intValue()));
                
                if (facilityId!=null && program.getFacilityId()!=facilityId.intValue()) continue;
                
                if (program.getProgramStatus() != null && program.getProgramStatus().equals("active")) pList.add(new LabelValueBean(program.getName(), program.getId().toString()));
            }
        }
        return pList;
    }

    public List getDemographicByBedProgramIdBeans(int programId, Date dt, String archiveView) {
        /*default time is Oscar default null time 0001-01-01.*/
        Date defdt = new GregorianCalendar(1, 0, 1).getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        dt = cal.getTime();
        Iterator iter;

        //if (archiveView != null && archiveView.equals("true")) iter = demographicDaoT.getArchiveDemographicByPromgram(programId, dt, defdt).iterator();
        if (archiveView != null && archiveView.equals("true")) iter = demographicDaoT.getArchiveDemographicByProgramOptimized(programId, dt, defdt).iterator();        
        else iter = demographicDaoT.getActiveDemographicByProgram(programId, dt, defdt).iterator();

        ArrayList demographicList = new ArrayList();
        Demographic de = null;
        while (iter.hasNext()) {
            de = (Demographic)iter.next();
            //logger.info("demoName="+de.getLastName()+","+de.getFirstName()+"::"+"demoID="+de.getDemographicNo().toString());
            demographicList.add(new LabelValueBean(de.getLastName() + ", " + de.getFirstName(), de.getDemographicNo().toString()));

        }
        return demographicList;
    }

    public int getDefaultProgramId() {
        String defProgramName = "Annex";
        List rs = bedProgramDao.getProgramIdByName(defProgramName);
        if (rs.isEmpty()) return 1;
        else return ((Integer)rs.get(0)).intValue();

    }

    public int getDefaultProgramId(String providerNo) {
        int defProgramId = 0;
        List rs = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if (rs.isEmpty()) {
            //setDefaultProgramId(providerNo,defProgramId);
            return defProgramId;
        }
        else return ((ProviderDefaultProgram)rs.get(0)).getProgramId().intValue();

    }

    public void setDefaultProgramId(String providerNo, int programId) {
        providerDefaultProgramDao.setDefaultProgram(providerNo, programId);
    }

    public Boolean getProviderSig(String providerNo) {
        List list = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if (list.isEmpty()) {
            ProviderDefaultProgram pdp = new ProviderDefaultProgram();
            pdp.setProgramId(new Integer(0));
            pdp.setProviderNo(providerNo);
            pdp.setSignnote(false);
            providerDefaultProgramDao.saveProviderDefaultProgram(pdp);
            return(new Boolean(false));
        }
        ProviderDefaultProgram pro = (ProviderDefaultProgram)list.get(0);
        return new Boolean(pro.isSignnote());

    }

    public void toggleSig(String providerNo) {
        providerDefaultProgramDao.toggleSig(providerNo);

    }

    public ProgramProviderDAO getProgramProviderDAOT() {
        return programProviderDAOT;
    }

    public void setProgramProviderDAOT(ProgramProviderDAO programProviderDAOT) {
        this.programProviderDAOT = programProviderDAOT;
    }

    public String[] getProgramInformation(int programId) {
        return this.bedProgramDao.getProgramInfo(programId);
    }

}
