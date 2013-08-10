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


package oscar.eform;

import java.util.ArrayList;

import net.sf.json.JSONArray;

import org.oscarehr.util.MiscUtils;

import oscar.eform.data.DatabaseAP;

/**
 *
 * @author jay
 */
public class APExecute {

    /** Creates a new instance of APExecute */
    public APExecute() {
    }

    public String execute(String ap,String demographicNo){
        EFormLoader.getInstance();
		DatabaseAP dap = EFormLoader.getAP(ap);
        String  sql = DatabaseAP.parserReplace("demographic", demographicNo, dap.getApSQL());
        String output = dap.getApOutput();
        MiscUtils.getLogger().debug("SQL----" + sql);
        ArrayList<String> names = DatabaseAP.parserGetNames(output); //a list of ${apName} --> apName
        sql = DatabaseAP.parserClean(sql);  //replaces all other ${apName} expressions with 'apName'
        
		if (dap.isJsonOutput()) {
			JSONArray values = EFormUtil.getJsonValues(names, sql);
			output = values.toString(); //in case of JsonOutput, return the whole JSONArray and let the javascript deal with it
		}
		else {
			ArrayList<String> values = EFormUtil.getValues(names, sql);
	        if (values.size() != names.size()) {
	            output = "";
	        } else {
	            for (int i=0; i<names.size(); i++) {
	                output = DatabaseAP.parserReplace(names.get(i), values.get(i), output);
	            }
	        }
		}
        return output;
    }
    
    public String execute(String ap, String demographicNo, Integer invoiceNo){
        EFormLoader.getInstance();
	DatabaseAP dap = EFormLoader.getAP(ap);
        MiscUtils.getLogger().debug("AP:" + ap);
        String sql = DatabaseAP.parserReplace("invoiceNo", String.valueOf(invoiceNo), dap.getApSQL());                       
        sql = DatabaseAP.parserReplace("demographic", demographicNo, sql); 
        
        String output = dap.getApOutput();
        MiscUtils.getLogger().debug("SQL----" + sql);
        
        ArrayList<String> names = DatabaseAP.parserGetNames(output); 
        sql = DatabaseAP.parserClean(sql);
        
        ArrayList<String> values = EFormUtil.getValues(names, sql);
        if (values.size() != names.size()) {
            output = "";
        } else {
            for (int i=0; i<names.size(); i++) {                
                output = DatabaseAP.parserReplace(names.get(i), values.get(i), output);
            }
        }

        return output;
    }
}
