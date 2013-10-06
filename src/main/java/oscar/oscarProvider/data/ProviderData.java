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


/*
 * ProviderData.java
 *
 * Created on August 19, 2004, 2:15 PM
 */

package oscar.oscarProvider.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author  Jay Gallagher
 * Used to access data in provider table
 */
public class ProviderData {
/*
+------------------------+--------------+------+-----+------------+-------+
| Field                  | Type         | Null | Key | Default    | Extra |
+------------------------+--------------+------+-----+------------+-------+
| provider_no            | varchar(6)   | NO   | PRI |            |       |
| last_name              | varchar(30)  | NO   |     |            |       |
| first_name             | varchar(30)  | NO   |     |            |       |
| provider_type          | varchar(15)  | NO   |     |            |       |
| specialty              | varchar(40)  | NO   |     |            |       |
| team                   | varchar(20)  | YES  |     |            |       |
| sex                    | char(1)      | NO   |     |            |       |
| dob                    | date         | YES  |     | NULL       |       |
| address                | varchar(40)  | YES  |     | NULL       |       |
| phone                  | varchar(20)  | YES  |     | NULL       |       |
| work_phone             | varchar(50)  | YES  |     | NULL       |       |
| ohip_no                | varchar(20)  | YES  |     | NULL       |       |
| rma_no                 | varchar(20)  | YES  |     | NULL       |       |
| billing_no             | varchar(20)  | YES  |     | NULL       |       |
| hso_no                 | varchar(10)  | YES  |     | NULL       |       |
| status                 | char(1)      | YES  |     | NULL       |       |
| comments               | text         | YES  |     | NULL       |       |
| provider_activity      | char(3)      | YES  |     | NULL       |       |
| practitionerNo         | varchar(20)  | YES  |     | NULL       |       |
| init                   | varchar(10)  | YES  |     | NULL       |       |
| job_title              | varchar(100) | YES  |     | NULL       |       |
| email                  | varchar(60)  | YES  |     | NULL       |       |
| title                  | varchar(20)  | YES  |     | NULL       |       |
| lastUpdateUser         | varchar(6)   | YES  |     | NULL       |       |
| lastUpdateDate         | date         | YES  |     | NULL       |       |
| signed_confidentiality | date         | YES  |     | 0001-01-01 |       |
+------------------------+--------------+------+-----+------------+-------+
*/

   String provider_no;
   String last_name;
   String first_name;
   String provider_type;
   String specialty;
   String team;
   String sex;
   String dob;
   String address;
   String phone;
   String work_phone;
   String ohip_no;
   String rma_no;
   String billing_no;
   String hso_no;
   String status;
   String comments;
   String provider_activity;
   String practitionerNo;
   String init;
   String job_title;
   String email;
   String title;
   String lastUpdateUser;
   String lastUpdateDate;
   String signed_confidentiality;


   /** Creates a new instance of ProviderData */
   public ProviderData() {
   }

   public ProviderData(String providerNo) {
      getProvider(providerNo);
   }

   public List<String> getProviderListWithInsuranceNo(){
       return getProviderListWithInsuranceNo("%");
   }

   public List<String> getProviderListWithInsuranceNo(String insurerNo){
        ArrayList<String> list = new ArrayList<String>();
        try {

                ResultSet rs;
                String sql = "select * from provider where provider_type='doctor' and ohip_no like '"+insurerNo+"' and ohip_no != '' order by last_name";

                rs = DBHandler.GetSQL(sql);
                while  (rs.next()) {
                   list.add(oscar.Misc.getString(rs, "provider_no"));
                }

                rs.close();

            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }
        return list;
   }



   public void getProvider(String providerNo){
          try {

                ResultSet rs;
                String sql = "SELECT * FROM provider WHERE provider_no = '" + providerNo +"'";

                rs = DBHandler.GetSQL(sql);

                if (rs.next()) {
                   this.provider_no = oscar.Misc.getString(rs, "provider_no");
                   this.last_name = oscar.Misc.getString(rs, "last_name");
                   this.first_name = oscar.Misc.getString(rs, "first_name");
                   this.provider_type = oscar.Misc.getString(rs, "provider_type");
                   this.specialty = oscar.Misc.getString(rs, "specialty");
                   this.team = oscar.Misc.getString(rs, "team");
                   this.sex= oscar.Misc.getString(rs, "sex");
                   this.dob= oscar.Misc.getString(rs, "dob");
                   this.address= oscar.Misc.getString(rs, "address");
                   this.phone= oscar.Misc.getString(rs, "phone");
                   this.work_phone= oscar.Misc.getString(rs, "work_phone");
                   this.ohip_no= oscar.Misc.getString(rs, "ohip_no");
                   this.rma_no= oscar.Misc.getString(rs, "rma_no");
                   this.billing_no= oscar.Misc.getString(rs, "billing_no");
                   this.hso_no= oscar.Misc.getString(rs, "hso_no");
                   this.status= oscar.Misc.getString(rs, "status");
                   this.comments= oscar.Misc.getString(rs, "comments");
                   this.provider_activity= oscar.Misc.getString(rs, "provider_activity");
                   this.practitionerNo= oscar.Misc.getString(rs, "practitionerNo");
                   this.init= oscar.Misc.getString(rs, "init");
                   this.job_title= oscar.Misc.getString(rs, "job_title");
                   this.email= oscar.Misc.getString(rs, "email");
                   this.title= oscar.Misc.getString(rs, "title");
                   this.lastUpdateUser= oscar.Misc.getString(rs, "lastUpdateUser");
                   this.lastUpdateDate= oscar.Misc.getString(rs, "lastUpdateDate");
                   this.signed_confidentiality= oscar.Misc.getString(rs, "signed_confidentiality");
                }

                rs.close();

            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }

   }

   /**
    * Getter for property provider_no.
    * @return Value of property provider_no.
    */
   public java.lang.String getProviderNo() {
      return provider_no;
   }

   /**
    * Setter for property provider_no.
    * @param provider_no New value of property provider_no.
    */
   public void setProviderNo(java.lang.String provider_no) {
      this.provider_no = provider_no;
   }

   /**
    * Getter for property last_name.
    * @return Value of property last_name.
    */
   public java.lang.String getLast_name() {
      return last_name;
   }

   /**
    * Setter for property last_name.
    * @param last_name New value of property last_name.
    */
   public void setLast_name(java.lang.String last_name) {
      this.last_name = last_name;
   }

   /**
    * Getter for property first_name.
    * @return Value of property first_name.
    */
   public java.lang.String getFirst_name() {
      return first_name;
   }

   /**
    * Setter for property first_name.
    * @param first_name New value of property first_name.
    */
   public void setFirst_name(java.lang.String first_name) {
      this.first_name = first_name;
   }

   /**
    * Getter for property provider_type.
    * @return Value of property provider_type.
    */
   public java.lang.String getProvider_type() {
      return provider_type;
   }

   /**
    * Setter for property provider_type.
    * @param provider_type New value of property provider_type.
    */
   public void setProvider_type(java.lang.String provider_type) {
      this.provider_type = provider_type;
   }

   /**
    * Getter for property specialty.
    * @return Value of property specialty.
    */
   public java.lang.String getSpecialty() {
      return specialty;
   }

   /**
    * Setter for property specialty.
    * @param specialty New value of property specialty.
    */
   public void setSpecialty(java.lang.String specialty) {
      this.specialty = specialty;
   }

   /**
    * Getter for property team.
    * @return Value of property team.
    */
   public java.lang.String getTeam() {
      return team;
   }

   /**
    * Setter for property team.
    * @param team New value of property team.
    */
   public void setTeam(java.lang.String team) {
      this.team = team;
   }

   /**
    * Getter for property sex.
    * @return Value of property sex.
    */
   public java.lang.String getSex() {
      return sex;
   }

   /**
    * Setter for property sex.
    * @param sex New value of property sex.
    */
   public void setSex(java.lang.String sex) {
      this.sex = sex;
   }

   /**
    * Getter for property dob.
    * @return Value of property dob.
    */
   public java.lang.String getDob() {
      return dob;
   }

   /**
    * Setter for property dob.
    * @param dob New value of property dob.
    */
   public void setDob(java.lang.String dob) {
      this.dob = dob;
   }

   /**
    * Getter for property address.
    * @return Value of property address.
    */
   public java.lang.String getAddress() {
      return address;
   }

   /**
    * Setter for property address.
    * @param address New value of property address.
    */
   public void setAddress(java.lang.String address) {
      this.address = address;
   }

   /**
    * Getter for property phone.
    * @return Value of property phone.
    */
   public java.lang.String getPhone() {
      return phone;
   }

   /**
    * Setter for property phone.
    * @param phone New value of property phone.
    */
   public void setPhone(java.lang.String phone) {
      this.phone = phone;
   }

   /**
    * Getter for property work_phone.
    * @return Value of property work_phone.
    */
   public java.lang.String getWork_phone() {
      return work_phone;
   }

   /**
    * Setter for property work_phone.
    * @param work_phone New value of property work_phone.
    */
   public void setWork_phone(java.lang.String work_phone) {
      this.work_phone = work_phone;
   }

   /**
    * Getter for property ohip_no.
    * @return Value of property ohip_no.
    */
   public java.lang.String getOhip_no() {
      return ohip_no;
   }

   /**
    * Setter for property ohip_no.
    * @param ohip_no New value of property ohip_no.
    */
   public void setOhip_no(java.lang.String ohip_no) {
      this.ohip_no = ohip_no;
   }

   /**
    * Getter for property rma_no.
    * @return Value of property rma_no.
    */
   public java.lang.String getRma_no() {
      return rma_no;
   }

   /**
    * Setter for property rma_no.
    * @param rma_no New value of property rma_no.
    */
   public void setRma_no(java.lang.String rma_no) {
      this.rma_no = rma_no;
   }

   /**
    * Getter for property billing_no.
    * @return Value of property billing_no.
    */
   public java.lang.String getBilling_no() {
      return billing_no;
   }

   /**
    * Setter for property billing_no.
    * @param billing_no New value of property billing_no.
    */
   public void setBilling_no(java.lang.String billing_no) {
      this.billing_no = billing_no;
   }

   /**
    * Getter for property hso_no.
    * @return Value of property hso_no.
    */
   public java.lang.String getHso_no() {
      return hso_no;
   }

   /**
    * Setter for property hso_no.
    * @param hso_no New value of property hso_no.
    */
   public void setHso_no(java.lang.String hso_no) {
      this.hso_no = hso_no;
   }

   /**
    * Getter for property status.
    * @return Value of property status.
    */
   public java.lang.String getStatus() {
      return status;
   }

   /**
    * Setter for property status.
    * @param status New value of property status.
    */
   public void setStatus(java.lang.String status) {
      this.status = status;
   }

   /**
    * Getter for property comments.
    * @return Value of property comments.
    */
   public java.lang.String getComments() {
      return comments;
   }

   /**
    * Setter for property comments.
    * @param comments New value of property comments.
    */
   public void setComments(java.lang.String comments) {
      this.comments = comments;
   }

   /**
    * Getter for property provider_activity.
    * @return Value of property provider_activity.
    */
   public java.lang.String getProvider_activity() {
      return provider_activity;
   }

   /**
    * Setter for property provider_activity.
    * @param provider_activity New value of property provider_activity.
    */
   public void setProvider_activity(java.lang.String provider_activity) {
      this.provider_activity = provider_activity;
   }

   /**
   * getters && setters
   **/

   public java.lang.String getPractitionerNo() {
        return practitionerNo;
   }

   public void setPractitionerNo(java.lang.String practitionerNo) {
	this.practitionerNo = practitionerNo;
   }

   public java.lang.String getInit() {
       return init;
   }

   public void setInit(java.lang.String init) {
       this.init = init;
   }

   public java.lang.String getJob_title() {
       return job_title;
   }

   public void setJob_title(java.lang.String job_title) {
       this.job_title = job_title;
   }

   public java.lang.String getEmail() {
       return email;
   }

   public void setEmail(java.lang.String email) {
       this.email = email;
   }

   public java.lang.String getTitle() {
       return title;
   }

   public void setTitle(java.lang.String title) {
       this.title = title;
   }

   public java.lang.String getLastUpdateUser() {
       return lastUpdateUser;
   }

   public void setLastUpdateUser(java.lang.String lastUpdateUser) {
       this.lastUpdateUser = lastUpdateUser;
   }

   public java.lang.String getLastUpdateDate() {
       return lastUpdateDate;
   }

   public void setLastUpdateDate(java.lang.String lastUpdateDate) {
       this.lastUpdateDate = lastUpdateDate;
   }

   public java.lang.String getSigned_confidentiality() {
       return signed_confidentiality;
   }

   public void setSigned_confidentiality(java.lang.String signed_confidentiality) {
       this.signed_confidentiality = signed_confidentiality;
   }



   //TODO: Add a cache of providers
   public static ArrayList<Hashtable<String,String>> getProviderList (boolean inactive) {
        try {

            ArrayList<Hashtable<String,String>> result = new ArrayList<Hashtable<String,String>>();
            String active = " and status = '1' ";
            if (inactive){
               active = "";
            }

            String sql = "select provider_no, first_name, last_name, ohip_no from provider where provider_type='doctor' "+active+" order by last_name , first_name";
            ResultSet rs = DBHandler.GetSQL(sql);
            while ( rs.next() ) {
                Hashtable<String,String> provider = new Hashtable<String,String>();
                provider.put("providerNo",oscar.Misc.getString(rs, "provider_no"));
                provider.put("firstName",oscar.Misc.getString(rs, "first_name"));
                provider.put("lastName",oscar.Misc.getString(rs, "last_name"));
		provider.put("ohipNo",oscar.Misc.getString(rs, "ohip_no"));
                result.add(provider);
            }
            return result;
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }
    }
   public static List<Hashtable<String,String>> searchProvider(String searchStr){
	   return searchProvider(searchStr, false);
   }


   public static List<Hashtable<String,String>> searchProvider(String searchStr,boolean onlyActive){
       String sql="select provider_no, first_name, last_name from provider where ";
       List<Hashtable<String,String>> retList=new ArrayList<Hashtable<String,String>>();
       String firstname=null;
       String lastname=null;
       if(searchStr.indexOf(",")!=-1){
            String[] array=new String[2];
            array=searchStr.split(",");
            lastname=array[0].trim();
            firstname=array[1].trim();
       }else{
           lastname=searchStr.trim();
       }
       if(lastname!=null && firstname==null)
           sql+="last_name like '"+lastname+"%' ";
       if(lastname!=null && firstname!=null)
           sql+="last_name like '"+lastname+ "%' AND first_name like '"+firstname+"%' ";

       if(onlyActive){
    	   if(lastname != null) {
    		   sql+=" and status = 1";
    	   }else{
    		   sql +=" status = 1";
    	   }
       }
       try{
           ResultSet rs=DBHandler.GetSQL(sql);
           while(rs.next()){
               Hashtable<String,String> provider=new Hashtable<String,String>();
               provider.put("providerNo", oscar.Misc.getString(rs, "provider_no"));
               provider.put("firstName", oscar.Misc.getString(rs, "first_name"));
               provider.put("lastName", oscar.Misc.getString(rs, "last_name"));
               retList.add(provider);
           }
           return retList;
       }catch(SQLException se){
           MiscUtils.getLogger().error("Error", se);
           return null;
       }


   }
   public static ArrayList<Hashtable<String,String>> getProviderListOfAllTypes (boolean inactive) {
        try {

            ArrayList<Hashtable<String,String>> result = new ArrayList<Hashtable<String,String>>();
            String active = "where status = '1'";
            if (inactive){
               active = "";
            }

            String sql = "select provider_no, first_name, last_name, ohip_no from provider "+active+" order by last_name , first_name";
            ResultSet rs = DBHandler.GetSQL(sql);
            while ( rs.next() ) {
            	Hashtable<String,String> provider = new Hashtable<String,String>();
                provider.put("providerNo",oscar.Misc.getString(rs, "provider_no"));
                provider.put("firstName",oscar.Misc.getString(rs, "first_name"));
                provider.put("lastName",oscar.Misc.getString(rs, "last_name"));
		provider.put("ohipNo",oscar.Misc.getString(rs, "ohip_no"));
                result.add(provider);
            }
            return result;
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }
    }

   public static ArrayList<Hashtable<String,String>> getProviderListOfAllTypes () {
      return getProviderListOfAllTypes (false);
   }

   public static ArrayList<Hashtable<String,String>> getProviderList () {
      return getProviderList (false);
   }


    public static String getProviderName(String providerNo) {
           try {



            String sql = "select first_name, last_name from provider where provider_no='"+providerNo+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if ( rs.next() ) {
                return ( oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "last_name") );
            } else {
                return "";
            }
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }
    }

    public String getMyOscarId() {
         if (myOscarId == null) this.initMyOscarId();
         return myOscarId;
    }

    public void initMyOscarId() {
         this.myOscarId = ProviderMyOscarIdData.getMyOscarId(this.getProviderNo());
    }
    private String myOscarId = null;

    public String getDefaultBillingView(String providerNo){

    	ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao)SpringUtils.getBean("providerPreferenceDao");
    	ProviderPreference providerPreference=providerPreferenceDao.find(providerNo);

    	if (providerPreference!=null) return(providerPreference.getDefaultServiceType());
    	else return(null);
    }



   public void getProviderWithOHIP(String ohipNo) {
       if (filled(ohipNo)) {
           String sql = "select provider_no from provider where ohip_no = '" + ohipNo + "'";
           getProvider(selectProviderNo(sql));
       }
   }

   public void getProviderWithNames(String firstName, String lastName) {
       if (filled(firstName) && filled(lastName)) {
           String sql = "select provider_no from provider where first_name='"+firstName+"' and last_name='"+lastName+"'";
           String noOHIP = " (ohip_no='' or ohip_no is null)";
           String extPvd = " provider_no < -1000";
           String not = " not";
           String and = " and";

           String providerNo = selectProviderNo(sql+and+noOHIP+and+extPvd);
           if (!filled(providerNo)) providerNo = selectProviderNo(sql+and+not+noOHIP+and+extPvd);
           if (!filled(providerNo)) providerNo = selectProviderNo(sql+and+noOHIP+and+not+extPvd);
           if (!filled(providerNo)) providerNo = selectProviderNo(sql+and+not+noOHIP+and+not+extPvd);
           getProvider(providerNo);
       }
   }

   public void getExternalProviderWithNames(String firstName, String lastName) {
       if (filled(firstName) && filled(lastName)) {
           String sql = "select provider_no from provider where first_name='"+firstName+"' and last_name='"+lastName+"'";
           String noOHIP = " (ohip_no='' or ohip_no is null)";
           String extPvd = " provider_no < -1000";
           String not = " not";
           String and = " and";

           String providerNo = selectProviderNo(sql+and+noOHIP+and+extPvd);
           if (!filled(providerNo)) providerNo = selectProviderNo(sql+and+not+noOHIP+and+extPvd);
           getProvider(providerNo);
       }
   }

    public void addExternalProvider(String firstName, String lastName, String ohipNo, String cpsoNo) {
        if (!filled(firstName) && !filled(lastName) && !filled(ohipNo)) return; //no information at all!

        //get latest external provider no
	String sql = "select min(cast(provider_no as signed)) from provider";
        String providerNo = selectProviderNo(sql);

        //assign new external provider no
        if (filled(providerNo)) {
            int lastPN = Integer.valueOf(providerNo);
            lastPN = lastPN<-1000 ? lastPN-1 : -1001;
            this.provider_no = String.valueOf(lastPN);
        } else {
            this.provider_no = "-1001";
        }

        //create new external provider
        this.first_name = filled(firstName) ? firstName : "";
        this.last_name = filled(lastName) ? lastName : "";
        this.ohip_no = filled(ohipNo) ? ohipNo : "";
        this.practitionerNo = filled(cpsoNo) ? cpsoNo : "";
        this.provider_type = "doctor";
        this.status = "1";
        this.specialty = "";
        this.sex = "";
        addUpdateProvider(false);
    }

   private String selectProviderNo(String sql) {
        String providerNo = "";
        if (!filled(sql) && !sql.trim().toLowerCase().startsWith("select")) {
            return null;
        }
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                providerNo = oscar.Misc.getString(rs, 1);
            }
            rs.close();
        } catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
        }
        return providerNo;
   }

    public Integer addUpdateProvider(boolean update) {
            Integer key = 0;
        try {
            String sql = "insert into provider values (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)";
            if (update) {
                sql = "update provider set last_name=?      and first_name=?        and provider_type=? " +
                                      "and specialty=?      and team=?              and sex=? " +
                                      "and dob=?            and address=?           and phone=? " +
                                      "and work_phone=?     and ohip_no=?           and rma_no=? " +
                                      "and billing_no=?     and hso_no=?            and status=? " +
                                      "and comments=?       and provider_activity=? and practitionerNo=? " +
                                      "and init=?           and job_title=?         and email=? " +
                                      "and title=?          and lastUpdateUser=?    and lastUpdateDate=? " +
                                      "and signed_confidentiality=?     where provider_no=?";
            }

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement write_rec = conn.prepareStatement(sql);

            Integer i = update ? 1 : 2;
            write_rec.setString(i, last_name);
            write_rec.setString(i + 1, first_name);
            write_rec.setString(i + 2, provider_type);
            write_rec.setString(i + 3, specialty);
            write_rec.setString(i + 4, team);
            write_rec.setString(i + 5, sex);
            write_rec.setString(i + 6, dob);
            write_rec.setString(i + 7, address);
            write_rec.setString(i + 8, phone);
            write_rec.setString(i + 9, work_phone);
            write_rec.setString(i + 10, ohip_no);
            write_rec.setString(i + 11, rma_no);
            write_rec.setString(i + 12, billing_no);
            write_rec.setString(i + 13, hso_no);
            write_rec.setString(i + 14, status);
            write_rec.setString(i + 15, comments);
            write_rec.setString(i + 16, provider_activity);
            write_rec.setString(i + 17, practitionerNo);
            write_rec.setString(i + 18, init);
            write_rec.setString(i + 19, job_title);
            write_rec.setString(i + 20, email);
            write_rec.setString(i + 21, title);
            write_rec.setString(i + 22, lastUpdateUser);
            write_rec.setString(i + 23, "now()");
            write_rec.setString(i + 24, signed_confidentiality);
            if (update) {
                write_rec.setString(i + 25, provider_no);
            } else {
                write_rec.setString(1, provider_no);
            }
            write_rec.executeUpdate();
            ResultSet rs = write_rec.getGeneratedKeys();
            if (rs.next()) {
                key = rs.getInt(1);
            }
            write_rec.close();
            rs.close();
        } catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
        }
        return key;
    }

    boolean filled(String s) {
        return (s!=null && !s.trim().equals(""));
    }
}
