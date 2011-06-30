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
    public List<HRMDocumentComment> getCommentsForDocument(String documentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.deleted=0 order by commentTime desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, Integer.parseInt(documentId));
		return query.getResultList();
	}
	
	public void deleteComment(String commentId) {
		HRMDocumentComment comment = this.find(Integer.parseInt(commentId));
		comment.setDeleted(true);
		
		this.merge(comment);
	}

}
