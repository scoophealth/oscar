/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.billing.cad.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oscar.billing.cad.model.CadTiposAtendimento;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;


/**
 * DAO used for maintaining attendance types.
 * (used in brazilian billing).
 * @author tomita - 13 nov 2003
 */
public class TipoAtendimentoDAO extends DAO {

	/**
	 * List types
	 */
    public List list() throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "select ta.co_tipo_atendimento, ta.ds_tipo_atendimento " +
            "from cad_tipos_atendimento ta";
        
        

        try {
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                CadTiposAtendimento tpAtend = new CadTiposAtendimento();
                tpAtend.setCoTipoatendimento(rs.getLong(1));
                tpAtend.setDsTipoatendimento(oscar.Misc.getString(rs, 2));                
                list.add(tpAtend);
            }
        } finally {
        }
        
        return list;
    }
}
