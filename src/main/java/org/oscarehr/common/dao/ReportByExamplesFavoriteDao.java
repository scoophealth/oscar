package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportByExamplesFavorite;
import org.springframework.stereotype.Repository;

@Repository
public class ReportByExamplesFavoriteDao extends AbstractDao<ReportByExamplesFavorite>{

	public ReportByExamplesFavoriteDao() {
		super(ReportByExamplesFavorite.class);
	}
}
