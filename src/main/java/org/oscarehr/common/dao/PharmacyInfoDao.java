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
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.PharmacyInfo;
import org.springframework.stereotype.Repository;

@Repository
public class PharmacyInfoDao extends AbstractDao<PharmacyInfo>{
	
	public PharmacyInfoDao() {
		super(PharmacyInfo.class);
	}

    synchronized public void addPharmacy(String name,String address,String city,String province,String postalCode, String phone1, String phone2, String fax, String email,String serviceLocationIdentifier, String notes) {
    	PharmacyInfo pharmacyInfo = new PharmacyInfo();
    	
    	pharmacyInfo.setName(name);
    	pharmacyInfo.setAddress(address);
    	pharmacyInfo.setCity(city);
    	pharmacyInfo.setProvince(province);
    	pharmacyInfo.setPostalCode(postalCode);
    	pharmacyInfo.setPhone1(phone1);
    	pharmacyInfo.setPhone2(phone2);
    	pharmacyInfo.setFax(fax);
    	pharmacyInfo.setEmail(email);
    	pharmacyInfo.setServiceLocationIdentifier(serviceLocationIdentifier);
    	pharmacyInfo.setNotes(notes);
    	pharmacyInfo.setStatus(PharmacyInfo.ACTIVE);
    	pharmacyInfo.setAddDate(new Date());
    	persist(pharmacyInfo);
    }

    public void updatePharmacy(Integer ID,String name,String address,String city,String province,String postalCode, String phone1, String phone2, String fax, String email, String serviceLocationIdentifier, String notes){
    	PharmacyInfo pharmacyInfo = new PharmacyInfo();
    	pharmacyInfo.setId(ID);
    	pharmacyInfo.setName(name);
    	pharmacyInfo.setAddress(address);
    	pharmacyInfo.setCity(city);
    	pharmacyInfo.setProvince(province);
    	pharmacyInfo.setPostalCode(postalCode);
    	pharmacyInfo.setPhone1(phone1);
    	pharmacyInfo.setPhone2(phone2);
    	pharmacyInfo.setFax(fax);
    	pharmacyInfo.setEmail(email);
    	pharmacyInfo.setServiceLocationIdentifier(serviceLocationIdentifier);
    	pharmacyInfo.setNotes(notes);
    	pharmacyInfo.setStatus(PharmacyInfo.ACTIVE);
    	pharmacyInfo.setAddDate(new Date());
    	merge(pharmacyInfo);
     }

    public void deletePharmacy(Integer ID){
          String sql = "update PharmacyInfo set status = ? where id = ?";
          Query query = entityManager.createQuery(sql);
          query.setParameter(1, PharmacyInfo.DELETED);
          query.setParameter(2, ID);
          query.executeUpdate();
     }
    
    @SuppressWarnings("unchecked")
    public List<PharmacyInfo> getPharmacies(List<Integer>idList) {
    	String sql = "select x from PharmacyInfo x where x.id in (:list)";
    	Query query = entityManager.createQuery(sql);
    	    	
    	query.setParameter("list",idList);
    	
    	return query.getResultList();
    }

    public PharmacyInfo getPharmacy(Integer ID){
    	return find(ID);
     }

    public PharmacyInfo getPharmacyByRecordID(Integer recordID){
    	String sql = "SELECT x FROM  PharmacyInfo x where x.id = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,recordID);
        @SuppressWarnings("unchecked")
        List<PharmacyInfo> results = query.getResultList();
        if(results.size()>0) {
      	  return results.get(0);
        }
        return null;
     }

    @SuppressWarnings("unchecked")
    public List<PharmacyInfo> getAllPharmacies(){
        List<PharmacyInfo>  pharmacyList =  new ArrayList<PharmacyInfo>();
        String sql = "select x from PharmacyInfo x where x.status = :status order by name";
        
        Query query = entityManager.createQuery(sql);
        query.setParameter("status", PharmacyInfo.ACTIVE);

        pharmacyList = query.getResultList();

        return pharmacyList;
     }
    
    @SuppressWarnings("unchecked")
    public List<PharmacyInfo> searchPharmacyByNameAddressCity(String name, String city ) {
    	
    	String sql = "select x from PharmacyInfo x where x.status = :status and (x.name like :name or x.address like :address) and x.city like :city order by x.name, x.address";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter("status", PharmacyInfo.ACTIVE);
    	query.setParameter("name", "%"+name+"%");
    	query.setParameter("address", "%"+name+"%");
    	query.setParameter("city", "%"+city+"%");
    	
    	return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<String> searchPharmacyByCity(String city ) {
    
    	String sql = "select distinct x.city from PharmacyInfo x where x.status = :status and x.city like :city";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter("status", PharmacyInfo.ACTIVE);
    	query.setParameter("city", "%"+city+"%");
    	
    	return query.getResultList();
    	
    }

}
