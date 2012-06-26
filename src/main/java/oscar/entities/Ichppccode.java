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
 * Encapsulates data from table ichppccode
 *
 */
public class Ichppccode {
  private String _ichppccode;
  private String diagnosticCode;
  private String description;

  /**
   * Class constructor with no arguments.
   */
  public Ichppccode() {}

  /**
   * Full constructor
   *
   * @param _ichppccode String
   * @param diagnosticCode String
   * @param description String
   */
  public Ichppccode(String _ichppccode, String diagnosticCode,
                    String description) {
    this._ichppccode = _ichppccode;
    this.diagnosticCode = diagnosticCode;
    this.description = description;
  }

  /**
   * Gets the _ichppccode
   * @return String _ichppccode
   */
  public String get_ichppccode() {
    return (_ichppccode != null ? _ichppccode : "");
  }

  /**
   * Gets the diagnosticCode
   * @return String diagnosticCode
   */
  public String getDiagnosticCode() {
    return (diagnosticCode != null ? diagnosticCode : "");
  }

  /**
   * Gets the description
   * @return String description
   */
  public String getDescription() {
    return (description != null ? description : "");
  }

  /**
   * Sets the _ichppccode
   * @param _ichppccode String
   */
  public void set_ichppccode(String _ichppccode) {
    this._ichppccode = _ichppccode;
  }

  /**
   * Sets the diagnosticCode
   * @param diagnosticCode String
   */
  public void setDiagnosticCode(String diagnosticCode) {
    this.diagnosticCode = diagnosticCode;
  }

  /**
   * Sets the description
   * @param description String
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
