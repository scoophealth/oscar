// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.PrintStream;
import org.apache.struts.action.ActionForm;

public final class EctViewConsultationRequestsForm extends ActionForm
{

    public String getSendTo()
    {
        System.out.println("get sendTo ".concat(String.valueOf(String.valueOf(sendTo))));
        return sendTo;
    }

    public void setSendTo(String str)
    {
        System.out.println("set send to ".concat(String.valueOf(String.valueOf(str))));
        sendTo = str;
    }

    public String getCurrentTeam()
    {
        System.out.println("get current team ".concat(String.valueOf(String.valueOf(currentTeam))));
        if(currentTeam == null)
            currentTeam = new String();
        return currentTeam;
    }

    public void setCurrentTeam(String str)
    {
        System.out.println("set current team ".concat(String.valueOf(String.valueOf(str))));
        currentTeam = str;
    }

    String sendTo;
    String currentTeam;
}
