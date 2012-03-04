package org.oscarehr.common.dao;

import org.oscarehr.common.model.Desaprisk;
import org.springframework.stereotype.Repository;

@Repository
public class DesapriskDao extends AbstractDao<Desaprisk> {

	public DesapriskDao() {
		super(Desaprisk.class);
	}
}
