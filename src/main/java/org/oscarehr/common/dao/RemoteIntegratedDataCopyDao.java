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

import java.security.MessageDigest;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.net.util.Base64;
import org.oscarehr.common.model.RemoteIntegratedDataCopy;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.ObjectMarshalUtil;
import org.springframework.stereotype.Repository;


@Repository
public class RemoteIntegratedDataCopyDao extends AbstractDao<RemoteIntegratedDataCopy> {

	public RemoteIntegratedDataCopyDao() {
		super(RemoteIntegratedDataCopy.class);
	}
	
	
	public void archiveDataCopyExceptThisOne(RemoteIntegratedDataCopy remoteIntegratedDataCopy){
		String sqlCommand = "Update RemoteIntegratedDataCopy x set x.archived = ?1 where x.id != ?2 and x.dataType = ?3 and x.facilityId = ?4 and x.demographicNo = ?5 ";
		
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1,Boolean.TRUE);
		query.setParameter(2,remoteIntegratedDataCopy.getId());
		query.setParameter(3,remoteIntegratedDataCopy.getDataType());
		query.setParameter(4,remoteIntegratedDataCopy.getFacilityId());
		query.setParameter(5,remoteIntegratedDataCopy.getDemographicNo());
		int i = query.executeUpdate();
		MiscUtils.getLogger().debug("i was "+i);
	}

	
	public  RemoteIntegratedDataCopy findByDemoType(Integer facilityId, Integer demographicNo,String dataType){
		String sqlCommand = "select x from RemoteIntegratedDataCopy x where x.demographicNo=?1 and x.dataType=?2  and x.facilityId = ?3 and x.archived != true order by x.id desc ";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);
		query.setParameter(2, dataType);
		query.setParameter(3, facilityId);
		
		@SuppressWarnings("unchecked")
		List<RemoteIntegratedDataCopy> results=query.getResultList();
		
		if (results.size() > 0){
			return results.get(0);
		}
		return null;
	}
	
	public  RemoteIntegratedDataCopy findByType(Integer facilityId,String dataType){
		String sqlCommand = "select x from RemoteIntegratedDataCopy x where x.dataType=?1  and x.facilityId = ?2 and x.archived != true order by x.id desc ";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, dataType);
		query.setParameter(2, facilityId);
		
		@SuppressWarnings("unchecked")
		List<RemoteIntegratedDataCopy> results=query.getResultList();
		
		if (results.size() > 0){
			return results.get(0);
		}
		return null;
	}
	
	
	public  RemoteIntegratedDataCopy findByDemoTypeSignature(Integer facilityId, Integer demographicNo,String dataType,String signature){
		String sqlCommand = "select x from RemoteIntegratedDataCopy x where x.demographicNo=?1 and x.dataType=?2 and x.signature=?3 and x.facilityId = ?4 and x.archived != true  ";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);
		query.setParameter(2, dataType);
		query.setParameter(3, signature);
		query.setParameter(4, facilityId);
		
		
		@SuppressWarnings("unchecked")
		List<RemoteIntegratedDataCopy> results=query.getResultList();
		
		if (results.size() > 0){
			return results.get(0);
		}
		return null;
	}
	
	
	
	
	public RemoteIntegratedDataCopy save(Integer demographicNo, Object obj,String providerNo, Integer facilityId) throws Exception{
		return  save( demographicNo,  obj, providerNo,  facilityId, null) ;
	}
	/**
	 * 
	 * @param demographicNo
	 * @param obj
	 * @param providerNo
	 * @param facilityId
	 * @return Returns null if it already existed in the database.
	 * @throws Exception
	 */
	public RemoteIntegratedDataCopy save(Integer demographicNo, Object obj,String providerNo, Integer facilityId,String type) throws Exception{
		
		if(obj == null){
			throw new Exception("Can't save null");
		}
		if(type == null){
			type = "";
		}else{
			type = "+"+type;
		}
		
		String dataType = obj.getClass().getName()+type;
		String marshalledObject = ObjectMarshalUtil.marshalToString(obj); 
        
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();
        byte[] digest = md.digest(marshalledObject.getBytes("UTF-8"));                
        String signature = new String(Base64.encodeBase64(digest), MiscUtils.DEFAULT_UTF8_ENCODING);
        
        MiscUtils.getLogger().debug("demo :"+demographicNo+" dataType : "+dataType+" Signature: "+signature+" providerNo "+providerNo+" facilityId "+facilityId);
		
		RemoteIntegratedDataCopy remoteIntegratedDataCopy = this.findByDemoTypeSignature(facilityId,demographicNo, dataType, signature);
		
		if (remoteIntegratedDataCopy == null){
			RemoteIntegratedDataCopy rid = new RemoteIntegratedDataCopy();
			rid.setDemographicNo(demographicNo);
			rid.setDataType(dataType);
			rid.setData(marshalledObject);
			rid.setSignature(signature);
			rid.setProviderNo(providerNo);
			rid.setFacilityId(facilityId);
			this.persist(rid);
			archiveDataCopyExceptThisOne(rid);//Set all other notes besides this one to archived.
			return rid;
		}
		return null;
    	
	}
	
	public <T> T getObjectFrom (Class<T> clazz, RemoteIntegratedDataCopy rid) throws Exception {
		return ObjectMarshalUtil.unmarshallToObject(clazz,rid.getData());
	}
	

 }
