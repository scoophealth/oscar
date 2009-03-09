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

import java.util.ArrayList;
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
        for (Drug drug : rs)
        {
        	PrescriptDrug prescriptDrug = new PrescriptDrug();
        	prescriptDrug.setLocalDrugId(drug.getId());
            prescriptDrug.setDate_prescribed(drug.getRxDate());
            prescriptDrug.setDrug_special(drug.getSpecial());
            prescriptDrug.setBN(drug.getBrandName());
            prescriptDrug.setGCN_SEQNO(drug.getGcnSeqNo());
            prescriptDrug.setCustomName(drug.getCustomName());
            prescriptDrug.setRegionalIdentifier(drug.getRegionalIdentifier());

            prescriptDrug.setEnd_date(drug.getEndDate());
            prescriptDrug.setDrug_achived(false);

            boolean b = true;
            for (int i = 0; i < rt.size(); i++) {
                PrescriptDrug p2 = (PrescriptDrug)rt.get(i);
                if (p2.getGCN_SEQNO().intValue() == prescriptDrug.getGCN_SEQNO().intValue()) {
                    if (p2.getGCN_SEQNO().intValue() != 0) // not custom - safe GCN
                    {
                        b = false;
                    }
                    else // custom
                    {
                        if (p2.getCustomName() != null && prescriptDrug.getCustomName() != null) {
                            if (p2.getCustomName().equals(prescriptDrug.getCustomName())) // same custom
                            {
                                b = false;
                            }
                        }
                    }
                }
            }
            if (b) rt.add(prescriptDrug);
        }
        return rt;

    }

	public List<PrescriptDrug> getPrescriptions(String demographic_no) {

    	List<Drug> rs=drugDao.findByDemographicIdOrderByDate(new Integer(demographic_no), null);
		List<PrescriptDrug> rt = new ArrayList<PrescriptDrug>();
        for (Drug drug : rs)
        {
			PrescriptDrug prescriptDrug = new PrescriptDrug();
			prescriptDrug.setLocalDrugId(drug.getId());
			prescriptDrug.setDate_prescribed(drug.getRxDate());
			prescriptDrug.setDrug_special(drug.getSpecial());
			prescriptDrug.setEnd_date(drug.getEndDate());
			prescriptDrug.setDrug_achived(drug.isArchived());
			prescriptDrug.setBN(drug.getBrandName());
			prescriptDrug.setGCN_SEQNO(drug.getGcnSeqNo());
			prescriptDrug.setCustomName(drug.getCustomName());
            prescriptDrug.setRegionalIdentifier(drug.getRegionalIdentifier());
			rt.add(prescriptDrug);
		}
		return rt;

	}
}
