/**
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
 * Jason Gallagher
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of DemographicSets
 *
 *
 */

package oscar.oscarReport.data;
import oscar.oscarDB.DBHandler;
import oscar.oscarReport.pageUtil.RptDemographicReportForm;
import oscar.util.UtilDateUtilities;


public class RptDemographicQueryBuilder {


    int theWhereFlag;
    int theFirstFlag;
    StringBuffer stringBuffer = null;

    public void whereClause(){
        if (stringBuffer != null){
            if (theWhereFlag == 0){
            stringBuffer.append(" where ");
            theWhereFlag = 1;
            }
        }
    }

    public void firstClause(){
        if (theFirstFlag != 0){
                stringBuffer.append(" and ");
                theFirstFlag = 1 ;
           }
    }

    public RptDemographicQueryBuilder() {
    }
    
    public java.util.ArrayList buildQuery(RptDemographicReportForm frm){
        return buildQuery(frm,null);
    }

    public java.util.ArrayList buildQuery(RptDemographicReportForm frm,String asofRosterDate){
      System.out.println("in buildQuery");

        String[] select = frm.getSelect();
        stringBuffer =  new StringBuffer("select " );

        String ageStyle         = frm.getAgeStyle();
        String yearStyle        = frm.getAge();
        String startYear        = frm.getStartYear();
        String endYear          = frm.getEndYear();
        String[] rosterStatus   = frm.getRosterStatus();
        String[] patientStatus  = frm.getPatientStatus();
        String[] providers      = frm.getProviderNo();

        
        String firstName        = frm.getFirstName();
        String lastName         = frm.getLastName();
        String sex              = frm.getSex();
        String queryName        = frm.getQueryName();
        

        String orderBy          = frm.getOrderBy();
        String limit            = frm.getResultNum();
        
        
        String asofDate         = frm.getAsofDate();

        
        if (UtilDateUtilities.getDateFromString(asofDate,"yyyy-MM-dd") == null){
           asofDate = "CURRENT_DATE";
        }else{
           asofDate = "'"+asofDate+"'";
        }
        

        RptDemographicColumnNames demoCols = new RptDemographicColumnNames();

        oscar.oscarMessenger.util.MsgStringQuote s = new oscar.oscarMessenger.util.MsgStringQuote();
        if (firstName != null ){
            firstName = firstName.trim();
        }

        if (lastName != null ){
            lastName = lastName.trim();
        }

        if (sex != null){
            sex = sex.trim();
        }

        theWhereFlag = 0;
        theFirstFlag = 0;

        boolean getprovider = false;
        for (int i = 0; i < select.length ; i++){
            if( select[i].equalsIgnoreCase("provider_name") ) {
                stringBuffer.append(" concat(p.last_name,', ',p.first_name) " + select[i] + " ");
                getprovider = true;
                if (i < (select.length - 1)){
                    stringBuffer.append(", ");
                }
                continue;
            }
            if (i == (select.length - 1)){
                stringBuffer.append(" d."+select[i]+" ");
            }else{
                stringBuffer.append(" d."+select[i]+", ");
            }

        }

        stringBuffer.append(" from demographic d ");
        if( getprovider ) {
            stringBuffer.append(", provider p");
        }
        int yStyle= 0;
        try{
            yStyle = Integer.parseInt(yearStyle);
        }catch (Exception e){}

        
       // value="0"> nothing specified
       // value="1">born before
       // value="2">born after
       // value="3">born in
       // value="4">born between

        /*switch (yStyle){
            case 1:
                whereClause();
                stringBuffer.append(" ( year_of_birth < "+startYear+"  )");
                theFirstFlag = 1;
                break;
            case 2:
                whereClause();
                stringBuffer.append(" ( year_of_birth > "+startYear+"  )");
                theFirstFlag = 1;
                break;
            case 3:
                whereClause();
                stringBuffer.append(" ( year_of_birth = "+startYear+"  )");
                theFirstFlag = 1;
                break;
            case 4:
                whereClause();
                stringBuffer.append(" ( year_of_birth > "+startYear+" and year_of_birth < "+endYear+" ) ");
                theFirstFlag = 1;
                break;
        }*/
       // value="0"> nothing specified
       // value="1">born before
       // value="2">born after
       // value="3">born in
       // value="4">born between

       System.out.println("date style"+yStyle);
        switch (yStyle){
            case 1:
                whereClause();
                if (ageStyle.equals("1")){
                   stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) <  "+startYear+" ) ");
                }else{
                   stringBuffer.append(" ( YEAR("+asofDate+") - d.year_of_birth < "+startYear+"  ) ");
                }
                theFirstFlag = 1;
                break;
            case 2:
                whereClause();
                //if (ageStyle.equals("1")){
                   stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) >  "+startYear+" ) ");
                //}else{
                //   stringBuffer.append(" ( YEAR("+asofDate+") - year_of_birth > "+startYear+"  ) ");
                //}
                theFirstFlag = 1;
                break;
            case 3:
                whereClause();
                if (ageStyle.equals("1")){
                  stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) =  "+startYear+" ) ");
                }else{
                  stringBuffer.append(" ( YEAR("+asofDate+") - d.year_of_birth = "+startYear+"  ) ");
                }
                theFirstFlag = 1;
                break;
            case 4:
                whereClause();
                System.out.println("age style "+ageStyle);
                if (!ageStyle.equals("2")){
                  // stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth),'-',(month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >  "+startYear+" and ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth),'-',(month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) <  "+endYear+"  ) ");
                  System.out.println("VERIFYING INT"+startYear);
                  //check to see if its a number 
                  if ( verifyInt (startYear) ){
                     stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) >  "+startYear+" ) ");
                  }else{
                     String interval = getInterval(startYear);
                     stringBuffer.append(" ( date_sub("+asofDate+",interval "+interval+") >= DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d')   ) ");
                  }
                  stringBuffer.append(" and ");
                  if ( verifyInt (endYear) ){
                    stringBuffer.append(" ( ( YEAR("+asofDate+") -YEAR (DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT("+asofDate+",5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'),5)) <  "+endYear+"  ) ");
                  }else{
                      ///
                    String interval = getInterval(endYear);
                    stringBuffer.append(" ( date_sub("+asofDate+",interval "+interval+") < DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d')   ) ");
                  }
                }else{
                  stringBuffer.append(" ( YEAR("+asofDate+") - d.year_of_birth > "+startYear+"  and YEAR("+asofDate+") - d.year_of_birth < "+endYear+"  ) ");
                }
                theFirstFlag = 1;
                break;
        }



        if ( rosterStatus != null ){
            whereClause();
            firstClause();
            stringBuffer.append(" ( ");
            for (int i = 0; i < rosterStatus.length ; i++){
                theFirstFlag = 1;
                if (i == (rosterStatus.length - 1)){
                    stringBuffer.append(" d.roster_status = '"+rosterStatus[i]+"' )");
                }else{
                    stringBuffer.append(" d.roster_status = '"+rosterStatus[i]+"' or  ");
                }
            }
        }

        if ( patientStatus != null ){
            whereClause();
            firstClause();
            stringBuffer.append(" ( ");
            for (int i = 0; i < patientStatus.length ; i++){
                theFirstFlag = 1;
                if (i == (patientStatus.length - 1)){
                    stringBuffer.append(" d.patient_status = '"+patientStatus[i]+"' )");
                }else{
                    stringBuffer.append(" d.patient_status = '"+patientStatus[i]+"' or  ");
                }
            }
        }


        if ( providers != null ){
            whereClause();
            firstClause();
            stringBuffer.append(" ( ");
            for (int i = 0; i < providers.length ; i++){
                theFirstFlag = 1;
                if (i == (providers.length - 1)){
                    stringBuffer.append(" d.provider_no = '"+providers[i]+"' )");
                }else{
                    stringBuffer.append(" d.provider_no = '"+providers[i]+"' or  ");
                }
            }
        }

        if (lastName != null && lastName.length() != 0 ){
            System.out.println("last name = "+lastName+"<size = "+lastName.length());
            whereClause();
            firstClause();
            theFirstFlag = 1;
            stringBuffer.append(" ( ");
            stringBuffer.append(" d.last_name like '"+s.q(lastName)+"%'");
            stringBuffer.append(" ) ");
        }

        if (firstName != null && firstName.length() != 0 ){
            whereClause();
            firstClause();
            theFirstFlag = 1;
            stringBuffer.append(" ( ");
            stringBuffer.append(" d.first_name like '"+s.q(firstName)+"%'");
            stringBuffer.append(" ) ");
        }

       yStyle = 0;
       try{
            yStyle = Integer.parseInt(sex);
       }catch (Exception e){}
       switch (yStyle){
            case 1:
                whereClause();
                firstClause();
                stringBuffer.append(" ( d.sex =  'F'  )");
                theFirstFlag = 1;
                break;
            case 2:
                whereClause();
                firstClause();
                stringBuffer.append(" ( d.sex = 'M' )");
                theFirstFlag = 1;
                break;

       }

       if (asofRosterDate != null){
           whereClause();
           firstClause();
           stringBuffer.append(" ( d.hc_renew_date <= '"+asofRosterDate+"')");
           theFirstFlag = 1;
       }

       if( getprovider ) {
           whereClause();
           firstClause();
           stringBuffer.append(" ( d.provider_no = p.provider_no )");
       }

       if (orderBy != null && orderBy.length() != 0 ){
            if (!orderBy.equals("0")){
                stringBuffer.append(" order by "+ demoCols.getColumnName(orderBy)+" ");
            }
       }

       if (limit != null && limit.length() != 0 ){
            if (!limit.equals("0")){
                try{
                    Integer.parseInt(limit);
                    stringBuffer.append(" limit "+limit+" ");
                }
                catch(Exception u){System.out.println("limit was not numeric >"+limit+"<");}
            }
       }



        System.out.println("SEARCH SQL STATEMENT \n"+stringBuffer.toString());
        java.util.ArrayList searchedArray = new java.util.ArrayList();
        try{
              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              java.sql.ResultSet rs;
              rs = db.GetSQL(stringBuffer.toString());
              System.out.println(stringBuffer.toString());

              while (rs.next()) {

                java.util.ArrayList tempArr  = new java.util.ArrayList();
                for (int i = 0; i < select.length ; i++){
                   tempArr.add( db.getString(rs,select[i]) );
                }
                searchedArray.add(tempArr);

              }



              rs.close();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }


    return searchedArray;
    }

   boolean verifyInt(String str){
      boolean verify = true;
      try{
         Integer.parseInt(str);
      }catch(Exception e){
         verify = false;
      }
      return verify;
   }
   
   String  getInterval(String startYear){
      System.out.println("in getInterval startYear "+startYear); 
      String str = "";
      if (startYear.charAt(startYear.length()-1) == 'm' ){
         str = startYear.substring(0,(startYear.length()-1)) + " month";
      }
      System.out.println(str);
      return str;
   }
}