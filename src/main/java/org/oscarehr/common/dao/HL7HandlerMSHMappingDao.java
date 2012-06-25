/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.dao;

import javax.persistence.Query;


import org.oscarehr.common.model.HL7HandlerMSHMapping;
import org.springframework.stereotype.Repository;

@Repository
public class HL7HandlerMSHMappingDao  extends AbstractDao<HL7HandlerMSHMapping> {
	
	public HL7HandlerMSHMappingDao() {
		super (HL7HandlerMSHMapping.class);
	}
	
	public HL7HandlerMSHMapping findByFacility(String facility) {
		String sql = "select x from HL7HandlerMSHMapping x where x.facility=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facility);
		
		return(getSingleResultOrNull(query));
	}
}
