/**
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
 * Yi Li
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of PreventionDS
 *
 *
 * PreventionDS.java
 *
 * Created on 2005-5-20
 *
 *
 * @author yilee18
 */
package oscar.login.tld;

import java.util.Properties;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarDB.DBHandler;

public class SecurityTag implements Tag {
    private PageContext pageContext;
    private Tag parentTag;
    private String roleName;
    private String orgCd;
    private String objectName;
    private String rights = "r";
    private boolean reverse = false;
    //private Vector roleInObj = new Vector();

    public void setPageContext(PageContext arg0) {
        this.pageContext = arg0;

    }

    public void setParent(Tag arg0) {
        this.parentTag = arg0;
    }

    public Tag getParent() {
        return this.parentTag;
    }

    public int doStartTag() throws JspException {
        /*
         * try { JspWriter out = pageContext.getOut(); out.print("goooooooo"); } catch (Exception e) { }
         */
        int ret = 0;
        Vector v = getPrivilegeProp(objectName);
        // if (checkPrivilege(roleName, (Properties) getPrivilegeProp(objectName).get(0), (Vector) getPrivilegeProp(
        ///        objectName).get(1)))
    	/*TODO: temporily allow current security work, the if statement should be removed */
        if (roleName == null) 
        {
        	if (checkPrivilege(objectName,orgCd, rights)) {
	            ret = EVAL_BODY_INCLUDE;
	        }else{
	            ret = SKIP_BODY;
	        }
        }
        else
        {
	        if (checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1))){
	            ret = EVAL_BODY_INCLUDE;
	        }else{
	            ret = SKIP_BODY;
	        }
        }
        //System.out.println("reverse: " + reverse);
        if (reverse) {
            if (ret == EVAL_BODY_INCLUDE)
                ret = SKIP_BODY;
            else
                ret = EVAL_BODY_INCLUDE;
        }
        return ret;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }    
    private Vector getPrivilegeProp(String objName) {
        Vector ret = new Vector();
        Properties prop = new Properties();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            java.sql.ResultSet rs;
            String [] objectNames  = getVecObjectName(objName);
            StringBuffer objectWhere = new StringBuffer();
            for (int i = 0; i < objectNames.length; i++){
                if (i < (objectNames.length - 1)){
                   objectWhere.append(" objectName = '"+objectNames[i]+"' or ");
                }else{
                   objectWhere.append(" objectName = '"+objectNames[i]+"'  "); 
                }
            }
            
            String sql = new String("select roleUserGroup,privilege from secObjPrivilege where "+ objectWhere.toString() +" order by priority desc");
            //System.out.println("sql for roles: "+sql );
            rs = db.GetSQL(sql);
            Vector roleInObj = new Vector();
            while (rs.next()) {
                prop.setProperty(db.getString(rs,"roleUserGroup"), db.getString(rs,"privilege"));
                roleInObj.add(db.getString(rs,"roleUserGroup"));
            }
            ret.add(prop);
            ret.add(roleInObj);
            //System.out.println(roleInObj);
            rs.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace(System.out);
        }
        return ret;
    }
    /**
     *returns the providers roles as properties object
     */
    private Properties getVecRole(String roleName) {
        Properties prop = new Properties();
        String[] temp = roleName.split("\\,");
        for (int i = 0; i < temp.length; i++) {
            prop.setProperty(temp[i], "1");
        }
        return prop;
    }
    
    private String[] getVecObjectName(String objectName) {
        String[] temp = objectName.split("\\,");
        return temp;
    }
    

    private Vector getVecPrivilege(String privilege) {
        Vector vec = new Vector();
        String[] temp = privilege.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            if ("".equals(temp[i]))
                continue;
            vec.add(temp[i]);
        }
        return vec;
    }

    private boolean checkPrivilege(String objName, String orgCd, String propPrivilege)
    {
    	try {
            com.quatro.service.security.SecurityManager secManager =(com.quatro.service.security.SecurityManager)pageContext.getSession().getAttribute("securitymanager"); 
            if (orgCd == null) orgCd = "";
            String x=secManager.GetAccess(objName, orgCd);
            return x.compareToIgnoreCase(propPrivilege) >= 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return(false);
        }
    }
    
    private boolean checkPrivilege(String roleName, Properties propPrivilege, Vector roleInObj) {
        boolean ret = false;
        Properties propRoleName = getVecRole(roleName);
        for (int i = 0; i < roleInObj.size(); i++) {
            if (!propRoleName.containsKey(roleInObj.get(i)))
                continue;

            String singleRoleName = (String) roleInObj.get(i);
            String strPrivilege = propPrivilege.getProperty(singleRoleName, "");
            Vector vecPrivilName = getVecPrivilege(strPrivilege);

            boolean[] check = { false, false };
            for (int j = 0; j < vecPrivilName.size(); j++) {
                //System.out.println("role: " + singleRoleName + " privilege:" +
                // vecPrivilName.get(j));
                check = checkRights((String) vecPrivilName.get(j), rights);
                //System.out.println("check: " + check);
                if (check[0]) { // get the rights, stop comparing
                    return true;
                }
                if (check[1]) { // get the only rights, stop and return the result
                    return check[0];
                }
            }
        }
        return ret;
    }

    private boolean[] checkRights(String privilege, String rights1) {
        boolean[] ret = { false, false }; // (gotRights, break/continue)
/*
        if ("*".equals(privilege)) {
            ret[0] = true;
        } else if (privilege.equals(rights1.toLowerCase())
                || (privilege.length() > 1 && privilege.startsWith("o") && privilege.substring(1).equals(
                        rights1.toLowerCase()))) {
            ret[0] = true;
            if (privilege.startsWith("o"))
                ret[1] = true; // break
        } else if (privilege.equals("o")) { // for "o"
            ret[0] = false;
            ret[1] = true; // break
        }
*/
        if ("x".equals(privilege)) {
            ret[0] = true;
        } else if (privilege.compareTo(rights1.toLowerCase()) >=0) {
            ret[0] = true;
        }
        return ret;
    }

    public void release() {
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}

}
