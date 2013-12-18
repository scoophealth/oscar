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

import oscar.form.data.EAVFormAttributeValue;

public class EAVAttributeValueDao {

	public EAVAttributeValueDao() {

	}

	/**
	 * 
	 * @param ref_form_entity
	 * @param con
	 * @return
	 */
	public List<EAVFormAttributeValue> selectAttributeValue(int ref_form_entity, Connection con) {
		PreparedStatement p;
		List<EAVFormAttributeValue> list_attr = new ArrayList<EAVFormAttributeValue>();
		try {
			p = con.prepareStatement("SELECT ref_form_entity,ref_form,ref_field,attribute_value_varchar,attribute_value_time,attribute_value_text,"
					+ "attribute_value_int_unsigned,attribute_value_int,attribute_value_enum,attribute_value_double,attribute_value_date,"
					+ "attribute_value_char,attribute_value_bit,attribute_value_bigint,attribute_name FROM eav_form_attribute_value WHERE ref_form_entity = ?;");
			
			p.setInt(1, ref_form_entity);
			ResultSet rs = p.executeQuery();
			
			while (rs.next()) {
				EAVFormAttributeValue attrValue = new EAVFormAttributeValue();
				attrValue.setRef_form_entity(rs.getInt("ref_form_entity"));
				attrValue.setRef_form(rs.getInt("ref_form"));
				attrValue.setRef_field(rs.getInt("ref_field"));
				attrValue.setAttribute_value_varchar(rs.getString("attribute_value_varchar"));
				attrValue.setAttribute_value_time(rs.getTime("attribute_value_time"));
				attrValue.setAttribute_value_text(rs.getString("attribute_value_text"));
				attrValue.setAttribute_value_int_unsigned(rs.getInt("attribute_value_int_unsigned"));
				if (rs.wasNull()) {
					attrValue.setAttribute_value_int_unsigned(null);
			    }
				attrValue.setAttribute_value_int(rs.getInt("attribute_value_int"));
				if (rs.wasNull()) {
					attrValue.setAttribute_value_int(null);
			    }
				attrValue.setAttribute_value_enum(rs.getString("attribute_value_enum"));
				attrValue.setAttribute_value_double(rs.getDouble("attribute_value_double"));
				attrValue.setAttribute_value_date(rs.getDate("attribute_value_date"));
				if(rs.getString("attribute_value_char") != null)
				attrValue.setAttribute_value_char(rs.getString("attribute_value_char").charAt(0));
				attrValue.setAttribute_value_bit(rs.getInt("attribute_value_bit"));
				if (rs.wasNull()) {
					attrValue.setAttribute_value_bit(null);
			    }
				attrValue.setAttribute_value_bigint(rs.getInt("attribute_value_bigint"));
				if (rs.wasNull()) {
					attrValue.setAttribute_value_bigint(null);
			    }
				attrValue.setAttribute_name(rs.getString("attribute_name"));
			
				list_attr.add(attrValue);
				
			}
			rs.close();
			return list_attr;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 MiscUtils.getLogger().error(e.toString());
		}

		return null;
	}

	/**
	 * insert a list of EAVFormAttributeValue
	 * @param attrValList : the list
	 * @param connection : the sql connection
	 * @throws SQLException : threw if a SQL exception is raised
	 */
	public void insertAttributeValues(List<EAVFormAttributeValue> attrValList, Connection connection) throws SQLException{
		for(EAVFormAttributeValue attrVal : attrValList){
			
		        PreparedStatement req = connection.prepareStatement("INSERT INTO eav_form_attribute_value (ref_form_entity,ref_form" 
		        			+ ",ref_field,attribute_value_varchar,attribute_value_time,attribute_value_text,attribute_value_int_unsigned,"
		        			+ "attribute_value_int,attribute_value_enum,attribute_value_double,attribute_value_date,"
		        			+ "attribute_value_char,attribute_value_bit,attribute_value_bigint,attribute_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		        
		        req.setInt(1, attrVal.getRef_form_entity());
		        req.setInt(2, attrVal.getRef_form());
		        req.setInt(3, attrVal.getRef_field());
		        req.setString(4, attrVal.getAttribute_value_varchar());
		        req.setTime(5, attrVal.getAttribute_value_time());
		        req.setString(6, attrVal.getAttribute_value_text());
		        req.setObject(7, attrVal.getAttribute_value_int_unsigned());
		        req.setObject(8, attrVal.getAttribute_value_int());
		        req.setString(9, attrVal.getAttribute_value_enum());
		        req.setObject(10, attrVal.getAttribute_value_double());
		        req.setDate( 11, attrVal.getAttribute_value_date());
		        req.setObject(12, attrVal.getAttribute_value_char());
		        req.setObject(13, attrVal.getAttribute_value_bit());
		        req.setObject(14, attrVal.getAttribute_value_bigint());
		        req.setString(15, attrVal.getAttribute_name());
		        
		        req.executeUpdate();
		        
		}
		
	}
	

}
