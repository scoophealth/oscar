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

public class ClinicalFactor {
  // Fields
  //
  private int factorTypeId;

  //
  private String factorName;

  //
  private String factorDesc;

  //
  private String startDate;

  //
  private String endDate;

  //
  private String clinicalNote;

  // Methods
  // Constructors
  // Accessor Methods
  /**
   * Get the value of factorTypeId
   *
   * @return the value of factorTypeId
   */
  private int getFactorTypeId() {
    return factorTypeId;
  }

  /**
   * Set the value of factorTypeId
   *
   *
   */
  private void setFactorTypeId(int value) {
    factorTypeId = value;
  }

  /**
   * Get the value of factorName
   *
   * @return the value of factorName
   */
  private String getFactorName() {
    return factorName;
  }

  /**
   * Set the value of factorName
   *
   *
   */
  private void setFactorName(String value) {
    factorName = value;
  }

  /**
   * Get the value of factorDesc
   *
   * @return the value of factorDesc
   */
  private String getFactorDesc() {
    return factorDesc;
  }

  /**
   * Set the value of factorDesc
   *
   *
   */
  private void setFactorDesc(String value) {
    factorDesc = value;
  }

  /**
   * Get the value of startDate
   *
   * @return the value of startDate
   */
  private String getStartDate() {
    return startDate;
  }

  /**
   * Set the value of startDate
   *
   *
   */
  private void setStartDate(String value) {
    startDate = value;
  }

  /**
   * Get the value of endDate
   *
   * @return the value of endDate
   */
  private String getEndDate() {
    return endDate;
  }

  /**
   * Set the value of endDate
   *
   *
   */
  private void setEndDate(String value) {
    endDate = value;
  }

  /**
   * Get the value of clinicalNote
   *
   * @return the value of clinicalNote
   */
  private String getClinicalNote() {
    return clinicalNote;
  }

  /**
   * Set the value of clinicalNote
   *
   *
   */
  private void setClinicalNote(String value) {
    clinicalNote = value;
  }
}

  // Operations
