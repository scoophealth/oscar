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
package oscar.billing.fat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oscar.billing.fat.model.FatFormularioProcedimento;
import oscar.billing.fat.model.FatFormularios;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;


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
			form.setDsFormulario(db.getString(rs,2));
		}
		} finally {
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
            form.setDsFormulario(db.getString(rs,2));
            list.add(form);
			System.out.println("adicionou reg");
        }
        } finally {
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
                formProc.getCadProcedimentos().setDsProcedimento(db.getString(rs,2));
				formProc.getFatFormularios().setCoFormulario(Integer.parseInt(
						id));
                list.add(formProc);
                
            }
        } finally {
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
                formProc.getCadProcedimentos().setDsProcedimento(db.getString(rs,2));
                formProc.getFatFormularios().setCoFormulario(rs.getInt(3));
                list.add(formProc);
            }
        } finally {
        }

        return list;
    }
}
