/*
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
 *  Ontario, Canada   Creates a new instance of Prevention
 *
 * SQLDenominator.java
 *
 * Created on June 17, 2006, 3:03 PM
 *
 */

package oscar.oscarReport.ClinicalReports;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import oscar.oscarDB.DBHandler;

/**
 * This is for straight SQLDenominators  not sure if it should return a more specialised list
 * @author jay
 */
public class SQLDenominator implements Denominator{
    String sql = null;
    String exeSql = null;
    String resultString = "demographic_no";
    String name;
    String id;
    String[] replaceKeys = null;
    Hashtable replaceableValues = null;
    
    
    
    /** Creates a new instance of SQLDenominator */
    public SQLDenominator() {
    }
    
    public void setSQL(String sql){
       this.sql = sql;   
    }
    
    public void setResultString(String str){
        this.resultString = str;
    }

    public List getDenominatorList() {
        ArrayList list = new ArrayList();
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            if (replaceableValues != null){
                System.out.println("has replaceablevalues"+replaceableValues.size());
                System.out.println("before replace \n"+sql);
                exeSql = replaceAll(sql, replaceableValues);
            }else{              
                System.out.println("doesn't have replaceablevalues");
                exeSql = sql;
                System.out.println("sql "+sql);
            }
            
            ResultSet rs = db.GetSQL(exeSql);
            System.out.println("SQL Statement: " + exeSql);
            while(rs.next()){
               String toAdd = db.getString(rs,resultString);
               list.add(toAdd);
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String getDenominatorName() {
        return this.name;
    }
    public void setDenominatorName(String name){
        this.name = name;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public String replaceAll(String str,Hashtable replacers){
        Enumeration e =  replacers.keys();
        while(e.hasMoreElements()){
           String processString = (String) e.nextElement();         
           String replaceValue = (String) replacers.get(processString);
           System.out.println ("key :"+processString+" value :"+replaceValue);
           str = str.replaceAll("\\$\\{"+processString+"\\}", replaceValue);
           System.out.println(str);
           
        }
        return str;
    }
    
    public String[] getReplaceableKeys(){
        return replaceKeys;
    }
    
    public void parseReplaceValues(String str){
        if (str != null){
            try{
                System.out.println("parsing string "+str);
                if (str.indexOf(",") != -1){
                replaceKeys = str.split(",");
                }else{
                    replaceKeys =  new String[1];
                    replaceKeys[0] = str;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public boolean hasReplaceableValues(){
        boolean repVal = false;
        if (replaceKeys != null){
            repVal = true;
        }
        return repVal;
    }

    public void setReplaceableValues(Hashtable vals) {
        replaceableValues = vals;
    }

    public Hashtable getReplaceableValues() {
        return replaceableValues;
    }
    
    
}
