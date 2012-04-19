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

import org.oscarehr.common.model.ConsultationRequestExt;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationRequestExtDao extends AbstractDao<ConsultationRequestExt> {

	public ConsultationRequestExtDao() {
		super(ConsultationRequestExt.class);
	}
	
	public List<ConsultationRequestExt> getConsultationRequestExts(int requestId) {
    	Query query = entityManager.createQuery("select cre from ConsultationRequestExt cre where cre.requestId=?1");
        query.setParameter(1, new Integer(requestId));
        return query.getResultList();
    }
	
	public String getConsultationRequestExtsByKey(int requestId,String key) {
		Query query = entityManager.createQuery("select cre.value from ConsultationRequestExt cre where cre.requestId=?1 and cre.key=?2");
        query.setParameter(1, new Integer(requestId));
        query.setParameter(2, key);
        List<String> results = query.getResultList();
        if(results.size()>0)
        	return results.get(0);
        return null;
	}
	
	public void clear(int requestId) {
		Query query = entityManager.createQuery("delete from ConsultationRequestExt cre where cre.requestId = ?1");
		query.setParameter(1, new Integer(requestId));
		query.executeUpdate();
	}
}
