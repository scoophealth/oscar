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
