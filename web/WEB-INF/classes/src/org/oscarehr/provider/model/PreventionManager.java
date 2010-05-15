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
 * <OSCAR TEAM>
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package org.oscarehr.provider.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.map.LRUMap;


import oscar.oscarPrevention.Prevention;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDS;

/**
 *
 * @author rjonasz
 */
public class PreventionManager {
    private static final int MAXITEMS = 500;
    private static final String PREVS = "dprv";

    private PreventionDisplayConfig pdc;    
    private LRUMap demoPrevs;
    private Map<String,LRUMap>mShell;    
    private PreventionDS pf;

    public PreventionManager() {
        pdc = PreventionDisplayConfig.getInstance();
        demoPrevs = new LRUMap(MAXITEMS);
        mShell = new HashMap<String,LRUMap>(1);
        mShell.put(PREVS, demoPrevs);
        mShell = Collections.synchronizedMap(mShell);        
        pf = PreventionDS.getInstance();
    }

    public synchronized String getWarnings(String demo) {
        String ret = (String)mShell.get(PREVS).get(demo);
        //System.out.println("Fetching Demo " + demo + " '" + ret + "'");
        if( ret == null ) {
            //synchronized(this) {
                try {
                    PreventionData prevData = new PreventionData();
                    Prevention prev = prevData.getPrevention(demo);
                    pf.getMessages(prev);
                    ArrayList warnings = prev.getWarnings();
                    ret = StringUtils.join(warnings, ". ");
                    //warnings = prev.getReminder();
                    if( ret.length() > 0 ) {
                        ret += ". ";
                    }
                    //ret += StringUtils.join(warnings, ". ");
                    
                    mShell.get(PREVS).put(demo, ret);
                    //System.out.println("Cached demo " + demo + " '" + ret + "'");
                } catch(Exception e) {
                    ret = "";
                    e.printStackTrace();
                }
            //}
        }
        return ret;
    }

     public synchronized void removePrevention(String demo) {
            mShell.get(PREVS).remove(demo);
         
     }

}
