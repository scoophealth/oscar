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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.MdsOBR;
import org.springframework.stereotype.Repository;

import oscar.util.ParamAppender;

@Repository
@SuppressWarnings("unchecked")
public class MdsOBRDao extends AbstractDao<MdsOBR>{

	public MdsOBRDao() {
		super(MdsOBR.class);
	}

    public List<Object[]> findByIdAndResultCodes(Integer id, List<String> resultCodes) {
		ParamAppender pa = new ParamAppender("SELECT DISTINCT mdsOBX.associatedOBR, mdsOBR.observationDateTime " +
				"FROM MdsOBX mdsOBX, MdsOBR mdsOBR");
		pa.and("mdsOBX.id = mdsOBR.id");
		pa.and("mdsOBX.associatedOBR = mdsOBR.ObrId");
		pa.and("mdsOBX.id = :id", "id", id);
		
		if (!resultCodes.isEmpty()) {
			ParamAppender codesPa = new ParamAppender();
			for(int i = 0; i < resultCodes.size(); i++) {
				String paramName = "observationSubId" + i;
				codesPa.or("mdsOBX.observationSubId like :" + paramName, paramName, "%" + resultCodes.get(i) + "%");
			}
			pa.and(codesPa);
		}
		pa.addOrder("mdsOBX.associatedOBR");
		
		Query query = entityManager.createQuery(pa.getQuery());
		pa.setParams(query);
		
		return query.getResultList();
    }
}
