package org.oscarehr.common.dao;

import org.oscarehr.common.model.TableModification;
import org.springframework.stereotype.Repository;

@Repository
public class TableModificationDao extends AbstractDao<TableModification>{

	public TableModificationDao() {
		super(TableModification.class);
	}
}
