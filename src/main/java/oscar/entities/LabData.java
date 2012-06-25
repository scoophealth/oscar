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

public class LabData {
  private String a1c;
  private String ldl;
  private String ratio;
  private String triglycerides;
  private String hypoglycemia;
  private String glucLab;
  private String microalbumin;
  private String kidneyfunction;
  public LabData() {

  }

  public String getA1c() {
    return a1c;
  }

  public void setA1c(String a1c) {
    this.a1c = a1c;
  }

  public String getLdl() {
    return ldl;
  }

  public void setLdl(String ldl) {
    this.ldl = ldl;
  }

  public String getRatio() {
    return ratio;
  }

  public void setRatio(String ratio) {
    this.ratio = ratio;
  }

  public String getTriglycerides() {
    return triglycerides;
  }

  public void setTriglycerides(String triglycerides) {
    this.triglycerides = triglycerides;
  }

  public String getHypoglycemia() {
    return hypoglycemia;
  }

  public void setHypoglycemia(String hypoglycemia) {
    this.hypoglycemia = hypoglycemia;
  }

  public String getGlucLab() {
    return glucLab;
  }

  public void setGlucLab(String glucLab) {
    this.glucLab = glucLab;
  }

  public String getMicroalbumin() {
    return microalbumin;
  }

  public void setMicroalbumin(String microalbumin) {
    this.microalbumin = microalbumin;
  }

  public String getKidneyfunction() {
    return kidneyfunction;
  }

  public void setKidneyfunction(String kidneyfunction) {
    this.kidneyfunction = kidneyfunction;
  }}
