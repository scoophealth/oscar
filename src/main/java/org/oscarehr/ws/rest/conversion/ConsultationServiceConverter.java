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

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.ConsultationServiceTo1;
import org.oscarehr.ws.rest.to.model.ProfessionalSpecialistTo1;
import org.springframework.stereotype.Component;

@Component
public class ConsultationServiceConverter extends AbstractConverter<ConsultationServices, ConsultationServiceTo1> {

    private ProfessionalSpecialistConverter converter = new ProfessionalSpecialistConverter();

	@Override
	public ConsultationServices getAsDomainObject(LoggedInInfo loggedInInfo, ConsultationServiceTo1 t) throws ConversionException {
		ConsultationServices d = new ConsultationServices();
		
	    d.setActive(t.getActive());
	    d.setServiceDesc(t.getServiceDesc());
	    d.setServiceId(t.getServiceId());
	    
	    List<ProfessionalSpecialist> specialists = new ArrayList<ProfessionalSpecialist>();
	    for (ProfessionalSpecialistTo1 specialist : t.getSpecialists()) {
	    	specialists.add(converter.getAsDomainObject(loggedInInfo, specialist));
	    }
	    d.setSpecialists(specialists);
		
		return d;
	}

	@Override
	public ConsultationServiceTo1 getAsTransferObject(LoggedInInfo loggedInInfo, ConsultationServices d) throws ConversionException {
		ConsultationServiceTo1 t = new ConsultationServiceTo1();
		
	    t.setActive(d.getActive());
	    t.setServiceDesc(d.getServiceDesc());
	    t.setServiceId(d.getServiceId());
	    
	    List<ProfessionalSpecialistTo1> specialists = new ArrayList<ProfessionalSpecialistTo1>();
	    for (ProfessionalSpecialist specialist : d.getSpecialists()) {
	    	specialists.add(converter.getAsTransferObject(loggedInInfo, specialist));
	    }
	    t.setSpecialists(specialists);
	
		return t;
	}
	

}
