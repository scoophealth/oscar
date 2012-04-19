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


/*
 * 
 */


package oscar.login;

import java.io.InputStream;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

/**
 * Class LoginViewTypeHlp : read mapping file from loginView.properties
 * 2003-01-05
*/

/*
 This class implements the pattern singleton.
 If you need instanciate it, instead 'new LoginViewTypeHlp()',
 use 'LoginViewTypeHlp.getInstance()'

 Use the method getProperty(String) to recover info
 about the login view type
 */
public class LoginViewTypeHlp extends Properties{
	
	public static LoginViewTypeHlp getInstance() {
		return myself;
	}

	static LoginViewTypeHlp myself = new LoginViewTypeHlp();

	private LoginViewTypeHlp() {
		/* getResourceAsStream is a method from Class() */
		InputStream is = getClass().getResourceAsStream("/loginView.properties");
		try { 
			load(is);
			is.close(); 
		} catch(Exception e) {
			MiscUtils.getLogger().debug("*** No ViewType File ***");
			MiscUtils.getLogger().error("Error", e);
		}
	}

}
