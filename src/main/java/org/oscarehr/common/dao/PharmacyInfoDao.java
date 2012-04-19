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
    	String sql = "select max(x.id2) from PharmacyInfo x";
    	Query query = entityManager.createQuery(sql);


        Integer id2 = (Integer)query.getSingleResult();
        if(id2 == null) {
        	id2=0;
        } else {
        	id2++;
        }
        String ID = Integer.toString(id2);

        updatePharmacy(ID,name,address,city,province,postalCode,phone1,phone2, fax,email, serviceLocationIdentifier, notes);
    }

    public void updatePharmacy(String ID,String name,String address,String city,String province,String postalCode, String phone1, String phone2, String fax, String email, String serviceLocationIdentifier, String notes){
    	PharmacyInfo pharmacyInfo = new PharmacyInfo();
    	pharmacyInfo.setId2(Integer.parseInt(ID));
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
    	pharmacyInfo.setStatus("1");
    	pharmacyInfo.setAddDate(new Date());
    	persist(pharmacyInfo);
     }

    public void deletePharmacy(String ID){
          String sql = "update PharmacyInfo set status = '0' where id2 = ?";
          Query query = entityManager.createQuery(sql);
          query.setParameter(1, Integer.parseInt(ID));
          query.executeUpdate();
     }

    public PharmacyInfo getPharmacy(String ID){
    	String sql = "SELECT x FROM  PharmacyInfo x where x.id2 = ? order by x.id desc";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,Integer.parseInt(ID));
        @SuppressWarnings("unchecked")
        List<PharmacyInfo> results = query.getResultList();
        if(results.size()>0) {
      	  return results.get(0);
        }
        return null;
     }

    public PharmacyInfo getPharmacyByRecordID(String recordID){
    	String sql = "SELECT x FROM  PharmacyInfo x where x.id = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,Integer.parseInt(recordID));
        @SuppressWarnings("unchecked")
        List<PharmacyInfo> results = query.getResultList();
        if(results.size()>0) {
      	  return results.get(0);
        }
        return null;
     }

    public List<PharmacyInfo> getAllPharmacies(){
        List<PharmacyInfo>  pharmacyList =  new ArrayList<PharmacyInfo>();
        String sql = "select max(id) as maxrec from PharmacyInfo where status = 1 group by id2 order by name";
        Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<Integer> ids = query.getResultList();

        for(Integer id:ids) {
        	pharmacyList.add(getPharmacyByRecordID(String.valueOf(id)));
        }

        return pharmacyList;
     }

}
