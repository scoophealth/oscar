/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;


public class VisitReportData {


  private String dateBegin=null;
  private String dateEnd=null;
  private String providerNo=null;

  public VisitReportData() {}

 	  public void setDateBegin(String value) {
	    dateBegin=value;
	  }

 	  public void setDateEnd(String value) {
	    dateEnd=value;
	  }

	   public void setProviderNo(String value) {
	  	    providerNo=value;
	  }


    public String[] getCreatorCount(){
       String retval = "";
       String retcount = "";
       String[] retVisit = new String[6];
       retVisit[0] = "0";
       retVisit[1] = "0";
       retVisit[2] = "0";
       retVisit[3] = "0";
       retVisit[4] = "0";
       retVisit[5] = "0";

       try{
             
             String sql = "select Right(visittype, 1) visit, count(*) n from billing where status<>'D' and appointment_no<>'0' and creator='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
             MiscUtils.getLogger().debug(sql);
             if (OscarProperties.getInstance().getBooleanProperty("isNewONbilling","true")){
                sql = "select Right(visittype, 1) visit, count(*) n from billing_on_cheader1 where status<>'D' and appointment_no<>'0' and creator='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
             }
             MiscUtils.getLogger().debug(sql);
             ResultSet rs = DBHandler.GetSQL(sql);
             while (rs.next()){
                retval = oscar.Misc.getString(rs, "visit");
                retcount =oscar.Misc.getString(rs, "n");
                retVisit[Integer.parseInt(retval)] = retcount;

			}
             rs.close();
          }
          catch(SQLException e){
             MiscUtils.getLogger().debug("There has been an error while retrieving a visit count");
             MiscUtils.getLogger().error("Error", e);
          }

       return retVisit;
    }

        public String[] getApptProviderCount(){
	       String retval = "";
	       String retcount = "";
	       String[] retVisit = new String[6];
	       retVisit[0] = "0";
	       retVisit[1] = "0";
	       retVisit[2] = "0";
	       retVisit[3] = "0";
	       retVisit[4] = "0";
               retVisit[5] = "0";

	       try{
	             
	             String sql = "select Right(visittype, 1) visit, count(*) n from billing where status<>'D' and appointment_no<>'0' and apptProvider_no='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
                     MiscUtils.getLogger().debug(sql);
                     if (OscarProperties.getInstance().getBooleanProperty("isNewONbilling","true")){
                        sql = "select Right(visittype, 1) visit, count(*) n from billing_on_cheader1 where status<>'D' and appointment_no<>'0' and apptProvider_no='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
                     }
                     MiscUtils.getLogger().debug(sql);
                     ResultSet rs = DBHandler.GetSQL(sql);
	             while (rs.next()){
	                retval = oscar.Misc.getString(rs, "visit");
	                retcount =oscar.Misc.getString(rs, "n");
	                retVisit[Integer.parseInt(retval)] = retcount;

				}
	             rs.close();
	          }
	          catch(SQLException e){
	             MiscUtils.getLogger().debug("There has been an error while retrieving a visit count");
	             MiscUtils.getLogger().error("Error", e);
	          }

	       return retVisit;
    }

  }
