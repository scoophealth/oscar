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

package org.oscarehr.casemgmt.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.caisi.model.EChart;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EchartDAO extends HibernateDaoSupport {

    public void saveCPPIntoEchart(CaseManagementCPP cpp, String providerNo) {
        String demoNo = cpp.getDemographic_no();
        String sql = "from EChart e where e.demographicNo=? order by e.id";
        List list = getHibernateTemplate().find(sql, new Integer(demoNo));
        EChart ec;
        if (list.size() != 0) ec = (EChart)list.get(list.size() - 1);
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
        ec.setTimeStamp(now);
        getHibernateTemplate().save(ec);
    }

    public String saveEchart(CaseManagementNote note, CaseManagementCPP cpp, String userName, String lastStr) {
        String demoNo = note.getDemographic_no();
        String sql = "from EChart e where e.demographicNo=? order by e.id";
        List list = getHibernateTemplate().find(sql, new Integer(demoNo));
        EChart oldec;
        if (list.size() != 0) oldec = (EChart)list.get(list.size() - 1);
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
        ec.setTimeStamp(now);
        String etext = oldec.getEncounter();

        SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd");
        String rtStr = "\n-----------------------------------\n[" + dt.format(now) + "]\n" + note.getNote();

        //if echart have this note return
        String dupliString = note.getNote();
        if (dupliString.lastIndexOf("[[") >= 0) {
            dupliString = dupliString.substring(0, dupliString.lastIndexOf("[["));
        }
        if (etext!=null && etext.lastIndexOf(dupliString) >= 0) return rtStr;
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

        getHibernateTemplate().save(ec);
        return rtStr;

    }

    public void updateEchartOngoing(CaseManagementCPP cpp) {
        String demoNo = cpp.getDemographic_no();
        String sql = "from EChart e where e.demographicNo=? order by e.id";
        List list = getHibernateTemplate().find(sql, new Integer(demoNo));
        EChart oldec;
        if (list.size() != 0) oldec = (EChart)list.get(list.size() - 1);
        else {
            return;
        }
        oldec.setOngoingConcerns(cpp.getOngoingConcerns());
        getHibernateTemplate().save(oldec);
    }

}
