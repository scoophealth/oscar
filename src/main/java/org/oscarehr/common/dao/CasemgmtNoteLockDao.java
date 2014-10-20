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

import org.oscarehr.common.model.CasemgmtNoteLock;
import org.springframework.stereotype.Repository;

@Repository
public class CasemgmtNoteLockDao extends AbstractDao<CasemgmtNoteLock> {
	public CasemgmtNoteLockDao() {
		super(CasemgmtNoteLock.class);
	}
	
	public CasemgmtNoteLock findByNoteDemo(Integer demographicNo, Long note_id) {
		Query query = entityManager.createQuery("select lock from CasemgmtNoteLock lock where lock.demographicNo = :demo and lock.noteId = :noteId");
		
		query.setParameter("demo", demographicNo);		
		query.setParameter("noteId", note_id);
		
		return getSingleResultOrNull(query);
	}
	
	public void remove(String providerNo, Integer demographicNo, Long note_id) {
		Query query = entityManager.createQuery("select lock from CasemgmtNoteLock lock where lock.demographicNo = :demo and lock.providerNo = :providerNo" +
				" and lock.noteId = :note_id");
		
		query.setParameter("demo", demographicNo);
		query.setParameter("providerNo", providerNo);
		query.setParameter("note_id", note_id);
		
		CasemgmtNoteLock casemgmtNoteLock = getSingleResultOrNull(query);
		
		if( casemgmtNoteLock != null ) {
			remove(casemgmtNoteLock);
		}
	}
	
	public List<CasemgmtNoteLock> findBySession(String sessionId) {
		Query query = entityManager.createQuery("select lock from CasemgmtNoteLock lock where lock.sessionId = :sessionId");
		
		query.setParameter("sessionId", sessionId);
		
		List<CasemgmtNoteLock> results = query.getResultList();
		
		return results;
	}
}
