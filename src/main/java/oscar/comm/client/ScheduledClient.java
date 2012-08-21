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


package oscar.comm.client;

import java.io.FileInputStream;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

@Deprecated
public class ScheduledClient {
	Properties param = new Properties();

    public static void main(String[] args) throws Exception {
        MiscUtils.getLogger().debug("Running oscarCommClient...");

        if(args.length != 1) {
			MiscUtils.getLogger().debug("Usage: oscarCommClient pathOfPropertiesFile");



        }  else  {
            MiscUtils.getLogger().debug("    propertiesFile:  " + args[0]);

			ScheduledClient aStudy = new ScheduledClient();
			//initial
			aStudy.init(args[0]);

            SendAddressBookClient cl = new SendAddressBookClient();
            cl.sendAddressBook("", aStudy.param.getProperty("db_name"));
            //cl.sendAddressBook(args[0], args[1]);
        }
    }

	private synchronized void init (String file) throws java.io.IOException  {
		param.load(new FileInputStream(file));
	}

}
