package oscar.billing.cad.dao;

import oscar.billing.cad.model.CadTiposAtendimento;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * DAO used for maintaining attendance types.
 * (used in brazilian billing).
 * @author tomita - 13 nov 2003
 */
public class TipoAtendimentoDAO extends DAO {
    public TipoAtendimentoDAO(Properties pvar) throws SQLException {
        super(pvar);
    }


	/**
	 * List types
	 */
    public List list() throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "select ta.co_tipo_atendimento, ta.ds_tipo_atendimento " +
            "from cad_tipos_atendimento ta";
        
        System.out.println(sql);

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                CadTiposAtendimento tpAtend = new CadTiposAtendimento();
                tpAtend.setCoTipoatendimento(rs.getLong(1));
                tpAtend.setDsTipoatendimento(rs.getString(2));                
                list.add(tpAtend);
            }
        } finally {
            db.CloseConn();
        }
        
        return list;
    }
}
