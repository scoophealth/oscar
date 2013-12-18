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

import oscar.form.data.EAVFormEntity;

public class EAVEntityDao {

	public EAVEntityDao(){
		
	}
	
	/**
	 * Select an entity based on the existing id and the demographic_no
	 * @param id_entity :the entity_ud
	 * @param demographic_no : the demographic_no
	 * @param con : the Connection
	 * @return the EAVFormEntity
	 */
	public EAVFormEntity selectEntity(int existingID, int demographic_no, Connection con)
	{
		try {
	        PreparedStatement p = con.prepareStatement("SELECT entity_id_eav, ref_form, formEdited, formCreated, demographic_no,ID FROM eav_form_entity WHERE "
	        		+ "ID = ? AND demographic_no = ?");
	        
	        p.setInt(1, existingID);
	        p.setInt(2, demographic_no);
	        
	        EAVFormEntity entity = null;
	        ResultSet rs = p.executeQuery();
	        while(rs.next())
	        {
	        	entity = new EAVFormEntity();
	        	
	        	entity.setEntity_id_eav(rs.getInt("entity_id_eav"));
	        	entity.setRef_form(rs.getInt("ref_form"));
	        	entity.setFormCreated(rs.getDate("formCreated"));
	        	entity.setFormEdited(rs.getTimestamp("formEdited"));
	        	entity.setDemographic_no(rs.getInt("demographic_no"));
	        	entity.setId(rs.getInt("ID"));
	        	
	        	
	        }
	        
	        rs.close();
	        return entity;
	       
	        
        } catch (SQLException e) {
	        // TODO Auto-generated catch block
        	MiscUtils.getLogger().error(e.toString());
        }
		return null;
	}

	
	/**
	 * Insert a form entity into de Database
	 * @param ent : the EAVFormEntity containing the data
	 * @param con : the Database connection
	 * @throws SQLException : threw if a SQL exception is raised
	 */
	public int insertEntity(EAVFormEntity ent, Connection con) throws SQLException
	{
	        PreparedStatement p = con.prepareStatement("INSERT INTO eav_form_entity (entity_id_eav, ref_form, formEdited, formCreated, demographic_no,ID) VALUES(?,?,?,?,?,?);",PreparedStatement.RETURN_GENERATED_KEYS);
	        
	        
	        p.setInt(1,ent.getEntity_id_eav() );
	        p.setInt(2,ent.getRef_form() );
	        p.setTimestamp(3, ent.getFormEdited());
	        p.setDate(4, new java.sql.Date(ent.getFormCreated().getTime()));
	        p.setInt(5, ent.getDemographic_no());
	        p.setInt(6, ent.getId());
	        p.execute();
	        ResultSet rs = p.getGeneratedKeys();
	        if(rs.next()){
	        	return rs.getInt(1);
	        }
	        return -1;
	        
	}
}
