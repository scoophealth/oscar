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

package oscar.oscarBilling.ca.bc.MSP;


public class AgeValidator
    extends ServiceCodeValidator {
  private int maxAge = 150;
  private int minAge = 0;
  private int inputAge;
  public AgeValidator() {
  }
  public AgeValidator(String svcCode,int inputAge) {
    this.serviceCode = svcCode;
    this.inputAge = inputAge;
  }
  public String getDescription(){
    return minAge + " - " + maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }

  public void setInputAge(int inputAge) {
    this.inputAge = inputAge;
  }

  public int getMaxAge() {
    return maxAge;
  }

  public int getMinAge() {
    return minAge;
  }

  public int getInputAge() {
    return inputAge;
  }

  public boolean isValid(){
    return (this.inputAge >= minAge && this.inputAge<= maxAge);
  }
}
