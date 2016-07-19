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


package oscar.oscarMessenger.tld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.HibernateException;
import org.oscarehr.common.model.Site;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.service.SiteRoleManager;
import oscar.util.SqlUtils;

public class MsgNewMessageTag extends TagSupport {

    public MsgNewMessageTag()    {
        numNewMessages = 0;
        numNewDemographicMessages = 0;
    }

    public void setProviderNo(String providerNo1)    {
       providerNo = providerNo1;
    }

    public String getProviderNo()    {
        return providerNo;
    }

    public int doStartTag() throws JspException    {
       
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                c = DbConnectionFilter.getThreadLocalDbConnection();
                						//String sql = new String("select count(*) from messagelisttbl where provider_no ='"+ providerNo +"' and status = 'new' ");
                String sql = "select count(*) from messagelisttbl m LEFT JOIN oscarcommlocations o ON m.remoteLocation = o.locationId where m.provider_no = ? and m.status = 'new' and o.current1=1" ;
                String siteStr = "";
                if(org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()){
                	SiteRoleManager siteRoleMgr = new SiteRoleManager();
                	List<Site> sites = siteRoleMgr.getSitesWhichUserCanOnlyAccess(providerNo);
                    if(sites!=null && sites.size()>0)
                    {
                    	for (Site site : sites)
    					{
    						if(siteStr.length()==0)
    							siteStr = "'"+site.getId()+"'";
    						else
    							siteStr = siteStr+",'"+site.getId()+"'";
    					}
                    	sql = "select count(1) "
                    		+" from messagelisttbl m LEFT JOIN oscarcommlocations o "
                    		+" ON m.remoteLocation = o.locationId "
                    		+" left join msgDemoMap dm on m.message = dm.messageID "
                    		+" left join demographicSite ds on dm.demographic_no=ds.demographicId "
                    		+" where m.provider_no = ? and m.status = 'new' and o.current1=1 "
                    		+" and (dm.demographic_no is null or dm.demographic_no = '' or ds.siteId in ("+siteStr+")) ";
                    }
                } 
                ps = c.prepareStatement(sql);
                ps.setString(1,providerNo);
                rs = ps.executeQuery();                
                while (rs.next()) {
                   numNewMessages = (rs.getInt(1));
                }
                
                String sqlCommand="select count(*) from messagelisttbl,msgDemoMap where provider_no =? and status = 'new' and messageID = message" ;
                ps = c.prepareStatement(sqlCommand);
                ps.setString(1,providerNo);
                rs = ps.executeQuery();
                while (rs.next()) {
                	numNewDemographicMessages = (rs.getInt(1));
                }
            }
            catch (SQLException e) {
                throw (new HibernateException(e));
            }
            finally {
                SqlUtils.closeResources(c, ps, rs);
            }
        
        try        {
            JspWriter out = super.pageContext.getOut();
            if(numNewMessages > 0)
                out.print("<span class='tabalert'>");
            else
                out.print("<span>");
        } catch(Exception p) {MiscUtils.getLogger().error("Error",p);
        }
        return(EVAL_BODY_INCLUDE);
    }

    public int doEndTag()        throws JspException    {
     //ronnie 2007-4-26
       try{
          JspWriter out = super.pageContext.getOut();
          if (numNewMessages > 0)
              out.print("<sup>"+numNewDemographicMessages+"/"+numNewMessages+"</sup></span>  ");
          else
              out.print("</span>  ");
       }catch(Exception p) {MiscUtils.getLogger().error("Error",p);
       }
       return EVAL_PAGE;
    }

    private String providerNo;
    private int numNewMessages;
    private int numNewDemographicMessages;
}
