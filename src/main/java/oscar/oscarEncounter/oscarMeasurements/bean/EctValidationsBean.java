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


package oscar.oscarEncounter.oscarMeasurements.bean;

import org.oscarehr.common.model.Validations;


public class EctValidationsBean{

       int id;
       String name;
       String regularExp = null;
       String maxValue = null;
       String minValue = null;
       String maxLength = null;
       String minLength = null;
       String isNumeric = null;       
       String isDate = null;
       
       public EctValidationsBean(){
       }

       public EctValidationsBean(String name, int id){
            this.name = name;
            this.id = id;
       }

	public EctValidationsBean(Validations v) {
		if (v.getName() != null) {
			setName(v.getName());
		}
		if (v.getRegularExp() != null) {
			setRegularExp(v.getRegularExp());
		}
		if (v.getMinValue() != null) {
			setMinValue("" + v.getMinValue());
		}
		if (v.getMaxValue() != null) {
			setMaxValue("" + v.getMaxValue());
		}
		if (v.getMinLength() != null) {
			setMinLength("" + v.getMinLength());
		}
		if (v.getMaxLength() != null) {
			setMaxLength("" + v.getMaxLength());
		}
		if (v.isNumeric() != null) {
			setIsNumeric("" + v.isNumeric());
		}
		if (v.isDate() != null) {
			setIsDate("" + v.isDate());
		}

	}

	public int getId(){
           return id;
       }
       public String getName(){
           return name;
       }
       public String getRegularExp(){
           return regularExp;
       }
       public String getMaxValue(){
           return maxValue;
       }
       public String getMinValue(){
           return minValue;
       }
       public String getMaxLength(){
           return maxLength;
       }
       public String getMinLength(){
           return minLength;
       }
       public String getIsNumeric(){
           return isNumeric;
       }
       public String getIsDate(){
           return isDate;
       }
       
       
       public void setId(int id){
           this.id = id;
       }
       public void setName(String name){
           this.name = name;
       }
       public void setRegularExp(String regularExp){
           this.regularExp = regularExp;
       }
       public void setMaxValue(String maxValue){           
           this.maxValue = maxValue;
       }
       public void setMinValue(String minValue){           
           this.minValue = minValue;           
       }       
       public void setMaxLength(String maxLength){           
           this.maxLength = maxLength;
       }
       public void setMinLength(String minLength){           
           this.minLength = minLength;           
       }
       public void setIsNumeric(String isNumeric){           
           this.isNumeric = isNumeric;
       }
       public void setIsDate(String isDate){           
           this.isDate = isDate;
       }
}
