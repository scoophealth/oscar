/*
 * 
 */


package oscar.login;

import java.util.*;

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