package oscar.login;

import java.util.*;
import java.io.*;

public class LoginCheckLogin {
    boolean bWAN = true ;
    Properties pvar = null;

    LoginCheckLoginBean lb = null;
    LoginInfoBean linfo = null;
    LoginList llist = null;

    public LoginCheckLogin() { }
    public LoginCheckLogin(String propFile) { 
        setOscarVariable(propFile) ;
    }


    public boolean isBlock(String ip) {
        boolean bBlock = false;

        //judge the local network
        if(ip.startsWith(pvar.getProperty("login_local_ip")) ) bWAN = false ;
  
        GregorianCalendar now = new GregorianCalendar();
        while(llist == null) {
            llist = LoginList.getLoginListInstance(); //LoginInfoBean info = null;
        }
        String sTemp = null;

        //delete the old entry in the loginlist if time out
        if (bWAN && !llist.isEmpty() ) {
            for (Enumeration e = llist.keys() ; e.hasMoreElements() ;) {
                sTemp = (String) e.nextElement();
                linfo = (LoginInfoBean) llist.get(sTemp );
                if (linfo.getTimeOutStatus(now) ) llist.remove(sTemp);
            }
    
            //check if it is blocked
            if( llist.get(ip) != null && ((LoginInfoBean) llist.get(ip) ).getStatus() ==0 )  bBlock = true;
        }
    
        return bBlock;
    }
    
    //authenticate is used to check password
    public String[] auth(String user_name, String password, String pin, String ip) {
        lb = new LoginCheckLoginBean() ;
        lb.ini(user_name, password, pin, ip, pvar);
        return lb.authenticate();
    }

    //update login list if login failed
    public synchronized void updateLoginList(String ip) {
        if (bWAN) {
            GregorianCalendar now = new GregorianCalendar();
            if (llist.get(ip) == null ) {
                linfo = new LoginInfoBean(now, Integer.parseInt(pvar.getProperty("login_max_failed_times")), Integer.parseInt(pvar.getProperty("login_max_duration")));
            } else {
                linfo = (LoginInfoBean) llist.get(ip);
                linfo.updateLoginInfoBean(now, 1);
            }
            llist.put(ip, linfo);
            System.out.println(ip+"  status: "+ ((LoginInfoBean) llist.get(ip)).getStatus() + " times: "+ linfo.getTimes()+ " time: " );
        }
    }

//        loginBean.setIp(request.getRemoteAddr());

    public void setOscarVariable(String propFile) {
        pvar = new Properties();
        pvar.setProperty("file_separator", System.getProperty("file.separator")); 
        pvar.setProperty("working_dir", System.getProperty("user.dir"));
        char sep = pvar.getProperty("file_separator").toCharArray()[0];
        String strTemp = pvar.getProperty("working_dir").substring(0,pvar.getProperty("working_dir").indexOf(sep));

        try {
	   //This line commented because it was substituted by the code below it.
           //There's no need to mount the file name this way because now we suppose the parameter 'propFile'
           //brings the complete configuration file name.
           //It was made because we don't want it to read files from the root diretory anymore.
	   //FileInputStream fis = new FileInputStream(strTemp + sep + "root" + sep + propFile ) ;

  	    FileInputStream fis = new FileInputStream(propFile);
            pvar.load(fis); 
            fis.close();
        } catch(Exception e) {
            System.out.println("*** No Property File ***");
            e.printStackTrace();
        }
    }

    public Properties getOscarVariable() {
        return pvar ;
    }

    public String [] getPreferences() {
        return lb.getPreferences() ;
    }

}
