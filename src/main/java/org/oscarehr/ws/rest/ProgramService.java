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
package org.oscarehr.ws.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.tools.ant.util.DateUtils;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.ws.rest.conversion.AdmissionConverter;
import org.oscarehr.ws.rest.conversion.ProgramConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.model.AdmissionTo1;
import org.oscarehr.ws.rest.to.model.ProgramTo1;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/program")
public class ProgramService extends AbstractServiceImpl {

	
	@Autowired
	ProgramManager2 programManager;
	
	@Autowired
	AdmissionManager admissionManager;
	
	
	@GET
	@Path("/patientList")
	@Produces("application/json")
	public AbstractSearchResponse<AdmissionTo1> getPatientList(@QueryParam("programNo")String programNo, @QueryParam("day") String day, @QueryParam("startIndex") Integer startIndex,  @QueryParam("numToReturn") Integer numToReturn) throws Exception {
		
		AbstractSearchResponse<AdmissionTo1> response = new  AbstractSearchResponse<AdmissionTo1>();
		
		if(day == null) {
			day = DateFormatUtils.format(Calendar.getInstance(), DateUtils.ISO8601_DATE_PATTERN);
		}
		
		
		Date d = new SimpleDateFormat(DateUtils.ISO8601_DATE_PATTERN).parse(day);	
		
		if(programNo == null) {
			//get default
			ProgramProvider current = null;
			ProgramProvider pp = programManager.getCurrentProgramInDomain(getLoggedInInfo(),getLoggedInInfo().getLoggedInProviderNo());
			if(pp != null) {
				current = pp;
			} else {
				List<ProgramProvider> ppList = programManager.getProgramDomain(getLoggedInInfo(),getLoggedInInfo().getLoggedInProviderNo());
				if(ppList.size()>0) {
					current = ppList.get(0);
				}
			}
			if(current != null) {
				programNo = String.valueOf(current.getProgramId());
			}
		}
		
		if(programNo == null) {
			throw new Exception("Can't get a program for this provider to use as default");
		}
		
		
		List<AdmissionTo1> transfers = new AdmissionConverter().includeDemographic(true).getAllAsTransferObjects(getLoggedInInfo(),admissionManager.findAdmissionsByProgramAndDate(getLoggedInInfo(), Integer.parseInt(programNo),d,startIndex,numToReturn));
		
		response.setContent(transfers);
		response.setTotal(admissionManager.findAdmissionsByProgramAndDateAsCount(getLoggedInInfo(), Integer.parseInt(programNo),d));
		
		return response;
	}
	
	@GET
	@Path("/programList")
	@Produces("application/json")
	public AbstractSearchResponse<ProgramTo1> getProgramList() throws Exception {
		AbstractSearchResponse<ProgramTo1> response = new  AbstractSearchResponse<ProgramTo1>();
		
		List<ProgramProvider> programProviders = programManager.getProgramDomain(getLoggedInInfo(), getLoggedInInfo().getLoggedInProviderNo());
		if (programProviders!=null) {
			List<ProgramTo1> listProgramTo1 = new ArrayList<ProgramTo1>();
			ProgramConverter converter = new ProgramConverter();
			
			for (ProgramProvider pp : programProviders) {
				Program program = programManager.getProgram(getLoggedInInfo(), pp.getProgramId().intValue());
				if (program.getType().equals(Program.BED_TYPE)) {
					listProgramTo1.add(converter.getAsTransferObject(getLoggedInInfo(), program));
				}
			}
			response.setContent(listProgramTo1);
			response.setTotal(listProgramTo1.size());
		}
		
		return response;
	}
}
