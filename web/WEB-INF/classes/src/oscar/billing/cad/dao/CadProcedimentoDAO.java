/*
 * TipoMovHandler.java
 *
 * Created on September 24, 2003, 10:55 AM
 */
package oscar.billing.cad.dao;

import oscar.billing.cad.model.CadProcedimentos;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerAdvanced;

import oscar.util.DAO;
import oscar.util.FieldTypes;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


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

        DBPreparedHandlerAdvanced db = getDBPreparedHandlerAdvanced();
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
                bean.setDsProcedimento(rs.getString("ds_procedimento"));
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
                    bean.setDsProcedimento(rs.getString("ds_procedimento"));
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
                bean.setDsProcedimento(rs.getString("ds_procedimento"));
            }
        } catch (Exception err) {
            err.printStackTrace();
            throw new SQLException(err.getMessage());
        }

        return bean;
    }
}
