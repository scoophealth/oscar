/*
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */
package oscar.eform.data;

import java.util.ArrayList;

public class DatabaseAP {
    private String apName = null;
    private String apSQL = null;
    private String apOutput = null;
    
    /** Creates a new instance of DatabaseAP */
    public DatabaseAP() {
    }
    
    public DatabaseAP(String apName, String apSQL, String apOutput) {
        set(apName, apSQL, apOutput);
    }
    
    public DatabaseAP(DatabaseAP ap2) {
        this.apName = ap2.getApName();
        this.apSQL = ap2.getApSQL();
        this.apOutput = ap2.getApOutput();
    }
    public void set(String apName, String apSQL, String apOutput) {
        this.apName = apName;
        this.apSQL = apSQL;
        this.apOutput = apOutput;
    }
    
    public void setApName(String apName) { this.apName = apName; }
    public void setApSQL(String apSQL) { this.apSQL = apSQL; }
    public void setApOutput(String apOutput) { this.apOutput = apOutput; }
    
    public String getApName() { return(apName); }
    public String getApSQL() { return(apSQL); }
    public String getApOutput() { return(apOutput); }
    
    
    public static String parserReplace(String name, String var, String str) {
        //replaces <$name$> with var in str
        StringBuffer strb = new StringBuffer(str);
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >= 0) {
            tagend = strb.indexOf("}", tagstart);
            if (strb.substring(tagstart+2, tagend).equals(name)) {
                strb.replace(tagstart, tagend+1, var);
            }
        }
        return strb.toString();
    }
    public static ArrayList parserGetNames(String str) {
        StringBuffer strb = new StringBuffer(str);
        ArrayList names = new ArrayList();
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >= 0) {
            tagend = strb.indexOf("}", tagstart);
            names.add(strb.substring(tagstart+2, tagend));
        }
        return names;
    }
    public static String parserClean(String str) {
        //removes left over ${...} in str; replaces with ""
        StringBuffer strb = new StringBuffer(str);
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >=0) {
            strb.replace(tagstart, tagstart+2, "\"");
            tagend = strb.indexOf("}", tagstart);
            strb.replace(tagend, tagend+2, "\"");
        }
        return strb.toString();
    }
    
}
