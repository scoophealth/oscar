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


package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.ws.transfer_objects.ProgramProviderTransfer;
import org.oscarehr.ws.transfer_objects.ProgramTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold=AbstractWs.GZIP_THRESHOLD)
public class ProgramWs extends AbstractWs {
	@Autowired
	private ProgramManager2 programManager;

	public ProgramTransfer[] getAllPrograms() {
		List<Program> tempResults = programManager.getAllPrograms(getLoggedInInfo());

		ProgramTransfer[] results = ProgramTransfer.toTransfers(tempResults);

		return (results);
	}
	
	public ProgramProviderTransfer[] getAllProgramProviders()
	{
		List<ProgramProvider> tempResults = programManager.getAllProgramProviders(getLoggedInInfo());

		ProgramProviderTransfer[] results = ProgramProviderTransfer.toTransfers(tempResults);

		return (results);
	}
}
