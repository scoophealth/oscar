package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ReportAgeSex;
import org.springframework.stereotype.Repository;

@Repository
public class ReportAgeSexDao extends AbstractDao<ReportAgeSex>{

	public ReportAgeSexDao() {
		super(ReportAgeSex.class);
	}

    public List<ReportAgeSex> findBeforeReportDate(Date reportDate) {
    	String sql = "select x from ReportAgeSex x where x.reportDate=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,reportDate);

        @SuppressWarnings("unchecked")
        List<ReportAgeSex> results = query.getResultList();
        return results;
    }
}
