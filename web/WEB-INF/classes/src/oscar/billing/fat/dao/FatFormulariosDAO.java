package oscar.billing.fat.dao;

import oscar.billing.fat.model.FatFormularioProcedimento;
import oscar.billing.fat.model.FatFormularios;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class FatFormulariosDAO extends DAO {
    public FatFormulariosDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

	public FatFormularios retrieve(String id) throws SQLException {
		FatFormularios form = new FatFormularios();
		String sql = "select co_formulario, ds_formulario from fat_formularios where st_ativo = 'S' and co_formulario = " + id;

		DBHandler db = getDb();

		try {
		ResultSet rs = db.GetSQL(sql);

		if (rs.next()) {
			form.setCoFormulario(rs.getInt(1));
			form.setDsFormulario(rs.getString(2));
		}
		} finally {
			db.CloseConn();
		}

		return form;
	}

    public List list() throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "select co_formulario, ds_formulario from fat_formularios where st_ativo = 'S'";
		System.out.println("sql = " + sql);

        DBHandler db = getDb();

        try {
        ResultSet rs = db.GetSQL(sql);
        System.out.println("executou sql");

        while (rs.next()) {
            FatFormularios form = new FatFormularios();
            form.setCoFormulario(rs.getInt(1));
            form.setDsFormulario(rs.getString(2));
            list.add(form);
			System.out.println("adicionou reg");
        }
        } finally {
			db.CloseConn();
        }

        return list;
    }

    public List listProcedimentoByForm(String id) throws SQLException {
        ArrayList list = new ArrayList();
        String sql =
            "select a.co_procedimento, b.ds_procedimento from rl_formulario_procedimento a, cad_procedimentos b where a.co_procedimento = b.co_procedimento and co_formulario = + " +
            id;

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                FatFormularioProcedimento formProc = new FatFormularioProcedimento();
                formProc.getCadProcedimentos().setCoProcedimento(rs.getInt(1));
                formProc.getCadProcedimentos().setDsProcedimento(rs.getString(2));
				formProc.getFatFormularios().setCoFormulario(Integer.parseInt(
						id));
                list.add(formProc);
                
            }
        } finally {
            db.CloseConn();
        }

        return list;
    }

    public List listProcedimentoByProc(String[] ids) throws SQLException {
        ArrayList list = new ArrayList();
        String sql =
            "select a.co_procedimento, b.ds_procedimento, a.co_formulario from rl_formulario_procedimento a, cad_procedimentos b where a.co_procedimento = b.co_procedimento and a.co_procedimento in (" +
            getStrIn(ids) + ")";

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);

            while (rs.next()) {
                FatFormularioProcedimento formProc = new FatFormularioProcedimento();
                formProc.getCadProcedimentos().setCoProcedimento(rs.getInt(1));
                formProc.getCadProcedimentos().setDsProcedimento(rs.getString(2));
                formProc.getFatFormularios().setCoFormulario(rs.getInt(3));
                list.add(formProc);
            }
        } finally {
            db.CloseConn();
        }

        return list;
    }
}
