package org.oscarehr.common.dao;

import org.oscarehr.common.model.PharmacyInfo;
import org.springframework.stereotype.Repository;

@Repository
public class PharmacyInfoDao extends AbstractDao<PharmacyInfo>{

	public PharmacyInfoDao() {
		super(PharmacyInfo.class);
	}
}
