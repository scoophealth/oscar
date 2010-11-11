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

 * McMaster University

 * Hamilton

 * Ontario, Canada

 */

package oscar.util;

import java.util.Properties;
import java.util.Vector;

import javax.servlet.jsp.PageContext;

import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarDB.DBHandler;

public class OscarRoleObjectPrivilege {
	
	private static PageContext pageContext;
	private static String rights = "r";
	
	public static Vector getPrivilegeProp(String objName) {
        Vector ret = new Vector();
        Properties prop = new Properties();
        try {
            
            java.sql.ResultSet rs;
            String [] objectNames  = getVecObjectName(objName);
            StringBuilder objectWhere = new StringBuilder();
            for (int i = 0; i < objectNames.length; i++){
                if (i < (objectNames.length - 1)){
                   objectWhere.append(" objectName = '"+objectNames[i]+"' or ");
                }else{
                   objectWhere.append(" objectName = '"+objectNames[i]+"'  "); 
                }
            }
            
            String sql = new String("select roleUserGroup,privilege from secObjPrivilege where "+ objectWhere.toString() +" order by priority desc");

            rs = DBHandler.GetSQL(sql);
            Vector roleInObj = new Vector();
            while (rs.next()) {
                prop.setProperty(oscar.Misc.getString(rs, "roleUserGroup"), oscar.Misc.getString(rs, "privilege"));
                roleInObj.add(oscar.Misc.getString(rs, "roleUserGroup"));
            }
            ret.add(prop);
            ret.add(roleInObj);

            rs.close();
        } catch (java.sql.SQLException e) {
           MiscUtils.getLogger().error("Error", e);
        }
        return ret;
    }
    /**
     *returns the providers roles as properties object
     */
    private static Properties getVecRole(String roleName) {
        Properties prop = new Properties();
        String[] temp = roleName.split("\\,");
        for (int i = 0; i < temp.length; i++) {
            prop.setProperty(temp[i], "1");
        }
        return prop;
    }
    
    private static String[] getVecObjectName(String objectName) {
        String[] temp = objectName.split("\\,");
        return temp;
    }
    

    private static Vector getVecPrivilege(String privilege) {
        Vector vec = new Vector();
        String[] temp = privilege.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            if ("".equals(temp[i]))
                continue;
            vec.add(temp[i]);
        }
        return vec;
    }

    public static boolean checkPrivilege(String objName, String orgCd, String propPrivilege)
    {
    	try {
            com.quatro.service.security.SecurityManager secManager =(com.quatro.service.security.SecurityManager)pageContext.getSession().getAttribute("securitymanager"); 
            if (orgCd == null) orgCd = "";
            String x=secManager.GetAccess(objName, orgCd);
            return x.compareToIgnoreCase(propPrivilege) >= 0;
        }
        catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return(false);
        }
    }
    public static boolean checkPrivilege(String roleName, Properties propPrivilege, Vector roleInObj) {
        return checkPrivilege( roleName,  propPrivilege,  roleInObj,rights);
    }
    public static boolean checkPrivilege(String roleName, Properties propPrivilege, Vector roleInObj,String rightCustom) {
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
                check = checkRights((String) vecPrivilName.get(j), rightCustom);

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

    private static boolean[] checkRights(String privilege, String rights1) {
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

    public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}
}
