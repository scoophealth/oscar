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

package oscar.entities;

import java.util.Date;

/**
 * Encapsulates data from table billactivity
 *
 */
public class Billactivity {
  private String monthCode;
  private int batchcount;
  private String htmlfilename;
  private String ohipfilename;
  private String providerohipno;
  private String groupno;
  private String creator;
  private String htmlcontext;
  private String ohipcontext;
  private String claimrecord;
  private Date updatedatetime;
  private String status;
  private String total;
  private int id;
  
  public static final String SENT = "S";
  public static final String NOTSENT = "A";
  

  /**
   * Class constructor with no arguments.
   */
  public Billactivity() {}

  /**
   * Full constructor
   *
   * @param monthCode String
   * @param batchcount int
   * @param htmlfilename String
   * @param ohipfilename String
   * @param providerohipno String
   * @param groupno String
   * @param creator String
   * @param htmlcontext String
   * @param ohipcontext String
   * @param claimrecord String
   * @param updatedatetime String
   * @param status String
   * @param total String
   */
  public Billactivity(String monthCode, int batchcount, String htmlfilename,
                      String ohipfilename, String providerohipno,
                      String groupno, String creator, String htmlcontext,
                      String ohipcontext, String claimrecord,
                      Date updatedatetime, String status, String total) {
    this.setMonthCode(monthCode);
    this.setBatchcount(batchcount);
    this.setHtmlfilename(htmlfilename);
    this.setOhipfilename(ohipfilename);
    this.setProviderohipno(providerohipno);
    this.setGroupno(groupno);
    this.setCreator(creator);
    this.setHtmlcontext(htmlcontext);
    this.setOhipcontext(ohipcontext);
    this.setClaimrecord(claimrecord);
    this.setUpdatedatetime(updatedatetime);
    this.setStatus(status);
    this.setTotal(total);
  }

  /**
   * Gets the monthCode
   * @return String monthCode
   */
  public String getMonthCode() {
    return (monthCode != null ? monthCode : "");
  }

  /**
   * Gets the batchcount
   * @return int batchcount
   */
  public int getBatchcount() {
    return batchcount;
  }

  /**
   * Gets the htmlfilename
   * @return String htmlfilename
   */
  public String getHtmlfilename() {
    return (htmlfilename != null ? htmlfilename : "");
  }

  /**
   * Gets the ohipfilename
   * @return String ohipfilename
   */
  public String getOhipfilename() {
    return (ohipfilename != null ? ohipfilename : "");
  }

  /**
   * Gets the providerohipno
   * @return String providerohipno
   */
  public String getProviderohipno() {
    return (providerohipno != null ? providerohipno : "");
  }

  /**
   * Gets the groupno
   * @return String groupno
   */
  public String getGroupno() {
    return (groupno != null ? groupno : "");
  }

  /**
   * Gets the creator
   * @return String creator
   */
  public String getCreator() {
    return (creator != null ? creator : "");
  }

  /**
   * Gets the htmlcontext
   * @return String htmlcontext
   */
  public String getHtmlcontext() {
    return (htmlcontext != null ? htmlcontext : "");
  }

  /**
   * Gets the ohipcontext
   * @return String ohipcontext
   */
  public String getOhipcontext() {
    return (ohipcontext != null ? ohipcontext : "");
  }

  /**
   * Gets the claimrecord
   * @return String claimrecord
   */
  public String getClaimrecord() {
    return (claimrecord != null ? claimrecord : "");
  }

  /**
   * Gets the updatedatetime
   * @return String updatedatetime
   */
  public Date getUpdatedatetime() {
    return updatedatetime;
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the total
   * @return String total
   */
  public String getTotal() {
    return (total != null ? total : "");
  }

  /**
   * Sets the monthCode
   * @param monthCode String
   */
  public void setMonthCode(String monthCode) {
    this.monthCode = monthCode;
  }

  /**
   * Sets the batchcount
   * @param batchcount int
   */
  public void setBatchcount(int batchcount) {
    this.batchcount = batchcount;
  }

  /**
   * Sets the htmlfilename
   * @param htmlfilename String
   */
  public void setHtmlfilename(String htmlfilename) {
    this.htmlfilename = htmlfilename;
  }

  /**
   * Sets the ohipfilename
   * @param ohipfilename String
   */
  public void setOhipfilename(String ohipfilename) {
    this.ohipfilename = ohipfilename;
  }

  /**
   * Sets the providerohipno
   * @param providerohipno String
   */
  public void setProviderohipno(String providerohipno) {
    this.providerohipno = providerohipno;
  }

  /**
   * Sets the groupno
   * @param groupno String
   */
  public void setGroupno(String groupno) {
    this.groupno = groupno;
  }

  /**
   * Sets the creator
   * @param creator String
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Sets the htmlcontext
   * @param htmlcontext String
   */
  public void setHtmlcontext(String htmlcontext) {
    this.htmlcontext = htmlcontext;
  }

  /**
   * Sets the ohipcontext
   * @param ohipcontext String
   */
  public void setOhipcontext(String ohipcontext) {
    this.ohipcontext = ohipcontext;
  }

  /**
   * Sets the claimrecord
   * @param claimrecord String
   */
  public void setClaimrecord(String claimrecord) {
    this.claimrecord = claimrecord;
  }

  /**
   * Sets the updatedatetime
   * @param updatedatetime String
   */
  public void setUpdatedatetime(Date updatedatetime) {
    this.updatedatetime = updatedatetime;
  }

  
  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the total
   * @param total String
   */
  public void setTotal(String total) {
    this.total = total;
  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
