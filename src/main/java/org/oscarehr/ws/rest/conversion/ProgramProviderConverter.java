/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest.conversion;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.ProgramProviderTo1;
import org.springframework.stereotype.Component;

@Component
/*
 * I see a component-scan for this in applicationContextREST.xml but it's not being autowired..can't figure it out
 * right now..but this is a problem for all these I think.
 */
public class ProgramProviderConverter  extends AbstractConverter<ProgramProvider, ProgramProviderTo1> {

//	private static Logger logger = Logger.getLogger(ProgramConverter.class);

	//@Autowired
	private ProgramDao programDao;

	//@Autowired 
	private SecRoleDao secRoleDao ;

	@Override
	public ProgramProvider getAsDomainObject(LoggedInInfo loggedInInfo,ProgramProviderTo1 t) throws ConversionException {
		throw new RuntimeException("not yet implemented");
	}
	
	@Override
	public ProgramProviderTo1 getAsTransferObject(LoggedInInfo loggedInInfo,ProgramProvider a) throws ConversionException {
		if(programDao == null) {
			programDao = SpringUtils.getBean(ProgramDao.class);
		}
		if(secRoleDao == null) {
			secRoleDao = SpringUtils.getBean(SecRoleDao.class);
		}
		ProgramProviderTo1 t = new ProgramProviderTo1();
		t.setId(a.getId().intValue());
		t.setProgramId(a.getProgramId().intValue());
		t.setProgram(new ProgramConverter().getAsTransferObject(loggedInInfo,programDao.getProgram(t.getProgramId())));
		t.setProviderNo(a.getProviderNo());
		t.setRoleId(a.getRoleId().intValue());
		
		SecRole secRole = secRoleDao.find(t.getRoleId());
		t.setRoleName(secRole != null? secRole.getName() : "N/A");
		
		return t;
	}

}
