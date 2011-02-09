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
/*
 * TipoMovHandler.java
 *
 * Created on September 24, 2003, 10:55 AM
 */
package oscar.billing.cad.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oscar.billing.cad.model.CadAtividadesSaude;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;


/**
 *
 * @author  lilian
 */
public class CadAtividadeSaudeDAO extends DAO {

    public List list(String codigo, String desc) throws SQLException {
        List beans = null;
        String sql = "select co_atividade, ds_atividade from cad_atividades_saude ";

        if ((codigo != null) && !codigo.trim().equals("")) {
            sql = sql + " where co_atividade like '" + codigo.trim() + "%'";

            if ((desc != null) && !desc.trim().equals("")) {
                sql = sql + " and ds_atividade like '" +
                    desc.trim().toUpperCase() + "%'";
            }
        } else {
            if ((desc != null) && !desc.trim().equals("")) {
                sql = sql + "  where ds_atividade like '" +
                    desc.trim().toUpperCase() + "%'";
            }
        }

        sql = sql + " order by ds_atividade";

        

        ResultSet rs = DBHandler.GetSQL(sql);

        if (rs != null) {
            beans = new ArrayList();

            while (rs.next()) {
                CadAtividadesSaude bean = new CadAtividadesSaude();
                bean.setCoAtividade(rs.getLong("co_atividade"));
                bean.setDsAtividade(oscar.Misc.getString(rs, "ds_atividade"));
                beans.add(bean);
            }
        }

        return beans;
    }
}
