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

import org.oscarehr.common.model.MsgDemoMap;
import org.springframework.stereotype.Repository;

@Repository
public class MsgDemoMapDao extends AbstractDao<MsgDemoMap>{

	public MsgDemoMapDao() {
		super(MsgDemoMap.class);
	}
	
	public List<MsgDemoMap> findByDemographicNo(Integer demographicNo) {
		String sql = "select x from MsgDemoMap x where x.id.demographicNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographicNo);

        @SuppressWarnings("unchecked")
        List<MsgDemoMap> results = query.getResultList();
        return results;
	}
	
	public List<MsgDemoMap> findByMessageId(Integer messageId) {
		String sql = "select x from MsgDemoMap x where x.id.messageId=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,messageId);

        @SuppressWarnings("unchecked")
        List<MsgDemoMap> results = query.getResultList();
        return results;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getMessagesAndDemographicsByMessageId(Integer messageId) {
		String sql = "FROM MsgDemoMap m, Demographic d " +
				"WHERE m.id.messageId = :msgId " + 
                "AND d.DemographicNo = m.id.demographicNo " +
                "ORDER BY d.LastName, d.FirstName";
		Query query = entityManager.createQuery(sql);
		query.setParameter("msgId", messageId);
        return query.getResultList();
    }

	public List<Object[]> getMapAndMessagesByDemographicNo(Integer demoNo) {
	    // TODO Auto-generated method stub
		String sql = "FROM MsgDemoMap map, MessageTbl m " +
				"WHERE m.id = map.id.messageId " +
				"AND map.id.demographicNo = :demoNo " +
				"ORDER BY m.date DESC, m.id DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		return query.getResultList();	    
    }
}
