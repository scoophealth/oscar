
/*
 * TipoMovHandler.java
 *
 * Created on September 24, 2003, 10:55 AM
 */
package oscar.billing.cad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.model.CadProcedimentos;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;
import oscar.util.FieldTypes;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;


/**
 *
 * @author  lilian
 */
@Deprecated
public class CadProcedimentoDAO extends DAO {

    public List<CadProcedimentos> list(String codigo, String desc) throws SQLException {
        List<CadProcedimentos> beans = null;
        String sql = "select co_procedimento, ds_procedimento from cad_procedimentos where tp_procedimento = ? and st_procedimento = ?";

        if ((codigo != null) && !codigo.trim().equals("")) {
            sql = sql + " and co_procedimento = " + codigo.trim();
        }

        if ((desc != null) && !desc.trim().equals("")) {
            sql = sql + " and ds_procedimento like '" +
                desc.trim().toUpperCase() + "%'";
        }

        sql = sql + " order by ds_procedimento";

        Connection c=DbConnectionFilter.getThreadLocalDbConnection();
        PreparedStatement pstmProc = c.prepareStatement(sql);

        try {
            SqlUtils.fillPreparedStatement(pstmProc, 1,
                CadProcedimentos.AMBULATORIAL, FieldTypes.CHAR);
            SqlUtils.fillPreparedStatement(pstmProc, 2, CadProcedimentos.ATIVO,
                FieldTypes.CHAR);

            ResultSet rs = pstmProc.executeQuery();
            beans = new ArrayList<CadProcedimentos>();

            while (rs.next()) {
                CadProcedimentos bean = new CadProcedimentos();
                bean.setCoProcedimento(rs.getLong("co_procedimento"));
                String temp=rs.getString("ds_procedimento");
                if (temp==null) temp="";
                bean.setDsProcedimento(temp);
                beans.add(bean);
            }
        } catch (Exception err) {
        	MiscUtils.getLogger().error("Error", err);
            throw new SQLException(err.getMessage());
        }

        return beans;
    }

    public List<CadProcedimentos> list(String[] ids) throws SQLException {
        List<CadProcedimentos> beans = null;
        String sql =
            "select co_procedimento, ds_procedimento from cad_procedimentos where co_procedimento in (" +
            StringUtils.getStrIn(ids) + ") order by ds_procedimento";

        try {

            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs != null) {
                beans = new ArrayList<CadProcedimentos>();

                while (rs.next()) {
                    CadProcedimentos bean = new CadProcedimentos();
                    bean.setCoProcedimento(rs.getLong("co_procedimento"));
                    bean.setDsProcedimento(oscar.Misc.getString(rs, "ds_procedimento"));
                    beans.add(bean);
                }
            }
        } catch (Exception err) {
        	MiscUtils.getLogger().error("Error", err);
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

            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                bean.setCoProcedimento(rs.getLong("co_procedimento"));
                bean.setDsProcedimento(oscar.Misc.getString(rs, "ds_procedimento"));
            }
        } catch (Exception err) {
        	MiscUtils.getLogger().error("Error", err);
            throw new SQLException(err.getMessage());
        }

        return bean;
    }
}
