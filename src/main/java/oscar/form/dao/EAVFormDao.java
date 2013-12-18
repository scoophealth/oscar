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

package oscar.form.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.form.data.EAVFormName;

public class EAVFormDao {

	/**
	 * Default Constructor
	 */
	public EAVFormDao() {

	}

	/**
	 * Select a form based on his name
	 * @param name : the form name
	 * @param con 
	 * @return the EAVFormName
	 */
	public EAVFormName selectForm(String name, Connection con) {
		PreparedStatement p;
        try {
	        p = con.prepareStatement("SELECT form_id_eav, form_name FROM eav_form_name WHERE form_name = ?;");
	        p.setString(1, name);
	        
	        ResultSet rs = p.executeQuery();
	        if(rs.next())
	        {
	        EAVFormName eav_form_name = new EAVFormName();
	        eav_form_name.setForm_id_eav(rs.getInt("form_id_eav"));
	        eav_form_name.setForm_name(rs.getString("form_name"));
	        return eav_form_name;
	        }
	        rs.close();
	        
	        
	        
        } catch (SQLException e) {
	        // TODO Auto-generated catch block
        	MiscUtils.getLogger().error(e.toString());
        }
		
        return null;
	}

	/**
	 * insert a EAVFormName into the database
	 * @param form : the EAVFormName
	 * @param con : the database connection
	 * @throws SQLException : threw if a SQL exception is raised
	 */
	public void insertForm(EAVFormName form, Connection con) throws SQLException {
		PreparedStatement p = con.prepareStatement("INSERT INTO eav_form_table (form_id_eav,form_name) VALUES (?,?);");

		p.setInt(1, form.getForm_id_eav());
		p.setString(2, form.getForm_name());

		p.executeUpdate();

	}

}
