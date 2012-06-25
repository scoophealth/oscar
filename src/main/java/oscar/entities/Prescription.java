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

/**
 *
 * SELECT *
 FROM `prescription`
 where demographic_no = 1;

 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Prescription {
  private String scriptNo;
  private String providerNo;
  private String demographicNo;
  private String datePrescribed;
  private String datePrinted;
  private String datesReprinted;
  private String textView;
  public Prescription() {
  }

  public String getScriptNo() {
    return scriptNo;
  }

  public void setScriptNo(String scriptNo) {
    this.scriptNo = scriptNo;
  }

  public String getProviderNo() {
    return providerNo;
  }

  public void setProviderNo(String providerNo) {
    this.providerNo = providerNo;
  }

  public String getDemographicNo() {
    return demographicNo;
  }

  public void setDemographicNo(String demographicNo) {
    this.demographicNo = demographicNo;
  }

  public String getDatePrescribed() {
    return datePrescribed;
  }

  public void setDatePrescribed(String datePrescribed) {
    this.datePrescribed = datePrescribed;
  }

  public String getDatePrinted() {
    return datePrinted;
  }

  public void setDatePrinted(String datePrinted) {
    this.datePrinted = datePrinted;
  }

  public String getDatesReprinted() {
    return datesReprinted;
  }

  public void setDatesReprinted(String datesReprinted) {
    this.datesReprinted = datesReprinted;
  }

  public String getTextView() {
    return textView;
  }

  public void setTextView(String textView) {
    this.textView = textView;
  }
}
