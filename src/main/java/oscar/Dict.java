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


package oscar;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class Dict extends Properties {
  //it is a help class for storing retrieving Strings, derived from Hashtable
  //case insensitive for the name
  public Dict() {}
  
  public void setDef(String name, String val) {
    setProperty(name.toUpperCase(), val);

  }
  public void setDef(String [][]pairs) {
    for(int i=0;i<pairs.length;i++)
      setDef(pairs[i][0],pairs[i][1]);
  }
  public void setDef(String[]names, String[]vals) {
    int len=names.length; if(len>vals.length) len=vals.length;
    for(int i=0; i<len; i++) {
      setDef(names[i], vals[i]);

    }
  }
  public void setDef(HttpServletRequest req) {
    java.util.Enumeration num=req.getParameterNames();
    while(num.hasMoreElements()) {
      String name=(String)num.nextElement();
      String val=req.getParameter(name);
      setDef(name,val);
    }
  }
  public String getDef(String name) {
    return getDef(name,"");
  }
  
  public String getDef(String name, String dflt) {
  	String val=getProperty(name.toUpperCase(),dflt);
  	return val;
  }
  public String getShortDef(String name, String dflt, int nLimit) {
  	String val=getProperty(name.toUpperCase(),dflt);
  	int nLength = val.length();
  	if(nLength>nLimit) {
  	  val = val.substring(0,nLimit);	
  	} 
  	return val;
  }
  
}
