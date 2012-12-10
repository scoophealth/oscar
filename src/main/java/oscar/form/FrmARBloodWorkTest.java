/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class FrmARBloodWorkTest {

	private int ar1BloodWorkTestListSize = 0;
	private Properties ar1Props = new Properties();
	
	public FrmARBloodWorkTest(int demographicNo, int formId){
		
		ResultSet rs = null;

        if(demographicNo > 0  &&  formId >= 0) {
        	
        	try{
				
	            String sql = " SELECT pg1_labHb, pg1_labMCV, pg1_labABO, pg1_labRh, pg1_labAntiScr, " + 
	                         " pg1_labRubella, pg1_labHBsAg, pg1_labVDRL, pg1_labHIV  FROM formONAR " + 
	                         " WHERE demographic_no=" + demographicNo + 
	                         " AND ID=" + formId;
	            
	            
	            rs = DBHandler.GetSQL(sql);
	            
	            if(rs.next()) {//only retrieve the first record

	            	ar1BloodWorkTestListSize = getFormARBloodWorkTestListSize(rs);
	            	ar1Props = getFormARBloodWorkTestABOAndRH(rs);
	            }

        	}catch(SQLException sqlEx){
        		MiscUtils.getLogger().error("error",sqlEx);
        	}
            finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                    	//ignore
                    }
                }
            }
        	
        }
	}
	
	private int getFormARBloodWorkTestListSize(ResultSet rs)
            throws SQLException    {
        int bwTestListSize = 0;
        
    	if(oscar.Misc.getString(rs,"pg1_labHb") != null  &&  oscar.Misc.getString(rs,"pg1_labHb").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labMCV") != null  &&  oscar.Misc.getString(rs,"pg1_labMCV").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labABO") != null  &&  oscar.Misc.getString(rs,"pg1_labABO").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labRh") != null  &&  oscar.Misc.getString(rs,"pg1_labRh").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labAntiScr") != null  &&  oscar.Misc.getString(rs,"pg1_labAntiScr").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labRubella") != null  &&  oscar.Misc.getString(rs,"pg1_labRubella").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labHBsAg") != null  &&  oscar.Misc.getString(rs,"pg1_labHBsAg").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labVDRL") != null  &&  oscar.Misc.getString(rs,"pg1_labVDRL").length() > 0 ){
    		bwTestListSize++;
    	}
    	if(oscar.Misc.getString(rs,"pg1_labHIV") != null  &&  oscar.Misc.getString(rs,"pg1_labHIV").length() > 0 ){
    		bwTestListSize++;
    	}
            	
    	MiscUtils.getLogger().debug("FrmARBloodWorkTest.getFormARBloodWorkTestListSize(): ar1BloodWorkTestCount = " + bwTestListSize);	            	

        return bwTestListSize;
    }

	private Properties getFormARBloodWorkTestABOAndRH(ResultSet rs)
    throws SQLException    {
		Properties props = new Properties();
		
    	if(oscar.Misc.getString(rs,"pg1_labABO") != null  &&  oscar.Misc.getString(rs,"pg1_labABO").length() > 0 ){
    		props.setProperty("pg1_labABO", oscar.Misc.getString(rs,"pg1_labABO"));
    	}
    	if(oscar.Misc.getString(rs,"pg1_labRh") != null  &&  oscar.Misc.getString(rs,"pg1_labRh").length() > 0 ){
    		props.setProperty("pg1_labRh", oscar.Misc.getString(rs,"pg1_labRh"));
    	}
		return props;
	}

	public int getAr1BloodWorkTestListSize() {
		return ar1BloodWorkTestListSize;
	}

	public Properties getAr1Props() {
		return ar1Props;
	}

}
