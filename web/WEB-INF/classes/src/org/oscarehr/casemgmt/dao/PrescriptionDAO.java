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

import java.util.List;

import org.oscarehr.casemgmt.model.Prescription;
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

	public List<Drug> getUniquePrescriptions(String demographic_no) {


		//List<Drug> rs=drugDao.findByDemographicIdOrderByDate(new Integer(demographic_no), false);
                List<Drug> rs=drugDao.getUniquePrescriptions(demographic_no);
        return rs;
    }

	public List<Drug> getPrescriptions(String demographic_no) {

    	List<Drug> rs=drugDao.findByDemographicIdOrderByDate(new Integer(demographic_no), null);
	return rs;
	}
}
