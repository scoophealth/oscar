/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
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

import org.apache.struts.util.LabelValueBean;
import org.caisi.dao.BedProgramDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class InfirmBedProgramManager {
    private BedProgramDao bedProgramDao;
    private DemographicDao demographicDao;
    private ProgramProviderDAO programProviderDAOT;
    private ProviderDefaultProgramDao providerDefaultProgramDao ;

    private ProgramDao programDao;

    @Required
    public void setProviderDefaultProgramDao(ProviderDefaultProgramDao providerDefaultProgramDao) {
    	this.providerDefaultProgramDao = providerDefaultProgramDao;
    }

    @Required
    public void setBedProgramDao(BedProgramDao dao) {
        this.bedProgramDao = dao;
    }

    @Required
    public void setProgramDao(ProgramDao dao) {
        this.programDao = dao;
    }

    @Required
    public void setDemographicDao(DemographicDao dao) {
        this.demographicDao = dao;
    }

    public List getPrgramNameID() {
        List rs = bedProgramDao.getAllProgramNameID();

        return rs;
    }

    public List getPrgramName() {
        List rs = bedProgramDao.getAllProgramName();
        return rs;
    }

    public List<LabelValueBean> getProgramBeans() {
        Iterator iter = bedProgramDao.getAllProgram().iterator();
        ArrayList<LabelValueBean> pList = new ArrayList<LabelValueBean>();
        while (iter.hasNext()) {
            Program p = (Program)iter.next();
            if (p != null) {
                //logger.debug("programName="+p.getName()+"::"+"programId="+p.getId().toString());
                pList.add(new LabelValueBean(p.getName(), p.getId().toString()));
            }
        }
        return pList;
    }

    public List<LabelValueBean> getProgramBeans(String providerNo, Integer facilityId) {
        if (providerNo == null || "".equalsIgnoreCase(providerNo.trim())) return new ArrayList<LabelValueBean>();
        Iterator iter = programProviderDAOT.getProgramProvidersByProvider(providerNo).iterator();
        ArrayList<LabelValueBean> pList = new ArrayList<LabelValueBean>();
        while (iter.hasNext()) {
            ProgramProvider p = (ProgramProvider)iter.next();
            if (p != null && p.getProgramId() != null && p.getProgramId().longValue() > 0) {
                //logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString());
                Program program = programDao.getProgram(new Integer(p.getProgramId().intValue()));

                if (facilityId!=null && program.getFacilityId()!=facilityId.intValue()) continue;

                if (program != null && program.isActive()) pList.add(new LabelValueBean(program.getName(), program.getId().toString()));
            }
        }
        return pList;
    }
    
    public List<LabelValueBean> getProgramBeansByFacilityId(Integer facilityId) {
    	if (facilityId <= 0) return new ArrayList<LabelValueBean>();
    	Iterator<Program> iter = providerDefaultProgramDao.findProgramsByFacilityId(facilityId).iterator();
    	ArrayList<LabelValueBean> pList = new ArrayList<LabelValueBean>();
    	while (iter.hasNext()) {
    		Program p = iter.next();
    		if (p != null && p.getId() != null && p.getName() != null && p.getName().length() > 0) {
    			pList.add(new LabelValueBean(p.getName(), p.getId().toString()));
    		}
    	}
		return pList;
    }

    public List getProgramForApptViewBeans(String providerNo, Integer facilityId) {
        if (providerNo == null || "".equalsIgnoreCase(providerNo.trim())) return new ArrayList<LabelValueBean>();
        Iterator iter = programProviderDAOT.getProgramProvidersByProvider(providerNo).iterator();
        ArrayList<LabelValueBean> pList = new ArrayList<LabelValueBean>();
        while (iter.hasNext()) {
            ProgramProvider p = (ProgramProvider)iter.next();
            if (p != null && p.getProgramId() != null && p.getProgramId().longValue() > 0) {
                //logger.debug("programName="+p.getProgram().getName()+"::"+"programId="+p.getProgram().getId().toString());
                Program program = programDao.getProgramForApptView(new Integer(p.getProgramId().intValue()));
                if(program==null) continue;
                if (facilityId!=null && program.getFacilityId()!=facilityId.intValue()) continue;

                if (program.isActive()) pList.add(new LabelValueBean(program.getName(), program.getId().toString()));
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
        Iterator<Demographic> iter;

        if (archiveView != null && archiveView.equals("true")) iter = demographicDao.getArchiveDemographicByProgramOptimized(programId, dt, defdt).iterator();
        else iter = demographicDao.getActiveDemographicByProgram(programId, dt, defdt).iterator();

        ArrayList<LabelValueBean> demographicList = new ArrayList<LabelValueBean>();
        Demographic de = null;
        while (iter.hasNext()) {
            de = iter.next();
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
        List<ProviderDefaultProgram> rs = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if (rs.isEmpty()) {
            //setDefaultProgramId(providerNo,defProgramId);
            return defProgramId;
        }
        else return rs.get(0).getProgramId();

    }

    public void setDefaultProgramId(String providerNo, int programId) {
        providerDefaultProgramDao.setDefaultProgram(providerNo, programId);
    }

    public Boolean getProviderSig(String providerNo) {
        List<ProviderDefaultProgram> list = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if (list.isEmpty()) {
            ProviderDefaultProgram pdp = new ProviderDefaultProgram();
            pdp.setProgramId(new Integer(0));
            pdp.setProviderNo(providerNo);
            pdp.setSign(false);
            providerDefaultProgramDao.saveProviderDefaultProgram(pdp);
            return(new Boolean(false));
        }
        ProviderDefaultProgram pro = list.get(0);
        return new Boolean(pro.isSign());

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
