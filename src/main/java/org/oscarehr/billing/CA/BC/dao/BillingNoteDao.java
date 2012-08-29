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
package org.oscarehr.billing.CA.BC.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.BillingNotes;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingNoteDao extends AbstractDao<BillingNotes> {

	protected BillingNoteDao() {
		super(BillingNotes.class);
	}

	/**
	 * Finds notes for the specified billing master and note type
	 * 
	 * @param billingmaster_no
	 * 		Billing master number to find notes for
	 * @param noteType
	 * 		Optional type of the notes to be found. If this parameter is left as null note type is not used for lookup.
	 * @return
	 * 		Returns all matching notes
	 */
	@SuppressWarnings("unchecked")
	public List<BillingNotes> findNotes(Integer billingmaster_no, Integer noteType) {
		StringBuilder buf = new StringBuilder("FROM BillingNotes n WHERE n.billingmasterNo = :bmno");
		boolean isNoteTypeSpecified = noteType != null;
		if (isNoteTypeSpecified) buf.append(" AND n.noteType = :noteType");
		Query query = entityManager.createQuery(buf.toString());
		query.setParameter("bmno", billingmaster_no);
		if (isNoteTypeSpecified) query.setParameter("noteType", noteType);
		return query.getResultList();
	}

	/**
	 * Gets single most recent note for the specified billing master number and note
	 * 
	 * @param billingmaster_no
	 * 		Billing master number to look notes for
	 * @param noteType
	 * 		Type of the note to be found
	 * @return
	 * 		Returns the note or null, if no note can be found
	 */
	public BillingNotes findSingleNote(Integer billingmaster_no, Integer noteType) {
		Query query = entityManager.createQuery("FROM BillingNotes n WHERE n.billingmasterNo = :bmno AND n.noteType = :noteType ORDER BY n.createdate desc");
		query.setParameter("bmno", billingmaster_no);
		query.setParameter("noteType", noteType);
		query.setMaxResults(1);		
		return getSingleResultOrNull(query);
	}

}
