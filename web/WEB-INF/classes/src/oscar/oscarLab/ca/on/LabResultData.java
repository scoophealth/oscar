/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of LabResultData
 *
 * LabResultData.java
 *
 * Created on April 21, 2005, 4:31 PM
 */

package oscar.oscarLab.ca.on;

/**
 *
 * @author Jay Gallagher
 */
public class LabResultData {
   
   public static String CML = "CML";
   public static String MDS = "MDS";
   public String segmentID;
   public String acknowledgedStatus;

   public String healthNumber;
   public String patientName;
   public String sex;
   public String resultStatus;
   public String dateTime;
   public String priority;
   public String requestingClient;
   public String discipline;
   public String reportStatus;   
   public boolean abn = false;
   public String labType; // ie CML or MDS
   
   public LabResultData() {
   }
   
   public LabResultData(String labT) {
      if (CML.equals(labT)){
         labType = CML;
      }else if (MDS.equals(labT)){
         labType = MDS;
      }
   }
   
   public boolean isAbnormal(){ return abn ; }
   
   public boolean isMDS(){
      boolean ret = false;
      if (MDS.equals(labType)){ ret = true; }
      return ret;
   }
   
   public boolean isCML(){
      boolean ret = false;
      if (CML.equals(labType)){ ret = true; }
      return ret;
   }   
}


