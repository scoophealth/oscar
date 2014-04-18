/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package oscar.oscarEncounter.data;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;
import org.caisi.service.InfirmBedProgramManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 *
 * @author rjonasz
 */
public class EctProgram {
    private HttpSession se;
    /** Creates a new instance of EctProgram */
    public EctProgram(HttpSession se) {
        this.se = se;
    }
    
    public String getProgram(String providerNo) {        		
		
		List programBean;
		
		InfirmBedProgramManager manager=getInfirmBedProgramManager();
		programBean=manager.getProgramBeans(providerNo, null);	
	
		//get default program
		int defaultprogramId=manager.getDefaultProgramId(providerNo);
		boolean defaultInList=false;
		for (int i=0;i<programBean.size();i++){
			int id=new Integer(((LabelValueBean) programBean.get(i)).getValue()).intValue();
			if ( defaultprogramId == id ) defaultInList=true;
		}
		if (!defaultInList) defaultprogramId=0;
		int OriprogramId=0;
		if (programBean.size()>0) OriprogramId=new Integer(((LabelValueBean) programBean.get(0)).getValue()).intValue();
		int programId=0;
		if (defaultprogramId!=0 && OriprogramId!=0) programId=defaultprogramId;
		else {
			if (OriprogramId==0) 
                            programId=0;
			if (defaultprogramId==0 && OriprogramId!=0) 
                            programId=OriprogramId;
		}
		
		return String.valueOf(programId);
    }
    
    public ApplicationContext getAppContext()
    {
        return WebApplicationContextUtils.getWebApplicationContext(
        		se.getServletContext());
    }

    public InfirmBedProgramManager getInfirmBedProgramManager() {
		InfirmBedProgramManager bpm = (InfirmBedProgramManager) getAppContext()
				.getBean("infirmBedProgramManager");
		return bpm;
    }
    
}
