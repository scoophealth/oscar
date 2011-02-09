/*
 *
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
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.entities;

//

/*
 select start_date,update_date,description from dxresearch, ichppccode,demographic
 where dxresearch.demographic_no = demographic.demographic_no
 and dxresearch.dxresearch_code = ichppccode.ichppccode
 and demographic.demographic_no = 1;
 */
public class Disease
    extends Condition {
  private String id;
  private String startDate;
  private String endDate;
  private String updateDate;
  private String status;
  private String codingSystem;
  private String diagnosticCode;
  private String diseaseCode;
  private String type;
  private String description;
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCodingSystem() {
    return codingSystem;
  }

  public void setCodingSystem(String codingSystem) {
    this.codingSystem = codingSystem;
  }

  public String getDiagnosticCode() {
    return diagnosticCode;
  }

  public void setDiagnosticCode(String diagnosticCode) {
    this.diagnosticCode = diagnosticCode;
  }

  public String getDiseaseCode() {
    return diseaseCode;
  }

  public void setDiseaseCode(String diseaseCode) {
    this.diseaseCode = diseaseCode;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  // Fields
  // Methods
  // Constructors
  // Accessor Methods
  // Operations
}
