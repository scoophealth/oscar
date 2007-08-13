package org.caisi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.caisi.model.RedirectLinkTracking;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import oscar.util.SqlUtils;

public class RedirectLinkTrackingDao {

	private SessionFactory sessionFactory = null;

	public SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	public void addInstanceOfRedirect(Date date, int providerId, int redirectLinkId) {

		Session session = sessionFactory.openSession();
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = session.connection();
			ps = c.prepareStatement("insert into RedirectLinkTracking values (?,?,?)");
			ps.setTimestamp(1, new Timestamp(date.getTime()));
			ps.setInt(2, providerId);
			ps.setInt(3, redirectLinkId);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, null);
			session.close();
		}
	}
	
	/**
	 * delete any row with date older thant the given date.
	 */
	public void deleteOldEntries(Date date)
	{
		Session session = sessionFactory.openSession();
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = session.connection();
			ps = c.prepareStatement("delete from RedirectLinkTracking where date<?");
			ps.setTimestamp(1, new Timestamp(date.getTime()));
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, null);
			session.close();
		}		
	}
	
    public List<RedirectLinkTracking> findByRedirectLinkId(int redirectLinkId) {
		Session session = sessionFactory.openSession();
		ArrayList<RedirectLinkTracking> result=new ArrayList<RedirectLinkTracking>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			c = session.connection();
			ps = c.prepareStatement("select * from RedirectLinkTracking where redirectLinkId=?");
			ps.setInt(1, redirectLinkId);
			rs=ps.executeQuery();
			
			RedirectLinkTracking temp=null;
			while (rs.next())
			{
				temp=new RedirectLinkTracking();
				temp.date=rs.getTimestamp("date");
				temp.providerNo=rs.getInt("provider_no");
				result.add(temp);
			}
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, rs);
			session.close();
		}		
		
		return(result);
	}
    
    /**
     * This method is used to count the providers
     * who have or have not used the link. 
     * @param have should be true for providers who have used the link or false for providers who have not used the link
     */
    public int countProvidersWhoHaveUsedLink(boolean have, int redirectLinkId) {
		Session session = sessionFactory.openSession();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			c = session.connection();
			ps = c.prepareStatement("select count(provider_no) from provider where provider_no "+(have?"":"not")+" in (select provider_no from RedirectLinkTracking where redirectLinkId=?)");
			ps.setInt(1, redirectLinkId);
			rs=ps.executeQuery();

			rs.next();
			
			return(rs.getInt(1));
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, rs);
			session.close();
		}		
	}

    /**
     * This method is used to count the number of times this link was used.
     */
    public int countRedirects(int redirectLinkId) {
		Session session = sessionFactory.openSession();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			c = session.connection();
			ps = c.prepareStatement("select count(*) from RedirectLinkTracking where redirectLinkId=?");
			ps.setInt(1, redirectLinkId);
			rs=ps.executeQuery();

			rs.next();
			
			return(rs.getInt(1));
		}
		catch (SQLException e) {
			throw (new HibernateException(e));
		}
		finally {
			// don't close hibernate connections.
			SqlUtils.closeResources(null, ps, rs);
			session.close();
		}		
	}
}
