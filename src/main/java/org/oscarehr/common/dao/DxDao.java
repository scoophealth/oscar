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

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DxDao extends AbstractDao<DxAssociation> {

	public DxDao() {
		super(DxAssociation.class);
	}

	public List<DxAssociation> findAllAssociations()
	{			
		Query query = entityManager.createQuery("select x from DxAssociation x order by x.dxCodeType,x.dxCode");
		
		@SuppressWarnings("unchecked")
		List<DxAssociation> results = query.getResultList();

		return(results);
	}
    
    public int removeAssociations() {
    	Query query = entityManager.createQuery("DELETE from DxAssociation");
    	return query.executeUpdate();
    }
    
    public DxAssociation findAssociation(String codeType, String code) {    	
    	Query query = entityManager.createQuery("SELECT x from DxAssociation x where x.codeType = ?1 and x.code = ?2");
    	query.setParameter(1, codeType);
    	query.setParameter(2, code);
    	    	
        @SuppressWarnings("unchecked")
    	List<DxAssociation> results = query.getResultList();
    	if(!results.isEmpty()) {
    		return results.get(0);
    	}
    	return null;
    }

    @NativeSql
    @SuppressWarnings("unchecked")
	public List<Object[]> findCodingSystemDescription(String codingSystem, String code) {
		try {
			String sql = "SELECT " + codingSystem +", description FROM " + codingSystem + " WHERE " + codingSystem + " = :code";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("code", code);
			return query.getResultList();
		} catch (Exception e) {
			// TODO Add exclude to the test instead when it's merged  
			return new ArrayList<Object[]>();
		}
    }
	
	@NativeSql
    @SuppressWarnings("unchecked")
	public List<Object[]> findCodingSystemDescription(String codingSystem, String[] keywords) {
		try {
			boolean flag=false;
			StringBuilder buf = new StringBuilder("select " + codingSystem + ", description from " + codingSystem);
			
			for(String keyword : keywords){
                if(keyword == null || keyword.trim().equals("")) {
                	continue;
                }
                if(!flag) {
                	buf.append(" where "); 
                }
                if(flag) {
                	buf.append(" or ");
                }
                buf.append(" " + codingSystem + " like '%" + keyword + "%' or description like '%" + keyword + "%' ");
                flag=true;
            }
			
			Query query = entityManager.createNativeQuery(buf.toString());
			return query.getResultList();
		} catch (Exception e) {
			MiscUtils.getLogger().error("error",e);
			return new ArrayList<Object[]>();
		}
		
	}
	
	@NativeSql
	public String getCodeDescription(String codingSystem, String code) {
		String desc="";
		StringBuilder buf = new StringBuilder("select description from " + codingSystem + " where "+ codingSystem + "='" + code + "'");
		try {
			Query query = entityManager.createNativeQuery(buf.toString());
			desc =  (String) query.getSingleResult();
		} catch (Exception e) {
			MiscUtils.getLogger().error("error "+buf,e);
		}
		return desc;
	}
		
}
