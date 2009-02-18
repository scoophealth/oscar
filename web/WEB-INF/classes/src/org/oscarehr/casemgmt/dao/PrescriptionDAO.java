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

package org.oscarehr.casemgmt.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.oscarehr.casemgmt.model.Prescription;
import org.oscarehr.casemgmt.web.PrescriptDrug;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PrescriptionDAO extends HibernateDaoSupport {

	private DrugDao drugDao = null;
	
	public void setDrugDao(DrugDao drugDao) {
    	this.drugDao = drugDao;
    }

	public Prescription getPrescription(Long id) {
		return (Prescription) this.getHibernateTemplate().get(Prescription.class, id);
	}

	public List<PrescriptDrug> getUniquePrescriptions(String demographic_no) {

		List<Drug> rs=drugDao.findByDemographicIdOrderByDate(new Integer(demographic_no), false);
    	List<PrescriptDrug> rt = new ArrayList<PrescriptDrug>();
        for (Drug ob : rs)
        {
        	PrescriptDrug pd = new PrescriptDrug();
            pd.setDate_prescribed(ob.getRxDate());
            pd.setDrug_special(ob.getSpecial());
            pd.setBN(ob.getBn());
            pd.setGCN_SEQNO(ob.getGcnSeqNo());
            pd.setCustomName(ob.getCustomName());

            pd.setEnd_date(ob.getEndDate());
            pd.setDrug_achived(false);
            Calendar calendar = new GregorianCalendar();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            Timestamp today = new Timestamp(calendar.getTimeInMillis());
            today.setNanos(0);
            if (today.compareTo(ob.getEndDate()) <= 0) pd.setExpired(false);
            else pd.setExpired(true);

            boolean b = true;
            for (int i = 0; i < rt.size(); i++) {
                PrescriptDrug p2 = (PrescriptDrug)rt.get(i);
                if (p2.getGCN_SEQNO().intValue() == pd.getGCN_SEQNO().intValue()) {
                    if (p2.getGCN_SEQNO().intValue() != 0) // not custom - safe GCN
                    {
                        b = false;
                    }
                    else // custom
                    {
                        if (p2.getCustomName() != null && pd.getCustomName() != null) {
                            if (p2.getCustomName().equals(pd.getCustomName())) // same custom
                            {
                                b = false;
                            }
                        }
                    }
                }
            }
            if (b) rt.add(pd);
        }
        return rt;

    }

	public List<PrescriptDrug> getPrescriptions(String demographic_no) {

    	List<Drug> rs=drugDao.findByDemographicIdOrderByDate(new Integer(demographic_no), null);
		List<PrescriptDrug> rt = new ArrayList<PrescriptDrug>();
        for (Drug ob : rs)
        {
			PrescriptDrug pd = new PrescriptDrug();
			pd.setDate_prescribed(ob.getRxDate());
			pd.setDrug_special(ob.getSpecial());
			pd.setEnd_date(ob.getEndDate());
			pd.setDrug_achived(ob.isArchived());
			pd.setBN(ob.getBn());
			pd.setGCN_SEQNO(ob.getGcnSeqNo());
			pd.setCustomName(ob.getCustomName());

			Calendar calendar = new GregorianCalendar();
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			Timestamp today = new Timestamp(calendar.getTimeInMillis());
			today.setNanos(0);
			if (today.compareTo(ob.getEndDate()) <= 0) pd.setExpired(false);
			else pd.setExpired(true);
			rt.add(pd);
		}
		return rt;

	}

}
