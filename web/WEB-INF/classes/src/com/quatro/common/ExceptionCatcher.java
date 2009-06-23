/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import org.hibernate.exception.SQLGrammarException;
import java.sql.SQLException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import java.util.Map;
import java.util.Iterator;

public class ExceptionCatcher extends ExceptionHandler{

    public ActionForward execute(Exception ex, ExceptionConfig ae,
            ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException{
		String xx="";
		
    	try {
    		String URL=request.getRequestURI();
    		Map map = request.getParameterMap();
    		StringBuffer sb= new StringBuffer();
    		String cr = Character.toString((char)13) + Character.toString((char)10);
    		for (Iterator it=map.entrySet().iterator(); it.hasNext(); ) {
    		  Map.Entry entry = (Map.Entry)it.next();
    		  Object key = entry.getKey();
    		  String[] value = (String[])entry.getValue();
    		  if(value!=null && value.length>0)
    		    sb.append(key.toString() + "=" + value[0].toString() + "&");
    		  else
      		    sb.append(key.toString() + "=" + "&");
    		}
    		String msg="<br><br>An Error was met on this page.<br>Please contact system administrator with following message:<br><br>";

   		    Logger log = Logger.getLogger(ExceptionCatcher.class);
   		    log.error("Start of Exceptions Trace" + cr);
    		populateException(ex, "\nURL is " + URL + "?" + sb.toString());
     	    
    		if(ex.getClass()==NullPointerException.class)
    			msg= msg + "NullPointerException";
    		else
    			msg= msg + ex.getMessage();

    		request.setAttribute("message", msg);
    		if(ex.getCause()!=null){
    			Exception ex2=(Exception)ex.getCause();
    			populateException(ex2, "");
        		if(ex2.getCause()!=null){
        			Exception ex3=(Exception)ex2.getCause();
        			populateException(ex3, "");
            		if(ex3.getCause()!=null){
            			Exception ex4=(Exception)ex3.getCause();
            			populateException(ex4, "");
                		if(ex4.getCause()!=null){
                			Exception ex5=(Exception)ex4.getCause();
                			populateException(ex5, "");
                		}
            		}
        		}
    		}
   		    log.error("End of Exceptions Trace" + cr + cr + cr);
    		
    	}catch (Exception e) {xx =e.getStackTrace().toString();}
    
    	return (super.execute(ex, ae, mapping, formInstance, request, response));
    }
    
    private void populateException(Exception ex, String URL){
		String cr = Character.toString((char)13) + Character.toString((char)10);
		if(ex.getClass()==ServletException.class){
	      ServletException ex1= (ServletException)ex;
  		  Logger log = Logger.getLogger(ServletException.class);
   	      if(!URL.equals(""))
   	       	log.error(cr + "URL is " + URL + cr + cr + cr +
   	       		ex1.getMessage() + cr +  getStackTrace(ex1) + cr);
   	      else
   	      	log.error(cr + cr +  
   	       		ex1.getMessage() + cr +  getStackTrace(ex1) + cr);
   	         	        
	    }else if(ex.getClass()==SQLGrammarException.class){
	      SQLGrammarException ex2= (SQLGrammarException)ex;
  		  Logger log = Logger.getLogger(SQLGrammarException.class);
  	      if(!URL.equals(""))
   	       	log.error(cr + "URL is " + URL + 
   	       		cr + cr + cr +  
   	       		ex2.getMessage() + cr + ex2.getSQL() + cr +  getStackTrace(ex2) + cr);
   	      else
   	       	log.error(cr + cr +
   	       		ex2.getMessage() + cr + ex2.getSQL() + cr +  getStackTrace(ex2) + cr);
	    }else if(ex.getClass()==SQLException.class){
		      SQLException ex4= (SQLException)ex;
	  		  Logger log = Logger.getLogger(SQLException.class);
	  	      if(!URL.equals(""))
	   	       	log.error(cr + "URL is " + URL + cr + cr + cr +  
	   	       		ex4.getMessage() + cr +  getStackTrace(ex4) + cr);
	   	      else
	   	       	log.error(cr + cr +  
	   	       		ex4.getMessage() + cr +  getStackTrace(ex4) + cr);
	    }else if(ex.getClass()==NullPointerException.class){
	    	NullPointerException ex5= (NullPointerException)ex;
  		    Logger log = Logger.getLogger(NullPointerException.class);
  	        if(!URL.equals(""))
	   	       	log.error(cr + "URL is " + URL + cr + cr + cr +  
	   	       		"java.lang.NullPointerException" + cr +  getStackTrace(ex5) + cr);
	   	    else
	   	      	log.error(cr + cr +  
	   	       		"java.lang.NullPointerException" + cr +  getStackTrace(ex5) + cr);
	    }else if(ex.getClass()==InvalidDataAccessResourceUsageException.class){
	      InvalidDataAccessResourceUsageException ex3= (InvalidDataAccessResourceUsageException)ex;
  		  Logger log = Logger.getLogger(InvalidDataAccessResourceUsageException.class);
	  	  if(!URL.equals(""))
	       	log.error(cr + "URL is " + URL + cr +
	       		cr + cr +  
	       		ex3.getMessage() + cr + getStackTrace(ex3) + cr);
	      else
	       	log.error(cr + cr +  
	       		ex3.getMessage() + cr + getStackTrace(ex3) + cr);
	    }else{
  		  Logger log = Logger.getLogger(Exception.class);
		  if(!URL.equals(""))
			log.error(cr + "URL is " + URL + cr + cr + cr +  
				ex.getMessage() + cr + getStackTrace(ex) + cr);
		  else
			log.error(cr + cr +  
				ex.getMessage() + cr +  getStackTrace(ex) + cr);
        }
    }

    private String getStackTrace(Exception ex){
		String cr = Character.toString((char)13) + Character.toString((char)10);
	    StackTraceElement[] lst = ex.getStackTrace();
        StringBuffer sb= new StringBuffer();
	    for(int i=0;i<lst.length;i++){
	    	sb.append(lst[i].getClassName() + "." +
  			lst[i].getMethodName() + "(" +
   			lst[i].getFileName() + ":" + String.valueOf(lst[i].getLineNumber()) + ")" + cr);
	    }
	    
	    return sb.toString();
    	
    }
}
