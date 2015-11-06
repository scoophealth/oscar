/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package oscar.oscarDB;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * deprecated Use JPA instead, no new code should be written against this class.
 */
public final class DBPreparedHandlerParam {
   private Date dateValue;
   private String stringValue;
   private int intValue;
   private String paramType;
   private Timestamp timestampValue;

   public static final String PARAM_STRING = "String";
   public static final String PARAM_DATE = "Date";
   public static final String PARAM_INT = "Int";
   public static final String PARAM_TIMESTAMP = "Timestamp";
   
   public DBPreparedHandlerParam(String stringValue){
	   this.intValue = 0;
	   this.stringValue= stringValue;
	   this.dateValue=null;
           this.timestampValue = null;
	   this.paramType=PARAM_STRING;
   }
   
   public DBPreparedHandlerParam(Date dateValue){
	   this.intValue = 0;
	   this.stringValue=null;
	   this.dateValue= dateValue;
           this.timestampValue = null;
           this.paramType=PARAM_DATE;
   }
   
    public DBPreparedHandlerParam(Timestamp dateValue){
	   this.intValue = 0;
	   this.stringValue=null;
	   this.dateValue= null;
           this.timestampValue = dateValue;
	   this.paramType=PARAM_TIMESTAMP;
   }
   

   public DBPreparedHandlerParam(int intValue){
	   this.intValue= intValue;
	   this.stringValue = "";
	   this.dateValue=null;
           this.timestampValue = null;
	   this.paramType=PARAM_INT;
   }

   public Date getDateValue() {

	  return dateValue;
   }

   
   public Timestamp getTimestampValue() {

	  return this.timestampValue;
   }

   
   public int getIntValue() {
		  return intValue;
   }

//   public void setDateValue(Date dateValue) {
//	  this.dateValue = dateValue;
//   }

   public String getParamType() {
	  return paramType;
   }

//   public void setParamType(String paramType) {
//	  this.paramType = paramType;
//   }

   public String getStringValue() {
	  return stringValue;
   }

//   public void setStringValue(String stringValue) {
//	  this.stringValue = stringValue;
//   }
   
}
