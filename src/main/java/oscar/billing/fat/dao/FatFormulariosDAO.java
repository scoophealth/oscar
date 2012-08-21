
package oscar.billing.fat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.billing.fat.model.FatFormularioProcedimento;
import oscar.billing.fat.model.FatFormularios;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;

@Deprecated
public class FatFormulariosDAO extends DAO {

	public FatFormularios retrieve(String id) throws SQLException {
		FatFormularios form = new FatFormularios();
		String sql = "select co_formulario, ds_formulario from fat_formularios where st_ativo = 'S' and co_formulario = " + id;



		try {
		ResultSet rs = DBHandler.GetSQL(sql);

		if (rs.next()) {
			form.setCoFormulario(rs.getInt(1));
			form.setDsFormulario(oscar.Misc.getString(rs, 2));
		}
		} finally {
			//empty
		}

		return form;
	}

    public List<FatFormularios> list() throws SQLException {
        ArrayList<FatFormularios> list = new ArrayList<FatFormularios>();
        String sql = "select co_formulario, ds_formulario from fat_formularios where st_ativo = 'S'";
		MiscUtils.getLogger().debug("sql = " + sql);



        try {
        ResultSet rs = DBHandler.GetSQL(sql);
        MiscUtils.getLogger().debug("executou sql");

        while (rs.next()) {
            FatFormularios form = new FatFormularios();
            form.setCoFormulario(rs.getInt(1));
            form.setDsFormulario(oscar.Misc.getString(rs, 2));
            list.add(form);
			MiscUtils.getLogger().debug("adicionou reg");
        }
        } finally {
        	//empty
        }

        return list;
    }

    public List<FatFormularioProcedimento> listProcedimentoByForm(String id) throws SQLException {
        ArrayList<FatFormularioProcedimento> list = new ArrayList<FatFormularioProcedimento>();
        String sql =
            "select a.co_procedimento, b.ds_procedimento from rl_formulario_procedimento a, cad_procedimentos b where a.co_procedimento = b.co_procedimento and co_formulario = + " +
            id;



        try {
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                FatFormularioProcedimento formProc = new FatFormularioProcedimento();
                formProc.getCadProcedimentos().setCoProcedimento(rs.getInt(1));
                formProc.getCadProcedimentos().setDsProcedimento(oscar.Misc.getString(rs, 2));
				formProc.getFatFormularios().setCoFormulario(Integer.parseInt(
						id));
                list.add(formProc);

            }
        } finally {
        	//empty
        }

        return list;
    }

    public List<FatFormularioProcedimento> listProcedimentoByProc(String[] ids) throws SQLException {
        ArrayList<FatFormularioProcedimento> list = new ArrayList<FatFormularioProcedimento>();
        String sql =
            "select a.co_procedimento, b.ds_procedimento, a.co_formulario from rl_formulario_procedimento a, cad_procedimentos b where a.co_procedimento = b.co_procedimento and a.co_procedimento in (" +
            getStrIn(ids) + ")";



        try {
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()) {
                FatFormularioProcedimento formProc = new FatFormularioProcedimento();
                formProc.getCadProcedimentos().setCoProcedimento(rs.getInt(1));
                formProc.getCadProcedimentos().setDsProcedimento(oscar.Misc.getString(rs, 2));
                formProc.getFatFormularios().setCoFormulario(rs.getInt(3));
                list.add(formProc);
            }
        } finally {
        	//empty
        }

        return list;
    }
}
