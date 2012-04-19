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


package oscar.oscarReport.pageUtil;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

public final class RptDemographicReportForm extends ActionForm {
        public String[] select         = null;
        public String age              = null;
        public String startYear        = null;
        public String endYear          = null;
        public String firstName        = null;
        public String lastName         = null;
        public String[] rosterStatus   = null;
        public String sex              = null;
        public String[] providerNo     = null;
        public String[] patientStatus  = null;
        public String query            = null;
        public String queryName        = null;
        public String savedQuery       = null;
        public String orderBy          = null;
        public String resultNum        = null;
        public String ageStyle         = null;
        public String asofDate         = null;
        

        ////////////////////////////////////////////////////////////////////////
        public void copyConstructor(RptDemographicReportForm drf){
            this.select         = drf.select;
            this.age            = drf.age;
            this.startYear      = drf.startYear;
            this.endYear        = drf.endYear;
            this.firstName      = drf.firstName;
            this.lastName       = drf.lastName;
            this.rosterStatus   = drf.rosterStatus;
            this.sex            = drf.sex;
            this.providerNo     = drf.providerNo;
            this.patientStatus  = drf.patientStatus;
            this.asofDate       = drf.asofDate;

///            this.queryName      = ;
//            this.savedQuery     = ;
        }
        //=---------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////
        public String getQuery(){
            return query;
        }

        public void setQuery( String str){
            MiscUtils.getLogger().debug("setQuery "+str);
            query = str;
        }
        //=---------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////
        public String[] getSelect(){
            return select;
        }

        public void addDemoIfNotPresent(){
            if (select != null){
                List list = Arrays.asList(select);
                if (!list.contains("demographic_no")){
                    ArrayList aList = new ArrayList(list);
                    aList.add(0,"demographic_no");
                    select = (String[]) aList.toArray(select);
                }
                
            }
        }
        
        public void setSelect( String[] str){
            select = str;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getAge(){
            return age;
        }
        public void setAge(String age){
            this.age = age;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getStartYear(){
            return startYear;
        }
        public void setStartYear(String startYear){
            this.startYear = startYear;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getEndYear(){
            return endYear;
        }
        public void setEndYear(String endYear){
            this.endYear = endYear;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getFirstName(){
            return firstName;
        }
        public void setFirstName(String firstName){
            this.firstName = firstName;
        }
        //=---------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////
        public String getLastName(){
            return lastName;
        }
        public void setLastName(String lastName){
            this.lastName = lastName;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String[] getRosterStatus(){
            return rosterStatus;
        }
        public void setRosterStatus(String[] rosterStatus){
            this.rosterStatus = rosterStatus;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getSex(){
            return sex;
        }
        public void setSex(String sex){
            this.sex = sex;
        }
        //=---------------------------------------------------------------------



        ////////////////////////////////////////////////////////////////////////
        public String[] getProviderNo(){
            return providerNo;
        }
        public void setProviderNo(String[] providerNo){
            this.providerNo = providerNo;
        }
        //=---------------------------------------------------------------------


        ////////////////////////////////////////////////////////////////////////
        public String[] getPatientStatus(){
            return patientStatus;
        }
        public void setPatientStatus(String[] patientStatus){
            this.patientStatus = patientStatus;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getQueryName (){
            return queryName;
        }

        public void setQueryName (String str){
            this.queryName = str;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getSavedQuery (){
            return this.savedQuery ;
        }

        public void setSavedQuery (String str){
            this.savedQuery = str;
        }

        //=---------------------------------------------------------------------


        ////////////////////////////////////////////////////////////////////////
        public String getOrderBy (){
            if ( this.orderBy == null){
                this.orderBy = new String();
            }
            return this.orderBy ;
        }

        public void setOrderBy (String str){
            this.orderBy = str;
        }
        //=---------------------------------------------------------------------

        ////////////////////////////////////////////////////////////////////////
        public String getResultNum (){
            if ( this.resultNum == null){
                this.resultNum = new String();
            }
            return this.resultNum ;
        }

        public void setResultNum (String str){
            this.resultNum = str;
        }
        //=---------------------------------------------------------------------

        public String getAgeStyle (){
            if ( this.ageStyle == null){
                this.ageStyle = new String();
            }
            return this.ageStyle ;
        }

        public void setAgeStyle (String str){
            this.ageStyle = str;
        }

        //=---------------------------------------------------------------------

        public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

     ActionErrors errors = new ActionErrors();
     
     MiscUtils.getLogger().debug("step1");
//     
//
//     if ((select == null || select.length == 0) ){
//        if (!query.equals("Load Query")){
//            errors.add("select", new ActionError("error.select.zeroEntries"));
//        }
//     }
//
//     if ( !age.equals("0")){
//        int ageStyle = 0;
//        try{
//            ageStyle = Integer.parseInt(age);
//        }catch(Exception q){ errors.add("ageStyle", new ActionError("error.RPTageStyle.notNumeric")); }
//
//        switch (ageStyle){
//                case 1:
//                    if (!validateYear(startYear)){
//                       errors.add("startYear", new ActionError("error.startYear.notNumeric"));
//                    }
//                    break;
//                case 2:
//                    if (!validateYear(startYear)){
//                       errors.add("startYear", new ActionError("error.startYear.notNumeric"));
//                    }
//                    break;
//                case 3:
//                    if (!validateYear(startYear)){
//                       errors.add("startYear", new ActionError("error.startYear.notNumeric"));
//                    }
//                    break;
//                case 4:
//                    if (!validateYear(startYear)){
//                       errors.add("startYear", new ActionError("error.startYear.notNumeric"));
//                    }
//                    if (!validateYear(endYear)){
//                       errors.add("endYear", new ActionError("error.endYear.notNumeric"));
//                    }
//                    break;
//            }
//
//
//    }
//
//    if (query != null && query.equals("Save Query")){
//        if (queryName == null || queryName.trim().length() == 0){
//            errors.add("queryName", new ActionError("error.savedQuery.notFilledIn"));
//         }
//    }

     MiscUtils.getLogger().debug("step2 "+errors.size());
     return errors;
  }

  public boolean validateYear(String str){
    boolean retval = false;
    if (str != null && str.length() > 0){
            str = str.trim();
            MiscUtils.getLogger().debug("Start Year = "+str+"< len = "+str.length());
            try{
                Integer.parseInt(str);
                retval = true;
            }catch(Exception e){}
         }
    return retval;
  }
  
  /**
   * Getter for property asofDate.
   * @return Value of property asofDate.
   */
  public java.lang.String getAsofDate() {
     return asofDate;
  }
  
  /**
   * Setter for property asofDate.
   * @param asofDate New value of property asofDate.
   */
  public void setAsofDate(java.lang.String asofDate) {
     this.asofDate = asofDate;
  }
  
}
