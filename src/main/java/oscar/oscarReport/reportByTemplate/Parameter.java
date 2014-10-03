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

package oscar.oscarReport.reportByTemplate;

/**
 * Created on December 19, 2006, 10:54 AM
 * This is an object representing each dynamic param in an sql query (i.e. billing_code) 
 * @author apavel (Paul)
 */

import java.util.ArrayList;

public class Parameter {
    public static final String LIST = "list";
    public static final String CHECK = "check";
    public static final String TEXT = "text";
    public static final String DATE = "date";
    public static final String TEXTLIST = "textlist"; 
    
    //public static final int TYPE_INTEGER = 1;
    //public static final int TYPE_STRING = 2;
    //public static final int TYPE_DATE = 3;
    private String paramId = "";
    private String paramDescription = "";
    private String paramType;
    private ArrayList paramChoices; //must be a str array, uninstantiated or empty if the type is TEXT and DATE
    
    public Parameter() {
    }
    
    //choices.size() = 0 if 'text' or 'date' type
    public Parameter(String paramId, String paramType, String paramDescription, ArrayList paramChoices) {
        this.setParamId(paramId);
        this.setParamType(paramType);
        this.setParamDescription(paramDescription);
        this.setParamChoices(paramChoices);
    }
    
    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }
    
    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
    
    public String getParamType() {
        return paramType;
    }

    public ArrayList getParamChoices() {
        return paramChoices;
    }

    public void setParamChoices(ArrayList paramChoices) {
        this.paramChoices = paramChoices;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

}
