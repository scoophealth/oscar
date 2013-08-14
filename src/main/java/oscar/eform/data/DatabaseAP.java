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


package oscar.eform.data;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

public class DatabaseAP {
    private String apName = null;
    private String apSQL = null;
    private String apOutput = null;
    private String apInSQL = null;
    private boolean apJsonOutput = false;
    private boolean isInputField = false;
    private String archive;
    

    /** Creates a new instance of DatabaseAP */
    public DatabaseAP() {
    }

    public DatabaseAP(String apName, String apSQL, String apOutput) {
        set(apName, apSQL, apOutput);
    }


    public DatabaseAP(String apName, String apSQL, String apOutput, String apInSQL) {
        set(apName, apSQL, apOutput, apInSQL);
    }

    public DatabaseAP(DatabaseAP ap2) {
        this.apName = ap2.getApName();
        this.apSQL = ap2.getApSQL();
        this.apOutput = ap2.getApOutput();
        this.apInSQL = ap2.getApInSQL();
    }
    public void set(String apName, String apSQL, String apOutput) {
        this.apName = apName;
        this.apSQL = apSQL;
        this.apOutput = apOutput;
    }

    public void set(String apName, String apSQL, String apOutput, String apInSQL) {
        this.apName = apName;
        this.apSQL = apSQL;
        this.apOutput = apOutput;
        this.apInSQL = apInSQL;
    }
    
    public void set(String apName, String apSQL, String apOutput, String apInSQL, String archive) {
        this.apName = apName;
        this.apSQL = apSQL;
        this.apOutput = apOutput;
        this.apInSQL = apInSQL;
        this.archive = archive;
    }

    public void setApName(String apName) { this.apName = apName; }
    public void setApSQL(String apSQL) { this.apSQL = apSQL; }
    public void setApOutput(String apOutput) { this.apOutput = apOutput; }

    public String getApName() { return(apName); }
    public String getApSQL() { return(apSQL); }
    public String getApOutput() { return(apOutput); }


    public String getApInSQL() {
		return apInSQL;
	}

	public void setApInSQL(String apInSQL) {
		this.isInputField = true;
		this.apInSQL = apInSQL;
	}
	
	public void setApJsonOutput(boolean apJsonOutput) {
		this.apJsonOutput = apJsonOutput;
	}
	

	public String getArchive() {
    	return archive;
    }

	public void setArchive(String archive) {
    	this.archive = archive;
    }

	public boolean isInputField() {
		return isInputField;
	}

	public boolean isJsonOutput() {
		return apJsonOutput;
	}

    public static String parserReplace(String name, String var, String str) {
        //replaces <$name$> with var in str
        StringBuilder strb = new StringBuilder(str);
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >= 0) {
            tagend = strb.indexOf("}", tagstart);
            if (strb.substring(tagstart+2, tagend).equals(name)) {
                strb.replace(tagstart, tagend+1, var==null?"":var);
            }
        }
        return strb.toString();
    }
    public static String parserReplace(String name, String var, DatabaseAP dbap, boolean inSql) {
		String sql;
		if (inSql) sql = dbap.getApInSQL();
		else sql = dbap.getApSQL();

		var = StringEscapeUtils.escapeSql(var);

		sql = DatabaseAP.parserReplace(name, var, sql);

		return sql;
	}

    public static ArrayList<String> parserGetNames(String str) {
        StringBuilder strb = new StringBuilder(str);
        ArrayList<String> names = new ArrayList<String>();
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
        StringBuilder strb = new StringBuilder(str);
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
