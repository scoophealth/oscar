/*
* Copyright (c) 2007-2008. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.oscarehr.common.model.RedirectLinkTracking;
import org.oscarehr.util.DbConnectionFilter;

import oscar.util.SqlUtils;

public class RedirectLinkTrackingDao {

    public void addInstanceOfRedirect(Date date, String providerId, int redirectLinkId) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("insert into RedirectLinkTracking values (?,?,?)");
            ps.setTimestamp(1, new Timestamp(date.getTime()));
            ps.setString(2, providerId);
            ps.setInt(3, redirectLinkId);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, null);
        }
    }

    /**
     * delete any row with date older than the given date.
     */
    public void deleteOldEntries(Date date) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("delete from RedirectLinkTracking where redirectDate<?");
            ps.setTimestamp(1, new Timestamp(date.getTime()));
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, null);
        }
    }

    public List<RedirectLinkTracking> findByRedirectLinkId(int redirectLinkId) {
        ArrayList<RedirectLinkTracking> result = new ArrayList<RedirectLinkTracking>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select * from RedirectLinkTracking where redirectLinkId=?");
            ps.setInt(1, redirectLinkId);
            rs = ps.executeQuery();

            RedirectLinkTracking temp = null;
            while (rs.next()) {
                temp = new RedirectLinkTracking();
                temp.date = rs.getTimestamp("redirectDate");
                temp.providerNo = rs.getString("provider_no");
                result.add(temp);
            }
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }

        return(result);
    }

    /**
     * This method is used to count the providers
     * who have or have not used the link. 
     * @param have should be true for providers who have used the link or false for providers who have not used the link
     */
    public int countProvidersWhoHaveUsedLink(boolean have, int redirectLinkId) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(provider_no) from provider where provider_no " + (have?"":"not") + " in (select provider_no from RedirectLinkTracking where redirectLinkId=?)");
            ps.setInt(1, redirectLinkId);
            rs = ps.executeQuery();

            rs.next();

            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }

    /**
     * This method is used to count the number of times this link was used.
     */
    public int countRedirects(int redirectLinkId) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DbConnectionFilter.getThreadLocalDbConnection();
            ps = c.prepareStatement("select count(*) from RedirectLinkTracking where redirectLinkId=?");
            ps.setInt(1, redirectLinkId);
            rs = ps.executeQuery();

            rs.next();

            return(rs.getInt(1));
        }
        catch (SQLException e) {
            throw (new HibernateException(e));
        }
        finally {
            SqlUtils.closeResources(c, ps, rs);
        }
    }
}
