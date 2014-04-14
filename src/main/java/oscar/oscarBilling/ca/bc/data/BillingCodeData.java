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


package oscar.oscarBilling.ca.bc.data;

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.util.SqlUtils;

/**
 *
 * @author  root
 */
public final class BillingCodeData implements Comparable      {
    private static BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");
  

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
   | gstFlag               | tinyint(1)  | NO   |     | 0       |                |
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

  public List<BillingService> search(String str,Date date) {
    return billingServiceDao.search(str,"BC",date);
  }

  public boolean editBillingCode(String servicecode,String desc, String val, String codeId) {
    boolean retval = true;   
    BillingService  billingService = billingServiceDao.find(Integer.parseInt(codeId));
    billingService.setServiceCode(servicecode);
    billingService.setDescription(desc);
    billingService.setValue(val);
    billingServiceDao.merge(billingService);
    return retval;
  }

  /**
   * Removes a private billing code from database
   * @param codeId String - The service code to be removed
   * @return boolean
   */
  public boolean deleteBillingCode(String codeId) {

     boolean retval = true;
     BillingService  billingService = billingServiceDao.find(Integer.parseInt(codeId));
     billingServiceDao.remove(billingService);
     return retval;
   }

  public boolean addBillingCode(String code, String desc, String val) {
    boolean retval = true;
    BillingService billingService = new BillingService();

    billingService.setServiceCompositecode("");
    billingService.setServiceCode(code);
    billingService.setDescription(desc);
    billingService.setValue(val);
    billingService.setPercentage("");
    billingService.setBillingserviceDate(new Date());
    billingService.setRegion("BC");
    billingService.setAnaesthesia("00");
    billingService.setGstFlag(false);
    billingService.setSliFlag(false);
    billingServiceDao.persist(billingService);
    return retval;
  }

  public boolean updateBillingCodePrice(String code, String val) {
    boolean retval = true;
    BillingService billingservice = billingServiceDao.searchBillingCode(code,"BC");
    billingservice.setBillingserviceNo(0);
    billingservice.setBillingserviceDate(new Date());
    billingservice.setValue(val);
    billingServiceDao.merge(billingservice);
    return retval;
  }



  public BillingService getBillingCodeByCode(String code,Date date){
    List list = billingServiceDao.findBillingCodesByCode( code,BillingServiceDao.BC,date,1);

    //List list = codeSearch("select * from billingservice where service_code like '" +code + "'" );
    if(list == null || list.size() ==0 ){
        return null;
    }
    return (BillingService) list.get(0);

  }

  
  
  public BillingService getBillingCodeByCode(String code){
    List list = billingServiceDao.findBillingCodesByCode( code,"BC");
    //List list = codeSearch("select * from billingservice where service_code like '" +code + "'" );
    if(list == null || list.size() ==0 ){
        return null;
    }
    return (BillingService) list.get(0);
  }
  
  /**
   * Finds private service codes by code id
   * @param code String - the service code
   * @param order int - the sort order: 1 = descending otherwise the order is ascending
   * @return ArrayList - list of codes
   */
  public List findBillingCodesByCode(String code,int order) {
    return billingServiceDao.findBillingCodesByCode(code,BillingServiceDao.BC,order);
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
