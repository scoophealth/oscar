/*
 * 
 */


package oscar.login;

import java.util.GregorianCalendar;

/**
 * Class LoginInfoBean : set login status when bWAN = true
 * 2003-01-29
*/
public final class LoginInfoBean {
    private GregorianCalendar starttime = null;
    private int times = 1;
    private int status = 1; // 1 - normal, 0 - block out
  
    private int maxtimes = 3;
    private int maxduration = 10;

    public LoginInfoBean() {}
    public LoginInfoBean(GregorianCalendar starttime1, int maxtimes1, int maxduration1 ) {
        starttime = starttime1;
        maxtimes = maxtimes1;
        maxduration = maxduration1;
	  }
	
    public void initialLoginInfoBean( GregorianCalendar starttime1 ) {
        starttime = starttime1;
        int times = 0;
        int status = 1; // 1 - normal, 0 - block out
	  }

    public void updateLoginInfoBean( GregorianCalendar now, int times1 ) {
  	    //if time out, initial bean again. 
  	    if (getTimeOutStatus(now) ) {
        		initialLoginInfoBean(now);
  		      return;
  	    }
       	//else times++. if times out, status block
      	++times;
  	    if (times > maxtimes) 
            status = 0; // 1 - normal, 0 - block out
	  }

    public boolean getTimeOutStatus( GregorianCalendar now ) {
  	    boolean btemp = false;
      	//if time out and status is 1, return true
  	    GregorianCalendar cal = (GregorianCalendar) starttime.clone();
      	cal.add(cal.MINUTE, maxduration);
  	    if (cal.getTimeInMillis() < now.getTimeInMillis() ) 
            btemp = true; //starttime = starttime1;
            //System.out.println(" "+cal.getTimeInMillis()+"  now: "+now.getTimeInMillis());      
        return btemp;
	  }

    public void setStarttime(GregorianCalendar starttime1 ) { 
        starttime = starttime1;
    }  
    public void setTimes(int times1 ) { 
        times = times1;
    }  
    public void setStatus(int status1 ) { 
        status = status1;
    }  

    public GregorianCalendar getStarttime() { 
        return (starttime);
    }  
    public int getTimes() { 
        return(times);
    }  
    public int getStatus() { 
        return(status);
    }  
}