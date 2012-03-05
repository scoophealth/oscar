package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlSpecialInstructions;
import org.springframework.stereotype.Repository;

@Repository
public class CtlSpecialInstructionsDao extends AbstractDao<CtlSpecialInstructions>{

	public CtlSpecialInstructionsDao() {
		super(CtlSpecialInstructions.class);
	}
}
