package org.oscarehr.common.dao;

import org.oscarehr.common.model.DxAssociation;
import org.springframework.stereotype.Repository;

@Repository
public class DxAssociationDao extends AbstractDao<DxAssociation>{

	public DxAssociationDao() {
		super(DxAssociation.class);
	}
}
