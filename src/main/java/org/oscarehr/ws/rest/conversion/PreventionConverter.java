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

import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.PreventionTo1;

public class PreventionConverter extends AbstractConverter<Prevention, PreventionTo1> {

	@Override
    public Prevention getAsDomainObject(LoggedInInfo loggedInInfo, PreventionTo1 t) throws ConversionException {
	    throw new ConversionException("not yet implemented");
    }

	@Override
    public PreventionTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Prevention d) throws ConversionException {
	   PreventionTo1 t = new PreventionTo1();
	   	   
	   t.setDemographicId(d.getDemographicId());
	   t.setPreventionDate(d.getPreventionDate());
	   t.setProviderNo(d.getProviderNo());
	   t.setPreventionType(d.getPreventionType());
	   t.setNextDate(d.getNextDate());
	   t.setCreatorProviderNo(d.getCreatorProviderNo());
	   t.setRestrictToProgram(d.getRestrictToProgram());
	   t.setProgramNo(d.getProgramNo());
	   
	   return t;
    }


}
