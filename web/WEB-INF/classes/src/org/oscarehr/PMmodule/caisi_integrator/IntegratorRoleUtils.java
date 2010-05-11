package org.oscarehr.PMmodule.caisi_integrator;

import org.oscarehr.caisi_integrator.ws.Role;

public final class IntegratorRoleUtils {
	
	/**
	 * @return the matching integrator role, the input can be null and it will just return null, roles that do not match will also return null.
	 */
	public static Role getIntegratorRole(String oscarRole)
	{
		try
		{
			return(Role.valueOf(oscarRole.toUpperCase()));
		}
		catch (Exception e)
		{
			// just ignore it, we're just testing for direct matches, null and non matches are expected
		}
		
		if ("Clinical Assistant".equals(oscarRole)) return(Role.CLINICAL_ASSISTANT);
		else if ("Housing Worker".equals(oscarRole)) return(Role.HOUSING_WORKER);
		
		return(null);
	}
}
