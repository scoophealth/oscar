/*
 * 
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
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
/*
 * 
 */


package oscar.login;

import java.util.Hashtable;

/**
 * Class LoginList : an application level login list: ip/LoginInfoBean 
 * 2003-01-29
*/
public final class LoginList extends Hashtable{
    //only one instance to use
    private static LoginList loginList;
    private LoginList() {}
    public synchronized static LoginList getLoginListInstance( ) {
        if (loginList == null) {
            loginList = new LoginList();
        }
        return loginList;
    }
}

/*
    private LoginList(GregorianCalendar starttime1, int maxtimes1, int maxduration1 ) {
        starttime = starttime1;
        maxtimes = maxtimes1;
        maxduration = maxduration1;
	  }
    public synchronized static LoginList getLoginListInstance(GregorianCalendar starttime1, int maxtimes1, int maxduration1 ) {
        if (loginList == null) {
            loginList = new LoginInfoBean(starttime1, maxtimes1, maxduration1 );
        }
        return logInfo;
    }
    
    private java.util.Hashtable loginInfo;
	  boolean bWAN = true ;
  
if(request.getRemoteAddr().startsWith(oscarVariables.getProperty("login_local_ip")) ) bWAN = false ;
  loginBean.setIp(request.getRemoteAddr());
  
  //delete the old entry in the loginfo bean
  GregorianCalendar now = new GregorianCalendar();
  LoginInfoBean info = null;
  String sTemp = null;
  if(!loginInfo.isEmpty() && bWAN) {
    for (Enumeration e = loginInfo.keys() ; e.hasMoreElements() ;) {
      sTemp = (String) e.nextElement();
      info = (LoginInfoBean) loginInfo.get(sTemp );
      if(info.getTimeOutStatus(now) ) loginInfo.remove(sTemp);
    }
    //check if it is blocked
    if( ((LoginInfoBean) loginInfo.get( request.getRemoteAddr())).getStatus() ==0 )  {
      response.sendRedirect("block.htm");
      return;
    }
  }

*/