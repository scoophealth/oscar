package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportItem;
import org.springframework.stereotype.Repository;

@Repository
public class ReportItemDao extends AbstractDao<ReportItem>{

	public ReportItemDao() {
		super(ReportItem.class);
	}
}
