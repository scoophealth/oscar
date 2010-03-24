/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */
package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Drug;
import org.springframework.stereotype.Repository;

@Repository
public class DrugDao extends AbstractDao<Drug> {

	public DrugDao() {
		super(Drug.class);
	}

	/**
     * @param archived can be null for both archived and non archived entries
     */
    public List<Drug> findByDemographicIdOrderByDate(Integer demographicId, Boolean archived) {
        // build sql string
        String sqlCommand = "select x from Drug x where x.demographicId=?1 " + (archived == null ? "" : "and x.archived=?2") + " order by x.rxDate desc, x.id desc";

        // set parameters
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, demographicId);
        if (archived != null) {
            query.setParameter(2, archived);
        }
        // run query
        @SuppressWarnings("unchecked")
        List<Drug> results = query.getResultList();

        return (results);
    }

    public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, String regionalIdentifier, String customName) {
        // build sql string
        String sqlCommand = "select x from Drug x where x.demographicId=?1 and x." + (regionalIdentifier != null ? "regionalIdentifier" : "customName") + "=?2 order by x.rxDate desc, x.id desc";

        // set parameters
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, demographicId);
        if (regionalIdentifier != null) {
            query.setParameter(2, regionalIdentifier);
        } else {
            query.setParameter(2, customName);
        }
        // run query
        @SuppressWarnings("unchecked")
        List<Drug> results = query.getResultList();

        return (results);
    }

    public List<Drug> findByDemographicIdSimilarDrugOrderByDate(Integer demographicId, String regionalIdentifier, String customName, String brandName) {
        // build sql string
        String sqlCommand ="";
        if(regionalIdentifier!=null && !regionalIdentifier.equalsIgnoreCase("null") && regionalIdentifier.trim().length()!=0)
            sqlCommand="select x from Drug x where x.demographicId=?1 and x.regionalIdentifier=?2 order by x.rxDate desc, x.id desc";
        else if(customName!=null && !customName.equalsIgnoreCase("null") && customName.trim().length()!=0)
            sqlCommand="select x from Drug x where x.demographicId=?1 and x.customName=?2 order by x.rxDate desc, x.id desc";
        else
            sqlCommand="select x from Drug x where x.demographicId=?1 and x.brandName=?2 order by x.rxDate desc, x.id desc";
        System.out.println("sqlCommand="+sqlCommand);
        // set parameters
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, demographicId);
        if(regionalIdentifier!=null && !regionalIdentifier.equalsIgnoreCase("null") && regionalIdentifier.trim().length()!=0)
             query.setParameter(2, regionalIdentifier);
        else if(customName!=null && !customName.equalsIgnoreCase("null") && customName.trim().length()!=0)
              query.setParameter(2,customName);
        else
             query.setParameter(2,brandName);
        // run query
        @SuppressWarnings("unchecked")
        List<Drug> results = query.getResultList();

        return (results);
    }

    ///////
    public List<Drug> getUniquePrescriptions(String demographic_no) {
        //   System.out.println("===========IN getUniquePrescriptions======");
        //    System.out.println("demographic_no="+demographic_no);
        List<Drug> rs = findByDemographicIdOrderByDate(new Integer(demographic_no), false);

        List<Drug> rt = new ArrayList<Drug>();
        for (Drug drug : rs) {
            //Drug prescriptDrug = new PrescriptDrug();
            
            boolean b = true;
            for (int i = 0; i < rt.size(); i++) {
                Drug p2 = rt.get(i);
                if (p2.getGcnSeqNo() == drug.getGcnSeqNo()) {
                    //    System.out.println("p2.getGCN_SEQNO().intValue() == prescriptDrug.getGCN_SEQNO().intValue()="+p2.getGCN_SEQNO());
                    if (p2.getGcnSeqNo() != 0){ // not custom - safe GCN
                        //       System.out.println("p2.getGCN_SEQNO().intValue() != 0" );
                        b = false;
                    } else {// custom
                        //       System.out.println("p2.getGCN_SEQNO().intValue() = 0");
                        if (p2.getCustomName() != null && drug.getCustomName() != null) {
                            //          System.out.println("p2.getCustomName() != null && prescriptDrug.getCustomName() != null");
                            if (p2.getCustomName().equals(drug.getCustomName())){ // same custom
                                //           System.out.println("p2.getCustomName().equals(prescriptDrug.getCustomName())");
                                b = false;
                            }
                        }
                    }
                }
            }
            if (b) {
                rt.add(drug);
            }
        }
        //  System.out.println("===========END getUniquePrescriptions======");
        return rt;
    }

    public List<Drug> getPrescriptions(String demographic_no) {
        List<Drug> rs = findByDemographicIdOrderByDate(new Integer(demographic_no), null);       
        return rs;

    }

    public List<Drug> getPrescriptions(String demographic_no, boolean all) {
        if (all) {
                return getPrescriptions(demographic_no);
        }
        return getUniquePrescriptions(demographic_no);
    }

}
