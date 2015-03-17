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


import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DrugReasonDao extends AbstractDao<DrugReason> {

	public DrugReasonDao() {
		super(DrugReason.class);
	}

    public boolean addNewDrugReason(DrugReason d){
       MiscUtils.getLogger().debug("trying to save a drug reason" );
        		
       try{
            entityManager.persist(d);
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
            return false;
        }
       return true;
    }
    
    
    public Boolean hasReason(Integer drugId,String codingSystem,String code, boolean onlyActive){
    	boolean hasReason = false;
    	String sqlCommand = "select x from DrugReason x where x.drugId=?1 and x.codingSystem = ?2 and x.code = ?3 and x.archivedFlag = ?4";

        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, drugId);
        query.setParameter(2, codingSystem);
        query.setParameter(3, code);
        query.setParameter(4, !onlyActive);
        

        @SuppressWarnings("unchecked")
        List<DrugReason> results = query.getResultList();
        if(results != null && results.size() > 0){
        	hasReason = true;
        }
        return hasReason;
    }
    
    
    public List<DrugReason>  getReasonsForDrugID(Integer drugId,boolean onlyActive){
    	
    	String sqlCommand = "select x from DrugReason x where x.drugId=?1 and x.archivedFlag = ?2";
		
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, drugId);
        query.setParameter(2,!onlyActive);
	
        @SuppressWarnings("unchecked")
        List<DrugReason> results = query.getResultList();

        return (results);
    }


}
