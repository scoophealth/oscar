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
package oscar.login;

import java.util.*;
import java.io.*;

public class LoginCheckLogin {
  //boolean bWAN = true ;
  Properties pvar = null;

  LoginCheckLoginBean lb = null;

  public static void main(String arg[]) {
    LoginCheckLogin a = new LoginCheckLogin();
  }

  public void setOscarVariable(String propFile) {
    pvar = new Properties();
    pvar.setProperty("file_separator", System.getProperty("file.separator")); 
    pvar.setProperty("working_dir", System.getProperty("user.dir"));
    char sep = pvar.getProperty("file_separator").toCharArray()[0];
    String strTemp = pvar.getProperty("working_dir").substring(0,pvar.getProperty("working_dir").indexOf(sep));

    try {
      pvar.load(new FileInputStream(strTemp + sep + "root" + sep + propFile )); 
    } catch(Exception e) {System.out.println("*** No Property File ***"); }
    
  }
  public Properties getOscarVariable() {
    return pvar ;
  }

  //authenticate is used to check password
  public String[] auth(String user_name, String password, String pin, String ip, String propFile) {
    setOscarVariable(propFile) ;
    lb = new LoginCheckLoginBean() ;
    lb.ini(user_name, password, pin, ip, pvar);
    return lb.authenticate();
  }

  public String [] getPreferences() {
    return lb.getPreferences() ;
  }

}
