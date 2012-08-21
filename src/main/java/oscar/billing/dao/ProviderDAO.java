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


package oscar.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.billing.model.Provider;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;


@Deprecated
public class ProviderDAO extends DAO {
    public ProviderDAO(Properties pvar)  {
    	//empty
    }

    public ArrayList<Provider> list(int type) throws SQLException {
        ArrayList<Provider> list = new ArrayList<Provider>();
        String sql = "select provider_no, last_name, first_name from provider";

        if (type == Provider.DOCTOR) {
            sql = sql + " where provider_type = 'doctor'";
        } else if (type == Provider.RECEPTIONIST) {
            sql = sql + " where provider_type = 'receptionist'";
        }

        sql = sql + " order by first_name, last_name";



        try {
            ResultSet rs = DBHandler.GetSQL(sql);
            MiscUtils.getLogger().debug("sql " + sql);

            Provider provider = new Provider();
            provider.setProviderNo("0");
			provider.setLastName("");
			provider.setFirstName("Todos");
			list.add(provider);

            while (rs.next()) {
                provider = new Provider();
                provider.setProviderNo(oscar.Misc.getString(rs, 1));
                provider.setLastName(oscar.Misc.getString(rs, 2));
                provider.setFirstName(oscar.Misc.getString(rs, 3));
                list.add(provider);
            }
        } finally {
        }

        return list;
    }
}
