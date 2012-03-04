package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportProvider;
import org.springframework.stereotype.Repository;

@Repository
public class ReportProviderDao extends AbstractDao<ReportProvider>{

	public ReportProviderDao() {
		super(ReportProvider.class);
	}

}
