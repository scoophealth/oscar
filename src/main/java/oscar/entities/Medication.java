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

import java.util.Collection;

// Class Medication
//
public class Medication
    extends ClinicalFactor {
  // Fields
  //
  private String dosage;

  //
  private String frequency;

  //
  private Collection interactionList;

  // Methods
  // Constructors
  // Accessor Methods
  /**
   * Get the value of dosage
   *
   * @return the value of dosage
   */
  private String getDosage() {
    return dosage;
  }

  /**
   * Set the value of dosage
   *
   *
   */
  private void setDosage(String value) {
    dosage = value;
  }

  /**
   * Get the value of frequency
   *
   * @return the value of frequency
   */
  private String getFrequency() {
    return frequency;
  }

  /**
   * Set the value of frequency
   *
   *
   */
  private void setFrequency(String value) {
    frequency = value;
  }

  /**
   * Get the value of interactionList
   *
   * @return the value of interactionList
   */
  private Collection getInteractionList() {
    return interactionList;
  }

  /**
   * Set the value of interactionList
   *
   *
   */
  private void setInteractionList(Collection value) {
    interactionList = value;
  }
}

  // Operations
