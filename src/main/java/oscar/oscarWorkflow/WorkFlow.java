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


/*
 * WorkFlow.java
 *
 * Created on November 7, 2006, 10:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarWorkflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Generic WorkFlow Description.  
 * 
 
 * @author jay
 */
public interface WorkFlow {
    public ArrayList getActiveWorkFlowList(String demographicNo);
    public ArrayList getActiveWorkFlowList();
    public String getState(String state);  
    public List getStates();
    public WorkFlowInfo executeRules(Hashtable hashtable);
    public WorkFlowInfo executeRules(WorkFlowDS wfDS,Hashtable hashtable);
    public String getLink(String demographic,String workFlowId);
    int addToWorkFlow(String providerNo, String demographicNo, Date endDate);
    public WorkFlowDS getWorkFlowDS();
    
}
