// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;

public class EctViewConsultationRequestsUtil {

    public boolean estConsultationVecByTeam(String team) {
        ids = new Vector();
        status = new Vector();
        patient = new Vector();
        provider = new Vector();
        service = new Vector();
        date = new Vector();
        boolean verdict = true;
        if(!team.equals("-1")) {
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = " select cr.status, cr.referalDate, cr.requestId, demo.last_name, demo.first_name,  pro.last_name as lName, pro.first_name as fName, ser.serviceDesc from consultationRequests cr,  demographic demo, provider pro, consultationServices ser where  demo.demographic_no = cr.demographicNo and pro.provider_no = cr.providerNo and  ser.serviceId = cr.serviceId and cr.status != 4 and sendTo ='" +team+ "' order by cr.referalDate desc";
                ResultSet rs= db.GetSQL(sql);
                while(rs.next()) {
                  date.add(rs.getString("referalDate"));
                  ids.add(rs.getString("requestId"));
                  status.add(rs.getString("status"));
                  patient.add(rs.getString("last_name") +", "+ rs.getString("first_name")) ;
                  provider.add(rs.getString("lName") +", "+ rs.getString("fName"));
                  service.add(rs.getString("serviceDesc"));
                }
                rs.close();
                db.CloseConn();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
                verdict = false;
            }
        } else {
            System.out.println("GETTIM ALL!!!!");
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = " select cr.status, cr.referalDate, cr.requestId, demo.last_name, demo.first_name,  pro.last_name as lName, pro.first_name as fName, ser.serviceDesc from consultationRequests cr,  demographic demo, provider pro, consultationServices ser where  demo.demographic_no = cr.demographicNo and pro.provider_no = cr.providerNo and  ser.serviceId = cr.serviceId and cr.status != 4 order by cr.referalDate desc";
                ResultSet rs;
                for(rs = db.GetSQL(sql); rs.next(); date.add(rs.getString("referalDate")))
                {
                    ids.add(rs.getString("requestId"));
                    status.add(rs.getString("status"));
                    patient.add(rs.getString("last_name") +", "+ rs.getString("first_name")) ;
                    provider.add(rs.getString("lName") +", "+ rs.getString("fName")) ;
                    service.add(rs.getString("serviceDesc"));
                }
                rs.close();
                db.CloseConn();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
                verdict = false;
            }
        }
        return verdict;
    }

    public boolean estConsultationVecByDemographic(String demoNo) {
        ids = new Vector();
        status = new Vector();
        patient = new Vector();
        provider = new Vector();
        service = new Vector();
        date = new Vector();
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = " select cr.status, cr.referalDate, cr.requestId, demo.last_name, demo.first_name,  pro.last_name as lName, pro.first_name as fName, ser.serviceDesc from consultationRequests cr,  demographic demo, provider pro, consultationServices ser where  demo.demographic_no = cr.demographicNo and pro.provider_no = cr.providerNo and  ser.serviceId = cr.serviceId and demographicNo ='"+demoNo+"' order by cr.referalDate ";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); date.add(rs.getString("referalDate")))
            {
                ids.add(rs.getString("requestId"));
                status.add(rs.getString("status"));
                patient.add(rs.getString("last_name")+", "+rs.getString("first_name"));
                provider.add(rs.getString("lName")+", "+rs.getString("fName"));
                service.add(rs.getString("serviceDesc"));
            }

            rs.close();
            db.CloseConn();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public Vector ids;
    public Vector status;
    public Vector patient;
    public Vector provider;
    public Vector service;
    public Vector date;
}
