/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.caisi_integrator;

import org.oscarehr.caisi_integrator.ws.Role;

public final class IntegratorRoleUtils {
	
	/**
	 * @return the matching integrator role, the input can be null and it will just return null, roles that do not match will also return null.
	 */
	public static Role getIntegratorRole(String oscarRole)
	{
		if (oscarRole==null) return(null);
		
		try
		{
			return(Role.valueOf(oscarRole.toUpperCase().replaceAll(" ", "_")));
		}
		catch (Exception e)
		{
			// just ignore it, we're just testing for direct matches, null and non matches are expected
		}

		// we can put special cases where mapping is not simple, like as an example :  
		// if ("Front Desk Worker".equals(oscarRole)) return(Role.FDW);
		
		return(null);
	}
}
