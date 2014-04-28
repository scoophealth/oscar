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


package oscar.oscarPrevention.reports;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.oscarMeasurements.util.WriteNewMeasurements;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class FollowupManagement {
    private static Logger log = MiscUtils.getLogger();
    final public String LETTER1 = "L1";
    final public String LETTER2 = "L2";
    final public String PHONE1 = "P1";
    
    /** Creates a new instance of FollowupManagement */
    public FollowupManagement() {
    }
    
    
    
    /**
     * Class used to mark prevention follow up procedures
     *@param followUpType   --  procedure type eg PAPF,FLUF,MAMF,FOBF,CIMF 
     *@param followUpValue  --  proedure value eg L1,L2,P1 letter 1, letter 2, phone call 1
     *@param demographicList  --  list of demographic numbers as strings 
     *@param providerNo            --  provider # generating the list
     *@param dateObserved          --  date list is generated 
     *@param comment				-- comment
     */
    public void markFollowupProcedure(String followUpType,String followUpValue, List demographicList, String providerNo,Date dateObserved,String comment){
        for (int i = 0; i < demographicList.size();i++){
            String demographicNo = (String) demographicList.get(i);
            writeProcedure(followUpType, followUpValue, demographicNo, providerNo ,dateObserved,comment);           
        }
        
    }
    
    /**
     * Class used to mark prevention follow up procedures
     *@param followUpType   --  procedure type eg PAPF,FLUF,MAMF,FOBF,CIMF 
     *@param followUpValue  --  proedure value eg L1,L2,P1 letter 1, letter 2, phone call 1
     *@param demographicList  --  list of demographic numbers as strings 
     *@param providerNo            --  provider # generating the list
     *@param dateObserved          --  date list is generated 
     *@param comment			-- comment
     */
    public void markFollowupProcedure(String followUpType,String followUpValue, String[] demographicList, String providerNo,Date dateObserved,String comment){
        for (int i = 0; i < demographicList.length;i++){          	
            writeProcedure(followUpType, followUpValue, demographicList[i], providerNo ,dateObserved,comment);
            
        }
        
    }

    private void writeProcedure(final String followUpType, final String followUpValue, final String demographicNo, final String providerNo,final Date dateObserved,final String comment ) {        
        log.debug("Calling WriteProcedure for "+demographicNo);
        Hashtable measure = new Hashtable();
        measure.put("value",followUpValue);
        measure.put("type",followUpType);
        measure.put("measuringInstruction","");
        measure.put("comments",  comment == null ? "":comment  );
        measure.put("dateObserved",UtilDateUtilities.DateToString(dateObserved, "yyyy-MM-dd HH:mm:ss"));
        measure.put("dateEntered",UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        WriteNewMeasurements.write(measure,demographicNo,providerNo);
    }
    
    
    
    
    
    
}
