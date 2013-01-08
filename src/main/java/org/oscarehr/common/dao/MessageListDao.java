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

import org.oscarehr.common.model.MessageList;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MessageListDao extends AbstractDao<MessageList> {

	public MessageListDao() {
		super(MessageList.class);
	}
	
	public List<MessageList> findByProviderNoAndMessageNo(String providerNo, Long messageNo) {
		Query query = createQuery("msg", "msg.providerNo = :pno AND msg.message = :msg");
		query.setParameter("pno", providerNo);
		query.setParameter("msg", messageNo);
		return query.getResultList();
	}

	public List<MessageList> findByProviderNoAndLocationNo(String providerNo, Integer remoteLocation) {
		Query query = createQuery("ml", "ml.providerNo = :providerNo and ml.status not like 'del' and ml.remoteLocation = :remoteLocation order by ml.message");
		query.setParameter("providerNo", providerNo);
		query.setParameter("remoteLocation", remoteLocation);
		return query.getResultList();
	}

	public List<MessageList> findByMessage(Long messageNo) {
	    Query query = createQuery("ml", "ml.message = :messageNo");
		query.setParameter("messageNo", messageNo);
		return query.getResultList();
    }

	public List<MessageList> findByProviderAndStatus(String providerNo, String status) {
		Query query = createQuery("ml", "ml.providerNo = :providerNo and ml.status = :status");
		query.setParameter("providerNo", providerNo);
		query.setParameter("status", status);
		return query.getResultList();	    
    }
}
