/*
 * TipoMovHandler.java
 *
 * Created on September 24, 2003, 10:55 AM
 */
package oscar.billing.cad.dao;

import oscar.billing.cad.model.CadAtividadesSaude;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 *
 * @author  lilian
 */
public class CadAtividadeSaudeDAO extends DAO {
    public CadAtividadeSaudeDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

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

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            if (rs != null) {
                beans = new ArrayList();

                while (rs.next()) {
                    CadAtividadesSaude bean = new CadAtividadesSaude();
                    bean.setCoAtividade(rs.getLong("co_atividade"));
                    bean.setDsAtividade(rs.getString("ds_atividade"));
                    beans.add(bean);
                }
            }
        } finally {
            db.CloseConn();
        }

        return beans;
    }
}
