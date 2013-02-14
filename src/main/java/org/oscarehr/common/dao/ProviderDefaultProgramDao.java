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
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderDefaultProgramDao extends AbstractDao<ProviderDefaultProgram>{

	public ProviderDefaultProgramDao() {
		super(ProviderDefaultProgram.class);
	}

    public List<ProviderDefaultProgram> getProgramByProviderNo(String providerNo) {
        String q = "SELECT pdp FROM ProviderDefaultProgram pdp WHERE pdp.providerNo=?";
        Query query = entityManager.createQuery(q);
        query.setParameter(1, providerNo);
        @SuppressWarnings("unchecked")
        List<ProviderDefaultProgram> results = query.getResultList();
        return results;
    }

    public void setDefaultProgram(String providerNo, int programId) {
        List<ProviderDefaultProgram> rs = getProgramByProviderNo(providerNo);
        ProviderDefaultProgram pdp = null;
        if (rs.size() == 0) {
            pdp = new ProviderDefaultProgram();
            pdp.setProviderNo(providerNo);
            pdp.setSign(false);
            pdp.setProgramId(programId);
            merge(pdp);
        }
        else {
            pdp = rs.get(0);
            pdp.setProgramId(programId);
            persist(pdp);
        }
    }

    public List<ProviderDefaultProgram> getProviderSig(String providerNo) {
        List<ProviderDefaultProgram> rs = getProgramByProviderNo(providerNo);
        return rs;
    }

    public void saveProviderDefaultProgram(ProviderDefaultProgram pdp) {
       if(pdp.getId() == null || pdp.getId().intValue() == 0) {
    	   persist(pdp);
       } else {
    	   merge(pdp);
       }
    }

    public void toggleSig(String providerNo) {
       List<ProviderDefaultProgram> rs = this.getProgramByProviderNo(providerNo);
       for(ProviderDefaultProgram pdp:rs) {
    	   pdp.setSign(!pdp.isSign());
    	   merge(pdp);
       }
    }

	public List<Program> findProgramsByProvider(String providerNo) {
		String sql = "FROM Program p WHERE p.id IN (SELECT pdp.programId FROM ProviderDefaultProgram pdp WHERE pdp.providerNo = :pr)";
		Query query = entityManager.createQuery(sql);
		query.setParameter("pr", providerNo);
		
		@SuppressWarnings("unchecked")
        List<Program> results = query.getResultList();
        return results;
	    
    }
	
	public List<Program> findProgramsByFacilityId(Integer facilityId) {
		String sql = "from Program p where p.id in (select distinct pg.id from Program pg ,ProgramProvider pp where pp.ProgramId=pg.id and pg.facilityId=?)";
		Query query;
		try {
			query = entityManager.createQuery(sql);
			query.setParameter(1, facilityId);
		} catch (Exception e) {
			MiscUtils.getLogger().error(e.getMessage(),e);
			return new ArrayList<Program>();
		}
		
		@SuppressWarnings("unchecked")
        List<Program> results = query.getResultList();
		return results;
	}
}
