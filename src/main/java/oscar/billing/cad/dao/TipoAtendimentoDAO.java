
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
@Deprecated
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
