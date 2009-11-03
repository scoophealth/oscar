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
package oscar.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import oscar.billing.model.ProcedimentoRealizado;
import oscar.oscarDB.DBHandler;
import oscar.util.DAO;
import oscar.util.DateUtils;


public class ProcedimentoRealizadoDAO extends DAO {
    public ProcedimentoRealizadoDAO(Properties pvar) throws SQLException {
        super(pvar);
    }


	/**
	 * Lists all done procedures within a given appointment.
	 * @param id - the appointment id.
	 * @returns a List of the procedures found.
	 */
    public List list(String id) throws SQLException {
        ArrayList list = new ArrayList();
        String sql =
            "select pr.co_procedimento, p.ds_procedimento, " +
            "pr.dt_realizacao, pr.co_tipo_atendimento " +
            "from cad_procedimento_realizado pr, cad_procedimentos p " +
            "where pr.co_procedimento = p.co_procedimento and " +
            "pr.appointment_no = " + id;
            
        System.out.println(sql);

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);
            
            // for each procedure found, assemble the 
            // object and put it in the list
            while (rs.next()) {
                ProcedimentoRealizado pr = new ProcedimentoRealizado();
                pr.getCadProcedimentos().setCoProcedimento(rs.getLong(1));
                pr.getCadProcedimentos().setDsProcedimento(db.getString(rs,2));
                pr.setDtRealizacao(rs.getDate(3,
                        DateUtils.getDateFormatter().getCalendar()));
                pr.getTpAtendimento().setCoTipoatendimento(rs.getLong(4));
                pr.getAppointment().setAppointmentNo(Long.parseLong(id));
                pr.setSave(true);
                list.add(pr);
            }
        } finally {
        }

        return list;
    }


	/**
	 * Update the attendance type of all done procedures within a given 
	 * appointment. tomita - 14 nov 2003
	 * @param id - the appointment id.
	 * @param atType - the attendance type
	 */
    public void updateAttendanceType(String id, String atType) throws SQLException {

        String sql = "UPDATE cad_procedimento_realizado " +
        	"SET co_tipo_atendimento = '" + atType + "' " +
        	"WHERE appointment_no = " + id;
        	
        System.out.println(sql);

        DBHandler db = getDb();
        try {
            db.RunSQL(sql);
        } finally {
        }

        return;
    }


}
