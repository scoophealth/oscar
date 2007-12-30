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
 * Ontario, Canada   Creates a new instance of BillingCode
 *
 *
 * BillingCodeData.java
 *
 * Created on August 4, 2004, 10:37 AM
 */

package oscar.oscarBilling.ca.bc.data;

import java.sql.*;
import java.util.*;
import oscar.*;
import oscar.oscarDB.*;
import oscar.util.SqlUtils;

/**
 *
 * @author  root
 */
public final class BillingCodeData implements Comparable      {

  /*
   +-----------------------+-------------+------+-----+---------+----------------+
   | Field                 | Type        | Null | Key | Default | Extra          |
   +-----------------------+-------------+------+-----+---------+----------------+
   | billingservice_no     | int(10)     |      | PRI | NULL    | auto_increment |
   | service_compositecode | varchar(30) | YES  |     | NULL    |                |
   | service_code          | varchar(10) | YES  | MUL | NULL    |                |
   | description           | text        | YES  |     | NULL    |                |
   | value                 | varchar(8)  | YES  |     | NULL    |                |
   | percentage            | varchar(8)  | YES  |     | NULL    |                |
   | billingservice_date   | date        | YES  |     | NULL    |                |
   | specialty             | varchar(15) | YES  |     | NULL    |                |
   | region                | varchar(5)  | YES  |     | NULL    |                |
   | anaesthesia           | char(2)     | YES  |     | NULL    |                |
   +-----------------------+-------------+------+-----+---------+----------------+
   */

  String billingserviceNo;
  String serviceCompositecode; //| service_compositecode | varchar(30) | YES  |     | NULL    |                |
  String serviceCode; //| service_code          | varchar(10) | YES  | MUL | NULL    |                |
  String description; //| description           | text        | YES  |     | NULL    |                |
  String value; //| value                 | varchar(8)  | YES  |     | NULL    |                |
  String percentage; //| percentage            | varchar(8)  | YES  |     | NULL    |                |
  String billingserviceDate; //| billingservice_date   | date        | YES  |     | NULL    |                |
  String specialty; //| specialty             | varchar(15) | YES  |     | NULL    |                |
  String region; //| region                | varchar(5)  | YES  |     | NULL    |                |
  String anaesthesia; // | anaesthesia

  /** Creates a new instance of BillingCodeData */
  public BillingCodeData() {
  }

  //public BillingCodeData(ResultSet rs) throws {
  //    fillCodeData(rs);
  //}

  public BillingCodeData fillCodeData(ResultSet rs) throws SQLException {
    billingserviceNo = rs.getString("billingservice_no");
    serviceCompositecode = rs.getString("service_compositecode");
    serviceCode = rs.getString("service_code");
    description = rs.getString("description");
    value = rs.getString("value");
    percentage = rs.getString("percentage");
    billingserviceDate = rs.getString("billingservice_date");
    specialty = rs.getString("specialty");
    region = rs.getString("region");
    anaesthesia = rs.getString("anaesthesia");
    return this;
  }

  public boolean editBillingCode(String servicecode,String desc, String val, String codeId) {
    boolean retval = true;
    DBHandler db = null;
    String str = null;

    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      str = "update billingservice set " +
          "service_code = '" + Misc.mysqlEscape(servicecode) + "', " +
          "description = '" + Misc.mysqlEscape(desc) + "', " +
          "value       = '" + Misc.mysqlEscape(val) + "' " +
          "where billingservice_no = '" + Misc.mysqlEscape(codeId) + "'";
      db.RunSQL(str);
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return retval;
  }

  /**
   * Removes a private billing code from database
   * @param codeId String - The service code to be removed
   * @return boolean
   */
  public boolean deleteBillingCode(String codeId) {
     boolean retval = true;
     DBHandler db = null;
     String str = null;
     try {
       db = new DBHandler(DBHandler.OSCAR_DATA);
       str = "delete from billingservice where billingservice_no = '" + Misc.mysqlEscape(codeId) + "'";
       db.RunSQL(str);
     }
     catch (Exception e1) {
       e1.printStackTrace();
     }
     finally {
       try {
         db.CloseConn();
       }
       catch (SQLException ex) {
         ex.printStackTrace();
       }
     }
     return retval;
   }

  public boolean addBillingCode(String code, String desc, String val) {
    boolean retval = true;
    DBHandler db = null;
    String str = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      str = "insert into billingservice " +
          " (service_compositecode,service_code,description,value,percentage,billingservice_date,specialty,region,anaesthesia) " +
          "values " +
          "(''," + //service_composite
          "'" + Misc.mysqlEscape(code) + "'," + //service_code
          "'" + Misc.mysqlEscape(desc) + "'," + //description
          "'" + Misc.mysqlEscape(val) + "'," + //value
          "''," + //percentage
          "now()," + //billingservice_date
          "''," + //specialty
          "'BC'," + //region
          "'00')"; //anaesthesia
      System.out.println(str);
      db.RunSQL(str);
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return retval;
  }

  public boolean updateBillingCodePrice(String code, String val) {
    boolean retval = true;
    DBHandler db = null;
    String str = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      str = "update billingservice set value = '"+val+"' where service_code = '"+code+"'";
      System.out.println(str);
      db.RunSQL(str);
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return retval;
  }
  
  
  
  private ArrayList codeSearch(String queryString) {
    ArrayList list = new ArrayList();
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(queryString);
      while (rs.next()) {
        list.add(new BillingCodeData().fillCodeData(rs));
      }
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    finally {
      try {
        rs.close();
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return list;
  }

  
  public BillingCodeData getBillingCodeByCode(String code){
    List list = codeSearch("select * from billingservice where service_code like '" +code + "'" );
    if(list == null || list.size() ==0 ){
        return null;
    }
    return (BillingCodeData) list.get(0);
  }
  
  /**
   * Finds private service codes by code id
   * @param code String - the service code
   * @param order int - the sort order: 1 = descending otherwise the order is ascending
   * @return ArrayList - list of codes
   */
  public ArrayList findBillingCodesByCode(String code,int order) {
    String orderByClause = order == 1?"order by service_code desc":"order by service_code";
    return codeSearch("select * from billingservice where service_code like '" +
                      Misc.mysqlEscape(code) + "%' " + orderByClause);
  }

  public ArrayList findBillingCodesByDesc(String desc) {
    return codeSearch("select * from billingservice where description like '" +
                      Misc.mysqlEscape(desc) + "%' ");
  }

  public List getBillingCodesLookup(String searchTerm){
    return  SqlUtils.getQueryResultsList("select service_code,description from billingservice where description like '" + Misc.mysqlEscape(searchTerm) + "%'");
  }

  
  
  
  
  /**
   * Getter for property billingserviceNo.
   * @return Value of property billingserviceNo.
   */
  public java.lang.String getBillingserviceNo() {
    return billingserviceNo;
  }

  /**
   * Setter for property billingserviceNo.
   * @param billingserviceNo New value of property billingserviceNo.
   */
  public void setBillingserviceNo(java.lang.String billingserviceNo) {
    this.billingserviceNo = billingserviceNo;
  }

  /**
   * Getter for property serviceCompositecode.
   * @return Value of property serviceCompositecode.
   */
  public java.lang.String getServiceCompositecode() {
    return serviceCompositecode;
  }

  /**
   * Setter for property serviceCompositecode.
   * @param serviceCompositecode New value of property serviceCompositecode.
   */
  public void setServiceCompositecode(java.lang.String serviceCompositecode) {
    this.serviceCompositecode = serviceCompositecode;
  }

  /**
   * Getter for property serviceCode.
   * @return Value of property serviceCode.
   */
  public java.lang.String getServiceCode() {
    return serviceCode;
  }

  /**
   * Setter for property serviceCode.
   * @param serviceCode New value of property serviceCode.
   */
  public void setServiceCode(java.lang.String serviceCode) {
    this.serviceCode = serviceCode;
  }

  /**
   * Getter for property description.
   * @return Value of property description.
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * Setter for property description.
   * @param description New value of property description.
   */
  public void setDescription(java.lang.String description) {
    this.description = description;
  }

  /**
   * Getter for property value.
   * @return Value of property value.
   */
  public java.lang.String getValue() {
    return value;
  }

  /**
   * Setter for property value.
   * @param value New value of property value.
   */
  public void setValue(java.lang.String value) {
    this.value = value;
  }

  /**
   * Getter for property percentage.
   * @return Value of property percentage.
   */
  public java.lang.String getPercentage() {
    return percentage;
  }

  /**
   * Setter for property percentage.
   * @param percentage New value of property percentage.
   */
  public void setPercentage(java.lang.String percentage) {
    this.percentage = percentage;
  }

  /**
   * Getter for property billingserviceDate.
   * @return Value of property billingserviceDate.
   */
  public java.lang.String getBillingserviceDate() {
    return billingserviceDate;
  }

  /**
   * Setter for property billingserviceDate.
   * @param billingserviceDate New value of property billingserviceDate.
   */
  public void setBillingserviceDate(java.lang.String billingserviceDate) {
    this.billingserviceDate = billingserviceDate;
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
   * Getter for property region.
   * @return Value of property region.
   */
  public java.lang.String getRegion() {
    return region;
  }

  /**
   * Setter for property region.
   * @param region New value of property region.
   */
  public void setRegion(java.lang.String region) {
    this.region = region;
  }

  /**
   * Getter for property anaesthesia.
   * @return Value of property anaesthesia.
   */
  public java.lang.String getAnaesthesia() {
    return anaesthesia;
  }

  /**
   * Setter for property anaesthesia.
   * @param anaesthesia New value of property anaesthesia.
   */
  public void setAnaesthesia(java.lang.String anaesthesia) {
    this.anaesthesia = anaesthesia;
  }

  public int compareTo(Object o) {
    return 0;
  }

}
