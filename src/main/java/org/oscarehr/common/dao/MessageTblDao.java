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

import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.MsgDemoMap;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MessageTblDao extends AbstractDao<MessageTbl>{

	public MessageTblDao() {
		super(MessageTbl.class);
	}
	
	public List<MessageTbl> findByMaps(List<MsgDemoMap> m) {
		String sql = "select x from MessageTbl x where x.id in (:m)";
    	Query query = entityManager.createQuery(sql);
    	List<Integer> ids = new ArrayList<Integer>();
    	for(MsgDemoMap temp:m) {
    		ids.add(temp.getId().getMessageId());
    	}
    	query.setParameter("m", ids);
        List<MessageTbl> results = query.getResultList();
        return results;
	}
	
	public List<MessageTbl> findByProviderAndSendBy(String providerNo, Integer sendBy) {
		Query query = createQuery("m", "m.sentByNo = :providerNo and m.sentByLocation = :sendBy");
		query.setParameter("providerNo", providerNo);
		query.setParameter("sendBy", sendBy);
		return query.getResultList();
	}

	public List<MessageTbl> findByIds(List<Integer> ids) {
		Query query = createQuery("m", "m.id in (:ids) order by m.date");
		query.setParameter("ids", ids);
		return query.getResultList();
    }
}
