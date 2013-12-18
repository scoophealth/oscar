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
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.form.data.EAVFormField;

public class EAVFieldDao {
	
	/**
	 * Default Constructor
	 */
	public EAVFieldDao()
	{
		
	}
	
	/**
	 * Select the fields based on a form_id
	 * @param id_form
	 * @return
	 */
	public List<EAVFormField> selectFields(int id_form, Connection con)
	{
		PreparedStatement p;
		List<EAVFormField> field_list = new ArrayList<EAVFormField>();
        try {
	        p = con.prepareStatement("SELECT field_id_eav, ref_form, field_name, field_type,sql_constraint,default_value FROM eav_form_field WHERE "
	        		+ "ref_form = ?");
	        p.setInt(1, id_form);
	        ResultSet rs = p.executeQuery();
	        while(rs.next())
	        {
	        	EAVFormField field = new EAVFormField();
	        	field.setField_id_eav(rs.getInt("field_id_eav"));
	        	field.setRef_form(rs.getInt("ref_form"));
	        	field.setField_name(rs.getString("field_name"));
	        	field.setField_type(rs.getString("field_type"));
	        	field.setSql_constraint(rs.getString("sql_constraint"));
	        	field.setDefault_value(rs.getString("default_value"));
	        	field_list.add(field);
	        }
	        	
	        rs.close();
	        return field_list;
	        
        } catch (SQLException e) {
	        // TODO Auto-generated catch block
        	MiscUtils.getLogger().error(e.toString());
        }
		
		
		return null;
	}
	
	
	/**
	 * insert a list of fields into the Database
	 * @param fields : the list of EAVFormField
	 * @param con : the database connection
	 * @throws SQLException : threw if a SQL exception is raised
	 */
	public void insertFields(List<EAVFormField> fields, Connection con) throws SQLException
	{
			for(EAVFormField f : fields){
			
		        PreparedStatement p = con.prepareStatement("INSERT INTO eav_form_field (field_id_eav,ref_form,field_name,field_type,sql_constraint,default_value) VALUES(?,?,?,?,?,?);");
		        
		        p.setInt(1, f.getField_id_eav());
		        p.setInt(2, f.getRef_form());
		        p.setString(3, f.getField_name());
		        p.setString(4, f.getField_type());
		        p.setString(5, f.getSql_constraint());
		        p.setString(6, f.getDefault_value());
	
		        
		        p.executeUpdate();
	        
			}
	}
	

}
