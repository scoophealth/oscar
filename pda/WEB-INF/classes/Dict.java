package oscar;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

public class Dict extends Properties {
  //it is a help class for storing retrieving Strings, derived from Hashtable
  //case insensitive for the name
  public Dict() {}
  
  public void setDef(String name, String val) {
    setProperty(name.toUpperCase(), val);
    //System.out.println("setDef(): name: "+name+"   value: " +val);
  }
  public void setDef(String [][]pairs) {
    for(int i=0;i<pairs.length;i++)
      setDef(pairs[i][0],pairs[i][1]);
  }
  public void setDef(String[]names, String[]vals) {
    int len=names.length; if(len>vals.length) len=vals.length;
    for(int i=0; i<len; i++) {
      setDef(names[i], vals[i]);
      //System.out.println("setDef([]): name: "+names[i]+"   value: " +vals[i]);
    }
  }
  public void setDef(HttpServletRequest req) {
    java.util.Enumeration enum=req.getParameterNames();
    while(enum.hasMoreElements()) {
      String name=(String)enum.nextElement();
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
