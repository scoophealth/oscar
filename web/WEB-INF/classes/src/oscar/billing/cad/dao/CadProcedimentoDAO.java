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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
/*
 * TipoMovHandler.java
 *
 * Created on September 24, 2003, 10:55 AM
 */
package oscar.billing.cad.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oscar.billing.cad.model.CadProcedimentos;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;
import oscar.util.DAO;
import oscar.util.FieldTypes;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;


/**
 *
 * @author  lilian
 */
public class CadProcedimentoDAO extends DAO {
    public CadProcedimentoDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public List list(String codigo, String desc) throws SQLException {
        List beans = null;
        String sql = "select co_procedimento, ds_procedimento from cad_procedimentos where tp_procedimento = ? and st_procedimento = ?";

        if ((codigo != null) && !codigo.trim().equals("")) {
            sql = sql + " and co_procedimento = " + codigo.trim();
        }

        if ((desc != null) && !desc.trim().equals("")) {
            sql = sql + " and ds_procedimento like '" +
                desc.trim().toUpperCase() + "%'";
        }

        sql = sql + " order by ds_procedimento";

        DBPreparedHandlerAdvanced db = new DBPreparedHandlerAdvanced();
        PreparedStatement pstmProc = db.getPrepareStatement(sql);

        try {
            SqlUtils.fillPreparedStatement(pstmProc, 1,
                CadProcedimentos.AMBULATORIAL, FieldTypes.CHAR);
            SqlUtils.fillPreparedStatement(pstmProc, 2, CadProcedimentos.ATIVO,
                FieldTypes.CHAR);

            ResultSet rs = db.executeQuery(pstmProc);
            beans = new ArrayList();

            while (rs.next()) {
                CadProcedimentos bean = new CadProcedimentos();
                bean.setCoProcedimento(rs.getLong("co_procedimento"));
                bean.setDsProcedimento(db.getString(rs,"ds_procedimento"));
                beans.add(bean);
            }
        } catch (Exception err) {
            err.printStackTrace();
            throw new SQLException(err.getMessage());
        }

        return beans;
    }

    public List list(String[] ids) throws SQLException {
        List beans = null;
        String sql =
            "select co_procedimento, ds_procedimento from cad_procedimentos where co_procedimento in (" +
            StringUtils.getStrIn(ids) + ") order by ds_procedimento";

        try {
            DBHandler db = getDb();
            ResultSet rs = db.GetSQL(sql);

            if (rs != null) {
                beans = new ArrayList();

                while (rs.next()) {
                    CadProcedimentos bean = new CadProcedimentos();
                    bean.setCoProcedimento(rs.getLong("co_procedimento"));
                    bean.setDsProcedimento(db.getString(rs,"ds_procedimento"));
                    beans.add(bean);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            throw new SQLException(err.getMessage());
        }

        return beans;
    }

    public CadProcedimentos retrieve(String id) throws SQLException {
        CadProcedimentos bean = new CadProcedimentos();
        String sql =
            "select co_procedimento, ds_procedimento from cad_procedimentos where co_procedimento = " +
            id + " order by ds_procedimento";

        try {
            DBHandler db = getDb();
            ResultSet rs = db.GetSQL(sql);

            if (rs.next()) {
                bean.setCoProcedimento(rs.getLong("co_procedimento"));
                bean.setDsProcedimento(db.getString(rs,"ds_procedimento"));
            }
        } catch (Exception err) {
            err.printStackTrace();
            throw new SQLException(err.getMessage());
        }

        return bean;
    }
}
