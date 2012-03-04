package org.oscarehr.common.dao;

import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.springframework.stereotype.Repository;

@Repository
public class LabPatientPhysicianInfoDao extends AbstractDao<LabPatientPhysicianInfo>{

	public LabPatientPhysicianInfoDao() {
		super(LabPatientPhysicianInfo.class);
	}
}
