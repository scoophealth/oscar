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
package oscar.login.jaas;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

/**
 * Configuration class for providing programmatic configuration options. This class is not intended to be used with enabled security manager 
 * and is capable of providing configuration for a single login module only, in order to avoid additional external configuration.
 * 
 * <p/>
 * 
 * Please consider using proper file-based configuration instead by specifying "java.security.auth.login.config" property with the value 
 * containing the full path to JAAS configuration file. 
 * 
 */
public class OscarConfiguration extends Configuration {

	private String moduleName;
	private String contextName;
	private Map<String, Object> options = new HashMap<String, Object>();

	/**
	 * Creates a new configuration instance with the specified login context name and login module name 
	 * 
	 * @param contextName
	 * 		Name of the login context
	 * @param moduleName
	 * 		Complete class name of the login module to be used for authentication 
	 */
	public OscarConfiguration(String contextName, String moduleName) {
		this.contextName = contextName;
		this.moduleName = moduleName;
	}

	public OscarConfiguration(String contextName, String moduleName, Map<String, Object> options) {
		this(contextName, moduleName);
		this.options = options;
	}
	
	public void setOption(String key, String value) {
		this.options.put(key, value);
	}
	
	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
		if (contextName.equalsIgnoreCase(name)) {
			AppConfigurationEntry entry = new AppConfigurationEntry(moduleName, LoginModuleControlFlag.REQUIRED, options);
			return new AppConfigurationEntry[] { entry };
		}
		return null;
	}

}
