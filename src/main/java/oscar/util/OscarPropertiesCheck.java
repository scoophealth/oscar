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


package oscar.util;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.OscarProperties;



/** Tag class for evaulating a property from oscar properties.
 *
 * If the value is equal, the jsp code will be included in the page.
 *
 */
public class OscarPropertiesCheck extends TagSupport {

    protected String value = null;
    protected String property = null;
    protected String defaultVal = null;
    protected String reverse = null;
    
    public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}

	public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value.trim();
    }

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property.trim();
    }



    public int doStartTag() throws JspException {

        boolean conditionMet = false ;
        
        String prop = getProperty();
        String val  = getValue();
        
        boolean rev=false;
        if(getReverse() != null && getReverse().equalsIgnoreCase("true")) {
        	rev=true;
        }
        
        try{            
            String oscarVal = OscarProperties.getInstance().getProperty(prop);
            if (oscarVal.equals(val)){
                conditionMet = true;
            }
            
            if(rev) {
            	conditionMet = !conditionMet;
            }
        }catch(Exception invalidProp){             
            if (defaultVal != null && defaultVal.equalsIgnoreCase("true")){
               conditionMet = true;
            }
            if(rev) {
            	conditionMet=true;
            }
        }
               
        if (conditionMet)
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);

    }

    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }
   
    public void release() {

        super.release();                
        value = null;        
        property = null;        
    }

    /**
     * Getter for property defaultVal.
     * @return Value of property defaultVal.
     */
    public java.lang.String getDefaultVal() {
       return defaultVal;
    }    

    /**
     * Setter for property defaultVal.
     * @param defaultVal New value of property defaultVal.
     */
    public void setDefaultVal(java.lang.String defaultVal) {
       this.defaultVal = defaultVal;
    }    


}
