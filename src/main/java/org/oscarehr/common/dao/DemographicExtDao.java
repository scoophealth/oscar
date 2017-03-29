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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicExtDao extends AbstractDao<DemographicExt>{

	public DemographicExtDao() {
		super(DemographicExt.class);
	}

 	public DemographicExt getDemographicExt(Integer id) {
 		return find(id);
 	}

     public List<DemographicExt> getDemographicExtByDemographicNo(Integer demographicNo) {

 		if (demographicNo == null || demographicNo.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		Query query = entityManager.createQuery("SELECT d from DemographicExt d where d.demographicNo=? order by d.dateCreated");
 		query.setParameter(1, demographicNo);

 	    @SuppressWarnings("unchecked")
 		List<DemographicExt> results = query.getResultList();

 		return results;
 	}

 	public DemographicExt getDemographicExt(Integer demographicNo, String key) {

 		if (demographicNo == null || demographicNo.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		if (key == null || key.length() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		Query query = entityManager.createQuery("SELECT d from DemographicExt d where d.demographicNo=? and d.key = ? order by d.dateCreated DESC");
 		query.setParameter(1, demographicNo);
 		query.setParameter(2, key);

 	    @SuppressWarnings("unchecked")
 		List<DemographicExt> results = query.getResultList();

 	    if (results.isEmpty()) return null;
 		DemographicExt result = results.get(0);

 		return result;
 	}

 	@SuppressWarnings("unchecked")
	public List<DemographicExt> searchDemographicExtByKeyAndValue( String key, String value ) { 

 		List<DemographicExt> results = null;		
 		Query query;
 		
 		if( key != null && ! key.isEmpty() ) {
 			
 			key = key.trim();
 			value = value.trim() + "%"; 
 			query = entityManager.createQuery("SELECT d from DemographicExt d where d.key = ? and d.value LIKE ?");
 	 		query.setParameter(1, key);
 	 		query.setParameter(2, value);

 	 		results = query.getResultList();
 		}

 	    return results;
 	}
 	
 	public List<DemographicExt> getDemographicExtByKeyAndValue(String key,String value) {

 		Query query = entityManager.createQuery("SELECT d from DemographicExt d where d.key = ? and d.value=? order by d.dateCreated DESC");
 		query.setParameter(1, key);
 		query.setParameter(2, value);

 	    @SuppressWarnings("unchecked")
 		List<DemographicExt> results = query.getResultList();

 	    return results;
 	}
 	
 	
 	public DemographicExt getLatestDemographicExt(Integer demographicNo, String key) {

 		if (demographicNo == null || demographicNo.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		if (key == null || key.length() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		Query query = entityManager.createQuery("SELECT d from DemographicExt d where d.demographicNo=? and d.key = ? order by d.dateCreated DESC, d.id DESC");
 		query.setParameter(1, demographicNo);
 		query.setParameter(2, key);

 	    @SuppressWarnings("unchecked")
 		List<DemographicExt> results = query.getResultList();

  		if (results.isEmpty()) return null;
 		DemographicExt result = results.get(0);

 		return result;
 	}

 	public void updateDemographicExt(DemographicExt de) {

 		if (de == null) {
 			throw new IllegalArgumentException();
 		}

 		merge(de);
 	}

 	public void saveDemographicExt(Integer demographicNo, String key, String value) {

 		if (demographicNo == null || demographicNo.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		if (key == null || key.length() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		if (value == null) {
 			return;
 		}

 		DemographicExt existingDe = this.getDemographicExt(demographicNo, key);

 		if (existingDe != null) {
 			existingDe.setDateCreated(new Date());
 			existingDe.setValue(value);
 			merge(existingDe);
 		}
 		else {
 			DemographicExt de = new DemographicExt();
 			de.setDateCreated(new Date());
 			de.setDemographicNo(demographicNo);
 			de.setHidden(false);
 			de.setKey(key);
 			de.setValue(value);
 			persist(de);
 		}

 	}

 	public void removeDemographicExt(Integer id) {
 		if (id == null || id.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		remove(id);
 	}

 	public void removeDemographicExt(Integer demographicNo, String key) {

 		if (demographicNo == null || demographicNo.intValue() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		if (key == null || key.length() <= 0) {
 			throw new IllegalArgumentException();
 		}

 		DemographicExt tmp = getDemographicExt(demographicNo, key);
 		if(tmp != null) {
 			remove(tmp.getId());
 		}
 	}

    public Map<String,String> getAllValuesForDemo(Integer demo){
    	Map<String,String> retval =  new HashMap<String,String>();
    	Query query = entityManager.createQuery("SELECT d from DemographicExt d where d.demographicNo=? order by d.dateCreated");
 		query.setParameter(1, demo);

 		@SuppressWarnings("unchecked")
        List<DemographicExt> demographicExts = query.getResultList();
 		for(DemographicExt demographicExt:demographicExts) {
			retval.put(demographicExt.getKey(), demographicExt.getValue());
			retval.put(demographicExt.getKey() + "_id", demographicExt.getId().toString());
 		}

        return retval;

     }

    /**
     * This Method is used to add a key value pair for a patient
     * @param providerNo providers Number entering the key value pair
     * @param demo Demographic number of the patient that the  key/value  pair is for
     * @param value The value for this key
     */
    public void addKey(String providerNo, Integer demo,String key, String value){
    	DemographicExt demographicExt = new DemographicExt();
    	demographicExt.setProviderNo(providerNo);
    	demographicExt.setDemographicNo(demo);
    	demographicExt.setKey(key);
    	demographicExt.setValue(value);
    	demographicExt.setDateCreated(new java.util.Date());
    	persist(demographicExt);
    }


    public void addKey(String providerNo, Integer demo,String key, String newValue,String oldValue){
       if ( oldValue == null ){
    	   oldValue = "";
       }
       if (newValue != null && !oldValue.equalsIgnoreCase(newValue)){
			DemographicExt demographicExt = new DemographicExt();
			demographicExt.setProviderNo(providerNo);
			demographicExt.setDemographicNo(demo);
			demographicExt.setKey(key);
			demographicExt.setValue(newValue);
			demographicExt.setDateCreated(new java.util.Date());
			persist(demographicExt);

       }
    }

    List<String[]> hashtable2ArrayList(Map<String,String> h){
        Iterator<String> e= h.keySet().iterator();
        List<String[]> arr = new ArrayList<String[]>();
        while(e.hasNext()){
           String key = e.next();
           String val = h.get(key);
           String[] sArr = new String[] {key,val};
           arr.add(sArr);
        }

        return  arr;
     }

     public List<String[]> getListOfValuesForDemo(Integer demo){
        return hashtable2ArrayList(getAllValuesForDemo(demo));
     }

     public String getValueForDemoKey(Integer demo, String key){
    	 DemographicExt ext = this.getDemographicExt(demo, key);
    	 if(ext != null) {
    		 return ext.getValue();
    	 }
    	 return null;
     }
     
	 public List<Integer> findDemographicIdsByKeyVal(String key, String val) {
	 		Query query = entityManager.createQuery("SELECT distinct d.demographicNo from DemographicExt d where d.key=? and d.value=?");
	 		query.setParameter(1, key);
	 		query.setParameter(2, val);
	 		
	 		@SuppressWarnings("unchecked")
	 		List<Integer> results = query.getResultList();

	 		return results;
	 	 }
	 
	 
		@SuppressWarnings("unchecked")
		public List<Demographic> searchDemographicByPhoneAndStatus(String phoneStr, List<String> statuses, int limit, int offset, String providerNo, boolean outOfDomain,boolean ignoreStatuses) {
			String queryString = "SELECT d from DemographicExt e,Demographic d where d.DemographicNo = e.demographicNo and e.key = ? and e.value like ?";

			if(statuses != null) {
				queryString += " and d.PatientStatus " + ((ignoreStatuses)?"not":"") + "  in (:statuses)";
			}
			 
			
			if(providerNo != null && !outOfDomain) {
				queryString += " AND d.id IN ("+ DemographicDao.PROGRAM_DOMAIN_RESTRICTION+") ";
			}
			
			Query query = entityManager.createQuery(queryString);
	 		query.setParameter(1, "demo_cell");
	 		query.setParameter(2, phoneStr + "%");

	 		query.setFirstResult(offset);
	 		query.setMaxResults(limit);
	 		
	 		if(statuses != null) {
				query.setParameter("statuses", statuses);
			}

			if(providerNo != null && !outOfDomain) {
				query.setParameter("providerNo", providerNo);
			}
			
	 	    List<Demographic> results = query.getResultList();
	 	    
			return results;
		}
}
