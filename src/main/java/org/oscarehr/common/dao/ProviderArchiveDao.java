package org.oscarehr.common.dao;

import org.oscarehr.common.model.ProviderArchive;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderArchiveDao extends AbstractDao<ProviderArchive> {

	public ProviderArchiveDao() {
		super(ProviderArchive.class);
	}
}
