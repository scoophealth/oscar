/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.model.EChart;
import org.springframework.stereotype.Repository;

@Repository
public class EChartDao extends AbstractDao<EChart>{

	
	public EChartDao() {
		super(EChart.class);
	}
	
    public EChart getLatestChart(int demographicNo) {
        
    	Query q = entityManager.createQuery("from EChart c where c.demographicNo = ? order by c.timestamp desc");
    	q.setParameter(1, demographicNo);
    	@SuppressWarnings("unchecked")
    	List<EChart> results = 	q.getResultList();
        if(results.size()>0) {
                return results.get(0);
        }
        return null;
    }

    public String saveEchart(CaseManagementNote note, CaseManagementCPP cpp, String userName, String lastStr) {
        String demoNo = note.getDemographic_no();
        String sql = "select e from EChart e where e.demographicNo=? order by e.id";
        Query q = entityManager.createQuery(sql);
        q.setParameter(1, new Integer(demoNo));
        @SuppressWarnings("unchecked")
        List<EChart> list =q.getResultList();
        EChart oldec;
        if (list.size() != 0) oldec =list.get(list.size() - 1);
        else {
            oldec = new EChart();
            oldec.setEncounter("");
        }

        EChart ec = new EChart();
        ec.setDemographicNo(new Integer(demoNo).intValue());
        ec.setProviderNo(note.getProviderNo());
        ec.setSubject("");

        if( cpp != null ) {
            ec.setFamilyHistory(cpp.getFamilyHistory());
            ec.setMedicalHistory(cpp.getMedicalHistory());
            ec.setOngoingConcerns(cpp.getOngoingConcerns());
            ec.setReminders(cpp.getReminders());
            ec.setSocialHistory(cpp.getSocialHistory());
        }
        
        Date now = new Date();
        ec.setTimestamp(now);
        String etext = oldec.getEncounter();

        SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd");
        String rtStr = "\n-----------------------------------\n[" + dt.format(now) + "]\n" + note.getNote();

        //if echart have this note return
        String dupliString = note.getNote();
        if (dupliString.lastIndexOf("[[") >= 0) {
            dupliString = dupliString.substring(0, dupliString.lastIndexOf("[["));
        }
        if (etext!=null && etext.lastIndexOf(dupliString) >= 0) 
        	return rtStr;
        
        //if old ecounter text>6000, auto split
        if (etext!=null && etext.length() > 6000) {
            etext = etext.substring(etext.length() - 5120) + "\n------------------------------------\n$$CAISI AUTO SPLIT CHART$$\n";
            ec.setSubject("SPLIT CHART");
        }
        /*remove the duplicate String from save button*/
        if (etext!=null && lastStr != null && etext.lastIndexOf(lastStr) >= 0) {
            String begetext = etext.substring(0, etext.lastIndexOf(lastStr));
            String endetext = etext.substring(etext.lastIndexOf(lastStr) + lastStr.length());
            etext = begetext + endetext;
        }

        etext = etext + rtStr;
        /*
         * String rolename=pcr.getCaisirole().getName(); if (rolename==null)
         * rolename=""; dt=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); if
         * (note.getSigning_provider_no()!=null ||
         * !note.getSigning_provider_no().equals("")) etext=etext+"\n[Signed on
         * "+dt.format(now)+" "+"Signed by "+userName+" ]\n";
         */

        ec.setEncounter(etext);

        persist(ec);
        return rtStr;
    }
    
    public void updateEchartOngoing(CaseManagementCPP cpp) {
        String demoNo = cpp.getDemographic_no();
        String sql = "select e from EChart e where e.demographicNo=? order by e.id";
        Query q = entityManager.createQuery(sql);
        q.setParameter(1,  new Integer(demoNo));
        List<EChart> list = q.getResultList();
        EChart oldec;
        if (list.size() != 0) oldec = list.get(list.size() - 1);
        else {
            return;
        }
        oldec.setOngoingConcerns(cpp.getOngoingConcerns());
       persist(oldec);

    }
    
    public void saveCPPIntoEchart(CaseManagementCPP cpp, String providerNo) {
    	
        String demoNo = cpp.getDemographic_no();
        String sql = "select e from EChart e where e.demographicNo=? order by e.id";
        Query q= entityManager.createQuery(sql);
        q.setParameter(1, Integer.parseInt(demoNo));
        @SuppressWarnings("unchecked")
        List<EChart> list = q.getResultList();
        
        EChart ec;
        if (list.size() != 0)
        	ec = list.get(list.size() - 1);
        else {
            ec = new EChart();
            ec.setDemographicNo(new Integer(demoNo).intValue());
            ec.setProviderNo(providerNo);
            ec.setEncounter("");
        }

        ec.setFamilyHistory(cpp.getFamilyHistory());
        ec.setMedicalHistory(cpp.getMedicalHistory());
        ec.setOngoingConcerns(cpp.getOngoingConcerns());
        ec.setReminders(cpp.getReminders());
        ec.setSocialHistory(cpp.getSocialHistory());

        Date now = new Date();
        ec.setTimestamp(now);
        persist(ec);

        
    }

	public Integer getMaxIdForDemographic(Integer demoNo) {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM EChart e WHERE e.demographicNo = :demoNo");
		query.setParameter("demoNo", demoNo);
		return (Integer) query.getSingleResult();	    
    }

	@SuppressWarnings("unchecked")
    public List<EChart> getChartsForDemographic(Integer demoNo) {
		Query query = createQuery("e", "e.demographicNo = :dNo ORDER BY e.id DESC");
		query.setParameter("dNo", demoNo);
		return query.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<EChart> findByDemoIdAndSubject(Integer demoNo, String subj) {
		Query query = createQuery("e", "e.demographicNo = :demoNo and e.subject = :subj ORDER BY e.timestamp");
		query.setParameter("demoNo", demoNo);
		query.setParameter("subj", subj);
		return query.getResultList();
    }
	
}

