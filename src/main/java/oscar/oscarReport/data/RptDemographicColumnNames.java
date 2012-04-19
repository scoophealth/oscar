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

public class RptDemographicColumnNames {
        java.util.Hashtable hstbl;
        java.util.Hashtable backhash;

        public RptDemographicColumnNames(){

        hstbl = new java.util.Hashtable();

        hstbl.put("demographic_no",new String("Demographic #"));
        hstbl.put("last_name",new String("Last Name"));
        hstbl.put("first_name",new String("First Name"));
        hstbl.put("address",new String("Address"));
        hstbl.put("city",new String("City"));
        hstbl.put("province",new String("Province"));
        hstbl.put("postal",new String("Postal Code"));
        hstbl.put("phone",new String("Phone"));
        hstbl.put("phone2",new String("Phone 2"));
        hstbl.put("year_of_birth",new String("Year of Birth"));
        hstbl.put("month_of_birth",new String("Month of Birth"));
        hstbl.put("date_of_birth",new String("Date of Birth"));
        hstbl.put("hin",new String("HIN"));
        hstbl.put("ver",new String("Version Code"));
        hstbl.put("roster_status",new String("Roster Status"));
        hstbl.put("patient_status",new String("Patient Status"));
        hstbl.put("date_joined",new String("Date Joined"));
        hstbl.put("chart_no",new String("Chart #"));
        hstbl.put("provider_no",new String("Provider #"));
        hstbl.put("provider_name",new String("Provider Name"));
        hstbl.put("sex",new String("Sex"));
        hstbl.put("end_date",new String("End Date"));
        hstbl.put("eff_date",new String("Eff. Date"));
        hstbl.put("pcn_indicator",new String("Pcn indicator"));
        hstbl.put("hc_type",new String("HC Type"));
        hstbl.put("hc_renew_date",new String("HC Renew Date"));
        hstbl.put("family_doctor",new String("Family Doctor"));
        hstbl.put("newsletter", new String("Newsletter"));
        hstbl.put("email", new String("Email"));

        backhash = new java.util.Hashtable();

        backhash.put("Demographic #",new String("demographic_no"));
        backhash.put("Last Name",new String("last_name"));
        backhash.put("First Name",new String("first_name"));
        backhash.put("Address",new String("address"));
        backhash.put("City",new String("city"));
        backhash.put("Province",new String("province"));
        backhash.put("Postal Code",new String("postal"));
        backhash.put("Phone",new String("phone"));
        backhash.put("Phone 2",new String("phone2"));
        backhash.put("Year of Birth",new String("year_of_birth"));
        backhash.put("Month of Birth",new String("month_of_birth"));
        backhash.put("Date of Birth",new String("date_of_birth"));
        backhash.put("HIN",new String("hin"));
        backhash.put("Version Code",new String("ver"));
        backhash.put("Roster Status",new String("roster_status"));
        backhash.put("Patient Status",new String("patient_status"));
        backhash.put("Date Joined",new String("date_joined"));
        backhash.put("Chart #",new String("chart_no"));
        backhash.put("Provider #",new String("provider_no"));        
        backhash.put("Sex",new String("sex"));
        backhash.put("End Date",new String("end_date"));
        backhash.put("Eff. Date",new String("eff_date"));
        backhash.put("Pcn indicator",new String("pcn_indicator"));
        backhash.put("HC Type",new String("hc_type"));
        backhash.put("HC Renew Date",new String("hc_renew_date"));
        backhash.put("Family Doctor",new String("family_doctor"));
        backhash.put("Random",new String("rand()"));
        backhash.put("newsletter", new String("Newsletter"));
        backhash.put("email", new String("Email"));
        }


    public String getColumnTitle(String str){
        if (hstbl != null){
            return (String) hstbl.get(str);
        }else{
            return new String();
        }
    }

    public String getColumnName(String str){
        if (backhash != null){
            return (String) backhash.get(str);
        }else{
            return new String();
        }
    }








}
