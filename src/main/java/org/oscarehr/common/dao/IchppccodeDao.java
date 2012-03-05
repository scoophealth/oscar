package org.oscarehr.common.dao;

import org.oscarehr.common.model.Ichppccode;
import org.springframework.stereotype.Repository;

@Repository
public class IchppccodeDao extends AbstractDao<Ichppccode>{

	public IchppccodeDao() {
		super(Ichppccode.class);
	}
}
