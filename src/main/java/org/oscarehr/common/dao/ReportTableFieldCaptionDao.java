package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportTableFieldCaption;
import org.springframework.stereotype.Repository;

@Repository
public class ReportTableFieldCaptionDao extends AbstractDao<ReportTableFieldCaption>{

	public ReportTableFieldCaptionDao() {
		super(ReportTableFieldCaption.class);
	}
}
