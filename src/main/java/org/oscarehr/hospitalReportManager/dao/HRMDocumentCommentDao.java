/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.hospitalReportManager.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentComment;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentCommentDao extends AbstractDao<HRMDocumentComment> {

	public HRMDocumentCommentDao() {
	    super(HRMDocumentComment.class);
    }
	
	@SuppressWarnings("unchecked")
    public List<HRMDocumentComment> getCommentsForDocument(Integer documentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.deleted=0 order by commentTime desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, documentId);
		return query.getResultList();
	}
	
	public void deleteComment(Integer commentId) {
		HRMDocumentComment comment = this.find(commentId);
		if(comment != null) {
			comment.setDeleted(true);
			this.merge(comment);
		}
	}

}
